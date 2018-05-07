package entity;

/**
 * 符号表中每个条目对应的entity
 * @author Administrator
 *
 */
public class Symbol {
	/**
	 * static data
	 * 关于类型与种属的分类的一些常量
	 */
	//简单变量
	public static final int SIMPLE_VARIETY=1;
	//整型常量
	public static final int TIDY_CONSTANT=2;
	//浮点型常量
	public static final int FLOAT_CONSTANT=3;
	//字符常量
	public static final int CHARACTER_CONSTANT=4;
	
	//private int id;
	/**
	 * 符号开头字符在lex数组中的位置
	 */
	private int lex_head;
	
	private int type;
	private int length;
	private int genus;
	private String value;
	/**
	 * 16进制作的内存地址
	 */
	private int ram;
	
	public Symbol(){
		
	}
	
	public Symbol(int lex_head,int type,int length){
		this.lex_head=lex_head;
		this.type=type;
		this.length=length;
	}
	
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public int getLex_head() {
		return lex_head;
	}
	public void setLex_head(int lex_head) {
		this.lex_head = lex_head;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getGenus() {
		return genus;
	}
	public void setGenus(int genus) {
		this.genus = genus;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
