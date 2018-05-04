package FileFragmentation_delete;
import java.util.ArrayList;

import p2pPeer.*;
public class FileFragmentation_delete_control extends Thread{
	Peer peer;
	int blockchain_range=500;//定义一个检查删除范围，比如现在区块高度为1000，我们只检查1000-500的区块删除，因为0-500的大概率已经删除到稳定节点个数
	 
	public FileFragmentation_delete_control(Peer peer){
		this.peer=peer;
	}
	
	
	public void run(){//循环一轮删除节点
    	while(true) {
    		try {
				Thread.sleep(43200200);//12个小时启动一次
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
    		for( int count=Define.blockchain_high;count>(Define.blockchain_high-blockchain_range);count--){
    			//Define.block_Fragmentationdelete_us.set(count, 1);
    			Thread DeletetThread=new Thread(new FileFragmentation_delete(count, Define.port_Fragmentation_socket, Define.ip_list, Define.block_Fragmentationdelete_us,peer));
    			//Define.block_Fragmentationdelete_us.set(count, 0);
    		}
    	}
    }
	
	
	
	
}
