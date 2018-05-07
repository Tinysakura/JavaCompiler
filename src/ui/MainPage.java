package ui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import entity.JavaWordSurface;
import entity.Symbol;
import entity.Exception;
import server.CharacterRecognizeServer;
import server.ExceptionTableHandlerServer;
import server.FileHandleServer;
import server.SymbolTableHandleServer;
import server.SyntaxRecognizeServer;
import server.TokenTableHandleServer;

public class MainPage extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * ui
	 */
    private JTextArea codeArea;
    private JScrollPane scrollPane;
    
    //cardPanel
    private JPanel parentPanel;
    private JPanel wordResultPanel;
    private JPanel syntaxResultPanel;
    
    //childPanel
    //wordResult child
    private JPanel codePanel;
    private JPanel symbolTablePanel;
    private JPanel tokenTablePanel;
    private JPanel exceptionPanel;
    
    //syntaxResult child
    private JPanel syntaxPanel;
    private JPanel syntaxExceptionPanel;
    
    private JMenuBar menuToolBar;
    
    private JMenu fileMenu;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveasItem;
    
    private JMenu lexicalAnalysisMenu;
    private JMenuItem autoItem;
    private JMenuItem manualItem;
    
    private JMenu syntaxAnalysisMenu;
    private JMenuItem syntaxAnalysis;
    
    //中间代码生成
    private JMenu intermediateCodeGenerationMenu;
    
    private JMenu targetCodeGenerationMenu;
    
    private JMenu helpMenu;
    
    //对话框弹出
    private JOptionPane mJOptionPane;
   
    
    /**
     * 展示最终数据的表格
     */
    private JTable tokenTable;
    private JTable symbolTable;
    private JTable exceptionTable;
    private JTable syntaxTable;
    private JTable syntaxExceptionTable;
    
    private DefaultTableModel update_token_table;  
    private DefaultTableModel update_symbol_table;
    private DefaultTableModel update_exception_table;
    private DefaultTableModel update_syntax_table;
    private DefaultTableModel update_syntaxException_table;
    
    private JScrollPane token_table_scroll;
    private JScrollPane symbol_table_scroll;
    private JScrollPane exception_table_scroll;
    private JScrollPane syntax_table_scroll;
    private JScrollPane syntaxException_table_scroll;
    
    /**
     * layout
     */
    private CardLayout mCardLayout;
    
    /**
     * data
     */
    private File nowSelectedFile;
    //当前行
    private int nowRow;
    //当前分析阶段
    private int nowAnalysisStage;
    
    /**
     * 分析阶段的状态码
     */
    private static final int READ_FILE_COMPLETE=1;
    
    private static final int WORD_ANALYSIS_STAGE=2;
    
    private static final int SYNTAX_ANALYSIS_STAGE=3;
    
    /**
     * server
     */
    private CharacterRecognizeServer mCharacterRecognizeServer;
	private ExceptionTableHandlerServer mExceptionTableHandlerServer;
	private SymbolTableHandleServer mSymbolTableHandleServer;
	private TokenTableHandleServer mTokenTableHandleServer;
	private SyntaxRecognizeServer mSyntaxRecognizeServer;
	
    public MainPage(){
    	System.out.println("application launch");
    	initCardLayout();
    	
    	initEvent();
    	
    	initServer();
    }
    
    public void initCardLayout(){
    	mJOptionPane=new JOptionPane();
    	mCardLayout=new CardLayout();
    	parentPanel=new JPanel();
    	parentPanel.setLayout(mCardLayout);
    	add(parentPanel);
    	
    	/**
    	 * 给JFrame设置menuBar
    	 */
    	menuToolBar=new JMenuBar();
    	
        fileMenu=new JMenu("文件");
        openItem=new JMenuItem("打开");
        saveItem=new JMenuItem("保存");
        saveasItem=new JMenuItem("另存为");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveasItem);
        menuToolBar.add(fileMenu);
        
        lexicalAnalysisMenu=new JMenu("词法分析");
        autoItem=new JMenuItem("自动生成过程分析");
        manualItem=new JMenuItem("手动生成过程分析");
        lexicalAnalysisMenu.add(autoItem);
        lexicalAnalysisMenu.add(manualItem);
        menuToolBar.add(lexicalAnalysisMenu);
        
        syntaxAnalysisMenu=new JMenu("句法分析器");
        syntaxAnalysis=new JMenuItem("句法分析");
        syntaxAnalysisMenu.add(syntaxAnalysis);
        menuToolBar.add(syntaxAnalysisMenu);
        
       
        intermediateCodeGenerationMenu=new JMenu("中间代码生成");
        menuToolBar.add(intermediateCodeGenerationMenu);
        
        targetCodeGenerationMenu=new JMenu("目标代码生成");
        menuToolBar.add(targetCodeGenerationMenu);
        
        helpMenu=new JMenu("生成");
        menuToolBar.add(helpMenu);
        
        //给JFrame设置menuBar
        setJMenuBar(menuToolBar);
    	
    	initWordResultPanel();
    	initSyntaxResult();
    	
    	parentPanel.add(wordResultPanel, "wordResult");
    	parentPanel.add(syntaxResultPanel,"syntaxResult");
    	
    	mCardLayout.show(parentPanel,"wordResult");
    }
	
    /**
     * 初始化词法分析面板与其控件
     */
    public void initWordResultPanel(){
    	wordResultPanel=new JPanel();
    	wordResultPanel.setLayout(new GridLayout(2, 4, 0, 0));
    	
    	codeArea=new JTextArea();	
    	scrollPane=new JScrollPane(codeArea);
    	scrollPane.setViewportView(codeArea);
    	
    	codePanel=new JPanel();
    	symbolTablePanel=new JPanel();
    	tokenTablePanel=new JPanel();
    	exceptionPanel=new JPanel();
    	
    	String[] tokenClownName={"符号","种别码"};
    	update_token_table=new DefaultTableModel(null, tokenClownName);
    	tokenTable=new JTable(update_token_table);
    	token_table_scroll=new JScrollPane(tokenTable);
    	token_table_scroll.setViewportView(tokenTable);
    	tokenTablePanel.add(token_table_scroll);
    	
    	String[] symbolClownName={
    			"单词名","长度","种属"
    	};
    	update_symbol_table=new DefaultTableModel(null, symbolClownName);
    	symbolTable=new JTable(update_symbol_table);
    	symbol_table_scroll=new JScrollPane(symbolTable);
    	symbol_table_scroll.setViewportView(symbolTable);
    	symbolTablePanel.add(symbol_table_scroll);
    	
    	String[] exceptionClownName={"错误类型","行数","列数"};
    	update_exception_table=new DefaultTableModel(null,exceptionClownName);
    	exceptionTable=new JTable(update_exception_table);
    	exception_table_scroll=new JScrollPane(exceptionTable);
    	exception_table_scroll.setViewportView(exceptionTable);
    	exceptionPanel.add(exception_table_scroll);
        
        menuToolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		codePanel.add(scrollPane);
		codePanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		codePanel.setLayout(new BorderLayout(0,0));
		codePanel.add(scrollPane, BorderLayout.CENTER);
		wordResultPanel.add(codePanel);
		
		symbolTablePanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		wordResultPanel.add(symbolTablePanel);
		
		tokenTablePanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		wordResultPanel.add(tokenTablePanel);
		
		exceptionPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		wordResultPanel.add(exceptionPanel);
    }
    
    /**
     * 初始化句法分析面板与其控件
     */
    public void initSyntaxResult(){
    	syntaxResultPanel=new JPanel();
    	syntaxResultPanel.setLayout(new GridLayout(1, 2));
    	
    	syntaxPanel=new JPanel();
    	syntaxExceptionPanel=new JPanel();
    	
    	String[] syntaxClownName={"句子内容"};
    	update_syntax_table=new DefaultTableModel(null, syntaxClownName);
    	syntaxTable=new JTable(update_syntax_table);
    	syntax_table_scroll=new JScrollPane(syntaxTable);
    	syntax_table_scroll.setViewportView(syntaxTable);
    	syntaxPanel.add(syntax_table_scroll);
    	
    	String[] exceptionClownName={"错误类型","行数","列数"};
    	update_syntaxException_table=new DefaultTableModel(null,exceptionClownName);
    	syntaxExceptionTable=new JTable(update_syntaxException_table);
    	syntaxException_table_scroll=new JScrollPane(syntaxExceptionTable);
    	syntaxException_table_scroll.setViewportView(syntaxExceptionTable);
    	syntaxExceptionPanel.add(syntaxException_table_scroll);
    	
    	syntaxResultPanel.add(syntaxPanel);
    	syntaxResultPanel.add(syntaxExceptionPanel);
    }
    
    /**
     * 给控件设置各种事件
     */
    public void initEvent(){
    	/**
    	 * 选择源文件
    	 */
    	openItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("open file");
				// TODO Auto-generated method stub
				javax.swing.JFileChooser jf = new javax.swing.JFileChooser();  
				jf.showOpenDialog(null);
		        java.io.File file=jf.getSelectedFile();
		        /**
		         * 将对于改文件的引用保留
		         */
		        nowSelectedFile=file;
		        
		        if(file!=null){
		        	codeArea.setText(FileHandleServer.ReadSourceFile(file));
		        }
		        
		        nowAnalysisStage=READ_FILE_COMPLETE;
			}
		});
    	
    	/**
    	 * 保存修改后的源文件
    	 */
    	saveItem.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(nowAnalysisStage==READ_FILE_COMPLETE){
					String content=codeArea.getText();
					System.out.println(content);
					
					FileHandleServer.WriteFile(nowSelectedFile, content);
					JOptionPane.showMessageDialog(rootPane, "文件保存成功");
				}else{
					mJOptionPane.showMessageDialog(MainPage.this, "请先打开分析文件");
				}
			}
		});
    	
    	/**
    	 * 将修改后的源文件另存为
    	 */
    	saveasItem.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(nowAnalysisStage==READ_FILE_COMPLETE){
					JFileChooser jFileChooser=new JFileChooser();
					jFileChooser.showSaveDialog(rootPane);
					
					File file=jFileChooser.getSelectedFile();
					
					String content=codeArea.getText();
					System.out.println(content);
					
					if(file!=null){
						FileHandleServer.WriteFile(file, content);
						JOptionPane.showMessageDialog(rootPane, "文件保存成功");
					}
				}else{
					mJOptionPane.showMessageDialog(MainPage.this, "请先打开分析文件");
				}
			}
		});
    	
    	/**
    	 * 给autoItem设置点击事件
    	 */
    	autoItem.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(nowAnalysisStage==READ_FILE_COMPLETE){
					System.out.println("begin analysis");
					mCardLayout.show(parentPanel, "wordResult");
					handleSourceFile();
					
			    	//改变当前分析阶段
			    	nowAnalysisStage=WORD_ANALYSIS_STAGE;
				}else{
					mJOptionPane.showMessageDialog(MainPage.this, "请先打开分析文件");
				}
			}
		});
    	
    	manualItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
    	
    	//给句法分析的menuItem设置点击事件
    	syntaxAnalysis.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("begin syntaxAnalysis");
				
				if(nowAnalysisStage==WORD_ANALYSIS_STAGE){
					mCardLayout.show(parentPanel, "syntaxResult");
					mSyntaxRecognizeServer.analysis();
					
					initSyntaxPanelUi();
					
					nowAnalysisStage=SYNTAX_ANALYSIS_STAGE;
				}else{
					mJOptionPane.showMessageDialog(MainPage.this,"请先进行词法分析");
				}
			}
		});
    }
    
    /**
     * 初始化字符识别服务
     */
    public void initServer(){
    	mCharacterRecognizeServer=
    			CharacterRecognizeServer.getCharaterRecognizeServer();
    	
		mExceptionTableHandlerServer=
				ExceptionTableHandlerServer.getExceptionTypeInServer();
		
		mSymbolTableHandleServer=
				SymbolTableHandleServer.getSymbolTableHandleServer();
		
		mTokenTableHandleServer=
				TokenTableHandleServer.getTokenTableHandleServer();
		
		mSyntaxRecognizeServer=
				SyntaxRecognizeServer.getSynaxRecofnizeServer();
    	
    	this.nowRow=1;
    }
    
    /**
     * 按行读取源程序交由CharacterRecognizeServer处理
     */
    public void handleSourceFile(){
    	BufferedReader bf=null;
    	try {
			bf=new BufferedReader(
					new FileReader(nowSelectedFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/**
    	 *按行读取数据
    	 */
    	String readtemp=null;
    	try {
			while((readtemp=bf.readLine())!=null){
				mCharacterRecognizeServer.analysis(readtemp, nowRow);
				nowRow++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/**
    	 * 使用词法分析完成后所获得的数据填充UI
    	 */
    	initUiWithData(mSymbolTableHandleServer.getSymbolList(),
    			mExceptionTableHandlerServer.getExceptionTable(),
    			mTokenTableHandleServer.getTokenList());
    }
    
    
    /**
     * 使用数据填充ui的方法
     * @param symbolList
     * @param exceptionList
     * @param tokenList
     */
    public void initUiWithData(List<Symbol> symbolList,
    		List<Exception> exceptionList,List<JavaWordSurface> tokenList){
    	System.out.println("initUiWithData");
    	/**
    	 * 填充tokenTable
    	 */
    	int p=0;
    	String[] tokenClownName={"符号","种别码"};
    	Object[][] tokenData=new Object[tokenList.size()][2];
    	
    	for(int i=0;i<tokenList.size();i++){
    		tokenData[i][0]=tokenList.get(i).getWord();
    		if(tokenData[i][0]=="id" || tokenData[i][0]=="整常数" || tokenData[i][0]=="实常数"
    				|| tokenData[i][0]=="字符常数"){
    			tokenData[i][0]=mSymbolTableHandleServer.getValue(symbolList.get(p).getLex_head());
    			
    			p++;
    		}
    		tokenData[i][1]=tokenList.get(i).getSeedCode();
    		
    		System.out.println("符号:"+tokenData[i][0]+" "+"种别码:"+tokenData[i][1]);
    	}
    	
    	update_token_table.setDataVector(tokenData, tokenClownName);
    	
    	/**
    	 * 填充symbolTable
    	 */
    	String[] symbolClownName={
    			"单词名","长度","种属"
    	};
    	Object[][] symbolData=
    			new Object[symbolList.size()][3];
    	
    	for(int i=0;i<symbolList.size();i++){
    		symbolData[i][0]=mSymbolTableHandleServer.getValue(symbolList.get(i).getLex_head());
    		symbolData[i][1]=symbolList.get(i).getLength();
    		symbolData[i][2]=symbolList.get(i).getType();
    		
    		System.out.println("单词名:"+symbolData[i][0]+
    				"长度:"+symbolData[i][1]+"种属:"+symbolData[i][2]);
    	}
    	
    	update_symbol_table.setDataVector(symbolData,symbolClownName);
    	
    	/**
    	 * 填充exceptionTable
    	 */
    	String[] exceptionClownName={"错误类型","行数","列数"};
    	Object[][] exceptionData=
    			new Object[exceptionList.size()][3];
    	
    	for(int i=0;i<exceptionList.size();i++){
    		exceptionData[i][0]=switchExceptionTypeToChinese(
    				exceptionList.get(i).getExceptionType()
    				);
    		exceptionData[i][1]=exceptionList.get(i).getRow();
    		exceptionData[i][2]=exceptionList.get(i).getClown();
    		
    		System.out.println("错误类型:"+exceptionData[i][0]+
    				"行数:"+exceptionData[i][1]+"列数:"+exceptionData[i][1]);
    	}
    	
    	update_exception_table.setDataVector(exceptionData, exceptionClownName);
    	//repaint();
    }
    
    /**
     * 使用分析完成的数据修改syntaxResultPanel的Ui
     */
    public void initSyntaxPanelUi(){
    	/**
    	 * 填充syntaxTable
    	 */
    	List<List<JavaWordSurface>> syntaxList=mSyntaxRecognizeServer.getSyntaxList();
    	String[] syntaxClownName={"句子内容"};
    	Object[][] syntaxData=new Object[syntaxList.size()][1];
    	
    	for(int i=0;i<syntaxList.size();i++){
    		for(int j=0;j<syntaxList.get(i).size();j++){
    			System.out.println(syntaxList.get(i).get(j).getWord());
    		}
    	}
    	
    	for(int i=0;i<syntaxList.size();i++){
    		StringBuilder sb=new StringBuilder();
    		List<JavaWordSurface> listTemp=syntaxList.get(i);
    		
    		for(int j=0;j<listTemp.size();j++){
    			sb.append(listTemp.get(j).getWord());
    			sb.append(" ");
    		}
    		
    		System.out.println("第"+i+"条语句是"+sb.toString());
    		syntaxData[i][0]=sb.toString();
    	}
    	
    	update_syntax_table.setDataVector(syntaxData, syntaxClownName);
    	
    	/**
    	 * 填充syntaxExceptionTable
    	 */
    	List<Exception> exceptionList=mExceptionTableHandlerServer.getSyntaxExceptionTable();
    	String[] syntaxExceptionClownName={"错误类型","行数","列数"};
    	Object[][] exceptionData=
    			new Object[exceptionList.size()][3];
    	
    	for(int i=0;i<exceptionList.size();i++){
    		exceptionData[i][0]=switchExceptionTypeToChinese(
    				exceptionList.get(i).getExceptionType()
    				);
    		exceptionData[i][1]=exceptionList.get(i).getRow();
    		exceptionData[i][2]=exceptionList.get(i).getClown();
    		
    		System.out.println("错误类型:"+exceptionData[i][0]+
    				"行数:"+exceptionData[i][1]+"列数:"+exceptionData[i][1]);
    	}
    	
    	update_syntaxException_table.setDataVector(exceptionData, syntaxExceptionClownName);
    }
    
    /**
     * 将错误类型转换为中文显示
     * @param type
     */
    public String switchExceptionTypeToChinese(int type){
    	String chineseResult = null;
    	
    	switch (type) {
		case Exception.ILEGAL_BYTE:
			chineseResult="非法字符";
			break;
		case Exception.CHARACTER_CONSTANT_OVER_LENGTH:
		    chineseResult="字符常数超长";
		    break;
		case Exception.ANNOTATION_OVER_LENGTH:
		    chineseResult="注释超长";
		    break;
		case Exception.ILEGAL_OPERATOR:
		    chineseResult="非法操作符";
		    break;
		case Exception.WORD_FORMATION_ERROR:
		    chineseResult="构词错误";
		    break;
		case Exception.ILLEGAL_SYNTAX:
			chineseResult="不合法的句法";
			break;
		case Exception.UNEXCEPT_BEGIN:
			chineseResult="不在分析范围内的句法";
			break;
		}
    	
    	return chineseResult;
    }
    
    public static void main(String[] args){
    	System.out.println("application launch");
    	MainPage mainPage=new MainPage();
    	mainPage.setTitle("java 编译器");
    	mainPage.setSize(1920, 1080);
    	mainPage.setLocationRelativeTo(null);
		mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPage.setVisible(true);
    }
}
