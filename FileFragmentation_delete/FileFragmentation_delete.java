package FileFragmentation_delete;

import block.Block;
import block.BlockService;
import config.Configuration;
import ip_net.My_ip;
import p2pPeer.Peer;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;





public class FileFragmentation_delete extends Thread {//在开始删除操作时得进行block_Fragmentationdelete_us的标识为1 结束时赋0
	Peer peer;
	public static int[] block_Fragmentationdelete_us;//哪个区块正在进行删除操作 这个需要在节点创造的同时定义一个相同的 然后给此变量赋节点变量的值
	ArrayList<String> ip_list=new ArrayList<String>();//所有节点的ip地址
	long blockchain_high;//区块高度
	int port_Fragmentation_socket;//端口号

	public  int block_Fragmentationdelete_using=1;//记录该区块分片删除操作是否在其他节点上进行
	public  int[] exits=new int [1000];//记录区块是否存在 与ip_list一起使用 3为连接异常 0为该节点不存在此文件 1为该节点存在
	public  int[] finish=new int [1000];//用来在socket通信结束前锁住程序进程
	Configuration config = new Configuration();
	int peer_number;//节点数
	int peer_exist_number=1;//拥有区块的节点数


	FileFragmentation_delete(int blockchain_high,int port_Fragmentation_socket,String[] IP,int[] block_Fragmentationdelete_us,Peer peer){
		try {

			for(int i=0;i<IP.length;i++){
				ip_list.add(IP[i]);
			}
			ip_list.remove(My_ip.getLocalHostLANAddress());//移除本机IP
			this.peer=peer;

			this.blockchain_high=blockchain_high;
			this.port_Fragmentation_socket=port_Fragmentation_socket;
			FileFragmentation_delete.block_Fragmentationdelete_us=block_Fragmentationdelete_us;
			peer_number=ip_list.size();
			System.out.println("服务器启动，等待客户端的连接。。。");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		for(int i=0;i<peer_number;i++){
			exits[i]=3;
		}

	}

	public void run(){
		File fileAtPeer1 = new File(peer.getFileAgent().getRoot(), String.valueOf(blockchain_high)+".block");
		if(fileAtPeer1.exists()){
			while(block_Fragmentationdelete_using==1){//询问当前区块是否在其他节点在删除操作 如果是则休眠再询问
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				block_Fragmentationdelete_using=0;

				for(int i=0;i<peer_number;i++){
					finish[i]=0;
				}

				for(int i=0;i<peer_number;i++){
					Thread socketThread=new Thread(new QuestThread(ip_list.get(i),blockchain_high,i));
					socketThread.start();

				}

				for(int i=0;i<peer_number;i++){
					while(finish[i]==0){
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
			}
			block_Fragmentationdelete_us[(int)blockchain_high%500]=1;//区块删除操作锁 上锁

			for(int i=0;i<peer_number;i++){
				finish[i]=0;
			}

			for(int i=0;i<peer_number;i++){//广播询问节点区块是否存在
				Thread socketThread=new Thread(new ClientThread(ip_list.get(i),blockchain_high,i));
				socketThread.start();
			}

			for(int i=0;i<peer_number;i++){
				while(finish[i]==0){
					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			}


			this.delete();//开始删除
		}
		else {
			//System.out.println("");
		}

		block_Fragmentationdelete_us[(int)blockchain_high%500]=0;//解锁
	}





	class ClientThread extends Thread{//询问节点区块是否存在的进程
		String ip;
		long blockchain_high;//区块高度
		int count;
		ClientThread(String ip,long blockchain_high,int count){
			this.ip=ip;
			this.blockchain_high=blockchain_high;
			this.count=count;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				Socket socket = new Socket(ip, port_Fragmentation_socket);

				// 2.获取该Socket的输出流，用来向服务器发送信息
				OutputStream os = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);

				oos.writeByte(3);
				oos.writeLong(blockchain_high);
				oos.flush();


				InputStream inputStream = socket.getInputStream();
				ObjectInputStream br=new ObjectInputStream(inputStream);
				int exit=br.readInt();
				if(exit==0){
					exits[count]=0;
				}
				else if (exit==1){
					exits[count]=1;
					peer_exist_number++;
					//ip_list_existing.add(ip_list.get(count));
				}

				finish[count]=1;
				oos.close();
				os.close();
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}




	class QuestThread extends Thread{//询问其他节点是否在进行区块删除操作的进程
		String ip;
		long blockchain_high;//区块高度
		int count;
		QuestThread(String ip,long blockchain_high,int count){
			this.ip=ip;
			this.blockchain_high=blockchain_high;
			this.count=count;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				Socket socket = new Socket(ip,port_Fragmentation_socket);

				// 2.获取该Socket的输出流，用来向服务器发送信息
				OutputStream os = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);

				oos.writeByte(4);
				oos.writeLong(blockchain_high);
				oos.flush();

				InputStream inputStream = socket.getInputStream();
				ObjectInputStream br=new ObjectInputStream(inputStream);
				int exit=br.readInt();
				if (exit==1){
					block_Fragmentationdelete_using=1;
				}


				finish[count]=1;
				oos.close();
				os.close();
				socket.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void delete(){
		//根据时间和节点个数进行删除
		//需要知道该文件的创造时间和该文件在p2p网络中所拥有的个数

		//!!!!!!!!!!!!!!!!!!!! 记录区块最新访问时间没对接
		//Configuration.exit_only_time.set((int) blockchain_high, LocalDate.now());//访问日期代码 添加到访问函数里面
		LocalDate today = LocalDate.now();
		int multiple=0;//计算概率时的时间倍数
		int number=this.peer_number+1;
		int exit_number=this.peer_exist_number;

		BlockService blockservice=new BlockService(peer);

		Block block=blockservice.getblock(blockchain_high);
		long exit_time=System.currentTimeMillis()-block.timestamp;
		//if(ChronoUnit.DAYS.between(today,Configuration.block_saccessTime.get((int) blockchain_high))>5){
		if(true){
			if(exit_time>config.getEXIT_ALL_TIME()&&exit_time<config.getEXIT_ONLY_TIME()){
				double Probability=1.0*(exit_number-config.getLEAST_EXIT())/(number-config.getLEAST_EXIT())*(exit_time-config.getEXIT_ALL_TIME())/(config.getEXIT_ONLY_TIME()-config.getEXIT_ALL_TIME());
				//这个Probability是一个%的复数 来表示概率
				System.out.println("mxdsb"+blockchain_high+"  "+Probability);
				double floatNumber = Math.random();
				if(floatNumber<Probability){
					//删除该区块
					File fileAtPeer1 = new File(peer.getFileAgent().getRoot(), String.valueOf(blockchain_high)+".block");//文件名还需修改
					fileAtPeer1.delete();

				}
			}
			else if(exit_time>config.getEXIT_ONLY_TIME()){
				if((exit_number-config.getLEAST_EXIT())>0){
					//删除该区块
					File fileAtPeer1 = new File(peer.getFileAgent().getRoot(), String.valueOf(blockchain_high)+".block");//文件名还需修改
					fileAtPeer1.delete();
				}
			}
		}

	}






}