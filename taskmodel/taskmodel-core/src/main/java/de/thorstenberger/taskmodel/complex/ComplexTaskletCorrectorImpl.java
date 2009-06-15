/*

Copyright (C) 2007 Thorsten Berger

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
package de.thorstenberger.taskmodel.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionModeType;
import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefRootImpl.CorrectOnlyProcessedTasksCorrectionMode;
import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefRootImpl.MultipleCorrectorsCorrectionMode;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskletCorrectorImpl implements ComplexTaskletCorrector {

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.ComplexTaskletCorrector#doCorrection(de.thorstenberger.taskmodel.complex.complextaskhandling.Try, de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot.CorrectionModeType, boolean)
	 */
	public Result doCorrection(ComplexTasklet ct, Try theTry, CorrectionModeType mode, boolean justCountPoints) {

		List<de.thorstenberger.taskmodel.complex.complextaskhandling.Page> pages = theTry.getPages();
		Result r;
		if( mode == ComplexTaskDefRoot.CorrectionModeType.CORRECTONLYPROCESSEDTASKS ){
			// we assert n>=1
			int n = ((CorrectOnlyProcessedTasksCorrectionMode)ct.getComplexTaskDefRoot().getCorrectionMode()).getFirst_n_tasks();
			r = doCorrectOnlyProcessedTasksCorrection( pages, n, false );
		}else if( mode == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
			// we assert n>=2  (otherwise, its nearly the regular type)
			int n = ((MultipleCorrectorsCorrectionMode)ct.getComplexTaskDefRoot().getCorrectionMode()).getCorrectors();
			r = doMultipleCorrectorsCorrection( pages, n, false );
		}
		else
			r = doRegularCorrection(pages, false);

		return r;

	}
	
	
	private Result doRegularCorrection(List<de.thorstenberger.taskmodel.complex.complextaskhandling.Page> pages, boolean justCountPoints) {
		boolean allCorrected = true;
		boolean allAutoCorrected = true;
		float points = 0;
		List<String> correctorsFound = new ArrayList<String>();
		
		// regular correction
		for( de.thorstenberger.taskmodel.complex.complextaskhandling.Page page : pages ){
			
			List<SubTasklet> subTasklets = page.getSubTasklets();
			
			for( SubTasklet subTasklet : subTasklets ){
		
				if( !justCountPoints )
					subTasklet.doAutoCorrection();
				
				if( !subTasklet.isCorrected() ){
					allCorrected = false;
					allAutoCorrected = false;
				}else{
					if( subTasklet.isAutoCorrected() ){
						points += subTasklet.getAutoCorrection().getPoints();
					}else{
						points += subTasklet.getManualCorrections().get( 0 ).getPoints();
						if( !correctorsFound.contains( subTasklet.getManualCorrections().get( 0 ).getCorrector() ) )
							correctorsFound.add( subTasklet.getManualCorrections().get( 0 ).getCorrector() );
						allAutoCorrected = false;
					}
				}
				
			}
		}
		
		if( allCorrected ){
			if( allAutoCorrected )
				return new ResultImpl( points, true );
			else{
				ResultImpl ret = new ResultImpl( null, true );
				String correctors = "";
				for( String c : correctorsFound ){
					correctors += c;
					if( correctorsFound.indexOf( c ) != correctorsFound.size() - 1 )
						correctors += ",";
				}
				ret.addManualCorrection( correctors.length() == 0 ? null : correctors, points );
				return ret;
			}
		}else
			return new ResultImpl( null, false );
		
	}
	
	/**
	 * @param allCorrected
	 * @param autoCorrectionPoints
	 * @param pages
	 */
	private Result doCorrectOnlyProcessedTasksCorrection(List<de.thorstenberger.taskmodel.complex.complextaskhandling.Page> pages, int n, boolean justCountPoints) {
		boolean allCorrected = true;
		boolean allAutoCorrected = true;
		
		float points = 0;
		List<String> correctorsFound = new ArrayList<String>();
		boolean mybreak = false;

		for( de.thorstenberger.taskmodel.complex.complextaskhandling.Page page : pages ){
			
			List<SubTasklet> subTasklets = page.getSubTasklets();
			
			for( SubTasklet subTasklet : subTasklets ){
				
				if( subTasklet.isProcessed() ){
					
					if( !justCountPoints )
						subTasklet.doAutoCorrection();
					
					if( !subTasklet.isCorrected() ){	// equivalent to !isNeedsManualCorrection
						allCorrected = false;
						allAutoCorrected = false;
					}else{
						if( subTasklet.isAutoCorrected() ){
							points += subTasklet.getAutoCorrection().getPoints();
						}else{
							points += subTasklet.getManualCorrections().get( 0 ).getPoints();
							if( !correctorsFound.contains( subTasklet.getManualCorrections().get( 0 ).getCorrector() ) )
								correctorsFound.add( subTasklet.getManualCorrections().get( 0 ).getCorrector() );
							allAutoCorrected = false;
						}
						
					}
					
					n--;
					
				}
				
				if( n == 0 ){
					mybreak = true;
					break;
				}
				
			}
			if( mybreak )
				break;
		}
		
		if( allCorrected ){
			if( allAutoCorrected )
				return new ResultImpl( points, true );
			else{
				ResultImpl ret = new ResultImpl( null, true );
				String correctors = "";
				for( String c : correctorsFound ){
					correctors += c;
					if( correctorsFound.indexOf( c ) != correctorsFound.size() - 1 )
						correctors += ",";
				}
				ret.addManualCorrection( correctors.length() == 0 ? null : correctors, points );
				return ret;
			}
		}else
			return new ResultImpl( null, false );
		
	}
	
	private Result doMultipleCorrectorsCorrection( List<de.thorstenberger.taskmodel.complex.complextaskhandling.Page> pages, int correctorsCount, boolean justCountPoints ){
		boolean allCorrected = true;
		boolean allAutoCorrected = true;
		
		float autoCorrectedPoints = 0;
		Map<String, Float> manualPoints = new HashMap<String, Float>();
		
		List<SubTasklet> manualSubTasklets = new LinkedList<SubTasklet>();
		Map<String, List<SubTasklet>> correctors = new HashMap<String, List<SubTasklet>>();
		List<String> correctorOrder = new LinkedList<String>();
//		Map<String, Map<Integer, CorrectorIndexCount>> indexCount = new HashMap<String, Map<Integer,CorrectorIndexCount>>();
		
		
		for( de.thorstenberger.taskmodel.complex.complextaskhandling.Page page : pages ){
			
			List<SubTasklet> subTasklets = page.getSubTasklets();
			for( SubTasklet subTasklet : subTasklets ){
				
				if( !justCountPoints )
					subTasklet.doAutoCorrection();
				
				if( !subTasklet.isCorrected() ){ // neither an auto correction nor at least one manual correction
					allCorrected = false;
					allAutoCorrected = false;
				}else{
					if( subTasklet.isAutoCorrected() ){
						autoCorrectedPoints += subTasklet.getAutoCorrection().getPoints();
					}else{
						// we need to build a list with all SubTasklets that need a manual correction, to determine wether they all
						// have been corrected by "correctorsCount" correctors.
						manualSubTasklets.add( subTasklet );
						
						List<ManualSubTaskletCorrection> manualCorrections = subTasklet.getManualCorrections();
						int i = 0;
						for( ManualSubTaskletCorrection msc : manualCorrections ){
							increasePointsForCorrector(manualPoints, msc.getCorrector(), msc.getPoints() );
							addSubTaskletOfCorrector(correctors, msc.getCorrector(), subTasklet );
							if( !correctorOrder.contains( msc.getCorrector() ) )
								correctorOrder.add( msc.getCorrector() );
//							increaseIndexCount( indexCount, msc.getCorrector(), i++ );
						}
						
						allAutoCorrected = false;
					}
				}
				
			}
		}
		
		// first case: simple
		if( !allCorrected )
			return new ResultImpl( null, false );
		
		// second case: all SubTasklets have been corrected automatically
		if( allCorrected && allAutoCorrected ){
			return new ResultImpl( autoCorrectedPoints, true );
		}
		
		// third case: we have manual corrections....
		// for every corrector we need to check wether he has corrected all SubTasklets that needed manual corrections and
		// determine the autoCorrectionPoints of the Tasklet associated to him (add his autoCorrectionPoints and the automatically calculated autoCorrectionPoints).
			
		//
		// problem no. 1: get the first "correctorsCount" correctors that have corrected all (manual) SubTasklets
		for( String corrector : correctors.keySet() ){
			List<SubTasklet> subTaskletsOfCorrector = correctors.get( corrector );
			if( subTaskletsOfCorrector.size() != manualSubTasklets.size() ){
				correctorOrder.remove( corrector );
			}
		}
		
		// if we did not find at least one corrector who has corrected all SubTasklets that needed manual correction:
		if( correctorOrder.size() <= 0 )
			return null;
		
		
//		//
//		// problem no. 2: determine the order of the correctors
//		List<String> toRemove = new LinkedList<String>();
//		for( String c : indexCount.keySet() ){
//			if( !realCorrectors.contains( c ) )
//				toRemove.add( c );
//		}
//		// eliminate the indexCount of correctors that did not correct all required Tasklets
//		for( String c : toRemove )
//			indexCount.remove( c );
//		// mix all indexes into one list such that we can sort it by  1st:index, 2nd: count, 3rd: corrector
//		List<CorrectorIndexCount> allIndexes = new ArrayList<CorrectorIndexCount>();
//		for( String c : indexCount.keySet() ){
//			Map<Integer, CorrectorIndexCount> ic = indexCount.get( c );
//			for( Integer integer : ic.keySet() )
//				allIndexes.add( ic.get( integer ) );
//		}
//		// sort by index, count and corrector (in this priority)
//		Collections.sort( allIndexes );
//		String[] correctorOrder = new String[ realCorrectors.size() ];
//		for( int j = 0; j<realCorrectors.size(); j++ ){
//			CorrectorIndexCount cic = allIndexes.get( 0 );
//			correctorOrder[ j ] = cic.getCorrector();
//			
//			// remove the index and the corrector from the list
//			List<CorrectorIndexCount> toRemove2 = new LinkedList<CorrectorIndexCount>();
//			for( CorrectorIndexCount c : allIndexes )
//				if( c.getCorrector().equals( cic.getCorrector() ) || c.getIndex() == cic.getIndex() )
//					toRemove2.add( c );
//			for( CorrectorIndexCount c : toRemove2 )
//				allIndexes.remove( c );
//			
//		}
//		// ok, so now we have the order of the correctors in correctOrder[]

		// instantiate the result (autoCorrectionPoints: null)
		ResultImpl result = new ResultImpl( null, false );
		
		// calculate the overall points and return the result
		for( int k = 0; k < correctorOrder.size(); k++ ){
			if( k >= correctorsCount )
				break;
			String corrector = correctorOrder.get( k );
			float pointsOfCorrector = manualPoints.get( corrector ) + autoCorrectedPoints;
			result.addManualCorrection( corrector, pointsOfCorrector );
		}
		
		// so, if we have enough correctors, declare the Tasklet for completely solved
		if( result.getManualCorrections().size() >= correctorsCount ){
			result.setCorrected( true );
		}
		
		return result;
	}

	private void increasePointsForCorrector( Map<String, Float> manualPoints, String corrector, float points ){
		Float p = manualPoints.get( corrector );
		if( p == null )
			p = new Float( 0 );
		manualPoints.put( corrector, p.floatValue() + points );
	}
	
	private void increaseIndexCount( Map<String, Map<Integer, CorrectorIndexCount>> correctorIndexCount, String corrector, int index ){
		Map<Integer, CorrectorIndexCount> indexCountMap = correctorIndexCount.get( corrector );
		if( indexCountMap == null )
			indexCountMap = new HashMap<Integer, CorrectorIndexCount>();
		CorrectorIndexCount cic = indexCountMap.get( index );
		if( cic == null )
			cic = new CorrectorIndexCount( corrector, index, 0 );
		cic.setCount( cic.getCount() + 1 );
		indexCountMap.put( index, cic );
		correctorIndexCount.put( corrector, indexCountMap );
	}
	
	private void addSubTaskletOfCorrector( Map<String, List<SubTasklet>> correctors, String corrector, SubTasklet subTasklet ){
		List<SubTasklet> subTasklets = correctors.get( corrector );
		if( subTasklets == null )
			subTasklets = new LinkedList<SubTasklet>();
		subTasklets.add( subTasklet );
		correctors.put( corrector, subTasklets );
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
	
	public static class ResultImpl implements Result{
		
		private Float autoCorrectionPoints;
		private boolean corrected;
		private List<ManualCorrection> manualCorrections;
		
		/**
		 * @param autoCorrectionPoints
		 * @param autoCorrected
		 */
		public ResultImpl(Float autoCorrectionPoints, boolean corrected ) {
			super();
			this.autoCorrectionPoints = autoCorrectionPoints;
			this.corrected = corrected;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.ComplexTaskletCorrector.Result#isCorrected()
		 */
		public boolean isCorrected() {
			return corrected;
		}
		/**
		 * @param corrected the corrected to set
		 */
		public void setCorrected(boolean corrected) {
			this.corrected = corrected;
		}
		/**
		 * @return the autoCorrectionPoints
		 */
		public Float getAutoCorrectionPoints() {
			return autoCorrectionPoints;
		}
		/**
		 * @param autoCorrectionPoints the autoCorrectionPoints to set
		 */
		public void setAutoCorrectionPoints(Float points) {
			this.autoCorrectionPoints = points;
		}
		
		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.ComplexTaskletCorrector.Result#getManualCorrections()
		 */
		public List<ManualCorrection> getManualCorrections() {
			return manualCorrections;
		}

		public void addManualCorrection( String corrector, Float points ){
			if( manualCorrections == null )
				manualCorrections = new LinkedList<ManualCorrection>();
			manualCorrections.add( new ManualCorrectionImpl( corrector, points ) );
		}
		
		public class ManualCorrectionImpl implements ManualCorrection{
			
			private String corrector;
			private float points;
			
			/**
			 * @param corrector
			 * @param points
			 */
			public ManualCorrectionImpl(String corrector, float points) {
				this.corrector = corrector;
				this.points = points;
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
			 * @return the points
			 */
			public float getPoints() {
				return points;
			}
			/**
			 * @param points the points to set
			 */
			public void setPoints(float points) {
				this.points = points;
			}
			
		}
		
//		public void addPointsForCorrector( String corrector, float autoCorrectionPoints ){
//			if( manualCorrections == null )
//				manualCorrections = new HashMap<String, Float>();
//			
//			Float p = manualCorrections.get( corrector );
//			if( p == null )
//				p = new Float( 0 );
//			p += autoCorrectionPoints;
//			manualCorrections.put( corrector, p );
//		}
		
	}

}
