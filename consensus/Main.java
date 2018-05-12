package consensus;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import block.Block;
import block.BlockService;
import block.MedicalRecords;
import config.Configuration;
import ip_net.My_ip;
import p2pPeer.Peer;

public class Main {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		

		String name = "1";
		MedicalRecords[] records = new MedicalRecords[5];
		for (int i = 0; i < records.length; i++) {
			records[i] = new MedicalRecords(1,1,1,null,"");
		}
		
		String[] s = new String[records.length]; // 设置发送信息
		for (int i = 0; i < s.length; i++) {
			s[i] = MD5Util.md5(records[i].toByteArray());
		}
		
		Byzantine_socket_info b_s_i = new Byzantine_socket_info();
		b_s_i.set_information(s);
		b_s_i.name = name;
		String my_ip="";
		try {
			my_ip=My_ip.getLocalHostLANAddress();
			System.out.println("my_ip:"+my_ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<String> ip_list = new ArrayList<String>(); 
		ip_list.add("169.254.140.106");
		ip_list.add("169.254.87.254");
		ip_list.add(my_ip);
		
		

		Server server = new Server(); // 开启服务端
		server.start();

		HashSet<ArrayList<String>> set = new HashSet<ArrayList<String>>(); // 发送端和接收端连接
		ArrayList<Byzantine_socket_info> bsi = new ArrayList<Byzantine_socket_info>();
		bsi.add(b_s_i);
		int[] cicle = new int[(Configuration.BYZANTINE_PEER_COUNT + 1) / 2];
		server.set_byzantine_hash(set, bsi, cicle);

		Byzantine bzt = new Byzantine();
		bzt.set(name, ip_list, set, bsi, cicle);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		bzt.cicle(); // 发送
		while (true) {
			if (cicle[cicle.length - 1] >= Configuration.BYZANTINE_PEER_COUNT - 1)
				break;
		}

		server.set_byzantine_block(bzt.result);

		MedicalRecords[] records_result = new MedicalRecords[bzt.result.size()];
		
		
		
		for (int i = 0, j = 0; i < s.length && j < records_result.length; i++) {
			if (bzt.result.contains(s[i])) {
				records_result[j] = records[i];			//设置共识后自己的数据
				j++;
			}
		}
		
		Block block=null;
		
		if(!ip_list.get(0).equals(my_ip)) {
			SendMedicalRecords_thread sendMedicalRecords = new SendMedicalRecords_thread(ip_list.get(0), Configuration.PORT, records_result);
			sendMedicalRecords.start();
			block=server.block;
			while(block!=null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			MedicalRecords[] data=block.data;
			if(data.length!=bzt.result.size()) {
				System.out.println("错误");
			}
			for(int i=0;i<data.length;i++) {
				if(!bzt.result.contains(MD5Util.md5(data[i].toByteArray()))) {
					System.out.println("错误");
				}
			}
			block.sign(name);
					//检查block
					//对block签名
			int next_ip=ip_list.indexOf(my_ip)+1;
			if(next_ip<ip_list.size()) {
				SendBlock_Thread sendblock=new SendBlock_Thread(ip_list.get(next_ip),Configuration.PORT,block);
				sendblock.start();
			}
		}else {
			for(int i=0;i<records_result.length;i++) {
				server.block_list.add(records_result[i]);
			}
			while (server.block_list.size() != bzt.result.size()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			HashSet result_block=server.block_list;
			System.out.println(result_block.size());
			BlockService bs=new BlockService();
			List dataList=new ArrayList<>(result_block);
			long index=0;									/**上一个区块高度****/
			block=bs.createBlock(dataList, bs.getblock(index));
			block.sign(name);
										//区块签名
			SendBlock_Thread sendblock=new SendBlock_Thread(ip_list.get(1),Configuration.PORT,block);
			sendblock.start();
		}
		
		if(ip_list.indexOf(my_ip)==ip_list.size()-1) {
			if(block!=null) {		//验证区块
				System.out.println("成功");
				Configuration.blockchain_high++;
			}
		}
		
		System.exit(0);
	}

}
