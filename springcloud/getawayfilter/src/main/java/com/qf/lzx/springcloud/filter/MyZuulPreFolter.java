package com.qf.lzx.springcloud.filter;

import com.netflix.zuul.ZuulFilter;

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
public class MyZuulPreFolter extends ZuulFilter{

	@Override
	/*
	这个是提前进行过滤
	 */
	public String filterType() {
		return "pre";
	}

	/**
	 * 这个返回值标志着加载顺序，数值越小加载越晚
	 * @return
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * 是否使用当前过滤器
	 * @return
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 *过滤器要干什么事情
	 * @return
	 */
	@Override
	public Object run() {
		System.out.println("前置过滤器已经被执行");
		return null;
	}
}
