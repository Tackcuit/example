package com.qf.websocket.server;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
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
@ServerEndpoint("/websocket/{orderid}")
//这个标记是用来标记请求的地址的
public class WebsocketServer {
	//创建长链接的类
	//由于标记长连接的唯一标识
	private String orderid;
	//获取长连接
	private Session session;

	//要将所有创建的连接保存起来,所以说需要一个hashmap
	//保证线程安全
	private static Map<String, Session> client = new ConcurrentHashMap<>();

	public WebsocketServer() {
		System.out.println("构造方法执行执行者是" + this.hashCode());
	}

	//有多个注解对应的方法都进行写
	@OnOpen
	public void onOpen(@PathParam("orderid") String orderid, Session session) {
		this.orderid = orderid;
		System.out.println("orderid"+orderid);
		this.session = session;
		System.out.println("session"+session);
		//将这个数据存入到map中
		client.put(orderid, session);
		System.out.println(client.toString());
	}

	@OnError
	//因为是error方法，所以一定会传个异常过来
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
		if (session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnClose
	public void onClose(Session session) {
		//这个时候将一个session从map中移除
		client.remove("orderid");
		if (session.isOpen()) {
			try {
				session.close();
				//一定不要忘记关闭
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		//这个时候解析收到的message，是json格式的，所以对json进行解析
		JSONObject jsonObject = JSONObject.parseObject(message);
		String toName = jsonObject.getString("toName");
		System.out.println("toName:"+toName);
		String content = jsonObject.getString("content");
		System.out.println("content:"+content);
		sendMessage(toName, "来自"+orderid+"的信息，内容是："+content);

	}

	public void sendMessage(String toName, String content) {
		//从这个toName中获取到当前注册的是那个session
		Session tosession = client.get(toName);
		if (tosession != null) {
			tosession.getAsyncRemote().sendText(content);
			return;
		}
		session.getAsyncRemote().sendText("对方不在线，请稍后再试");
		//不要忘记关闭

	}
}
