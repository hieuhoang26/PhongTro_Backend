package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.PayByWalletRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.dto.response.pay.OrderDto;
import vn.hhh.phong_tro.dto.response.pay.TransactionDto;
import vn.hhh.phong_tro.dto.response.user.UserDetailResponse;
import vn.hhh.phong_tro.model.Order;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.model.Transaction;
import vn.hhh.phong_tro.model.Wallet;
import vn.hhh.phong_tro.service.PayService;
import vn.hhh.phong_tro.util.OrderStatus;
import vn.hhh.phong_tro.util.TransactionType;
import vn.hhh.phong_tro.util.Uri;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(Uri.Pay)
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final PayService payService;

    /* Wallet */
    @GetMapping("/wallet")
    public ResponseEntity<BigDecimal> getWalletBalance(@RequestParam Integer userId) {
        BigDecimal balance = payService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }


    @Operation(summary = "Create order by wallet", description = "Thanh toán bằng ví và tạo đơn hàng")
    @PostMapping("/pay-by-wallet")
    public ResponseData<?> payByWallet(@RequestBody PayByWalletRequest request) {
        try {
            Integer orderId = payService.createOrderByWallet(
                    request.getUserId(),
                    request.getPostId(),
                    request.getAmount(),
                    request.getIsVip(),
                    request.getDateTime()
            );
            return new ResponseData<>(HttpStatus.OK.value(), "Thanh toán thành công", orderId);
        } catch (IllegalArgumentException e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống khi thanh toán");
        }
    }



    @PostMapping("/wallet-order")
    public ResponseEntity<Wallet> OrderByWallet(@RequestParam Integer userId, @RequestParam BigDecimal amount) {
        Wallet wallet = payService.updateBalance(userId, amount);
        return ResponseEntity.ok(wallet);
    }

    /* transactions */
    @Operation(summary = "Get transaction ", description = "")
    @GetMapping("/transaction")
    public ResponseData<?> getTransactionHistory(@RequestParam Integer userId) {
        try {
            List<TransactionDto> list = payService.getHistory(userId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get list transaction", list);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(summary = "recordTransaction ", description = "")
    @PostMapping("/transaction")
    public ResponseData<?> recordTransaction(
            @RequestParam Integer userId,
            @RequestParam TransactionType type,
            @RequestParam BigDecimal amount,
            @RequestParam String method,
            @RequestParam(required = false) String description) {
        try {
            TransactionDto tx = payService.record(userId, type, amount, method, description);
            return new ResponseData<>(HttpStatus.OK.value(), "recordTransaction", tx);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /* order */
    @PostMapping("/order")
    public ResponseData<?> createOrder(
            @RequestParam Integer userId,
            @RequestParam Integer postId,
            @RequestParam BigDecimal amount,
            @RequestParam String method,
            @RequestParam OrderStatus status) {

        try {
            OrderDto order = payService.createOrder(userId, postId, amount, method, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Create Order", order);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/order")
    public ResponseData<?>  getOrders(@RequestParam Integer userId) {

        try {
            List<OrderDto> orders = payService.getOrders(userId);
            return new ResponseData<>(HttpStatus.OK.value(), "recordTransaction", orders);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }



}
