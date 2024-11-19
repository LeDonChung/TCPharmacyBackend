package vn.edu.iuh.fit.pharmacy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.pharmacy.POJOs.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN o.orderDetails as od WHERE  od.medicine.id IN :ids")
    List<Order> findAllByProductIdIn(List<Long> ids);
}