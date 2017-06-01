package seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import seckill.entity.Seckill;

public class RedisDao {

	private final JedisPool jedisPool;
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	public RedisDao(String ip,int port) {
		
		jedisPool = new JedisPool(ip, port);
	}
	
	public Seckill getSeckill(long seckillId) {
		
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				//并没有实现内部序列化操作
				//get的是byte[]数组--》需要反序列化--》Seckill
				//采用自定义的序列化方式
				byte[] bytes = jedis.get(key.getBytes());
				
				//如果缓存中有
				if(bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					
					//Seckill被 反序列化了
					return seckill;
				}
				
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {

		}
		
		return null;
	}
	
	public String putSeckill(Seckill seckill) {
		//把 seckill 序列化成 byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				String result = jedis.setex(key.getBytes(), 3600, bytes);
				return result;
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {

		}
		
		return null;
	}
	
}
