/*

Copyright (C) 2010 Steffen Dienst

This program is free software, you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY, without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program, if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.thorstenberger.examServer.pdf.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Encapsulate all needed informations for signing and timestamping pdf documents:
 * <table>
 * <tr><td>keystoreFile</td><td>valid path to a java keystore</td></tr>
 * <tr><td>keystorePassword</td><td>password of the keystore</td></tr>
 * <tr><td>keyAlias</td><td>private key alias</td></tr>
 * <tr><td>privateKeyPassword</td><td>private key password</td></tr>
 * <tr><td>certificateChainAlias</td><td>chain of trust alias</td></tr>
 * <tr><td>timestampServerUrl</td><td>url of a free and trusted(!) timestamp server</td></tr>
 * <tr><td>signatureReason</td><td>metadata for pdf</td></tr>
 * <tr><td>signatureLocation</td><td>metadata for pdf</td></tr>
 * <tr><td>signatureContact</td><td>metadata for pdf</td></tr>
 * </table>
 * @author Steffen Dienst
 *
 */
public class SignatureInfos {
  protected final static Log log = LogFactory.getLog(SignatureInfos.class);
  private String
    keystoreFile,
    keystorePassword, privateKeyPassword,
    keyAlias,
    certificateChainAlias,
    timestampServerUrl,
    signatureReason, signatureLocation, signatureContact;

  /**
   * @param keystoreFile
   *          the keystoreFile to set
   */
  public void setKeystoreFile(final String keystoreFile) {
    this.keystoreFile = keystoreFile;
  }

  /**
   * @param keystorePassword
   *          the keystorePassword to set
   */
  public void setKeystorePassword(final String keystorePassword) {
    this.keystorePassword = keystorePassword;
  }

  /**
   * @param privateKeyPassword
   *          the privateKeyPassword to set
   */
  public void setPrivateKeyPassword(final String privateKeyPassword) {
    this.privateKeyPassword = privateKeyPassword;
  }

  /**
   * @param keyAlias
   *          the keyAlias to set
   */
  public void setKeyAlias(final String keyAlias) {
    this.keyAlias = keyAlias;
  }

  /**
   * @param certificateChainAlias
   *          the certificateChainAlias to set
   */
  public void setCertificateChainAlias(final String certificateChainAlias) {
    this.certificateChainAlias = certificateChainAlias;
  }

  /**
   * @param timestampServerUrl
   *          the timestampServerUrl to set
   */
  public void setTimestampServerUrl(final String timestampServerUrl) {
    this.timestampServerUrl = timestampServerUrl;
  }

  /**
   * @param signatureReason
   *          the signatureReason to set
   */
  public void setSignatureReason(final String signatureReason) {
    this.signatureReason = signatureReason;
  }

  /**
   * @param signatureLocation
   *          the signatureLocation to set
   */
  public void setSignatureLocation(final String signatureLocation) {
    this.signatureLocation = signatureLocation;
  }

  /**
   * @param signatureContact
   *          the signatureContact to set
   */
  public void setSignatureContact(final String signatureContact) {
    this.signatureContact = signatureContact;
  }

  /**
   * @return the keystoreFile
   */
  public String getKeystoreFile() {
    return keystoreFile;
  }

  /**
   * @return the keystorePassword
   */
  public String getKeystorePassword() {
    return keystorePassword;
  }

  /**
   * @return the privateKeyPassword
   */
  public String getPrivateKeyPassword() {
    return privateKeyPassword;
  }

  /**
   * @return the keyAlias
   */
  public String getKeyAlias() {
    return keyAlias;
  }

  /**
   * @return the certificateChainAlias
   */
  public String getCertificateChainAlias() {
    return certificateChainAlias;
  }

  /**
   * @return the timestampServerUrl
   */
  public String getTimestampServerUrl() {
    return timestampServerUrl;
  }

  /**
   * @return the signatureReason
   */
  public String getSignatureReason() {
    return signatureReason;
  }

  /**
   * @return the signatureLocation
   */
  public String getSignatureLocation() {
    return signatureLocation;
  }

  /**
   * @return the signatureContact
   */
  public String getSignatureContact() {
    return signatureContact;
  }

  public SignatureInfos(){
    this.keystoreFile=System.getProperty("user.home") + File.separatorChar + ".keystore";
    this.keystorePassword="";
    this.privateKeyPassword="";
    this.certificateChainAlias="tomcat";
    this.keyAlias="tomcat";
    this.timestampServerUrl="http://zeitstempel.dfn.de";
    this.signatureLocation=getHostname();
    this.signatureContact = "admin@" + signatureLocation;
    this.signatureReason = "Evidence of document integrity";
  }

  /**
   * @return hostname of the current server
   */
  private String getHostname() {
    try {
      final InetAddress addr = InetAddress.getLocalHost();
      return addr.getHostName();
    } catch (final UnknownHostException e) {
      return "localhost";
    }

  }


  /**
   * @return
   * @throws KeyStoreException
   */
  public PrivateKey getPrivateKey() throws KeyStoreException {
    try {

      return (PrivateKey) getKeystore().getKey(getKeyAlias(), getPrivateKeyPassword().toCharArray());

    } catch (final NoSuchAlgorithmException e) {
      throw new KeyStoreException(e);
    } catch (final UnrecoverableKeyException e) {
      throw new KeyStoreException(e);
    }
  }

  /**
   * @return
   * @throws KeyStoreException
   */
  public X509Certificate[] getCertificateChain() throws KeyStoreException {
    final Certificate[] chain = getKeystore().getCertificateChain(getCertificateChainAlias());

    final X509Certificate[] xchain = new X509Certificate[chain.length];
    System.arraycopy(chain, 0, xchain, 0, chain.length);

    return xchain;
  }

  private KeyStore getKeystore() throws KeyStoreException {
    try {
      final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(new FileInputStream(getKeystoreFile()), getKeystorePassword().toCharArray());
      return ks;
    } catch (final NoSuchAlgorithmException e) {
      throw new KeyStoreException(e);
    } catch (final CertificateException e) {
      throw new KeyStoreException(e);
    } catch (final FileNotFoundException e) {
      throw new KeyStoreException(e);
    } catch (final IOException e) {
      throw new KeyStoreException(e);
    }
  }


}
