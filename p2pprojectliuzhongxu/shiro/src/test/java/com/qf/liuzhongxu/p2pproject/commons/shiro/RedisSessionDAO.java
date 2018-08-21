package com.qf.liuzhongxu.p2pproject.commons.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

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
public class RedisSessionDAO extends AbstractSessionDAO{
	@Override
	protected Serializable doCreate(Session session) {
		//这个方法在shiro创建session的时候会调用一下
		//这个方法要是自己写的话就比较特殊了，建议看看源码是怎么写的
		Serializable sessionId = generateSessionId(session);
		//进行绑定，关联
		assignSessionId(session,sessionId);
		//关联之后就能用sessionId去找session了
		RedisClient redisClient = new RedisClient();
		redisClient.set(sessionId.toString(),session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable serializable) {
		//这个是读取的方法，所以说在redisClient中对应应该有一个读取的方法，
		// 如果这个方法返回的是空，会自动去创建一个，然后调用都Create方法
		if (serializable == null){
			return null;
		}
		Session session = null;
		RedisClient redisClient = new RedisClient();
		try {
			session = redisClient.get(serializable.toString());
			return session;
		}catch (Exception e){
			e.printStackTrace();
			//捕获到异常就不要进行返回操作了，还是直接再处理层去处理吧
		}
		return session;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {

	}

	@Override
	public void delete(Session session) {

	}

	@Override
	public Collection<Session> getActiveSessions() {
		return null;
	}
}
