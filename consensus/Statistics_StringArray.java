package consensus;

import java.util.ArrayList;

public class Statistics_StringArray {
	
	ArrayList<String> String=new ArrayList();//用于存放字符串
//	int[] nums=new int [1000];//用于统计字符串的出现频率(int 数组类型)
	ArrayList<Integer> num=new ArrayList();//同上（arraylist 类型）
	static public void main(String[] args){
		ArrayList<String>[] list=new ArrayList[2];
		for (int i = 0; i < list.length; i++) {
			list[i]=new ArrayList<String>();
		}
		list[0].add("abc");
		list[0].add("abcd");
		list[0].add("abcde");
		list[1].add("abc");
		list[1].add("asd");
		Statistics_StringArray A=new  Statistics_StringArray();
		A.count_array(list);
	//	A.return_nums();
		A.return_String();
		
	}
	
	void count_array(ArrayList[] str){
	
	
		for(int i=0;i<str.length;i++){
			for(int j=0;j<str[i].size();j++){
			if(String.size()==0){
				String.add((String)str[i].get(j));
	//			nums[0]++;
				num.add(1);
			}
			else {
				int z=0;
				for(;z<String.size();z++){
			    	if(str[i].get(j).equals(String.get(z))){
			    		int temporary=num.get(z);
			    		num.set(z,temporary+1);
	//		    		nums[z]++;
			    		break;
			    	}
			    }
			    if(z==(String.size())){
			      
			    	String.add((String) str[i].get(j));
			    	num.add(1);
	//		    	nums[z]++;
			    }
				
			}
		}
	}

	
}


	ArrayList<String> return_String(){
		return String;
	}

	ArrayList<Integer> return_num(){
		return num;
	}


}