SELECT 'Total','Time Range','Sent','Failed','Missed','Waiting','Recipients' 
UNION 
SELECT CAST(COUNT(*) AS CHAR(12)) Total, CAST(CONCAT(MIN(TIME_FORMAT(duedate,'%H:%i:%s')),' - ',MAX(TIME_FORMAT(duedate,'%H:%i:%s'))) AS CHAR(100)) DATETIME, 
CAST(SUM(CASE WHEN status='SENT' THEN 1 ELSE 0 END) AS CHAR(10)) sent, 
CAST(SUM(CASE WHEN status='FAILED' THEN 1 ELSE 0 END) AS CHAR(10)) failed, 
CAST(SUM(CASE WHEN status='MISSED' THEN 1 ELSE 0 END) AS CHAR(10)) missed,
CAST(SUM(CASE WHEN status IN ('PENDING','UNKNOWN') THEN 1 ELSE 0 END) AS CHAR(10)) waiting, 
CAST(COUNT(DISTINCT recipient) AS CHAR(10)) recipients 
FROM outboundmessage o 
WHERE DATE(duedate) = CURDATE() 
AND projectId = @projectId 
GROUP BY SUBSTRING(duedate, 1, 12) 
UNION 
SELECT CAST(COUNT(*) AS CHAR(12)) Total, CONCAT('Today: ',DATE(duedate)) DATE, 
CAST(SUM(CASE WHEN status='SENT' THEN 1 ELSE 0 END) AS CHAR(10)) sent, 
CAST(SUM(CASE WHEN status='FAILED' THEN 1 ELSE 0 END) AS CHAR(10)) failed, 
CAST(SUM(CASE WHEN status='MISSED' THEN 1 ELSE 0 END) AS CHAR(10)) missed,
CAST(SUM(CASE WHEN status IN ('PENDING','UNKNOWN') THEN 1 ELSE 0 END) AS CHAR(10)) waiting, 
CAST(COUNT(DISTINCT recipient) AS CHAR(10)) recipients 
FROM outboundmessage o 
WHERE DATE(duedate) = CURDATE() 
AND projectId = @projectId 