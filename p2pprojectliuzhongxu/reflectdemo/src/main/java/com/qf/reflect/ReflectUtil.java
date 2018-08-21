package com.qf.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

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
public class ReflectUtil {
	public static <T> T parse(Map<String, String[]> map,Class<T> clazz) throws Exception {
		T t = clazz.newInstance();
		Set<Map.Entry<String, String[]>> entries = map.entrySet();
		for (Map.Entry<String, String[]> entry : entries) {
			String key = entry.getKey();
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(key,clazz);
			//最重要的是进行判断能不能进行反射注入值
			if (propertyDescriptor != null) {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				writeMethod.invoke(t,entry.getValue()[0]);
			}


		}
		return t;
	}
}
