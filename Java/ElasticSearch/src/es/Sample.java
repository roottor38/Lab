package es;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

public class Sample {
	// Guide : https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-document-index.html
	public static void main(String[] args) {
		try {
			test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void test() throws IOException {
		RestHighLevelClient client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http")));
		IndexRequest indexRequest = new IndexRequest("posts")
			    .id("1")
			    .source("user", "kimchy",
			        "postDate", new Date(),
			        "message", "trying out Elasticsearch")
			    .opType(DocWriteRequest.OpType.CREATE); // 기본 설정은 update
		IndexResponse indexResponse = null;
		try {
			 indexResponse = client.index(indexRequest, RequestOptions.DEFAULT); 
			 if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
				 System.out.println("저장 성공");
			 } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
				 System.out.println("수정 성공");
			 }
		} catch(ElasticsearchException e) {
		    if (e.status() == RestStatus.CONFLICT) {
			    System.out.println("저장 실패 : 이미 존재하는 데이터");
		    } else {
		    	e.printStackTrace();
		    }
		}
		client.close();
	}
}
