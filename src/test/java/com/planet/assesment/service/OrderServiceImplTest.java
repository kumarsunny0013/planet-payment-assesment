package com.planet.assesment.service;

import com.planet.assesment.repository.OrderEntity;
import com.planet.assesment.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class OrderServiceImplTest {

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Mock
    private OrderRepository orderRepository;

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
    public void test_processCSV_throwsExceptionWhenFileNotFound() throws IOException {
        // this csv have 4 records
        String filePath = "src/main/resources/testError.csv";
        Assertions.assertThrows(IOException.class,()->{
            orderService.saveOrders(filePath);
        });
    }
}