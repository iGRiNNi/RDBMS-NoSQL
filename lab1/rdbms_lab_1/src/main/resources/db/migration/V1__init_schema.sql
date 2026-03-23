CREATE TABLE field (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    region VARCHAR(50),
    latitude NUMERIC(9,6),
	longitude NUMERIC(9,6),
    start_date DATE DEFAULT CURRENT_DATE,
    oil_reserves NUMERIC(12,2),
	CHECK (latitude BETWEEN -90 AND 90),
    CHECK (longitude BETWEEN -180 AND 180),
	UNIQUE(name, region),
	UNIQUE(latitude, longitude)
);

CREATE TABLE cluster (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    field_id BIGINT REFERENCES field(id) ON DELETE CASCADE NOT NULL,
    parent_cluster_id BIGINT REFERENCES cluster(id) ON DELETE SET NULL
);

CREATE TABLE well (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'disabled' CHECK (status IN ('active', 'disabled', 'maintenance')),
    depth NUMERIC(8,2),
    diameter NUMERIC(5,2),
    cluster_id BIGINT REFERENCES cluster(id) ON DELETE CASCADE NOT NULL,
	UNIQUE(number, cluster_id)
);

CREATE TABLE production (
    id BIGSERIAL PRIMARY KEY,
    well_id BIGINT REFERENCES well(id) ON DELETE CASCADE NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    oil NUMERIC(12,2) NOT NULL,
    gas NUMERIC(12,2),
    water NUMERIC(12,2),
	UNIQUE(date, well_id)
	);

CREATE TABLE contractor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    specialization VARCHAR(80) NOT NULL,
    phone VARCHAR(20),
    field_id BIGINT REFERENCES field(id) ON DELETE SET NULL
);