package com.qf.liuzhongxu.p2p.pojo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
public class BorrowData {
	private String borrowDataId;
	private String borrowDataName;
	private String fczPic;
	private String otherData;

	public BorrowData() {
	}

	public String getBorrowDataId() {
		return borrowDataId;
	}

	public void setBorrowDataId(String borrowDataId) {
		this.borrowDataId = borrowDataId;
	}

	public String getBorrowDataName() {
		return borrowDataName;
	}

	public void setBorrowDataName(String borrowDataName) {
		this.borrowDataName = borrowDataName;
	}

	public String getFczPic() {
		return fczPic;
	}

	public void setFczPic(String fczPic) {
		this.fczPic = fczPic;
	}

	public String getOtherData() {
		return otherData;
	}

	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}

	//反射应用的小栗子
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Class<? extends BorrowData> aClass = this.getClass();
		Field[] fields = aClass.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			sb.append(name + ":=");
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, this.getClass());
				Method readMethod = propertyDescriptor.getReadMethod();
				Object invoke = readMethod.invoke(this);
				sb.append(invoke == null ? "null" : invoke.toString());
				sb.append("\r\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
