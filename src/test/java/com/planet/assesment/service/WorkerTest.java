package com.planet.assesment.service;

import com.planet.assesment.repository.OrderEntity;
import com.planet.assesment.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WorkerTest {

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testWorkerProcessItems() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("1, john@example.com, 123456789, 2.5");
        queue.add("2, jane@example.com, 987654321, 1.8");

        Worker worker = new Worker(queue, orderRepository);

        Thread workerThread = new Thread(worker);

        workerThread.start();
        //Added wait for 1 sec, else test will fail.
        Thread.sleep(1000);
        verify(orderRepository, times(2)).save(any(OrderEntity.class));
    }

    @Test
    public void testWorkerProcessItems_databaseException() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("1, john@example.com, 123456789, 2.5");
        queue.add("2, jane@example.com, 987654321, 1.8");
        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenThrow(new RuntimeException("Exception occurred while saving data"));
        Worker worker = new Worker(queue, orderRepository);
        Thread workerThread = new Thread(worker);
        Assertions.assertThrows(RuntimeException.class, workerThread::run);
    }
}
