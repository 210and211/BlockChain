package general_hospital;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import block.BlockService;
import block.MedicalRecords;
import config.Configuration;
import consensus.POS;

public class SendMedicalRecord_thread extends Thread{
	String ip;
	int port;
	// Byzantine_socket_info bsi=null;
	MedicalRecords Record = null;

	SendMedicalRecord_thread(String ip, int port, MedicalRecords Record) {
		this.ip = ip;
		this.port = port;
		this.Record = Record;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(ip, port);

			// 2.获取该Socket的输出流，用来向服务器发送信息
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeByte(1);
//
			
		//	oos.writeByte(-1);
			oos.writeObject(Record);
			oos.flush();
			oos.close();
			os.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		list=(ArrayList<String>) Configuration.ip_list.clone();
		BlockService bs=new BlockService();
		ArrayList<String> ip_list=new POS().get_node(list, 
				Configuration.BYZANTINE_PEER_COUNT, 
				bs.getblock(Configuration.blockchain_high).gethash());
		MedicalRecords record = new MedicalRecords(1,1,1,null,"");
		
		for(int i=0;i<ip_list.size();i++) {
			new SendMedicalRecord_thread(ip_list.get(i),Configuration.PORT,record).start();
		}
	}

}
