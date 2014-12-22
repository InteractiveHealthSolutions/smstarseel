SELECT MAX(latestCallDate) latestCallDate, 
	SUM(CASE WHEN DATE(latestCallDate)=CURDATE() THEN readToday ELSE 0 END) readToday, 
	SUM(CASE WHEN DATE(latestCallDate)=CURDATE() THEN unreadToday ELSE 0 END) unreadToday,
	SUM(CASE WHEN DATE(latestCallDate)=CURDATE() THEN callersToday ELSE 0 END) callersToday,  
	SUM(totalToday) callsLastWeek , 
	TRUNCATE(AVG(totalToday),2) avgDailyCallRate
FROM (
	SELECT MAX(callDate) latestCallDate, COUNT(*) totalToday, 
	SUM(CASE WHEN callStatus='READ' THEN 1 ELSE 0 END) readToday, 
	SUM(CASE WHEN callStatus='UNREAD' THEN 1 ELSE 0 END) unreadToday, 
	COUNT(DISTINCT callerNumber) callersToday
	FROM calllog WHERE DATE(callDate) >= SUBDATE(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(callDate)
) AS innertbl