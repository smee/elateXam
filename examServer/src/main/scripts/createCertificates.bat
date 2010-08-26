@echo off
rem author sdienst@informatik.uni-leipzig.de
rem Create a new jks keystore with a self signed certificate as well as a PKCS12 keypair. Both
rem files can be used for implementing SSL in tomcat 5.5 including client authentication.

set commonname=localhost
set /P commonname=Please enter the domainname of the elateXam server [ %commonname% ]: 
if ""=="%commonname%" set commonname=localhost

set ownername=free form name of the institution
set /P ownername=Please enter the name of your organization [ %ownername% ]: 
if ""=="%ownername%" set ownername=free form name of the institution

set location=Unknwown
set /P location=Please enter your location/city: [ %location% ]: 
if ""=="%location%" set location=Unknwown

set password=password
set /P password=Please enter the password for all keys: 
if ""=="%password%" set password=password

echo ""

set serveralias=tomcat
set clientalias=clientCert

echo Generating new server certificate key...
keytool -genkeypair -alias %serveralias% -keyalg RSA -validity 365 -keystore server.keystore -dname cn="%commonname%",o="%ownername%",l="%location%" -keypass %password% -storepass %password%
sleep 1
echo Generating new client certificate key...
keytool -genkeypair -alias %clientalias% -keyalg RSA -validity 365 -storetype pkcs12 -keystore clientcertificate.p12 -dname cn="%commonname%",o="%ownername%",l="%location%"  -storepass %password% -keypass %password%

rem export certificates
echo Importing public client certificate into server keystore...
keytool -exportcert -alias %clientalias% -keystore clientcertificate.p12 -storetype pkcs12 -storepass %password% -file client-public.cer
keytool -importcert -v -alias %clientalias% -file client-public.cer -keystore server.keystore -storepass %password% -noprompt -trustcacerts

del client-public.cer
keytool -list -keystore server.keystore -storepass %password%

echo ""
echo Please import clientcertificate.p12 into your browser, use server.keystore as keystore/truststore in tomcat 5.5.
echo For details on configuring tomcat please refer to http://tomcat.apache.org/tomcat-5.5-doc/ssl-howto.html
echo Done!

