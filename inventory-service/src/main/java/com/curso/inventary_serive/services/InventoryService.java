package com.curso.inventary_serive.services;

import com.curso.inventary_serive.model.dtos.BaseResponse;
import com.curso.inventary_serive.model.dtos.OrderItemResquest;
import com.curso.inventary_serive.model.entities.Inventory;
import com.curso.inventary_serive.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySku(sku);

        return inventory.filter(item -> item.getQuantity() > 0).isPresent();
    }

    public BaseResponse areInStock(List<OrderItemResquest> orderItems) {
        var errorList = new ArrayList<>();

        List<String> skus = orderItems.stream().map(OrderItemResquest::getSku).toList();

        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach(orderItem -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItem.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errorList.add("Product with sku " + orderItem.getSku() + " does not exist");
            } else if (inventory.get().getQuantity() < orderItem.getQuantity()) {
                errorList.add("Product with sku " + orderItem.getSku() + " has insufficient quantity");
            }
        });

        return !errorList.isEmpty() ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);

    }
}
