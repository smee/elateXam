#!/bin/bash
# Create a new jks keystore with a self signed certificate as well as a PKCS12 keypair. Both
# files can be used for implementing SSL in tomcat 5.5 including client authentication.
commonname="localhost"
echo -n "Please enter the domainname of the elateXam server [" $commonname "]: "
read commonname
ownername="free form name of the institution"
echo -n "Please enter the name of your organization [" $ownername "]: "
read ownername
location="Unknwown"
echo -n "Please enter your location/city: [" $location "]: "
read location
password="password"
echo -n "Please enter the password for all keys: "
read -s password
echo ""

# use default values for empty variables
: ${commonname:="localhost"}
: ${ownername:="Name of the organization"}
: ${password:="password"}
: ${location:="Unknown"}

serveralias="tomcat"
clientalias="clientCert"

echo "Generating new server certificate key..."
keytool -genkeypair -alias ${serveralias} -keyalg RSA -validity 365 -keystore server.keystore -dname cn="${commonname}",o="${ownername}",l="${location}" -keypass ${password} -storepass ${password}
sleep 1
echo "Generating new client certificate key..."
keytool -genkeypair -alias ${clientalias} -keyalg RSA -validity 365 -storetype pkcs12 -keystore clientcertificate.p12 -dname cn="${commonname}",o="${ownername}",l="${location}"  -storepass ${password} -keypass ${password}

# export certificates
echo "Importing public client certificate into server keystore..."
keytool -exportcert -alias ${clientalias} -keystore clientcertificate.p12 -storetype pkcs12 -storepass ${password} -file client-public.cer
keytool -importcert -v -alias ${clientalias} -file client-public.cer -keystore server.keystore -storepass ${password} -noprompt -trustcacerts

rm client-public.cer
keytool -list -v -keystore server.keystore -storepass ${password}
echo "Please import clientcertificate.p12 into your browser, use server.keystore as keystore/truststore in tomcat 5.5."
echo "For details on configuring tomcat please refer to http://tomcat.apache.org/tomcat-5.5-doc/ssl-howto.html"
echo "Done!"

