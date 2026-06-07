USE oil_lab;

DROP TABLE IF EXISTS productions_by_well;
DROP TABLE IF EXISTS wells_by_id;
DROP TABLE IF EXISTS fields_by_id;

CREATE TABLE IF NOT EXISTS fields_by_id (
    id uuid PRIMARY KEY,
    name text,
    region text,
    latitude double,
    longitude double,
    start_date date
);

CREATE TABLE IF NOT EXISTS wells_by_id (
    id uuid PRIMARY KEY,
    field_id uuid,
    number text,
    status text,
    well_type text,
    depth double,
    diameter double,

    producer_pump_type text,
    producer_target_oil double,

    injector_injection_pressure double,
    injector_target_water double,

    exploration_core_depth double,
    exploration_seismic_quality text
);

CREATE TABLE IF NOT EXISTS productions_by_well (
    well_id uuid,
    production_date date,
    id timeuuid,
    oil double,
    gas double,
    water double,
    PRIMARY KEY ((well_id), production_date, id)
) WITH CLUSTERING ORDER BY (production_date DESC, id ASC);

CREATE TABLE IF NOT EXISTS wells_by_id (
    id uuid PRIMARY KEY,
    field_id uuid,
    number text,
    status text,
    well_type text,
    depth double,
    diameter double,

    producer_pump_type text,
    producer_target_oil double,

    injector_injection_pressure double,
    injector_target_water double,

    exploration_core_depth double,
    exploration_seismic_quality text
);

CREATE TABLE IF NOT EXISTS wells_by_field (
    field_id uuid,
    id uuid,
    number text,
    status text,
    well_type text,
    depth double,
    diameter double,

    producer_pump_type text,
    producer_target_oil double,

    injector_injection_pressure double,
    injector_target_water double,

    exploration_core_depth double,
    exploration_seismic_quality text,

    PRIMARY KEY ((field_id), id)
);

CREATE TABLE IF NOT EXISTS wells_by_status_depth (
    status text,
    depth double,
    id uuid,
    field_id uuid,
    number text,
    well_type text,
    diameter double,

    producer_pump_type text,
    producer_target_oil double,

    injector_injection_pressure double,
    injector_target_water double,

    exploration_core_depth double,
    exploration_seismic_quality text,

    PRIMARY KEY ((status), depth, id)
) WITH CLUSTERING ORDER BY (depth DESC, id ASC);

CREATE TABLE IF NOT EXISTS wells_by_type_status (
    well_type text,
    status text,
    id uuid,
    field_id uuid,
    number text,
    depth double,
    diameter double,

    producer_pump_type text,
    producer_target_oil double,

    injector_injection_pressure double,
    injector_target_water double,

    exploration_core_depth double,
    exploration_seismic_quality text,

    PRIMARY KEY ((well_type, status), id)
);