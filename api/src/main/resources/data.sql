/* inicijalizacija grupa products i customers */

INSERT INTO products_group (name) VALUES ('PECIVO');
INSERT INTO products_group (name) VALUES ('HLEB');
INSERT INTO products_group (name) VALUES ('SNRZNUTI PROGRAM');

INSERT INTO customers_group (name) VALUES ('STR');
INSERT INTO customers_group (name) VALUES ('VELIKI SISTEMI');
INSERT INTO customers_group (name) VALUES ('MALOPRODAJA');
INSERT INTO customers_group (name) VALUES ('TENDERI');

/* inicijalizacija rola */

INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('EMPLOYEE');
INSERT INTO roles (name) VALUES ('MANAGER');
INSERT INTO roles (name) VALUES ('GENERALMANAGER');
INSERT INTO roles (name) VALUES ('ADMIN');

/* inicijalizacija permissiona */

INSERT INTO permissions (name) VALUES ('CUSTOMER_READ');
INSERT INTO permissions (name) VALUES ('CUSTOMER_WRITE');
INSERT INTO permissions (name) VALUES ('PRODUCT_READ');
INSERT INTO permissions (name) VALUES ('PRODUCT_WRITE');
INSERT INTO permissions (name) VALUES ('ORDER_READ');
INSERT INTO permissions (name) VALUES ('ORDER_WRITE');
INSERT INTO permissions (name) VALUES ('REPORT_READ');

/* grupisanje permissiona u role */
/* 1 USER -> 1,3,5,6 */

INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (1,1);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (1,3);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (1,5);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (1,6);
/* 2 EMPLOYEE -> 1,3,5,6 */
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (2,1);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (2,3);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (2,5);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (2,6);
/* 3 MANAGER -> 1,2,3,4,5 */
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (3,1);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (3,2);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (3,3);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (3,4);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (3,5);
/* 4 GENERALMANAGER -> 1,2,3,4,5,7 */
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,1);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,2);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,3);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,4);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,5);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (4,7);
/* 5 ADMIN -> 1,2,3,4,5,6,7 */
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,1);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,2);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,3);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,4);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,5);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,6);
INSERT INTO roles_has_permissions (roles_id,permissions_id) VALUES (5,7);




