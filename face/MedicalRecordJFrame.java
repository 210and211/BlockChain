package face;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

public class MedicalRecordJFrame implements ActionListener {
	XMLParser xml;
	JFrame frame;
	JButton close;
	JButton small;
	MedicalRecordJFrame(String path){
		xml=new XMLParser(path);
		init();
	}
	void init() {
		frame = new JFrame("病历");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setBounds((screenWidth-800)/2, (screenHeight-450) / 2, 800, 450);

		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);


		ImageIcon ii = new ImageIcon("image_interface/background2.png");
		JLabel jb1 = new JLabel(ii);
		jb1.setBounds(0, 0, ii.getIconWidth(), ii.getIconHeight());
		frame.getLayeredPane().add(jb1,new Integer(Integer.MIN_VALUE));
		JPanel oneJPanel = (JPanel)frame.getContentPane();
		oneJPanel.setOpaque(false);
		




		/*
		 * 北部组件，包括
		 * 最小化，关闭按钮，窗口活动，窗口标题
		 */
		JPanel jp_N = new JPanel();
		jp_N.setLayout(null);
		jp_N.setPreferredSize(new Dimension(0, 45));
		MouseEventListener mouseListener = new MouseEventListener(this);

		jp_N.addMouseListener(mouseListener);
		jp_N.addMouseMotionListener(mouseListener);
		
		
		
		
		jp_N.setOpaque(false);
		jp_N.setBounds(0, 0, 800, 100);
		
		JLabel title = new JLabel("患者病历");
		title.setFont(new Font("微软雅黑",Font.PLAIN,30));
//		title.setForeground(Color.RED);
		title.setBounds(300, 20,200, 80);

		close= new JButton(new ImageIcon("image_interface/关闭1.png"));
		close.setRolloverIcon(new ImageIcon("image_interface/关闭2.png"));//鼠标悬停
		close.setPressedIcon(new ImageIcon("image_interface/关闭1.png"));//鼠标按下
		close.setBounds(742, 5, 40, 30);
		close.setBorderPainted(false);
		close.addActionListener(this);

		small= new JButton(new ImageIcon("image_interface/最小化1.png"));
		small.setRolloverIcon(new ImageIcon("image_interface/最小化2.png"));//鼠标悬停
		small.setPressedIcon(new ImageIcon("image_interface/最小化1.png"));//鼠标按下
		small.setBounds(695, 5, 40, 30);
		small.setBorderPainted(false);
		small.addActionListener(this);

		jp_N.add(title);
		jp_N.add(close);
		jp_N.add(small);
		

		JPanel panel_filework = new JPanel();
		panel_filework.setVisible(true);
		panel_filework.setLayout(null);
		panel_filework.setBackground(null);
		panel_filework.setOpaque(false);
		panel_filework.setBounds(0, 100,800, 410);
		JLabel Label1 = new JLabel("姓名");
		Label1.setBounds(40, 20,50, 40);
//		Label1.setForeground(Color.gray);
		Label1.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label1.setVisible(true);
		panel_filework.add(Label1);

		JTextField text1 = new JTextField();
		text1.setBounds(90, 20, 90, 40);
		text1.setOpaque(false);
		text1.setBorder(null);
		text1.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text1.setText(xml.get_name());
		text1.setEditable(false);
		panel_filework.add(text1);

		JLabel Label2 = new JLabel("ID");
		Label2.setBounds(210,20,30, 40);
//		Label2.setForeground(Color.gray);
		Label2.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label2.setVisible(true);
		panel_filework.add(Label2);

		JTextField text2 = new JTextField();
		text2.setBounds(240, 20, 100, 40);
		text2.setOpaque(false);
		text2.setBorder(null);
		text2.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text2.setText(xml.get_id());
		text2.setEditable(false);
		panel_filework.add(text2);

		JLabel Label3 = new JLabel("性别");
		Label3.setBounds(360, 20,50, 40);
//		Label3.setForeground(Color.gray);
		Label3.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label3.setVisible(true);
		panel_filework.add(Label3);

		JTextField text3 = new JTextField();
		text3.setBounds(410, 20, 50, 40);
		text3.setOpaque(false);
		text3.setBorder(null);
		text3.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text3.setText(xml.get_sex());
		text3.setEditable(false);
		panel_filework.add(text3);


		JLabel Label4 = new JLabel("年龄");
		Label4.setBounds(40, 80,50, 40);
//		Label4.setForeground(Color.gray);
		Label4.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label4.setVisible(true);
		panel_filework.add(Label4);

		JTextField text4 = new JTextField();
		text4.setBounds(90, 80, 35, 40);
		text4.setOpaque(false);
		text4.setBorder(null);
		text4.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text4.setText(xml.get_age());
		text4.setEditable(false);
		panel_filework.add(text4);

		JLabel Label5 = new JLabel("时间");
		Label5.setBounds(150, 80,50, 40);
//		Label5.setForeground(Color.gray);
		Label5.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label5.setVisible(true);
		panel_filework.add(Label5);

