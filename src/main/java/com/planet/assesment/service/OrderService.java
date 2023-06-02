package com.planet.assesment.service;

import java.io.IOException;

public interface OrderService {

    void saveOrders(String csvFilePath) throws IOException;

    void saveOrdersMultithreading(String csvFilePath) throws IOException;
}
