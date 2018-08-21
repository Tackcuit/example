package com.qf.liuzhongxu.p2pproject.commons;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;

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
public class MQ {

	private static String EXCHANGE = "fanoutExchange";
	@Test
	public void sender() throws Exception{
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		//声明交换机，交换机的类型
		channel.exchangeDeclare(EXCHANGE,"fanout",true);
		String message = "啤酒饮料矿泉水,花生瓜子火腿肠,哎,腿让一下,让车过一下";
		//进行发布数据
		channel.basicPublish(EXCHANGE, "",  MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println("哦~~~~:"+message);
		channel.close();
		connection.close();

		//这样就能够拿到连接，这个连接是rabbit提供的连接,但是获取连接致亲爱按需要告诉工厂怎么创建这个连接对象

	}
}
