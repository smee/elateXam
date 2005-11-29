package de.elatexam.logserver;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;


// Contributors:  Moses Hohman <mmhohman@rainbow.uchicago.edu>

/**
 * Adapted from org.apache.log4j.net.SocketNode13 of the apache-log4j-receivers project.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author Paul Smith (psmith@apache.org)
 */
public class SocketNode implements Runnable {
  private static Logger log = Logger.getLogger(SocketNode.class);

  /**
   * Socket.
   */
  private final Socket socket;

  private final LoggerRepository loggerRepository;




  /**
   * Constructor for socket and logger repository.
   * 
   * @param s
   *          socket
   * @param loggerRepository
   * @param hierarchy
   *          logger repository
   */
  public SocketNode(final Socket s, final LoggerRepository loggerRepository) {
    this.socket = s;
    this.loggerRepository = loggerRepository;
  }


  /**
   * Deserialize events from socket until interrupted.
   */
  public void run() {
    LoggingEvent event;
    ObjectInputStream ois = null;

    try {
      ois =
        new ObjectInputStream(
            new BufferedInputStream(socket.getInputStream()));
    } catch (final Exception e) {
      ois = null;
      log.error("Exception opening ObjectInputStream to " + socket, e);
    }

    if (ois != null) {

      final String hostName = socket.getInetAddress().getHostName();
      final String remoteInfo = hostName + ":" + socket.getPort();


      try {
        while (!socket.isClosed()) {
          // read an event from the wire
          event = (LoggingEvent) ois.readObject();
          // store the known remote info in an event property
          event.setProperty("log4j.remoteSourceInfo", remoteInfo);

          // if configured with a receiver, tell it to post the event
          // get a logger from the hierarchy. The name of the logger
          // is taken to be the name contained in the event.
          final Logger remoteLogger = loggerRepository.getLogger(event.getLoggerName());

          // apply the logger-level filter
          if (event.getLevel().isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
            // finally log the event as if was generated locally
            remoteLogger.callAppenders(event);
          }
        }
      } catch (final java.io.EOFException e) {
        log.info("Caught java.io.EOFException closing connection.");
      } catch (final java.net.SocketException e) {
        log.info("Caught java.net.SocketException closing connection.");
      } catch (final IOException e) {
        log.info("Caught java.io.IOException: " + e);
        log.info("Closing connection.");
      } catch (final Exception e) {
        log.error("Unexpected exception. Closing connection.", e);
      }
    }

    // close the socket
    try {
      if (ois != null) {
        ois.close();
      }
    } catch (final Exception e) {
      log.info("Could not close connection.", e);
    }
    onClose();
  }


  /**
   * Overwrite this method to be informed as soon as the socket got closed.
   */
  protected void onClose() {
    // nothing to do.

  }

}
