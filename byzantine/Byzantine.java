package consensus;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Byzantine{
	String myname;;
	ArrayList<String> ip_list;
	int[] cicle;
//	Byte cicle_num=0;
	HashSet<ArrayList<String>> set;
	ArrayList<Byzantine_socket_info> bsi;
	HashSet<String> result=new HashSet<>();


	void set(String myname,ArrayList<String> ip_list,HashSet<ArrayList<String>> set,
			ArrayList<Byzantine_socket_info> bsi,int[] cicle){
		this.ip_list=ip_list;
		this.myname=myname;
		this.set=set;
		this.bsi=bsi;
		this.cicle=cicle;
	}
	
	
	void send(byte cicle_num) {
		for (int i = 0; i < bsi.size(); i++) {
			bsi.get(i).list=bsi.get(i).Sign(myname);
		}
		
		for (int i = 0; i < Configuration.byzantine_peer_count-1; i++) {
			Byzantine_socket_info[] info=new Byzantine_socket_info[bsi.size()];
			bsi.toArray(info);
			Thread socketThread=new Thread(new ClientThread(ip_list.get(i),Configuration.port,info,cicle_num));
			socketThread.start();
		}
	}
	void cicle() {
		for (int i = 0; i < bsi.size(); i++) {
			set.add(bsi.get(i).get_info());
		}
		for (int i = 0; i<(Configuration.byzantine_peer_count+1)/2; i++) {
		//	cicle_num=(byte) i;
			System.out.println("send"+i);
			send((byte) i);
			while(true) {
				try {
					if(cicle[i]>=Configuration.byzantine_peer_count-1) {
						break;
					}
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}

		Statistics_StringArray sa=new Statistics_StringArray();
		ArrayList<String>[] list=new ArrayList[bsi.size()];
		Iterator<ArrayList<String>> temp = set.iterator();
		
		for (int i = 0; i < list.length; i++) {
			ArrayList<String> st = temp.next();
			st.remove(0);
			list[i]=st;
		}
		sa.count_array(list);
		
		
		for (int j = 0; j < sa.return_num().size(); j++) {
			if((int)sa.return_num().get(j)>=(Configuration.byzantine_peer_count+1)/2) {
				result.add(sa.return_String().get(j));
			}
		}

	}
	class ClientThread extends Thread{
		String ip;
		int port;
	//	Byzantine_socket_info bsi=null;
		Byzantine_socket_info[] listbsi=null;
		byte cicle_num;
		ClientThread(String ip,int port,Byzantine_socket_info[] listbsi,byte cicle_num){
			this.ip=ip;
			this.port=port;
			this.listbsi=listbsi;
			this.cicle_num=cicle_num;
		}
		public void run() {
	        // TODO Auto-generated method stub
			try {
	            Socket socket = new Socket(ip, port);
	            
	            // 2.获取该Socket的输出流，用来向服务器发送信息
	            OutputStream os = socket.getOutputStream();
	            ObjectOutputStream oos = new ObjectOutputStream(os);
	            oos.writeByte(2);
	            oos.writeByte(cicle_num);
	            oos.writeObject(listbsi);
	            oos.flush();
	            oos.close();
	            os.close();
	            socket.close();
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
	}
	
}
