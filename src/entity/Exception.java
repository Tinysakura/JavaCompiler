package entity;

/**
 * 描述错误信息的entity
 * @author Administrator
 *
 */
public class Exception {
	/**
	 * 关于错误类型的static数据
	 * 目前只包含可以在词法分析阶段被检测出的错误
	 */
	//非法字符
	public static final int ILEGAL_BYTE=1;
    //字符常量超长
	public static final int CHARACTER_CONSTANT_OVER_LENGTH=2;
	//注释超长
	public static final int ANNOTATION_OVER_LENGTH=3;
	//非法运算符
	public static final int ILEGAL_OPERATOR=4;
	//构词错误
	public static final int WORD_FORMATION_ERROR=5;
	
	/**
	 * 语法分析阶段可以检测到的错误
	 */
	//不在预期中的句法
	public static final int UNEXCEPT_BEGIN=6;
	//不合法的句法
	public static final int ILLEGAL_SYNTAX=7;
	
	private int clown;
	private int row;
	private int exceptionType;
	
	public Exception(){
		
	}
	
	public Exception(int row,int clown,int exceptionType){
		this.row=row;
		this.clown=clown;
		this.exceptionType=exceptionType;
	}
	
	public Exception(int exceptionType){
		this.exceptionType=exceptionType;
	}

	public int getClown() {
		return clown;
	}

	public void setClown(int clown) {
		this.clown = clown;
	}

	public int getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(int exceptionType) {
		this.exceptionType = exceptionType;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

}
