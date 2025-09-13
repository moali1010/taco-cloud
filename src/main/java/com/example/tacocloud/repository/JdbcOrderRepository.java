package com.example.tacocloud.repository;

import com.example.tacocloud.model.Order;
import com.example.tacocloud.model.Taco;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbc;


    private final SimpleJdbcInsert orderInserter;
    private final SimpleJdbcInsert orderTacoInserter;
    private final ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
//        order.setPlacedAt(new java.util.Date());
//        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
//                "insert into Taco_Order (deliveryName, deliveryStreet, deliveryCity, deliveryState, deliveryZip, ccNumber, ccExpiration, ccCvv, placedAt) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
//        pscf.setReturnGeneratedKeys(true);
//        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(List.of(
//                order.getName(), order.getStreet(), order.getCity(),
//                order.getState(), order.getZip(), order.getCcNumber(),
//                order.getCcExpiration(), order.getCcCVV(), new java.sql.Timestamp(order.getPlacedAt().getTime())
//        ));
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(psc, keyHolder);
//        long orderId = keyHolder.getKey().longValue();
//        order.setId(orderId);
//
//        // save tacos to join table
//        for (Taco taco : order.getTacos()) {
//            jdbc.update("insert into Taco_Order_Tacos (tacoOrder, taco) values (?, ?)", orderId, taco.getId());
//        }
//        return order;
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }
        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values =
                objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());
        long orderId =
                orderInserter
                        .executeAndReturnKey(values)
                        .longValue();
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
}
