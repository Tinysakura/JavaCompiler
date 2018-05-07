package entity;

/**
 * java单词编码的数据结构
 * @author Administrator
 *
 */
public class JavaWordSurface {
	private String word;
	private int seedCode;
	
	public JavaWordSurface(){
		
	}
	
	public JavaWordSurface(String word,int seedCode){
		this.word=word;
		this.seedCode=seedCode;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getSeedCode() {
		return seedCode;
	}

	public void setSeedCode(int seedCode) {
		this.seedCode = seedCode;
	}

}
