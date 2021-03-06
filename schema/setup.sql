-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema testdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema testdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `testdb` DEFAULT CHARACTER SET latin1 ;
USE `testdb` ;

-- -----------------------------------------------------
-- Table `testdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`users` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `testdb`.`managers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`managers` (
  `manager_id` INT(11) NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`manager_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `testdb`.`departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`departments` (
  `department_id` INT(11) NOT NULL AUTO_INCREMENT,
  `dept_name` VARCHAR(45) NULL DEFAULT NULL,
  `dept_manager` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`department_id`),
  INDEX `dept_managerFK_idx` (`dept_manager` ASC),
  CONSTRAINT `dept_managerFK`
    FOREIGN KEY (`dept_manager`)
    REFERENCES `testdb`.`managers` (`manager_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `testdb`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`employees` (
  `employee_id` INT(11) NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `department` INT(11) NOT NULL,
  PRIMARY KEY (`employee_id`),
  INDEX `departmentFK_idx` (`department` ASC),
  CONSTRAINT `departmentFK`
    FOREIGN KEY (`department`)
    REFERENCES `testdb`.`departments` (`department_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `testdb`.`audit_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`audit_history` (
  `auditHistory_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_no` INT NULL,
  `employee_no` INT NULL,
  `action` VARCHAR(45) NULL,
  `action_date` TIMESTAMP(6) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`auditHistory_id`),
  INDEX `userFK_idx` (`user_no` ASC),
  INDEX `employeeFK_idx` (`employee_no` ASC),
  CONSTRAINT `userFK`
    FOREIGN KEY (`user_no`)
    REFERENCES `testdb`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `employeeFK`
    FOREIGN KEY (`employee_no`)
    REFERENCES `testdb`.`employees` (`employee_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
