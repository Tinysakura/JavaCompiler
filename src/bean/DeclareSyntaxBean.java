package bean;

import java.util.ArrayList;
import java.util.List;

import server.ExceptionTableHandlerServer;
import server.SyntaxRecognizeServer;
import server.TokenTableHandleServer;
import entity.Exception;
import entity.JavaWordSurface;

/**
 * 声明语句的句法bean
 * @author Administrator
 * declare的句法：
 * 是LL(1)文法
 * F->DECLARE id A;
 * A->NULL | =C
 * C-> id | new id() | 整常数 | 实常数 | 字符常数 | 布尔常数
 * DECLARE = boolean|char|double|float|int|long|new|short|String|id
 */
public class DeclareSyntaxBean implements SyntaxBean{
	/**
	 * server
	 */
	private SyntaxRecognizeServer mSyntaxRecognizeServer;
	private TokenTableHandleServer mTokenTableHandleServer;
	private ExceptionTableHandlerServer mExceptionTableHandlerServer;
	
	/**
	 * data
	 */
	//存放分析结果的数据结构
	private List<JavaWordSurface> result;
	//标志当前处理的token
	private JavaWordSurface lookAhead;
	//标志读取token表的开始位置与读取结束时的位置
	@SuppressWarnings("unused")
	private int startIndex;
	private int endIndex;
	private List<JavaWordSurface> tokenList;
	//表示是否出错
	private boolean isError;
	
	/**
	 * 终结符的first集
	 */
	String[] DECLARE_first;
	String[] F_first;
	String[] A_first;
	String[] C_first;
	
	/**
	 * 终结符的follow集
	 */
	String[] DECLARE_follow;
	String[] F_follow;
	String[] A_follow;
	String[] B_follow;
	String[] C_follow;
	
	public DeclareSyntaxBean(){
		System.out.println("DeclareSyntaxBean created");
		this.mTokenTableHandleServer=TokenTableHandleServer.getTokenTableHandleServer();
		this.mExceptionTableHandlerServer=ExceptionTableHandlerServer.getExceptionTypeInServer();
		
		this.tokenList=mTokenTableHandleServer.getTokenList();
		this.result=new ArrayList<JavaWordSurface>();
		
		initFirstAndFollow();
		isError=false;
	}
	
	@Override
	public void parsing() {
		// TODO Auto-generated method stub
		System.out.println("syntaxbean begin parsing");
		
		F();
	}

	@Override
	public void setStarIndex(int index) {
		// TODO Auto-generated method stub
		this.startIndex=index;
		this.endIndex=index;
		
		this.lookAhead=tokenList.get(endIndex);
		System.out.println("index:"+index+" "+"content:"+lookAhead.getWord());
	}

	@Override
	public void initFirstAndFollow() {
		// TODO Auto-generated method stub
		DECLARE_first=new String[]{
				"boolean","char","double",
				"float","int","long","new","short","String","id"
		};
		
		DECLARE_follow=new String[]{
				"id"
		};
		
		F_first=new String[]{
				"boolean","char","double",
				"float","int","long","new","short","String","id"	
		};
		
		F_follow=new String[]{
		};
		
		A_first=new String[]{
				null,"="
		};
		
		A_follow=new String[]{
				";"
		};
		
		C_first=new String[]{
				"id","new","整常数 ","实常数 ","字符常数 |","布尔常数"
		};
		
		C_follow=new String[]{
				";"
		};
	}
	
	/**
	 * 构造非终结符的递归下降分析程序
	 */
	public void F(){
		System.out.println("F");
		
		DECLARE();
		
		match("id");
		
		A();
		
		match(";");
		
		onComplete();
	}
	
	public void A(){
		System.out.println("A");
		
		if(!isError){
			if(lookAhead.getWord().equals("=")){
				System.out.println("=");
				match("=");
				
				C();
			}	
		}
	}
	
	public void C(){
		System.out.println("C");
		
		if(!isError){
			switch (lookAhead.getWord()) {
			case "id":
				match("id");
				break;
				
			case "new":
				match("new");
				match("id");
				match("(");
				match(")");
				break;
				
			case "整常数":
				match("整常数");
				break;
				
			case "实常数":
				match("实常数");
				break;
				
			case "字符常数":
				match("字符常数");
				break;
				
			case "布尔常数":
				match("布尔常数");
				break;
				
			default:
				onError();
				break;
			}	
		}
	}
	
	
	public void DECLARE(){
		System.out.println("DECLARE");
		System.out.println("isError:"+isError);
		
		if(!isError){
			System.out.println("DECLARE:"+lookAhead.getWord());
			switch (lookAhead.getWord()) {
			case "boolean":
				match("boolean");
				break;

			case "char":
				match("char");
				break;
				
			case "double":
				match("double");
				break;
				
			case "float":
				match("float");
				break;
				
			case "int":
				System.out.println("match int");
				match("int");
				break;
				
			case "long":
				match("long");
				break;
				
			case "new":
				match("new");
				break;
				
			case "short":
				match("short");
				break;
				
			case "String":
				match("String");
				break;
				
			case "id":
				match("id");
				break;
				
			default:
				onError();
			}
		}
	}

	@Override
	/**
	 * onError函数执行后则忽略之后的所有匹配
	 */
	public int onError(){
		// TODO Auto-generated method stub
		System.out.println("onError");
		
		isError=true;
		
		return endIndex;
	}

	@Override
	public int onComplete() {
		System.out.println("onComplete");
		
		mSyntaxRecognizeServer=SyntaxRecognizeServer.getSynaxRecofnizeServer();
		// TODO Auto-generated method stub
		if(!isError){
			mSyntaxRecognizeServer.modifyNodeHead(endIndex);
			mSyntaxRecognizeServer.typeIn(result);
			
			result=new ArrayList<JavaWordSurface>();
		}else{
			System.out.println("因出现错误而终止分析");
			result.clear();
			mSyntaxRecognizeServer.modifyNodeHead(endIndex);
			
			mExceptionTableHandlerServer.syntaxExceptionTypeIn(new Exception(Exception.ILLEGAL_SYNTAX));
			System.out.println("不合法的句法");
		}
		
		isError=false;
		return endIndex;
	}

	@Override
	public boolean inFirts(String[] first) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		boolean isIn=false;
		
		for(int i=0;i<first.length;i++){
			if(first[i]==lookAhead.getWord()){
				isIn=true;
				break;
			}
		}
		
		return false;
	}

	@Override
	public boolean inFollow(String[] follow) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		boolean isIn=false;
		
		for(int i=0;i<follow.length;i++){
			if(follow[i]==lookAhead.getWord()){
				isIn=true;
				break;
			}
		}
		
		return false;
	}

	@Override
	public void match(String operatorObject) {
		// TODO Auto-generated method stub
		System.out.println("operatorObject:"+operatorObject+" "+"lookAhead:"+lookAhead.getWord());
		if(operatorObject.equals(lookAhead.getWord())){
			result.add(lookAhead);
			lookAhead=getNextLooKAhead();
		}else{
			onError();
		}
	}

	@Override
	public JavaWordSurface getNextLooKAhead() {
		// TODO Auto-generated method stub
		endIndex++;
		
		if(endIndex<tokenList.size()){
			return tokenList.get(endIndex);
		}else{
			return null;
		}
	}
	
	@Override
	public String toString(){
		return "DeclareSyntaxBean created";
	}

}
