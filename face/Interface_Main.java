package face;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import config.Configuration;
import medic_information.*;
import patientCard.PatientCard;
import cryptogram.*;
import block.*;
import config.*;



public class Interface_Main extends JFrame implements ActionListener{
	public static void main(String[] args) {
		new Interface_Main("xinxianquan","2581751912@qq.com");
	}

	JFrame frame;
	JPanel jp_N;
	// 上边面板的控件
	private JButton btn_suyuan, btn_upload,btn_zhanghu, close, small,submit1,submit2,fileworkscan_choose,fileworkscan_read;
	private JPanel panel_filework;
	private DrawPanel line=new DrawPanel();
	public  JTextField text1,text3,text2,text4,text5,text6,text7,text8,text9,text10,text11;
	private JTextArea t,read;
	private String key,name,section,sign;
	private long preBlockIndex,ID;
	private int infoID;
	private JTable table;
	private DefaultTableModel tableModel;
	private JFileChooser jfc=new JFileChooser();
	PatientCard A;

	Configuration config = new Configuration();

	public Interface_Main(String userid, String email){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		MainWindow();

	}

	public void MainWindow() {

		/*
		 * 以下是整个窗口的设计，包括
		 * 整个窗口的大小，背景，出现的位置
		 */
		
		ID=001;
		//hospitalID=002;
		preBlockIndex=0;
		sign="隔壁老王";
		section="外科";
		
		frame = new JFrame("主界面");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setBounds(screenWidth /3, screenHeight / 4, 1000, 644);

		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);


		ImageIcon ii = new ImageIcon("image_interface/background.png");
		JLabel jb1 = new JLabel(ii);
		jb1.setBounds(0, 0, ii.getIconWidth(), ii.getIconHeight());
		frame.getLayeredPane().add(jb1,new Integer(Integer.MIN_VALUE));
		JPanel oneJPanel = (JPanel)frame.getContentPane();
		oneJPanel.setOpaque(false);





		/*
		 * 北部组件，包括
		 * 最小化，关闭按钮，窗口活动，窗口标题
		 */
		jp_N = new JPanel();
		jp_N.setLayout(null);
		jp_N.setPreferredSize(new Dimension(0, 44));
		MouseEventListener mouseListener = new MouseEventListener(this);

		jp_N.addMouseListener(mouseListener);
		jp_N.addMouseMotionListener(mouseListener);
		jp_N.setOpaque(false);
		jp_N.setBounds(0, 0, 1000, 65);

		close= new JButton(new ImageIcon("image_interface/关闭1.png"));
		close.setRolloverIcon(new ImageIcon("image_interface/关闭2.png"));//鼠标悬停
		close.setPressedIcon(new ImageIcon("image_interface/关闭1.png"));//鼠标按下
		close.setBounds(942, 35, 40, 30);
		close.addActionListener(this);

		small= new JButton(new ImageIcon("image_interface/最小化1.png"));
		small.setRolloverIcon(new ImageIcon("image_interface/最小化2.png"));//鼠标悬停
		small.setPressedIcon(new ImageIcon("image_interface/最小化1.png"));//鼠标按下
		small.setBounds(895, 35, 40, 30);
		small.addActionListener(this);

		jp_N.add(close);
		jp_N.add(small);


		/*
		 * 中北部组件，包括
		 * 嵌入，下载，删除,刷新按钮
		 *
		 */

		panel_filework = new JPanel();
		panel_filework.setBounds(240, 110,760, 534);
		//panel_filework.setBounds(240, 110,500, 500);
		panel_filework.setVisible(true);
		panel_filework.setLayout(null);
		panel_filework.setBackground(null);
		panel_filework.setOpaque(false);
		frame.getContentPane().add(panel_filework);




		/*
		 * logo
		 *
		 */
		ImageIcon p = new ImageIcon("image_interface/b3.png");
		JLabel logo = new JLabel(p);
		logo.setFocusable(false);
		logo.setBounds(20, 5, p.getIconWidth(), p.getIconHeight());
		frame.add(logo);


		JLabel email2 = new JLabel("区块链医疗系统1.0");
		email2.setFont(new Font("宋体",Font.PLAIN,15));
		email2.setBounds(50, 50, 160, 50);
		frame.add(email2);

