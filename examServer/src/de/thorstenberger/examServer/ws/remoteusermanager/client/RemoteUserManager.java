/**
 * RemoteUserManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package de.thorstenberger.examServer.ws.remoteusermanager.client;

public interface RemoteUserManager extends java.rmi.Remote {
    public de.thorstenberger.examServer.ws.remoteusermanager.client.UserBean getUserData(java.lang.String login, java.lang.String pwd) throws java.rmi.RemoteException, de.thorstenberger.examServer.ws.remoteusermanager.client.RemoteUserManagerException;
}
