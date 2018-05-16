package FileFragmentation_delete;
import java.util.ArrayList;
import config.*;

import p2pPeer.*;
public class FileFragmentation_delete_control extends Thread{
	Peer peer;
	int blockchain_range=500;//定义一个检查删除范围，比如现在区块高度为1000，我们只检查1000-500的区块删除，因为0-500的大概率已经删除到稳定节点个数
	ArrayList<Integer> block_us;
	public FileFragmentation_delete_control(Peer peer,ArrayList<Integer> block_us){
		this.peer=peer;
		this.block_us=block_us;
	}
	
	
	public void run(){//循环一轮删除节点
		int count=21;
		//for( int count=Configuration.blockchain_high;count>(Configuration.blockchain_high-blockchain_range)&&count>0;count--){
			//Configuration.block_Fragmentationdelete_us.set(count, 1);
			Thread DeletetThread=new Thread(new FileFragmentation_delete(count, Configuration.port_Fragmentation_socket, Configuration.ip_list,block_us,peer));
			System.out.println("kaishishanchu"+count+"号区块");
			DeletetThread.start();
//    	while(true) {
//    		try {
//				Thread.sleep(5000);//12个小时启动一次
//			} catch (InterruptedException e) {
//				// TODO 自动生成的 catch 块
//				e.printStackTrace();
//			}
//    		//int count=21;
//    		//for( int count=Configuration.blockchain_high;count>(Configuration.blockchain_high-blockchain_range)&&count>0;count--){
//    			//Configuration.block_Fragmentationdelete_us.set(count, 1);
//    			//Thread DeletetThread=new Thread(new FileFragmentation_delete(count, Configuration.port_Fragmentation_socket, Configuration.ip_list, Configuration.block_Fragmentationdelete_us,peer));
//    			//System.out.println("kaishishanchu"+count+"号区块");
//    			//DeletetThread.start();
//    			
//    			//Configuration.block_Fragmentationdelete_us.set(count, 0);
//    		//}
//    	}
    }
	
	
	
	
}
