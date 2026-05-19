package org.example.dao.hash;

import org.example.domain.model.Field;
import org.example.infrastructure.redis.RedisValueUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FieldHashDao extends AbstractHashDao<Field> {

    public FieldHashDao() {
        super(
                "field",
                List.of("name", "region", "latitude", "longitude"),
                "name"
        );
    }

    @Override
    protected long getId(Field field) {
        return field.getId();
    }

    @Override
    protected Map<String, String> toRedisMap(Field field) {
        Map<String, String> values = new LinkedHashMap<>();

        values.put("name", RedisValueUtils.toRedisString(field.getName()));
        values.put("region", RedisValueUtils.toRedisString(field.getRegion()));
        values.put("latitude", RedisValueUtils.toRedisString(field.getLatitude()));
        values.put("longitude", RedisValueUtils.toRedisString(field.getLongitude()));

        return values;
    }

    @Override
    protected Field fromRedisMap(long id, Map<String, String> values) {
        return new Field(
                id,
                values.get("name"),
                values.get("region"),
                RedisValueUtils.parseNullableDouble(values.get("latitude")),
                RedisValueUtils.parseNullableDouble(values.get("longitude"))
        );
    }
}