package vn.hhh.phong_tro.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient openAiWebClient;

    private final String schemaInfo = """
        Tables:
        posts(id, user_id, title, description, price, area, address, type_id, is_vip, vip_expiry_date, status, created_at, updated_at, name_contact, phone_contact)
        post_types(id, code, name)
        categories(id, name)
        post_category(post_id, category_id)
        post_images(id, post_id, image_url, is_thumbnail)
        cities(id, name)
        districts(id, name, city_id)
        wards(id, name, district_id)
        posts_addresses(id, post_id, detail_address, ward_id, latitude, longitude, geo_hash)

        Relationships:
        - posts.type_id → post_types.id
        - posts.user_id → users.id
        - post_category: post_id → posts.id, category_id → categories.id
        - posts_addresses.post_id → posts.id, ward_id → wards.id
        - wards.district_id → districts.id, districts.city_id → cities.id
        """;

    public String generateSqlFromQuestion(String naturalLanguageQuestion) {
        String prompt = String.format("""
            You are a helpful assistant that writes SQL queries based on the following schema.
            %s

            Question: %s

            SQL:
            """, schemaInfo, naturalLanguageQuestion);

        Map<String, Object> requestBody = Map.of(
//                "model", "gpt-3.5-turbo",
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that writes MySQL queries."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        return openAiWebClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("choices").get(0).get("message").get("content").asText())
                .block();
    }

}
