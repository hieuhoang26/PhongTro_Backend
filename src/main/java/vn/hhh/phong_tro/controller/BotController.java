package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hhh.phong_tro.dto.request.BotRequest;
import vn.hhh.phong_tro.service.OpenAiService;

import java.util.Map;
@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotController {

    private final OpenAiService openAiService;
    @PostMapping
    public ResponseEntity<Map<String, String>> chatWithGpt(@RequestBody BotRequest request) {
        String userMessage = request.getMessage();

        // Gọi OpenAI GPT API ở đây
        String response = openAiService.getResponse(userMessage); // tạo service này

        return ResponseEntity.ok(Map.of("reply", response));
    }
}
