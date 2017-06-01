package seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import seckill.entity.Seckill;

public interface SeckillDao {
    
	/**
	 * 减少库存的操作
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	
	/**
	 * 查询需求
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 获得商品列表
	 * @param offset 从哪开始
	 * @param limit 需要多少条
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	
	void killByProcedure(Map<String,Object> map);

}