		/*
		 *
		 * 上传，解密，提取，溯源
		 *
		 */
		btn_zhanghu=new JButton(new ImageIcon("image_interface/账户1.png"));
		btn_zhanghu.setRolloverIcon(new ImageIcon("image_interface/账户2.png"));
		btn_zhanghu.setVisible(true);
		btn_zhanghu.setBounds(0,161, 180, 69);
		frame.add(btn_zhanghu);
		btn_zhanghu.addActionListener(this);




		btn_upload=new JButton(new ImageIcon("image_interface/上传1.png"));
		btn_upload.setRolloverIcon(new ImageIcon("image_interface/上传2.png"));
		btn_upload.setVisible(true);
		btn_upload.setBounds(0,253, 180, 69);
		frame.add(btn_upload);
		btn_upload.addActionListener(this);


		btn_suyuan = new JButton(new ImageIcon("image_interface/溯源1.png"));
		btn_suyuan.setRolloverIcon(new ImageIcon("image_interface/溯源2.png"));
		btn_suyuan.setBounds(0,345,180,69);
		btn_suyuan.setVisible(true);
		frame.add(btn_suyuan);
		btn_suyuan.addActionListener(this);





		frame.add(jp_N);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
	public void paint(Graphics g){
		super.paint(g);
		//g.drawImage(sun, 100, 100, null);
		//画线
		g.drawLine(100, 100, 200, 200);
		g.setColor(Color.BLUE);
		//画矩形
		g.drawRect(50, 50, 100, 90);
		//画椭圆
		g.drawOval(50, 50, 100, 100);
		g.setColor(Color.yellow);
		//画字符串在窗口上
		g.drawString("Java自学时间开始", 50, 80);
	}


	
		
		
		
		public void actionPerformed(ActionEvent e) {
			Object event=e.getSource();
			if(event==close){
				System.exit(0);
			}
			if(event==small){
				frame.setExtendedState(JFrame.ICONIFIED);
			}

			if(event==btn_upload){
				panel_filework.removeAll();



				line.setBounds(240, 110,760, 534);
				line.setVisible(true);
				line.setLayout(null);
				line.setBackground(null);
				line.setOpaque(false);
				frame.getContentPane().add(line);

				JLabel Label1 = new JLabel("姓名");
				Label1.setBounds(50, 20,50, 40);
				Label1.setForeground(Color.gray);
				Label1.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label1.setVisible(true);
				panel_filework.add(Label1);

				text1 = new JTextField();
				text1.setBounds(100, 20, 100, 40);
				text1.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text1);

				JLabel Label2 = new JLabel("ID");
				Label2.setBounds(250, 20,30, 40);
				Label2.setForeground(Color.gray);
				Label2.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label2.setVisible(true);
				panel_filework.add(Label2);

				text2 = new JTextField();
				text2.setBounds(280, 20, 100, 40);
				text2.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text2);

				JLabel Label3 = new JLabel("性别");
				Label3.setBounds(450, 20,50, 40);
				Label3.setForeground(Color.gray);
				Label3.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label3.setVisible(true);
				panel_filework.add(Label3);

