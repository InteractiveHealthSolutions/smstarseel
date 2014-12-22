The project is API component of smstarseel and is responsible for managing all communication between database and user business logic.

The API can be added to any project to build capability to communicate with smstarseel Database.

For API user:
------------
If you are a developer and want to integrate your project to use smstarseel API or drive the behavior of smstarseel according to your business logic, the process is really simple; add jar into your project and use services to communicate with smstarseel database. Below is detailed process of how to do it.

Before starting using the API it is assumed that you have 

# Setup smstarseel database
Database init scripts are in project`s dist folder. Create a database with name smstarseel and run scripts for table creation. You can either run smstarseel_init20141222.sql single script file OR use smstarseel_init20141222 dump folder for setting up database.

# Add smstarseel and dependencies into classpath
Export smstarseel jar from this project (make sure to exclude .settings, dist, lib) OR get smstarseel.jar from dist folder and add this jar in your project`s classpath/lib 

Apart from smstarseel jar you need to include following libraries into your project`s classpath that smstarseel is dependant on. These jar files could be found in dist/required folder or could be downloaded from internet

- antlr-2.7.7.jar
- c3p0-0.9.1.jar
- commons-collections-3.2.1.jar
- dom4j-1.6.1.jar
- hibernate-c3p0-3.6.0.Final.jar
- hibernate-commons-annotations-3.2.0.Final.jar
- hibernate-core-3.6.5.Final-mod.jar
- hibernate-jpa-2.0-api-1.0.1.Final.jar
- javassist-3.17.1-GA.jar
- joda-time-2.0.jar
- jta-1.1.jar
- log4j-1.2.16.jar
- mysql-connector-java-5.1.20.jar
- slf4j-api-1.6.4.jar
- slf4j-log4j12-1.6.1.jar

##### Note: 
if you donot want to use c3p0 connection pool, comment the C3P0 connection properties in cfg.xml and remove jar files c3p0-0.9.1.jar and hibernate-c3p0-3.6.0.Final.jar from classpath/lib

# Add smstarseel.cfg.xml into classpath. 
Add a hibernate cfg.xml file that smstarseel would use to configure hibernate. The minimum content of file is given below.

smstarseel.cfg.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
		"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
		<property name="hibernate.connection.password">mypassword</property>
		<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/smstarseel</property>
		<property name="hibernate.connection.username">myusername</property>
		<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>

		<!-- MUST be included if you want to use unicode or utf-8 column type, 
			otherwise hibernate is unable to recognize utf-8 input -->
		<property name="hibernate.connection.useUnicode">true</property>
		<property name="hibernate.connection.characterEncoding">UTF-8</property>
		<property name="hibernate.connection.charSet">UTF-8</property>

		<!-- Use the C3P0 connection pool provider -->
		<property name="hibernate.connection.provider_class">org.hibernate.connection.C3P0ConnectionProvider</property>
		<property name="hibernate.c3p0.min_size">5</property>
		<property name="hibernate.c3p0.max_size">20</property>
		<property name="hibernate.c3p0.timeout">120</property>
		<property name="hibernate.c3p0.acquireRetryAttempts">3</property>
		<property name="hibernate.c3p0.max_statements">50</property>
	</session-factory>
</hibernate-configuration>
```

You can also configure smstarseel by providing java.util.Properties instead of providing a cfg.xml

# Instantiate TarseelContext and use services as per requirement
Once configured successfully you need to instantiate service engine once and use services as per your logic.

```
TarseelContext.instantiate(null, "smstarseel.cfg.xml"); 
// Or provide java.util.properties as first argument. This method must be called once and only once in code before using TarseelServices.

TarseelServices tsc = TarseelContext.getServices();

tsc.getSmsService().....
```

For API developer:
-----------------


