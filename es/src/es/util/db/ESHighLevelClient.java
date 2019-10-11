package es.util.db;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

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
	
	public boolean addDocument(String index, Map<String, String> map) throws IOException, ElasticsearchStatusException {
		IndexResponse indexResponse = client.index((new IndexRequest(index))
				.source(map).opType(DocWriteRequest.OpType.CREATE), RequestOptions.DEFAULT);
		return indexResponse.getResult() == DocWriteResponse.Result.CREATED ? true : false;
	}
	
	public boolean addDocument(String index, String id, Map<String, String> map) throws IOException, ElasticsearchStatusException {
		IndexResponse indexResponse = client.index((new IndexRequest(index))
				.id(id).source(map).opType(DocWriteRequest.OpType.CREATE), RequestOptions.DEFAULT);
		return indexResponse.getResult() == DocWriteResponse.Result.CREATED ? true : false;
	}
}
