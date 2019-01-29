package info;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * @author techlife
 * 
 */
public class InfoTester {
	public static void main(String[] args) {
		InfoTester tester = new InfoTester();
		tester.test();

	}
	
	@Test
	public void test() {
		// 创建两条路线信息
		Route route[] = new Route[2];
		route[0] = new Route();
		route[0].id = 1;
		route[0].starting = "中大";
		route[0].ending = "南方学院";

		route[1] = new Route();
		route[0].id = 2;
		route[0].starting = "南方学院";
		route[0].ending = "中大";

		// 创建班车
		Calendar  calendar  =  Calendar.getInstance();
		calendar.set(2014, 9, 1, 10, 30); // 2009年9月1日10时30分
		Date date = calendar.getTime();

		Shuttle shuttle[] = new Shuttle[2];
		shuttle[0] = new Shuttle(50);
		shuttle[0].setRoute(route[0]);
		shuttle[0].setDate(date);
		shuttle[0].setFee(22);
		shuttle[0].setId(1);
		
		shuttle[1] = new Shuttle(50);
		calendar.set(2014, 9, 1, 12, 30); // 2009年9月1日12时30分
		date = calendar.getTime();
		shuttle[1].setRoute(route[1]);
		shuttle[1].setDate(date);
		shuttle[1].setFee(22);
		shuttle[1].setId(2);
		
		// 创建用户
		User user1 = new User();
		user1.setId(100001);
		user1.setName("张三");
		// user1.setPwd("123");
		user1.setStuNum(Integer.toString(10001));
		
		// 创建车票/订单
		calendar.set(2014, 9, 1, 12, 30); // 2009年9月1日12时30分
		date = calendar.getTime();
		
		Ticket ticket1 = new Ticket(1);
		ticket1.setShuttle(shuttle[0]);
		ticket1.setUser(user1);
		ticket1.setStatus("已订票");

		// 显示车票信息
		ticket1.show();
		
	}
}
