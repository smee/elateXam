# elateXam - Open Source eAssessment #

This repository contains the complete code of a GPL v2 licensed eAssessment solution suitable for exams in an educational context.

Subprojects
-----------

* taskmodel  
 - embeddable components (api, reference implementation, addons)
* examserver 
 - Struts 1/JSP based server, runs in Jetty or Tomcat
* taskmodel-log 
 - client for remote log4J-logs [SocketHubAppender of log4j](http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j
* elateXam-integrationtest 
 - [Selenium](http://seleniumhq.org/) UI tests for examserver

Building
---------

Make sure you have a recent installation of [Java JDK 1.6.0+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) as well as [Maven 2](http://maven.apache.org/download.html).

1. Checkout sources: `git checkout git://github.com/smee/elateXam.git`
2. Build and install the taskmodel:
    cd taskmodel
    mvn clean install
    cd ..
3. Build and install the examserver:
    cd examserver
    mvn clean install

If you have an installation of Tomcat v5.5 available, you may also directly install all needed artifacts by changing the maven commands to
    mvn clean install -PdeployTomcat -Dtomcat.path=/path/to/tomcat-5.5.27/'
