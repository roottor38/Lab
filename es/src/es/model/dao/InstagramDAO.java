package es.model.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.model.domain.InstagramDTO;
import es.util.db.ESHighLevelClient;

public class InstagramDAO {
	private static InstagramDAO instance = new InstagramDAO();
	private ESHighLevelClient client = ESHighLevelClient.getInstance();
	final String INDEX = "instagram";
	
	private InstagramDAO () {};
	
	public static InstagramDAO getInstance() {
		return instance;
	}
	
	public boolean addInstagram(String id, InstagramDTO instagram) throws Exception {
		client.connect();
		try {
			return client.addDocument(INDEX, id, instagram.getMap());
		} finally {
			client.close();			
		}
	}
	
	public InstagramDTO findInstagramById(Long id) throws Exception {
		InstagramDTO instagram = null;
		client.connect();
		try {
			Map<String, Object> map = client.findDocumentById(INDEX, id.toString());
			instagram = new InstagramDTO(id,
					map.get("contents").toString(),
					map.get("hashtags") != null
					? Arrays.asList(map.get("hashtags").toString().replaceAll("[\\[\\]]", "").split(","))
							: null,
					Integer.parseInt(map.get("like").toString()),
					Integer.parseInt(map.get("comment").toString())
					);
			if(map.get("hashtags") != null) {
				;
			}
		} finally {
			client.close();			
		}
		return instagram;
	}

	public boolean existInstagramById(Long id) throws Exception {
		client.connect();
		try {
			return client.existDocument(INDEX, id.toString());
		} finally {
			client.close();
		}
	}

	public boolean deleteInstagramById(Long id) throws Exception {
		client.connect();
		try {
			return client.deleteDocument(INDEX, id.toString());
		} finally {
			client.close();			
		}
	}

	public boolean updateIntagramById(Long id, HashMap<String, Object> map) throws Exception {
		client.connect();
		try {
			return client.updatetDocument(INDEX, id.toString(), map);
		} finally {
			client.close();
		}		
	}
	
}
