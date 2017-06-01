package seckill.dao;

import org.apache.ibatis.annotations.Param;

import seckill.entity.SuccessSeckilled;

public interface SuccessSeckilledDao {

	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return 返回的是影响的行数
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * 根据产品ID来查询记录，并携带秒杀产品对象实体
	 * @param seckillId
	 * @return
	 */
	SuccessSeckilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	
}
