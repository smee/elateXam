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
package de.thorstenberger.taskmodel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public class TaskmodelUtil {

	public static Tasklet.Status getStatus( String status ){
		for( Tasklet.Status s : Tasklet.Status.values() ){
			if( s.getValue().equals( status ) )
				return s;
		}
		throw new TaskModelPersistenceException( "Invalid status value \"" + status + "\"!" );
	}
	
	/**
	 * Assume you have a list of CorrectorIndexCount and you want do determine an order of
	 * correctors that fits best. I.e. if Corrector c1 is most often at position (index) 1, he should also
	 * be in position one at the resulting String[].
	 *
	 */
	public static String[] determineOrder( List<CorrectorIndexCount> correctorIndexCounts, int correctors ){
		
		// sort by index, count and corrector (in this priority)
		Collections.sort( correctorIndexCounts );
		String[] correctorOrder = new String[ correctors ];
		for( int j = 0; j < correctors; j++ ){
			CorrectorIndexCount cic = correctorIndexCounts.get( 0 );
			correctorOrder[ j ] = cic.getCorrector();
			
			// remove the index and the corrector from the list
			List<CorrectorIndexCount> toRemove2 = new LinkedList<CorrectorIndexCount>();
			for( CorrectorIndexCount c : correctorIndexCounts )
				if( c.getCorrector().equals( cic.getCorrector() ) || c.getIndex() == cic.getIndex() )
					toRemove2.add( c );
			for( CorrectorIndexCount c : toRemove2 )
				correctorIndexCounts.remove( c );
			
		}
		
		return correctorOrder;
		
	}
	
	
	
	public static class CorrectorIndexCount implements Comparable<CorrectorIndexCount>{
		
		private String corrector;
		private int index;
		private int count;
		
		/**
		 * @param corrector
		 * @param index
		 * @param count
		 */
		public CorrectorIndexCount(String corrector, int index, int count) {
			super();
			this.corrector = corrector;
			this.index = index;
			this.count = count;
		}
		/**
		 * @return the corrector
		 */
		public String getCorrector() {
			return corrector;
		}
		/**
		 * @param corrector the corrector to set
		 */
		public void setCorrector(String corrector) {
			this.corrector = corrector;
		}
		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}
		/**
		 * @param count the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}
		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
		/**
		 * @param index the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(CorrectorIndexCount o) {
			if ( getIndex() < o.getIndex() )
				return -1;
			else if( getIndex() > o.getIndex() )
				return 1;
			else{
				if( getCount() < o.getCount() )
					return -1;
				else if( getCount() > o.getCount() )
					return 1;
				else{
					return getCorrector().compareTo( o.getCorrector() );
				}
			}
		}
		
		
		
	}
	
}
