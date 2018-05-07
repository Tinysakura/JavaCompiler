package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.DeclareSyntaxBean;
import bean.SyntaxBean;
import entity.Exception;
import entity.JavaWordSurface;
import factory.SyntaxBeanFactory;

/**
 *提供句法分析服 务的单例服务类，通过识别句子开头的单词调用不同的狄递归下降分析程序进行句子的合法性判断
 * @author Administrator
 *
 */
public class SyntaxRecognizeServer {
	/**
	 * 组件
	 */
	private SyntaxBeanFactory mSyntaxBeanFactory;
	
	private TokenTableHandleServer mTokenTableHandleServer;
	
	private ExceptionTableHandlerServer mExceptionTableHandlerServer;
	
	/**
	 * data
	 */
	private Map<String, SyntaxBean> syMap;
	private List<JavaWordSurface> tokenList;
	//用来存放句法分析结果的数据结构
	private List<List<JavaWordSurface>> syntaxList;
	//当前需要分析的token与其指针
	private JavaWordSurface tokenHead;
	private int tokenHeadIndex;
	
	/**
	 *bean id
	 */
	private static final String DECLARE_SYNTAX_ID="declare";
	
	private static volatile SyntaxRecognizeServer synaxRecognizeServer;
	
	private SyntaxRecognizeServer(){
		System.out.println("SyntaxRecognizeServer start");
		
		mSyntaxBeanFactory=SyntaxBeanFactory.getInstance();
		syMap=mSyntaxBeanFactory.create();
		mTokenTableHandleServer=TokenTableHandleServer.getTokenTableHandleServer();
		mExceptionTableHandlerServer=ExceptionTableHandlerServer.getExceptionTypeInServer();
		
		syntaxList=new ArrayList<>();
		tokenHeadIndex=0;
	}
	
	public static SyntaxRecognizeServer getSynaxRecofnizeServer(){
		if(synaxRecognizeServer==null){
			synchronized (SyntaxRecognizeServer.class) {
				if(synaxRecognizeServer==null){
					synaxRecognizeServer=new SyntaxRecognizeServer();	
				}
			}
		}
		
		return synaxRecognizeServer;
	}
	
	/**
	 * 分析逻辑的主体,根据读入的首个java符号将分析任务派发特定的SyntaxBean执行
	 */
	public void analysis(){
		mTokenTableHandleServer=TokenTableHandleServer.getTokenTableHandleServer();
		tokenList=mTokenTableHandleServer.getTokenList();
		tokenHead=tokenList.get(0);
		
		while(tokenHeadIndex<tokenList.size()){
			System.out.println(tokenHead.getWord());
			tokenHead=tokenList.get(tokenHeadIndex);
			SyntaxBean nowOperatorBean=syMap.get(idMap(tokenHead.getWord()));
			if(nowOperatorBean!=null){
				if(nowOperatorBean instanceof DeclareSyntaxBean){
					System.out.println("it is DeclareSyntaxBean");
				}
				
				nowOperatorBean.setStarIndex(tokenHeadIndex);
				
				nowOperatorBean.parsing();
			}else{
				System.out.println("找不到以"+tokenHead.getWord()+"开头相应的递归下降分析子程序，跳入下一个token继续分析");
				tokenHeadIndex++;
				//将错误存入exception表		
				//应该在语法分析阶段为所有token指定行定位符方便句法分析时的错误定位
				mExceptionTableHandlerServer.syntaxExceptionTypeIn(
						new Exception(Exception.UNEXCEPT_BEGIN));
				
				System.out.println("不在分析范围内的句法");
			}
		}
	}
	
	public List<List<JavaWordSurface>> getSyntaxList(){
		return syntaxList;
	}
	
	/**
	 * 提供给syntaxBean的修改tokenHead的接口
	 */
	public void modifyNodeHead(int tokenHeadIndex){
		this.tokenHeadIndex=tokenHeadIndex;
		if(tokenHeadIndex<tokenList.size()){
			this.tokenHead=tokenList.get(tokenHeadIndex);
		}
	}
	
	/**
	 * 提供给syntaxBean的录入分析结果的typeIn方法
	 */
	public void typeIn(List<JavaWordSurface> syntax){
		this.syntaxList.add(syntax);
	}
	
	/**
	 * 将JavaWordSurface映射成对应的SyntaxBean的id
	 */
	public String idMap(String javaWordSurface){
		String idString=null;
		
		if(javaWordSurface.equals("boolean") || javaWordSurface.equals("char") ||
				javaWordSurface.equals("double") || javaWordSurface.equals("float") ||
				javaWordSurface.equals("int") || javaWordSurface.equals("long") || 
				javaWordSurface.equals("new") ||javaWordSurface.equals("short") || 
				javaWordSurface.equals("String") || javaWordSurface.equals("id")){
			idString=DECLARE_SYNTAX_ID;
		}
		
		System.out.println("idString:"+idString);
		return idString;
	}
}
