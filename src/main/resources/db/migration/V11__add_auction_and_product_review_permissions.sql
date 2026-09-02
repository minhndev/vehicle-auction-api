INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'PRODUCT', 'PRODUCT_APPROVE', 'Approve product for auction', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'PRODUCT_APPROVE');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'PRODUCT', 'PRODUCT_REJECT', 'Reject product for auction', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'PRODUCT_REJECT');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'AUCTION', 'AUCTION_CREATE', 'Create auction sessions', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'AUCTION_CREATE');

INSERT INTO permissions (id, group_name, name, description, is_system)
SELECT gen_random_uuid(), 'AUCTION', 'AUCTION_CANCEL', 'Cancel auction sessions', true
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'AUCTION_CANCEL');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('PRODUCT_APPROVE', 'PRODUCT_REJECT', 'AUCTION_CREATE', 'AUCTION_CANCEL')
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );

