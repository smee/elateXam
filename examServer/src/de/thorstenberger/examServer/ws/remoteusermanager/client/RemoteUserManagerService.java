/**
 * RemoteUserManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package de.thorstenberger.examServer.ws.remoteusermanager.client;

public interface RemoteUserManagerService extends javax.xml.rpc.Service {
    public java.lang.String getRemoteUserManagerAddress();

    public de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager getRemoteUserManager() throws javax.xml.rpc.ServiceException;

    public de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManager getRemoteUserManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
