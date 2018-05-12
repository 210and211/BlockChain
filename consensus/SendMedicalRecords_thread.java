package consensus;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import block.MedicalRecords;

public class SendMedicalRecords_thread extends Thread {
	String ip;
	int port;
	// Byzantine_socket_info bsi=null;
	MedicalRecords[] listRecord = null;

	SendMedicalRecords_thread(String ip, int port, MedicalRecords[] listRecord) {
		this.ip = ip;
		this.port = port;
		this.listRecord = listRecord;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(ip, port);

			// 2.获取该Socket的输出流，用来向服务器发送信息
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeByte(2);
			oos.writeByte(-1);
			oos.writeObject(listRecord);
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