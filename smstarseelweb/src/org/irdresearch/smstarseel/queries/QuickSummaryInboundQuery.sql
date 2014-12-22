SELECT MAX(latestReceived) latestReceived, 
	SUM(CASE WHEN DATE(latestReceived)=CURDATE() THEN readToday ELSE 0 END) readToday, 
	SUM(CASE WHEN DATE(latestReceived)=CURDATE() THEN unreadToday ELSE 0 END) unreadToday,
	SUM(CASE WHEN DATE(latestReceived)=CURDATE() THEN originators ELSE 0 END) originatorsToday,  
	SUM(totalToday) receivedLastWeek , 
	TRUNCATE(AVG(totalToday),2) avgDailyReceiveRate
FROM (
	SELECT MAX(recievedate) latestReceived, COUNT(*) totalToday, 
	SUM(CASE WHEN status='READ' THEN 1 ELSE 0 END) readToday, 
	SUM(CASE WHEN status='UNREAD' THEN 1 ELSE 0 END) unreadToday, 
	COUNT(DISTINCT originator) originators
	FROM inboundmessage WHERE DATE(recievedate) >= SUBDATE(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(recievedate)
) AS innertbl