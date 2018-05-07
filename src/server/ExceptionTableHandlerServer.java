package server;

import java.util.ArrayList;
import java.util.List;

import entity.Exception;

/**
 * 负责向exception table录入错误信息的sever类
 * @author Administrator
 *
 */
public class ExceptionTableHandlerServer {
	/**
	 * data
	 * 负责存储错误信息的表
	 */
	private List<entity.Exception> exceptionTable;
	private List<Exception> syntaxExceptionTable;
	
	private static volatile ExceptionTableHandlerServer exceptionTypeInServer;
	
	private ExceptionTableHandlerServer(){
		System.out.println("ExceptionTableHandlerServer launch");
		exceptionTable=new ArrayList<entity.Exception>();
		syntaxExceptionTable=new ArrayList<Exception>();
	}
	
	public static ExceptionTableHandlerServer getExceptionTypeInServer(){
		if(exceptionTypeInServer==null){
			synchronized (ExceptionTableHandlerServer.class) {
				if(exceptionTypeInServer==null){
					exceptionTypeInServer=new ExceptionTableHandlerServer();
				}
			}
		}
		
		return exceptionTypeInServer;
	}
	
	/**
	 * 向外暴露的方法，负责将一条错误信息录入表内
	 */
	public void TypeIn(Exception exception){
		exceptionTable.add(exception);
	}
	
	/**
	 * 将一条句法类型的错误录入表中
	 */
	public void syntaxExceptionTypeIn(Exception exception){
		syntaxExceptionTable.add(exception);
	}
	
	public List<entity.Exception> getExceptionTable(){
		return exceptionTable;
	}
	
	public List<Exception> getSyntaxExceptionTable(){
		return syntaxExceptionTable;
	}
}
