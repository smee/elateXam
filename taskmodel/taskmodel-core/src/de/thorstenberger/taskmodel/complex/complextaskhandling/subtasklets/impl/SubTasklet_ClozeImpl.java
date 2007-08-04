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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.ClozeBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.subtaskdefs.impl.ClozeSubTaskDefImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.ClozeCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.PageImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.ClozeSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.ClozeTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.ClozeSubTask;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_ClozeImpl implements SubTasklet_Cloze {

	private Block block;
	private ClozeTaskBlock clozeTaskBlock;
	private ClozeSubTaskDef clozeSubTaskDef;
	private ClozeSubTask clozeSubTask;
	
	/**
	 * 
	 */
	public SubTasklet_ClozeImpl( Block block, ClozeSubTaskDefImpl clozeSubTaskDefImpl, ClozeSubTask clozeSubTask ) {
		this.block = block;
		this.clozeTaskBlock = ((ClozeBlockImpl)block).getClozeTaskBlock();
		this.clozeSubTaskDef = clozeSubTaskDefImpl.getClozeSubTaskDef();
		this.clozeSubTask = clozeSubTask;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getSubTaskDefId()
	 */
	public String getSubTaskDefId() {
		return clozeSubTaskDef.getId();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#addToPage(de.thorstenberger.taskmodel.complex.complextaskhandling.Page)
	 */
	public void addToPage(Page page) {
		PageImpl pageImpl = (PageImpl)page;
		pageImpl.getPageType().getMcSubTaskOrClozeSubTaskOrTextSubTask().add( clozeSubTask );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#setVirtualSubtaskNumber(java.lang.String)
	 */
	public void setVirtualSubtaskNumber(String number) {
		clozeSubTask.setVirtualNum( number );
	}


	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#getVirtualSubtaskNumber()
	 */
	public String getVirtualSubtaskNumber() {
		return clozeSubTask.getVirtualNum();
	}
	
	public float getReachablePoints(){
		return clozeTaskBlock.getConfig().getPointsPerTask();
	}
	
	public String getProblem(){
		return clozeSubTaskDef.getProblem() == null ? "" : clozeSubTaskDef.getProblem();
	}
	
	public String getHint(){
		return clozeSubTaskDef.getHint();
	}
	
	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( clozeSubTask.getRefId() );
		List gaps = clozeSubTask.getGap();
		ret.append( gaps.size() );
		for( int i=0; i<gaps.size(); i++ ){
			ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType gap =
				(ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType) gaps.get( i );
			ret.append( gap.getGapValue() );
		}
		
		return ret.toString().hashCode();
	}
	
	public void doSave( SubmitData submitData ) throws IllegalStateException{
		ClozeSubmitData csd = (ClozeSubmitData) submitData;
		Gap[] gaps = getGaps();
		for( int i=0; i<gaps.length; i++ ){
			gaps[i].setGapValue( csd.getGapValue( i ) );
		}
		
	}
	
	public void doAutoCorrection(){
		
		Gap[] gaps = getGaps();
		float points = clozeTaskBlock.getConfig().getPointsPerTask();
		float negativePoints = clozeTaskBlock.getClozeConfig().getNegativePoints();
		boolean noneProcessed = true;
		boolean couldCorrectAllGaps = true;
		
		for( int i=0; i<gaps.length; i++ ){
			
			if( gaps[i].getGapValue() == null || isEmptyString( gaps[i].getGapValue() ) ){
				gaps[i].setAutoCorrection( false );
				points -= negativePoints;
			}else if( gaps[i].valueEqualsCorrectContent() ){
				gaps[i].setAutoCorrection( true );
				noneProcessed = false;
			}else{
				couldCorrectAllGaps = false;
				noneProcessed = false;
			}
			
		}
		
		if( noneProcessed ){
			setCorrection( 0 );
			return;
		}
		
		if( couldCorrectAllGaps ){
			if( points < 0 )
				points = 0;
			setCorrection( points );
		}else
			clozeSubTask.setNeedsManualCorrection( true );
		
	}
	
	
		
	public void doManualCorrection( CorrectionSubmitData csd ){
	    
	    ClozeCorrectionSubmitData ccsd = (ClozeCorrectionSubmitData) csd;
	    
	    Gap[] gaps = getGaps();
	    for( int i=0; i<gaps.length; i++ ){
	        if( !gaps[i].isAutoCorrected() )
	            gaps[i].setManualCorrection( ccsd.isCorrect( i ) );
	    }
	    
	    calculatePoints();
	    clozeSubTask.setNeedsManualCorrection( false );
	}
	
	private void calculatePoints(){
	    Gap[] gaps = getGaps();
		float points = clozeTaskBlock.getConfig().getPointsPerTask();
		float negativePoints = clozeTaskBlock.getClozeConfig().getNegativePoints();
		boolean allCorrected = true;
		
	    for( int i=0; i<gaps.length; i++ ){
	        
	        if( gaps[i].isCorrected() ){
	            if( !gaps[i].isCorrect() )
	                points -= negativePoints;
	        }else
	            allCorrected = false;
	        
	    }
	    
	    if( points < 0)
	        points = 0;
	    
	    if( allCorrected )
	        setCorrection( points );
	}

	
	
	private boolean isEmptyString( String s ){
		if( s.length() == 0 )
			return true;
		
		for( int i=0; i<s.length(); i++ ){
			if( s.charAt( i ) != ' ' )
				return false;
		}
		
		return true;
	}
	
	private void setCorrection( float points ){
		ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.CorrectionType corr = clozeSubTask.getCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypeClozeSubTaskTypeCorrectionType();
				clozeSubTask.setCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
	}
	
	public boolean isCorrected(){
		return clozeSubTask.getCorrection() != null;
	}
	
	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#isNeedsManualCorrection()
	 */
	public boolean isNeedsManualCorrection() {
		return clozeSubTask.isNeedsManualCorrection();
	}
	
	public float getPoints() throws IllegalStateException{
		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return clozeSubTask.getCorrection().getPoints();
	}
	
	public boolean isProcessed(){
		Gap[] gaps = getGaps();
		for( int i=0; i<gaps.length; i++ )
			if( gaps[i].getGapValue()!=null && gaps[i].getGapValue().length()!=0 )
				return true;
			
		return false;
	}
	
	private Gap[] getGaps(){
		List gaps = clozeSubTask.getGap();
		Gap[] ret = new Gap[ gaps.size() ];
		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		int gapIndex = 0;
		
		for( int i=0; i<content.size(); i++ ){

			if( content.get( i ) instanceof ClozeSubTaskDefType.ClozeType.Gap ){
				ret[gapIndex] = new GapImpl( gapIndex,
						(ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType) gaps.get( gapIndex ),
						(ClozeSubTaskDefType.ClozeType.Gap)content.get( i ) );
				gapIndex++;
			}
		}
		
		return ret;
		
	}
	
	/**
	 * List mit Instanzen von entweder
	 * - String
	 * - SubTask_CLOZE.Gap
	 * @return
	 */
	public List getContent(){
		List ret = new ArrayList();
		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		List gaps = clozeSubTask.getGap();
		int gapIndex = 0;
		
		for( int i=0; i<content.size(); i++ ){
			if( content.get( i ) instanceof ClozeSubTaskDefType.ClozeType.Text )
				ret.add( ( (ClozeSubTaskDefType.ClozeType.Text)content.get( i ) ).getValue() );
			else if( content.get( i ) instanceof ClozeSubTaskDefType.ClozeType.Gap ){
				ret.add( new GapImpl( gapIndex,
						(ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType) gaps.get( gapIndex ),
						(ClozeSubTaskDefType.ClozeType.Gap)content.get( i ) ) );
				gapIndex++;
			}
		}
		return ret;
		
	}
	
	public String getCorrectionHint(){
	    return null;
	}
	
	public class GapImpl implements Gap{
		
		private int index;
		private ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType gap;
		private ClozeSubTaskDefType.ClozeType.Gap gapDef;
		
		public GapImpl( int index, ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType gap,
								ClozeSubTaskDefType.ClozeType.Gap gapDef ){
			this.index = index;
			this.gap = gap;
			this.gapDef = gapDef;
		}
		
		
		public int getIndex() {
			return index;
		}
		
		public int getInputLength(){
			if( gapDef.isSetInputLength() )
				return gapDef.getInputLength();
			else
				return 20;
		}
		
		public String getGapValue(){
			return gap.getGapValue();
		}
		
		public void setGapValue( String value ){
			gap.setGapValue( value );
		}
		
		private boolean isIgnoreCase(){
			if( gapDef.isSetIgnoreCase() )
				return gapDef.isIgnoreCase();
			else if( clozeTaskBlock.getClozeConfig().isSetIgnoreCase() )
				return clozeTaskBlock.getClozeConfig().isIgnoreCase();
			else
				return false;	// default
		}
		
		public boolean valueEqualsCorrectContent(){
			List correct = gapDef.getCorrect();
			
			for( int i=0; i<correct.size(); i++ ){
				if( !isIgnoreCase() ){
					if( removeLeadingTrailingSpaces( getGapValue() ).equals( removeLeadingTrailingSpaces( (String) correct.get( i ) ) ) )
						return true;
				}else{
					if( removeLeadingTrailingSpaces( getGapValue() ).equalsIgnoreCase( removeLeadingTrailingSpaces( (String) correct.get( i ) ) ) )
						return true;
				}
			}
			
			return false;
		}
		
		public void setAutoCorrection( boolean correct ){
			gap.setCorrected( true );
			gap.setCorrect( correct );
			gap.setAutoCorrected( true );
		}
		
		public void setManualCorrection( boolean correct ){
			gap.setCorrected( true );
			gap.setCorrect( correct );
			gap.setAutoCorrected( false );
		}
		
		public boolean isCorrected(){
		    return gap.isCorrected();
		}
		
		public boolean isAutoCorrected(){
		    return isCorrected() && gap.isAutoCorrected();
		}
		
		public boolean isCorrect() throws IllegalStateException{
		    if( !isCorrected() )
		        throw new IllegalStateException();
		    return gap.isCorrect();
		}
		
		public String[] getCorrectValues(){
		    List correct = gapDef.getCorrect();
		    String ret[] = new String[ correct.size() ];
		    for( int i=0; i<ret.length; i++ )
		        ret[i] = (String) correct.get( i );
		    return ret;
		}
		
		public String removeLeadingTrailingSpaces( String s ){
			if( s.length() == 0 )
				return s;
			
			int i;
			for( i=0; i<s.length(); i++ ){
				if( s.charAt( i ) != ' ' )
					break;
			}
			
			int j;
			for( j = s.length() - 1; j>=0; j-- ){
				if( s.charAt( j ) != ' ' )
					break;
			}
			if( i > j )
				return "";
			
			return s.substring( i, j + 1 );
		}
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
	public void build() throws TaskApiException {
		try {
			addGaps( clozeSubTask, clozeSubTaskDef );
		} catch (JAXBException e) {
			throw new TaskApiException( e );
		}		
	}

	private void addGaps( ComplexTaskHandlingType.TryType.PageType.ClozeSubTask newClozeSubTask,
			ClozeSubTaskDefType clozeSubTaskDef ) throws JAXBException{
		
		ObjectFactory objectFactory = new ObjectFactory();
		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		for( int i=0; i<content.size(); i++ ){
			Object token = content.get( i );
			if( token instanceof ClozeSubTaskDefType.ClozeType.Gap ){
				
				ComplexTaskHandlingType.TryType.PageType.ClozeSubTaskType.GapType gap;
				gap = objectFactory.createComplexTaskHandlingTypeTryTypePageTypeClozeSubTaskTypeGapType();
				gap.setGapValue( "" );
				gap.setCorrected( false );
				newClozeSubTask.getGap().add( gap );
		
			}
		}
	}

}
