/**
 * 
 */
package client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import info.Shuttle;
import info.Ticket;
import info.User;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import messages.MessageShuttleQuery;
import messages.MessageShuttleQueryAck;

/**
 * @author techlife
 * 
 */
public class TicketBooker extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7285459016493447805L;

	private Ticket ticket = new Ticket();
	// 路线 - 起点、终点，使用下拉菜单
	private static final String arrayStarting[] = { "中大", "南方学院" };
	private JComboBox JCB_Starting = new JComboBox(arrayStarting);
	private JComboBox JCB_Ending = new JComboBox(arrayStarting);
	// private JButton buttonCalendar = new JButton("日期");
	private static final String arrayYear[] = { "2014", "2015", "2016", "2017", "2018" };
	private static final String arrayMonth[] = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12" };
	private static final String arrayDate[] = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };
	private JComboBox JCB_Year = new JComboBox(arrayYear);
	private JComboBox JCB_Month = new JComboBox(arrayMonth);
	private JComboBox JCB_Date = new JComboBox(arrayDate);

	private JButton buttonSearch = new JButton("查询");
	private JButton buttonBook = new JButton("预订");

	private JTable tableResult = null;
	DefaultTableModel tableModelDefault = null;
	private User user = null;
	private Shuttle shuttle = null;
	private Vector<Shuttle> searchResult = new Vector<Shuttle>();

	public TicketBooker(User user) {
		this.user = user;

		this.setTitle("车票预订");
		this.setLocation(400, 300);
		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);

		// 设置布局, 页面分上中下三部分：
		// 上面为选项，依次为：JLabel Route, JComboBox JCB_Starting JCB_Ending, JButton
		// Calendar选择, JButton 查询
		// 中间为查寻结果，采用JTable：依次为时间，票价，现有车票数, 选中任何一行后即可点击预订按钮
		// 下面为JButton 预订，点击后显示预订结果
		JPanel upper = new JPanel();
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		upper.setLocation(0, 0);
		upper.setSize(400, 100);
		middle.setLocation(0, 100);
		middle.setSize(400, 400);
		bottom.setLocation(0, 500);
		bottom.setSize(400, 50);

		// 在上面的面板添加元素
		upper.setLayout(new FlowLayout());
		upper.add(new JLabel("路线"));
		JCB_Starting.setToolTipText("起点");
		upper.add(JCB_Starting);
		JCB_Ending.setToolTipText("终点");
		upper.add(JCB_Ending);

		JCB_Year.setToolTipText("年");
		upper.add(JCB_Year);
		JCB_Month.setToolTipText("月");
		upper.add(JCB_Month);
		JCB_Date.setToolTipText("日");
		upper.add(JCB_Date);
		// 设置当前日期
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		JCB_Year.setSelectedIndex(date.getYear() + 1900
				- Integer.parseInt(arrayYear[0]));
		JCB_Month.setSelectedIndex(date.getMonth());
		JCB_Date.setSelectedIndex(date.getDate() - 1);

		upper.add(new JLabel());
		// upper.add(buttonCalendar);
		upper.add(buttonSearch);
		buttonSearch.addActionListener(this);
		this.add(upper);

		// 在中间的Panel上添加元素
		middle.setLayout(new BorderLayout());
		// resultTable = 初始化
		createResultTable();
		JScrollPane scrollPane = new JScrollPane(tableResult);

		middle.add(scrollPane);
		this.add(middle);

		// 在下面的Panel上添加元素
		bottom.setLayout(new BorderLayout());
		bottom.add(buttonBook, BorderLayout.CENTER);
		buttonBook.addActionListener(this);
		this.add(bottom);

		// 显示界面
		this.setVisible(true);
	}

	private void createResultTable() {
		if (tableResult != null)
			return;

		Object[][] data = {};
		// JTable：依次为时间，票价，现有车票数, 选中任何一行后即可点击预订按钮
		String[] name = { "时间", "票价", "剩余票数" };
		tableModelDefault = new DefaultTableModel(data, name);
		tableResult = new JTable(tableModelDefault);
		// 设置为单选
		tableResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void updateResultTable(Vector<Shuttle> searchResult) {
		if (tableResult == null || tableModelDefault == null)
			return;

		// 清空查询结果
		tableModelDefault.setRowCount(0);
				
		for (int i = 1; i <= searchResult.size(); i++) {
			Shuttle shuttle = searchResult.get(i-1);
			Date date = shuttle.getDate();
			// 增加第 i 行
			String time = "" + date.getHours() + " 时 " + date.getMinutes()
					+ " 分";
			Object data[] = { time, shuttle.getFee(), shuttle.getSeating() };
			tableModelDefault.addRow(data);
		}
	}

	private Vector<Shuttle> shuttleSearch() {
		String starting = (String) JCB_Starting.getSelectedItem();
		String ending = (String) JCB_Ending.getSelectedItem();
		int year = Integer.parseInt((String) JCB_Year.getSelectedItem());
		int month = Integer.parseInt((String) JCB_Month.getSelectedItem());
		int day = Integer.parseInt((String) JCB_Date.getSelectedItem());

		// 查询是否有班车
		MessageShuttleQuery msgSnt = new MessageShuttleQuery(starting, ending, year, month, day);
		
		// 发给服务器
		if(UserLogin.sendMessage(msgSnt)){
			MessageShuttleQueryAck msgAck = (MessageShuttleQueryAck)UserLogin.receiveMessage();
			//msgAck.show();
			searchResult = msgAck.getShuttles();
		}else{
			searchResult.setSize(0);
		}
		
		return searchResult;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonSearch) {
			//Vector<Shuttle> searchResult = shuttleSearch();
			searchResult = shuttleSearch();
			updateResultTable(searchResult);
		} else if (e.getSource() == buttonBook) {
			// tableResult 与 searchResult的内容是按顺序一一对应的
			int selectedRow = tableResult.getSelectedRow();
			// 检查是否选中某个班车
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(this, "请选择车次（单选）");
			} else {
				new TicketBookingGUI(user, searchResult.get(selectedRow));
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		User user = new User();
		user.setId(1001);
		user.setName("user");
		user.setStuNum("1001");

		new TicketBooker(user);
	}

}
