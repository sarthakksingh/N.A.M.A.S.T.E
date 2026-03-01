-- Code systems
INSERT INTO code_systems (name, version, is_active)
VALUES ('NAMASTE', '1.0', true);

INSERT INTO code_systems (name, version, is_active)
VALUES ('ICD11_TM2', '2025-06', true);

INSERT INTO code_systems (name, version, is_active)
VALUES ('ICD11_BIOMED', '2025-06', true);

-- Concepts for NAMASTE
INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  'N123',
  'Ama Jwara',
  'Fever due to impaired digestion',
  (SELECT id FROM code_systems WHERE name = 'NAMASTE'),
  true
);

INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  'N456',
  'Madhumeha',
  'Sweet urination disorder',
  (SELECT id FROM code_systems WHERE name = 'NAMASTE'),
  true
);

-- Concepts for ICD11_TM2
INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  'TM2-789',
  'Fever Pattern',
  'Traditional fever concept',
  (SELECT id FROM code_systems WHERE name = 'ICD11_TM2'),
  true
);

INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  'TM2-999',
  'Metabolic Disorder Pattern',
  'Sugar disorder pattern',
  (SELECT id FROM code_systems WHERE name = 'ICD11_TM2'),
  true
);

-- Concepts for ICD11_BIOMED
INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  '5A11',
  'Type 2 Diabetes Mellitus',
  'Modern diabetes classification',
  (SELECT id FROM code_systems WHERE name = 'ICD11_BIOMED'),
  true
);

INSERT INTO concepts (code, display_name, description, code_system_id, is_active)
VALUES (
  'AA12',
  'Fever',
  'Fever in modern medicine',
  (SELECT id FROM code_systems WHERE name = 'ICD11_BIOMED'),
  true
);

-- Mappings: Madhumeha -> Type 2 Diabetes
INSERT INTO mappings (from_concept_id, to_concept_id, mapping_type, confidence)
VALUES (
  (SELECT c1.id FROM concepts c1
   JOIN code_systems cs1 ON c1.code_system_id = cs1.id
   WHERE c1.code = 'N456' AND cs1.name = 'NAMASTE'),
  (SELECT c2.id FROM concepts c2
   JOIN code_systems cs2 ON c2.code_system_id = cs2.id
   WHERE c2.code = '5A11' AND cs2.name = 'ICD11_BIOMED'),
  'EQUIVALENT',
  0.95
);

-- Mappings: Ama Jwara -> Fever
INSERT INTO mappings (from_concept_id, to_concept_id, mapping_type, confidence)
VALUES (
  (SELECT c1.id FROM concepts c1
   JOIN code_systems cs1 ON c1.code_system_id = cs1.id
   WHERE c1.code = 'N123' AND cs1.name = 'NAMASTE'),
  (SELECT c2.id FROM concepts c2
   JOIN code_systems cs2 ON c2.code_system_id = cs2.id
   WHERE c2.code = 'AA12' AND cs2.name = 'ICD11_BIOMED'),
  'EQUIVALENT',
  0.90
);
