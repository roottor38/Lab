package es.db.elastic;

import java.io.IOException;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionListenerResponseHandler;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import es.model.domain.Social;
import es.service.SocialUseCase;
import reactor.core.publisher.Mono;

@Component
public class ElasticsearchProvider implements SocialUseCase {

	private final RestHighLevelClient restHighLevelClient;

	public ElasticsearchProvider(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public Mono<Void> addDocument(Social social) throws IOException {
		// index - type 1:1 대응 (6.1부터) so 인덱스로만 리퀘스트
		IndexRequest indexRequest = new IndexRequest("social").source("id", social.getId(), "contents", social.getContents(),
				"hashtag", social.getHashtag(), "like", social.getLike(), "comment", social.getComment());
		return Mono.create(sink -> {
			ActionListener<IndexResponse> actionListener = new ActionListener<IndexResponse>() {
				@Override
				public void onResponse(IndexResponse response) {
					sink.success();
				}
				@Override
				public void onFailure(Exception e) {
				}
			};
			restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, actionListener);
		});
	}
}
