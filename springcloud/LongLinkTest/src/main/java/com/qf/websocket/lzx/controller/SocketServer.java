package com.qf.websocket.lzx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class SocketServer {
	//怎么创建长连接
	//为每一个session都创建一个名字，然后应该有一个长连接对象
	private String name;
	private Session session;

	//创建一个map来城防所有的客户端
	private static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();

	public SocketServer() {
		System.out.println("创建了一个session对象，hash为" + this.hashCode());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	//怎么才能监听到session被创建?创建的时候不用区分怎么回事。但是获取的时候是要从map中去获取的
	@OnOpen
	public void onOpen(String name, Session session) {
		this.name = name;
		this.session = session;
		clients.put(name, session);
	}

	@OnClose
	public void onClose(Session session) {
		//不能只是关闭session，一定要把map中的删除了
		clients.remove(name);
		if (session != null) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		//一定要求传过来json数据，然后解析json数据
		JSONObject jsonObject = JSON.parseObject(message);
		String toName = jsonObject.getString("toName");
		String content = jsonObject.getString("content");
		//然后写一个发送数据的方法
		send(content, toName);
	}

	private void send(String message, String toName) {
		//要发送的人的数据是要被客户端取出来的
		Session session = clients.get(toName);
		if (session != null) {
			RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
			asyncRemote.sendText(message);
			return;
		}
	}

	@OnError
	public void onError(Throwable throwable){
		throwable.printStackTrace();
		//除了异常为了安全就要把session移除
		if (session != null) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			clients.remove(name);
		}
	}

}