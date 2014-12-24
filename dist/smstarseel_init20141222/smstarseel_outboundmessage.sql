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
-- Table structure for table `outboundmessage`
--

DROP TABLE IF EXISTS `outboundmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outboundmessage` (
  `outboundId` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `dueDate` datetime NOT NULL,
  `errorMessage` varchar(255) DEFAULT NULL,
  `failureCause` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `originator` varchar(255) DEFAULT NULL,
  `periodType` varchar(255) NOT NULL,
  `priority` int(11) NOT NULL,
  `projectId` int(11) DEFAULT NULL,
  `recipient` varchar(255) NOT NULL,
  `referenceNumber` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `tries` int(11) DEFAULT NULL,
  `sentDate` datetime DEFAULT NULL,
  `systemProcessingEndDate` datetime DEFAULT NULL,
  `systemProcessingStartDate` datetime DEFAULT NULL,
  `text` varchar(2000) CHARACTER SET utf8 NOT NULL,
  `type` varchar(255) NOT NULL,
  `validityPeriod` int(11) NOT NULL,
  PRIMARY KEY (`outboundId`),
  UNIQUE KEY `referenceNumber_UNIQUE` (`referenceNumber`),
  KEY `FKECE2E717658E14D0` (`projectId`),
  KEY `duedate_index` (`dueDate`),
  KEY `recipient_index` (`recipient`),
  KEY `status_index` (`status`)
) ENGINE=MyISAM AUTO_INCREMENT=108409 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outboundmessage`
--

LOCK TABLES `outboundmessage` WRITE;
/*!40000 ALTER TABLE `outboundmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `outboundmessage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-22 12:03:39
