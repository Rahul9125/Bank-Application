package org.ecommerce.ecommerce.serviceimpl;

//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.ecommerce.ecommerce.dtos.*;
import org.ecommerce.ecommerce.entity.Order;
import org.ecommerce.ecommerce.entity.OrderItem;
import org.ecommerce.ecommerce.entity.Product;
import org.ecommerce.ecommerce.entity.Users;
import org.ecommerce.ecommerce.feignclient.BankClient;
import org.ecommerce.ecommerce.handleexceptions.InsufficientStockException;
import org.ecommerce.ecommerce.handleexceptions.ResourceNotFoundException;
import org.ecommerce.ecommerce.repository.OrderRepository;
import org.ecommerce.ecommerce.repository.ProductRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BankClient bankClient;
    @Value("${ecommerce.bankaccount}")
    private String toAccountNo;
    @Transactional
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequestDto itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
//            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());

            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemDto.getQuantity();
        }

        // Bank transfer
        FundTransferRequest transferRequest = new FundTransferRequest();
        transferRequest.setFromAccount(request.getAccountNumber());
        transferRequest.setToAccount(Long.parseLong(toAccountNo));
        transferRequest.setAmount(totalAmount);
        bankClient.transferFund(transferRequest);

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Map to DTO
        List<OrderItemDto> itemDtos = savedOrder.getItems().stream().map(item -> {
            OrderItemDto dto = new OrderItemDto();
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).toList();

        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(savedOrder.getId());
        response.setOrderDate(savedOrder.getOrderDate().toString());
        response.setUserId(savedOrder.getUser().getId());
        response.setItems(itemDtos);
        response.setTotalAmount(totalAmount);

        return response;
    }

    @Override
    public Page<OrderResponseDto> getOrderResponsesByUserId(int userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);

        return orders.map(order -> {
            double totalAmount = order.getItems().stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
                OrderItemDto dto = new OrderItemDto();
                dto.setProductName(item.getProduct().getName());
                dto.setPrice(item.getProduct().getPrice());
                dto.setQuantity(item.getQuantity());
                return dto;
            }).toList();

            OrderResponseDto response = new OrderResponseDto();
            response.setOrderId(order.getId());
            response.setOrderDate(order.getOrderDate().toString());
            response.setUserId(order.getUser().getId());
            response.setItems(itemDtos);
            response.setTotalAmount(totalAmount);

            return response;
        });
    }


}
