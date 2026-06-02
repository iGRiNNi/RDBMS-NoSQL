USE oil_lab;

CREATE CUSTOM INDEX IF NOT EXISTS wells_status_idx
ON wells_by_id (status)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_type_idx
ON wells_by_id (well_type)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_depth_idx
ON wells_by_id (depth)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_field_id_idx
ON wells_by_id (field_id)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS fields_region_idx
ON fields_by_id (region)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_producer_pump_type_idx
ON wells_by_id (producer_pump_type)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_injector_injection_pressure_idx
ON wells_by_id (injector_injection_pressure)
USING 'StorageAttachedIndex';

CREATE CUSTOM INDEX IF NOT EXISTS wells_exploration_seismic_quality_idx
ON wells_by_id (exploration_seismic_quality)
USING 'StorageAttachedIndex';