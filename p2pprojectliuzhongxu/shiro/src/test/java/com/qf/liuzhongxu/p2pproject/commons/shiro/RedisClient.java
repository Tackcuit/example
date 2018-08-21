package com.qf.liuzhongxu.p2pproject.commons.shiro;

import com.qf.liuzhongxu.p2pproject.commons.JedisInterface;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.*;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼           BUG辟易
//
//                             佛曰:
//
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
@Component
public class RedisClient {
	//用jedis的连接池去创建对象
	@Resource
	private JedisInterface jedisInterface;

	public Jedis getJedis() {
		return jedisInterface.getJedis();
	}

	public void set(String sessionId, Session session) {
		//这个时候要把序列化好的session对象放到redis中
		String serialize = serialize(session);
		getJedis().set(sessionId, serialize);
	}

	public Session get(String sessionId) {
		String s = getJedis().get(sessionId);
		return (Session) deserialize(s);
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
