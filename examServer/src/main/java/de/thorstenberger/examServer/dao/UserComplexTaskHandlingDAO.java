/**
 * 
 */
package de.thorstenberger.examServer.dao;

import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;

/**
 * Provides {@link ComplexTaskHandlingRoot} for specific users.
 * 
 * @author Steffen Dienst
 *
 */
public interface UserComplexTaskHandlingDAO {
	/**
	 * Load the task handling informations for the given user.
	 * @param username
	 * @param complexTaskDefRoot the correspongding task definition
	 * @return
	 */
	public ComplexTaskHandlingRoot load( String username, String taskId, ComplexTaskDefRoot complexTaskDefRoot );
	
	/**
	 * Store the task handling informations for the given user.
	 * @param username
	 * @param complexTaskHandlingRoot
	 */
	public void save( String username,String taskId, ComplexTaskHandlingRoot complexTaskHandlingRoot );

}
