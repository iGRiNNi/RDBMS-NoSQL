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


INSERT INTO field (id, name, region, latitude, longitude, start_date, oil_reserves)
VALUES
    (1, 'Самотлорское', 'Ханты-Мансийский АО', 61.154000, 76.684000, '1965-05-29', 2700000000.00),
    (2, 'Приобское', 'Ханты-Мансийский АО', 61.000000, 73.500000, '1982-11-15', 5000000000.00),
    (3, 'Ромашкинское', 'Республика Татарстан', 54.900000, 52.300000, '1948-07-25', 2300000000.00),
    (4, 'Мухановское', 'Самарская область', 53.650000, 51.350000, '1947-09-10', NULL),
    (5, 'Среднеботуобинское', NULL, 61.700000, 113.900000, '1970-06-18', 1400000000.00);


	INSERT INTO cluster (id, name, field_id, parent_cluster_id)
VALUES
    (1, 'Группы Самотлорского', 1, NULL),
    (2, 'Северная группа', 1, 1),
    (3, 'Южная группа', 1, 1),
    (4, 'Северная группа - блок А', 1, 2),

    (5, 'Группы Приобского', 2, NULL),
    (6, 'Центральная группа', 2, 5),
    (7, 'Восточная группа', 2, 5),

    (8, 'Группы Ромашкинского', 3, NULL),
    (9, 'Западная группа', 3, 8),

    (10, 'Группы Мухановского', 4, NULL),
    (11, 'Северо-Западная группа', 4, 10),
    (12, 'Юго-Восточная группа', 4, 10),

    (13, 'Группы Среднеботуобинского', 5, NULL),
    (14, 'Центральная группа', 5, 13),
    (15, 'Северная группа', 5, 13);

INSERT INTO well (id, number, status, depth, diameter, cluster_id)
VALUES
    (1, 'СМ-101', 'active',      3050.00, 0.22, 2),
    (2, 'СМ-102', 'active',      3120.00, 0.22, 2),
    (3, 'СМ-103', 'maintenance', NULL,    0.18, 2),
    (4, 'СМ-201', 'active',      3210.00, 0.22, 3),
    (5, 'СМ-202', 'disabled',    3150.00, NULL, 3),
    (6, 'СМ-301', 'active',      3300.00, 0.25, 4),

    (7,  'ПР-101', 'active',      3400.00, 0.25, 6),
    (8,  'ПР-102', 'active',      NULL,    0.22, 6),
    (9,  'ПР-201', 'maintenance', 3450.00, 0.25, 7),
    (10, 'ПР-202', 'active',      3500.00, NULL, 7),

    (11, 'РМ-101', 'active',      2800.00, 0.18, 9),
    (12, 'РМ-102', 'disabled',    NULL,    NULL, 9),

    (13, 'МХ-101', 'active',      2450.00, 0.16, 11),
    (14, 'МХ-102', 'active',      2520.00, NULL, 11),
    (15, 'МХ-201', 'maintenance', 2480.00, 0.18, 12),

    (16, 'СБ-101', 'active',      3600.00, 0.25, 14),
    (17, 'СБ-102', 'active',      3660.00, 0.25, 14),
    (18, 'СБ-201', 'disabled',    NULL,    0.22, 15);

INSERT INTO production (id, well_id, date, oil, gas, water)
VALUES
    (1,  1,  '2026-03-10', 120.50, 540.00, 18.20),
    (2,  2,  '2026-03-10', 132.00, 560.50, 20.10),
    (3,  4,  '2026-03-10', 140.30, 600.00, NULL),
    (4,  6,  '2026-03-10', 150.20, 620.80, 21.00),
    (5,  7,  '2026-03-10', 165.00, NULL,   24.30),
    (6,  8,  '2026-03-10', 158.60, 705.40, 23.90),
    (7,  10, '2026-03-10', 172.40, 730.00, 25.80),
    (8,  11, '2026-03-10', 95.20,  410.00, NULL),
    (9,  13, '2026-03-10', 88.40,  210.00, 12.40),
    (10, 14, '2026-03-10', 91.10,  NULL,   13.10),
    (11, 16, '2026-03-10', 184.50, 780.00, 28.60),
    (12, 17, '2026-03-10', 189.20, 795.40, NULL),

    (13, 1,  '2026-03-11', 121.00, 538.00, 18.00),
    (14, 2,  '2026-03-11', 131.40, 559.00, 20.50),
    (15, 4,  '2026-03-11', 141.10, 598.70, 19.10),
    (16, 6,  '2026-03-11', 149.90, NULL,   20.80),
    (17, 7,  '2026-03-11', 166.20, 712.00, 24.00),
    (18, 8,  '2026-03-11', 157.80, 701.20, NULL),
    (19, 10, '2026-03-11', 173.00, 731.30, 26.10),
    (20, 11, '2026-03-11', 96.10,  412.40, 15.20),
    (21, 13, '2026-03-11', 87.90,  NULL,   12.70),
    (22, 14, '2026-03-11', 92.00,  226.40, NULL),
    (23, 16, '2026-03-11', 185.30, 782.20, 28.30),
    (24, 17, '2026-03-11', 190.00, 798.00, 29.40);

