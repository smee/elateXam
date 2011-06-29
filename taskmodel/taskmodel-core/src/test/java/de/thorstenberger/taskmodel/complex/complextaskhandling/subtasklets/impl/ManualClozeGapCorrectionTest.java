/*

Copyright (C) 2011 Steffen Dienst

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
package de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.complextaskdef.blocks.impl.GenericBlockImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.submitdata.ClozeSubmitData;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef.Cloze;
import de.thorstenberger.taskmodel.complex.jaxb.ClozeSubTaskDef.Cloze.Gap;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef.Category.ClozeTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDef.Category.ClozeTaskBlock.ClozeConfig;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.ClozeSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.Config;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;

/**
 * Given there are explicit values for <code>needManualCorrection</code> for a cloze tasklbock or a cloze gap, this test
 * tests the combinations of expected behaviour.
 * TODO test cases where these values are not set!
 * @author Steffen Dienst
 *
 */
@RunWith(value = Parameterized.class)
public class ManualClozeGapCorrectionTest {

  private SubTasklet_ClozeImpl sc;
  private boolean shouldbeCorrected;
  private float points;
  
  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] { 
        // correct answer
        { false, false, false, false, "correct", "correct", true, 1 }, 
        { false, false, true,  false, "correct", "correct", true, 1 }, 
        { true,  false, false, false, "correct", "correct", true, 1 }, 
        { true,  false, true,  false, "correct", "correct", true, 1 }, 

        // incorrect answer, cloze taskblock needManual flag is set
        { false, true, false, false, "correct", "incorrect", true, 0 }, 
        { false, true, true,  false, "correct", "incorrect", true, 0 }, 
        { true,  true, false, false, "correct", "incorrect", false, 1 }, 
        { true,  true, true,  false, "correct", "incorrect", false, 1 }, 

        // incorrect answer, gap needManual flag is set
        { false, false, false, true, "correct", "incorrect", true, 0 }, 
        { false, false, true,  true, "correct", "incorrect", false, 0 }, 
        { true,  false, false, true, "correct", "incorrect", true, 0 }, 
        { true,  false, true,  true, "correct", "incorrect", false, 1 }, 

        // incorrect answer, both flags are set
        { false, true, false, true, "correct", "incorrect", true, 0 }, 
        { false, true, true,  true, "correct", "incorrect", false, 0 }, 
        { true,  true, false, true, "correct", "incorrect", true, 0 }, 
        { true,  true, true,  true, "correct", "incorrect", false, 1 }, 

      };
    return Arrays.asList(data);
  }

  public ManualClozeGapCorrectionTest(
      boolean isBlockNeedManualCorrection, 
      boolean isSetBlockNeedManualCorrection, 
      boolean isGapNeedManualCorrection,
      boolean isSetGapNeedManualCorrection,
      String userValue, 
      String correct, 
      boolean shouldbeCorrected, 
      float points) throws TaskApiException{
    
    this.points = points;
    this.shouldbeCorrected = shouldbeCorrected;
      
    ObjectFactory factory = new ObjectFactory();
    ClozeConfig cc =factory.createComplexTaskDefCategoryClozeTaskBlockClozeConfig();
    // emulate whether this optional attribute is set or not
    if(isSetBlockNeedManualCorrection)
      cc.setNeedManualCorrection(isBlockNeedManualCorrection);
    
    cc.setNegativePoints(1);
    cc.setIgnoreCase(false);
    Config cfg = factory.createConfig();
    cfg.setPointsPerTask(1);
    
    ClozeTaskBlock block = factory.createComplexTaskDefCategoryClozeTaskBlock();
    block.setClozeConfig(cc);
    block.setConfig(cfg);
    
    Gap gap = factory.createClozeSubTaskDefClozeGap();
    gap.setInitialValue("initial");
    // emulate whether this optional attribute is set or not
    if(isSetGapNeedManualCorrection)
      gap.setNeedManualCorrection(isGapNeedManualCorrection);
    gap.getCorrect().addAll(Arrays.asList(correct));
    
    Cloze clozes = factory.createClozeSubTaskDefCloze();
    clozes.getTextOrGap().add(gap);
    ClozeSubTaskDef std = factory.createClozeSubTaskDef();
    std.setId("ID");
    std.setCloze(clozes);
    
    ClozeSubTask clozeSubTask = factory.createComplexTaskHandlingTryPageClozeSubTask();
    clozeSubTask.setVirtualNum("1");
    clozeSubTask.setRefId("ID");
    this.sc = new SubTasklet_ClozeImpl(new GenericBlockImpl(block, 0), std, clozeSubTask, null);

    sc.build(0);
    
    ClozeSubmitData submitData=new ClozeSubmitData();
    submitData.setGapValue(0, userValue);
    sc.doSave(submitData);
  }

  
 @Test
  public void test() throws TaskApiException {
    //when
    sc.doAutoCorrection();
    
    // then
    assertEquals(shouldbeCorrected,sc.isAutoCorrected());
    
    if(shouldbeCorrected)
      assertEquals(points,sc.getAutoCorrection().getPoints(),1e-6);
  }

}
