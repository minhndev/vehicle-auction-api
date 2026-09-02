INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_VIEW', 'View users', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_VIEW');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_VIEW_DETAILS', 'View user details', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_VIEW_DETAILS');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_UPDATE_STATUS', 'Update user account status', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_UPDATE_STATUS');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('USER_VIEW', 'USER_VIEW_DETAILS', 'USER_UPDATE_STATUS')
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );

