package es;

import java.util.HashMap;

import org.elasticsearch.ElasticsearchException;

import es.model.dao.InstagramDAO;
import es.model.domain.InstagramDTO;

public class Test {
	static InstagramDAO dao = InstagramDAO.getInstance();

	public static void main(String[] args) {
//		추가 (index API)
// 		https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-index.html
//		InstagramDTO instagram = new InstagramDTO(5L, "인생은 고기서 고기");
//		instagram.setHashtags(Arrays.asList("#쇠고기", "#돼지고기", "#닭고기"));
//		addInstagram(instagram);
		
//		검색 (get API)
// 		https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-index.html
//		System.out.println(findInstagram(1L));
//		System.out.println(findInstagram(3L));
//		System.out.println(findInstagram(1000L));
		
//		확인 (Exists API)		
//		System.out.println(ExistInstagram(1L));
//		System.out.println(ExistInstagram(1000L));

//		삭제 (Delete API)
//		addInstagram(new InstagramDTO(10L, "삭제될 것이다"));
//		System.out.println(existInstagram(10L));
//		System.out.println(findInstagram(10L));
//		deleteInstagram(10L);
//		System.out.println(existInstagram(10L));
//		System.out.println(findInstagram(10L));
//		deleteInstagram(10L);

//		수정(Update API)
// 		https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-update.html
		addInstagram(new InstagramDTO(20L, "수정될 것이다"));
		System.out.println(findInstagram(20L));
		HashMap<String, Object> map = new HashMap<>();
		map.put("contents", "수정되었다");
		map.put("comment", 7);
		updateInstagram(20L, map);
		System.out.println(findInstagram(20L));
		deleteInstagram(20L);
	}
	
	private static void updateInstagram(Long id, HashMap<String, Object> map) {
		if(!existInstagram(id)) {
			System.out.println("수정 실패 : 존재하지 않는 데이터");
			return;
		}
		try {
			dao.updateIntagramById(id, map);
		} catch (ElasticsearchException e) {
		    System.out.println("수정 실패 : 엘라스틱 서치 오류");
		    e.printStackTrace();
		} catch (Exception e) {
			System.out.println("수정 실패 : 기타 오류");
			e.printStackTrace();
		}
	}

	private static void deleteInstagram(Long id) {
		if(!existInstagram(id)) {
			System.out.println("삭제 실패 : 존재하지 않는 데이터");
			return;
		}
		try {
			if(dao.deleteInstagramById(id)) {
				System.out.println("삭제 성공");
			} else {
				System.out.println("삭제 실패");
			}
		} catch (ElasticsearchException e) {
		    System.out.println("검색 실패 : 엘라스틱 서치 오류");
		    e.printStackTrace();
		} catch (Exception e) {
			System.out.println("검색 실패 : 기타 오류");
			e.printStackTrace();
		}
	}

	public static boolean existInstagram(Long id) {
		try {
			return dao.existInstagramById(id);
		} catch (ElasticsearchException e) {
		    System.out.println("검증 실패 : 엘라스틱 서치 오류");
		    e.printStackTrace();		
		} catch (Exception e) {
			System.out.println("검증 실패 : 기타 오류");
			e.printStackTrace();
		}
		return false;
	}

	public static InstagramDTO findInstagram(Long id) { 
		if(!existInstagram(id)) {
			System.out.println("검색 실패 : 존재하지 않는 데이터");
			return null;
		}
		try {
			return dao.findInstagramById(id);
		} catch (ElasticsearchException e) {
		    System.out.println("검색 실패 : 엘라스틱 서치 오류");
		    e.printStackTrace();
		} catch (Exception e) {
			System.out.println("검색 실패 : 기타 오류");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addInstagram(InstagramDTO instagram) {
		if(existInstagram(instagram.getId())) {
			System.out.println("저장 실패 : 존재하는 데이터");
			return;
		}
		try {
			if(dao.addInstagram(instagram.getId().toString(), instagram)) {
				System.out.println("저장 성공");
			}
		} catch (ElasticsearchException e) {
	    	System.out.println("저장 실패 : 엘라스틱 서치 오류");
	    	e.printStackTrace();
		} catch (Exception e) {
			System.out.println("저장 실패 : 기타 오류");
			e.printStackTrace();
		}
	}
	
}
