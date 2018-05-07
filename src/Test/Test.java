package Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {
	public static void main(String[] args){
		A a=new A();
		A a1=new A();
		
		a.change();
		a.change();a.change();
		System.out.println(a1.toString());
	}
	
	public static String[] getKeywordSupplement(){
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
		System.out.println(nodeList.getLength());

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
