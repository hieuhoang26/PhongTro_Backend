package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.Order;
import vn.hhh.phong_tro.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);

//    boolean existsByPaymentMethod(String transactionCode);
}

