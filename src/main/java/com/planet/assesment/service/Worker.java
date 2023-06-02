package com.planet.assesment.service;

import com.planet.assesment.repository.OrderEntity;
import com.planet.assesment.repository.OrderRepository;
import com.planet.assesment.utility.OrderUtils;

import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    private final BlockingQueue<String> queue;
    private final OrderRepository orderRepository;

    public Worker(BlockingQueue<String> queue, OrderRepository orderRepository) {
        this.queue = queue;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = queue.take()) != null) {
                String[] fields = line.split(",");

                OrderEntity orderEntity = OrderEntity.builder()
                        .id(Long.valueOf(fields[0].trim()))
                        .email(fields[1].trim())
                        .phoneNumber(fields[2].trim())
                        .parcelWeight(Double.valueOf(fields[3].trim()))
                        .country(OrderUtils.determineCountryByPhone(fields[2].trim()))
                        .build();
                orderRepository.save(orderEntity);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

