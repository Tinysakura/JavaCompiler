package bean;

import entity.JavaWordSurface;

/**
 * 句法bean的接口，用于工厂模式创建syntaxbean
 * @author Administrator
 *
 */
public interface SyntaxBean{
	/**
	 * 设置bean开始分析的java符号在token表中的位置
	 */
	public void setStarIndex(int index);
	
	/**
	 * 初始化first集与follow集
	 */
	public void initFirstAndFollow();
	
	/**
	 * 开始分析
	 */
	public void parsing();
	
	/**
	 * 出错处理，返回出错时读取token表的定位码
	 */
	public int onError();
	
	/**
	 * 未出错情况下返回句法分析完成时读取token表的定位码
	 */
	public int onComplete();
	
	/**
	 * 判断lookAhead是否在某个特定的first集中
	 */
	public boolean inFirts(String[] first);
	
	/**
	 * 判断lookAhead是否在某个特定的follow集中
	 */
	public boolean inFollow(String[] follow);
	
	/**
	 * 判断读入的终结符是否符合句法的要求
	 */
	public void match(String operatorObject);
	
	/**
	 * 修改指针获取next lookAhead
	 */
	public JavaWordSurface getNextLooKAhead();

}
