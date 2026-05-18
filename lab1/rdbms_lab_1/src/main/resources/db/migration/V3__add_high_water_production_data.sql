INSERT INTO production (well_id, date, oil, gas, water)
VALUES
    ( 1, CURRENT_DATE - 10, 40.00, 500.00, 120.00),
    (1, CURRENT_DATE - 5,  42.00, 510.00, 125.00),

    ( 4, CURRENT_DATE - 9,  55.00, 600.00, 140.00),
    ( 4, CURRENT_DATE - 4,  53.00, 610.00, 138.00),

    ( 13, CURRENT_DATE - 7, 35.00, 220.00, 90.00),
    ( 13, CURRENT_DATE - 2, 37.00, 225.00, 95.00);