-- MariaDB dump 10.19  Distrib 10.6.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: vdhg_db
-- ------------------------------------------------------
-- Server version	10.7.7-MariaDB-1:10.7.7+maria~ubu2004

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_addresses`
--

DROP TABLE IF EXISTS `t_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_addresses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL,
  `province` varchar(256) DEFAULT NULL COMMENT 'Or state',
  `street` varchar(256) DEFAULT NULL,
  `complement` varchar(256) DEFAULT NULL,
  `zip_code` varchar(32) DEFAULT NULL,
  `city` varchar(256) DEFAULT NULL,
  `attribute_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_addresses_FK` (`country_id`),
  KEY `t_addresses_fk_attr_types` (`attribute_type_id`),
  CONSTRAINT `t_addresses_FK` FOREIGN KEY (`country_id`) REFERENCES `t_countries` (`id`),
  CONSTRAINT `t_addresses_fk_attr_types` FOREIGN KEY (`attribute_type_id`) REFERENCES `t_attribute_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Addresses Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_attribute_types`
--

DROP TABLE IF EXISTS `t_attribute_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_attribute_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Atribute [Address, Phone, Emails] Types Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_categories`
--

DROP TABLE IF EXISTS `t_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `category_type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_categories_FK` (`category_type_id`),
  CONSTRAINT `t_categories_FK` FOREIGN KEY (`category_type_id`) REFERENCES `t_category_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_category_types`
--

DROP TABLE IF EXISTS `t_category_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_category_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Category Types Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_civil_status`
--

DROP TABLE IF EXISTS `t_civil_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_civil_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Civil Status Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_companies`
--

DROP TABLE IF EXISTS `t_companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_companies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Company Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_countries`
--

DROP TABLE IF EXISTS `t_countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_countries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iso_code` varchar(3) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `calling_codes` varchar(16) DEFAULT NULL COMMENT 'Separated by semicolons ;',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Countries Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_customers`
--

DROP TABLE IF EXISTS `t_customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lead_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `subscribe_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `unsubscribe_time` timestamp NULL DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `membership_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_customer_fk_lead` (`lead_id`),
  KEY `t_customer_fk_customer_status` (`status_id`),
  KEY `t_customers_fk_users` (`user_id`),
  KEY `t_customers_fk_membership_types` (`membership_type_id`),
  CONSTRAINT `t_customer_fk_customer_status` FOREIGN KEY (`status_id`) REFERENCES `t_customers_status` (`id`),
  CONSTRAINT `t_customer_fk_lead` FOREIGN KEY (`lead_id`) REFERENCES `t_lead` (`id`),
  CONSTRAINT `t_customers_fk_membership_types` FOREIGN KEY (`membership_type_id`) REFERENCES `t_membership_types` (`id`),
  CONSTRAINT `t_customers_fk_users` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Customer Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_customers_status`
--

DROP TABLE IF EXISTS `t_customers_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customers_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Customer Status Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_emails`
--

DROP TABLE IF EXISTS `t_emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_emails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(1024) NOT NULL,
  `valid` int(1) NOT NULL DEFAULT 0,
  `check_time` timestamp NULL DEFAULT NULL,
  `attribute_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_emails_fk_attr_types` (`attribute_type_id`),
  CONSTRAINT `t_emails_fk_attr_types` FOREIGN KEY (`attribute_type_id`) REFERENCES `t_attribute_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Emails Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_employee_types`
--

DROP TABLE IF EXISTS `t_employee_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_employee_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Employee Type Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_employees`
--

DROP TABLE IF EXISTS `t_employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creation_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `person_id` int(11) NOT NULL,
  `subsidiary_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `type_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_employees_fk_people` (`person_id`),
  KEY `t_employees_fk_subsidiaries` (`subsidiary_id`),
  KEY `t_employees_fk_users` (`user_id`),
  KEY `t_employees_fk_types` (`type_id`),
  CONSTRAINT `t_employees_fk_people` FOREIGN KEY (`person_id`) REFERENCES `t_people` (`id`),
  CONSTRAINT `t_employees_fk_subsidiaries` FOREIGN KEY (`subsidiary_id`) REFERENCES `t_subsidiaries` (`id`),
  CONSTRAINT `t_employees_fk_types` FOREIGN KEY (`type_id`) REFERENCES `t_employee_types` (`id`),
  CONSTRAINT `t_employees_fk_users` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Employees Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_genders`
--

DROP TABLE IF EXISTS `t_genders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_genders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Genders Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_identity_document_types`
--

DROP TABLE IF EXISTS `t_identity_document_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_identity_document_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Identity Document Type Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_identity_documents`
--

DROP TABLE IF EXISTS `t_identity_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_identity_documents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `identity_document_type_id` int(11) NOT NULL,
  `country_id` int(11) NOT NULL COMMENT 'Document issuing country',
  `value` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_identity_documents_FK` (`country_id`),
  KEY `t_identity_documents_FK_1` (`identity_document_type_id`),
  CONSTRAINT `t_identity_documents_FK` FOREIGN KEY (`country_id`) REFERENCES `t_countries` (`id`),
  CONSTRAINT `t_identity_documents_FK_1` FOREIGN KEY (`identity_document_type_id`) REFERENCES `t_identity_document_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Identity Documents';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_lead`
