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
            cities(id, name) -- city, e.g. 'Hà Nội'
            districts(id, name, city_id)  -- district (quận/huyện), e.g. 'QuậnCầu Giấy' 
            wards(id, name, district_id) -- ward (phường/xã), e.g. 'Phường Dịch Vọng'
            posts_addresses(id, post_id, detail_address, ward_id, latitude, longitude, geo_hash)

            Relationships:
            - posts.type_id → post_types.id
            - posts.user_id → users.id
            - post_category: post_id → posts.id, category_id → categories.id
            - posts_addresses.post_id → posts.id, ward_id → wards.id
            - wards.district_id → districts.id, districts.city_id → cities.id
            Notes:
            - City (Tỉnh/Thành phố) is in table 'cities'
            - District (Quận/Huyện) is in table 'districts'
            - Ward (Phường/Xã) is in table 'wards'
            - Example: 'Quận Cầu Giấy' = districts.name = 'Cầu Giấy', cities.name = 'Hà Nội'
            """;

    public String generateSqlFromQuestion(String naturalLanguageQuestion) {
        String prompt = String.format("""
                You are a helpful assistant that writes MySQL queries based on the schema below.
                Schema:
                %s

                The user will ask about posts and location filters (e.g. city, district, ward).
                Remember:
                - get at least fields:  id,title, price,area, address, name_contact, phone_contact
                - If user says "Quận Cầu Giấy", it means districts.name = 'Quận Cầu Giấy'
                - If user says "Phường Dịch Vọng", it means wards.name = 'Phường Dịch Vọng'
                - If user says "Hà Nội", it means cities.name = 'Hà Nội'
                
                Now, write a SELECT SQL query that answers this question:
                Question: %s
                SQL:
                """, schemaInfo, naturalLanguageQuestion);


        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
//                "model", "gpt-4o",
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

    public String askQuestionNormally(String question) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", question)
                ),
                "temperature", 0.7
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
