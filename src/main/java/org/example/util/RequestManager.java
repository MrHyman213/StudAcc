package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Map;

public class RequestManager {

    public static HttpRequest post(String url, Object body, Map<String, String> headers){
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(body)));
            for (Map.Entry<String, String> entry: headers.entrySet())
                request.setHeader(entry.getKey(), entry.getValue());
            return request.build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpRequest put(String url, Object body, Map<String, String> headers){
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(body)));
            for (Map.Entry<String, String> entry: headers.entrySet())
                request.setHeader(entry.getKey(), entry.getValue());
            return request.build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpRequest get(String url, Map<String, String> headers){
        HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url)).GET();
        for (Map.Entry<String, String> entry: headers.entrySet())
            request.setHeader(entry.getKey(), entry.getValue());
        return request.build();
    }

    public static HttpRequest delete(String url, Map<String, String> headers) {
        HttpRequest.Builder request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE();
        for (Map.Entry<String, String> entry: headers.entrySet())
            request.setHeader(entry.getKey(), entry.getValue());
        return request.build();
    }

}
