package config;

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
}
