package com.struggle.sys.matcher;

import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author strugglesnail
 * @date 2021/3/3
 * @desc
 */
public class MyServerWebExchange implements ServerWebExchange {
    @Override
    public ServerHttpRequest getRequest() {
        return new ServerHttpRequest() {
            @Override
            public HttpHeaders getHeaders() {
                return null;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return null;
            }

            @Override
            public String getMethodValue() {
                return null;
            }

            @Override
            public URI getURI() {
                return null;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public RequestPath getPath() {
                return null;
            }

            @Override
            public MultiValueMap<String, String> getQueryParams() {
                return null;
            }

            @Override
            public MultiValueMap<String, HttpCookie> getCookies() {
                return null;
            }
        };
    }

    @Override
    public ServerHttpResponse getResponse() {
        return new ServerHttpResponse() {
            @Override
            public HttpHeaders getHeaders() {
                return null;
            }

            @Override
            public DataBufferFactory bufferFactory() {
                return null;
            }

            @Override
            public void beforeCommit(Supplier<? extends Mono<Void>> supplier) {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> publisher) {
                return null;
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> publisher) {
                return null;
            }

            @Override
            public Mono<Void> setComplete() {
                return null;
            }

            @Override
            public boolean setStatusCode(HttpStatus httpStatus) {
                return false;
            }

            @Override
            public HttpStatus getStatusCode() {
                return null;
            }

            @Override
            public MultiValueMap<String, ResponseCookie> getCookies() {
                return null;
            }

            @Override
            public void addCookie(ResponseCookie responseCookie) {

            }
        };
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Mono<WebSession> getSession() {
        return null;
    }

    @Override
    public <T extends Principal> Mono<T> getPrincipal() {
        return null;
    }

    @Override
    public Mono<MultiValueMap<String, String>> getFormData() {
        return null;
    }

    @Override
    public Mono<MultiValueMap<String, Part>> getMultipartData() {
        return null;
    }

    @Override
    public LocaleContext getLocaleContext() {
        return null;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return null;
    }

    @Override
    public boolean isNotModified() {
        return false;
    }

    @Override
    public boolean checkNotModified(Instant instant) {
        return false;
    }

    @Override
    public boolean checkNotModified(String s) {
        return false;
    }

    @Override
    public boolean checkNotModified(String s, Instant instant) {
        return false;
    }

    @Override
    public String transformUrl(String s) {
        return null;
    }

    @Override
    public void addUrlTransformer(Function<String, String> function) {

    }

    @Override
    public String getLogPrefix() {
        return null;
    }
}
