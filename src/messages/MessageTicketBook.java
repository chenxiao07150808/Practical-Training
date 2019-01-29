package messages;

import java.io.Serializable;

public class MessageTicketBook extends Message  implements Serializable{
	private long shuttleId = 0;
	private long userId = 0;
	
	public MessageTicketBook(long shuttleId, long userId) {
		super(MSG_TYPE.MSG_TICKET_BOOK_REQ);
		this.shuttleId = shuttleId;
		this.userId = userId;
	}

	public long getShuttleId() {
		return shuttleId;
	}

	public long getUserId() {
		return userId;
	}	
}
