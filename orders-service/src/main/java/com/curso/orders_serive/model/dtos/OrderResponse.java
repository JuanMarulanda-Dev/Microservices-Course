package com.curso.orders_serive.model.dtos;

import java.util.List;

/*¿Qué es un Record en Java? Un record es esencialmente una clase que define una estructura de datos con campos,
 pero a diferencia de una clase normal, un record es inmutable
 (es decir, sus campos no pueden ser modificados una vez que se han inicializado).*/
public record OrderResponse(Long id, String orderNumber, List<OrderItemResponse> orderItems) {
}
