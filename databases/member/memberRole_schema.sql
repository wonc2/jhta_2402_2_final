-- USERS 테이블 생성 (AUTO_INCREMENT 적용)
CREATE TABLE `USERS` (
                         `USER_PK` INT AUTO_INCREMENT NOT NULL,
                         `USER_NAME` VARCHAR(50) NULL,
                         `USER_PASSWORD` VARCHAR(255) NULL,
                         `USER_ID` VARCHAR(50) NULL,
                         `USER_EMAIL` VARCHAR(255) NULL,
                         PRIMARY KEY (`USER_PK`)
);

-- ROLE_NAME 테이블 생성 (AUTO_INCREMENT 적용)
CREATE TABLE `ROLE_NAME` (
                             `ROLE_NAME_PK` INT AUTO_INCREMENT NOT NULL,
                             `ROLE_NAME` VARCHAR(50) NULL,
                             PRIMARY KEY (`ROLE_NAME_PK`)
);

-- ROLE 테이블 생성 (AUTO_INCREMENT 적용)
CREATE TABLE `ROLE` (
                        `ROLE_PK` INT AUTO_INCREMENT NOT NULL,
                        `USER_PK` INT NOT NULL,
                        `ROLE_NAME_PK` INT NOT NULL,
                        PRIMARY KEY (`ROLE_PK`),
                        FOREIGN KEY (`USER_PK`) REFERENCES `USERS` (`USER_PK`),
                        FOREIGN KEY (`ROLE_NAME_PK`) REFERENCES `ROLE_NAME` (`ROLE_NAME_PK`)
);