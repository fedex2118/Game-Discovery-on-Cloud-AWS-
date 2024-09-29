CREATE DATABASE  IF NOT EXISTS `games` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `games`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: games
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
-- Table structure for table `developers`
--

DROP TABLE IF EXISTS `developers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `developers` (
  `developer_id` bigint NOT NULL AUTO_INCREMENT,
  `developer_name` varchar(255) NOT NULL,
  PRIMARY KEY (`developer_id`),
  UNIQUE KEY `developer_name_UNIQUE` (`developer_name`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_developers`
--

DROP TABLE IF EXISTS `game_developers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_developers` (
  `game_id` bigint NOT NULL,
  `developer_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`developer_id`),
  KEY `developers_developer_id_idx` (`developer_id`),
  CONSTRAINT `developers_developer_id` FOREIGN KEY (`developer_id`) REFERENCES `developers` (`developer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `developers_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_genres`
--

DROP TABLE IF EXISTS `game_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_genres` (
  `game_id` bigint NOT NULL,
  `genre_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`genre_id`),
  KEY `genre_id_idx` (`genre_id`),
  CONSTRAINT `genres_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `genres_genre_id` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`genre_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_platforms`
--

DROP TABLE IF EXISTS `game_platforms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_platforms` (
  `game_id` bigint NOT NULL,
  `platform_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`platform_id`),
  KEY `platform_id_idx` (`platform_id`),
  CONSTRAINT `platforms_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `platforms_platform_id` FOREIGN KEY (`platform_id`) REFERENCES `platforms` (`platform_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_publishers`
--

DROP TABLE IF EXISTS `game_publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_publishers` (
  `game_id` bigint NOT NULL,
  `publisher_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`publisher_id`),
  KEY `publishers_publisher_id_idx` (`publisher_id`),
  CONSTRAINT `publishers_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `publishers_publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`publisher_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` bigint NOT NULL AUTO_INCREMENT,
  `game_name` varchar(255) NOT NULL DEFAULT 'game_name_placeholder',
  `release_date` datetime NOT NULL DEFAULT '9999-12-31 23:59:59',
  `price` double NOT NULL DEFAULT '1',
  `is_singleplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_multiplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_pvp` tinyint(1) NOT NULL DEFAULT '0',
  `is_pve` tinyint(1) NOT NULL DEFAULT '0',
  `description` varchar(255) NOT NULL DEFAULT 'description_placeholder',
  `is_2d` tinyint(1) NOT NULL DEFAULT '0',
  `is_3d` tinyint(1) NOT NULL DEFAULT '0',
  `cover_art_url` varchar(255) NOT NULL DEFAULT 'cover_art_placeholder',
  `average_rating` decimal(4,2) NOT NULL DEFAULT '0.00',
  `review_quantity` int NOT NULL DEFAULT '0',
  `status` int NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`game_id`),
  KEY `idx_games_status_createdat` (`status`,`timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genres` (
  `genre_id` bigint NOT NULL AUTO_INCREMENT,
  `genre_name` varchar(255) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `genre_name_UNIQUE` (`genre_name`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `platforms`
--

DROP TABLE IF EXISTS `platforms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platforms` (
  `platform_id` bigint NOT NULL AUTO_INCREMENT,
  `platform_name` varchar(255) NOT NULL,
  PRIMARY KEY (`platform_id`),
  UNIQUE KEY `platform_name_UNIQUE` (`platform_name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publishers` (
  `publisher_id` bigint NOT NULL AUTO_INCREMENT,
  `publisher_name` varchar(255) NOT NULL,
  PRIMARY KEY (`publisher_id`),
  UNIQUE KEY `publisher_name_UNIQUE` (`publisher_name`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `updatable_games`
--

DROP TABLE IF EXISTS `updatable_games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `updatable_games` (
  `game_id` bigint NOT NULL,
  `game_name` varchar(255) NOT NULL DEFAULT 'game_name_placeholder',
  `release_date` datetime NOT NULL DEFAULT '9999-12-31 23:59:59',
  `price` double NOT NULL DEFAULT '1',
  `is_singleplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_multiplayer` tinyint(1) NOT NULL DEFAULT '0',
  `is_pvp` tinyint(1) NOT NULL DEFAULT '0',
  `is_pve` tinyint(1) NOT NULL DEFAULT '0',
  `description` varchar(255) NOT NULL DEFAULT 'description_placeholder',
  `is_2d` tinyint(1) NOT NULL DEFAULT '0',
  `is_3d` tinyint(1) NOT NULL DEFAULT '0',
  `cover_art_url` varchar(255) NOT NULL DEFAULT 'cover_art_placeholder',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`game_id`),
  KEY `idx_games_status_createdat` (`timestamp`),
  CONSTRAINT `updatable_games_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`)
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

-- Dump completed on 2024-05-21 22:27:31
