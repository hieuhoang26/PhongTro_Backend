package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hhh.phong_tro.dto.request.BotRequest;
import vn.hhh.phong_tro.service.OpenAiService;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotController {

    private final OpenAiService openAiService;

    private final JdbcTemplate jdbcTemplate;
    @PostMapping
    public ResponseEntity<?> handleNaturalLanguageQuery(@RequestBody Map<String, String> body) {
        String question = body.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing question"));
        }

        String rawSql = openAiService.generateSqlFromQuestion(question);
        String sql = extractSql(rawSql);
        System.out.println(sql);
        // Validation để tránh DROP/DELETE/ALTER
        String lowerSql = sql.toLowerCase();
        if (lowerSql.contains("drop ") || lowerSql.contains("delete ") || lowerSql.contains("alter ")) {
            return ResponseEntity.status(400).body(Map.of("error", "Unsafe SQL detected!", "sql", sql));
        }

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(Map.of(
                    "sql", sql,
                    "result", result
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "sql", sql
            ));
        }
    }
    /**
     * Tách câu lệnh SQL trong đoạn text có format markdown ```sql ... ```
     * Nếu không tìm thấy sẽ cố gắng tìm câu lệnh bắt đầu bằng SELECT
     */
    public static String extractSql(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        // Tìm đoạn code block ```sql ... ```
        String startTag = "```sql";
        String endTag = "```";

        int start = text.indexOf(startTag);
        if (start != -1) {
            int sqlStart = start + startTag.length();
            int end = text.indexOf(endTag, sqlStart);
            if (end != -1) {
                String sql = text.substring(sqlStart, end).trim();
                return sql;
            }
        }

        // Nếu không có code block, cố tìm câu lệnh SELECT
        int selectIndex = text.toUpperCase().indexOf("SELECT");
        if (selectIndex != -1) {
            // Lấy từ SELECT đến hết đoạn text
            String sql = text.substring(selectIndex).trim();
            return sql;
        }

        // Không tìm thấy câu SQL
        return null;
    }
}

