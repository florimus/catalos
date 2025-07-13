package com.commerce.catalos.rest;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders buildHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        return httpHeaders;
    }

    public <T> ResponseEntity<T> get(
            String baseUrl,
            String endpoint,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders(headers));
        return restTemplate.exchange(baseUrl + endpoint, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> post(
            String baseUrl,
            String endpoint,
            Map<String, String> headers,
            Object body,
            Class<T> responseType
    ) {
        HttpEntity<Object> entity = new HttpEntity<>(body, buildHeaders(headers));
        return restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> put(
            String baseUrl,
            String endpoint,
            Map<String, String> headers,
            Object body,
            Class<T> responseType
    ) {
        HttpEntity<Object> entity = new HttpEntity<>(body, buildHeaders(headers));
        return restTemplate.exchange(baseUrl + endpoint, HttpMethod.PUT, entity, responseType);
    }

    public <T> ResponseEntity<T> delete(
            String baseUrl,
            String endpoint,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders(headers));
        return restTemplate.exchange(baseUrl + endpoint, HttpMethod.DELETE, entity, responseType);
    }
}
