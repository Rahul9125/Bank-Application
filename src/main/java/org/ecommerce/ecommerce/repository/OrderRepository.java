package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.entity.Order;
import org.ecommerce.ecommerce.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByUserId(int userId, Pageable pageable);

}