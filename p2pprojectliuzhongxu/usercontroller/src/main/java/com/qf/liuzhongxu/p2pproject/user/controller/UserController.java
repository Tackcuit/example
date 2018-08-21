package com.qf.liuzhongxu.p2pproject.user.controller;

import com.qf.liuzhongxu.p2pproject.commons.CodeUtil;
import com.qf.liuzhongxu.p2pproject.commons.JedisClient;
import com.qf.liuzhongxu.p2pproject.commons.ResultBean;
import com.qf.liuzhongxu.p2pproject.user.pojo.User;
import com.qf.liuzhongxu.p2pproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.lang.model.element.VariableElement;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;

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
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClient jedisClient;

	@RequestMapping("/register")
	@ResponseBody
	public ResultBean register(User user, HttpServletRequest request, String vcode, String validcode, String usertypeid1) {
		String value = null;
		Jedis jedis = null;
		//注册首先进行数据合法性的校验
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		if (cookies == null) {
			return ResultBean.setError(194, "图片验证码失效", null);
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase("piccode")) {
				value = cookie.getValue();
			}
		}
		jedis = jedisClient.getJedis();
		String s = jedis.get(value + "lzx");
		System.out.println("--------------------");
		System.out.println("phone = " + user.getPhoneNum());
		System.out.println(vcode);
		System.out.println(s);
		String s1 = jedis.get(user.getPhoneNum() + "lzx");
		System.out.println("s = " + s1);
		System.out.println("s += " + validcode);
		System.out.println("--------------------");

		if (!s.equalsIgnoreCase(vcode)) {
			return ResultBean.setError(12, "图片验证码输入错误", null);
		}
		//这个时候这么获取欧诺个户的手机号？不要被思维定式了，直接去从user中就能获取到
		if (!jedis.get(user.getPhoneNum() + "lzx").equalsIgnoreCase(validcode)) {
			return ResultBean.setError(10, "手机验证码输入错误", null);
		}
		user.setUsertypeid("1".equalsIgnoreCase(usertypeid1) ? "3139c895-83e9-11e8-8c6b-001c427107e9" : "66073ebb-83ee-11e8-8c6b-001c427107e9");
		user.setCreditlevelid("a7336944-84a2-11e8-90fa-201a06be2f93");
		user.setCreditlevelname("F");
		user.setUserstatus((short) 1);
		try {
			userService.insertUser(user);
			return ResultBean.setOJBK(user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();//关闭链接
			}
		}
		return ResultBean.setError(null);
	}

	@RequestMapping("/login")
	@ResponseBody
	public ResultBean login(String userName, String password, HttpServletResponse response) {
		System.out.println("123123");
		//登录之后一定要将一部分数据放在redis中
		ResultBean resultBean = userService.findUserByUsernameAndPassword(userName, password);
		User user = (User) resultBean.getData();
		//这个时候可以确保我拿到的一定是User对象。所以说就直接进行强转
		//拿到user之后要进行个推荐码的自增（这个过程放在redis中去做），然后进行分辨一些数值是不是应该存放进去
		Jedis jedis = jedisClient.getJedis();
		//这个时候因为userid是不一样的，所以说往redis中存的时候key就用userid就行了
		if (user != null) {
			try {
				System.out.println(user.getUserId());
				System.out.println(user);
				jedisClient.hset(user.getUserId(),user,jedis);
//				user.setPaypassword(user.getPaypassword()==null?"0":"1");//通过修改缓存中用户密码的值来确定用户是否设置过支付密码 0代表没有设置,1代表设置了
				//注意要设置过期时间
				//为了以后能够直接去获取，所有往cookie中存一份
				Cookie cookie = new Cookie("user",user.getUserId());
				cookie.setPath("/");
				response.addCookie(cookie);

				System.out.println("****************");
				return ResultBean.setOJBK(user);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return null;

	}


	@RequestMapping("/uesrInfo")
	@ResponseBody
	public ResultBean userInfo(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		Jedis jedis = jedisClient.getJedis();

		if (cookies != null){
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase("user")) {
					String value = cookie.getValue();
					//这个会后取出的就是user的id

				}
			}
		}
		return null;
	}



}