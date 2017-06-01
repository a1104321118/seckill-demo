package seckill;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import seckill.dto.Exposer;
import seckill.dto.SeckillExecution;
import seckill.entity.Seckill;
import seckill.service.SeckillService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void getSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		
		System.out.println(seckillList);
	}
	@Test
	public void getById() {
		long id = 1001;
		
		System.out.println(seckillService.getById(id));
	}
	
	@Test
	public void exportSeckillUrl() {
		long id = 1001;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		
		System.out.println(exposer);
	}
	
	@Test
	public void executeSeckill() {
		long id = 1001;
		
		long userPhone = 176945646;
		
		String md5 = "604b2901d8a66d978d61a738f3b7a144";
		
		SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
		
		System.out.println(seckillExecution);
	}
	
	@Test
	public void executeSeckillProcedure() {
		long id = 1001;
		
		long userPhone = 17694456646L;
		
		Exposer exposer = seckillService.exportSeckillUrl(id);
		
		if(exposer.isExposed()) {
			String md5 = exposer.getMd5();
			SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(id, userPhone, md5);
			System.out.println(seckillExecution.getStateInfo());
		}
		
		
		
		
		
		
	}


}
