package com.qf.lzx;

import javafx.scene.control.Tab;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
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
public class TestMain {
	//用于测试redis做分布式锁

	/**
	 * 没有加锁的方法
	 */
	public void goumai() {
		Jedis jedis = new Jedis("10.9.166.125", 8100);
//		jedis.auth("redis001");
		//每次买什么商品，买几件
		jedis.decrBy("item1000lzx", 1000);
	}

	/**
	 * 加分布式锁的方法
	 */
	public void locaBuy() {
		Jedis jedis = new Jedis("10.9.166.125", 8100);
//		jedis.auth("redis001");
		//set方法可以通过这几个数值的设定将设置不变数值的方法变成原子性的
		String set = jedis.set("buy1000lzx", "suibian" + new Random().nextInt(100000), "nx", "ex", 20);
		System.out.println(set);
		if (set != null) {
			//如果set不为空，说明设置已经成功,说明已经拿到锁了
			//还是为了防止出问题，所以说进行一步watch的操作
			jedis.watch("buy1000lzx");
			//开启事务，保证一致性，如果有人卡住了还是能够超时并且将所有的事务都进行回滚
			Transaction transaction = jedis.multi();
			//下面所有的操作都使用事务进行，最后如果自己watch的key没有超时就星星提交，但是可能存在key失效但是提交成功的问题

			/**
			 * 这个时候要注意进行一次判断，防止出现问题
			 * 这个时候在执行之前如果出了问题，所以取出值判断一下，
			 * 如果有就进行更改，如果没有就说明中间还是出现问题了，所以说还是要丢弃
			 */
			Jedis jedis1 = new Jedis("10.9.166.125", 8100);
//			jedis1.auth("redis001");
//			jedis.set("test","123");
			String goumai1000 = jedis1.get("buy1000lzx");
			if (goumai1000 == null) {
				//出现问题，丢弃所做的修改
				transaction.discard();
				//这个时候要解除watch
				jedis.unwatch();
			} else {
				//这个时候说明还是能够修改的
				transaction.decrBy("item1000lzx",100);
				List<Object> exec = transaction.exec();
				System.out.println(exec);
				//这个时候必要移除这个key吗？
				jedis.del("item");
			}
		} else {
			//已经有人设置过redis，而且没有失效，所以说阻止其他人购买
			System.out.println("已经有人在购买了");
		}
	}

	@Test
	public void test1() {
		for (int i = 0; i < 10; i++) {
			//goumai();
			new Thread(){
				@Override
				public void run() {
					locaBuy();
				}
			}.start();
		}
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test02(){
		Jedis jedis = new Jedis("10.9.166.125", 8100);
//		jedis.auth("redis001");
		//set方法可以通过这几个数值的设定将设置不变数值的方法变成原子性的
		String set = jedis.set("buy1000lzx", "suibian" + new Random().nextInt(100000), "nx", "ex", 20);
		System.out.println(set);
		jedis.set("test","001");
	}
}
