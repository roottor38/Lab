package regex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

public class Test {
	public static void main(String[] args) {
		final String url = "http://api.visitkorea.or.kr/guide/inforArea.do?langtype=KOR&arrange=A&mode=listOk&pageNo=1";
		
		// group으로
//		final String regex1 = "contentId=(\\d*)&langtype=KOR&typeid=(\\d*)";
//		try {
//			Jsoup.connect(url).get().select(".galleryList > li > a")
//					.stream().map(v -> findGroup(v.attr("href"), regex1))
//					.forEach(Test::result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// find-while로
		final String regex2 = "d=(\\d*)";
		try {
			Jsoup.connect(url).get().select(".galleryList > li > a")
				.stream()
				.map(v -> findAll(v.attr("href"), regex2))
				.forEach(Test::result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void result(List<String> list) {
		System.out.printf("contentId : %s, typeId : %s\n", list.get(0), list.get(1));
	}
	
	public static List<String> findGroup(String text, String regex) {
		Matcher m = Pattern.compile(regex).matcher(text);
		m.find();
		List<String> list = new ArrayList<>();
		for(int i = 1; i <= m.groupCount(); i++) {
			list.add(m.group(i));
		}
		return list;
	}
	
	public static List<String> findAll(String text, String regex) {
		Matcher m = Pattern.compile(regex).matcher(text);
		List<String> list = new ArrayList<>();
		while(m.find()) {		
			if(m.group(1).length() != 0) {				
				list.add(m.group(1));
			}
		}
		return list;
	}
}
