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

--
-- Table structure for table `con_test`
--

DROP TABLE IF EXISTS `con_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `con_test` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `con_test`
--

LOCK TABLES `con_test` WRITE;
/*!40000 ALTER TABLE `con_test` DISABLE KEYS */;
/*!40000 ALTER TABLE `con_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `deviceId` int(11) NOT NULL AUTO_INCREMENT,
  `addedByUserId` varchar(255) DEFAULT NULL,
  `addedByUsername` varchar(255) DEFAULT NULL,
  `commport` varchar(255) DEFAULT NULL,
  `commportCommand` varchar(255) DEFAULT NULL,
  `dateAdded` datetime DEFAULT NULL,
  `dateEdited` datetime DEFAULT NULL,
  `dateLastCalllogPing` datetime DEFAULT NULL,
  `dateLastInboundPing` datetime DEFAULT NULL,
  `dateLastOutboundPing` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `deviceName` varchar(255) DEFAULT NULL,
  `editedByUserId` varchar(255) DEFAULT NULL,
  `editedByUsername` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `pin` varchar(255) DEFAULT NULL,
  `sim` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`deviceId`),
  KEY `FKB06B1E56658E14D0` (`projectId`),
  KEY `devicestatus_index` (`status`),
  KEY `deviceimei_index` (`imei`),
  KEY `devicesim_index` (`sim`),
  CONSTRAINT `FKB06B1E56658E14D0` FOREIGN KEY (`projectId`) REFERENCES `project` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inboundmessage`
--

DROP TABLE IF EXISTS `inboundmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inboundmessage` (
  `inboundId` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` varchar(255) DEFAULT NULL,
  `originator` varchar(255) DEFAULT NULL,
  `projectId` int(11) DEFAULT NULL,
  `recieveDate` datetime DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `referenceNumber` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `systemProcessingEndDate` datetime DEFAULT NULL,
  `systemProcessingStartDate` datetime DEFAULT NULL,
  `systemRecieveDate` datetime DEFAULT NULL,
  `text` varchar(2000) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`inboundId`),
  UNIQUE KEY `referenceNumber` (`referenceNumber`),
  KEY `FKC9483A6E658E14D0` (`projectId`),
  CONSTRAINT `FKC9483A6E658E14D0` FOREIGN KEY (`projectId`) REFERENCES `project` (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inboundmessage`
--

LOCK TABLES `inboundmessage` WRITE;
/*!40000 ALTER TABLE `inboundmessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `inboundmessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logging_event`
--

DROP TABLE IF EXISTS `logging_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logging_event` (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text NOT NULL,
  `logger_name` varchar(254) NOT NULL,
  `level_string` varchar(254) NOT NULL,
  `thread_name` varchar(254) DEFAULT NULL,
  `reference_flag` smallint(6) DEFAULT NULL,
  `arg0` varchar(254) DEFAULT NULL,
  `arg1` varchar(254) DEFAULT NULL,
  `arg2` varchar(254) DEFAULT NULL,
  `arg3` varchar(254) DEFAULT NULL,
  `caller_filename` varchar(254) NOT NULL,
  `caller_class` varchar(254) NOT NULL,
  `caller_method` varchar(254) NOT NULL,
  `caller_line` char(4) NOT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logging_event`
--

LOCK TABLES `logging_event` WRITE;
/*!40000 ALTER TABLE `logging_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `logging_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logging_event_exception`
--

DROP TABLE IF EXISTS `logging_event_exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logging_event_exception` (
  `event_id` bigint(20) NOT NULL,
  `i` smallint(6) NOT NULL,
  `trace_line` varchar(254) NOT NULL,
  PRIMARY KEY (`event_id`,`i`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logging_event_exception`
--

LOCK TABLES `logging_event_exception` WRITE;
/*!40000 ALTER TABLE `logging_event_exception` DISABLE KEYS */;
/*!40000 ALTER TABLE `logging_event_exception` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `permissionId` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`permissionId`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,NULL,'DEVICE_OPEARTIONS');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `projectId` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`projectId`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,NULL,'test');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `createdByUserId` varchar(255) DEFAULT NULL,
  `createdByUserName` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastEditedByUserId` varchar(255) DEFAULT NULL,
  `lastEditedByUserName` varchar(255) DEFAULT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roleId`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'DEVICE'),(2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `roleId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`),
  KEY `FKBD40D5385E015256` (`permissionId`),
  KEY `FKBD40D53898D043A4` (`roleId`),
  CONSTRAINT `FKBD40D5385E015256` FOREIGN KEY (`permissionId`) REFERENCES `permission` (`permissionId`),
  CONSTRAINT `FKBD40D53898D043A4` FOREIGN KEY (`roleId`) REFERENCES `role` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1),(2,1);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `createdByUserId` varchar(255) DEFAULT NULL,
  `createdByUserName` varchar(255) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastEditedByUserId` varchar(255) DEFAULT NULL,
  `lastEditedByUserName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNo` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,NULL,NULL,NULL,NULL,'ADMINISTRATOR',NULL,NULL,'',NULL,NULL,'admin','U+rF8sDI2DhrAOGyvjgY4dK9GWIdDY3NCAXd/3tseOc=',NULL,'ACTIVE');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`),
  KEY `FK143BF46A98D043A4` (`roleId`),
  KEY `FK143BF46A9E25990E` (`userId`),
  CONSTRAINT `FK143BF46A98D043A4` FOREIGN KEY (`roleId`) REFERENCES `role` (`roleId`),
  CONSTRAINT `FK143BF46A9E25990E` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'smstarseel'
--
/*!50003 DROP PROCEDURE IF EXISTS `OutboundCleanup` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `OutboundCleanup`(IN deviceProjectId INT, IN maxDuplicates INT, IN maxSms INT, 
IN maxRetries INT, IN lostRetryIntervalmin INT, IN failedRetryIntervalmin INT)
BEGIN
-- We need some cleanup done before message could be sent to mobile for sending out to recipient
-- 1) Check for SPAMS. If any recipient has been sent messages equal to the Max limit defined in Params, Just mark message as SPAM
-- 2) Check for messages that has passed their validity period
-- 3) Check for smses those have status UNKNOWN for long time(specified above), and revert them to PENDING to send again provided that these are valid and not passed maxRetries (specified)
-- 4) Check for smses those have status FAILED for long time(specified above), and revert them to PENDING to send again provided that these are valid and not passed maxRetries (specified)

DECLARE statementDynam VARCHAR(10000);

DECLARE obId BIGINT;
DECLARE done INT DEFAULT FALSE;
DECLARE num_rows INT;

-- Get IDS of outbounds which have been sent messages reaching SPAM limit defined above
DECLARE cObToUpdate CURSOR FOR 
		SELECT tinner.outboundId FROM (select o.outboundid, o.recipient, A.count Acount,B.count Bcount from outboundmessage as o 
		left join (select text,recipient, count(*) as count from outboundmessage 
			where (date(sentdate) = curdate() and status in ('sent') 
				OR date(duedate) = curdate() and status in ('unknown') )
			group by recipient) as A on A.recipient = o.recipient and A.text=o.text 
		left join (select recipient, count(*) as count from outboundmessage 
			where (date(sentdate) = curdate() and status in ('sent') 
				OR date(duedate) = curdate() and status in ('unknown') )
			group by recipient) as B on B.recipient = o.recipient 
		where projectId = deviceProjectId AND o.status = 'pending' 
		having Acount > maxDuplicates OR Bcount > maxSms) AS tinner;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

-- Concat OutboundIds, to append with update query
set statementDynam='';
OPEN cObToUpdate;
select FOUND_ROWS() into num_rows;
 
IF num_rows > 0 THEN 

  read_loop: LOOP
    FETCH cObToUpdate INTO obId;
    IF done THEN
      LEAVE read_loop;
    ELSE
		set statementDynam = concat(obId,',',statementDynam);
	END IF;
  END LOOP;

-- Remove extra comma at the end of list
  set statementDynam = substring(statementDynam,1,length(statementDynam)-1);
 
-- Mark all outbounds as MISSED and flag them as SPAM
  set @QueryString='';
  SET @QueryString=concat("update outboundmessage 
						set systemProcessingStartDate = now(), 
						status = 'MISSED',
						failureCause = concat(IFNULL(failureCause,''),'SPAM;'), 
						errorMessage = concat(IFNULL(errorMessage,''),'SPAM_DETECTED ', now(), ';'), 
						systemProcessingEndDate = now() 
						where outboundId in (",statementDynam,")");
  PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END IF;

CLOSE cObToUpdate;

-- Mark all outbounds as MISSED and flag 'VALIDITY PERIOD PASSED' if sms not sent sent within specified time
-- 14 days limit as we donot want to process messages before it. It should never happen ideally...
  set @QueryString='';
  SET @QueryString=concat("update outboundmessage 
						set status = 'MISSED' 
						, failureCause = concat(IFNULL(failureCause,''),'VALIDITY_PERIOD_PASSED;') 
						, errormessage = concat(IFNULL(errormessage,''),'VALIDITY_PERIOD_PASSED ', now(), ';') 
						where projectId = ",deviceProjectId," AND status='PENDING' 
						and TIMESTAMPDIFF(MINUTE,(
						case when periodtype ='YEAR' then DATE_ADD(duedate,INTERVAL validityPeriod YEAR) 
						when periodtype ='MONTH' then DATE_ADD(duedate,INTERVAL validityPeriod MONTH) 
						when periodtype ='WEEK' then DATE_ADD(duedate,INTERVAL (validityPeriod*7) DAY) 
						when periodType='DAY' then DATE_ADD(duedate,INTERVAL validityPeriod DAY) 
						when periodtype ='HOUR' then DATE_ADD(duedate,INTERVAL validityPeriod HOUR) 
						when periodtype ='MINTUE' then DATE_ADD(duedate,INTERVAL validityPeriod MINUTE)
						when periodtype ='SECOND' then DATE_ADD(duedate,INTERVAL validityPeriod SECOND)
						else DATE_ADD(duedate,INTERVAL 1 SECOND) END), now()) > 1 ");
  PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

-- Mark all sms as PENDING to be sent again which are UNKNOWN since the time specified, provided that specified retries are not exceeded
 --
  set @QueryString='';
  SET @QueryString=concat("update outboundmessage 
						set status = 'PENDING'
					    , failureCause = concat(IFNULL(failureCause,''),'LOST_RETRY;') 
					    , errormessage = concat(IFNULL(errormessage,''),'LOST_RETRY ', now(), ';') 
					    where projectId = ",deviceProjectId," AND status = 'UNKNOWN' 
					    and duedate > DATE_SUB(curdate(),INTERVAL 7 DAY) 
					    and DATE_ADD(systemProcessingStartDate,INTERVAL ",lostRetryIntervalmin," MINUTE) < now() 
					    and tries <= ", maxRetries);
  PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

-- Mark all sms as PENDING to be sent again which are FAILED since the time specified, provided that specified retries are not exceeded
  set @QueryString='';
  SET @QueryString=concat("update outboundmessage 
					set status = 'PENDING' 
					, failureCause = concat(IFNULL(failureCause,''),'FAILED_RETRY;') 
					, errormessage = concat(IFNULL(errormessage,''),'FAILED_RETRY ', now(), ';') 
					where projectId = ",deviceProjectId," AND status = 'FAILED' 
					and duedate > DATE_SUB(curdate(),INTERVAL 7 DAY) 
					and TIMESTAMPDIFF(MINUTE,(
					case when periodtype ='YEAR' then DATE_ADD(duedate,INTERVAL validityPeriod YEAR) 
					when periodtype ='MONTH' then DATE_ADD(duedate,INTERVAL validityPeriod MONTH) 
					when periodtype ='WEEK' then DATE_ADD(duedate,INTERVAL (validityPeriod*7) DAY) 
					when periodType='DAY' then DATE_ADD(duedate,INTERVAL validityPeriod DAY) 
					when periodtype ='HOUR' then DATE_ADD(duedate,INTERVAL validityPeriod HOUR) 
					when periodtype ='MINTUE' then DATE_ADD(duedate,INTERVAL validityPeriod MINUTE)
					when periodtype ='SECOND' then DATE_ADD(duedate,INTERVAL validityPeriod SECOND)
					else DATE_ADD(duedate,INTERVAL 1 SECOND) END), now()) < 60  
					and DATE_ADD(systemProcessingStartDate,INTERVAL ",failedRetryIntervalmin," MINUTE) < now() 
					and tries <= " , maxRetries);
  PREPARE stmt1 FROM @QueryString;
  EXECUTE stmt1; 
  DEALLOCATE PREPARE stmt1;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-22 12:03:59
