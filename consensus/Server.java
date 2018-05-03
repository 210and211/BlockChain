package consensus;

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


public class Server extends Thread{
	HashSet<ArrayList<String>> set;
	ArrayList<Byzantine_socket_info> bsi;
	int[] cicle;
	@Override
	public void run(){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Configuration.port);
			while(true){
				Socket socket=serverSocket.accept();
				ServerSocketThread sht=new ServerSocketThread(socket);
				sht.start();
				InetAddress inetAddress = socket.getInetAddress();
				System.out.println("当前客户端的IP地址是："+inetAddress.getHostAddress());
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	void set_byzantine(HashSet<ArrayList<String>> set,
			ArrayList<Byzantine_socket_info> bsi,int[] cicle) {
		this.set=set;
		this.bsi=bsi;
		this.cicle=cicle;
	}
	class ServerSocketThread extends Thread{
	    Socket socket=null;
	   
	    ServerSocketThread(Socket socket) {
	        this.socket = socket;
	    }
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	    	ArrayList<Integer> block_Fragmentationdelete_us;//这个值必须与peer里面的绑定 因为这个变量值时刻在变化
	        OutputStream os = null;
	        PrintWriter pw = null;
	        try {
	            InputStream is = socket.getInputStream();
	            ObjectInputStream ois=new ObjectInputStream(is);
	            //readObject()方法必须保证服务端和客户端的User包名一致，要不然会出现找不到类的错误
	            Byte mark1=ois.readByte();
	            
	            if(mark1==2) {
	            	Byte mark2=ois.readByte();
	            	cicle[mark2]++;
	            	System.out.println(cicle[mark2]+" "+mark2);
	            	Byzantine_socket_info[] info=(Byzantine_socket_info[]) ois.readObject();
	                for (int i = 0; i < info.length; i++) {
	                	if(info[i].verifySign()) {
	    	            	if(set.add(info[i].get_info()))
	    	            		bsi.add(info[i]);          		
	    	            	
	    	            }	
	    			}
	            }else if(mark1==3) {
	            	String s=(String) ois.readObject();
	            	int count=Integer.valueOf(s).intValue();
	            	int exit_blockchain = 0;//区块是否存在 缺省为不存在
	            	////此处写一个确认区块是否存在的函数返回值对exit_blockchain赋值有1 没有0
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
	            	if(block_Fragmentationdelete_us.get(count)==1){
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
	        }finally{
	            try{
	                if(pw!=null){
	                    pw.close();
	                }
	                if(os!=null){
	                    os.close();
	                }
	                if(socket!=null){
	                    socket.close();
	                }
	            }catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	            }

	        }
	    }
	    
	} 

}