package com.planet.assesment.service;

import com.planet.assesment.repository.OrderEntity;
import com.planet.assesment.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;


@SpringBootTest
class OrderServiceImplTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    @Captor
    private ArgumentCaptor<List<OrderEntity>> orderEntityList;

    @Test
    public void test_processCSV() throws IOException {
        // this csv have 4 records
        String filePath = "src/main/resources/test.csv";
        orderService.saveOrders(filePath);
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).saveAllAndFlush(orderEntityList.capture());
        int value = orderEntityList.getValue().size();
        Assertions.assertEquals(4, value);
    }

    @Test
    public void test_processCSV_databaseException() {
        // this csv have 4 records
        String filePath = "src/main/resources/test.csv";
        Mockito.when(orderRepository.saveAllAndFlush(Mockito.any())).thenThrow(new RuntimeException("Error persisting data"));
        Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.saveOrders(filePath);
        });
        Mockito.verify(orderRepository, Mockito.times(1)).saveAllAndFlush(Mockito.any(List.class));
    }

    @Test
    public void test_processCSV_throwsExceptionWhenFileNotFound() throws IOException {
        // this csv have 4 records
        String filePath = "src/main/resources/testError.csv";
        Assertions.assertThrows(IOException.class, () -> {
            orderService.saveOrders(filePath);
        });
    }
}