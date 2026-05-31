CREATE TABLE IF NOT EXISTS fields (
    id UInt64,
    name String,
    region Nullable(String),
    latitude Nullable(Float64),
    longitude Nullable(Float64)
)
ENGINE = MergeTree
ORDER BY id;

CREATE TABLE IF NOT EXISTS wells (
    id UInt64,
    number String,
    status String,
    depth Nullable(Float64),
    diameter Nullable(Float64),
    field_id Nullable(UInt64)
)
ENGINE = MergeTree
ORDER BY id;

CREATE TABLE IF NOT EXISTS productions (
    id UInt64,
    well_id UInt64,
    production_date Date,
    oil Float64,
    gas Nullable(Float64),
    water Nullable(Float64)
)
ENGINE = MergeTree
ORDER BY (well_id, production_date, id);

CREATE TABLE IF NOT EXISTS well_versioned (
    id UInt64,
    number String,
    status String,
    depth Nullable(Float64),
    diameter Nullable(Float64),
    field_id Nullable(UInt64),
    version UInt64
)
ENGINE = ReplacingMergeTree(version)
ORDER BY id;

CREATE TABLE IF NOT EXISTS production_collapsing (
    id UInt64,
    well_id UInt64,
    oil Float64,
    gas Float64,
    water Float64,
    sign Int8
)
ENGINE = CollapsingMergeTree(sign)
ORDER BY id;