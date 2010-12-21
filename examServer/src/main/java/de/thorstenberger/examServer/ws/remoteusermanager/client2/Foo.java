package de.thorstenberger.examServer.ws.remoteusermanager.client2;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

public class Foo {

  public static void main(String[] args) throws Exception {
    String wsdl = "http://elate.uni-leipzig.de:9080/elatePA/services/RemoteUserManager?wsdl";
    URL url = new URL(wsdl);

    QName serviceName = new QName("http://server.ws.uebman.thorstenberger.de", "RemoteUserManagerService");
    QName portName = new QName("http://server.ws.uebman.thorstenberger.de", "RemoteUserManager");

    Service service = Service.create(url, serviceName);
    Dispatch<SOAPMessage> dispatch = service.createDispatch(portName,
        SOAPMessage.class, Service.Mode.MESSAGE);

    MessageFactory mf = MessageFactory.newInstance();
    SOAPMessage req = mf.createMessage();

    SOAPElement operation = req.getSOAPBody().addChildElement("getUserData", "ns1", "http://server.ws.uebman.thorstenberger.de");
    SOAPElement username = operation.addChildElement("username");
    username.addTextNode("wstest");
    SOAPElement pw = operation.addChildElement("password");
    pw.addTextNode("testws");
    req.saveChanges();
    try {
    SOAPMessage res = dispatch.invoke(req);
    res.writeTo(System.out);
      System.out.println();
      System.out.println(res.getSOAPBody());
    } catch (SOAPFaultException e) {
      e.printStackTrace();
    }

  }

}
