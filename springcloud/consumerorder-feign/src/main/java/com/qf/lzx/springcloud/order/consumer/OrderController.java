package com.qf.lzx.springcloud.order.consumer;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.converters.Auto;
import com.qf.lzx.springcloud.order.feign.ClientFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;

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
@RestController
public class OrderController {

	//restTemplate是要自己创建的，但是eurekaClient是可以直接注入的
//	@Autowired
	private RestTemplate restTemplate;
//	private String url = "http://localhost:7900/user/";
	//一定不能忘了协议名，会报不知道的协议的错误

//	@Autowired
	private EurekaClient eurekaClient;

	@Autowired
	private ClientFeign clientFeign;

	@GetMapping("/order/{id}")
	public User getOrder(@PathVariable("id") Integer id) {
		/*InstanceInfo provider = eurekaClient.getNextServerFromEureka("PROVIDER", false);
//		System.out.println("之前" + url);
		String url = provider.getHomePageUrl();
		System.out.println("之后" + url);

		User user = restTemplate.getForObject( "http://PROVIDER/user/" + id, User.class);
		return user;*/
		User order = clientFeign.getorder(id);
		return order;
	}

//	@Bean
//	public RestTemplate getRestTemplate() {
//		return new RestTemplate();
//	}

}
