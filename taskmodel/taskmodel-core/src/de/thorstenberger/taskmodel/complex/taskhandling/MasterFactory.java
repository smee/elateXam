/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex.taskhandling;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MasterFactory implements Serializable{

//	private static MasterFactory instance;
//	
//	private Map master;
//	
//	/**
//	 * 
//	 */
//	private MasterFactory() {
//		master = new HashMap();
//
//		synchronized( this ){
//			TaskDef[] tasks = DataAccessFactory.getInstance().getTaskManager().getTasks();
//			for( int i=0; i<tasks.length; i++ ){
//				if( tasks[i] instanceof TaskDef_Complex ){
//					if( ((TaskDef_Complex)tasks[i]).isGenerateMaster() ){
//						generateMaster( (TaskDef_Complex)tasks[i] );
//					}
//					
//				}
//			}
//		}
//	}
//
//	public static MasterFactory getInstance(){
//		if( instance == null )
//			instance = new MasterFactory();
//		return instance;
//	}
//	
//	public Master getClonedMaster( int id ){
//		synchronized(this){
//			return clone( master.get( new Integer( id ) ) );
//		}
//	}
//	
//    private Master clone (Object obj)
//    {
//        try
//        {
//            ByteArrayOutputStream out = new ByteArrayOutputStream ();
//            ObjectOutputStream oout = new ObjectOutputStream (out);
//            oout.writeObject (obj);
//            
//            ObjectInputStream in = new ObjectInputStream (
//                new ByteArrayInputStream (out.toByteArray ()));
//            return (Master) in.readObject ();
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException ("cannot clone class [" +
//                obj.getClass ().getName () + "] via serialization: " +
//                e.toString ());
//        }
//    }
//	
//	private void generateMaster( TaskDef_Complex complexTaskDef ){
//		SubTaskFactory subTaskFactory = new SubTaskFactory( complexTaskDef.getComplexTaskDataHandler() );
//		master.put( new Integer( complexTaskDef.getId()) ,
//				new Master( ComplexTaskHandler.generateNewTry( subTaskFactory, complexTaskDef.getComplexTaskDataHandler() ) ) );
//	}
	
	
	
	public class Master implements Serializable{
		
		private ComplexTaskHandlingType.TryType.PageType[] pages;

		
		public Master( ComplexTaskHandlingType.TryType.PageType[] pages ){
			this.pages = pages;
		}
		
		public ComplexTaskHandlingType.TryType.PageType[] getPages() {
			return pages;
		}
	}
}
