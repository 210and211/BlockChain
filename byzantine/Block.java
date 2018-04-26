package consensus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Block implements Serializable{

	String s;
	String hash;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}
	public byte[] toByteArray () {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();      
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			bytes = bos.toByteArray ();
			oos.close();         
			bos.close();
		} catch (IOException ex) {        
			ex.printStackTrace();   
		}      
		return bytes;
	}   
}
