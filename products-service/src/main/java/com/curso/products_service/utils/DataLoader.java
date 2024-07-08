package com.curso.products_service.utils;

import com.curso.products_service.model.entities.Product;
import com.curso.products_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;


    private List<Product> products = IntStream.range(3, 5)
            .mapToObj(index ->
                            Product.builder()
                            .sku("00000"+index)
                            .name("PC Gamer "+index)
                            .description("Bestr.PC "+index)
                            .price(1000.0).
                            status(true).build()
            ).toList();

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading Data...");
        if (productRepository.findAll().isEmpty()){
            productRepository.saveAll(products);
        }
        log.info("Data Loaded");
    }
}
