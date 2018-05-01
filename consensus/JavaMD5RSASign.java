package consensus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;

public class JavaMD5RSASign extends Configuration{
	
	PrivateKey privateKey;
	PublicKey publicKey;
	

	void createkey() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);  // 初始化长度
			KeyPair keyPair = keyPairGenerator.generateKeyPair();  
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();  // 生成私钥
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());  //私钥转换成pkcs8格式
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec); // 用key工厂对象生成私钥
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	void setkey(PublicKey publicKey,PrivateKey privateKey) {
		this.privateKey=privateKey;
		this.publicKey=publicKey;

	}
	
	public void setpublickey(String username) {
		ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(publickeypath+username+".key"));
            publicKey =(PublicKey) ois.readObject();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}
	public void setprivatekey(String username) {
		ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(privatekeypath+username+".key"));
            privateKey =(PrivateKey) ois.readObject();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}	
	
	
	public String CreateSign(byte[] by) {
		try {
			//用私钥进行签名
			Signature signature = Signature.getInstance("MD5withRSA");  //  md5 RSA签名对象
			signature.initSign(privateKey);  //初始化签名
			signature.update(by);
			
			byte[] result = signature.sign();  //对消息进行签名
			Base64.Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(result);
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		} 
	}
	Boolean privatekey_store(String username) {
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(privatekeypath+username+".key");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(privateKey);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
		
	}
	Boolean publickey_store(String username) {
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(publickeypath+username+".key");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(publicKey);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
		
	}
	public Boolean VerifySign(byte[] info,String sign) {
		Signature signature;
		try {
			signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(publicKey);
			signature.update(info);
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] by=decoder.decode(sign);
			boolean verify = signature.verify(by);
			return verify;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}  //  md5 RSA签名对象
		
	}
	
	
	
	public static void main(String[] args) {
		JavaMD5RSASign rsasign=new JavaMD5RSASign();
//		rsasign.createkey();
		rsasign.setpublickey("1");
		rsasign.setprivatekey("1");
		String str="666666666";
		String sign=rsasign.CreateSign(str.getBytes());
		System.out.println(rsasign.VerifySign(str.getBytes(),sign));
//		rsasign.publickey_store("3");
//		rsasign.privatekey_store("3");
		
	}
}

