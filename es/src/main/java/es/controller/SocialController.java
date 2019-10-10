package es.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.model.domain.Social;
import es.service.SocialUseCase;
import reactor.core.publisher.Mono;

@RestController
public class SocialController {
	@Autowired
	SocialUseCase socialUseCase;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Void> add(@RequestBody Social social) throws IOException {
		return socialUseCase.addDocument(social);
	}
}
