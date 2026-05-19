package org.example.dao.hash;

import org.example.domain.model.Well;
import org.example.infrastructure.redis.RedisValueUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WellHashDao extends AbstractHashDao<Well> {

    public WellHashDao() {
        super(
                "well",
                List.of("number", "status", "depth", "diameter", "cluster_id"),
                "number"
        );
    }

    @Override
    protected long getId(Well well) {
        return well.getId();
    }

    @Override
    protected Map<String, String> toRedisMap(Well well) {
        Map<String, String> values = new LinkedHashMap<>();

        values.put("number", RedisValueUtils.toRedisString(well.getNumber()));
        values.put("status", RedisValueUtils.toRedisString(well.getStatus()));
        values.put("depth", RedisValueUtils.toRedisString(well.getDepth()));
        values.put("diameter", RedisValueUtils.toRedisString(well.getDiameter()));
        values.put("cluster_id", RedisValueUtils.toRedisString(well.getClusterId()));

        return values;
    }

    @Override
    protected Well fromRedisMap(long id, Map<String, String> values) {
        return new Well(
                id,
                values.get("number"),
                values.get("status"),
                RedisValueUtils.parseNullableDouble(values.get("depth")),
                RedisValueUtils.parseNullableDouble(values.get("diameter")),
                RedisValueUtils.parseRequiredLong(values.get("cluster_id"), "cluster_id")
        );
    }
}
