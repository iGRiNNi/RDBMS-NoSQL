package org.example.dao.keyvalue;

import org.example.domain.model.Cluster;
import org.example.infrastructure.redis.RedisValueUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClusterKeyValueDao extends AbstractKeyValueDao<Cluster> {

    public ClusterKeyValueDao() {
        super(
                "cluster",
                List.of("name", "field_id", "parent_cluster_id"),
                "name"
        );
    }

    @Override
    protected long getId(Cluster cluster) {
        return cluster.getId();
    }

    @Override
    protected Map<String, String> toRedisMap(Cluster cluster) {
        Map<String, String> values = new LinkedHashMap<>();

        values.put("name", RedisValueUtils.toRedisString(cluster.getName()));
        values.put("field_id", RedisValueUtils.toRedisString(cluster.getFieldId()));
        values.put("parent_cluster_id", RedisValueUtils.toRedisString(cluster.getParentClusterId()));

        return values;
    }

    @Override
    protected Cluster fromRedisMap(long id, Map<String, String> values) {
        return new Cluster(
                id,
                values.get("name"),
                RedisValueUtils.parseRequiredLong(values.get("field_id"), "field_id"),
                RedisValueUtils.parseNullableLong(values.get("parent_cluster_id"))
        );
    }
}
