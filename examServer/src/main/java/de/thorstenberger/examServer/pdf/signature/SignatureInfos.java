/*

Copyright (C) 2009 Steffen Dienst

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

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
  /**
   * Keys for infos to fetch via {@link SignatureInfos#get(Attr)}.
   *
   */
  private enum Attr {
    keystoreFile,
    keystorePassword, privateKeyPassword,
    keyAlias,
    certificateChainAlias,
    timestampServerUrl,
    signatureReason, signatureLocation, signatureContact}

  private final Properties prop;

  /**
   * Load SignatureInfos from a properties file.
   *
   * @param propertyFile
   * @throws FileNotFoundException
   * @throws IOException
   */
  public SignatureInfos(final File propertyFile) throws FileNotFoundException, IOException {
    this(new Properties());
    prop.load(new FileInputStream(propertyFile));
  }

  private SignatureInfos(final Properties prop2) {
    this.prop = prop2;
  }

  /**
   * Load default settings from classpath file 'signature.properties'.
   */
  public SignatureInfos() {
    this.prop = getDefaultSettings().prop;

    final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("signature.properties");
    if (inputStream != null) {
      try {
        prop.load(inputStream);
        inputStream.close();
      } catch (final IOException e) {
        log.warn("Could not load default settings for pdf signatures from classpath!", e);
      }
    }
  }

  /**
   * Default settings. Assumes an existing default keystore file at the user's home with no password and an alias of
   * 'key' as well as a certificate chain alias 'certificate'.
   */
  public static SignatureInfos getDefaultSettings() {
    final Properties prop = new Properties();
    prop.setProperty(Attr.keystoreFile.name(), System.getProperty("user.home") + File.separatorChar + ".keystore");
    prop.setProperty(Attr.keystorePassword.name(), "tomcat");
    prop.setProperty(Attr.privateKeyPassword.name(), "tomcat");
    prop.setProperty(Attr.keyAlias.name(), "tomcat");
    prop.setProperty(Attr.certificateChainAlias.name(), "tomcat");
    prop.setProperty(Attr.timestampServerUrl.name(), "http://zeitstempel.dfn.de");
    prop.setProperty(Attr.signatureReason.name(), "unknown reason");
    prop.setProperty(Attr.signatureLocation.name(), "unknown place");
    prop.setProperty(Attr.signatureContact.name(), "unknown@unknown.org");
    return new SignatureInfos(prop);
  }

  /**
   * Lookup property.
   *
   * @param key
   * @return
   */
  private String get(final Attr key) {
    return prop.getProperty(key.name());
  }

  /**
   * @return
   * @throws KeyStoreException
   */
  public PrivateKey getPrivateKey() throws KeyStoreException {
    try {

      return (PrivateKey) getKeystore().getKey(get(Attr.keyAlias), get(Attr.privateKeyPassword).toCharArray());

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
    final Certificate[] chain = getKeystore().getCertificateChain(get(Attr.certificateChainAlias));

    final X509Certificate[] xchain = new X509Certificate[chain.length];
    System.arraycopy(chain, 0, xchain, 0, chain.length);

    return xchain;
  }

  private KeyStore getKeystore() throws KeyStoreException {
    try {
      final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(new FileInputStream(get(Attr.keystoreFile)), get(Attr.keystorePassword).toCharArray());
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

  /**
   * @param file
   * @throws IOException
   */
  public void save(final File file) throws IOException {
    prop.store(new FileOutputStream(file), "");

  }

  /**
   * @return
   */
  public String getTimeStampServerUrl() {
    return get(Attr.timestampServerUrl);
  }

  public String getSignatureContact() {
    return get(Attr.signatureContact);
  }

  public String getSignatureLocation() {
    return get(Attr.signatureLocation);
  }

  public String getSignatureReason() {
    return get(Attr.signatureReason);
  }
}
