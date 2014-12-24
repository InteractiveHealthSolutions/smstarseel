For Smstarseel Users:
--------------------
The Smstarseel is a pluggable solution for your projects requiring bulk sms sending, received sms or call log logging on web server. It comprises of three components.
1- smstarseel-web:
The web app is responsible for handling communication between smstarseel-mobile and the underlying database. The web component also allows you to manage configuration for system and get a detailed overview of system's activity via cool looking user interface.

Export war from project smstarseelweb ~~OR~~ find a distribution smstarseelweb.war in dist folder. Change database credentials and other properties in WEB-INF/classes/smstarseel.properties file.

2- smstarseel-mobile: 
The mobile app is deployed on android mobile device that physically receives sms/calls or sends sms logged into smstarseel database. The mobile app constantly pings server (smstarseelweb) for any new or due sms to be sent and updates the status back to server. It also logs received sms or call log on mobile in smstarseel database. All these services are configurable and user have full control over the start, stop and ping intervals for each service.

3- smstarseel-api: 
Smstarseel is designed as a generic application for managing call log, sms inbox and allows to send sms to any recipient. The application doesnot associate recipient with any business entity and neither does it map any sms with any event. It is just a log of activity of sms or call sent/received on device. 

Sometimes our business logic is derived based on sms or call log and we need to control further processing based on certain communication events from our entities. This can not be done with smstarseel alone, since it has no knowledge of our business objects and events. To use smstarseel for this purpose we modify the behavior of system by adding few tables in our project and hooking certain event handlers on sms/call received or sent events. The smstarseel-api (smstarseelmodel) facilitates developers to access smstarseel's underlying database and services.

If you need to use this functionality read README of smstarseelmodel.

For details on internal working of smstarseel read wiki or README for each project. Below are the steps on how to setup smstarseel on your server.

## SMSTRSEEL SETUP:
To setup smstarseel you must have 
- Access to tomcat server to deploy web app. 
- Privileged access to mysql database to setup database (smstarseel in only tested on mysql so donot try configuring it for any other database server)
- Android mobile device (above GingerBread 2.3.3 and below Kitkat 4.4). If sms sending rate is too high make sure you have rooted device otherwise above 100 sms/hour would fail to send. (details ??????????????????????????????????)

### Database Initialization:
- Create a database with name smstarseel and run scripts for table creation. `Database init scripts are in project's dist folder. You can either run smstarseel_init20141222.sql single script file OR use smstarseel_init20141222 dump folder for setting up database.`

### Web app deployment:
- Export war from project smstarseelweb ~~OR~~ find a distribution smstarseelweb.war in dist folder. Change database credentials and other properties in WEB-INF/classes/smstarseel.properties file.

- Stop your tomcat server, deploy war file there and start it again. If you are updating a previous smstarseel instance make sure to clean logs and work directory to clear cache.

- Test if smstarseel has been deployed successfully by navigating to http://your-server-url:your-port/smstarseelweb on your web browser. (Welcome to smstarseel shows that web app has been deployed).

### Web app and system configuration:
- Navigate to http://your-server-url:your-port/smstarseelweb/login.htm and login using default credentials admin:admin123
- Goto Admin/Settings, read all and configure according to your server specs and bandwidth. You can leave all as it is if these meet your requirements, however, two settings notifier.daily-summary.recipients and admin.email-address must be edited.
- Goto Admin/Project or devices and edit the Test project name. 
- Goto Admin/Users and create a new user for device so that your mobile device monitor doesnot need to have admin credentials
- And donot forget to change your (admin) password.

Your web app has been configured successfully and now its time to test mobile app.

### Mobile app deployment and configuration:
- Export an apk file from project smstarseel ~~OR~~ get one from dist folder (smstarseel.apk).
- Make sure you device has sdcard/memory card, a wifi/grprs connection and sim with credit 
- Make sure that the device setting Security/Unknown Sources (allow installation of apps from unknown sources) is checked.
- Install apk file on your android mobile device. 
Connect device to your computer via usb cable.
Make sure the connect profile is not charge only.
Copy apk file on device sdcard
- Install and open smstarseel
- Change server url (http://your-server-url:your-port/smstarseelweb/smstarseel) to one provided to you i.e. your web server url and port. Leave /smstarseelweb/smstarseel part of url as it is
- Enter your device username and password to login. In some devices due to screen resolution complete url is not visible. Tap on url and rotate device to view  full url.
- You must register your device for project you want to use sms service for. Many sims donot allow sim number to be read by custom apps. If you find sim-number text field empty fill it manually with your sim number.
- choose the project carefully and register.
- Choose Console Log. It would show your sim number, imei, user and project registered. This shows all activity currently going on in your device
- Choose Settings. It allows to configure sms/call services according to your device's load capacity.
1- Setting Admin cell number allows you to get sms notifications incase device is unable to access server. Note that in some cases it can send spam sms if your wifi connectivity is very poor. Provide admin cell number only if you have a good wifi/internet connection and sms sending cost in not high
2- Choose Sms Dispenser Fetch Interval. Note that if your server or internet connectivity is poor, a very short time interval could lead to frequent app/server crahes. A high inteval would lead to low sms rate or missed smses. Choose this setting according to your server/wifi capacity and the expected rate of sms sending.
3- Similarly choose Sms collector and Calllog reader service intervals keeping in mind the business requirements of your app.
4- Sms collector and fetch size should not be high if you have poor internet connectivity.
- Start services as per requirement.
- Lock Screen and close app. Logout would shutdown all services so make sure to choose only if you want all services to shutdown.
- If you want to access app again make sure to press unlock screen button (login in again would shutdown all services)

**Disclaimer**: Starting services would start sending logged sms (if any), send inbox sms and call log on server and delete those. Donot use it on your personal cell phone for testing.
