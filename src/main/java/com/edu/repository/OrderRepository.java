package com.edu.repository;

import com.edu.entity.Order;
import com.edu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNo(String orderNo);
    
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByUserAndStatus(User user, Order.OrderStatus status);
}