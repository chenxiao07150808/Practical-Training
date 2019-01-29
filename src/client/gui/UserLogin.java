package client.gui;

import info.User;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import messages.Message;
import messages.MessageLoginAck;
import messages.MessageLoginReq;

public class UserLogin extends JFrame implements ActionListener {
	private JTextField fieldAccount = new JTextField();
	private JPasswordField fieldPassword = new JPasswordField();
	private JButton buttonLogin = new JButton("登   陆");

	// ======= for TCP: Begin ======================
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

	// ======= for TCP: End ======================

	public static void main(String[] args) {
		new UserLogin();

	}

	public UserLogin() {
		this.setTitle("用户登陆");
		this.setLocation(400, 300);
		this.setSize(300, 400);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		// 用户账号
		JLabel labelAccount = new JLabel("账  号");
		labelAccount.setAlignmentY(CENTER_ALIGNMENT);
		labelAccount.setBounds(50, 100, 100, 30);
		fieldAccount.setBounds(150, 100, 100, 30);
		this.getContentPane().add(labelAccount);
		this.getContentPane().add(fieldAccount);

		// 用户密码
		JLabel labelPwd = new JLabel("密  码");
		labelPwd.setAlignmentY(CENTER_ALIGNMENT);
		labelPwd.setBounds(50, 150, 100, 30);
		fieldPassword.setBounds(150, 150, 100, 30);
		this.getContentPane().add(labelPwd);
		this.getContentPane().add(fieldPassword, BorderLayout.CENTER);

		// 第6行为登陆按钮
		buttonLogin.setBounds(75, 250, 150, 30);
		this.getContentPane().add(buttonLogin);
		buttonLogin.addActionListener(this);

		this.setVisible(true);

		connectToServer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonLogin) {
			String account = fieldAccount.getText();
			String password = new String(fieldPassword.getPassword());

			User user = verifyAccount(account, password);
			if (user != null) {
				// 弹出车票预订窗口
				new TicketBooker(user);

				// 关闭当前窗口
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "账号或密码错误");
				fieldPassword.setText(null);
			}
		}
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

	private void disconnectFromServer() {
		if (socket != null) {
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

	static synchronized boolean sendMessage(Message msg) {
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static synchronized Message receiveMessage() {
		try {
			return (Message) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private User verifyAccount(String account, String password) {
		if (!isConnected) {
			int maxRetry = 10, j = 0;
			while (j++ < maxRetry) {
				if(connectToServer())
					break; // 连接成功
			}
		}
		
		// 如果没有与服务器建立连接，提示错误
		if (!isConnected)		{
			JOptionPane.showMessageDialog(this, "Failed to connect to server: " + serverIP);
			return null;
		}

		// 与服务器通信，验证用户
		MessageLoginReq msgLoginReq = new MessageLoginReq(account, password);
		MessageLoginAck msgLoginAck = null;
		try {
			oos.writeObject(msgLoginReq);			
			msgLoginAck = (MessageLoginAck) ois.readObject();;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}		
		
		return (msgLoginAck != null ? msgLoginAck.getUser() : null);
	}
}
