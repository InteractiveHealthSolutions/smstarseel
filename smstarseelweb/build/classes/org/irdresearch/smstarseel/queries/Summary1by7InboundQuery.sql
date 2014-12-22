SELECT 'Date', 'Total', 'Originators'
UNION 
SELECT CAST(recieveDate AS CHAR(10)), CAST(total AS CHAR(10)), CAST(originator AS CHAR(10)) FROM 
(
SELECT DATE(recieveDate) recieveDate, COUNT(*) total, COUNT(DISTINCT originator) originator FROM inboundmessage i 
WHERE i.recieveDate >= SUBDATE(CURDATE(), INTERVAL 7 DAY) 
GROUP BY DATE(i.recieveDate) DESC) AS innert;