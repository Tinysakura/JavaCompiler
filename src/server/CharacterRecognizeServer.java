package server;

import java.util.ArrayList;
import java.util.List;





import entity.Constant;
import entity.Delimiters;
import entity.Exception;
import entity.Identifer;
import entity.JavaWordSurface;
import entity.KeyWord;
import entity.Operator;
import entity.Symbol;

/**
 * 设计成单例模式的字符识别服务，根据读入的第一个字符进行判断并进行合法性判断
 * @author Administrator
 *
 */
public class CharacterRecognizeServer {
	private static volatile CharacterRecognizeServer characterRecognizeServer;
	
	/**
	 * data
	 */
	//一个维护当前读取指针的字符数组
	private char[] nowCharacterArray;
	//当前读入数据在源码中的行数，用于错误信息的包装
	private int nowRow;
	//当前指针
	private int nowPosition;
	
	//判断当前行是否是注释内容
	private boolean commentsStatu=false;
	
	/**
	 * static final data
	 */
	//数值常数
	private static final int NUMERICAL_CONSTANT=1;
	//字符常数
	private static final int CHARACTER_CONSTANT=2;
	//标识符
	private static final int IDENTIFER=3;
	//注释或除号
	private static final int COMMENTS=4;
	//界符
	private static final int DELIMITERS=5;
	//运算符
	private static final int OPERATOR=6;
	
	
    /**
     * server
     */
	private ExceptionTableHandlerServer mExceptionTableHandlerServer;
	private JavaWordSurfaceMapServer mJavaWordSurfaceMapServer;
	private SymbolTableHandleServer mSymbolTableHandleServer;
	private TokenTableHandleServer mTokenTableHandleServer;
	
	/**
	 * 装入所有需要使用的服务
	 */
	private CharacterRecognizeServer(){
		System.out.println("CharacterRecognizeServer launch");
		mExceptionTableHandlerServer=
				ExceptionTableHandlerServer.getExceptionTypeInServer();
		
		mJavaWordSurfaceMapServer=
				JavaWordSurfaceMapServer.getJavaWordSurfaceMap();
		
		mSymbolTableHandleServer=
				SymbolTableHandleServer.getSymbolTableHandleServer();
		
		mTokenTableHandleServer=
				TokenTableHandleServer.getTokenTableHandleServer();
	}
	
	public static CharacterRecognizeServer getCharaterRecognizeServer(){
		if(characterRecognizeServer==null){
            synchronized (CharacterRecognizeServer.class){
                if(characterRecognizeServer== null){
                   characterRecognizeServer = new CharacterRecognizeServer();
                }
            }
		}
		
		return characterRecognizeServer;
	}
	
	/**
	 * 接收一行数据进行字符分析,将分析结果插入符号表与字符表中
	 */
	public void analysis(String linedata,int nowRow){
		/**
		 * 初始化相关数据
		 * trim()去掉首尾空格  
		 */
		linedata=linedata.trim();
		setNowCharacterArray(linedata);
//		System.out.println(linedata);
//		System.out.println(nowCharacterArray.length);
//		for(int i=0;i<nowCharacterArray.length;i++){
//			System.out.println(nowCharacterArray[i]);
//		}
		
		this.nowRow=nowRow;
		this.nowPosition=0;
		
		/**
		 * 开始分析
		 */
		while(nowPosition<nowCharacterArray.length){
			/**
			 * 先判断是否处于注释行。若是则直接进行注释分析
			 */
			if(commentsStatu==true){
				recogCom();
			}
			else{
				/**
				 * 根据读入的第一个字符对判断逻辑进行分发
				 */
				System.out.println("nowposition:"+nowPosition+" "+
				 "nowcharactersize:"+nowCharacterArray.length);
				char firstChar=nowCharacterArray[nowPosition];
				
				//如果是空格则重新读入下一个字符
				while(firstChar==32 && nowPosition<nowCharacterArray.length-1){
					nowPosition++;
					firstChar=nowCharacterArray[nowPosition];
					
					System.out.println(firstChar);
				}
				
				int type=judgeCharType(firstChar);
				System.out.println(firstChar+":"+(int)firstChar);
				
				switch (type) {
				case NUMERICAL_CONSTANT:
					recogDig();
					break;
					
				case CHARACTER_CONSTANT:
					recogStr();
					break;
					
				case IDENTIFER:
					recogId();
					break;
					
				case COMMENTS:
					recogCom();
					break;
					
				case DELIMITERS:
					recogDel();
					break;
					
				case OPERATOR:
					recogOpe();
					break;
				/**
				 * 如果不是以上所有情况则说明录入了非法字符，进行出错处理
				 */
				default:
					Exception exception=
					new Exception(nowRow, nowPosition, Exception.ILEGAL_BYTE);
					System.out.println("exceptionType:"+Exception.ILEGAL_BYTE
							+"nowRow:"+nowRow+" "+"nowColwn:"+nowPosition);
					
			
					System.out.println(firstChar+":"+(int)firstChar);
					mExceptionTableHandlerServer.TypeIn(exception);
					nowPosition++;
					
					break;
				}
			}
		}
		
	}
	
	/**
	 * 判断char类型的方法
	 */
	public int judgeCharType(char judgeChar){
		int type=0;
		
		int unicode=(int)judgeChar;
		
		if(unicode>=48 && unicode<=57){
			type=NUMERICAL_CONSTANT;
		}
		
		
		if(unicode==34 || unicode==39){
			type=CHARACTER_CONSTANT;
		}
	
		
		if((unicode>=65 && unicode<=90) ||
				(unicode>=97 && unicode<=122) || unicode==95 || unicode==36){
			type=IDENTIFER;
		}
		
		if(unicode==47){
			type=COMMENTS;
		}
		
		/**
		 * delimiter
		 */
		int[] delimeters={
				';',',','\\',':','(',')','.','[',']','{','}','@','#'
		};
		for(int i=0;i<delimeters.length;i++){
			if(unicode==delimeters[i]){
				type=DELIMITERS;
				break;
			}
		}
		
		/**
		 * operator begin
		 */
		int[] operators={
				'+','-','*','%','!','>','<','&','|',
				'^','~','='
		};
		for(int i=0;i<operators.length;i++){
			if(unicode==operators[i]){
				type=OPERATOR;
				break;
			}
		}
		
		return type;
	}
	
	/**
	 * 将读入的数据转化为字符数组
	 */
	private void setNowCharacterArray(String data){
		nowCharacterArray=new char[data.length()];
		data.getChars(0, data.length(), nowCharacterArray, 0);
		
		nowPosition=0;
	}
	
	/**
	 * 将List<Character>转换为字符串
	 */
	public String charlistToString(List<Character> chars){
		char[] temp=new char[chars.size()];
		
		for(int i=0;i<chars.size();i++){
			temp[i]=chars.get(i);
		}
		
		return new String(temp);
	}
	
	/**
	 * 识别数值常数
	 */
	public void recogDig(){
		System.out.println("recogDig");
		List<Character> digs=
				new ArrayList<Character>();
		
		//标识.号是否已出现过
		boolean appear=false;
		
		/**
		 * 先判断0开头的字符常数
		 */
		if((int)nowCharacterArray[nowPosition]==48){
			/**
			 * 将字符分别录入token表与symbol表
			 */
			JavaWordSurface token=
					new JavaWordSurface("0", mJavaWordSurfaceMapServer.keywordIndex("id"));
			mTokenTableHandleServer.typeIn(token);
			
			mSymbolTableHandleServer.typeIn("0",Symbol.TIDY_CONSTANT);
			
			nowPosition++;
		}else{
			while((judgeCharType(nowCharacterArray[nowPosition])
					==NUMERICAL_CONSTANT || nowCharacterArray[nowPosition]=='.')){
				/**
				 * 没读到.号的情况
				 */
				if((int)(nowCharacterArray[nowPosition])!=46){
					digs.add(nowCharacterArray[nowPosition]);
					nowPosition++;
				}
				else{
					digs.add('.');
					System.out.println("add .");
					//预读下一个字符，若不是数字则出现构词错误
					nowPosition++;
					if(judgeCharType(nowCharacterArray[nowPosition])
					!=NUMERICAL_CONSTANT){
						Exception exception=
								new Exception(nowRow,nowPosition,Exception.WORD_FORMATION_ERROR);
						
						mExceptionTableHandlerServer.TypeIn(exception);
					}
					else{
						//标识.号已出现,则不允许出现第二个.号
						appear=true;
						
						while(judgeCharType(nowCharacterArray[nowPosition])
								==NUMERICAL_CONSTANT && nowPosition<nowCharacterArray.length){
							digs.add(nowCharacterArray[nowPosition]);
							nowPosition++;
						}
					}
				}
			}
			
			/**
			 *并将结果存入token表与symbol表
			 */
			
			//将list<character>中存储的数据转换为String
			String result=charlistToString(digs);
			
			if(appear){
				mTokenTableHandleServer.typeIn(
						new Constant("整常数",
						mJavaWordSurfaceMapServer.keywordIndex("整常数")));
				
				mSymbolTableHandleServer.typeIn(result, Symbol.TIDY_CONSTANT);
			}else{
				mTokenTableHandleServer.typeIn(
						new Constant("实常数",
						mJavaWordSurfaceMapServer.keywordIndex("实常数")));
				
				mSymbolTableHandleServer.typeIn(result, Symbol.FLOAT_CONSTANT);
			}
			
		}
	
	}
	
	/**
	 * 识别字符常数
	 */
	public void recogStr(){
		System.out.println("recog str");
		List<Character> str=new ArrayList<Character>();
		int length=0;
		
		if((int)(nowCharacterArray[nowPosition])==34){
			nowPosition++;
			/**
			 * 直到读到下个双引号之前的内容全部作为字符常数的内容但要判断不能超长
			 */
			while(nowCharacterArray[nowPosition]!=34){
				str.add(nowCharacterArray[nowPosition]);
				length++;
				nowPosition++;
			}
			
			nowPosition++;
		}
		else if((int)(nowCharacterArray[nowPosition])==39){
			System.out.println("enter 39");
			nowPosition++;
			/**
			 * 直到读到下个单引号之前的内容全部作为字符常数的内容但要判断不能超长
			 */
			while(nowCharacterArray[nowPosition]!=39){
				str.add(nowCharacterArray[nowPosition]);
				length++;
				nowPosition++;
			}
			
			nowPosition++;
		}
		
		/**
		 * 判断字符常数的合法性并将结果存入token表与symbol表
		 */
		//限定字符串的长度不超过255
		if(length>255){
			Exception exception=
					new Exception(nowRow,nowPosition,Exception.CHARACTER_CONSTANT_OVER_LENGTH);
		
		    mExceptionTableHandlerServer.TypeIn(exception);
		}
		else{
			String result=charlistToString(str);
			
			mTokenTableHandleServer.typeIn(
					new Constant("字符常数", 
							mJavaWordSurfaceMapServer.keywordIndex("字符常数")));
			
			mSymbolTableHandleServer.typeIn(result, 
					Symbol.CHARACTER_CONSTANT);
		}
	}
	
	/**
	 * 识别标识符
	 */
	public void recogId(){
		List<Character> id=new ArrayList<Character>();
		id.add(nowCharacterArray[nowPosition]);
		nowPosition++;
	    int unicode=(int)(nowCharacterArray[nowPosition]); 
		
		while(((unicode>=65 && unicode<=90) ||
				(unicode>=97 && unicode<=122) || unicode==95 || 
				unicode==36 || (unicode>=48 && unicode<=57))
				&& (nowPosition<nowCharacterArray.length)){
			id.add(nowCharacterArray[nowPosition]);
			System.out.println(nowPosition+":"+nowCharacterArray[nowPosition]);
			
			nowPosition++;
			if(nowPosition!=nowCharacterArray.length){
				unicode=nowCharacterArray[nowPosition];
			}
		}
		
		/**
		 * 回退nowPosition并将结果存入token与symbol表
		 */
		//nowPosition--;
		String result=charlistToString(id);
		System.out.println("result:"+result);
		
		/**
		 * 判断是否是关键字
		 */
		if(mJavaWordSurfaceMapServer.isKeyWord(result)){
			System.out.println("is keyword");
			mTokenTableHandleServer.typeIn(
					new KeyWord(result, 
							mJavaWordSurfaceMapServer.keywordIndex(result)));
			System.out.println(result+"'s token_id is"+mJavaWordSurfaceMapServer.keywordIndex(result));
		}else{
			mTokenTableHandleServer.typeIn(
					new Identifer("id", 
							mJavaWordSurfaceMapServer.keywordIndex("id")));
			System.out.println("id's token_id is"+mJavaWordSurfaceMapServer.keywordIndex("id"));
			
			mSymbolTableHandleServer.typeIn(result, Symbol.SIMPLE_VARIETY);
		}
	}
	
	/**
	 *识别注释和除号
	 */
	public void recogCom(){
		if(commentsStatu==true){
			while(nowPosition<nowCharacterArray.length){
				/**
				 * 如果读到*则判断下一个字符是否是'/'，若是则注释结束
				 */
				if(nowCharacterArray[nowPosition]==42){
					nowPosition++;
					
					if(nowPosition<nowCharacterArray.length){
						if(nowCharacterArray[nowPosition]==47){
							commentsStatu=false;
							nowPosition++;
							break;
						}
					}else{
						break;
					}
				}
				
				nowPosition++;
			}
		}else{
			/**
			 * 读入下一个字符，若是'/'或'*'则说明是注释，否则为除号
			 */
			nowPosition++;
			int unicode=(int)nowCharacterArray[nowPosition];
			
			//注释的内容不放入token表与symbol表
			if(unicode==47){
				//如果判断是单行注释则直接跳到本行末尾
				nowPosition=nowCharacterArray.length;
			}
			else if(unicode==42){
				//如果判断是多行注释，则在读到注释的结束符时一直维护commentsStatu为true
				commentsStatu=true;
			}else{
				//回退指针
				nowPosition--;
				
				mTokenTableHandleServer.typeIn(
						new Operator("/", 
								mJavaWordSurfaceMapServer.keywordIndex("/")));
			}
		}
	}
	
	/**
	 * 识别界符
	 */
	public void recogDel(){
		
		char[] temp={nowCharacterArray[nowPosition]};
		
		String result=new String(temp);
		System.out.println("recog del:"+result);
		
	    mTokenTableHandleServer.typeIn(new Delimiters(result, 
	    		mJavaWordSurfaceMapServer.keywordIndex(result))); 
	    
	    nowPosition++;
	}
	
	/**
	 * 识别运算符
	 */
	public void recogOpe(){
		/**
		 * 读到运算符的首字符后向后一直读到非运算符的组成成分为止，
		 * 判断得到的字符串是否是合法运算符，若是则存入token表，否则视为异常存入异常表
		 */
		List<Character> operator=new ArrayList<Character>();
		
		while(judgeCharType(nowCharacterArray[nowPosition])==OPERATOR){
			operator.add(nowCharacterArray[nowPosition]);
			nowPosition++;
		}
		
		char[] temp=new char[operator.size()];
		
		for(int i=0;i<operator.size();i++){
			temp[i]=operator.get(i);
		}
		
		String result=new String(temp);
		System.out.println("operator result:"+result);
		
		/**
		 * 判断是否是合法的运算符
		 */
		if(mJavaWordSurfaceMapServer.isOperator(result)){
		    mTokenTableHandleServer.typeIn(new Operator(result, 
		    		mJavaWordSurfaceMapServer.keywordIndex(result)));  
		}else{
			Exception exception=
					new Exception(nowRow,nowPosition,Exception.ILEGAL_OPERATOR);
			
			mExceptionTableHandlerServer.TypeIn(exception);
		}
	}
	
}
