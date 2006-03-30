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

import javax.xml.bind.JAXBException;

import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.complex.jaxb.TextSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTask_TEXT extends SubTask{

	private ComplexTaskDefType.CategoryType.TextTaskBlock block;
	private TextSubTaskDefType textSubTaskDef;
	private ComplexTaskHandlingType.TryType.PageType.TextSubTaskType textSubTask;
	
	/**
	 * @param id
	 */
	public SubTask_TEXT( ComplexTaskDefType.CategoryType.TextTaskBlock block,
			TextSubTaskDefType textSubTaskDef,
			ComplexTaskHandlingType.TryType.PageType.TextSubTaskType textSubTask ) {
		
		super( textSubTask.getRefId() );
		this.textSubTaskDef = textSubTaskDef;
		this.textSubTask = textSubTask;
		this.block = block;
		
	}

	/**
	 * @see de.thorstenberger.uebman.task.beans.complexTask.SubTask#addToPage(de.thorstenberger.uebman.xml.taskhandling.jaxb.ComplexTaskHandlingType.TryType.PageType)
	 */
	public void addToPage(PageType page) {
		page.getMcSubTaskOrClozeSubTaskOrTextSubTask().add( textSubTask );

	}
	
	public float getReachablePoints(){
		return block.getConfig().getPointsPerTask();
	}
	
	public String getProblem(){
		return textSubTaskDef.getProblem();
	}
	
	public String getHint(){
		return textSubTaskDef.getHint();
	}
	
	public void setVirtualNum( String virtualNum ){
		textSubTask.setVirtualNum( virtualNum );
	}
	
	public String getVirtualNum(){
		return textSubTask.getVirtualNum();
	}
	
	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( textSubTask.getRefId() );		
		ret.append( getAnswer() );
		ret.append( getVirtualNum() );
		return ret.toString().hashCode();
	}
	
	public String getAnswer(){
		return textSubTask.getAnswer();
	}
	
	public void doSave( SubmitData submitData ) throws IllegalStateException{
		TEXTSubmitData tsd = (TEXTSubmitData) submitData;
		textSubTask.setAnswer( tsd.getAnswer() );
	}
	
	public void doAutoCorrection(){
		if( getAnswer() == null || getAnswer().length() == 0 ){
			setCorrection( 0 , true);
		}
		
		// mehr können wir leider nicht machen...
	}
	
	public void doManualCorrection( CorrectionSubmitData csd ){
	    CorrectionSubmitData_TEXT csdt = (CorrectionSubmitData_TEXT) csd;
	    float points = csdt.getPoints();
	    if( points>=0 && points <= block.getConfig().getPointsPerTask() )
	        setCorrection( points, false );
	}
	
	private void setCorrection( float points, boolean auto ){
		ComplexTaskHandlingType.TryType.PageType.TextSubTaskType.CorrectionType corr = textSubTask.getCorrection();
		if( corr == null ){
			ObjectFactory of = new ObjectFactory();
			try {
				corr = of.createComplexTaskHandlingTypeTryTypePageTypeTextSubTaskTypeCorrectionType();
				textSubTask.setCorrection( corr );
			} catch (JAXBException e) {
				throw new TaskModelPersistenceException( e );
			}
		}
		corr.setPoints( points );
		corr.setAutoCorrected( auto );
	}

	
	public boolean isCorrected(){
		return textSubTask.getCorrection() != null;
	}
	
	public float getPoints() throws IllegalStateException{
		if( !isCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
		
		return textSubTask.getCorrection().getPoints();
	}
	
	public boolean isProcessed(){
		return textSubTask.getAnswer()!=null && textSubTask.getAnswer().length() > 0;
	}
	
	public int getTextFieldWidth(){
		return textSubTaskDef.isSetTextFieldWidth() ?
					textSubTaskDef.getTextFieldWidth() : 60;
	}
	
	public int getTextFieldHeight(){
		return textSubTaskDef.isSetTextFieldHeight() ?
					textSubTaskDef.getTextFieldHeight() : 15;
	}

	public String getCorrectionHint(){
	    return textSubTaskDef.getCorrectionHint();
	}
}
