package com.qf.liuzhongxu.p2pproject.commons.shiro;

import redis.clients.jedis.Jedis;

public interface JedisClient {
	Jedis getJedis();

	void set(String key, String value);
	String get(String key);
	
	void expire(String key, int seconds);//设置有效期
	void del(String key);//删除指定的key

	void hSet(Object object, String keyName) throws Exception;//添加 hash,key 为需要当前对象上作为主键的key的属性名, 实现会将这个属性的值作为 hash 的外部 key

	void set(String key, String value, Jedis jedis);//需要手动关闭 jedis
	String get(String key, Jedis jedis);//需要手动关闭 jedis

	void expire(String key, int seconds, Jedis jedis);//设置有效期,需要手动关闭 jedis
	void del(String key, Jedis jedis);//删除指定的key需要手动关闭 jedis

	void hSet(Object object, String keyName, Jedis jedis) throws Exception;//添加 hash,key 为需要当前对象上作为主键的key的属性名, 实现会将这个属性的值作为 hash 的外部 key,需要手动关闭 jedis

	void close(Jedis jedis);// 关闭 jedis
}
