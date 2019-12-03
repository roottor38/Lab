package test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bitbucket.eunjeon.seunjeon.Analyzer;

public class Apply {
	public static void main(String[] args) {
		String text = "요즘 예약 없이 오셨다가 돌아가시는 분들이 많아져서 진심으로 죄송한 말씀...드립니다...사전예약!해주시면.최대한 시간 맞춰서 예약해드리겠습니다^^감사합니다!^^\r\n" + 
				"예쁜 한옥사진은 전부 #경복궁사진관 입니다^^";
        HashMap<String, Integer> counter = new HashMap<>();
        count(counter, nounsExtract(text));
        getResult(counter, 5).forEach(System.out::println);
	}

	static Stream<Entry<String, Integer>> getResult(HashMap<String, Integer> map, int limit) {
		// 최종 결과 반환 (limit 설정 가능)
		return map.entrySet().stream()
	        	.sorted(Comparator.comparing((Entry<String, Integer>::getValue)).reversed())
	        	.limit(limit);
	}
	
	static Stream<Entry<String, Integer>> getResult(HashMap<String, Integer> map) {
		// 최종 결과 반환
		return map.entrySet().stream()
	        	.sorted(Comparator.comparing((Entry<String, Integer>::getValue)).reversed()); // https://nkcnow.tistory.com/219
	}
	
	static List<String> nounsExtract(String text) {
		// 명사 추출
		return StreamSupport.stream(Analyzer.parseJava(text).spliterator(), false) // iterable to stream (https://www.baeldung.com/java-iterable-to-stream)
	       		.filter(v -> v.morpheme().getFeatureHead().equals("NNG") || v.morpheme().getFeatureHead().equals("NNP")) // 일반명사(NNG), 고유명사(NNP) 필터링
	       		.map(v -> v.morpheme().getSurface()) // 필터링 된 것에 원래 단어를 매핑
	       		.sorted()
	       		.collect(Collectors.toList());
	}
	
	static void count(HashMap<String, Integer> map, List<String> list) {
		// 카운터에 추가 (외부 for-loop를 가정)
		for (String v : list) {
			if (map.containsKey(v)) {
				map.put(v, map.get(v) + 1);
			} else {
				map.put(v, 1);
			}
		}
	}
}
