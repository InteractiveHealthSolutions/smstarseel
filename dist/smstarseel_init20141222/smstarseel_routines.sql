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

-- Dump completed on 2014-12-22 12:03:42
