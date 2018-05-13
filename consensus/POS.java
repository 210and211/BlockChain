package consensus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class POS {
	
	ArrayList<String> get_node(ArrayList<String> list,int select_cout,long seed){
		Random random=new Random(seed);
		Collections.shuffle(list,random);
		
		return new ArrayList(list.subList(0, select_cout));
	}
	ArrayList<String> get_node(ArrayList<String> list,int select_cout,String seed_str){
	    long seed = 0;  
	    for (int ix = 0; ix < 8; ++ix) {  
	        seed <<= 8;  
	        seed |= (seed_str.getBytes()[ix] & 0xff);  
	    }  
		Random random=new Random(seed);
		Collections.shuffle(list,random);
		
		return new ArrayList(list.subList(0, select_cout));
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ArrayList<String> list = new ArrayList<>(Arrays.asList("AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH"));
		 POS pos=new POS();
		 System.out.println(pos.get_node(list, 5,1));
		 
		 
	}

}
