SELECT 'Date', 'Total', 'Callers'
UNION 
SELECT CAST(callDate AS CHAR(10)), CAST(total AS CHAR(10)), CAST(callerNumber AS CHAR(10)) FROM 
(
SELECT DATE(callDate) callDate, COUNT(*) total, COUNT(DISTINCT callerNumber) callerNumber FROM calllog c 
WHERE c.callDate >= SUBDATE(CURDATE(), INTERVAL 7 DAY) 
GROUP BY DATE(c.callDate) DESC) AS innert;