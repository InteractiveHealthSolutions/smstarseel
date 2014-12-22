SELECT 'Date', 'Total', 'Sent', 'Failed', 'Missed', 'Waiting', 'Recipients'
UNION 
SELECT CAST(duedate AS CHAR(10)), CAST(total AS CHAR(10)), CAST(sent AS CHAR(10)), CAST(failed AS CHAR(10)), CAST(missed AS CHAR(10)),
CAST(waiting AS CHAR(10)), CAST(recipients AS CHAR(10)) FROM 
(SELECT DATE_FORMAT(DATE(o.duedate),'%d-%m-%Y') duedate, COUNT(*) total, 
SUM(CASE WHEN status='SENT' THEN 1 ELSE 0 END) sent, 
SUM(CASE WHEN status='FAILED' THEN 1 ELSE 0 END) failed, 
SUM(CASE WHEN status='MISSED' THEN 1 ELSE 0 END) missed,
SUM(CASE WHEN status IN ('PENDING','UNKNOWN') THEN 1 ELSE 0 END) waiting, 
COUNT(DISTINCT recipient) recipients
FROM outboundmessage o 
WHERE o.duedate >= SUBDATE(CURDATE(), INTERVAL 14 DAY) 
GROUP BY DATE(o.duedate) DESC) AS innert;