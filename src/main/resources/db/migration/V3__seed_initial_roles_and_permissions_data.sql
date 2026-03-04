INSERT INTO permissions (id, group_name, name, description, is_system, created_at, updated_at)
VALUES
(gen_random_uuid(), 'ROLE', 'ROLE_VIEW', 'View all roles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'ROLE', 'ROLE_VIEW_DETAILS', 'View details about a role', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'ROLE', 'ROLE_CREATE', 'Create new roles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'ROLE', 'ROLE_UPDATE', 'Update existing roles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'ROLE', 'ROLE_DELETE', 'Delete existing roles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'ROLE', 'ROLE_RESTORE', 'Restore deleted roles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_VIEW', 'View all permissions', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_VIEW_DETAILS', 'View details about a permission', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_CREATE', 'Create new permissions', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_UPDATE', 'Update existing permissions', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_DELETE', 'Delete existing permissions', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'PERMISSION', 'PERMISSION_RESTORE', 'Restore deleted permissions', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO roles (id, name, description, is_system)
VALUES
(gen_random_uuid(), 'ADMIN', 'System Administrator with full access', true),
(gen_random_uuid(), 'USER', 'The user has permissions to participate in the auction.', true);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p WHERE r.name = 'ADMIN';