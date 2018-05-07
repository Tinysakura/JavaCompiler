package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * 一个自定义的日志类
 * @author Administrator
 *
 */
public class CustomeLog {
	private static volatile CustomeLog customeLog;
	private static final String LOG_FILE_PATH=
			"C://Users//Administrator//Desktop//logs//";
	
	private File logFile;
	private BufferedWriter bfWriter;
	private FileOutputStream fileOutputStream;
	
	private CustomeLog(){
		/**
		 * 在指定位置新建一个日志文件，并打开该文件的输入流
		 * 使用当前时间的时间戳作为日志文件名
		 */
		logFile=new File(LOG_FILE_PATH+new Date().getTime()+".txt");
		
		try {
			fileOutputStream=new FileOutputStream(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bfWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
	}
	
	public static CustomeLog getCustomeLog(){
		if(customeLog==null){
			synchronized (CustomeLog.class) {
				if(customeLog==null){
					customeLog=new CustomeLog();
				}
			}
		}
		
		return customeLog;
	}
	
	public void writeLog(String logContent){
		try {
			bfWriter.write(logContent);
			bfWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeStream(){
		try {
			bfWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args){
//		CustomeLog customeLog=CustomeLog.getCustomeLog();
//		
//		customeLog.writeLog("hello world");
//		customeLog.writeLog("hello world");
//		customeLog.closeStream();
//	}
}
