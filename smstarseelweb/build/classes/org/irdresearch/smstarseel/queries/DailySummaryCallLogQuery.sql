SELECT 'Total','Time Range','Read','Unread','Callers' 
UNION 
SELECT CAST(COUNT(*) AS CHAR(12)) Total,  CAST(CONCAT(MIN(TIME_FORMAT(callDate,'%H:%i:%s')),' - ',MAX(TIME_FORMAT(callDate,'%H:%i:%s'))) AS CHAR(100)) DATETIME, 
CAST(SUM(CASE WHEN callStatus='READ' THEN 1 ELSE 0 END) AS CHAR(10)) readc, 
CAST(SUM(CASE WHEN callStatus='UNREAD' THEN 1 ELSE 0 END) AS CHAR(10)) unreadc, 
CAST(COUNT(DISTINCT callerNumber) AS CHAR(10)) callerNumber 
FROM calllog c  
WHERE DATE(callDate) = CURDATE() 
AND projectId = @projectId
GROUP BY SUBSTRING(callDate, 1, 12)
UNION
SELECT CAST(COUNT(*) AS CHAR(12)) Total, CONCAT('Today: ',DATE(callDate)) DATE, 
CAST(SUM(CASE WHEN callStatus='READ' THEN 1 ELSE 0 END) AS CHAR(10)) readc, 
CAST(SUM(CASE WHEN callStatus='UNREAD' THEN 1 ELSE 0 END) AS CHAR(10)) unreadc, 
CAST(COUNT(DISTINCT callerNumber) AS CHAR(10)) callerNumber 
FROM calllog c 
WHERE DATE(callDate) = CURDATE() 
AND projectId = @projectId