package client;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import messages.Message;
import messages.MessageExample;
import messages.MessageLoginAck;
import messages.MessageLoginReq;
import messages.MessageShuttleQuery;
import messages.MessageShuttleQueryAck;
import messages.MessageTicketBook;
import messages.MessageTicketBookAck;

public class ClientDummy {

	public static void main(String[] args) {
		new ClientDummy();
	}

	// 假设Server IP为本机IP
	private String serverIP = "127.0.0.1";
	private int serverPort = 54321;
	// 记录是否已经建立了到服务器的TCP socket连接
	private boolean isConnected = false;
	private Socket socket = null;
	private OutputStream os = null;
	private InputStream is = null;
	private static ObjectInputStream ois = null;
	private static ObjectOutputStream oos = null;

    public ClientDummy() {
        if (connectToServer()) {
            // System.out.println("sendMessageExample: " +
            // sendMessageExample());
            // System.out.println("sendMessageLoginReq: " +
            // sendMessageLoginReq());
            // System.out.println("sendMessageShuttleQuery: " +
            // sendMessageShuttleQuery());
            System.out.println("sendMessageShuttleQuery: "
                    + sendMessageTicketBook());
        } else {
            System.out.println("Failed to connect to server " + serverIP + "/"
                    + serverIP);
        }

        try {
            Thread.sleep(1000); // 等1000毫秒后退出
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disconnectFromServer();
    }

	private boolean connectToServer() {
		// Socket尚未初始化，在端口打开连接，取出对象输入输出流
		try {
			socket = new Socket(serverIP, serverPort);
			socket.setSoTimeout(3000); // 如果服务器没有反映，尝试3000毫秒
			os = socket.getOutputStream();
			is = socket.getInputStream();
			oos = new ObjectOutputStream(os);
			ois = new ObjectInputStream(is);

			isConnected = true;
		} catch (IOException e) {
			isConnected = false;
		} finally {
		}

		return isConnected;
	}
	//断开服务器
	private void disconnectFromServer(){
		if(socket != null){
			try {				
				socket.shutdownOutput();
				socket.shutdownInput();
				socket.close();
				socket = null;
				oos = null; 
				ois = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static synchronized boolean sendMessage(Message msg) {
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static synchronized Message receiveMessage() {
		try {
			return (Message) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean sendMessageExample() {
		MessageExample msgSnt = new MessageExample();
		msgSnt.setData("Hello");
		
		if (sendMessage(msgSnt)) {
			// 接收服务器返回的消息并显示出来
			MessageExample msgRev = (MessageExample) receiveMessage();
			if (msgRev != null) {
				System.out.println("Message Received: " + msgRev.getData());
				return true;
			}
		}
		return false;
	}
	
	private boolean sendMessageLoginReq(){
		String account = "10001";
		String password = "10001";
		
		MessageLoginReq msgSnt = new MessageLoginReq(account, password);
		
		if (sendMessage(msgSnt)) {
			// 接收服务器返回的消息并显示出来
			MessageLoginAck msgRev = (MessageLoginAck) receiveMessage();
			if (msgRev != null) {
				msgRev.show();
				return true;
			}
		}
		return false;
	}
	
	private boolean sendMessageShuttleQuery(){
		String starting = "中大";
		String ending = "南方学院";
		int year = 2014, month = 8, date = 9;
		
		MessageShuttleQuery msgSnt = new MessageShuttleQuery(starting, ending, year, month, date);
		
		if (sendMessage(msgSnt)) {
			// 接收服务器返回的消息并显示出来
			MessageShuttleQueryAck msgRev = (MessageShuttleQueryAck) receiveMessage();
			if (msgRev != null) {
				msgRev.show();
				return true;
			}
		}
		return false;
	}

    private boolean sendMessageTicketBook() {
		long shuttleId = 3;
        long userId = 10002;

		MessageTicketBook msgSnt = new MessageTicketBook(shuttleId, userId);

		if (sendMessage(msgSnt)) {
			// 接收服务器返回的消息并显示出来
			MessageTicketBookAck msgRev = (MessageTicketBookAck) receiveMessage();
			if (msgRev != null) {
				return msgRev.isSuccess();
			}
		}
		return false;
    }
}