INSERT INTO production (id, well_id, date, oil, gas, water)
VALUES
    (25, 1, CURRENT_DATE - 10, 40.00, 500.00, 120.00),
    (26, 1, CURRENT_DATE - 5,  42.00, 510.00, 125.00),

    (27, 4, CURRENT_DATE - 9,  55.00, 600.00, 140.00),
    (28, 4, CURRENT_DATE - 4,  53.00, 610.00, 138.00),

    (29, 13, CURRENT_DATE - 7, 35.00, 220.00, 90.00),
    (30, 13, CURRENT_DATE - 2, 37.00, 225.00, 95.00);

INSERT INTO contractor (id, name, specialization, phone, field_id)
VALUES
    (1, 'ООО БурСервис', 'Буровые работы', '+7-900-100-10-10', 1),
    (2, 'ООО НефтеГеоКонтроль', 'Геофизические исследования', '+7-900-200-20-20', 2),
    (3, 'АО СамараНефтеРемонт', 'Капитальный ремонт скважин', '+7-900-300-30-30', 4),
    (4, 'ООО ЯкутПромИзоляция', 'Изоляционные работы', '+7-900-400-40-40', 5),
    (5, 'ООО ТехДиагностика', 'Диагностика оборудования', '+7-900-500-50-50', NULL),
    (6, 'ООО СеверЭнергоМонтаж', 'Электромонтажные работы', '+7-900-600-60-60', 1),
    (7, 'ООО РезервПодряд', 'Общестроительные работы', NULL, NULL);


SELECT * FROM cluster


    SELECT w.id, w.number, w.status, w.cluster_id, p.date, p.oil
      FROM well w
INNER JOIN production p ON p.well_id = w.id
     WHERE w.status IN ('active', 'maintenance');

	SELECT w.id, w.number, w.status, w.cluster_id, p.date, p.oil
      FROM well w
 FULL JOIN production p ON p.well_id = w.id
     WHERE w.status IN ('active', 'maintenance');

	 SELECT f.name AS "месторождение"
	      , f.region AS "регион"
		  , c.name AS "подрядчик" 
	   FROM field f
 INNER JOIN contractor c ON c.field_id = f.id

 	 SELECT f.name AS "месторождение"
	      , f.region AS "регион"
		  , c.name AS "подрядчик" 
	   FROM field f
  FULL JOIN contractor c ON c.field_id = f.id

  	 SELECT f.name AS "месторождение"
	      , f.region AS "регион"
		  , c.name AS "подрядчик" 
	   FROM field f
  LEFT JOIN contractor c ON c.field_id = f.id

  	 SELECT f.name AS "месторождение"
	      , f.region AS "регион"
		  , c.name AS "подрядчик" 
	   FROM field f
 RIGHT JOIN contractor c ON c.field_id = f.id

   	 SELECT f.name AS "месторождение"
	      , f.region AS "регион"
		  , c.name AS "подрядчик" 
	   FROM field f
 CROSS JOIN contractor c


-- 	 SELECT f.name AS месторождение
--     	  ,	f.region AS регион
--           ,  COUNT(c.id) AS количество_подрядчиков
-- FROM field f
-- LEFT JOIN contractor c ON c.field_id = f.id
-- GROUP BY f.id, f.name, f.region
-- ORDER BY количество_подрядчиков DESC, месторождение;

-- три активные скважины, где средний объём воды больше среднего объёма нефти за последний месяц
	SELECT c.name AS group_name
	     , w.number AS well_number
	     , AVG(p.oil) AS avg_oil
	     , AVG(p.water) AS avg_water
	  FROM well w
	  JOIN cluster c ON c.id = w.cluster_id
	  JOIN production p ON p.well_id = w.id
	 WHERE w.status = 'active'
	   AND p.date >= CURRENT_DATE - INTERVAL '1 month'
	   AND p.date <= CURRENT_DATE
  GROUP BY c.name, w.number
    HAVING AVG(p.water) > AVG(p.oil)
  ORDER BY avg_water DESC
     LIMIT 3;

-- сравнить среднюю добычу скважины со средней в группе
WITH well_avg AS (
    SELECT w.cluster_id
         , c.name AS group_name
         , w.number AS well_number
         , AVG(p.oil) AS avg_oil_by_well
      FROM well w
      JOIN cluster c ON c.id = w.cluster_id
      JOIN production p ON p.well_id = w.id
  GROUP BY w.cluster_id, c.name, w.number
)
  SELECT group_name
       , well_number
       , avg_oil_by_well
       , AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id) AS avg_oil_in_group
       , CASE
           WHEN avg_oil_by_well > AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id)
             THEN 'Выше среднего'
           WHEN avg_oil_by_well = AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id)
             THEN 'Равно среднему'
           ELSE 'Ниже среднего'
         END AS above_group_avg
    FROM well_avg
ORDER BY group_name, well_number;

-- вывести все кластеры иерархией
WITH RECURSIVE rec(id, name, level) AS (
    SELECT id, name, 1 as level
	  FROM cluster 
	 WHERE field_id = 1 AND parent_cluster_id IS NULL
     UNION ALL
    SELECT c.id, c.name, rec.level + 1 as level 
      FROM cluster c 
	  JOIN rec ON rec.id = c.parent_cluster_id
)
SELECT * FROM rec;

SELECT * FROM cluster WHERE field_id = 1

-- DROP TABLE production;
-- DROP TABLE contractor;
-- DROP TABLE well;
-- DROP TABLE cluster;
-- DROP TABLE field;