		JTextField text5 = new JTextField();
		text5.setBounds(200, 80, 115, 40);
		text5.setOpaque(false);
		text5.setBorder(null);
		text5.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text5.setText(xml.get_time());
		text5.setEditable(false);
		panel_filework.add(text5);

		JLabel Label6 = new JLabel("科室");
		Label6.setBounds(335, 80,50, 40);
	//	Label6.setForeground(Color.gray);
		Label6.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label6.setVisible(true);
		panel_filework.add(Label6);

		JTextField text6 = new JTextField();
		text6.setBounds(385, 80, 75, 40);
		text6.setOpaque(false);
		text6.setBorder(null);
		text6.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text6.setText(xml.get_department());
		text6.setEditable(false);
		panel_filework.add(text6);
		
		
		JLabel Label7 = new JLabel("医院");
		Label7.setBounds(40, 140,50, 40);
	//	Label7.setForeground(Color.gray);
		Label7.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label7.setVisible(true);

		panel_filework.add(Label7);

		JTextField text7 = new JTextField();
		text7.setBounds(90, 140, 370, 40);
		text7.setOpaque(false);
		text7.setBorder(null);
		text7.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text7.setText(xml.get_hospital());
		text7.setEditable(false);
		//text6.setOpaque(false);
		panel_filework.add(text7);


		

		JLabel Label8 = new JLabel("确认疾病");
		Label8.setBounds(40, 200,100, 40);
	//	Label8.setForeground(Color.gray);
		Label8.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label8.setVisible(true);
		panel_filework.add(Label8);

		JTextField text8 = new JTextField();
		text8.setBounds(125, 200, 125, 40);
		text8.setOpaque(false);
		text8.setBorder(null);
		text8.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text8.setText(xml.get_disease());
		text8.setEditable(false);
		panel_filework.add(text8);


		JLabel Label9 = new JLabel("病情描述");
		Label9.setBounds(470, 0,100, 40);
//		Label9.setForeground(Color.gray);
		Label9.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label9.setVisible(true);

		panel_filework.add(Label9);

		JTextArea t =new JTextArea();
		
		t.setFont(new Font("微软雅黑",Font.PLAIN,18));
		t.setLineWrap(true);        //激活自动换行功能 
		t.setWrapStyleWord(true);            // 激活断行不断字功能
		t.setText(xml.get_description());
		t.setBackground(Color.WHITE);
		t.setBorder(null);
		t.setEditable(false);
		JScrollPane scroll = new JScrollPane(t);
		//把定义的JTextArea放到JScrollPane里面去
		//分别设置水平和垂直滚动条自动出现
		scroll.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(470,50, 300, 250);
		panel_filework.add(scroll);

		JLabel Label10 = new JLabel("诊治医生");
		Label10.setBounds(260, 200,100, 40);
//		Label10.setForeground(Color.gray);
		Label10.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label10.setVisible(true);
		panel_filework.add(Label10);

		JTextField text10 = new JTextField();
		text10.setBounds(345, 200, 115, 40);
		text10.setOpaque(false);
		text10.setBorder(null);
		text10.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text10.setText(xml.get_doctor());
		text10.setEditable(false);
		panel_filework.add(text10);
	

		JLabel Label11 = new JLabel("处方");
		Label11.setBounds(40, 260,50, 40);
//		Label11.setForeground(Color.gray);
		Label11.setFont(new Font("微软雅黑",Font.PLAIN,18));
		Label11.setVisible(true);
		panel_filework.add(Label11);

		JTextField text11 = new JTextField();
		text11.setBounds(90, 260, 370, 40);
		text11.setOpaque(false);
		text11.setBorder(null);
		text11.setFont(new Font("微软雅黑",Font.PLAIN,18));
		text11.setText(xml.get_cure());
		text11.setEditable(false);
		panel_filework.add(text11);
		
		frame.add(jp_N);
		frame.add(panel_filework);
		frame.show();
	}
	public void actionPerformed(ActionEvent e) {
		Object event=e.getSource();
		if(event==close){
			frame.dispose();

		}
		if(event==small){
			frame.setExtendedState(JFrame.ICONIFIED);
		}
	}
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		MedicalRecordJFrame m=new MedicalRecordJFrame("test.xml");
	}

}
class MouseEventListener implements MouseInputListener{

	Point origin;
	//鼠标拖拽想要移动的目标组件
	MedicalRecordJFrame face;

	public MouseEventListener(MedicalRecordJFrame face) {
		this.face = face;
		origin = new Point();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * 记录鼠标按下时的点
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		origin.x = e.getX();
		origin.y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	/**
	 * 鼠标移进标题栏时，设置鼠标图标为移动图标
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		this.face.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	/**
	 * 鼠标移出标题栏时，设置鼠标图标为默认指针
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		this.face.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * 鼠标在标题栏拖拽时，设置窗口的坐标位置
	 * 窗口新的坐标位置 = 移动前坐标位置+（鼠标指针当前坐标-鼠标按下时指针的位置）
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = this.face.frame.getLocation();
		this.face.frame.setLocation(
				p.x + (e.getX() - origin.x),
				p.y + (e.getY() - origin.y));
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

}
