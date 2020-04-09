-- MySQL Script generated by MySQL Workbench
-- Thu Apr  9 10:54:56 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hleb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hleb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hleb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `hleb` ;

-- -----------------------------------------------------
-- Table `hleb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `active` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`permissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`users_has_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`users_has_roles` (
  `users_id` INT NOT NULL,
  `roles_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `roles_id`),
  INDEX `fk_users_has_roles_roles1_idx` (`roles_id` ASC) VISIBLE,
  INDEX `fk_users_has_roles_users_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_roles_users`
    FOREIGN KEY (`users_id`)
    REFERENCES `hleb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `hleb`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`roles_has_permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`roles_has_permissions` (
  `roles_id` INT NOT NULL,
  `permissions_id` INT NOT NULL,
  PRIMARY KEY (`roles_id`, `permissions_id`),
  INDEX `fk_roles_has_permissions_permissions1_idx` (`permissions_id` ASC) VISIBLE,
  INDEX `fk_roles_has_permissions_roles1_idx` (`roles_id` ASC) VISIBLE,
  CONSTRAINT `fk_roles_has_permissions_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `hleb`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_has_permissions_permissions1`
    FOREIGN KEY (`permissions_id`)
    REFERENCES `hleb`.`permissions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`customers_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`customers_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(10) NOT NULL,
  `street_and_number` VARCHAR(100) NULL,
  `postcode` INT NULL,
  `city` VARCHAR(45) NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `customers_group_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_customers_customers-group1_idx` (`customers_group_id` ASC) VISIBLE,
  UNIQUE INDEX `secret-code_UNIQUE` (`code` ASC) VISIBLE,
  CONSTRAINT `fk_customers_customers-group`
    FOREIGN KEY (`customers_group_id`)
    REFERENCES `hleb`.`customers_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`products_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`products_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `code` VARCHAR(10) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `description` VARCHAR(100) NULL,
  `picture` VARCHAR(100) NULL,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `products_group_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  INDEX `fk_products_products-group1_idx` (`products_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_products-group`
    FOREIGN KEY (`products_group_id`)
    REFERENCES `hleb`.`products_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(10) NOT NULL,
  `date` DATE NOT NULL,
  `status` VARCHAR(45) NOT NULL DEFAULT 'PORUCENO',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `customers_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE,
  INDEX `fk_orders_customers1_idx` (`customers_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_customers1`
    FOREIGN KEY (`customers_id`)
    REFERENCES `hleb`.`customers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`orders_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`orders_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `quantity` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `orders_id` INT NOT NULL,
  `products_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders-items_orders1_idx` (`orders_id` ASC) VISIBLE,
  INDEX `fk_orders-items_products1_idx` (`products_id` ASC) INVISIBLE,
  UNIQUE INDEX `order_item_unique` (`orders_id` ASC, `products_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders-items_orders1`
    FOREIGN KEY (`orders_id`)
    REFERENCES `hleb`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders-items_products1`
    FOREIGN KEY (`products_id`)
    REFERENCES `hleb`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hleb`.`users_has_customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hleb`.`users_has_customers` (
  `users_id` INT NOT NULL,
  `customers_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `customers_id`),
  INDEX `fk_users_has_customers_customers1_idx` (`customers_id` ASC) VISIBLE,
  INDEX `fk_users_has_customers_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_customers_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `hleb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_customers_customers1`
    FOREIGN KEY (`customers_id`)
    REFERENCES `hleb`.`customers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
