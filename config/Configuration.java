package config;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class Configuration {

	private String PUBLIC_ID = "everyone";
	private String PUBLIC_PASSWORD = "very-secret-password";
	private String PUBLIC_PIN = "secret-pin";
	private String PUBLICKEY_PATH ="./publickey/";
	private String PRIVATEKEY_PATH ="./privatekey/";
	private int BYZANTINE_PEER_COUNT =3;
	private int PORT =8000;
	private String BLOCKCHAIN_PATH ="./block/";
	private int BLOCK_DATA_MAX_NUM = 128;
	private int LEAST_EXIT =1;
	private int EXIT_ALL_TIME =10000;
	private int EXIT_ONLY_TIME =1000*3600*48*2;
	private String IP_LIST;
	private long BLOCKCHAIN_HIGH=21;//区块总高度
	private ArrayList<LocalDate> BLOCK_SCAESS_TIME =new ArrayList<LocalDate>();//存在区块的最新访问时间
	private int BLOCKCHAIN_RANGE=500;//定义一个检查删除范围，比如现在区块高度为1000，我们只检查1000-500的区块删除，因为0-500的大概率已经删除到稳定节点个数


	public Configuration(){
		Properties properties = new Properties();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("properties.conf"));
			properties.load(bufferedReader);

			PUBLIC_ID = properties.getProperty("PUBLIC_ID");
			PUBLIC_PASSWORD = properties.getProperty("PUBLIC_PASSWORD");
			PUBLIC_PIN = properties.getProperty("PUBLIC_PIN");
			PUBLICKEY_PATH = properties.getProperty("PUBLICKEY_PATH");
			PRIVATEKEY_PATH = properties.getProperty("PRIVATEKEY_PATH");
			BYZANTINE_PEER_COUNT = Integer.parseInt(properties.getProperty("BYZANTINE_PEER_COUNT"));
			PORT = Integer.parseInt(properties.getProperty("PORT"));
			LEAST_EXIT = Integer.parseInt(properties.getProperty("LEAST_EXIT"));
			EXIT_ALL_TIME = Integer.parseInt(properties.getProperty("EXIT_ALL_TIME"));
			EXIT_ONLY_TIME = Integer.parseInt(properties.getProperty("EXIT_ONLY_TIME"));
			IP_LIST = properties.getProperty("IP_LIST");
			BLOCKCHAIN_HIGH = Long.parseLong(properties.getProperty("BLOCKCHAIN_HIGH"));
			BLOCKCHAIN_RANGE = Integer.parseInt(properties.getProperty("BLOCKCHAIN_RANGE"));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR:properties.conf 配置文件未找到。");
			e.printStackTrace();
		}catch (IOException e){
			System.out.println("ERROR:配置文件加载异常。");
			e.printStackTrace();
		}
	}

	public String getPUBLIC_ID() {
		return PUBLIC_ID;
	}

	public String getPUBLIC_PASSWORD() {
		return PUBLIC_PASSWORD;
	}

	public String getPUBLIC_PIN() {
		return PUBLIC_PIN;
	}

	public ArrayList<LocalDate> getBLOCK_SCAESS_TIME() {
		return BLOCK_SCAESS_TIME;
	}

	public String getPUBLICKEY_PATH() {
		return PUBLICKEY_PATH;
	}

	public String getPRIVATEKEY_PATH() {
		return PRIVATEKEY_PATH;
	}

	public int getBLOCK_DATA_MAX_NUM() {
		return BLOCK_DATA_MAX_NUM;
	}

	public int getBYZANTINE_PEER_COUNT() {
		return BYZANTINE_PEER_COUNT;
	}

	public int getPORT() {
		return PORT;
	}

	public long getBLOCKCHAIN_HIGH() {
		return BLOCKCHAIN_HIGH;
	}

	public int getBLOCKCHAIN_RANGE() {
		return BLOCKCHAIN_RANGE;
	}

	public int getEXIT_ALL_TIME() {
		return EXIT_ALL_TIME;
	}

	public int getEXIT_ONLY_TIME() {
		return EXIT_ONLY_TIME;
	}

	public int getLEAST_EXIT() {
		return LEAST_EXIT;
	}

	public String getBLOCKCHAIN_PATH() {
		return BLOCKCHAIN_PATH;
	}

	public String[] getIP_LIST() {
		ArrayList<String> ipList = new ArrayList<String>();
		JSONParser parser=new JSONParser();
		try {
			Object obj = parser.parse(IP_LIST);
			JSONArray array = (JSONArray) obj;
			JSONObject js;
			for(int i = 0; i < array.size(); i++){
				js = (JSONObject) array.get(i);
				ipList.add(js.get(String.valueOf(i)).toString());
			}

			return ipList.toArray(new String[ipList.size()]);
		} catch (ParseException e) {
			System.out.println("ERROR:IP_LIST json 解析错误。");
			e.printStackTrace();
		}

		return null;
	}


	//不要忘记线程互斥


}