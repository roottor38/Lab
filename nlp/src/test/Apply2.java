package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bitbucket.eunjeon.seunjeon.Analyzer;

public class Apply2 {
	public static void main(String[] args) {
		try {
			frequency(fileRead("돈까스").orElse("내용 없음")).forEachOrdered(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static Stream<Entry<String, Long>> frequency(String text) {
		// 명사 추출
		return StreamSupport.stream(Analyzer.parseJava(text).spliterator(), false) // iterable to stream (https://www.baeldung.com/java-iterable-to-stream)
	       		.filter(v -> v.morpheme().getFeatureHead().equals("NNG") || v.morpheme().getFeatureHead().equals("NNP")) // 일반명사(NNG), 고유명사(NNP) 필터링
	       		.map(v -> v.morpheme().getSurface()) // 필터링 된 것에 원래 단어를 매핑
	       		.collect(Collectors.groupingBy(String::toString, Collectors.counting()))
	       		// 같은 문자열끼리 그룹핑 후 카운트 값 매핑 (https://palpit.tistory.com/652)
	       		.entrySet().stream().sorted(Comparator.comparing((Entry<String, Long>::getValue)).reversed()); // 내림 차순으로 정렬
	}
	
	static Optional<String> fileRead(String kwd) throws IOException {
		return Files.readAllLines(Paths.get("corpus_" + kwd + ".txt"), StandardCharsets.UTF_8).stream().reduce((v1, v2) -> v1 + "\n" + v2);	
	}
}
