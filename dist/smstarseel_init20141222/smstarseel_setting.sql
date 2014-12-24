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
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `settingId` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedByUserId` varchar(255) DEFAULT NULL,
  `lastEditedByUserName` varchar(255) DEFAULT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `validatorRegx` varchar(255) DEFAULT NULL,
  `isEditable` bit(1) DEFAULT NULL,
  `isViewable` bit(1) DEFAULT NULL,
  PRIMARY KEY (`settingId`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `displayName` (`displayName`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `setting`
--

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting` DISABLE KEYS */;
INSERT INTO `setting` VALUES (1,'Maximum outbounds/outgoing smses that should be sent to device for sending out in one fetch cycle. This must be a valid numeric number ranging in 1-19',NULL,NULL,NULL,'outbound.fetch-per-go','6','max outgoing sms in one go','[0-1]?[0-9]','',''),(2,'Maximum duplicate outbounds/outgoing smses allowed for any recipient in a day. Any smses exceeding range would be marked as SPAM. Value for the field should lie b/w 1-99',NULL,NULL,NULL,'outbound.spam.max-duplicate-allowed','4','max duplicate outgoing sms per recipient per day','[0-9]?[0-9]','',''),(3,'Maximum outbounds/outgoing smses allowed for any recipient in a day. Any smses exceeding range would be marked as SPAM. Value for the field should lie b/w 1-9999',NULL,NULL,NULL,'outbound.spam.max-sms-allowed','10','max outgoing sms per recipient per day','[0-9]?[0-9]?[0-9]?[0-9]','',''),(4,'Maximum attempts to send any sms. After exceeding retries smses would be marked as MISSED/FAILED. Should be integer value between 1-9',NULL,NULL,NULL,'outbound.max-retries','4','max retries','[1-9]','',''),(5,'Duration between two successive retry attempts if sms gets lost during sending process by mobile phone. Should be in minutes',NULL,NULL,NULL,'outbound.lost-retry.interval-min','60','retry interval for lost outgoing sms (minutes)','\\\\d{2,3}','',''),(6,'Duration between two successive retry attempts if sms gets failed (network error) during sending process by mobile phone. Should be in minutes',NULL,NULL,NULL,'outbound.failed-retry.interval-min','180','retry interval for failed outgoing sms (minutes)','\\\\d{2,3}','',''),(7,'Email address which would be notified via email for any error in code / system. Only 1 address should be specified',NULL,NULL,NULL,'admin.email-address','maimoona.kausar@irdinformatics.org','email to send error alerts','((\\\\w+)|(\\\\w+\\\\.+\\\\w+)|(\\\\w+\\\\-+\\\\w+))\\@((\\\\w+)|(\\\\w+\\\\-\\\\w+))\\\\.\\\\w{2,4}','',''),(8,'Email addresses which would be sent daily summary emails for system`s activity. Multiple addresses should be separated by comma','admin','ADMINISTRATOR ','2013-07-18 16:00:02','notifier.daily-summary.recipients','maimoona.kausar@irdinformatics.org','emails to send daily activity summary','(((\\\\w+)|(\\\\w+\\\\.+\\\\w+)|(\\\\w+\\\\-+\\\\w+))\\\\@((\\\\w+)|(\\\\w+\\\\-\\\\w+))\\\\.\\\\w{2,4})(\\\\,\\\\s?((\\\\w+)|(\\\\w+\\\\.+\\\\w+)|(\\\\w+\\\\-+\\\\w+))\\\\@((\\\\w+)|(\\\\w+\\\\-\\\\w+))\\\\.\\\\w{2,4})*','',''),(9,'Time at which daily summary emails would be sent. If task should run multiple times, comma separated times should be provided. May require system restart if changed','admin','ADMINISTRATOR ','2013-07-18 15:59:12','notifier.daily-summary.email-time','15:30:00, 16:30:00','time to email daily summary','(\\\\d\\\\d:\\\\d\\\\d:\\\\d\\\\d)(\\\\,\\\\s?\\\\d\\\\d:\\\\d\\\\d:\\\\d\\\\d)*','',''),(10,'Validator regex for cell numbers provided in Send Sms page',NULL,NULL,NULL,'cell-number.validator-regex','\\d+','cell number validator regex','.*','',''),(11,'Last time when daily summary was sent. This setting is to control summary behaviour. Not Editable via interface. ',NULL,'','2013-08-12 16:30:50','notifier.daily-summary.last-run','16:30:00','time daily summary last emailed',NULL,'\0','');
/*!40000 ALTER TABLE `setting` ENABLE KEYS */;
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
