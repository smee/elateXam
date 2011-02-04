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
package de.thorstenberger.taskmodel.impl;

import java.util.ArrayList;
import java.util.List;

import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.Tasklet;

/**
 * @author Thorsten Berger
 *
 */
public abstract class AbstractTaskFactory implements TaskFactory {


	/**
	 * generic implementation, overwrite to improve performance
	 */
	public List<String> getUserIdsOfTaskletsAssignedToCorrector(long taskId,
			String correctorId ) {

		List<Tasklet> tasklets = getTasklets( taskId );
		List<String> ret = new ArrayList<String>();

		for( Tasklet tasklet : tasklets ){

			if( tasklet.getStatus() != Tasklet.Status.INITIALIZED && correctorId.equals( tasklet.getTaskletCorrection().getCorrector() ) ) {
        ret.add( tasklet.getUserId() );
      }

		}

		return ret;

	}

	/**
	 * generic implementation, overwrite to improve performance
	 */
	public List<String> getUserIdsOfAvailableTasklets(long taskId) {
		List<Tasklet> tasklets = getTasklets( taskId );
		List<String> ret = new ArrayList<String>();
		for( Tasklet tasklet : tasklets ) {
      ret.add( tasklet.getUserId() );
    }
		return ret;
	}

  /*
   * (non-Javadoc)
   * 
   * @see de.thorstenberger.taskmodel.TaskFactory#reset()
   */
  public void reset() {
  };


}
