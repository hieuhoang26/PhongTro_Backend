package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate;


    public String getResponse(String userMessage) {
        String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
        String OPENAI_API_KEY = "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";


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", userMessage));
        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(OPENAI_API_URL, request, (Class<Map<String, Object>>)(Class<?>)Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, String> message = (Map<String, String>) firstChoice.get("message");

            return message.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't process your request.";
        }
    }

}
