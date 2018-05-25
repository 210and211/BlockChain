package cryptogram;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES {
	
	// 共通鍵
	private   String ENCRYPTION_KEY = "RwcmlVpg";
	private   String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";
//	public static
	public AES(String ENCRYPTION_KEY,String ENCRYPTION_IV){
		this.ENCRYPTION_KEY=ENCRYPTION_IV;
		if(ENCRYPTION_IV.length()==16) {		//初始向量必须16长
			this.ENCRYPTION_IV=ENCRYPTION_IV;
		}
	}
	public byte[] encrypt(String path) {
		return encrypt(getbytes(path));
	}
	private byte[] encrypt(byte[] m) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
			return cipher.doFinal(m);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public  byte[] decrypt(String path) {
		return decrypt(getbytes(path));
	}
	private  byte[] decrypt(byte[] s) {
		String decrypted = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
			return cipher.doFinal(s);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	AlgorithmParameterSpec makeIv() {
		try {
			return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
 	Key makeKey() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
			return new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public  byte[] getbytes(String path) {
		File file=new File(path);
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			byte[] bytIn = new byte[(int) file.length()];  
			bis.read(bytIn);  
			bis.close();
			return bytIn;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	public  boolean save(byte[] data,String path) {
		  
        try {
        	File file = new File(path);  
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
            bos.write(data);
			bos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return false;
	}
	
	public static void main(String[] args) {
		AES aes =new AES("key","4e5Wa71fYoT7MFE1");
		String src = "C:\\Users\\chen\\Desktop\\加密文件\\";
		byte[] encrypted = aes.encrypt(src+"1.xml");
		aes.save(encrypted, src+"2.xml");
		
		byte[] decrypted = aes.decrypt(src+"2.xml");
		aes.save(decrypted, src+"3.xml");

	}
	
	
}
