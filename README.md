# Embedded Jetty template application

This is a template for a web application that uses embedded Jetty. The sample code consists of a JSP (this page) and a simple servlet.

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -cp target/dev-utils-1.0-jar-with-dependencies.jar com.omb.devutils.Main

Navigate to localhost:8080

## Running the application via Heroku
With Heroku's CLI installed, run it using:

    heroku local web

Navigate to localhost:5000

## Logging SQL queries in Hibernate
You need to enable logging of SQL and binds in Hibernate. In a log4j2 xml logging configuration, this would look like:

    <Logger name="org.hibernate.SQL" level="DEBUG" additivity="false">
    	<AppenderRef ref="Console" />
    </Logger>
    <Logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE" additivity="false">
    	<AppenderRef ref="Console" />
    </Logger>  

## Usage

With hibernate sql and bind logging enabled, start your application and perform some actions. Copy the SQL you need from the log, e.g.:

    17:20:14.906 [MyoshWeb-BGTask-Thread] DEBUG org.hibernate.SQL - SELECT rrf.id,rrf.field_id,rrf.displayed_field_id,CAST(rrf.display_order AS INTEGER),rrf.is_optional,rrf.is_keyword,rrf.is_hierarchy_type,f.module_form_link_target_id,f.fieldtype FROM recordlink_recordlist_field rrf INNER JOIN field f ON (rrf.field_id = f.id AND f.id IN (?, ?, ?, ?)) WHERE rrf.field_id IN (?, ?, ?, ?) and f.removed=false
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [144]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [2] as [BIGINT] - [145]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [3] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [4] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [5] as [BIGINT] - [144]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [6] as [BIGINT] - [145]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [7] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [8] as [BIGINT] - [146]
    
Paste this into the 'Paste SQL Logs' box and click 'Parse logs'.