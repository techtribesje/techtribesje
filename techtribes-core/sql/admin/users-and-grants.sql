CREATE USER 'techtribesje'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, LOCK_TABLES ON techtribesje.* to 'techtribesje'@'localhost';

CREATE SCHEMA `techtribesje_test` DEFAULT CHARACTER SET utf8;
CREATE USER 'test'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON techtribesje_test.* to 'test'@'localhost' IDENTIFIED BY 'password';
