package org.ecommerce.ecommerce.service;

import org.ecommerce.ecommerce.dtos.OrderRequestDto;
import org.ecommerce.ecommerce.dtos.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto request);
    Page<OrderResponseDto> getOrderResponsesByUserId(int userId, Pageable pageable);

}
