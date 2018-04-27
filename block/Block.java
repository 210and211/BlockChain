package block;

import java.io.Serializable;
import java.util.ArrayList;

import consensus.JavaMD5RSASign;

public class Block  implements Serializable{
    int index;
    int version;
    String fatherhash;
    String merkle_root;
    long   timestamp;
    int num;
    Node root;
    String[][] data;
    String[] sign;
    public Block(int index,String fatherhash,String merkle_root,int num,Node root,String[][] data) {
    	creatBlock(index,1,fatherhash,merkle_root,System.currentTimeMillis(),num,root,data);	//size很难计算"m树"
    }

	public void creatBlock(int index,int version,String fatherhash, String merkle_root, 
			long timestamp,int num, Node root,String[][] data) {
        this.index = index;
        this.version=version;
        this.fatherhash=fatherhash;
        this.merkle_root=merkle_root;
        this.timestamp = timestamp;
        this.num=num;
        this.root=root;
        this.data = data;
    }

    public String gethash() {
    	String s=index+version+fatherhash+merkle_root+timestamp+num;
    	if(sign!=null) {
	    	for (int i = 0; i < sign.length; i++) {
				s+=sign[i];
			}
    	}
        String hash=MD5.MD5_32(s);
        return hash;
    }
    void sign(String username) {
    	JavaMD5RSASign jmrs=new JavaMD5RSASign();
    	jmrs.setprivatekey(username);
    	String sign_info=jmrs.CreateSign(gethash().getBytes());
    	ArrayList<String> list=new ArrayList<>();
    	if(sign!=null) {
    		for (int i = 0; i < sign.length; i++) {
				list.add(sign[i]);
			}
    	}
    	list.add(sign_info);
    	list.toArray(sign);
    }
    
    boolean check_sign() {
    	JavaMD5RSASign jmrs=new JavaMD5RSASign();
    	String s=index+version+fatherhash+merkle_root+timestamp+num;
    	for (int i = 0; i < sign.length/2; i++) {
    		s+=sign[i*2];
			jmrs.setpublickey(sign[i*2]);
			if(!jmrs.VerifySign(s.getBytes(), sign[i*2+1]))
				return false;
			s+=sign[i*2+1];
		}
    	return true;
    }
    
    boolean equals(Block newblock) {
    	if(index==newblock.index&&version==newblock.version&&
    			fatherhash.equals(newblock.fatherhash)&&
    			merkle_root.equals(newblock.merkle_root)&&
    			timestamp==newblock.timestamp&&num==newblock.num&&
    			data.equals(newblock.data)&&sign.equals(newblock.sign)) {
    		return true;
    	}else {
    		return false;
    	}
    }
}

