csvloader
=========

Utility for loading .csv files to Databases

Usage
-----

If you want to add .csv uploading functional to database in your "spring-applications", just add this configuration in "application-context.xml":

```xml
  <bean id="dataSource"
  	class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="${jdbc.driverClassName}" />
		<property name="url" value="${jdbc.url}" />
		<property name="username" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
	</bean>

	<bean id="cheques"
		class="org.alexbezverkhniy.csvloader.dbloader.BaseDatabaseLoader">
		<property name="seprator" value=";" />
		<property name="tableName" value="public.cheques" />
		<property name="columnNames" value="ID, NUMBER, SUMM, BONUS, CREATE_DATE" />
		<property name="dateFormat" value="yyyy-MM-dd" />
		<property name="dataSource" ref="embeddedDataSource" />
	</bean>
	
	<context:property-placeholder location="jdbc.properties" />
```

or embedded-database mode:

```xml
    <jdbc:embedded-database id="embeddedDataSource" type="HSQL">
        <jdbc:script location="classpath:schema.sql"/>
    </jdbc:embedded-database>
```

For starting the uploading process of .csv file, add this code:

```java
  ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
  BaseDatabaseLoader databaseLoader = (BaseDatabaseLoader) context.getBean("cheques");
```

If your database uses non-standard sql syntax, you can overload method `loadData` of `BaseDatabaseLoader` class in your class child.

