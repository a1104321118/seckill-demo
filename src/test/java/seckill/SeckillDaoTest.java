package seckill;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import seckill.dao.SeckillDao;
import seckill.dao.SuccessSeckilledDao;
import seckill.entity.Seckill;
import seckill.entity.SuccessSeckilled;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

	@Resource
	private SeckillDao seckillDao;
	
	@Resource
	private SuccessSeckilledDao successSeckilledDao;
	
	@Test
	public void testQueryById() {
		long id = 1001;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill);
	}
	
	@Test
	public void testQueryAll() {
		List<Seckill> seckillList = seckillDao.queryAll(0, 10);
		for (Seckill seckill : seckillList) {
			System.out.println(seckill);
		}
	}
	
	@Test
	public void reduceNumber() {
		Date killTime = new Date();
		long seckillId = 1001;
		
		int i = seckillDao.reduceNumber(seckillId, killTime);
		System.out.println(i);
		
	}
	
	@Test
	public void insertSuccessKilled() {
		long id = 1001;
		long phone = 12321323;
		
		successSeckilledDao.insertSuccessKilled(id, phone);
		
	}
	
	@Test
	public void queryByIdWithSeckill() {
		long id = 1001;
		long phone = 12321323;
		
		SuccessSeckilled successSeckilled= successSeckilledDao.queryByIdWithSeckill(id, phone);
		
		//返回的结果是携带 Seckill 实体对象的。可以通过 get()方法得到
		System.out.println(successSeckilled);
	}

}
