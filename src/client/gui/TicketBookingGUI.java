package client.gui;

import info.Route;
import info.Shuttle;
import info.Ticket;
import info.User;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import messages.MessageTicketBook;
import messages.MessageTicketBookAck;

public class TicketBookingGUI extends JFrame implements ActionListener {
	private Ticket ticket = new Ticket();
	private JButton buttonConfirm = new JButton("确  认");
	private JButton buttonCancel = new JButton("取  消");
    
	public TicketBookingGUI(User user, Shuttle shuttle) {
		this.ticket.setUser(user);
		this.ticket.setShuttle(shuttle);

		/*
		 * 界面效果 第1行：用户名，学号 第2行：路线：起点，终点 第3行：日期 第4行：确认按钮，取消按钮
		 */
		this.setTitle("车票信息");
		this.setLocation(500, 300);
		this.setSize(300, 160);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(4, 1, 0, 0));

		JPanel panel = new JPanel();
		// 第1行：用户名，学号
		panel.setLayout(new GridLayout(1, 4, 0, 0));
		panel.add(new JLabel("用户名"));
		panel.add(new JLabel(user.getName()));
		panel.add(new JLabel("学  号"));
		panel.add(new JLabel(user.getStuNum()));
		this.add(panel);

		// 第2行：路线：起点，终点
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 4, 0, 0));
		panel.add(new JLabel("起  点"));
		panel.add(new JLabel(shuttle.getRoute().starting));
		panel.add(new JLabel("终  点"));
		panel.add(new JLabel(shuttle.getRoute().ending));
		this.add(panel);

		// 第3行：日期
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("时  间    "));
		Date date = shuttle.getDate();
		panel.add(new JLabel("" + (date.getYear() + 1900) + " 年 "
				+ date.getMonth() + " 月 " + date.getDate() + " 日 "
				+ date.getHours() + " 时 " + date.getMinutes() + " 分 "));
		this.add(panel);

		// 第4行：确认按钮，取消按钮
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		panel.add(buttonConfirm);
		panel.add(buttonCancel);
		buttonConfirm.addActionListener(this);
		buttonCancel.addActionListener(this);
		this.add(panel);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonConfirm) {
			// 向数据库中增加记录，更新班次中车票数量
			if (handleTicketBook(ticket.getShuttle().getId(), ticket.getUser()
					.getId())) {
				JOptionPane.showMessageDialog(this, "订票成功");
			} else {
				JOptionPane.showMessageDialog(this, "订票失败");
			}
		} else if (e.getSource() == buttonCancel) {
			System.out.println(">>>>>>>>>>>>> 取消成功 <<<<<<<<<<<<<<<");
		}

		dispose();
	}

	private boolean handleTicketBook(long shuttleId, long userId) {
		MessageTicketBook msgSnt = new MessageTicketBook(shuttleId, userId);
		if (UserLogin.sendMessage(msgSnt)) {
			// 接收服务器返回的消息并显示出来
			MessageTicketBookAck msgRev = (MessageTicketBookAck) UserLogin
					.receiveMessage();
			if (msgRev != null) {
				return msgRev.isSuccess();
			}
		}

		return false;
	}

	public static void main(String[] args) {
		User user = new User();
		user.setId(100001);
		user.setName("张三");
		user.setStuNum(Integer.toString(10001));

		Route route = new Route();
		route.id = 1;
		route.starting = "中大";
		route.ending = "南方学院";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 9, 1, 10, 30); // 2009年9月1日10时30分
		Date date = calendar.getTime();
		Shuttle shuttle = new Shuttle(50);
		shuttle.setRoute(route);
		shuttle.setDate(date);
		shuttle.setFee(22);
		shuttle.setId(1);

		new TicketBookingGUI(user, shuttle);

	}

}
