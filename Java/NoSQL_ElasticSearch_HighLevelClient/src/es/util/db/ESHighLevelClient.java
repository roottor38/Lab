package es.util.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

public class ESHighLevelClient {
	private static RestHighLevelClient client = null;
	private static ESHighLevelClient instance = new ESHighLevelClient();
	
	private ESHighLevelClient() {};
	
	public static ESHighLevelClient getInstance() {
		return instance;
	}
	
	public void connect() {
		client = new RestHighLevelClient(
			        RestClient.builder(
			                new HttpHost(ESTool.IP.getText(), ESTool.PORT.getNum(), "http")));
	}
	
	public void close() throws IOException {
		client.close();
	}
	
	public boolean existDocument(String index, String id) throws IOException {
		GetRequest getRequest = new GetRequest(index, id);
			getRequest.fetchSourceContext(new FetchSourceContext(false)); 
			getRequest.storedFields("_none_");
		return client.exists(getRequest, RequestOptions.DEFAULT);
	}
	
	public boolean addDocument(String index, Map<String, String> map, boolean opt) throws IOException {
		IndexResponse indexResponse;
		if(opt) {			
			indexResponse = client.index((new IndexRequest(index))
					.source(map).opType(DocWriteRequest.OpType.CREATE), RequestOptions.DEFAULT);
		} else {
			indexResponse = client.index((new IndexRequest(index))
					.source(map), RequestOptions.DEFAULT);
		}
		return indexResponse.getResult() == DocWriteResponse.Result.CREATED ? true : false;
	}
	
	public boolean addDocument(String index, Map<String, String> map) throws IOException {
		IndexResponse indexResponse = client.index((new IndexRequest(index))
				.source(map).opType(DocWriteRequest.OpType.CREATE), RequestOptions.DEFAULT);
		return indexResponse.getResult() == DocWriteResponse.Result.CREATED ? true : false;
	}
	
	public boolean addDocument(String index, String id, Map<String, String> map) throws IOException {
		IndexResponse indexResponse = client.index((new IndexRequest(index))
				.id(id).source(map).opType(DocWriteRequest.OpType.CREATE), RequestOptions.DEFAULT);
		return indexResponse.getResult() == DocWriteResponse.Result.CREATED ? true : false;
	}
	
	public Map<String, Object> findDocumentById(String index, String id) throws IOException {
		GetResponse getResponse = client.get(new GetRequest(index, id), RequestOptions.DEFAULT);
		return getResponse.isExists() ? getResponse.getSourceAsMap() : null;
	}

	public boolean deleteDocument(String index, String id) throws IOException {
		DeleteResponse deleteResponse = client.delete(new DeleteRequest(index, id), RequestOptions.DEFAULT);
		return deleteResponse.getResult() == DocWriteResponse.Result.DELETED ? true : false;
	}

	public boolean updatetDocument(String index, String id, HashMap<String, Object> map) throws IOException {
		UpdateResponse updateResponse = client.update(new UpdateRequest(index, id)
				.doc(map), RequestOptions.DEFAULT);
		return updateResponse.getResult() == DocWriteResponse.Result.UPDATED ? true : false;
	}
}
