package seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import seckill.dao.SeckillDao;
import seckill.dao.SuccessSeckilledDao;
import seckill.dao.cache.RedisDao;
import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.entity.Seckill;
import seckill.entity.SuccessSeckilled;
import seckill.enums.SeckillStatEnum;
import seckill.exception.RepeatKillException;
import seckill.exception.SeckillCloseException;
import seckill.exception.SeckillException;
import seckill.service.SeckillService;

@Service("seckillService")
public class SeckillServiceImpl implements SeckillService{

	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Resource
	private SeckillDao seckillDao;
	
	@Resource
	private SuccessSeckilledDao successSeckilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	//随便写
	private final String slat = "dqoidjo3ue$#@FTYF**^%&*TGOI(**&(&((H&*^IKHK";
	
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		
		//通过 redis缓存起来，解放服务器压力
		//缓存优化，少执行一下去数据库查询 秒杀产品
		//超时的基础上维持一致性：缓存和数据库的一致性
		/**
		 * get from cache
		 * if null
		 * get from db
		 * then 
		 * put cache
		 */
		//1.访问redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill == null) {
			//2.如果缓存中不存在，那么访问数据库
			seckill = seckillDao.queryById(seckillId);
			
			if(seckill == null) {
				//数据库中不存在
				return new Exposer(false,seckillId);
				
			} else {
				//如果数据库中存在，那么取得对象后，放入缓存
				redisDao.putSeckill(seckill);
			}
		}
		
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date now = new Date();
		
		if(now.before(startTime) || now.after(endTime)) {
			return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
		}
		
		String md5 = getMD5(seckillId);
		
		return new Exposer(true, md5, seckillId);
	}

	/**
	 * 使用注解控制事务方法的优点
     * 1：开发团队达成一致，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短，不要穿插其他的网络操作，RPC/HTTP请求
     * 或者剥离到事务方法外部
     * 3：不是所有的方法都需要事务，如：只有一条修改操作，只读操作不需要事务。
     * 两条以上需要同时去完成添加事务
	 */
	@Transactional
	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("Seckill data rewrite");
		}
		
		/**
		 * 优化第一种方法：
		 * 把 update库存和 insert购买记录 顺序掉换
		 * 因为 update会持有行级锁，先insert 减少 update 的持有行级锁
		 */
		
		//减库存
		try {
			int updateCount = seckillDao.reduceNumber(seckillId, new Date());
			
			//记录购买行为
			if(updateCount == 0) {
				throw new SeckillCloseException("Seckill is closed");
			}else {
				int insertCount = successSeckilledDao.insertSuccessKilled(seckillId, userPhone);
				if(insertCount == 0) {
					throw new RepeatKillException("RepeatKillException");
				} else {
					SuccessSeckilled successSeckilled = successSeckilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successSeckilled);
				}
			}
		} catch(SeckillCloseException e) {
			throw e;
		} catch(RepeatKillException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			//把编译期异常转换为 运行期异常
			throw new SeckillException("SeckillException");
		}
		
	}
	
	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId,
			long userPhone, String md5) {
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		//最后map里面的result 执行完存储过程后会被赋值
		try{
			//调用存储过程
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result",-2);
			if(result == 1) {
				SuccessSeckilled sk = successSeckilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,sk);
			}else {
				return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
			}
		}catch (Exception e){
			return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ORROR);
		}
		
	}
	
	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		
		return md5;
	}

}
