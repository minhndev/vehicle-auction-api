INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_CREATE', 'Create new user', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_CREATE');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_UPDATE', 'Update user information', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_UPDATE');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_DELETE', 'Delete user', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_DELETE');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_RESTORE', 'Restore deleted user', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_RESTORE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('USER_CREATE', 'USER_UPDATE', 'USER_DELETE', 'USER_RESTORE')
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );