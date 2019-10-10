package es.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class Social {
	Long id;
	String contents;
	List<String> hashtag;
	int like;
	int comment;
}
