package com.curso.orders_serive.services;

import com.curso.orders_serive.model.dtos.*;
import com.curso.orders_serive.model.entities.Order;
import com.curso.orders_serive.model.entities.OrderItem;
import com.curso.orders_serive.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placedOrder(OrderRequest orderRequest){

        //Check for inventory
        // HTTP clients
        // Rest Template -> Se encuentra en modo de mantenimiento
        // Web Client -> es una implementacion reactiva de un cliente HTTP.
        BaseResponse result= this.webClientBuilder.build()
                .post()
                .uri("http://localhost:8083/api/inventory/in-stock")
                .bodyValue(orderRequest.getOrderItems())
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();

        if (result == null || result.hasErrors()){
            throw new IllegalArgumentException("Some of the products are not in stock");
        }

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .build();

        order.setOrderItems(orderRequest.getOrderItems()
                .stream().map(item -> mapOrderItemRequestToOrderItem(item, order))
                .toList());

        this.orderRepository.save(order);
    }

    private OrderItem mapOrderItemRequestToOrderItem(OrderItemRequest orderItemRequest, Order order) {
        return OrderItem.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();

        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber()
                , order.getOrderItems().stream().map(this::mapToOrderItemRequest).toList());
    }

    private OrderItemResponse mapToOrderItemRequest(OrderItem orderItems) {
        return new OrderItemResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }
}
