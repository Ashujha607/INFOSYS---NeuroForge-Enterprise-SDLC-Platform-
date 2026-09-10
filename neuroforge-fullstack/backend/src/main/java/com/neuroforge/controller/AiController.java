package com.neuroforge.controller;

import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "openai/gpt-oss-20b";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key:}")
    private String apiKey;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userPrompt = payload.getOrDefault("prompt", "");
        if (userPrompt.trim().isEmpty()) {
            return Map.of("response", "Please provide a prompt.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            return Map.of("response", "AI service is not configured. Set GROQ_API_KEY before starting the backend.");
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            
            Object[] messages = new Object[] {
                Map.of("role", "system", "content", "You are NeuroForge AI, an expert SDLC & AI coding assistant integrated into the NeuroForge Enterprise platform. Provide concise, helpful responses to project management, software development, bug fixing, and task breakdown queries."),
                Map.of("role", "user", "content", userPrompt)
            };

            Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", messages
            );

            String jsonPayload = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROQ_API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                var jsonNode = objectMapper.readTree(response.body());
                String content = jsonNode.path("choices").get(0).path("message").path("content").asText();
                return Map.of("response", content);
            } else {
                return Map.of("response", "Groq API Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            return Map.of("response", "Error calling AI service: " + e.getMessage());
        }
    }
}
