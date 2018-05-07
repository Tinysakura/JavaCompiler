package server;

import java.util.ArrayList;
import java.util.List;

import entity.Symbol;

/**
 * 提供符号录入服务的server类,使用lex数组作为实际存储符号的数据结构
 * @author Administrator
 *
 */
public class SymbolTableHandleServer {
	/**
	 * data
	 */
	private char[] lex;
	private List<Symbol> symbollist;
	//lex数组的当前位置
	private int nowPosition;
	
	private static volatile SymbolTableHandleServer symbolTableHandleServer;
	
	private SymbolTableHandleServer(){
		System.out.println("SymbolTableHandleServer launch");
		//lex数组的长度暂定为20000
		lex=new char[10000];
		symbollist=new ArrayList<Symbol>();
		
		nowPosition=0;
	}
	
	public static SymbolTableHandleServer
	    getSymbolTableHandleServer(){
		if(symbolTableHandleServer==null){
			synchronized (SymbolTableHandleServer.class) {
				symbolTableHandleServer=new SymbolTableHandleServer();
			}
		}
		
		return symbolTableHandleServer;
	}
	
	/**
	 * 提供录入symbol的服务，将符号存入lex数组中，使用symbollist索引
	 */
	public void typeIn(String value,int type){
		/**
		 * 对value进行类型判断与位置判断后封装成symbol对象装入list
		 */
		Symbol symbol=new Symbol(nowPosition,type,value.length());
		
		//为了避免重复装入，装入前应检查符号是否在符号表中已存在
		boolean needTypeIn=true;
		
		for(int i=0;i<symbollist.size();i++){
			if(symbollist.get(i).getValue()==value){
				needTypeIn=false;
				break;
			}
		}
		if(needTypeIn){
			System.out.println(value+" need TypeIn");
			symbollist.add(symbol);
			
			/**
			 * 将符号拆分成字节填入字节数组，使用"\0"作为分隔符
			 */
			char[] temp=new char[value.length()];
			
			value.getChars(0, value.length(), temp, 0);
			System.out.println("temp_length:"+temp.length);
			
			for(int i=0;i<temp.length;i++){
				lex[nowPosition]=temp[i];
				nowPosition++;
				System.out.print("temp"+i+"="+temp[i]+" ");
			}
			
			lex[nowPosition]=0;
			nowPosition++;
		}
	}
	
	public List<Symbol> getSymbolList(){
		return symbollist;
	}
	
	/**
	 * 根据符号在lex数组开始的位置取出value的方法
	 */
	public String getValue(int headPosition){
		List<Character> temp=new ArrayList<Character>();
		while(lex[headPosition]!=0){
			temp.add(lex[headPosition]);
			headPosition++;
		}
		
		char[] charTemp=new char[temp.size()];
		for(int i=0;i<charTemp.length;i++){
			charTemp[i]=temp.get(i);
		}
		
		return new String(charTemp);
	}
	
	public char[] getLex(){
		return lex;
	}

}
