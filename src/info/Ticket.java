package info;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 */

/**
 * @author techlife
 * 
 */
public class Ticket  implements Serializable {
	private long id = -1;
	private Shuttle shuttle = null;
	private User user = null;
	private String status = new String("未乘坐");

	public Ticket() {
	}
	
	public Ticket(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Shuttle getShuttle() {
		return shuttle;
	}

	public void setShuttle(Shuttle shuttle) {
		this.shuttle = shuttle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void show(){
		System.out.println("=== Ticket Information ===");
		user.show();
		shuttle.show();
		System.out.println("*** Status : " + status);
		System.out.println();
	}

}
