package org.hive2hive.examples;

import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;





public class FileFragmentation_delete extends Thread {//在开始删除操作时得进行block_Fragmentationdelete_us的标识为1 结束时赋0
	ArrayList<Integer> block_Fragmentationdelete_us;//哪个区块正在进行删除操作 这个需要在节点创造的同时定义一个相同的 然后给此变量赋节点变量的值
	ArrayList<String> ip_list;//所有节点的ip地址
	int blockchain_high;//区块高度
	int port_Fragmentation_socket;//端口号
	
	public  int block_Fragmentationdelete_using=1;//记录该区块分片删除操作是否在其他节点上进行
	public  int[] exits=new int [1000];//记录区块是否存在 与ip_list一起使用 3为连接异常 0为该节点不存在此文件 1为该节点存在
	ArrayList<String> ip_list_existing;//该文件存在的ip地址
	int peer_number;//节点数
	int peer_exist_number=0;//拥有区块的节点数
	ServerSocket serverSocket;
	//Boolean flag=true;
	
//	public 
	FileFragmentation_delete(int blockchain_high,int port_Fragmentation_socket,ArrayList<String> IP,ArrayList<Integer> block_Fragmentationdelete_us){
		try {
			IP.remove(InetAddress.getLocalHost().getHostAddress());//移除本机IP
			this.ip_list=IP;
			this.blockchain_high=blockchain_high;
			this.port_Fragmentation_socket=port_Fragmentation_socket;
			this.block_Fragmentationdelete_us=block_Fragmentationdelete_us;
			peer_number=ip_list.size();
			serverSocket = new ServerSocket(port_Fragmentation_socket);//端口号
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
		while(block_Fragmentationdelete_using==1){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			block_Fragmentationdelete_using=0;
			for(int i=0;i<peer_number;i++){
				Thread socketThread=new Thread(new QuestThread(ip_list.get(i),blockchain_high,i));
				socketThread.start();
			}
		}
		
		
		for(int i=0;i<peer_number;i++){
			Thread socketThread=new Thread(new ClientThread(ip_list.get(i),blockchain_high,i));
			socketThread.start();
		}
		
		
		this.delete();
		
		
	}
	
	
	
	
	
	class ClientThread extends Thread{
		String ip;
		String blockchain_high;//区块高度
		int count;
		ClientThread(String ip,int blockchain_high,int count){
			this.ip=ip;
			this.blockchain_high=String.valueOf(blockchain_high);
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
	            oos.writeBytes(blockchain_high);
	            oos.flush();
	            oos.close();
	            os.close();
	            InputStream inputStream = socket.getInputStream();
	            ObjectInputStream br=new ObjectInputStream(inputStream);
	            String exit=(String)br.readObject();
	            if(exit=="0"){
	            	exits[count]=0;
	            }
	            else if (exit=="1"){
	            	exits[count]=1;
	            	peer_exist_number++;
	            	ip_list_existing.add(ip_list.get(count));
	            }
	            socket.close();
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
	}
	
	
	
	
	class QuestThread extends Thread{
		String ip;
		String blockchain_high;//区块高度
		int count;
		QuestThread(String ip,int blockchain_high,int count){
			this.ip=ip;
			this.blockchain_high=String.valueOf(blockchain_high);
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
	            oos.writeBytes(blockchain_high);
	            oos.flush();
	            oos.close();
	            os.close();
	            InputStream inputStream = socket.getInputStream();
	            ObjectInputStream br=new ObjectInputStream(inputStream);
	            String exit=(String)br.readObject();
	            if (exit=="1"){
	            	block_Fragmentationdelete_using=1;
	            }
	            socket.close();
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
	}
	
	
	public void delete(){
		//根据时间和节点个数进行删除
		//需要知道该文件的创造时间和该文件在p2p网络中所拥有的节点个数
		int multiple=0;//计算概率时的时间倍数
		int number=this.ip_list.size()-1;
		int exit_number=this.peer_exist_number;
		int exit_time=0;//读取区块的存在时间
		if(exit_time>Define.exit_all_time&&exit_time<Define.exit_only_time){
			double Probability=(exit_number-Define.least_exit)/(number-Define.least_exit)*(exit_time-Define.exit_all_time)/multiple;
			//这个Probability是一个%的负数 来表示概率
			double floatNumber = Math.random();
			if(floatNumber<Probability){
			//删除该节点
		}
		}
		else if(exit_time>Define.exit_only_time){
			if((exit_number-Define.least_exit)>0){
				//删除该节点
			}
		}
	}
	
	 
	
}
