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
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.complex.TaskHandlingConstants;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.GenericBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.correctionsubmitdata.ClozeCorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.ClozeSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef.Category.ClozeTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef.Category.ClozeTaskBlock.ClozeConfig;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.Config;
import de.thorstenberger.taskmodel.complex.jaxb.ManualCorrectionType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_ClozeImpl extends AbstractSubTasklet implements SubTasklet_Cloze {

	private ClozeTaskBlock clozeTaskBlock;
	private ClozeSubTaskDef clozeSubTaskDef;
	private ClozeSubTask clozeSubTask;

	/**
	 *
	 */
	public SubTasklet_ClozeImpl( Block block, SubTaskDefType clozeSubTaskDef, SubTaskType clozeSubTask, ComplexTaskDefRoot complexTaskDefRoot ) {
		super( complexTaskDefRoot, block, clozeSubTaskDef, clozeSubTask  );
		this.clozeTaskBlock = (ClozeTaskBlock) ((GenericBlockImpl)block).getJaxbTaskBlock();
		this.clozeSubTaskDef = (ClozeSubTaskDef) clozeSubTaskDef;
		this.clozeSubTask = (ClozeSubTask) clozeSubTask;
	}



	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( clozeSubTask.getRefId() );
		List gaps = clozeSubTask.getGap();
		ret.append( gaps.size() );
		for( int i=0; i<gaps.size(); i++ ){
      ComplexTaskHandling.Try.Page.ClozeSubTask.Gap gap =
          (ComplexTaskHandling.Try.Page.ClozeSubTask.Gap) gaps.get(i);
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
    ClozeConfig cfg = clozeTaskBlock.getClozeConfig();
    //this taskblock is set as fully automatic correction,
    final boolean isBlockFullyAutomatic = cfg.isSetNeedManualCorrection() && cfg.isNeedManualCorrection() == false;
    
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
			  // gap value matches a given correct value
				gaps[i].setAutoCorrection( true );
				noneProcessed = false;
			}else if(isBlockFullyAutomatic && !gaps[i].isSetNeedsManualCorrection() || !gaps[i].isNeedsManualCorrection()){
			  /* if the task block does not need manual corrections and this gap do
			   * this gap is wrong iff its contents are not defined as valid solution
			   */
			  gaps[i].setAutoCorrection( false );
			  noneProcessed = false;
			  points -= negativePoints;
			}else{
				couldCorrectAllGaps = false;
				noneProcessed = false;
			}

		}

		if( noneProcessed ){
			setAutoCorrection( 0 );
			return;
		}

    // either some gaps where empty or correct, or this taskblock is set as fully automatic correction
		if( couldCorrectAllGaps){
			if( points < 0 ) {
                points = 0;
            }
			setAutoCorrection( points );
		}
        else {
            clozeSubTask.setNeedsManualCorrection( true );
			// ok, we set the flag, but remember that the flag will not be removed anymore, see logic in #isNeedsManualCorrection()
        }

	}


	public void doManualCorrection( CorrectionSubmitData csd ) throws IllegalStateException{

		if( isAutoCorrected() )
			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_AUTO_CORRECTED );

	    ClozeCorrectionSubmitData ccsd = (ClozeCorrectionSubmitData) csd;

	    Gap[] gaps = getGaps();
	    for( int i=0; i<gaps.length; i++ ){
	        if( !gaps[i].isAutoCorrected() ) {
                gaps[i].setManualCorrection( csd.getCorrector(),
	            		complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS, ccsd.isCorrect( i ) );
            }
	    }

	    calculatePoints( csd.getCorrector(), complexTaskDefRoot.getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS );
	}

	/**
	 * Calculate points for manual correction. The main difference between single and multiple corrector mode is:
	 * Multiple Corrector Mode: each gap correction is separated between the correctors and the actual corrector is
	 * denoted by the @param corrector.
	 * Single Corrector Mode: each gap correction is not separated between various correctors. Each corrector overwrites the
	 * gap correction of the other and the last correctors login is saved.
	 *
	 *
	 */
	private void calculatePoints( String corrector, boolean multipleCorrectionMode ){
	    Gap[] gaps = getGaps();
		float points = clozeTaskBlock.getConfig().getPointsPerTask();
		float negativePoints = clozeTaskBlock.getClozeConfig().getNegativePoints();
		boolean allCorrected = true;

	    for( int i=0; i<gaps.length; i++ ){

	        if( gaps[i].isCorrected() ){

	        	if( gaps[i].isAutoCorrected() ){

	        		if( !gaps[i].isCorrectByAutoCorrection() ) {
                        points -= negativePoints;
                    }

	        	}else{ // branch by correction mode

	        		if( multipleCorrectionMode ){

	        			if( gaps[i].isCorrectedByCorrector( corrector ) ){
	        				if( !gaps[i].isCorrectByCorrector( corrector ) ) {
                                points -= negativePoints;
                            }
	        			}else{
	        				allCorrected = false;
	        				break;
	        			}


	        		}else{ // single corrector mode

	        			// there has to be at least one manual correction
	        			if( !gaps[i].getManualCorrections().get( 0 ).isCorrect() ) {
                            points -= negativePoints;
                        }

	        		}
	        	}


	        }else{
	            allCorrected = false;
	            break;
	        }

	    }

	    if( points < 0) {
            points = 0;
        }

	    if( allCorrected ){

	    	if( multipleCorrectionMode ){
	    		setManualCorrectionInMultipleCorrectorMode( corrector, points );
	    	}else{
	    		setManualCorrectionInSingleCorrectorMode(corrector, points);
	    	}
	    }
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

  private void setManualCorrectionInMultipleCorrectorMode(String corrector, float points) {
    // try to find the corrector
    List<ManualCorrectionType> mcs = clozeSubTask.getManualCorrection();
    for (ManualCorrectionType mc : mcs) {
      if (mc.getCorrector().equals(corrector)) {
        mc.setPoints(points);
        return;
      }
    }
    // not found, so create a new manual correction for that corrector
    ManualCorrectionType mct = objectFactory.createManualCorrectionType();
    mct.setCorrector(corrector);
    mct.setPoints(points);
    mcs.add(mct);
  }

  private void setManualCorrectionInSingleCorrectorMode(String corrector, float points) {
    // get the first corrector
    List<ManualCorrectionType> mcs = clozeSubTask.getManualCorrection();
    if (mcs.size() < 1) {
      de.thorstenberger.taskmodel.complex.jaxb.ManualCorrectionType mct = objectFactory.createManualCorrectionType();
      mct.setCorrector(corrector);
      mct.setPoints(points);
      mcs.add(mct);
    } else {
      mcs.get(0).setCorrector(corrector);
      mcs.get(0).setPoints(points);
    }
  }

//	public float getPoints() throws IllegalStateException{
//		if( !isCorrected() )
//			throw new IllegalStateException( TaskHandlingConstants.SUBTASK_NOT_CORRECTED );
//
//		return clozeSubTask.getCorrection().getPoints();
//	}

  public boolean isProcessed(){
    for (final Gap gap : getGaps()) {
      if(gap.isProcessed())
        return true;
    }
    return false;
  }

	private Gap[] getGaps(){
		List gaps = clozeSubTask.getGap();
		Gap[] ret = new Gap[ gaps.size() ];
		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		int gapIndex = 0;

		for( int i=0; i<content.size(); i++ ){

      if (content.get(i) instanceof ClozeSubTaskDef.Cloze.Gap) {
				ret[gapIndex] = new GapImpl( gapIndex,
            (ComplexTaskHandling.Try.Page.ClozeSubTask.Gap) gaps.get(gapIndex),
            (ClozeSubTaskDef.Cloze.Gap) content.get(i));
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
      if (content.get(i) instanceof String) {
        ret.add(content.get(i));
      } else {
				ret.add( new GapImpl( gapIndex,
            (ComplexTaskHandling.Try.Page.ClozeSubTask.Gap) gaps.get(gapIndex),
            (ClozeSubTaskDef.Cloze.Gap) content.get(i)));
				gapIndex++;
			}
		}
		return ret;

	}

	@Override
    public String getCorrectionHint(){
	    return null;
	}

	public class GapImpl implements Gap{

		private int index;
    private ComplexTaskHandling.Try.Page.ClozeSubTask.Gap gap;
    private ClozeSubTaskDef.Cloze.Gap gapDef;

		public GapImpl(int index, ComplexTaskHandling.Try.Page.ClozeSubTask.Gap gap,
                ClozeSubTaskDef.Cloze.Gap gapDef) {
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

    public boolean isProcessed() {
      final String gapValue = getGapValue();
      return gapValue != null && gapValue.length() != 0 && !gapValue.equals(gapDef.getInitialValue());
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
			List<String> correct = trimWhitespace(gapDef.getCorrect());
			final String gapValue = trimWhitespace(getGapValue());

			for( String alternative: correct ){
				if( !isIgnoreCase() ){
					if( gapValue.equals( alternative ) )
						return true;
				}else{
					if( gapValue.equalsIgnoreCase( alternative ) )
						return true;
				}
			}

			return false;
		}

		/**
		 * Remove multiple occurances of whitespace from every string in the list. 
		 * I.e. "    a b  c      d  " ==> "a b c d"
     * @param correct
     * @return
     */
    private List<String> trimWhitespace(List<String> correct) {
      List<String> res = new ArrayList<String>();
      for (String string : correct) {
        res.add(trimWhitespace(string));
      }
      return res;
    }
    /**
     * Remove multiple occurances of whitespace. I.e. "    a b  c      d  " ==> "a b c d"
     * @param correct
     * @return
     */
    private String trimWhitespace(String string){
      return StringUtils.trim(string).replaceAll("\\s\\s", " ");
    }


    /* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#getManualCorrections()
		 */
		public List<ManualGapCorrection> getManualCorrections() throws IllegalStateException {
			List<ManualGapCorrection> ret = new LinkedList<ManualGapCorrection>();
      List<de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection> mcs = gap.getManualCorrection();
      for (de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection mc : mcs) {
                ret.add( new ManualGapCorrectionImpl( mc.getCorrector(), mc.isCorrect() ) );
            }
			return ret;
		}


		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#isCorrectByCorrector(java.lang.String)
		 */
		public boolean isCorrectByCorrector(String corrector) throws IllegalStateException {
			List<ManualGapCorrection> gcs = getManualCorrections();
			if( complexTaskDefRoot.getCorrectionMode().getType() != ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS ){
				if( gcs.size() <= 0 )
					throw new IllegalStateException( "Gap has not been corrected by any corrector." );
				return gcs.get( 0 ).isCorrect();
			}else{
				for( ManualGapCorrection gc : gcs ){
					if( gc.getCorrector().equals( corrector ) )
						return gc.isCorrect();
				}
				throw new IllegalStateException( "Gap has not been corrected by corrector \"" + corrector + "\"." );
			}
		}


		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#isCorrectedByCorrector(java.lang.String)
		 */
		public boolean isCorrectedByCorrector(String corrector) {
			if( isAutoCorrected() )
				return false;
			List<ManualGapCorrection> gapCorrections = getManualCorrections();

			if( complexTaskDefRoot.getCorrectionMode().getType() != ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS )
				return gapCorrections != null && gapCorrections.size() > 0;

			for( ManualGapCorrection gc : gapCorrections )
				if( gc.getCorrector().equals( corrector ) )
					return true;
			return false;

		}


		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#isCorrectByAutoCorrection()
		 */
		public boolean isCorrectByAutoCorrection() throws IllegalStateException {
			if( !isAutoCorrected() )
				throw new IllegalStateException( "Gap has not been corrected automatically." );
			return gap.getAutoCorrection().isCorrect();
		}


		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#setManualCorrection(java.lang.String, boolean)
		 */
		public void setManualCorrection(String corrector, boolean multiCorrectorMode, boolean correct) {
      List<de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection> manualCorrections = gap.getManualCorrection();
			if( multiCorrectorMode ){

        for (de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection mc : manualCorrections) {
					if( mc.getCorrector().equals( corrector ) ){
						mc.setCorrect( correct );
						return;
					}
				}
				// corrector not found, so create a new ManualCorrection for him
        de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection mc;
        mc = objectFactory.createComplexTaskHandlingTryPageClozeSubTaskGapManualCorrection();
				mc.setCorrector( corrector );
				mc.setCorrect( correct );
				manualCorrections.add( mc );

			}else{

        de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.ManualCorrection mc;
				if( manualCorrections.size() > 0 ){
					mc = manualCorrections.get( 0 );
					mc.setCorrector( corrector );
					mc.setCorrect( correct );
				}else{
          mc = objectFactory.createComplexTaskHandlingTryPageClozeSubTaskGapManualCorrection();
					mc.setCorrector( corrector );
					mc.setCorrect( correct );
					manualCorrections.add( mc );
				}
			}
		}


		public void setAutoCorrection( boolean correct ){
      de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask.Gap.AutoCorrection ac = gap.getAutoCorrection();
			if( ac == null ){
        ac = objectFactory.createComplexTaskHandlingTryPageClozeSubTaskGapAutoCorrection();
				gap.setAutoCorrection( ac );
			}
			ac.setCorrect( correct );
		}

		public boolean isCorrected(){
		    return gap.isSetAutoCorrection() || gap.isSetManualCorrection();
		}

		public boolean isAutoCorrected(){
		    return isCorrected() && gap.isSetAutoCorrection();
		}

		public String[] getCorrectValues(){
		    List correct = gapDef.getCorrect();
		    String ret[] = new String[ correct.size() ];
		    for( int i=0; i<ret.length; i++ ) {
                ret[i] = (String) correct.get( i );
            }
		    return ret;
		}


    /* (non-Javadoc)
     * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#isNeedsManualCorrection()
     */
    public boolean isNeedsManualCorrection() {
      return !gapDef.isSetNeedManualCorrection() || gapDef.isNeedManualCorrection();
    }


    /* (non-Javadoc)
     * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.Gap#isSetNeedsManualCorrection()
     */
    public boolean isSetNeedsManualCorrection() {
      return gapDef.isSetNeedManualCorrection();
    }

	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
    public void build(long randomSeed) throws TaskApiException {
		try {
			addGaps( clozeSubTask, clozeSubTaskDef );
		} catch (JAXBException e) {
			throw new TaskModelPersistenceException( e );
		}
	}

  private void addGaps(ComplexTaskHandling.Try.Page.ClozeSubTask newClozeSubTask,
      ClozeSubTaskDef clozeSubTaskDef) throws JAXBException {

		List content = clozeSubTaskDef.getCloze().getTextOrGap();
		for( int i=0; i<content.size(); i++ ){
			Object token = content.get( i );
      if (token instanceof ClozeSubTaskDef.Cloze.Gap) {

				ComplexTaskHandling.Try.Page.ClozeSubTask.Gap gap;
        gap = objectFactory.createComplexTaskHandlingTryPageClozeSubTaskGap();
        gap.setGapValue(((ClozeSubTaskDef.Cloze.Gap) token).isSetInitialValue() ?
            ((ClozeSubTaskDef.Cloze.Gap) token).getInitialValue() : "");
				newClozeSubTask.getGap().add( gap );

			}
		}
	}

	public class ManualGapCorrectionImpl implements ManualGapCorrection{

		private String corrector;
		private boolean correct;

		/**
		 * @param corrector
		 * @param correct
		 */
		public ManualGapCorrectionImpl(String corrector, boolean correct) {
			super();
			this.corrector = corrector;
			this.correct = correct;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.ManualGapCorrection#getCorrector()
		 */
		public String getCorrector() {
			return corrector;
		}

		/* (non-Javadoc)
		 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze.ManualGapCorrection#isCorrect()
		 */
		public boolean isCorrect() {
			return correct;
		}

	}

}
