package com.struggle.sys.util;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author strugglesnail
 * @date 2021/3/2
 * @desc
 */
public class GreetingWebClient {

    private WebClient client = WebClient.create("http://localhost:8003");
    private Mono<ClientResponse> result = client.get().uri("/1").accept(MediaType.APPLICATION_JSON).exchange();
    public String getResult() {
        return result.flatMap(res -> res.bodyToMono(String.class)).block();
    }
}
