/*

Copyright (C) 2006 Thorsten Berger

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
/**
 *
 */
package de.thorstenberger.examServer.service;

import de.thorstenberger.examServer.pdf.signature.SignatureInfos;


/**
 * @author Thorsten Berger
 *
 */
public interface ConfigManager {

    public String getHTTPAuthMail();

    public String getHTTPAuthURL();

    public String getRemoteUserManagerURL();

    public String getTitle();

    public boolean isLoadJVMOnStartup();

    public boolean isSetFlag(String flag);

    public boolean isStudentsLoginEnabled();

    public void setHTTPAuthMail(String address);

    public void setHTTPAuthURL(String url);

    public void setLoadJVMOnStartup(boolean value);

    public void setRemoteUserManagerURL(String url);

    public void setStudentsLoginEnabled(boolean value);

    public void setTitle(String title);

    public void toggleFlag(String flagName, boolean state);

  /**
   * Url for a Radius server to authenticate against (RFC 2865).
   *
   * @return
   */
    String getRadiusHost();
    void setRadiusHost(String host);

  /**
   * Shared secret for a Radius server to authenticate against (see RFC 2865).
   *
   * @return
   */
    String getRadiusSharedSecret();
    void setRadiusSharedSecret(String secret);

  /**
   * Settings for signing and timestamping PDF files.
   * 
   * @return
   */
  SignatureInfos getPDFSignatureInfos();

  void setPDFSignatureInfos(SignatureInfos si);
}
