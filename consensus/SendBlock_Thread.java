package consensus;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import block.Block;

public class SendBlock_Thread extends Thread {
	String ip;
	int port;
	// Byzantine_socket_info bsi=null;
	Block block=null;

	SendBlock_Thread(String ip, int port, Block block) {
		this.ip = ip;
		this.port = port;
		this.block = block;

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
			oos.writeByte(-2);
			oos.writeObject(block);
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