package com.commerce.test;
import com.commerce.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestClient {
    // Replace this with your actual Railway URL
    private static final String BASE_URL = "https://usc-csce548-blake-seekings-production.up.railway.app";
    private static final HttpClient client = HttpClient.newHttpClient();
    public static void main(String[] args) throws Exception {
        System.out.println("--- E-Commerce Cloud Tester ---");
        

        Category c = new Category(0, "Medical", "Health Stuff");
        POST(c, "categories");
        // DELETE("categories",catId);

    }

    private static String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            // Log the error (as a Database Engineer, you know how vital logs are!)
            System.err.println("Error serializing Customer to JSON: " + e.getMessage());
            return "{}";
        }
    }

    private static void GET(String table) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());
        } else {
            System.out.println("❌ Failed to fetch. Status: " + response.statusCode());
        }
    }

    private static void GET(String table, int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table + "/" + id))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println(response.body());
        } else {
            System.out.println("❌ Failed to fetch. Status: " + response.statusCode());
        }

    }

    private static void DELETE(String table, int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status Code: " + response.statusCode());
    }

    private static JsonNode POST(Object obj, String table) throws Exception {
        String json = toJson(obj);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + table))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(response);
        // JsonNode rootNode = mapper.readTree(response.body());
        // return rootNode;
        return null;
    }
}