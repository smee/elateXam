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
package de.thorstenberger.taskmodel.view.webapp.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.taskmodel.TaskModelServices;
import de.thorstenberger.taskmodel.view.SubTaskViewFactoryImpl;

/**
 * @author Steffen Dienst
 * 
 */
public class InitViewFactoryListener implements ServletContextListener {
    private final Log log = LogFactory.getLog(InitViewFactoryListener.class);

    /*
     * (non-Javadoc)
     * 
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    public void contextDestroyed(final ServletContextEvent sce) {
        log.info("Removing default subtaskviewfactory");
        TaskModelServices.getInstance().setSubTaskViewFactory(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    public void contextInitialized(final ServletContextEvent sce) {
        log.info("Initializing default subtaskviewfactory");
        TaskModelServices.getInstance().setSubTaskViewFactory(new SubTaskViewFactoryImpl());

    }

}
