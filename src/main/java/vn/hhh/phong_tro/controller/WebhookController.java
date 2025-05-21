package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hhh.phong_tro.dto.request.post.PostFilterRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.post.PostList;
import vn.hhh.phong_tro.service.PostService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("👉 Full request body from Dialogflow:");
//            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request));

            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            if (queryResult == null) {
                System.out.println("⚠️ Không có queryResult trong payload");
                return ResponseEntity.ok(Map.of("fulfillmentText", "Lỗi: queryResult không tồn tại."));
            }

            Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");
            if (parameters == null) {
                System.out.println("⚠️ Không có parameters trong queryResult");
                return ResponseEntity.ok(Map.of("fulfillmentText", "Lỗi: parameters không tồn tại."));
            }

            System.out.println("📦 Parameters:");
            parameters.forEach((key, value) -> System.out.println(key + ": " + value));

            String location = (String) parameters.get("location");
            String roomType = (String) parameters.get("room-type");
//            Double price = parameters.get("number") != null ? Double.valueOf(parameters.get("number").toString()) : null;


//            System.out.println("💰 Price: " + price);

            PostFilterRequest filter = new PostFilterRequest();
//            if (price != null) {
//                filter.setMinPrice(BigDecimal.valueOf(price - 1000000));
//                filter.setMaxPrice(BigDecimal.valueOf(price + 1000000));
//            }
            if (roomType != null) {
                filter.setTypeId(Long.valueOf(roomType));
            }
            if (location != null) {
                filter.setCityId(Long.valueOf(location));
            }


            Pageable pageable = PageRequest.of(0, 3);
            PageResponse<?> response = postService.advanceSearch(filter, pageable, null);

            List<PostList> posts = (List<PostList>) response.getItems();
            StringBuilder reply = new StringBuilder();
            if (posts.isEmpty()) {
                reply.append("Xin lỗi, hiện không có bài đăng phù hợp với yêu cầu của bạn.");
            } else {
                for (PostList post : posts) {
                    reply.append("🏠 ").append(post.getTitle()).append("\n")
                            .append("📍 ").append(post.getAddress()).append("\n")
                            .append("💰 ").append(post.getPrice()).append(" VNĐ\n\n");
                }
            }

            Map<String, Object> fulfillment = new HashMap<>();
            fulfillment.put("fulfillmentText", reply.toString());

            return ResponseEntity.ok(fulfillment);
        } catch (Exception e) {
            System.out.println("❌ Lỗi xử lý webhook:");
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("fulfillmentText", "Đã xảy ra lỗi xử lý yêu cầu."));
        }
    }
}
