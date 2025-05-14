package vn.hhh.phong_tro.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.service.UserService;
import vn.hhh.phong_tro.service.VNPayService;
import vn.hhh.phong_tro.util.TransactionType;
import vn.hhh.phong_tro.util.Uri;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(Uri.Pay)
@RequiredArgsConstructor
public class VNPayController {

     private final VNPayService vnpayService;
     private final UserService userService;

    // 1. Tạo URL thanh toán
    @PostMapping("/vnpay")
    public ResponseEntity<?> createPayment(@RequestParam BigDecimal amount,
                                           @RequestParam TransactionType type,
                                           @RequestParam Integer userId,
                                           @RequestParam(required = false) Integer postId,
                                           HttpServletRequest request) {
        try {
            String orderInfo = "Nạp tiền vào tài khoản";

            String paymentUrl = vnpayService.createPaymentUrl(amount, orderInfo,type,userId,postId, request);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi tạo thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/return")
    public ResponseEntity<?> handleReturn(@RequestParam Map<String, String> allParams) {
        return vnpayService.processVNPayReturn(allParams);
    }

    // Hiển thị kết quả trên trình duyệt người dùng (không xử lý logic)
//    @GetMapping("/return")
//    public ResponseEntity<String> handleReturnUI(@RequestParam Map<String, String> params) {
//        String result = "Thanh toán " + ("00".equals(params.get("vnp_ResponseCode")) ? "thành công" : "thất bại");
//        return ResponseEntity.ok(result);
//    }
//
//    // IPN (server-to-server): xử lý chính xác giao dịch
//    @PostMapping("/ipn")
//    public ResponseEntity<?> handleIpn(@RequestParam Map<String, String> params) {
//        return vnPayService.processVNPayReturn(params);
//    }

}

