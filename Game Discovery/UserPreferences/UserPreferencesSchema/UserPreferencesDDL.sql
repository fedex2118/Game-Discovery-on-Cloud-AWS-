CREATE DATABASE  IF NOT EXISTS `preferences` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `preferences`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: preferences
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user_created_developers`
--

DROP TABLE IF EXISTS `user_created_developers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_created_developers` (
  `developer_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`developer_id`),
  KEY `user_created_developers_user_id_idx` (`user_id`),
  CONSTRAINT `user_created_developers_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_created_games`
--

DROP TABLE IF EXISTS `user_created_games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_created_games` (
  `game_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`game_id`),
  KEY `created_games_user_id` (`user_id`),
  KEY `idx_user_id_updatedat` (`user_id`,`updated_at`),
  CONSTRAINT `created_games_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `created_games_game_id_greater_than_0` CHECK ((`game_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_created_publishers`
--

DROP TABLE IF EXISTS `user_created_publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_created_publishers` (
  `publisher_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`publisher_id`),
  KEY `user_created_publishers_user_id_idx` (`user_id`),
  CONSTRAINT `user_created_publishers_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_library`
--

DROP TABLE IF EXISTS `user_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_library` (
  `game_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`user_id`),
  KEY `library_user_id` (`user_id`),
  CONSTRAINT `library_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `library_game_id_greater_than_0` CHECK ((`game_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_pref_developers`
--

DROP TABLE IF EXISTS `user_pref_developers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_pref_developers` (
  `developer_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`developer_id`,`user_id`),
  KEY `developers_user_id` (`user_id`),
  CONSTRAINT `developers_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `developer_id_greater_than_0` CHECK ((`developer_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_pref_genres`
--

DROP TABLE IF EXISTS `user_pref_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_pref_genres` (
  `genre_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`genre_id`,`user_id`),
  KEY `genres_user_id` (`user_id`),
  CONSTRAINT `genres_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `genre_id_greater_than_0` CHECK ((`genre_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_pref_platforms`
--

DROP TABLE IF EXISTS `user_pref_platforms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_pref_platforms` (
  `platform_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`platform_id`,`user_id`),
  KEY `platforms_user_id` (`user_id`),
  CONSTRAINT `platforms_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `platform_id_greater_than_0` CHECK ((`platform_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_pref_publishers`
--

DROP TABLE IF EXISTS `user_pref_publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_pref_publishers` (
  `publisher_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`publisher_id`,`user_id`),
  KEY `publishers_user_id` (`user_id`),
  CONSTRAINT `publishers_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `publisher_id_greater_than_0` CHECK ((`publisher_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_preferences`
--

DROP TABLE IF EXISTS `user_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_preferences` (
  `user_id` bigint NOT NULL,
  `starting_date` datetime NOT NULL DEFAULT '1899-12-31 23:59:59',
  `starting_price` double NOT NULL DEFAULT '0',
  `is_singleplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_multiplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_pvp` tinyint(1) NOT NULL DEFAULT '0',
  `is_pve` tinyint(1) NOT NULL DEFAULT '0',
  `is_2d` tinyint(1) NOT NULL DEFAULT '0',
  `is_3d` tinyint(1) NOT NULL DEFAULT '0',
  `starting_average_rating` decimal(4,2) NOT NULL DEFAULT '0.00',
  `starting_review_quantity` int NOT NULL DEFAULT '0',
  `skip_genres` tinyint(1) NOT NULL DEFAULT '0',
  `skip_publishers` tinyint(1) NOT NULL DEFAULT '0',
  `skip_developers` tinyint(1) NOT NULL DEFAULT '0',
  `skip_platforms` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_preferences_chk_1` CHECK ((`user_id` >= 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_wishlist`
--

DROP TABLE IF EXISTS `user_wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_wishlist` (
  `game_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`user_id`),
  KEY `wishlist_user_id` (`user_id`),
  CONSTRAINT `wishlist_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_preferences` (`user_id`),
  CONSTRAINT `wishlist_game_id_greater_than_0` CHECK ((`game_id` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-19 19:46:40
