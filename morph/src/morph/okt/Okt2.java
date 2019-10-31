package morph.okt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;

public class Okt2 {
	public static void main(String[] args) {
		List<String> corpus = null;
		try {
			System.out.println("// 인스타 크롤링 + 정규화");
			corpus =  getInstaPosts("탕수육");
		} catch (IOException e) {
			e.printStackTrace();
		}
		corpus.forEach(System.out::println);
		System.out.println("// Hashtag");
		makeListByTag("Hashtag", corpus).forEach(System.out::println);
		System.out.println("// Noun");
		List<List<String>> nounList = makeListByTag("Noun", corpus);
		nounList.forEach(System.out::println);
		System.out.println("// Noun Fequency");
		makeFreqTable(10, nounList).forEach(System.out::println);
	}
	
	public static List<String> getInstaPosts(String tag) throws IOException {
		return Jsoup.connect("https://www.instazu.com/tag/" + tag).get()
				.select(".box-photo").stream()
				.map(v -> OpenKoreanTextProcessorJava.normalize(
						v.selectFirst(".photo-description").text()).toString())
				.collect(Collectors.toList());
	}
	
	public static List<List<String>> makeListByTag(String tag, List<String> list) {
		return list.stream()
				.map(v -> OpenKoreanTextProcessorJava.tokenize(v))
				.map(v -> OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(v))
				.map(v -> v.stream().filter(v2 -> v2.getPos().toString().equals(tag))
							.map(v2 -> v2.getText())
							.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}
	
	public static Stream<Entry<String, Long>> makeFreqTable(int num, List<List<String>> list) {
		List<String> all = new ArrayList<>();
		for(List<String> v : list) {
			all.addAll(v);
		}
		return all.stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()))
				.entrySet().stream()
				.sorted(Comparator.comparing((Entry<String, Long>::getValue)).reversed())
				.limit(num);
	}
}
