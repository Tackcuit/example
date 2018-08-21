package com.qf.lzx;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;

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
public class TestZookeeper {
	private static final String CONNECTION_ADD = "qianfeng.qfjava.cn:8091";
	private static final int TIME_OUT = 5000;
	static int i = 10;
	//使用zookeeper的临时节点进行通知
	public static void decr(){
		--i;//这个就不能先进行操作再减了，必须先减再操作
		System.out.println(i);
	}

	public static void main(String[] args) throws Exception{
		//这个地方创建的是什么意思？RetryPolicy是重试的，连接失败之后每一秒重连一次，重连10次之后放弃
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,10);
		//这个地方连接zookeeper的方法非常诡异，记住
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECTION_ADD).connectionTimeoutMs(TIME_OUT).retryPolicy(retryPolicy).build();
		curatorFramework.start();
		//要保证线程创建之后不直接运行，需要的类 TODO 这个类是什么意思？
		//TODO 阻塞线程用的吗？
		CountDownLatch countDownLatch = new CountDownLatch(1);
		//这时候可以创建多个线程
		for (int i1 = 0; i1 < 10; i1++) {

			new Thread(){
				@Override
				public void run() {
					//这个时候就在向zookeeper中写一个临时节点，如果临时节点空了，zookeeper会通知所有观察的节点，然后自己去抢
					InterProcessMutex lock =new InterProcessMutex(curatorFramework,"/abc/123");
					//创建多个线程模拟多个客户端访问
					try {
						//保证所有的客户端同时并发访问
						countDownLatch.await();
						//抢锁的操作
						lock.acquire();
						//每个线程都尝试进行减一的操作
						decr();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						//最后不要忘了释放锁
						try {
							lock.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		//在创建线程完毕之后就放弃阻塞线程
		countDownLatch.countDown();
		Thread.sleep(10000000);
	}
}
