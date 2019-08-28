package com.lenovo.map2root;

public class BackTracking2 {
	public static void main(String[] args){
		String s="吕亚鸿";
		arrange(s,"");
	}
	public static void arrange(String s, String temp){//参数设计地尽量地简洁
		if(s.length()==0){
			System.out.println(temp);
			return;
		}
		for(int i=0;i<s.length();i++){
			String news=s.substring(0, i)+s.substring(i+1,s.length());//去掉String中的某个字母
			System.out.println("new="+news);
			arrange(news, temp+s.charAt(i));
		}
	}
}
 
