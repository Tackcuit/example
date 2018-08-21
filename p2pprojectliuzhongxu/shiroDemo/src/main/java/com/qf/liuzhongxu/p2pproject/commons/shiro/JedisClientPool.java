package com.qf.liuzhongxu.p2pproject.commons.shiro;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component("jedisClientPool")
public class JedisClientPool implements  JedisClient{
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private SimpleDateFormat simpleDateFormat;

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public Jedis getJedis() {
		Jedis resource = jedisPool.getResource();
//		resource.auth("redis001");
		return resource;
	}

	@Override
	public void set(String key, String value) {
	 Jedis jedis = jedisPool.getResource();
	 set(key,value,jedis);
	 jedis.close();
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String string = get(key,jedis);
		jedis.close();
		return string;
	}

	@Override
	public void expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		expire(key,seconds,jedis);
		jedis.close();
	}

	@Override
	public void del(String key) {
		Jedis jedis = jedisPool.getResource();
		del(key,jedis);
		jedis.close();
	}

	@Override
	public void hSet(Object object, String keyName) throws Exception{

		Jedis jedis = jedisPool.getResource();
		hSet(object,keyName,jedis);
		jedis.close();
	}

	@Override
	public void set(String key, String value, Jedis jedis) {
		jedis.set(key, value);
	}

	@Override
	public String get(String key, Jedis jedis) {
		return jedis.get(key);
	}

	@Override
	public void expire(String key, int seconds, Jedis jedis) {
		jedis.expire(key, seconds);
	}

	@Override
	public void del(String key, Jedis jedis) {
		jedis.del(key);
	}

	@Override
	public void hSet(Object object, String keyName, Jedis jedis) throws Exception {
		Class clazz = object.getClass();//反射获取类

		String hkey = (String) getFielValue(keyName, clazz, object);
		if (hkey == null) {
			throw new MyException("keyName错误或者对应值空");
		}
		//大懒指使小懒
		//获取所有私有变量
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			//获取每个属性的 name 和调用 get 方法获取值,然后 name 作为 hash 的小 key
			Object fieldValue = getFielValue(field.getName(), clazz, object);
			//如果是日期类型,则进行转换
			if (fieldValue instanceof Date) {
				jedis.hset(hkey, field.getName(), simpleDateFormat.format(fieldValue));
			} else {
				jedis.hset(hkey, field.getName(), fieldValue.toString());
			}
		}
	}

	@Override
	public void close(Jedis jedis) {
		jedis.close();
	}

	/**
	 * 根据属性名获取对应的值
	 * @param name
	 * @param clazz
	 * @return
	 */
	private Object getFielValue(String name, Class clazz,Object object) throws Exception {
		//获取 keyName 对应的值
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, clazz);
		Method writeMethod = propertyDescriptor.getWriteMethod();
		Object value = null;
		if (writeMethod != null) {
			value =writeMethod.invoke(object);
		}
		return value;
	}
}