--

DROP TABLE IF EXISTS `t_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_lead` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status_id` int(11) NOT NULL,
  `company_id` int(11) DEFAULT NULL,
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_lead_fk_status_lead` (`status_id`),
  KEY `t_lead_fk_company` (`company_id`),
  KEY `t_lead_fk_person` (`person_id`),
  CONSTRAINT `t_lead_fk_company` FOREIGN KEY (`company_id`) REFERENCES `t_companies` (`id`),
  CONSTRAINT `t_lead_fk_person` FOREIGN KEY (`person_id`) REFERENCES `t_people` (`id`),
  CONSTRAINT `t_lead_fk_status_lead` FOREIGN KEY (`status_id`) REFERENCES `t_lead_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Lead Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_lead_status`
--

DROP TABLE IF EXISTS `t_lead_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_lead_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Status Lead Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_membership_types`
--

DROP TABLE IF EXISTS `t_membership_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_membership_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Membership Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bookings_orders`
--

DROP TABLE IF EXISTS `t_bookings_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bookings_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creation_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `code` varchar(128) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_orders_FK` (`customer_id`),
  CONSTRAINT `t_orders_FK` FOREIGN KEY (`customer_id`) REFERENCES `t_customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Orders Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_orders_products`
--

DROP TABLE IF EXISTS `t_orders_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_orders_products` (
  `product_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`order_id`),
  KEY `t_orders_products_fk_orders` (`order_id`),
  CONSTRAINT `t_orders_products_fk_orders` FOREIGN KEY (`order_id`) REFERENCES `t_orders` (`id`),
  CONSTRAINT `t_orders_products_fk_products` FOREIGN KEY (`product_id`) REFERENCES `t_products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate products to orders';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_people`
--

DROP TABLE IF EXISTS `t_people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_people` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `surname` varchar(256) NOT NULL,
  `nationality_id` int(11) DEFAULT NULL,
  `title_id` int(11) DEFAULT NULL,
  `gender_id` int(11) DEFAULT NULL,
  `civil_status_id` int(11) DEFAULT NULL,
  `creation_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `identity_document_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_people_FK_nacionality` (`nationality_id`),
  KEY `t_people_FK_civil_status` (`civil_status_id`),
  KEY `t_people_FK_gender` (`gender_id`),
  KEY `t_people_FK` (`title_id`),
  KEY `t_people_FK_1` (`identity_document_id`),
  CONSTRAINT `t_people_FK` FOREIGN KEY (`title_id`) REFERENCES `t_person_titles` (`id`),
  CONSTRAINT `t_people_FK_1` FOREIGN KEY (`identity_document_id`) REFERENCES `t_identity_documents` (`id`),
  CONSTRAINT `t_people_FK_civil_status` FOREIGN KEY (`civil_status_id`) REFERENCES `t_civil_status` (`id`),
  CONSTRAINT `t_people_FK_gender` FOREIGN KEY (`gender_id`) REFERENCES `t_genders` (`id`),
  CONSTRAINT `t_people_FK_nacionality` FOREIGN KEY (`nationality_id`) REFERENCES `t_countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='People Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_people_addresses`
--

DROP TABLE IF EXISTS `t_people_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_people_addresses` (
  `person_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  PRIMARY KEY (`person_id`,`address_id`),
  KEY `t_people_addresses_FK_1` (`address_id`),
  CONSTRAINT `t_people_addresses_FK` FOREIGN KEY (`person_id`) REFERENCES `t_people` (`id`),
  CONSTRAINT `t_people_addresses_FK_1` FOREIGN KEY (`address_id`) REFERENCES `t_addresses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate addresses to people';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_people_emails`
--

DROP TABLE IF EXISTS `t_people_emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_people_emails` (
  `person_id` int(11) NOT NULL,
  `email_id` int(11) NOT NULL,
  PRIMARY KEY (`person_id`,`email_id`),
  KEY `t_people_emails_FK_email` (`email_id`),
  CONSTRAINT `t_people_emails_FK_email` FOREIGN KEY (`email_id`) REFERENCES `t_emails` (`id`),
  CONSTRAINT `t_people_emails_FK_people` FOREIGN KEY (`person_id`) REFERENCES `t_people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate emails to people';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_people_phones`
--

DROP TABLE IF EXISTS `t_people_phones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_people_phones` (
  `person_id` int(11) NOT NULL,
  `phone_id` int(11) NOT NULL,
  PRIMARY KEY (`person_id`,`phone_id`),
  KEY `t_people_phones_FK` (`phone_id`),
  CONSTRAINT `t_people_phones_FK` FOREIGN KEY (`phone_id`) REFERENCES `t_phones` (`id`),
  CONSTRAINT `t_people_phones_FK_1` FOREIGN KEY (`person_id`) REFERENCES `t_people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate phones to people';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_person_titles`
--

DROP TABLE IF EXISTS `t_person_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_person_titles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Person Titles Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_phones`
--

DROP TABLE IF EXISTS `t_phones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_phones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(32) NOT NULL,
  `valid` varchar(1) NOT NULL DEFAULT '0',
  `check_time` timestamp NULL DEFAULT NULL,
  `attribute_type_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_phones_fk_attr_types` (`attribute_type_id`),
  CONSTRAINT `t_phones_fk_attr_types` FOREIGN KEY (`attribute_type_id`) REFERENCES `t_attribute_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Phones Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_privileges`
--

DROP TABLE IF EXISTS `t_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_privileges` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='The operations that the user can do';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_products`
--

DROP TABLE IF EXISTS `t_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_products_fk_categories` (`category_id`),
  CONSTRAINT `t_products_fk_categories` FOREIGN KEY (`category_id`) REFERENCES `t_categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Products Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_roles`
--

DROP TABLE IF EXISTS `t_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Contains the privilege group to associate with the user.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_roles_privileges`
--

DROP TABLE IF EXISTS `t_roles_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_roles_privileges` (
  `role_id` int(11) NOT NULL,
  `privilege_id` int(11) NOT NULL,
  PRIMARY KEY (`privilege_id`,`role_id`),
  KEY `t_roles_privileges_fk_roles` (`role_id`),
  CONSTRAINT `t_roles_privileges_fk_privileges` FOREIGN KEY (`privilege_id`) REFERENCES `t_privileges` (`id`),
  CONSTRAINT `t_roles_privileges_fk_roles` FOREIGN KEY (`role_id`) REFERENCES `t_roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join table, to relate privileges to roles.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_subsidiaries`
--

DROP TABLE IF EXISTS `t_subsidiaries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_subsidiaries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Subsidiaries Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_status`
--

DROP TABLE IF EXISTS `t_user_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='User Status Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_users`
--

DROP TABLE IF EXISTS `t_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(256) DEFAULT NULL,
  `creation_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `status_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `t_users_FK_user_status` (`status_id`),
  CONSTRAINT `t_users_FK_user_status` FOREIGN KEY (`status_id`) REFERENCES `t_user_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Users Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_users_groups`
--

DROP TABLE IF EXISTS `t_users_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='User Groups Table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_users_groups_roles`
--

DROP TABLE IF EXISTS `t_users_groups_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users_groups_roles` (
  `users_group_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`users_group_id`,`role_id`),
  KEY `t_users_groups_roles_fk_roles` (`role_id`),
  CONSTRAINT `t_users_groups_roles_fk_roles` FOREIGN KEY (`role_id`) REFERENCES `t_roles` (`id`),
  CONSTRAINT `t_users_groups_roles_fk_users_groups` FOREIGN KEY (`users_group_id`) REFERENCES `t_users_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate roles to users groups';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_users_groups_users`
--

DROP TABLE IF EXISTS `t_users_groups_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users_groups_users` (
  `users_group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`users_group_id`,`user_id`),
  KEY `t_users_groups_users_FK_users` (`user_id`),
  CONSTRAINT `t_users_groups_users_FK_users` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`),
  CONSTRAINT `t_users_groups_users_fk_users_groups` FOREIGN KEY (`users_group_id`) REFERENCES `t_users_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate users to users groups';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_users_roles`
--

DROP TABLE IF EXISTS `t_users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `t_users_roles_FK_users` (`user_id`),
  CONSTRAINT `t_users_roles_FK_roles` FOREIGN KEY (`role_id`) REFERENCES `t_roles` (`id`),
  CONSTRAINT `t_users_roles_FK_users` FOREIGN KEY (`user_id`) REFERENCES `t_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Join Table, relate roles to users';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-09 23:32:16