package com.qf.weixin.servlet;

import com.qf.weixin.utils.PayCommonUtil;
import com.qf.weixin.utils.PayConfigUtil;
import com.qf.weixin.utils.XMLUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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
public class ResultServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//开始处理业务逻辑,这个时候是已经发起支付请求了，微信会返回结果的时候

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public void weixin_notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//因为现在咱们不知道能不能成功，所以说默认是使用支付失败
		String writeContent = "默认支付失败";
		//为什么现在能够从request中获取到微信返回的数据,为什么这个时候就能确定是微信的回调呢？什么时候才会请求这个servlet，谁来请求的？是微信吗
		ServletInputStream in = request.getInputStream();
		StringBuffer sb = new StringBuffer();
		//将结果写入一个文件中，
		String path = request.getServletContext().getRealPath("file");//保存结果文件的位置
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		//创建输出流以备后边写数据使用
		FileOutputStream fileOutputStream = new FileOutputStream(path + "/result.txt", true);
		//创建字符输入流，然后打印结果
		BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
		String s;
		while ((s = bfr.readLine()) != null) {
			sb.append(s);
		}
		//然后将所有的输入流都关上，只留下输出流
		bfr.close();
		in.close();

		//这个时候sb已经有了微信所传回来的所有信息，所以先去解析微信返回的信息
		//微信的返回是xml类型的，所以说用xml解析工具去解析这个玩意
		Map<String, String> map = new HashMap<>();
		map = XMLUtil.doXMLParse(sb.toString());
		//这个map中包含的是微信传回来的信息，但是为什么要放到treemap中？这个又不是要给微信发送的数据，
		// 为什么要排序？难道只是为了判断空？
		SortedMap<Object, Object> packageParams = new TreeMap<>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String next = it.next();
			String s1 = map.get(next);
			String v = "";
			if (s1 != null) {
				v = s1.trim();
			}
			packageParams.put(next, v);
		}
		String key = PayConfigUtil.API_KEY; // key

		System.err.println(packageParams);
		String out_trade_no = (String) packageParams.get("out_trade_no");//订单号,实际开发中应该在下面的 IF 中,除非需要对每个订单的每次支付结果做记录
		//要验证签名，是否是腾讯的支付
		boolean tenpaySign = PayCommonUtil.isTenpaySign("utf-8", packageParams, key);
		String resXml = "";
		if (tenpaySign) {
			//还要判断有没有支付成功！
			if ("SUCCESS".equals((String) packageParams.get("result_code"))) {

				String mch_id = (String) packageParams.get("mch_id");
				String openid = (String) packageParams.get("openid");
				String is_subscribe = (String) packageParams.get("is_subscribe");
				// String out_trade_no = (String)packageParams.get("out_trade_no");

				String total_fee = (String) packageParams.get("total_fee");

				System.err.println("mch_id:" + mch_id);
				System.err.println("openid:" + openid);
				System.err.println("is_subscribe:" + is_subscribe);
				System.err.println("out_trade_no:" + out_trade_no);
				System.err.println("total_fee:" + total_fee);

				//////////执行自己的业务逻辑////////////////

				System.err.println("支付成功");
				writeContent = "订单:" + out_trade_no + "支付成功";//拼接支付结果信息,写入文件,实际开发中删除
				//通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
				//这个时候一定要跟微信确认支付成功了，要不然微信会退款的，这个确认成功都需要按照微信的规定去写
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				//这个地方进行一个长链接的通知，用于页面重定向，通知用户付款成功
				//说明这个哥们没有篡改数据

				//这个地方给网页发送数据通知网页已经连通了
				WebSocketservlet.sendResult(out_trade_no,"支付成功");

			} else {
				writeContent = "订单" + out_trade_no + "支付失败,错误信息：" + packageParams.get("err_code");//拼接支付结果信息,写入文件,实际开发中删除
				System.err.println("订单" + out_trade_no + "支付失败,错误信息：" + packageParams.get("err_code"));
				//必须要通知吗？等过时可以是可以但是会浪费服务器性能
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
			//得到结果集之后就将所有的数据，都要返还给腾讯，有腾讯判断你是否失败
			BufferedOutputStream out = new BufferedOutputStream(
					response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();

		} else {
			//这个时候默认用户已经修改了提交信息，支付失败，提示应该是签名被修改，无法支付成功
			writeContent = "订单" + out_trade_no + "通知签名验证失败,支付失败";//拼接支付结果信息,写入文件,实际开发中删除
			System.err.println("通知签名验证失败");
		}
		//不要忘了记录日志writeContent，但是取决于业务逻辑
		fileOutputStream.write(writeContent.getBytes());
		fileOutputStream.close();
	}
}
