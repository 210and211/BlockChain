package config;

import java.time.LocalDate;
import java.util.ArrayList;

public class Configuration {

	public final static String PUBLIC_ID = "everyone";
	public final static String PUBLIC_PASSWORD = "very-secret-password";
	public final static String PUBLIC_PIN = "secret-pin";
	
	public final static String PUBLICKEY_PATH ="./publickey/";
	public final static String PRIVATEKEY_PATH ="./privatekey/";
	public final static int BYZANTINE_PEER_COUNT =3;
	public final static int PORT =8000;
	public final static String BLOCKCHAINPATH ="./block/";
	public final static int BLOCK_DATA_MAX_NUM = 128;
	public final static int least_exit=1;
	public final static int exit_all_time=10000;
	public final static int exit_only_time=1000*3600*48;
	public final static long create_block_time=1*60*1000;
	public final static int port_Fragmentation_socket=8000;//分片socket通信端口号
	public final static String[] ip_list= {"192.168.1.104","192.168.1.107","192.168.1.106"};
	public static int blockchain_high=21;//区块总高度
	public static ArrayList<LocalDate> block_saccessTime=new ArrayList<LocalDate>();//存在区块的最新访问时间
	public static int blockchain_range=500;//定义一个检查删除范围，比如现在区块高度为1000，我们只检查1000-500的区块删除，因为0-500的大概率已经删除到稳定节点个数
	
	
}