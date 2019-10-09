package test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Apply {
	public static void main(String[] args) {
		String kwd = "돈까스";
		try {
			makeFile(kwd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String makeCorpus(String kwd) {
		System.out.println("== 말뭉치 생성 시작 ==");
		Selenium s = Selenium.start();
		ArrayList<String> contents = new ArrayList<>();
		String corpus = "";
		try {
			s.access("https://www.instagram.com/explore/tags/@/?hl=ko".replace("@", kwd));
			sleep(4);
			System.out.println("== 인기 게시글 ==");
			s.findAll("//*[@id=\"react-root\"]/section/main/article/div/div/div/div/div/a")
			.stream().forEach(v -> contents.add(v.getAttribute("href")));			
			System.out.println("== 최근 사진 ==");
			s.findAll("//*[@id=\"react-root\"]/section/main/article/div/div/div/div/a")
			.stream().forEach(v -> contents.add(v.getAttribute("href")));
			System.out.println("== 말뭉치 생성 시작 ==");
			for(String url : contents) {
				s.access(url);
				sleep(0.3);
				corpus += s.find("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/div[1]/ul/div/li/div/div/div[2]/span")
						.getText().replace("\n", "");
				corpus += "\n";
				System.out.println(new StringBuilder().append(url).append(" ").append(corpus.length()).toString());
			}
		} finally {
			s.quit();
		}
		System.out.println("== 말뭉치 생성 완료 ==");
		return corpus;
	}

	static void makeFile(String kwd) throws IOException {
		// https://csw7432.tistory.com/entry/Java-Input-Output-Stream
		// https://negafix.tistory.com/entry/Java로-UTF-8-파일-쓰기
		BufferedWriter bWriter = null;
		try {
			bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("corpus_" + kwd + ".txt"), "UTF-8"), 1024);
			bWriter.write(makeCorpus(kwd));
		} finally {
			bWriter.close();
		}
	}
	
	static void sleep(double i) {
		try {
			Thread.sleep((long) (i * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
