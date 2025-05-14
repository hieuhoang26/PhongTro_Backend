package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.response.pay.OrderDto;
import vn.hhh.phong_tro.dto.response.pay.TransactionDto;
import vn.hhh.phong_tro.model.*;
import vn.hhh.phong_tro.repository.OrderRepository;
import vn.hhh.phong_tro.repository.TransactionRepository;
import vn.hhh.phong_tro.repository.WalletRepository;
import vn.hhh.phong_tro.util.OrderStatus;
import vn.hhh.phong_tro.util.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayService {

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final OrderRepository orderRepository;

    private  final UserService userService;
    private final PostService postService;

    public Wallet getOrCreateWallet(Integer userId) {
        User  user = userService.getById(userId);
        return walletRepository.findByUser(user)
                .orElseGet(() -> {
                    Wallet wallet = new Wallet();
                    wallet.setUser(user);
                    wallet.setBalance(BigDecimal.ZERO);
                    return walletRepository.save(wallet);
                });
    }

    public BigDecimal getBalance(Integer userId) {
        return getOrCreateWallet(userId).getBalance();
    }

    public Wallet updateBalance(Integer userId, BigDecimal delta) {
        Wallet wallet = getOrCreateWallet(userId);
        wallet.setBalance(wallet.getBalance().add(delta));
        return walletRepository.save(wallet);
    }

    public TransactionDto record(Integer userId, TransactionType type, BigDecimal amount, String method, String desc) {
        User  user = userService.getById(userId);
        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setMethod(method);
        tx.setDescription(desc);
        transactionRepository.save(tx);
        return TransactionDto.builder()
                .id(tx.getId())
                .user(tx.getUser().getId())
                .type(tx.getType())
                .amount(tx.getAmount())
                .method(tx.getMethod())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    public List<TransactionDto> getHistory(Integer userId) {
        User  user = userService.getById(userId);
        List<Transaction> trans =  transactionRepository.findByUserOrderByCreatedAtDesc(user);
        return trans.stream().map(transaction -> {
            return TransactionDto.builder()
                    .id(transaction.getId())
                    .user(transaction.getUser().getId())
                    .type(transaction.getType())
                    .amount(transaction.getAmount())
                    .method(transaction.getMethod())
                    .description(transaction.getDescription())
                    .createdAt(transaction.getCreatedAt())
                    .build();
        }).toList();
    }
    public OrderDto createOrder(Integer userId, Integer postId, BigDecimal amount, String method, OrderStatus status) {
        User user = userService.getById(userId);
        Post post = postService.getById(Long.valueOf(postId));
        Order order = new Order();
        order.setUser(user);
        order.setPost(post);
        order.setAmount(amount);
        order.setPaymentMethod(method);
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }


    public List<OrderDto> getOrders(Integer userId) {
        User user = userService.getById(userId);
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public boolean isTransactionProcessed(String txnRef) {
        return transactionRepository.existsByDescription(txnRef);
//                || orderRepository.existsByPaymentMethod(txnRef);
    }


    private OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .user(order.getUser().getId())
                .post(order.getPost().getId())
                .paymentMethod(order.getPaymentMethod())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
