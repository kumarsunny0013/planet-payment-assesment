package com.planet.assesment.service;

import com.planet.assesment.repository.OrderEntity;
import com.planet.assesment.repository.OrderRepository;
import com.planet.assesment.utility.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * OrderService Implementation class for processing the csv data and updating it in db.
 */

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final int NUM_THREADS = 5; // Number of worker threads

    /**
     * Below code is without multithreading. Added Async in method so that when we hit the api doesnt hold the thread . As execution will take time as it need to save 1M records
     *
     * @param csvFilePath
     * @throws IOException
     */
    @Override
    @Async
    public void saveOrders(String csvFilePath) throws IOException {
        log.info("Entry save Orders");
        long start = Instant.now().getEpochSecond();
        log.info("Start Time:" + LocalDateTime.now());
        try {
            List<OrderEntity> orderEntityList = processCSV(csvFilePath);

//        This is taking less time
            orderRepository.saveAllAndFlush(orderEntityList);
        } catch (IOException ioException) {
            log.error("Exception while reading the file");
            throw ioException;
        } catch (Exception exception) {
//            log.error(exception.getStackTrace().toString());
            throw exception;
        }

        log.info("End Time:" + LocalDateTime.now());
        long end = Instant.now().getEpochSecond();
        log.info("Total Time Taken " + (end - start));
    }

    /**
     * Reads the CSV file and create list of OrderEntity
     *
     * @param csvFilePath
     * @throws IOException
     */
    private List<OrderEntity> processCSV(String csvFilePath) throws IOException {
        List<OrderEntity> orderEntityList = new ArrayList<>();
//        Resource resource = resourceLoader.getResource("classpath:/" + csvFilePath);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            //      Skip header line
            reader.readLine();
            String line;
            //read line by line and create orderEntity Object
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                OrderEntity orderEntity = OrderEntity.builder()
                        .id(Long.valueOf(fields[0].trim()))
                        .email(fields[1].trim())
                        .phoneNumber(fields[2].trim())
                        .parcelWeight(Double.valueOf(fields[3].trim()))
                        .country(OrderUtils.determineCountryByPhone(fields[2].trim()))
                        .build();
                orderEntityList.add(orderEntity);
//            This takes more time
//            orderRepository.save(orderEntity);
            }
        }

        return orderEntityList;
    }

    /**
     * Below method uses one by one flushing to db in 5 parallel thread.
     * this takes less time but high CPU usage
     */
    @Override
    @Async
    public void saveOrdersMultithreading(String csvFilePath) throws IOException {
        log.info("Entry saveOrdersMultithreading");
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                queue.put(line);
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getStackTrace().toString());
        }

//        create 5 threads
        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.execute(new Worker(queue, orderRepository));
        }

        executorService.shutdown();
    }

//    @Override
//    public void saveOrders(String csvFilePath) throws IOException {
//        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
//        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
//        long start= Instant.now().getEpochSecond();
//        System.out.println("Start Time:"+LocalDateTime.now());
//        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/test_file_2.csv"))) {
//            String line;
//            reader.readLine();
//            while ((line = reader.readLine()) != null) {
//                queue.put(line);
//            }
//        } catch (IOException | InterruptedException e) {
//            // Handle the exception
//        }
//        List<OrderEntity> orders = new ArrayList<>();
//
//        for (int i = 0; i < NUM_THREADS; i++) {
//            executorService.execute(new Worker(queue, orders));
//        }
//
//        executorService.shutdown();
//
//        // Wait for all threads to finish their work
//        while (!executorService.isTerminated()) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                // Handle the exception
//            }
//        }
//        orderRepository.saveAll(orders);
//        System.out.println("End Time:"+LocalDateTime.now());
//        long end= Instant.now().getEpochSecond();
//        System.out.print("Total Time Taken " + (end-start));
//    }


}
