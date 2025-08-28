package joliebank.romanellas.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public class HttpSendCallback {

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public void sendCallback(String token, String status) throws Exception {
        System.out.println("Sending status " + status + " to " + token);
        String path = "/payments/" + token + "/status/";
        String url = "https://soap.joliebank.romanellas.cloud" + path;

        String json = "{\"status\":\"" + status + "\"}";

        HttpRequest.Builder rb = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");

        System.out.println("=== HTTP REQUEST ===");
        System.out.println("URL: " + url);
        System.out.println("Method: PUT");
        rb.build().headers().map().forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("Body: " + json);
        System.out.println("====================");

        HttpRequest request = rb.PUT(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpResponse<Void> resp = http.send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println("=== HTTP RESPONSE ===");
        System.out.println("Status: " + resp.statusCode());
        resp.headers().map().forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("Body: " + resp.body());
        System.out.println("====================");

        System.out.println("Sent status " + status + " to " + token);
    }

}
