package com.example.tacocloud.repository;

import com.example.tacocloud.model.Order;
import com.example.tacocloud.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbc;

    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new java.util.Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order (deliveryName, deliveryStreet, deliveryCity, deliveryState, deliveryZip, ccNumber, ccExpiration, ccCvv, placedAt) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(List.of(
                order.getName(), order.getStreet(), order.getCity(),
                order.getState(), order.getZip(), order.getCcNumber(),
                order.getCcExpiration(), order.getCcCVV(), new java.sql.Timestamp(order.getPlacedAt().getTime())
        ));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        // save tacos to join table
        for (Taco taco : order.getTacos()) {
            jdbc.update("insert into Taco_Order_Tacos (tacoOrder, taco) values (?, ?)", orderId, taco.getId());
        }
        return order;
    }
}
