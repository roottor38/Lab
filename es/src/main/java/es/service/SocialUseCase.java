package es.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import es.model.domain.Social;
import reactor.core.publisher.Mono;

@Service
public interface SocialUseCase {
	public Mono<Void> addDocument(Social social) throws IOException;
}
