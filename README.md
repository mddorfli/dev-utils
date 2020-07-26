# Embedded Jetty template application

This is a template for a web application that uses embedded Jetty. The sample code consists of a JSP (this page) and a simple servlet.

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -cp target/classes:target/dependency/* com.example.Main



    17:20:14.906 [MyoshWeb-BGTask-Thread] DEBUG org.hibernate.SQL - SELECT rrf.id,rrf.field_id,rrf.displayed_field_id,CAST(rrf.display_order AS INTEGER),rrf.is_optional,rrf.is_keyword,rrf.is_hierarchy_type,f.module_form_link_target_id,f.fieldtype FROM recordlink_recordlist_field rrf INNER JOIN field f ON (rrf.field_id = f.id AND f.id IN (?, ?, ?, ?)) WHERE rrf.field_id IN (?, ?, ?, ?) and f.removed=false
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [144]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [2] as [BIGINT] - [145]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [3] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [4] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [5] as [BIGINT] - [144]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [6] as [BIGINT] - [145]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [7] as [BIGINT] - [146]
    17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [8] as [BIGINT] - [146]