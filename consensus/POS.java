package consensus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class POS {
	
	List<String> get_node(List<String> list,int select_cout,long seed){
		Random random=new Random(seed);
		Collections.shuffle(list,random);
		
		return list.subList(0, select_cout);
	}
	
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		 List<String> list = new ArrayList<>(Arrays.asList("AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH"));
		 POS pos=new POS();
		 System.out.println(pos.get_node(list, 5,1));
		 
		 
	}

}
