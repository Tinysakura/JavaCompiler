package server;

import java.util.ArrayList;
import java.util.List;

import entity.JavaWordSurface;

/**
 * 使用单例模式创建的处理Token表的server类
 * @author Administrator
 *
 */
public class TokenTableHandleServer {
	private static volatile TokenTableHandleServer tokenTableHandleServer;

	/**
	 * data
	 */
	private List<JavaWordSurface> tokenList;
	
	private TokenTableHandleServer(){
		System.out.println("TokenTableHandleServer launch");
		tokenList=new ArrayList<JavaWordSurface>();
	}
	
	public static TokenTableHandleServer 
	  getTokenTableHandleServer(){
		if(tokenTableHandleServer==null){
			synchronized (TokenTableHandleServer.class) {
				tokenTableHandleServer=new TokenTableHandleServer();
			}
		}
		
		return tokenTableHandleServer;
	}
	
	/**
	 * 提供的将token存入tokenlist的服务
	 */
	public void typeIn(JavaWordSurface javaWordSurface){
		tokenList.add(javaWordSurface);
	}
	
	public List<JavaWordSurface> getTokenList(){
		return tokenList;
	}
	
}
