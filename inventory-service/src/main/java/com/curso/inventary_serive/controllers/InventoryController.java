package com.curso.inventary_serive.controllers;

import com.curso.inventary_serive.model.dtos.BaseResponse;
import com.curso.inventary_serive.model.dtos.OrderItemResquest;
import com.curso.inventary_serive.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku") String sku) {
        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse listIsInStock(@RequestBody List<OrderItemResquest> orderItemResquest) {
        return inventoryService.areInStock(orderItemResquest);
    }
}
