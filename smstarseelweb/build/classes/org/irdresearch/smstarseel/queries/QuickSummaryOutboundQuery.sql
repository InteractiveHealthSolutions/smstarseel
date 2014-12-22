SELECT 
	SUM(CASE WHEN DATE(latestDue)=CURDATE() THEN totalToday ELSE 0 END) dueToday, 
	SUM(CASE WHEN DATE(latestDue)=CURDATE() THEN pendingToday ELSE 0 END) pendingToday, 
	SUM(CASE WHEN DATE(latestDue)=CURDATE() THEN sentToday ELSE 0 END) sentToday,
	MAX(latestSent) latestSent, 
	MAX(latestDue) latestDue,
	SUM(totalToday) dueLastWeek , 
	SUM(sentToday) sentLastWeek, 
	SUM(CASE WHEN DATE(latestDue)=CURDATE() THEN recipients ELSE 0 END) recipientsToday,  
	TRUNCATE(AVG(sentToday),2) avgDailySentRate,
	TRUNCATE((SUM(sentToday)/SUM(totalToday))*100,2) weeklySentPercent 
FROM (
	SELECT MAX(sentdate) latestSent, MAX(duedate) latestDue, COUNT(*) totalToday, 
	SUM(CASE WHEN status='PENDING' THEN 1 ELSE 0 END) pendingToday, 
	SUM(CASE WHEN status='SENT' THEN 1 ELSE 0 END) sentToday, 
	COUNT(DISTINCT recipient) recipients
	FROM outboundmessage WHERE DATE(duedate) >= SUBDATE(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(duedate)
) AS innertbl