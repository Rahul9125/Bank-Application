package org.ecommerce.ecommerce.serviceimpl;

import org.ecommerce.ecommerce.dtos.OrderItemDto;
import org.ecommerce.ecommerce.dtos.OrderResponseDto;
import org.ecommerce.ecommerce.entity.OrderItem;
import org.ecommerce.ecommerce.entity.Product;
import org.ecommerce.ecommerce.entity.Users;
import org.ecommerce.ecommerce.entity.Order;
import org.ecommerce.ecommerce.repository.OrderRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.serviceimpl.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)

public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeAll
    static void staticSetUp() { }


    @AfterEach
    public void tearDown() {  }

    @AfterAll
    static void staticTearDown() { }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderResponsesByUserId () {
//        Mockito.when(orderRepository.findByUserId(Mockito.anyInt(), PageRequest.of(0, 5))).
//                thenReturn(create a order page);

        int userId = 1;
            Pageable pageable = PageRequest.of(0, 5);

            Product product = new Product();
            product.setName("Test Product");
            product.setPrice(100.0);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(2);

            Users user = new Users();
            user.setId(userId);

            Order order = new Order();
            order.setId(101);
            order.setOrderDate(LocalDateTime.now());
            order.setUser(user);
            order.setItems(List.of(orderItem));

            Page<Order> orderPage = new PageImpl<>(List.of(order));

            //Mock Behaviour
            when(orderRepository.findByUserId(userId, pageable)).thenReturn(orderPage);

            // Act
            Page<OrderResponseDto> result = orderService.getOrderResponsesByUserId(userId, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());

            OrderResponseDto response = result.getContent().get(0);
            assertEquals(101, response.getOrderId());
            assertEquals(userId, response.getUserId());
            assertEquals(200.0, response.getTotalAmount());
            assertEquals(1, response.getItems().size());

            OrderItemDto itemDto = response.getItems().get(0);
            assertEquals("Test Product", itemDto.getProductName());
            assertEquals(100.0, itemDto.getPrice());
            assertEquals(2, itemDto.getQuantity());
        }

}
