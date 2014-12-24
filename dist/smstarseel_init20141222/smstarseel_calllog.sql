CREATE DATABASE  IF NOT EXISTS `smstarseel` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `smstarseel`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 125.209.94.150    Database: smstarseel
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `calllog`
--

DROP TABLE IF EXISTS `calllog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calllog` (
  `callLogId` bigint(20) NOT NULL AUTO_INCREMENT,
  `callDate` datetime DEFAULT NULL,
  `callStatus` varchar(255) DEFAULT NULL,
  `callType` varchar(255) DEFAULT NULL,
  `callerNumber` varchar(255) DEFAULT NULL,
  `durationInSec` int(11) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `referenceNumber` varchar(255) NOT NULL,
  `systemProcessingEndDate` datetime DEFAULT NULL,
  `systemProcessingStartDate` datetime DEFAULT NULL,
  `description` varchar(245) DEFAULT NULL,
  PRIMARY KEY (`callLogId`),
  UNIQUE KEY `referenceNumber_UNIQUE` (`referenceNumber`),
  KEY `FK20B3A426658E14D0` (`projectId`)
) ENGINE=MyISAM AUTO_INCREMENT=16578 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calllog`
--

LOCK TABLES `calllog` WRITE;
/*!40000 ALTER TABLE `calllog` DISABLE KEYS */;
/*!40000 ALTER TABLE `calllog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-22 12:03:40
