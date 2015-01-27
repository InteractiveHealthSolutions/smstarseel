SMSTARSEEL
----------

### TROUBLESHOOTING TIPS:
#### Application crashes as opened: 
This can occur when you sdcard is installed. Make sure to install and mount an sdcard. If problem still persists contact program vendor.
#### Application freezes on pressing `Login` button: 
Wait for a minute. Connection retry interval is 30 sec. It would display respective error message after 30 sec.
##### Malformed IPV4 address: 
The server address / url you provided is incorrect. Check if there are any spaces, linebreaks, special characters in server url. Make sure that url conform to standard url pattern.
##### Transport endpoint is not connected: 
Make sure server is up and running. Mostly this error occurs when no server/web service is running on given url
##### Network is unreachable: 
Make sure your wifi is turned on, and internet and WLAN is accessible (if web app is on public IP) or WLAN is connected (if web app can be accessed on local IP)
##### No value for respcd: 
Make sure that application Smstarseel is hosted on given url, and there was no error or exception in deployment process. Mostly this error occurs when there are critical and severe errors during deployment and application is partially deployed, i.e. server is started but application is crashed and not available for serving the requests. In short this error mostly occurs when server url is correct and also up and running, but it doesnot have corresponding Smstarseel web app for handling requests from mobile application.
##### How to make sure web service is up and running: 
If any error message related to connection and server problems appears: 
•	Make sure wifi is turned on
•	Make sure url and port values provided are correct and well formed.
•	If all above seems fine then close application, open mobile web browser, navigate to url provided, remove end part ‘/smstarseel’ i.e. url should only be http://yourweburl:port/smstarseelweb (replace yourweburl and port with respective values) . If it does not show welcome message, make sure again that your url and port values are correct. 
•	If still it does not work, make sure you can access internet on your phone (check google). 
•	Make sure your server is on public IP or is accessible on WLAN (try accessing server url from any other computer or device).
•	Try configuring your network connection settings, so that you can access application http://yourweburl:port/smstarseelweb from any external device (A welcome message would appear)
##### Invalid username / password: 
Make correct username and password is provided. Note that usernames and passwords are case-sensitive.
##### Permissions not granted/User authentication error: 
Contact your system administrator and ask for a username and password that have permissions to perform DEVICE_OPERATIONS
##### A user is already logged in: 
Look for another button beside ‘Login’ with label ‘Unlock Screen’. Logging in again may interrupt with services already running from last login. Login should be used only if there is no option for Unlocking Screen or you have forgotten username and password previously used.
##### Date and time on phone invalid: 
Make sure datetime on phone is in sync with server upto 1 hour. Make sure time zone is corresponding with server`s time zone. If after changing time zone it still displays same error message, set correct time timezone, set correct datetime and do a phone restart. It should work now.
##### Device is already registered for xxxx: 
If you see this warning, make sure that you are picking up correct project name from list. If device was registered for any other project on last login, this message appears to reduce chances of incorrect project registration.
##### Other known glitches: 
•	If you donot see any recent activity on Console but services (sms/call) are turned on, wait for a while for error message to appear. Mostly, these are due to lost connectivity, which could be ‘Connection timeout’, ‘Peer not found’, ‘IOExceptions’, ‘No value for respcd’. Resolve these errors by making sure that wifi and network connectivity is fine. If it doesnot show any error message, shutdown and start services (SmsDispenser, SmsCollector, CallLogReader ) again.
•	Smses not sent or received into database: Make sure application on phone is running. If application has crashed just restart phone, login again and restart services again.
•	Check wifi connectivity in notification area (top bar on every android phone). Wifi icon may be missing. It is a known android problem that a device disconnects permanently if it doesnot find network for a long period of time and it doesnot connect automatically again on problem resolution. Solution to this is resetting wifi manually. Goto android wifi settings, turn off wifi, and then again turn on. It would reconnect and obtain IP address; a wifi icon would appear in the notification bar. Now goto smstarseel application, unlock screen, and check in console if it has started sending smses or reading smses again. Wait for a while, if it doesnot display any message, logout and login again. Restart services and it should start working as expected.
•	If nothing works, try restarting device, logging in and restarting services again. If it still does not work, contact program vendor.