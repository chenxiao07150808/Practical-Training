package server.database;

import info.Shuttle;
import info.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class CPYDDatabase {
	private static Connection conn = null;

	// 取得数据库连接，本服务器程序仅使用这一个连接实例
	private static Connection getConnection() {
		if (conn != null) {
			return conn;
		}

		String driver_MySQL = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/CPYD?useUnicode=true&characterEncoding=UTF-8";
		String account_MySQL = "root";
		String password_MySQL = "123";

		try {
			Class.forName(driver_MySQL);
			conn = DriverManager.getConnection(url, account_MySQL,
					password_MySQL);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据库连接失败！");
		}
		return conn;
	}

	private static String toSqlString(String str) {
		return new String(" '" + str + "' ");
	}

	// account is Name
	public static User userQquery(String accountName) {
		User user = null;

		String sql = "select * from userlist where name = " + toSqlString(accountName);
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next() == true) {
				user = new User();
				user.setId(rs.getLong(1));
				user.setName(rs.getString(2));
				user.setStuNum(rs.getString(3));
				user.setPwd(rs.getString(4));
			}
		} catch (SQLException sqle) {
			System.out.println("查询数据出现异常: " + sqle.getMessage());
		}

		return user;
	}
	public static void main(String[] args) {
		CPYDDatabase a = new CPYDDatabase();
		a.getConnection();
		a.shuttleQquery("中大", "南方学院", 2014, 8, 8);
	}
	/** 
	 * @param starting
	 * @param ending
	 * @param year
	 * @param month
	 * @param day
	 * @return 为null表示没有相应车次，否则查询到
	 */
	public static Vector<Shuttle> shuttleQquery(String starting, String ending,
			int year, int month, int day) {
		Vector<Shuttle> result = new Vector<Shuttle>();
		Shuttle shuttle = null;
		Calendar calendar = Calendar.getInstance();
		Date date = null;

		String sql = "select id, s_time, capacity, seating from shuttlelist  where "
				+ "s_starting = "
				+ toSqlString(starting)
				+ " AND "
				+ "s_ending = "
				+ toSqlString(ending)
				+ " AND "
				+ "s_date = "
				+ toSqlString(new String("" + year + "-" + month + "-" + day));
		System.out.println(sql);
		//System.out.println("ss1s");
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next() == true) {
				
				long id = rs.getLong(1);
				String str = rs.getString(2); // TIME, e.g. '12:30:00'
				int capacity = rs.getShort(3);
				int seating = rs.getShort(4);
				int hourOfDay = Integer.parseInt(str.substring(0, 2));
				int minute = Integer.parseInt(str.substring(3, 5));
				calendar.set(year, month, day, hourOfDay, minute);
				date = calendar.getTime();

				shuttle = new Shuttle(capacity, seating);
				shuttle.setId(id); // ID
				shuttle.setRoute(starting, ending); // s_starting, s_starting
				shuttle.setDate(date);
				System.out.println(shuttle.getDate());
				result.add(shuttle);
			}
		} catch (SQLException sqle) {
			System.out.println("[shuttleQquery]查询数据出现异常: " + sqle.getMessage());
		}
      
		return result;
	}
	
	
	/** 
	 * @param shuttleId
	 * @param userId
	 * @return true表示预定成功，false表示预定失败
	 */
	public static synchronized boolean ticketBook(long shuttleId, long userId) {
		String sql = null;
		try {
			// 更新shuttlelist
			Statement stmt = getConnection().createStatement();
			sql = "update shuttlelist set seating = seating - 1 where id = "
					+ shuttleId;
			stmt.executeUpdate(sql);
			sql = "insert into ticketlist(shuttle_id, user_id, status) values (" + 
					shuttleId + ", " + userId + ", '已预订');" ;
			stmt.executeUpdate(sql);
			
			return true;
		} catch (SQLException sqle) {
			System.out.println("[ticketBook]查询数据出现异常: " + sqle.getMessage());
		}
		return false;
	}
}
