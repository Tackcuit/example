package com.qf.liuzhongxu.p2pproject.user.service.impl;

import com.qf.liuzhongxu.p2pproject.commons.JedisInterface;
import com.qf.liuzhongxu.p2pproject.user.mapper.UserMapper;
import com.qf.liuzhongxu.p2pproject.user.pojo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;

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
@Component
public class InitUserType {
	//在这里进行数据预热，可以利用spring的创建对象时候的初始化功能，也可以使用监听器，然后手动用jdbc查找
	@Autowired
	private JedisInterface jedisInterface;
	@Autowired
	private UserMapper userMapper;

	public void InitUserType() {
		//在配置文件中指定初始化方法
		List<UserType> userTypes = userMapper.selectAllUserType();
		Jedis jedis = jedisInterface.getJedis();
		try {
			for (UserType userType : userTypes) {
				jedisInterface.hset(userType.getUserTypeId(),userType,jedis);
			}
		} catch (Exception e) {
			System.out.println("数据预热失败");
			e.printStackTrace();
		}
	}
}
