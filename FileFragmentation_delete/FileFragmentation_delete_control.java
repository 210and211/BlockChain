package FileFragmentation_delete;
import config.*;

import p2pPeer.*;
public class FileFragmentation_delete_control extends Thread{
	Peer peer;
	int[] block_us;
	public FileFragmentation_delete_control(Peer peer,int[] block_us){
		this.peer=peer;
		this.block_us=block_us;
	}


	public void run(){//循环一轮删除节点
		Configuration config = new Configuration();
		for( long count=config.getBLOCKCHAIN_HIGH();count>(config.getBLOCKCHAIN_HIGH()-config.getBLOCKCHAIN_RANGE())&&count>0;count--){
			Thread DeletetThread=new Thread(new FileFragmentation_delete(count, config.getPORT(), config.getIP_LIST(),block_us,peer));
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
//    			//Thread DeletetThread=new Thread(new FileFragmentation_delete(count, Configuration.port_Fragmentation_socket, Configuration.ip_list, Configuration.block_Fragmentationdelete_us,peer));
//    			//System.out.println("kaishishanchu"+count+"号区块");
//    			//DeletetThread.start();
//    		//}
		}
	}




}