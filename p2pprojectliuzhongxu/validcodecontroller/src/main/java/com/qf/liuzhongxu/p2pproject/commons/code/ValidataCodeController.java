package com.qf.liuzhongxu.p2pproject.commons.code;

import cn.dsna.util.images.ValidateCode;
import com.qf.liuzhongxu.p2pproject.commons.JedisClient;
import com.qf.liuzhongxu.p2pproject.commons.JedisInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

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
@RequestMapping("/code")
@Component//不要忘记扫描
public class ValidataCodeController {
	@Autowired
	private JedisInterface jedisInterface;

	@RequestMapping("/phonenum")
	@ResponseBody
	public String createPhoneCode(String phonenum, String callback) {
		System.out.println("1111111111111");
		ValidateCode validateCode = new ValidateCode(260, 60, 4, 10);
		String code = validateCode.getCode();
		System.out.println(code);
		//到现在要生成redis中的缓存数据
		Jedis jedis = jedisInterface.getJedis();
		jedisInterface.set(phonenum + "lzx", code, jedis);
		//这样就可放到redis中
		//还需要设置失效时间，因为手机验证码是由有效期的，先设置个180秒
		jedis.expire(phonenum + "lzx", 180);
		//之后需要的时候直接去redis中去获取
		jedis.close();
		//这个是jquery中一个很神奇的地方，所以说要进行深度的学习
		code = callback == null ? code : callback + "('" + code + "')";
		return code;
	}

	@RequestMapping("/piccode")
	@ResponseBody
	public void createPicCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
		//这个的实现要放到cookie中！最关键的一步,因为访问到的图片验证码是在别的地方生成的，所以要跨域
		//最好的解决办法就是放到cookie中，在service层进行校验
		System.out.println("2222222222222");
		ValidateCode validateCode = new ValidateCode(260, 130, 4, 150);
		Cookie cookie = null;
		String uuid = null;
		Cookie[] cookies = request.getCookies();
		System.out.println(cookies);
		if (cookies != null) {

			for (Cookie cookie1 : cookies) {
				if (cookie1 != null) {
					//有cookie，那就看看cookie中有没有pic校验码
					if (cookie1.getName().equalsIgnoreCase("piccode")) {
						cookie = cookie1;
						uuid = cookie.getValue();
					}
				}

			}
		}
		//如果么有cookie就放一个cookie过去
		if (cookie == null) {
			uuid = UUID.randomUUID().toString();
			cookie = new Cookie("piccode", uuid);
			cookie.setPath("/");
		}
		//这个时候要去redis中存储一下
		Jedis jedis = jedisInterface.getJedis();
		jedisInterface.set(uuid + "lzx", validateCode.getCode(), jedis);
		response.addCookie(cookie);
		BufferedImage brim = validateCode.getBuffImg();
		//这个创建图片一定要注意
		ImageIO.write(brim, "jpg", response.getOutputStream());
//		validateCode.write(response.getOutputStream());
//		这个是validatacode的输出方式
		jedis.close();
	}
}
