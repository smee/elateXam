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
package de.thorstenberger.examServer.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import de.thorstenberger.examServer.service.ExamServerManager;

/**
 * @author Thorsten Berger
 *
 */
public class ExamServerManagerImpl implements ExamServerManager, ApplicationContextAware {

  static final String HOME = "home";
  static final String TASKDEFS = "taskdefs";
  static final String SYSTEM = "system";

  private ApplicationContext ac;
  private File repositoryFile;

  /**
   *
   */
  public ExamServerManagerImpl() {
    super();
    // TODO Auto-generated constructor stub
  }

    private void configLogging(final InputStream inputStream) {

    final Properties p = new Properties();
    try {
            p.load(inputStream);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    p.setProperty("examServer.repository.path", repositoryFile.getAbsolutePath());
    PropertyConfigurator.configure(p);

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.service.ExamServerManager#getHomeDir()
   */
  public File getHomeDir() {
    return new File(repositoryFile, HOME);
  }

  /**
   * @return Returns the repositoryFile.
   */
  public File getRepositoryFile() {
    return repositoryFile;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.service.ExamServerManager#getSystemDir()
   */
  public File getSystemDir() {
    return new File(repositoryFile, SYSTEM);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.service.ExamServerManager#getTaskDefDir()
   */
  public File getTaskDefDir() {
    return new File(repositoryFile, TASKDEFS);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.service.ExamServerManager#init()
   */
  public void init() {

    String contextName = null;
    ServletContext sc = null;

    if (ac instanceof WebApplicationContext) {
      sc = ((WebApplicationContext) ac).getServletContext();
      final String path = sc.getRealPath("");
      int pos = path.lastIndexOf('\\');
      if (pos != -1) {
        contextName = path.substring(pos + 1, path.length());
      } else {
        pos = path.lastIndexOf('/');
        if (pos != -1) {
          contextName = path.substring(pos + 1, path.length());
        }
      }
    }

    final String repositoryPath = System.getProperty("user.home") + File.separatorChar +
    "ExamServerRepository" + (contextName != null ? "_" + contextName : "");

    repositoryFile = new File(repositoryPath);

    // create if not exists
    if (!repositoryFile.exists()) {
      repositoryFile.mkdirs();
    }

    // system dir
    final File system = new File(repositoryPath + File.separatorChar + SYSTEM);
    if (!system.exists()) {
      system.mkdirs();
    }

    // homes dir
    final File home = new File(repositoryPath + File.separatorChar + HOME);
    if (!home.exists()) {
      home.mkdirs();
    }

    // taskDefs dir
    final File taskDefs = new File(repositoryPath + File.separatorChar + TASKDEFS);
    if (!taskDefs.exists()) {
      taskDefs.mkdirs();
    }

    if (sc != null) {
            configLogging(this.getClass().getClassLoader().getResourceAsStream("examserverLog4j.properties"));
    }

  }

  /*
   * (non-Javadoc)
   *
   * @seeorg.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
   * ApplicationContext)
   */
  public void setApplicationContext(final ApplicationContext ac) throws BeansException {
    this.ac = ac;
  }

}
