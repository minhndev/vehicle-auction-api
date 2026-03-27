INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('PRODUCT_UPDATE', 'PRODUCT_DELETE','PRODUCT_VIEW', 'PRODUCT_VIEW_DETAILS','PRODUCT_RESTORE',
                                 'CATEGORY_VIEW', 'CATEGORY_VIEW_DETAILS','CATEGORY_CREATE','CATEGORY_UPDATE','CATEGORY_DELETE', 'CATEGORY_RESTORE')
WHERE r.name = 'SELLER'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );