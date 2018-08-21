package com.qf.liuzhongxu.p2pproject.user.service.impl;

import com.qf.liuzhongxu.p2pproject.commons.ResultBean;
import com.qf.liuzhongxu.p2pproject.user.mapper.UserMapper;
import com.qf.liuzhongxu.p2pproject.user.pojo.User;
import com.qf.liuzhongxu.p2pproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public void soutText() {
		System.out.println("服务层正常运行，而且被调用");

	}

	@Override
	public void insertUser(User user) throws Exception {
		userMapper.insertUser(user);
	}

	@Override
	public ResultBean findUserByUsernameAndPassword(String userName, String password) {
		User user = userMapper.findUserByUsernameAndPassword(userName, password);

		return ResultBean.setOJBK(user);
	}
}
