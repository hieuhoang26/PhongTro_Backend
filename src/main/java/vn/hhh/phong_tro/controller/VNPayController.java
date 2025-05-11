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

    // 2. Xử lý callback từ VNPay
//    @GetMapping("/return")
//    public ResponseEntity<?> handleReturn(@RequestParam Map<String, String> allParams) {
//        String vnp_ResponseCode = allParams.get("vnp_ResponseCode");
//        String vnp_Amount = allParams.get("vnp_Amount");
//        String vnp_TxnRef = allParams.get("vnp_TxnRef");
//        String vnp_SecureHash = allParams.get("vnp_SecureHash");
//
//        // TODO: validate chữ ký (nếu cần)
//        if ("00".equals(vnp_ResponseCode)) {
//            BigDecimal amount = new BigDecimal(vnp_Amount).divide(BigDecimal.valueOf(100));
//
//            return ResponseEntity.ok("Nạp tiền thành công");
//        }
//
//        return ResponseEntity.status(400).body("Thanh toán thất bại hoặc bị hủy");
//    }

}

