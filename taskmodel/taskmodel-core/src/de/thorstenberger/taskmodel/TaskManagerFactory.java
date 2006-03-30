/*

Copyright (C) 2005 Thorsten Berger

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
package de.thorstenberger.taskmodel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;


/**
 * 
 * Spring component factory.
 * 
 * @author Thorsten Berger
 * @deprecated ... conceptionally wrong
 */
public class TaskManagerFactory {

	private TaskManager taskManager;
	private XmlBeanFactory beanFactory;
	
	/**
	 * Tries to find the spring configuration "taskAPISpring.xml" in the classpath.
	 */
	public TaskManagerFactory() throws IOException{
		
		beanFactory = new XmlBeanFactory( new ClassPathResource( "taskAPISpring.xml" ) );
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.postProcessBeanFactory( beanFactory );
		
	}
	
	public TaskManagerFactory( FileInputStream fis ) throws IOException{
		
		BufferedInputStream bis = new BufferedInputStream( fis );
		beanFactory = new XmlBeanFactory( 
				new InputStreamResource( bis ) );
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.postProcessBeanFactory( beanFactory );
		
		bis.close();
		
	}
	
	public TaskManager getTaskManager(){
		if( taskManager == null )
			taskManager = (TaskManager) beanFactory.getBean( "TaskManager" );
		return taskManager;
	}

}
