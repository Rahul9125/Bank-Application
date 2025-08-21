package org.ecommerce.ecommerce.controller;


import org.ecommerce.ecommerce.dtos.OrderRequestDto;
import org.ecommerce.ecommerce.dtos.OrderResponseDto;
import org.ecommerce.ecommerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> orderPlace(@RequestBody OrderRequestDto request) {
        OrderResponseDto order = orderService.placeOrder(request);
        return ResponseEntity.ok(order);

    }

     @GetMapping("user/{id}")
     public ResponseEntity<Page<OrderResponseDto>> getOrderByUserId(
     @PathVariable int id,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "10") int size) {

        Page<OrderResponseDto> orders = orderService.getOrderResponsesByUserId(id, PageRequest.of(page, size));
         return ResponseEntity.ok(orders);
     }




}
