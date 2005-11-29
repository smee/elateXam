package de.elatexam.logserver;
/*

Copyright (C) 2010 Steffen Dienst

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

import java.net.ConnectException;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.net.SocketHubAppender;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Simple LogServer that knows how to connect to a running application with a configured {@link SocketHubAppender}
 * instance. Use this class to store logs in a distributed fashion.
 * 
 * @author Steffen Dienst
 * 
 */
public class SimpleLogClient {

  static Logger log = Logger.getLogger(SimpleLogClient.class);
  final static Object lock = new Object();
  final String host;
  final int port;

  public SimpleLogClient(final String host, final int port, final String configFile) {
    this.host = host;
    this.port = port;

    if (configFile.endsWith(".xml")) {
      DOMConfigurator.configure(configFile);
    } else {
      PropertyConfigurator.configure(configFile);
    }
  }

  public static void main(final String argv[]) {
    if (argv.length != 3) {
      System.err.println(
          "Usage: java " + SimpleLogClient.class.getName() + " hostname port log4jConfigFile");
    }
    int port = -1;
    try {
      port = Integer.parseInt(argv[1]);
    } catch (final NumberFormatException e) {
      System.err.println("Port must be a numeric value!");
      System.exit(1);
    }
    new SimpleLogClient(argv[0], port, argv[2]).runNextConnectionAttempt();

  }

  /**
   * Keep trying to connect to the given host every 30 seconds. Reconnect on socket failures as soon as possible.
   * 
   * @param host
   * @param port
   */
  protected void runNextConnectionAttempt() {
    while (true) {
      // keep trying to connect to the log4j SocketHupAppender
      try {
        log.info(String.format("Attempting to connect to %s:%d", host, port));
        final Socket socket = new Socket(host, port);
        socket.setKeepAlive(true);
        log.info("Connected to server at " + socket.getInetAddress());
        new Thread(new SocketNode(socket, LogManager.getLoggerRepository()) {
          @Override
          protected void onClose() {
            synchronized (lock) {
              lock.notifyAll();
            }
          }
        }).start();
        // block this thread until this socket gets closed
        synchronized (lock) {
          lock.wait();
        }
      } catch (final ConnectException e) {
        sleep();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * Sleep for 30sec.
   */
  private static void sleep() {
    try {
      Thread.currentThread().sleep(30000);
    } catch (final InterruptedException e) {
    }

  }

}
