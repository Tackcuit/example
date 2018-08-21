package com.qf.lzx;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.RedisPipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.Random;

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
public class TestMain1 {
	@Test
	public void addItem() {
		Jedis jedis = new Jedis("10.9.166.125", 8100);
		jedis.set("001good", "2020");
	}


	@Test
	public void testbuy(){
		new Thread(){
			//想要在后边run，那么这个一定得是内名内部类才行
			@Override
			public void run() {
				test00();
			}
		}.start();
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void test00() {
		//应该先创建一个Jedis,交给spring创建，而且要生成工具类
		Jedis jedis = new Jedis("10.9.166.125", 8100);
		//购买的时候应该往redis中放点东西
		String set = jedis.set("item10", new Random().nextInt(10000) + "", "nx", "ex", 10);
		if (set != null) {
			//这个时候只有set不为空才能进行操作，如果为空那么直接返回
			//放进去随便的值的时候要顺便watch一下，如果这个值发生了变化，那么就要放弃操作
			String item10 = jedis.watch("item10");
			Transaction transaction = jedis.multi();
			//这个地方要把所有的数据都先预先放到应该放的地方，然后进行数量减少之类的操作

//				System.out.println("111111111111");
				transaction.decrBy("001good", 100);
				//如果中间没有出问题，就进行提交，如果有问题，就抓着问题然后回滚

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			transaction.exec();
				//购买成功了之后千万不要忘了把设置好的key删除掉
				jedis.del("item10");
				jedis.close();
		}else{
			System.out.println("有人正在购买");
			return;
		}
	}
}
