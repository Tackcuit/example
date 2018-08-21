package com.cnxinxinshen;
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

import com.alibaba.fastjson.JSON;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Belfast
 **/
//标志着这个是一个websocket的服务端
@ServerEndpoint("/websocket")
public class WebSocketHandler {
	//spring4之后加入了对于websocket的支持，所以直接用注解去进行操作就行了
	private Session session;
	private Map<String, Session> allClients = new ConcurrentHashMap<String, Session>();
	private String name;
	private static Integer onLineNum = 0;

	public WebSocketHandler() {

	}

	private static synchronized Integer addOnlineNum() {
		return ++onLineNum;
	}

	private static synchronized Integer subOnlineNum() {
		if (onLineNum == 0) {
			return 0;
		} else {
			return --onLineNum;
		}
	}

	@OnOpen
	public void onOpen(@PathParam("name") String name, Session session) {
		//连接之后一定要将客户端存在map中，下次发送的时候要从map中取出来
		this.session = session;
		this.name = name;
		allClients.put(name, session);
		Set<String> names = allClients.keySet();
		for (String cname : names) {
			System.out.println(cname);
		}
	}

	@OnClose
	public void onClose(Session session) {
		allClients.remove(name);//这个时候的name到底是在哪里
		Set<String> names = allClients.keySet();
		for (String cname : names) {
			System.out.println(cname);
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		Map map = JSON.parseObject(message, Map.class);
		String fromName = (String) map.get("fromName");
		String toName = (String) map.get("toName");
		String content = (String) map.get("content");

	}

	private void sendMessage(String message, String toName) {
		Session toSession = allClients.get("toName");
		if (toSession != null) {
			//这个时候说明是接受方在线的
			toSession.getAsyncRemote().sendText(message);
			return;
		}
		if (toSession == null) {
			//存入数据库然后等上线的时候在发送

		}
	}

	@OnError
	public void onError(Session session, Throwable throwable){
		throwable.printStackTrace();
		//出异常要把所有的东西都移除出去
		if (session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		allClients.remove(name);
	}

}