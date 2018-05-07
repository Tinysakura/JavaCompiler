package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 提供文件处理的服务
 * @author Administrator
 *
 */
public class FileHandleServer {
	
	public static String ReadSourceFile(File file){
		BufferedReader bf=null;
		StringBuilder sb = null;
		try {
			bf=new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(bf!=null){
			sb=new StringBuilder();
			String readTemp;
			try {
				while((readTemp=bf.readLine())!=null){
					sb.append(readTemp).append("\r\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(sb.toString());
		try {
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	
	public static void WriteFile(File file,String content){
		FileOutputStream fileOutputStream=null;
		
		try {
			fileOutputStream=new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			/**
			 * 由于textarea中的换行符为\n。存入文件之前要先进行处理
			 */
			content.replaceAll("\n", "\r\n");
			fileOutputStream.write(content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
