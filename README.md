For Smstarseel Users:
--------------------
The Smstarseel is a pluggable solution for your projects requiring bulk sms sending, received sms or call log logging from mobile device to web server. It comprises of three components.

### 1- smstarseel-web:
The web app is responsible for handling communication between smstarseel-mobile and underlying database. The web component also allows you to manage configuration for system and get a detailed overview of system's activity via cool looking user interface. It provides you to view all historic sms/call data and current system configuration. It also allows to get daily email notifications of system's activity i.e. (sms sent/received, call log).

### 2- smstarseel-mobile: 
The mobile app is deployed on android mobile device that physically receives sms/calls or sends sms logged into smstarseel database via web app. The mobile app constantly pings server (smstarseelweb) for any new or due sms to be sent and updates the status back to server. It also logs received sms i.e. inbox or call log on mobile into smstarseel database. All these services are configurable and user have full control over the start, stop and ping intervals for each service.

### 3- smstarseel-api: 
**If you are not a developer jump to** [SMSTRSEEL SETUP](#smstrseel-setup)  

Smstarseel is designed as a generic application for managing call log, sms inbox and allows to send sms to any recipient. The application doesnot associate recipient with any business entity and neither does it map any sms with any event. It is just a log of activity of sms or call sent/received on device. 

Sometimes our business logic is derived based on sms or call log and we need to control further processing based on certain communication events from our entities. This can not be done with smstarseel alone, because it has no knowledge of our business objects and events. To use smstarseel for this purpose we modify the behavior of system by adding few tables in our project and hooking certain event handlers on sms/call received or sent events. The smstarseel-api (smstarseelmodel) facilitates developers to access smstarseel's underlying database and services.

If you need to use this functionality read detailed [README of smstarseelmodel](smstarseelmodel). For details on internal architecture and working of smstarseel read wiki or README for each project. 

Below are the steps on how to setup smstarseel on your server.

## SMSTRSEEL SETUP:
To setup smstarseel you must have 
- Basic knowledge of application deployment on tomcat, database backup/restore on mysql and/or linux shell commands for ssh, tomcat services, mysql services
- Access to tomcat server to deploy web app. 
- Privileged access to mysql to setup database (smstarseel is only tested on mysql so donot try configuring it for any other database server)
- Android mobile device (above GingerBread [2.3.3] and below Kitkat [4.4]). If sms send rate is high, make sure to get a rooted device with sms limit removed. Android restricts sms dispatch rate to 100 sms/hour only. The is even lower on some some Jellybean devices 30 sms/30 min. All extra sms are failed. For details read [here](https://plus.google.com/+AlSutton/posts/6XP2HcvPkvX))

### Database Initialization:
Database init scripts are in dist folder. You can either run `smstarseel_init20141222.sql` single script file ***OR*** use `smstarseel_init20141222` dump folder for mysql-workbench for setting up database.

***Note:*** Running above scripts on a server that already has smstarseel database would override all existing tables and flush all data. These scripts are ONLY for setting up smstarseel first time.

On Workbench: Read tutorial [here](https://help.fasthosts.co.uk/app/answers/detail/a_id/1404/~/back-up-and-restore-mysql-databases-using-mysql-workbench-5) to see how to import data using mysql workbench (pictures may differ accross different versions of workbench).

On Commanline: Read [this](http://www.linuxbrigade.com/import-mysql-database-command-line/) for commands to import database using commandline

### Web app deployment:
- Export war from project smstarseelweb **OR** find a distribution smstarseelweb.war in dist folder. Change database credentials and other properties in WEB-INF/classes/smstarseel.properties file.

Properties those MUST be changed are given below. If you are a technical user and understand hibernate well, modify other properties according to your server capacity.
```
# Database credentials
smstarseel.database.connection.username=root
smstarseel.database.connection.password=yourpassword
smstarseel.database.connection.url=jdbc:mysql://localhost:3306/smstarseel
```
Smstrseel sends admin crash alerts and server activity daily summary emails. For this you must have an email account on gmail ***OR*** your own POP3 email server. 

If you donot have an email account create one on gmail. Then goto [this](https://www.google.com/settings/security/lesssecureapps) page to allow application use this account for automated emails. Change email settings specified below. 
```
# -------------------------------------EMAIL ENGINE PROPERTIES------------------------------------
mail.user.username=your-gmail-username@gmail.com
mail.user.password=your-gmail-password
```
***Note:*** If you donot have gmail account, other email engine properties should also be modified according to your email server. Not all servers allow emails to be sent programmatically. Ask your email server administrator about other properties. i.e.
```
mail.transport.protocol=smtp
mail.host=smtp.gmail.com
mail.user.username=youusername@gmail.com
mail.user.password=yourpassword
mail.smtp.auth=true
mail.smtp.port=465
```

- Stop your tomcat server, deploy war file and start it again. If you are updating a previous smstarseel instance make sure to clean logs and work directory to clear cache. Sometimes hot swaping or restart command on tomcat causes few memory and resource leaks in app. Hence, stopping services and starting again after cleaning cache and deployment is strongly recommended.

- Test if smstarseel has been deployed successfully by navigating to http://your-server-url:your-port/smstarseelweb on your web browser. (Welcome to smstarseel shows that web app has been deployed).

### Web app and system configuration:
- Navigate to http://your-server-url:your-port/smstarseelweb/login.htm and login using default credentials admin:admin123
- Goto Admin/Settings, read all and configure according to your server specs and bandwidth. You can leave all as it is if these meet your requirements, however, two settings notifier.daily-summary.recipients and admin.email-address must be modified.
- Goto Admin/Project or devices and edit the Test project name. 
- Goto Admin/Users and create a new user for device so that your mobile device monitor doesnot need to have admin credentials
- And donot forget to change your (admin) password.

Your web app has been configured successfully and now its time to test mobile app.

### Mobile app deployment and configuration:
- Export an apk file from project smstarseel **OR** get one from dist folder (smstarseel.apk).
- Make sure your device has sdcard/memory card, a wifi/grprs connection and a sim with credit (Wifi is recommended since application uses huge amount of bandwidth) 
- Make sure that the device setting Security/Unknown Sources (allow installation of apps from unknown sources) is checked.
- Install apk file on your android mobile device. 
	Connect device to your computer via usb cable.
	Make sure the connect profile is not charge only.
	Copy apk file on device sdcard
- Install and open smstarseel
- Change server url (http://your-server-url:your-port/smstarseelweb/smstarseel) to one provided to you i.e. your web server url and port. Leave /smstarseelweb/smstarseel part of url as it is
- Enter your device username and password to login. In some devices due to screen resolution complete url is not visible. Tap on url and rotate device to view  full url.
- You must register your device for project you want to use sms service for. Many sims donot allow sim number to be read by custom apps. If you find sim-number textfield empty, fill it manually.
- Choose the project carefully and register.
- Choose Console Log. It would show your sim number, imei, user and project registered. This shows all activity going on in your device at any point of time
- Choose Settings. It allows you to configure sms/call services according to your device's load capacity.
	- 1- Setting Admin cell number allows you to get sms notifications incase device is unable to access server. Note that in some cases it can send spam sms if your wifi connectivity is very poor. Provide admin cell number only and only if you have a good wifi/internet connection and sms cost in not high
	- 2- Choose Sms Dispenser Fetch Interval. Note that if your server or internet connectivity is poor, a very short time interval could lead to frequent app/server crahes. A high inteval would lead to low sms rate or missed smses. Choose this setting according to your server/wifi capacity and the expected rate of sms sending. If at any point of time sms rate could be 100sms/hour, get a rooted android device with sms limit removed.
	- 3- Similarly, choose Sms collector and Calllog reader service intervals keeping in mind the business requirements of your app.
	- 4- Sms collector and call log reader fetch size should not be high if you have poor internet connectivity.
- Start services as per requirement. Sms Dispenser (sender), Sms Collector (inbox reader) and CallLog Reader (device call log reader) work completely independent of each other.
- Lock Screen and close app. Note that Logout would shutdown all services so make sure to choose only if you want all services to shutdown.
- If you need to access app again, make sure to press Unlock screen button and NOT Login(logging in again would shutdown all services)

**Disclaimer**: Starting services would immediately start sending scheduled due sms (if any), transfer all inbox sms and call log on server and delete from device. Do not use it on your personal cell phone for testing.