package es.util.db;

import lombok.Getter;

@Getter
public enum ESTool {
	// http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
	IP("localhost"),
	PORT(9200);
	
	private String text;
	private int num;
	
	ESTool(String text) {
		this.text = text;
	}
	
	ESTool(int num) {
		this.num = num;
	}
}
