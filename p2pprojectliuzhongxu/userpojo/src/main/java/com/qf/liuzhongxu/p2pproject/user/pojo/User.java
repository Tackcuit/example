package com.qf.liuzhongxu.p2pproject.user.pojo;

import com.qf.liuzhongxu.p2pproject.commons.anno.SkipRedis;

import java.io.Serializable;

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
public class User implements Serializable{
	private String userId;
	private String userName;
	private String phoneNum;
	@SkipRedis
	private String password;
	private String referee;
	private String mycode;
	private String usertypeid;
	private String creditlevelid;
	private String creditlevelname;

	private Short userstatus;
	private String email;

	public User() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public String getMycode() {
		return mycode;
	}

	public void setMycode(String mycode) {
		this.mycode = mycode;
	}

	public String getUsertypeid() {
		return usertypeid;
	}

	public void setUsertypeid(String usertypeid) {
		this.usertypeid = usertypeid;
	}

	public String getCreditlevelid() {
		return creditlevelid;
	}

	public void setCreditlevelid(String creditlevelid) {
		this.creditlevelid = creditlevelid;
	}

	public String getCreditlevelname() {
		return creditlevelname;
	}

	public void setCreditlevelname(String creditlevelname) {
		this.creditlevelname = creditlevelname;
	}

	public Short getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(Short userstatus) {
		this.userstatus = userstatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", phoneNum='" + phoneNum + '\'' +
				", password='" + password + '\'' +
				", referee='" + referee + '\'' +
				", mycode='" + mycode + '\'' +
				", usertypeid='" + usertypeid + '\'' +
				", creditlevelid='" + creditlevelid + '\'' +
				", creditlevelname='" + creditlevelname + '\'' +
				", userstatus=" + userstatus +
				", email='" + email + '\'' +
				'}';
	}
}
