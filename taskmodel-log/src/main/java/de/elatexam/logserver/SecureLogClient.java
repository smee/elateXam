package de.elatexam.logserver;
import java.io.Console;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/*

Copyright (C) 2009 Steffen Dienst

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
 * @author Steffen Dienst
 *
 */
public class SecureLogClient {

  /**
   * @param args
   * @throws JSchException
   */
  public static void main(final String[] args) throws JSchException {
    final Console console = System.console();
    final String hostname = console.readLine("Servername   : ");
    final String username = console.readLine("Benutzername : ");
    final String password = new String(console.readPassword("Passwort     : "));

    final JSch ssh = new JSch();
    // load known host keys
    ssh.setKnownHosts("known_hosts");

    final Session session = ssh.getSession(username, hostname);
    session.setPassword(password);
    // TODO make configurable
    final int port = 4560;
    session.setPortForwardingL(port, "localhost", port);
    session.connect();

    new SimpleLogClient("localhost", port, "log4j.properties").runNextConnectionAttempt();
  }

}
