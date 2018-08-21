package com.qf.liuzhongxu.p2pproject.commons.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.*;

@Component
public class RedisClient {
    @Resource
    private JedisClientPool jedisClientPool;



    /*
    private static JedisPool pool;
    private static String redisServerIp = "192.168.1.128";*/

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     *
     */
    /*private static void createJedisPool() {

		// 建立连接池配置参数
		JedisPoolConfig config = new JedisPoolConfig();

		// 设置最大连接数
		//config.setMaxActive(1000);
		config.setMaxTotal(1000);

		// 设置最大阻塞时间，记住是毫秒数milliseconds
		//config.setMaxWait(1000);

		// 设置空间连接
		config.setMaxIdle(10);

		// 创建连接池
		pool = new JedisPool(config, redisServerIp, 6379);

	}

	*//**
     * 在多线程环境同步初始化
     *//*
    private static synchronized void poolInit() {
		if (pool == null)
			createJedisPool();
	}*/

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    private Jedis getJedis() {
/*
        if (pool == null)
			poolInit();
		return pool.getResource();*/
        return jedisClientPool.getJedis();
    }

    /**
     * 归还一个连接
     *
     * @param jedis
     */
    private void returnRes(Jedis jedis) {
        // pool.returnResource(jedis);
        jedisClientPool.close(jedis);
    }

    void set(String sessionId, Session session) {
        jedis = getJedis();
        String serialize = serialize(session);
        jedis.set(sessionId, serialize);
        returnRes(jedis);
    }

    void replace(String sessionId, Session session) {
        jedis = getJedis();
        String serialize = serialize(session);
        jedis.set(sessionId, serialize);
        returnRes(jedis);
    }

    Jedis jedis = null;

    void delete(String sessionId) {
        jedis = getJedis();
        jedis.del(sessionId);
        returnRes(jedis);
    }

    Object get(String sessionId) {
        Object obj = null;
        try {
            jedis = getJedis();
            obj = deserialize(jedis.get(sessionId));
        } finally {
            returnRes(jedis);
        }
        return obj;
    }

    /**
     * 反序列化
     *
     * @param str
     * @return
     */
    private static Object deserialize(String str) {
        if (str == null) {
            return null;
        }
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(Base64.decode(str));
            ois = new ObjectInputStream(bis);
            Object o = ois.readObject();
            return o;
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        } finally {
            try {
                if (ois != null) {

                    ois.close();
                }
                if (bis != null) {

                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    private static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            String encode = Base64.encodeToString(bos.toByteArray());
            return encode;
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        } finally {
            try {
                if (oos != null) {

                    oos.close();
                }
                if (bos != null) {

                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
