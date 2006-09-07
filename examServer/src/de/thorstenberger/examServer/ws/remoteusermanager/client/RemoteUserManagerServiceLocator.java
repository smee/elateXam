/**
 * RemoteUserManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package de.thorstenberger.examServer.ws.remoteusermanager.client;

public class RemoteUserManagerServiceLocator extends org.apache.axis.client.Service implements de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerService {

    public RemoteUserManagerServiceLocator() {
    }


    public RemoteUserManagerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RemoteUserManagerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RemoteUserManager
    private java.lang.String RemoteUserManager_address = "http://localhost:9080/elatePA/services/RemoteUserManager";

    public java.lang.String getRemoteUserManagerAddress() {
        return RemoteUserManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RemoteUserManagerWSDDServiceName = "RemoteUserManager";

    public java.lang.String getRemoteUserManagerWSDDServiceName() {
        return RemoteUserManagerWSDDServiceName;
    }

    public void setRemoteUserManagerWSDDServiceName(java.lang.String name) {
        RemoteUserManagerWSDDServiceName = name;
    }

    public de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager getRemoteUserManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RemoteUserManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRemoteUserManager(endpoint);
    }

    public de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager getRemoteUserManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerSoapBindingStub _stub = new de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getRemoteUserManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRemoteUserManagerEndpointAddress(java.lang.String address) {
        RemoteUserManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager.class.isAssignableFrom(serviceEndpointInterface)) {
                de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerSoapBindingStub _stub = new de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerSoapBindingStub(new java.net.URL(RemoteUserManager_address), this);
                _stub.setPortName(getRemoteUserManagerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RemoteUserManager".equals(inputPortName)) {
            return getRemoteUserManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.ws.uebman.thorstenberger.de", "RemoteUserManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.ws.uebman.thorstenberger.de", "RemoteUserManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RemoteUserManager".equals(portName)) {
            setRemoteUserManagerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
