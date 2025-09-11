package com.example.tacocloud.repository;

import com.example.tacocloud.model.Order;

public interface OrderRepository {

    Order save(Order order);
}
