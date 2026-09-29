-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: speedfast_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entrega`
--

DROP TABLE IF EXISTS `entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrega` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int NOT NULL,
  `id_repartidor` int NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_pedido` (`id_pedido`),
  KEY `id_repartidor` (`id_repartidor`),
  CONSTRAINT `entrega_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id`),
  CONSTRAINT `entrega_ibfk_2` FOREIGN KEY (`id_repartidor`) REFERENCES `repartidor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrega`
--

LOCK TABLES `entrega` WRITE;
/*!40000 ALTER TABLE `entrega` DISABLE KEYS */;
INSERT INTO `entrega` VALUES (1,1001,1,'2026-09-28','21:01:42'),(2,1002,1,'2026-09-28','21:01:43'),(3,1003,1,'2026-09-28','21:01:44'),(4,1004,1,'2026-09-28','21:01:45'),(5,1008,1,'2026-09-28','21:01:46'),(6,1001,1,'2026-09-28','21:09:39'),(7,1002,1,'2026-09-28','21:09:40'),(8,1003,1,'2026-09-28','21:09:41'),(9,1004,1,'2026-09-28','21:09:43'),(10,1008,1,'2026-09-28','21:09:44'),(11,1001,1,'2026-09-28','21:13:02'),(12,1002,1,'2026-09-28','21:13:03'),(13,1003,1,'2026-09-28','21:13:04'),(14,1004,1,'2026-09-28','21:13:05'),(15,1008,1,'2026-09-28','21:13:06'),(16,1001,1,'2026-09-28','21:20:07'),(17,1002,1,'2026-09-28','21:20:08'),(18,1003,1,'2026-09-28','21:20:09'),(19,1004,1,'2026-09-28','21:20:10'),(20,1008,1,'2026-09-28','21:20:11'),(21,1001,1,'2026-09-28','21:20:47'),(22,1002,1,'2026-09-28','21:20:48'),(23,1003,1,'2026-09-28','21:20:49'),(24,1004,1,'2026-09-28','21:20:50'),(25,1008,1,'2026-09-28','21:20:51'),(26,1001,1,'2026-09-28','21:30:01'),(27,1002,1,'2026-09-28','21:30:02'),(28,1003,1,'2026-09-28','21:30:03'),(29,1004,1,'2026-09-28','21:30:04'),(30,1008,1,'2026-09-28','21:30:05'),(31,1001,1,'2026-09-28','21:30:31'),(32,1002,1,'2026-09-28','21:30:32'),(33,1003,1,'2026-09-28','21:30:33'),(34,1004,1,'2026-09-28','21:30:34'),(35,1008,1,'2026-09-28','21:30:35'),(36,1001,1,'2026-09-28','21:30:53'),(37,1002,1,'2026-09-28','21:30:54'),(38,1003,1,'2026-09-28','21:30:55'),(39,1004,1,'2026-09-28','21:30:56'),(40,1008,1,'2026-09-28','21:30:57'),(41,1001,1,'2026-09-28','21:31:19'),(42,1002,1,'2026-09-28','21:31:20'),(43,1003,1,'2026-09-28','21:31:21'),(44,1004,1,'2026-09-28','21:31:22'),(45,1008,1,'2026-09-28','21:31:23');
/*!40000 ALTER TABLE `entrega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `direccion` varchar(150) NOT NULL,
  `tipo` varchar(30) NOT NULL,
  `estado` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1001,'Tetano #123, clavo','COMIDA','PENDIENTE'),(1002,'Peste negra #123, Suciedad','ENCOMIENDA','PENDIENTE'),(1003,'Influenza #123, Mascarilla','COMIDA','PENDIENTE'),(1004,'Primavera #123, Septiembre','COMIDA','PENDIENTE'),(1008,'Ginko biloba #123, Amarillo','COMIDA','PENDIENTE');
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repartidor`
--

DROP TABLE IF EXISTS `repartidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repartidor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repartidor`
--

LOCK TABLES `repartidor` WRITE;
/*!40000 ALTER TABLE `repartidor` DISABLE KEYS */;
INSERT INTO `repartidor` VALUES (1,'Ana');
/*!40000 ALTER TABLE `repartidor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-28 21:45:21
