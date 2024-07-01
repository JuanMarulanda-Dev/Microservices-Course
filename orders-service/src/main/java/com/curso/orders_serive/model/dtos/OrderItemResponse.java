package com.curso.orders_serive.model.dtos;

public record OrderItemResponse(Long id, String sku, Double price, Long quanitity) {
}
