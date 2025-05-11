package vn.hhh.phong_tro.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.util.OrderStatus;
import vn.hhh.phong_tro.util.TransactionType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.hhh.phong_tro.util.TransactionType.TOP_UP;

@Service
@RequiredArgsConstructor
public class VNPayService {
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    private final UserService userService;
    private  final PayService payService;
    private final PostService postService;

    public String createPaymentUrl(BigDecimal amount, String orderInfo, TransactionType type,Integer userId,Integer postId, HttpServletRequest request) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderType = "other";
        String vnp_TxnRef ="";
        if(type == TOP_UP){
             vnp_TxnRef = "topup_" + userId + "_" + System.currentTimeMillis();
        }
        else {
            vnp_TxnRef = "vip_" + userId + "_"+ postId + "_" + System.currentTimeMillis();
        }

        String vnp_IpAddr = request.getRemoteAddr();
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100)).intValue())); // VNP yêu cầu nhân 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Sort params
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            if (value != null) {
                hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                query.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            }
        }
        // Remove last &
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return vnp_PayUrl + "?" + query.toString();
    }
    public ResponseEntity<?> processVNPayReturn(Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");
        String amountStr = params.get("vnp_Amount");

        if (!"00".equals(responseCode)) {
            return ResponseEntity.status(400).body("Thanh toán thất bại hoặc bị hủy");
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
            String[] parts = txnRef.split("_");
            String type = parts[0];

            switch (type) {
                case "topup":
                    return handleTopUp(parts, amount);
                case "vip":
                    return handleVipPurchase(parts, amount);
                default:
                    return ResponseEntity.badRequest().body("Loại giao dịch không hợp lệ");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi xử lý callback: " + e.getMessage());
        }
    }

    private ResponseEntity<?> handleTopUp(String[] parts, BigDecimal amount) {
        Integer userId = Integer.parseInt(parts[1]);
//        User user = userService.findById(userId);
        payService.updateBalance(userId, amount);
        payService.record(userId, TransactionType.TOP_UP, amount, "VNPay", "Nạp tiền VNPay");

        return ResponseEntity.ok("Nạp tiền thành công");
    }

    private ResponseEntity<?> handleVipPurchase(String[] parts, BigDecimal amount) {

        Integer userId = Integer.parseInt(parts[1]);
        Integer postId = Integer.parseInt(parts[2]);

        payService.createOrder(userId, postId, amount, "VNPay", OrderStatus.COMPLETED);
        payService.record(userId, TransactionType.PAYMENT, amount, "VNPay", "Mua bài VIP qua VNPay");
//        Post post = postService.getById(Long.valueOf(postId));
//        post.setIsVip(6);
//        post.setVipExpiryDate(LocalDateTime.now().plusDays(7));
//        postService.save(post);
        return ResponseEntity.ok("Mua VIP thành công");
    }
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hash = hmac512.doFinal(data.getBytes());
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating HMAC SHA512", e);
        }
    }
}
