package es;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.rest.RestStatus;

import es.model.dao.InstagramDAO;
import es.model.domain.InstagramDTO;

public class Test {

	public static void main(String[] args) {
		InstagramDAO dao = InstagramDAO.getInstance();
		InstagramDTO dto = new InstagramDTO(2L, "집에 가고 싶다");
		try {
			if(dao.addInstagram(dto.getId().toString(), dto)) {
				System.out.println("저장 성공");
			}
		} catch (ElasticsearchException e) {
		    if (e.status() == RestStatus.CONFLICT) {
			    System.out.println("저장 실패 : 이미 존재하는 데이터");
		    } else {
		    	System.out.println("저장 실패 : 엘라스틱 서치 오류");
		    	e.printStackTrace();
		    }
		} catch (Exception e) {
			System.out.println("저장 실패 : 기타 오류");
			e.printStackTrace();
		}
	}

}
