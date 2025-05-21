package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.hhh.phong_tro.dto.response.statistic.RevenueStatistic;
import vn.hhh.phong_tro.model.Transaction;
import vn.hhh.phong_tro.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByDescription(String transactionCode);


    //    Statistic
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'TOP_UP'")
    long countTopUpTransactions();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'PAYMENT'")
    BigDecimal sumTotalPaymentAmount();

    //    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as date, SUM(amount) as totalRevenue " +
//            "FROM transactions " +
//            "WHERE type = 'PAYMENT' " +
//            "GROUP BY DATE_FORMAT(created_at, '%Y-%m-%d') " +
//            "ORDER BY DATE_FORMAT(created_at, '%Y-%m-%d')", nativeQuery = true)
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, SUM(amount) as totalRevenue " +
            "FROM transactions " +
            "WHERE type = 'PAYMENT' " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> calculateRevenueByDateNative();


}

