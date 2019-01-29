package server.database;

import info.Shuttle;
import info.User;

import java.util.Vector;

public class DatabaseUsage {
	public static void main(String[] args) {
		new DatabaseUsage();
	}
	
	public DatabaseUsage(){
		//System.out.println("useUserAPI result: " + this.useUserAPI());
		//System.out.println("useShuttleQuery result: " + this.useShuttleQuery());
		System.out.println("useShuttleQuery result: " + this.useTicketBook());
	}
	
	public boolean useUserAPI(){
		String name = "10001";
		String password = "10001";
		
		User user = CPYDDatabase.userQquery(name);
		if (user == null){
			return false;
		}
		else if(! user.verifyPwd(password)) {
			return false;
		}
		
		user.show();
		
		return true;
	}	
	
	public boolean useShuttleQuery(){
		String starting = "中大";
		String ending = "南方学院";
		int year = 2014, month = 8, day = 9;
		
		Vector<Shuttle> shuttles = CPYDDatabase.shuttleQquery(starting, ending, year, month, day);		
		if (shuttles == null || shuttles.size() == 0){
			return false;
		}
		// else 有车
		
		for(Shuttle shuttle : shuttles){
			shuttle.show();
		}
		
		return true;
	}
	
	public boolean useTicketBook(){
		long shuttleId = 1;
		long userId = 10001;
		return CPYDDatabase.ticketBook(shuttleId, userId);
	}
	
}
