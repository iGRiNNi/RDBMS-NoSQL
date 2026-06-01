CREATE KEYSPACE IF NOT EXISTS oil_lab
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};