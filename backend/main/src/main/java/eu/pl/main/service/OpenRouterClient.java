package eu.pl.main.service;

import eu.pl.main.dto.openrouter.OpenRouterChatRequest;
import eu.pl.main.dto.openrouter.OpenRouterChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class OpenRouterClient {

    private final WebClient webClient;

    public OpenRouterClient(WebClient.Builder webClientBuilder,
                            @Value("${openrouter.api.url}") String baseUrl,
                            @Value("${openrouter.api.key}") String apiKey) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Mono<OpenRouterChatResponse> sendChatRequest(OpenRouterChatRequest request) {
        return webClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenRouterChatResponse.class)
            .timeout(Duration.ofSeconds(30)); // Set a reasonable timeout
    }
}
