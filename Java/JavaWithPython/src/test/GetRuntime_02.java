package test;

import java.io.IOException;

public class GetRuntime_02 {
	public static void main(String[] args) {
		// https://codechacha.com/ko/java-examples-how-to-get-current-path/
		String path = System.getProperty("user.dir"); // 폴더 패스 잡기
		System.out.println("Working Directory = " + path);
		String command = "cmd.exe /c start " + path + "\\src\\test.py";
		/*
		  	[test.py]
		  	from time import sleep
			print("HELLO JAVA")
			sleep(5)
			print("I'm Python")
			sleep(5)
		 */
		Process pr = null;
		try {
			pr = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pr.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pr.destroy();
		}
	}
}
