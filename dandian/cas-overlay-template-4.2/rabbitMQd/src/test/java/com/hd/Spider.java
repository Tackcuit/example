package com.hd;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Spider {
	@Test
	public void testSpider() throws Exception {
		//这个是正则的表达式，自己上网搜
		String reg = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		//编译字符串
		Pattern pattern = Pattern.compile(reg);

		//创建流将一个文件读取进来
		BufferedReader bufr = new BufferedReader(new FileReader("D:\\mail.txt"));
		String s;
		//按照行读取文件
		while ((s = bufr.readLine())!= null){
			//创建匹配器
			Matcher matcher = pattern.matcher(s);
			//匹配器如果匹配上回返回true
			if (matcher.find()){
				//找到之后就获取一次
				String group = matcher.group();
				System.out.println(group);
			}
		}

	}

	@Test
	public void testNetSpider() throws Exception{

		String reg = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		//编译字符串
		Pattern pattern = Pattern.compile(reg);

		//创建流将一个文件读取进来
		URL url = new URL("https://zhidao.baidu.com/question/1772307510687057620.html");
		URLConnection connection = url.openConnection();
		BufferedInputStream bufr = new BufferedInputStream(connection.getInputStream());
		String s;
		byte[] bytes = new byte[1024];
		//按照行读取文件
		System.setOut(new PrintStream(new File("d://123.txt")));
		while (bufr.read(bytes)!= -1){
			//创建匹配器
			System.out.println(new String(bytes));
			Matcher matcher = pattern.matcher(new String(bytes));
			//如果匹配器找到
			if (matcher.find()){
				String group = matcher.group();
				System.out.println(group);
			}
		}
	}

}
