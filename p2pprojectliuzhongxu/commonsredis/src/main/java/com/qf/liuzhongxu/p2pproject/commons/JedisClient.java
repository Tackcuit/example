package com.qf.liuzhongxu.p2pproject.commons;

import com.qf.liuzhongxu.p2pproject.commons.anno.SkipRedis;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
public class JedisClient implements JedisInterface{
	@Autowired
	private JedisPool jedisPool;

	@Override
	public Jedis getJedis(){
		Jedis jedis = jedisPool.getResource();
		jedis.auth("redis001");
		//省的每次都要去验证
		return jedis;
		//这样就从连接池中获取到Redis的连接了
	}
	@Override
	public void set(String key, String value, Jedis jedis) {
		//除了在这个地方直接使用getJedis之外，还有一种方法就是直接将所有的jedis传过来
		jedis.set(key,value);
	}

	@Override
	public String get(String key, Jedis jedis) {
		return jedis.get(key);
	}

	@Override
	public void del(String key, Jedis jedis) {
		jedis.del(key);
	}

	@Override
	public void hset(String key, Object o, Jedis jedis) throws Exception {
		Class<?> clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			//先不要着急获取变量名，先查看有没有注解
			SkipRedis annotation = field.getAnnotation(SkipRedis.class);
			if (annotation != null){
				continue;
			}
			String name = field.getName();
//			Object o1 = field.get(o);为什么要填o，是因为要获取变量的时候需要获取变量的对象
			PropertyDescriptor descriptor = new PropertyDescriptor(name,clazz);
			//属性描述，这个属性描述需要属性名字和对象
			//这个时候要进行非空判断，要是属性为空就不调用了
			if (descriptor != null) {
				Method readMethod = descriptor.getReadMethod();
				Object invoke = readMethod.invoke(o);
				if (invoke != null) {//因为有属性没有，只能这么验证
					hset(key,name,invoke.toString(),jedis);
				}
			}

		}
	}

	@Override
	public void hset(String key, String filedName, String value, Jedis jedis) {
		jedis.hset(key,filedName,value);
	}

	@Override
	public String hGet(String key, String filed, Jedis jedis) {

		return jedis.hget(key, filed);
	}

	@Override
	public Long incr(String everyCode, Jedis jedis) {
		Long incr = jedis.incr(everyCode);
		return incr;
	}
}
