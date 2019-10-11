package es.model.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InstagramDTO {
	@NonNull
	Long id;
	@NonNull
	String contents;
	List<String> hashtags;
	int like;
	int comment;

	public Map<String, String> getMap() {
		HashMap<String, String> map = new HashMap<>();
		map.put("id", this.id.toString());
		map.put("contents", this.contents);
		map.put("hashtags", this.hashtags != null ? this.hashtags.toString() : null);
		map.put("like", Integer.toString(like));
		map.put("comment", Integer.toString(comment));
		return map;
	}
}
