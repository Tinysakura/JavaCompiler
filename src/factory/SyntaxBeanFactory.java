package factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bean.SyntaxBean;

/**
 * 读取配置文件创建句法分析bean的单例的简单工厂模式
 * @author Administrator
 *
 */
public class SyntaxBeanFactory {
	private static volatile SyntaxBeanFactory instance;
	
	public static SyntaxBeanFactory getInstance(){
		if(instance==null){
			synchronized (SyntaxBeanFactory.class) {
				if(instance==null){
					instance=new SyntaxBeanFactory();
				}
			}
		}
		
		return instance;
	}
	
	
	private SyntaxBeanFactory(){
		System.out.println("beanfactory created");
	}
	
	public Map<String,SyntaxBean> create(){
		Map<String,SyntaxBean> syMap=new HashMap<String, SyntaxBean>();
		
		File keywordSupplement=
				new File("D://myeclipseworkspace//JavaCompiler//src//syntaxbean.xml");
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
				System.out.println("length:"+nodeList.getLength());
				System.out.println(doc.getElementsByTagName("ID").item(i)
							  .getFirstChild().getNodeValue());
				System.out.println(doc.getElementsByTagName("ADRESS").item(i)
							  .getFirstChild().getNodeValue());
				try {
					  syMap.put(doc.getElementsByTagName("ID").item(i)
							  .getFirstChild().getNodeValue(),
							  createBean(
									  doc.getElementsByTagName("ADRESS").item(i)
									  .getFirstChild().getNodeValue()));
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return syMap;
	}
	
	/**
	 * 通过反射机制映射bean类
	 */
	public SyntaxBean createBean(String adress) throws Exception{
		SyntaxBean syntaxBean = null;
		try {
			@SuppressWarnings("unchecked")
			Class<SyntaxBean> syClass=(Class<SyntaxBean>) Class.forName(adress);
			syntaxBean=syClass.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(syntaxBean!=null){
			return syntaxBean;
		}else{
			throw new Exception("no class found exception");
		}
	}
}
