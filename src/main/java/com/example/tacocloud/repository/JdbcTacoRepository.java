package com.example.tacocloud.repository;

import com.example.tacocloud.model.Ingredient;
import com.example.tacocloud.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert tacoInserter;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.tacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco")
                .usingGeneratedKeyColumns("id");
    }

    private long saveTacoInfo(Taco taco) {
//        taco.setCreatedAt(new Date());
//        PreparedStatementCreator psc =
//                new PreparedStatementCreatorFactory(
//                        "insert into Taco (name, createdAt) values (?, ?)",
//                        Types.VARCHAR, Types.TIMESTAMP
//                ).newPreparedStatementCreator(
//                        Arrays.asList(
//                                taco.getName(),
//                                new Timestamp(taco.getCreatedAt().getTime())));
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(psc, keyHolder);
//        return keyHolder.getKey().longValue();
//        taco.setCreatedAt(new Date());
//
//        PreparedStatementCreatorFactory pscf =
//                new PreparedStatementCreatorFactory(
//                        "insert into Taco (name, createdAt) values (?, ?)",
//                        Types.VARCHAR, Types.TIMESTAMP
//                );
//        pscf.setReturnGeneratedKeys(true); // This line is crucial
//
//        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
//                Arrays.asList(
//                        taco.getName(),
//                        new Timestamp(taco.getCreatedAt().getTime())));
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(psc, keyHolder);
//
//        // Add null check to handle cases where no key is returned
//        if (keyHolder.getKey() != null) {
//            return keyHolder.getKey().longValue();
//        } else {
//            throw new RuntimeException("Unable to retrieve generated key for Taco");
//        }
        taco.setCreatedAt(new Date());
        Map<String, Object> values = new HashMap<>();
        values.put("name", taco.getName());
        values.put("createdAt", new Timestamp(taco.getCreatedAt().getTime()));
        return tacoInserter.executeAndReturnKey(values).longValue();
    }

    private void saveIngredientToTaco(
            Ingredient ingredient, long tacoId) {
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient) " +
                        "values (?, ?)",
                tacoId, ingredient.getId());
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
//        return null;
    }
}
