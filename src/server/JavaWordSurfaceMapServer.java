package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import entity.Constant;
import entity.Delimiters;
import entity.Identifer;
import entity.JavaWordSurface;
import entity.KeyWord;
import entity.Operator;

/**
 * 使用单例模式维护一个java语言的单词表，提供基本的索引服务
 * @author Administrator
 *
 */
public class JavaWordSurfaceMapServer {
	private static volatile JavaWordSurfaceMapServer javaWordSurfaceMap=null;
	private static final String KEYWORD_FILEPATH="D://myeclipseworkspace//JavaCompiler//src//keyword.xml";
	
	private Map<String,JavaWordSurface> map;
	
	private JavaWordSurfaceMapServer(){
		System.out.println("JavaWordSurfaceMapServer launch");
		map=new HashMap<String, JavaWordSurface>();
		
		initMap();
	}

	public static JavaWordSurfaceMapServer getJavaWordSurfaceMap(){
		if(javaWordSurfaceMap==null){
            synchronized (JavaWordSurfaceMapServer.class){
                if(javaWordSurfaceMap== null){
                    javaWordSurfaceMap = new JavaWordSurfaceMapServer();
                }
            }
		}
		
		return javaWordSurfaceMap;
	}
	
	/**
	 * 为map填充数据
	 */
	private void initMap(){
		
		/**
		 * 填充关键字类型数据
		 */
		String[] keywordString={
				"abstract","assert","boolean",
				"break","byte","case","catch",
				"char","class","const","continue",
				"default","do","double","else",
				"enum","extends","final","finally",
				"float","for","goto","if","implements",
				"import","instanceof","int","interface",
				"long","native","new","package","private",
				"protected","public","return","strictfp",
				"short","static","super","switch","String",
				"synchronized","this","throw","throws",
				"transient","try","void","volatile","while"
		};
	    
		/**
		 * 判断配置文件是否存在
		 */
		if(new File(KEYWORD_FILEPATH).exists()){
			String[] keywordSupplement=getKeywordSupplement();
			
			/**
			 * 将补充的关键字表与keywordString合并
			 */
			System.arraycopy(keywordSupplement, 0, keywordString, 0, keywordSupplement.length);
		}
		
		for(int i=0;i<keywordString.length;i++){
			KeyWord temp=new KeyWord(keywordString[i],i+1);
			map.put(keywordString[i], temp);
		}
	
		
		/**
		 * 填充运算符类型数据
		 */
		String[] operatorStrings={
			"+","-","*","/","%","++","--",
			"++-","+++","--+","---",
			"==","!=",">","<",">=","<=",
			"&","|","^","<<",">>",">>>","~",
			"&&","||","!",
			"=","+=","-=","*=","<<=",">>=",
			"&=","^=","|=",
		};
		
		for(int i=0;i<operatorStrings.length;i++){
			Operator temp=new Operator(operatorStrings[i], keywordString.length+i+1);
			map.put(operatorStrings[i], temp);
		}
		
		/**
		 * 填充标识符与常数类型数据
		 */
		map.put("id", new Identifer("id", keywordString.length+operatorStrings.length+1));
		
		map.put("整常数", new Constant("整常数", keywordString.length+operatorStrings.length+2));
		map.put("实常数", new Constant("实常数", keywordString.length+operatorStrings.length+3));
		map.put("字符常数",new Constant("字符常数",keywordString.length+operatorStrings.length+4));
		map.put("布尔常数",new Constant("布尔常数",keywordString.length+operatorStrings.length+5));
		
		
		/**
		 * 填充界符
		 * 不考虑使用泛型的情况
		 */
		String[] delimiterString={
			";",",","\"",":","(",")",".","[","]","{","}","@","#",
		};
		for(int i=0;i<delimiterString.length;i++){
			Delimiters temp=new Delimiters(delimiterString[i],
					keywordString.length+operatorStrings.length+6+i);
			
			map.put(delimiterString[i], temp);
		}
	}
	
	/**
	 * 按单词名索引对应的标识符
	 */
	public int keywordIndex(String keyword){
		int seedcode=0;
		
		seedcode=map.get(keyword).getSeedCode();
		
		return seedcode;
	}
	
	/**
	 * 判断符号是否是关键字
	 */
	public boolean isKeyWord(String judgeValue){
		boolean isKeyWord=false;
		
		String[] keywords={
				"abstract","assert","boolean",
				"break","byte","case","catch",
				"char","class","const","continue",
				"default","do","double","else",
				"enum","extends","final","finally",
				"float","for","goto","if","implements",
				"import","instanceof","int","interface",
				"long","native","new","package","private",
				"protected","public","return","strictfp",
				"short","static","super","switch",
				"synchronized","this","throw","throws",
				"transient","try","void","volatile","while"
		};
		
		for(int i=0;i<keywords.length;i++){
			if(judgeValue.equals(keywords[i])){
				isKeyWord=true;
			}
		}
		
		return isKeyWord;
	}
	
	/**
	 * 判断符号是否是运算符
	 */
	public boolean isOperator(String value){
		boolean isOperator=false;
		
		String[] operatorStrings={
				"+","-","*","/","%","++","--",
				"++-","+++","--+","---",
				"==","!=",">","<",">=","<=",
				"&","|","^","<<",">>",">>>","~",
				"&&","||","!",
				"=","+=","-=","*=","/=","(%)=","<<=",">>=",
				"&=","^=","|=",
			};
		
		for(int i=0;i<operatorStrings.length;i++){
			if(operatorStrings[i].equals(value)){
				isOperator=true;
				break;
			}
		}
		
		return isOperator;
	}
	
	/**
	 * 使用配置文件的方式进行关键字的补充
	 * 使用DOM的方式读取xml文件
	 */
	public String[] getKeywordSupplement(){
	    List<String> keywordSupplementList=new ArrayList<String>();
	    
		File keywordSupplement=
				new File("D://myeclipseworkspace//JavaCompiler//src//keyword.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}   
		Document doc = null;
		try {
			doc = builder.parse(keywordSupplement);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		NodeList nodeList= doc.getElementsByTagName("VALUE");
		
		for(int i=0;i<nodeList.getLength();i++){
			if(doc.getElementsByTagName("VALUE").item(i)
				  .getFirstChild()!=null){
				  keywordSupplementList.add(doc.getElementsByTagName("VALUE").item(i)
						  .getFirstChild().getNodeValue()); 
				  
				  System.out.println(doc.getElementsByTagName("VALUE").item(i)
						  .getFirstChild().getNodeValue());
			}
		}
		
		String[] temp=new String[keywordSupplementList.size()];
		for(int i=0;i<keywordSupplementList.size();i++){
			temp[i]=keywordSupplementList.get(i);
		}
		
		return temp;
	}
}
