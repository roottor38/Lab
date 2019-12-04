package test;

import java.io.IOException;

public class GetRuntime_03 {
	public static void main(String[] args) {
		String path = System.getProperty("user.dir"); // 폴더 패스 잡기
		System.out.println("Working Directory = " + path);
		String command = "cmd.exe /c start " + path + "\\src\\test2.py";
		String param = "Don't worry";
		/*
		 	[test2.py]
		 	from time import sleep
			import sys
			print("HELLO JAVA")
			sleep(2)
			print(str(sys.argv))
			sleep(5)
		 */
		// https://m.blog.naver.com/jhnyang/221407227360
		callPython(command, param);
		String[] arg = new String[5];
		arg[0] = "cmd.exe";
		arg[1] = "/c";
		arg[2] = "start";
		arg[3] = path + "\\src\\test2.py";
		arg[4] = "Be happy";
		callPythonArr(arg);
	}

	public static void callPython(String command, String param) {
		Process pr = null;
		try {
			pr = Runtime.getRuntime().exec(command + " " + param);
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

	private static void callPythonArr(String[] arg) {
		Process pr = null;
		try {
			pr = Runtime.getRuntime().exec(arg);
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
