package Test;

public class A {
	private static int a=0;
	
	public A(){
		
	}
	
	public void change(){
		a++;
	}
	
	@Override
	public String toString(){
		return a+"";
	}
}
