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
package de.thorstenberger.examServer.webapp.form;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.thorstenberger.examServer.pdf.signature.SignatureInfos;

/**
 * @author Thorsten Berger
 *
 */
public class SystemConfigForm extends BaseForm {

	private String title;
	private String remoteUserManagerURL;
	private boolean loadJVMOnStartup;

	private String httpAuthURL;
	private String httpAuthMail;
	private String radiusHost;
	private String radiusSharedSecret;
  private SignatureInfos si = new SignatureInfos();
  private List<String> mailSuffixes;

  /**
   * @return the mailSuffixes
   */
  public List<String> getRadiusMailSuffixes() {
    return mailSuffixes;
  }

  /**
   * Same contents as {@link #getRadiusMailSuffixes()} but as a single space delimited string.
   * 
   * @return
   */
  public String getRadiusMailSuffixesDelimited() {
    return StringUtils.join(mailSuffixes, " ");
  }

  /**
   * Split the string among whitespaces, add them to the {@link #getRadiusMailSuffixes()} list.
   *
   * @param s
   */
  public void setRadiusMailSuffixesDelimited(String s) {
    this.mailSuffixes = Arrays.asList(s.split("\\s+"));
  }

  /**
   * @param mailSuffixes
   *          the mailSuffixes to set
   */
  public void setRadiusMailSuffixes(List<String> mailSuffixes) {
    this.mailSuffixes = mailSuffixes;
  }

  private String todo;

	/**
   * @return name of the submit button pressed
   */
  public String getTodo() {
    return todo;
  }

  /**
   * @param todo
   *          name of the submit button pressed
   */
  public void setTodo(final String todo) {
    this.todo = todo;
  }

  /**
   * @return the radiusHost
   */
  public String getRadiusHost() {
    return radiusHost;
  }
  /**
   * @param radiusHost the radiusHost to set
   */
  public void setRadiusHost(final String radiusHost) {
    this.radiusHost = radiusHost;
  }
  /**
   * @return the radiusSharedSecret
   */
  public String getRadiusSharedSecret() {
    return radiusSharedSecret;
  }
  /**
   * @param radiusSharedSecret the radiusSharedSecret to set
   */
  public void setRadiusSharedSecret(final String radiusSharedSecret) {
    this.radiusSharedSecret = radiusSharedSecret;
  }
  /**
	 * @return Returns the loadJVMOnStartup.
	 */
	public boolean isLoadJVMOnStartup() {
		return loadJVMOnStartup;
	}
	/**
	 * @param loadJVMOnStartup The loadJVMOnStartup to set.
	 */
	public void setLoadJVMOnStartup(final boolean loadJVMOnStartup) {
		this.loadJVMOnStartup = loadJVMOnStartup;
	}
	/**
	 * @return Returns the remoteUserManagerURL.
	 */
	public String getRemoteUserManagerURL() {
		return remoteUserManagerURL;
	}
	/**
	 * @param remoteUserManagerURL The remoteUserManagerURL to set.
	 */
	public void setRemoteUserManagerURL(final String remoteUserManagerURL) {
		this.remoteUserManagerURL = remoteUserManagerURL;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
	/**
	 * @return the httpAuthURL
	 */
	public String getHttpAuthURL() {
		return httpAuthURL;
	}
	/**
	 * @param httpAuthURL the httpAuthURL to set
	 */
	public void setHttpAuthURL(final String httpAuthURL) {
		this.httpAuthURL = httpAuthURL;
	}
	/**
	 * @return the httpAuthMail
	 */
	public String getHttpAuthMail() {
		return httpAuthMail;
	}
	/**
	 * @param httpAuthMail the httpAuthMail to set
	 */
	public void setHttpAuthMail(final String httpAuthMail) {
		this.httpAuthMail = httpAuthMail;
	}

  /**
   * @param si
   *          the si to set
   */
  public void setSignatureSettings(final SignatureInfos si) {
    this.si = si;
  }

  /**
   * @return the si
   */
  public SignatureInfos getSignatureSettings() {
    return si;
  }



}
