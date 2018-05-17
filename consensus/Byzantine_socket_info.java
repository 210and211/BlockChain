package consensus;

import java.io.Serializable;
import java.util.ArrayList;



public class Byzantine_socket_info implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5881884990998086297L;
	ArrayList<String[]> list=new ArrayList<String[]>();
	String name;			//来源
	ArrayList<String> get_info(){
		ArrayList<String> result=new ArrayList<String>();
		result.add(name);
		for (int i = 0; i < list.size(); i++) {
			result.add(list.get(i)[0]);
		}
		return result;
	}
	void set_information(String[] info) {
		for (int i = 0; i < info.length; i++) {
			String [] str=new String[1];
			str[0]=info[i];
			list.add(str);
		}
	}
	ArrayList<String[]> Sign(String myname){
		ArrayList<String[]> result=new ArrayList<String[]>();
		for (int i = 0; i <list.size(); i++) {
			String[] temp=new String[list.get(i).length+2];
			String s="";
			s+=name;
			for (int j = 0; j < temp.length-2; j++) {
				temp[j]=list.get(i)[j];
				s+=temp[j];
			}
			JavaMD5RSASign sign=new JavaMD5RSASign();
			temp[temp.length-2]=myname;
			sign.setprivatekey(myname);
			temp[temp.length-1]=sign.CreateSign(s.getBytes());
			result.add(temp);
		}
		return result;
	}
	
	Boolean verifySign() {
		for (int i = 0; i <list.size(); i++) {
			String[] temp=list.get(i);
			for (int j = 2; j < temp.length; j=j+2) {
				String s="";
				s+=name;
				for(int k=0;k<j-1;k++) {
					s+=temp[k];
				}
				JavaMD5RSASign sign=new JavaMD5RSASign();
				sign.setpublickey(temp[j-1]);
				if(sign.VerifySign(s.getBytes(), temp[j])==false) {
					return false;
				}
			}
			
		}
		return true;	
	}
	void print() {
		for (int i = 0; i <list.size(); i++) {
			System.out.print(list.get(i)[0]+" ");
		}
		
	}
	
	/**
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ArrayList<String[]> list=new ArrayList<String[]>();
		String[] s1= {"a"};
		list.add(s1);
		String[] s2= {"b"};
		list.add(s2);
		
		Byzantine_socket_info b_s_i=new Byzantine_socket_info();
		b_s_i.name="1";
		b_s_i.list=list;
		b_s_i.list=b_s_i.Sign("1");
		b_s_i.list=b_s_i.Sign("1");
		ArrayList<String[]> result=b_s_i.Sign("1");
		
		System.out.println(result.get(0).length);
		
		b_s_i.list=result; 
		
		Boolean b=b_s_i.verifySign();
		
		
		System.out.println(b);
		
	}
	 /**/
	
	
	
	
}
