package com.qf.liuzhongxu.p2pproject.commons.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

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
@Component("sessionDAO")
public class RedisSessionDao extends AbstractSessionDAO {
	@Resource
	private RedisClient sessionCacheClient;

	Logger log= LoggerFactory.getLogger(getClass());
	@Override
	public void update(Session session) throws UnknownSessionException {
		log.info("更新seesion,id=[{}]",session.getId().toString());
		try {
			sessionCacheClient.replace(session.getId().toString(), session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Session session) {
		log.info("删除seesion,id=[{}]",session.getId().toString());
		try {
			sessionCacheClient.delete(session.getId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		log.info("获取存活的session");
		return Collections.emptySet();
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
//		((SerSession)session).setId(sessionId);
		log.info("创建seesion,id=[{}]",session.getId().toString());
		try {
			sessionCacheClient.set(sessionId.toString(),  session);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			return null;
		}
		log.info("获取seesion,id=[{}]",sessionId.toString());
		Session session = null;
		try {
			session = (Session) sessionCacheClient.get(sessionId.toString());
			return  session;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return session;
	}
}
