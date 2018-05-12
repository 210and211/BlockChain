package consensus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import block.Block;
import block.MedicalRecords;
import config.Configuration;
import p2pPeer.*;


public class Server extends Thread {
	HashSet<ArrayList<String>> set;
	ArrayList<Byzantine_socket_info> bsi;
	int[] cicle;
	HashSet<MedicalRecords> block_list = new HashSet<MedicalRecords>();
	HashSet<String> hash_list = null;
	Block block=null;
	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Configuration.PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				ServerSocketThread sht = new ServerSocketThread(socket);
				sht.start();
				InetAddress inetAddress = socket.getInetAddress();
				System.out.println("当前客户端的IP地址是：" + inetAddress.getHostAddress());
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	void set_byzantine_hash(HashSet<ArrayList<String>> set, ArrayList<Byzantine_socket_info> bsi, int[] cicle) {
		this.set = set;
		this.bsi = bsi;
		this.cicle = cicle;
	}

	void set_byzantine_block(HashSet<String> hash_list) {
		this.hash_list = hash_list;
	}

	class ServerSocketThread extends Thread {
		Socket socket=null;
	    Peer peer;//添加一个peer变量  传入参数为当前结点
	    ServerSocketThread(Socket socket) {
	        this.socket = socket;
	    }
	    public void peer_value(Peer peer){
	    	this.peer=peer;
	    }

		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			OutputStream os = null;
			PrintWriter pw = null;
			try {
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				// readObject()方法必须保证服务端和客户端的User包名一致，要不然会出现找不到类的错误
				Byte mark1 = ois.readByte();
				
				if (mark1 == 1) {
					MedicalRecords medicalRecords = (MedicalRecords) ois.readObject();
					//发给接收方
					
					
				} else if (mark1 == 2) {
					Byte mark2 = ois.readByte();
					if (mark2 == -1) {
						MedicalRecords[] list = (MedicalRecords[]) ois.readObject();
						while (hash_list == null) {
							Thread.sleep(100);
						}
						for (int i = 0; i < list.length; i++) {
							if (hash_list.contains(MD5Util.md5(list[i].toByteArray()))) {
								block_list.add(list[i]);
							}
						}
					} else if(mark2 == -2) {
						block=(Block) ois.readObject();
					}else if (mark2 < cicle.length) {
						cicle[mark2]++;
						System.out.println(cicle[mark2] + " " + mark2);
						Byzantine_socket_info[] info = (Byzantine_socket_info[]) ois.readObject();
						for (int i = 0; i < info.length; i++) {
							if (info[i].verifySign()) {
								if (set.add(info[i].get_info())) {
									bsi.add(info[i]);
								}
							}
						}
					}
				} else if(mark1==3) {
	            	String s=(String) ois.readObject();
	            	int count=Integer.valueOf(s).intValue();
	            	int exit_blockchain = 0;//区块是否存在 缺省为不存在
	            	File fileAtPeer1 = new File(peer.getFileAgent().getRoot(), String.valueOf(Configuration.blockchain_high)+".block");
	            	if(fileAtPeer1.exists()){
	            		exit_blockchain = 1;
	            	}
	            	////此处写一个确认区块是否存在的函数返回值对exit_blockchain赋值有1 没有0 需要传参数peer
	            	os = socket.getOutputStream();
		            ObjectOutputStream oos = new ObjectOutputStream(os);
		            oos.writeByte(exit_blockchain);
		            oos.flush();
		            oos.close();
		            os.close();
	            }
	            else if(mark1==4){
	            	int using_blockchain=0;//区块删除是否在使用 缺省为不在
	            	String s=(String) ois.readObject();
	            	int count=Integer.valueOf(s).intValue();
	            	if(Configuration.block_Fragmentationdelete_us.get(count)==1){
	            		using_blockchain=1;
	            	}
	            	os = socket.getOutputStream();
		            ObjectOutputStream oos = new ObjectOutputStream(os);
		            oos.writeByte(using_blockchain);
		            oos.flush();
		            oos.close();
		            os.close();
	            }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (pw != null) {
						pw.close();
					}
					if (os != null) {
						os.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
