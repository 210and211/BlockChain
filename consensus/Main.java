package consensus;

import config.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String name="3";
		String[] s= {"a","c","e"};					//设置发送信息
		Byzantine_socket_info b_s_i=new Byzantine_socket_info();
		b_s_i.set_information(s);
		b_s_i.name=name;

		ArrayList<String> ip_list=new ArrayList<String>();	//设置对方IP
		ip_list.add("169.254.48.49");
		ip_list.add("169.254.87.254");

		Server server=new Server();		//开启服务端
		server.start();
		
		HashSet<ArrayList<String>> set=new HashSet<ArrayList<String>>();	//发送端和接收端连接
		ArrayList<Byzantine_socket_info> bsi =new ArrayList<Byzantine_socket_info>();
		bsi.add(b_s_i);
		int[] cicle=new int[(Configuration.BYZANTINE_PEER_COUNT +1)/2];
		server.set_byzantine(set, bsi, cicle);
		
		Byzantine bzt=new Byzantine();
		bzt.set(name, ip_list, set, bsi, cicle);
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		bzt.cicle();			//发送
		
		Iterator<String> iterator = bzt.result.iterator();
		for (int i = 0; i <bzt.result.size(); i++) {
			System.out.println(iterator.next());
		}

	
		
	}

}
