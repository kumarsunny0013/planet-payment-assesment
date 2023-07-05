package com.planet.assesment.controller;

import com.planet.assesment.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/v1/orders")
    private ResponseEntity<String> saveOrders() throws IOException {
        try {
            orderService.saveOrders("src/main/resources/test_file_2.csv");
        } catch (Exception exception) {
            // Just added for simplicity. We generally use Global Exception Handler for handling exception globally.
            return ResponseEntity.internalServerError().body("FAILED");
        }
        return ResponseEntity.ok().body("SUCCESS");
    }

    @GetMapping("/v2/orders")
    private ResponseEntity<String> saveOrdersV2() throws IOException {
        try {
            orderService.saveOrdersMultithreading("src/main/resources/test_file_2.csv");
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("FAILED");
        }
        return ResponseEntity.ok().body("SUCCESS");
    }

}
