SELECT 'Total','Time Range','Read','Unread','Senders' 
UNION 
SELECT CAST(COUNT(*) AS CHAR(12)) Total, CAST(CONCAT(MIN(TIME_FORMAT(recieveDate,'%H:%i:%s')),' - ',MAX(TIME_FORMAT(recieveDate,'%H:%i:%s'))) AS CHAR(100)) DATETIME, 
CAST(SUM(CASE WHEN status='READ' THEN 1 ELSE 0 END) AS CHAR(10)) readm, 
CAST(SUM(CASE WHEN status='UNREAD' THEN 1 ELSE 0 END) AS CHAR(10)) unreadm, 
CAST(COUNT(DISTINCT originator) AS CHAR(10)) originator 
FROM inboundmessage i 
WHERE DATE(recieveDate) = CURDATE() 
AND projectId = @projectId
GROUP BY SUBSTRING(recieveDate, 1, 12)
UNION
SELECT CAST(COUNT(*) AS CHAR(12)) Total, CONCAT('Today: ',DATE(recieveDate)) DATE, 
CAST(SUM(CASE WHEN status='READ' THEN 1 ELSE 0 END) AS CHAR(10)) readm, 
CAST(SUM(CASE WHEN status='UNREAD' THEN 1 ELSE 0 END) AS CHAR(10)) unreadm, 
CAST(COUNT(DISTINCT originator) AS CHAR(10)) originator 
FROM inboundmessage i 
WHERE DATE(recieveDate) = CURDATE() 
AND projectId = @projectId