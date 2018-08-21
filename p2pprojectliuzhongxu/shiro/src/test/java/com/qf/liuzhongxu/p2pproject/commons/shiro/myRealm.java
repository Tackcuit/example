package com.qf.liuzhongxu.p2pproject.commons.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

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
public class myRealm extends AuthorizingRealm {//因为我们继承了这个，所以说在调用的时候调用默认的然后会转到自己写的这个了

	//取出的数据都要放在redis中，下一次在访问的时候先去redis中去获取
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//这个是授权
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//认证信息
		String principal = (String) token.getPrincipal();
		String credential = new String((char[]) token.getCredentials());
		if(!principal.equals("admin")){
			throw new UnknownAccountException("用户名或密码不正确");
		}
		//从数据库取出数据,如果获取不到用户名就说明已经用户名不存在，但是一定要返回用户名或密码错误
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, "123", new SimpleByteSource(principal), this.getName());
		//这个info要传入从数据库查出的密码
		return info;
	}
}