				text3 = new JTextField();
				text3.setBounds(500, 20, 50, 40);
				text3.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text3);


				JLabel Label4 = new JLabel("年龄");
				Label4.setBounds(50, 80,50, 40);
				Label4.setForeground(Color.gray);
				Label4.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label4.setVisible(true);
				panel_filework.add(Label4);

				text4 = new JTextField();
				text4.setBounds(100, 80, 50, 40);
				text4.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text4);

				JLabel Label5 = new JLabel("时间");
				Label5.setBounds(250, 80,50, 40);
				Label5.setForeground(Color.gray);
				Label5.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label5.setVisible(true);
				panel_filework.add(Label5);

				text5 = new JTextField();
				text5.setBounds(300, 80, 130, 40);
				text5.setFont(new Font("微软雅黑",Font.PLAIN,18));
				LocalDate today = LocalDate.now();
				text5.setText(today.toString());
				panel_filework.add(text5);

				JLabel Label6 = new JLabel("医院");
				Label6.setBounds(450, 80,50, 40);
				Label6.setForeground(Color.gray);
				Label6.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label6.setVisible(true);

				panel_filework.add(Label6);

				text6 = new JTextField();
				text6.setBounds(500, 80, 150, 40);
				text6.setFont(new Font("微软雅黑",Font.PLAIN,18));
				//text6.setOpaque(false);
				panel_filework.add(text6);


				JLabel Label7 = new JLabel("科室");
				Label7.setBounds(50, 140,50, 40);
				Label7.setForeground(Color.gray);
				Label7.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label7.setVisible(true);
				panel_filework.add(Label7);

				text7 = new JTextField();
				text7.setBounds(100, 140, 75, 40);
				text7.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text7);

				JLabel Label8 = new JLabel("确认疾病");
				Label8.setBounds(250, 140,100, 40);
				Label8.setForeground(Color.gray);
				Label8.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label8.setVisible(true);
				panel_filework.add(Label8);

				text8 = new JTextField();
				text8.setBounds(330, 140, 100, 40);
				text8.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text8);


				JLabel Label9 = new JLabel("病情描述");
				Label9.setBounds(50, 200,100, 40);
				Label9.setForeground(Color.gray);
				Label9.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label9.setVisible(true);

				panel_filework.add(Label9);

				t =new JTextArea();
				t.setEditable(true);
				t.setFont(new Font("微软雅黑",Font.PLAIN,18));
				JScrollPane scroll = new JScrollPane(t);
				//把定义的JTextArea放到JScrollPane里面去
				//分别设置水平和垂直滚动条自动出现
				scroll.setHorizontalScrollBarPolicy(
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.setVerticalScrollBarPolicy(
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scroll.setBounds(50, 235, 500, 150);
				panel_filework.add(scroll);


				JLabel Label10 = new JLabel("处方");
				Label10.setBounds(50, 390,50, 40);
				Label10.setForeground(Color.gray);
				Label10.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label10.setVisible(true);
				panel_filework.add(Label10);

				text10 = new JTextField();
				text10.setBounds(100, 390, 300, 40);
				text10.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text10);


				JLabel Label11 = new JLabel("诊治医生");
				Label11.setBounds(50, 450,100, 40);
				Label11.setForeground(Color.gray);
				Label11.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label11.setVisible(true);
				panel_filework.add(Label11);

				text11 = new JTextField();
				text11.setBounds(150, 450, 100, 40);
				text11.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text11);


				submit1=new JButton(new ImageIcon("image_interface/提交1.png"));
				submit1.setRolloverIcon(new ImageIcon("image_interface/提交2.png"));
				submit1.setVisible(true);
				submit1.setBounds(600,460, 100, 50);
				panel_filework.add(submit1);
				submit1.addActionListener(this);



				panel_filework.repaint();
			}

			if(event==btn_suyuan){
				panel_filework.removeAll();
				frame.remove(line);
				line.repaint();
				panel_filework.repaint();

				JLabel Label1 = new JLabel("科室");
				Label1.setBounds(50, 20,50, 40);
				Label1.setForeground(Color.gray);
				Label1.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label1.setVisible(true);
				panel_filework.add(Label1);

				text1 = new JTextField();
				text1.setBounds(100, 20, 100, 40);
				text1.setFont(new Font("微软雅黑",Font.PLAIN,18));
				//text1.cornerRadius = 5;
				panel_filework.add(text1);

				JLabel Label2 = new JLabel("医院");
				Label2.setBounds(250, 20,50, 40);
				Label2.setForeground(Color.gray);
				Label2.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label2.setVisible(true);
				panel_filework.add(Label2);

				text2 = new JTextField();
				text2.setBounds(300, 20, 100, 40);
				text2.setFont(new Font("微软雅黑",Font.PLAIN,18));
				panel_filework.add(text2);

				JLabel Label3 = new JLabel("时间");
				Label3.setBounds(430, 20,50, 40);
				Label3.setForeground(Color.gray);
				Label3.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label3.setVisible(true);
				panel_filework.add(Label3);

				text3 = new JTextField();
				text3.setBounds(480, 20, 120, 40);
				text3.setFont(new Font("微软雅黑",Font.PLAIN,18));
				text3.setText("2000-05-01");
				panel_filework.add(text3);

				JLabel Label4 = new JLabel("--");
				Label4.setBounds(600, 20,20, 40);
				Label4.setForeground(Color.gray);
				Label4.setFont(new Font("微软雅黑",Font.PLAIN,18));
				Label4.setVisible(true);

				panel_filework.add(Label4);

				text4 = new JTextField();
				text4.setBounds(620, 20, 120, 40);
				text4.setFont(new Font("微软雅黑",Font.PLAIN,18));
				text4.setText("2010-05-01");
				panel_filework.add(text4);

				submit2=new JButton(new ImageIcon("image_interface/提交1.png"));
				submit2.setRolloverIcon(new ImageIcon("image_interface/提交2.png"));
				submit2.setVisible(true);
				submit2.setBounds(600,460, 100, 50);
				panel_filework.add(submit2);
				submit2.addActionListener(this);



				String[] columnTitle = {"医院" , "科室"  , "时间" };
				String[][] tabledate={
						//new Object[]{"1","1","uiui ","qwe"},
						//new Object[]{"1","1","uiui ","qwe"}
				};
				tableModel = new DefaultTableModel(tabledate,columnTitle);
				table=new JTable(tableModel);
				table.setRowHeight(30);//行高

				//第3列列宽
				TableColumnModel cm = table.getColumnModel();
				TableColumn column = cm.getColumn(2);
				column.setPreferredWidth(150);


				JScrollPane jsp=new JScrollPane(table);
				jsp.setVisible(true);
				table.getTableHeader().setFont(new Font("黑体", Font.PLAIN,18));
				jsp.setBounds(50, 100, 600, 300);
				panel_filework.add(jsp);

			}
			if(event==submit1){//病例上传
				//首先获取当前拜占庭节点ip，之后选取一个ip发送信息
				//PatientCard A= PatientCard.getPatientCard(text1.getText());
				while(true){
				ArrayList<String> bztIP=null;
				Socket socket;
				try {
					socket = new Socket(config.getIP_LIST().get(0),config.getPORT());
					OutputStream os = socket.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeByte(5);
					oos.flush();

					InputStream is = socket.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					preBlockIndex=ois.readLong();
					bztIP = (ArrayList<String>) ois.readObject();
					oos.close();
					os.close();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					System.out.println("ArrayList<String>类未能正确解析.");
					e1.printStackTrace();
				}

				Xml_produce.BulidXml(text1.getText(),text2.getText(),text4.getText(),text3.getText(),text6.getText(),text5.getText(),
						text7.getText(),text8.getText(),t.getText(),text10.getText(),text11.getText());
				
				
				A.addKey(10010, "10010");//
				key=A.getKey(Integer.parseInt(text6.getText()));
				AES aes =new AES(key,"4e5Wa71fYoT7MFE1");
				String src = "xml\\";
				byte[] encrypted = aes.encrypt(src+"test.xml");
				aes.save(encrypted, src+"2.xml");
				//Boolean[] lock=new Boolean[bztIP.size()];
				int number=0;
				try {
					Document doc=Xml_produce.Xml2Doc("xml\\test.xml");
					MedicalRecords upload=new MedicalRecords(preBlockIndex,  LocalDate.parse(text5.getText()), infoID, Integer.parseInt(text6.getText()), ID,text7.getText(), text11.getText(), doc);
					for(int i=0;i<bztIP.size();i++){
						Socket socket2 = new Socket(bztIP.get(i), config.getPORT());

						// 2.获取该Socket的输出流，用来向服务器发送信息
						OutputStream os = socket2.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						
						
						oos.writeByte(1);
						oos.writeObject(upload);
						oos.flush();
						
						InputStream inputStream = socket2.getInputStream();
						ObjectInputStream br=new ObjectInputStream(inputStream);
						//lock[i]=br.readBoolean();
						if(br.readBoolean()==true)
							number++;
						oos.close();
						br.close();
						socket2.close();
					}

				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				if(number<(bztIP.size()/2))
					break;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				}
				
				A.addPreBlockIndex(preBlockIndex);
				A.addInfoID();
				
				
			}
			if(event==submit2){//与服务端连接进行溯源
				Socket socket;
				int hospitalid;
				System.out.println("'"+text2.getText()+"'");
				if(text2.getText().equals(""))
					hospitalid=0;
				else 
					hospitalid=Integer.parseInt(text2.getText());
					
				Suyuan S=new Suyuan(preBlockIndex, ID, hospitalid, text1.getText(), text3.getText(), text4.getText());
				try {
					socket = new Socket(config.getIP_LIST().get(0), config.getPORT());
					OutputStream os = socket.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);

					oos.writeByte(6);
					oos.writeObject(S);



					oos.flush();
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream br=new ObjectInputStream(inputStream);

					try {
						MedicalRecords[] Med=(MedicalRecords[]) br.readObject();
						for(int i=0;i<Med.length;i++){
							String []rowValues = {String.valueOf(Med[i].getHospitalID()),Med[i].getSection(),String.valueOf(Med[i].getOperateTime())};
							tableModel.addRow(rowValues);
						}
					} catch (ClassNotFoundException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}







				} catch (UnknownHostException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				// 2.获取该Socket的输出流，用来向服务器发送信息






			}
			if(event==btn_zhanghu){
				panel_filework.removeAll();
				frame.remove(line);
				line.repaint();
				panel_filework.repaint();

				

				JLabel Label1 = new JLabel("选择要导入的用户资料:");
		        Label1.setBounds(150, 20, 200, 40);
		        Label1.setFont(new Font("微软雅黑",Font.PLAIN,18));
		        panel_filework.add(Label1);
		        
		        text1 = new JTextField();
		        text1.setBounds(150, 85, 400, 30);
		        text1.setFont(new Font("宋体", 1, 12));
		        panel_filework.add(text1);
		        
		        
		        fileworkscan_choose=new JButton(new ImageIcon("image_interface/浏览1.png"));
		        fileworkscan_choose.setRolloverIcon(new ImageIcon("image_interface/浏览2.png"));
		        fileworkscan_choose.setBounds(430, 25, 100, 50);
		        fileworkscan_choose.addActionListener(this);
		        panel_filework.add(fileworkscan_choose);
		        
		        
		        fileworkscan_read=new JButton(new ImageIcon("image_interface/读取1.png"));
		        fileworkscan_read.setRolloverIcon(new ImageIcon("image_interface/读取2.png"));
		        fileworkscan_read.setBounds(430, 150, 100, 50);
		        fileworkscan_read.addActionListener(this);
		        panel_filework.add(fileworkscan_read);
		        
		        read =new JTextArea();
				read.setEditable(true);
				read.setFont(new Font("微软雅黑",Font.PLAIN,18));
				JScrollPane scroll = new JScrollPane(read);
				//把定义的JTextArea放到JScrollPane里面去
				//分别设置水平和垂直滚动条自动出现
				scroll.setHorizontalScrollBarPolicy(
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.setVerticalScrollBarPolicy(
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scroll.setBounds(150, 205, 400, 150);
				panel_filework.add(scroll);
		        
		        
		        panel_filework.add(jfc);


			}
			if (event == fileworkscan_choose) {
				
				jfc.setFileSelectionMode(0);//设定只能选择到文件
		            int state=jfc.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
		            if(state==1){
		                return;//撤销则返回
		            }
		            else{
		                File f= jfc.getSelectedFile();//f为选择到的文件
		                text1.setText(f.getAbsolutePath());
				} 	
	        }
			if (event == fileworkscan_read) {
				A= PatientCard.getPatientCard(text1.getText());
				ID=A.getPatientID();
				infoID=A.getInfoID();
				preBlockIndex=A.getPreBlockIndex();
				name=A.getName();
				String out="name:"+name+"\n"+"ID:"+ID+"\n"+"infoID:"+infoID+infoID+"\n"+"preBlockIndex:"+preBlockIndex+"\n";
				read.setText(out);
			}
		}
	

	class MouseEventListener implements MouseInputListener{

		Point origin;
		//鼠标拖拽想要移动的目标组件
		Interface_Main face;

		public MouseEventListener(Interface_Main face) {
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
	class DrawPanel extends JPanel {
		//
		public void paint(Graphics g){
			super.paintComponent(g);
			g.setColor(Color.gray);
			g.drawLine(50,65,650,65);
			g.drawLine(50,125,650,125);
			g.drawLine(50,185,650,185);
			g.drawLine(50,387,650,387);
			g.drawLine(50,440,650,440);
		}
	}
	 
	 }


