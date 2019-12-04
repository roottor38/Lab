package test;

public class GetRuntime_01 {
	public static void main(String[] args) {
		// https://roadrunner.tistory.com/214
		try{
		    Runtime.getRuntime().exec("notepad.exe");
		}catch(Exception e){
		    System.out.println(e);
		}
	}
}
