package com.qf.weixin.servlet;

import com.qf.weixin.utils.PayCommonUtil;
import com.qf.weixin.utils.ZxingUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
//@WebServlet("/paymentservlet")
//这个servlet用来进行请求的处理
public class PaymentServlet extends HttpServlet {
	private Random random = new Random();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//在这里处理真正的业务
		//先要设置一下乱码
		request.setCharacterEncoding("utf-8");
		//拿到要买的商品之后就要进行准备封装微信所需要的接口数据了，应该是过去商品id的，然后去数据库查询，或者是从redis中获取
		String body = request.getParameter("body");
		//这个地方需要获取价格，现在写死了，价格以分为单位
		String price = "1";
		//模拟订单id
		int r = random.nextInt(1000000000);
		try {
			//获取到请求地址,用于后续生成二维码
			String s = PayCommonUtil.weixin_pay(price, body, r + "");
			//根据请求地址生成二维码，然后放到request中
			BufferedImage image = ZxingUtil.createImage(s, 300, 300);
			//这个是方便在imageservlet中进行图片的输出
			//这个session是jsp的一个默认容器，不放这里边是在jsp页面中获取不到的
			request.getSession().setAttribute("image",image);
			request.getSession().setAttribute("orderid",r);
			//最后不要忘记跳转支付页面
			response.sendRedirect("/weixinzhifu/pay.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
