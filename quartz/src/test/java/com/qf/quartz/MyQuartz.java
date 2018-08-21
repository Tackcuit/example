package com.qf.quartz;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;

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
public class MyQuartz {
	@Test
	//在进行test测试的时候一定要把线程阻塞住才行，要不然就直接结束了
	public void test01() throws Exception {
		//第一步配置任务详情
		//newJob的时候要吧job类传过去，不能瞎写
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).usingJobData("name","123").withIdentity("job","group1").build();
//		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job1","laowang").usingJobData("name","abc").build();
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		//第二部配置表达式


		//第三步配置调度器

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("t1","group1").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(10)).build();
//		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("t1","laowang").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(10)).build();

		scheduler.scheduleJob(jobDetail,trigger);

		scheduler.start();

		Thread.sleep(10000000);
	}

	@Test
	public void test02() throws Exception{
		Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();

		//withIntervalInSeconds重复频率为每秒
		//repeatForever重复无限次数
		//Trigger trigger = TriggerBuilder.newTrigger().withIdentity("t1","laowang").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("t1","laowang").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(10)).build();
		//树莓派
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job1","laowang").usingJobData("name","abc").build();
		scheduler.scheduleJob(jobDetail,trigger);
		scheduler.start();
	}

	@Test
	public void test03() throws Exception {
		//配置任务详情
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("lzx","group1").build();
		//配置表达式
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("t1","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/1 51-52 * * * ? *")).build();

		//配置调度器
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		scheduler.scheduleJob(jobDetail,cronTrigger);

		scheduler.start();

		Thread.sleep(10000000);
	}

	@Test
	public void test04() throws Exception {
		//工作详情
		JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("lzx","group1").usingJobData("name","lzx").build();
		//表达式
		CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/1 58-59 * * * ? *")).withIdentity("lzx","group1").build();

		//调度器
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		scheduler.scheduleJob(jobDetail,trigger);

		scheduler.start();

		//因为实在测试方法中执行的，所以最好将线程阻塞掉，保证执行
		Thread.sleep(10000000);
	}

	@Test
	public void test05(){

	}

}
