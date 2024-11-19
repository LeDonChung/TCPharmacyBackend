package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderDetail;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderDetailId;

import java.sql.Timestamp;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    // Truy vấn chi tiết đơn hàng từ các đơn hàng trong 3 tháng gần nhất
    @Query("SELECT od FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.orderDate >= :threeMonthsAgo")
    List<OrderDetail> findOrderDetailsFromLastThreeMonths(@Param("threeMonthsAgo") Timestamp threeMonthsAgo);

    List<OrderDetail> findAllByOrderIdIn(List<Long> ids);


    @Query("SELECT od FROM OrderDetail od WHERE od.medicine.id IN :ids")
    List<OrderDetail> findByProductId(List<Long> ids);
}