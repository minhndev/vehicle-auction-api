INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'USER', 'USER_GRANT_SELLER', 'Grant SELLER role to user', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'USER_GRANT_SELLER');

INSERT INTO roles (id, name, description, is_system)
SELECT gen_random_uuid(), 'SELLER', 'Seller role for listing auction products', true
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SELLER');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'USER_GRANT_SELLER'
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'PRODUCT_CREATE'
WHERE r.name = 'SELLER'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );

