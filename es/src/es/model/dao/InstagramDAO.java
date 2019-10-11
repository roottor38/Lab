package es.model.dao;

import es.model.domain.InstagramDTO;
import es.util.db.ESHighLevelClient;

public class InstagramDAO {
	static InstagramDAO instance = new InstagramDAO();
	ESHighLevelClient client = ESHighLevelClient.getInstance();
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
}
