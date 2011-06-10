/*

Copyright (C) 2010 Steffen Dienst

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;

import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.McSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling.Try.Page.McSubTask.Answer;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef.Correct;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDef.Incorrect;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.util.ReflectionHelper;
/**
 * @author Steffen Dienst
 *
 */
public class SubTasklet_MCBuilderTest {

    private SubTasklet_MCBuilder mcbuilder;
    private McSubTask subtask;
    private ObjectFactory factory;

    @Before
    public void setup() throws JAXBException {
        this.mcbuilder = new SubTasklet_MCBuilder(new RandomUtil());
        this.factory = new ObjectFactory();
    }
    @org.junit.Test
    public void shouldSelectAllAnswers() throws JAXBException {
        String[] answers = new String[] { "good1", "bad1", "good2", "bad2" };
        boolean[] correct = new boolean[] { true, false, true, false };

    McSubTaskDef mcSubTaskDef = create(answers, correct, 2, 2, 4, true);
        mcbuilder.constructAnswersForMCSubTask(subtask, mcSubTaskDef);

        Assert.assertTrue(ArrayUtils.isEquals(correct, getChoosenAnswerTypes()));
    }

    @org.junit.Test
    public void shouldKeepAnswersSorted() throws JAXBException {
        String[] answers = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
        boolean[] correct = new boolean[] { true, false, true, false, false, true, false, false };
        int count = 300;
        while (count-- >= 0) {
      McSubTaskDef mcSubTaskDef = create(answers, correct, 1, 3, 5, true);
            mcbuilder.constructAnswersForMCSubTask(subtask, mcSubTaskDef);

            checkAnswersSorted(mcSubTaskDef, subtask);
        }
    }

  void checkAnswersSorted(McSubTaskDef mcSubTaskDef, McSubTask subtask) {
        List<String> answers = new ArrayList<String>();

        for (Object o : subtask.getAnswer()) {
      Answer answer = (Answer) o;
            answers.add(getAnswertest(mcSubTaskDef.getCorrectOrIncorrect(), answer.getRefId()));
        }
        List<String> sortedAnswers = new ArrayList<String>(answers);
        Collections.sort(sortedAnswers);
        Assert.assertEquals(answers, sortedAnswers);
    }

    private String getAnswertest(List correctOrIncorrect, String refId) {
        for (Object o : correctOrIncorrect) {
            if (refId.equals(ReflectionHelper.callMethod(o, "getId", new Object[0])))
                return ReflectionHelper.callMethod(o, "getValue", new Object[0]);
        }
        throw new IllegalArgumentException("no such mc answer!");
    }
    @org.junit.Test
    public void testSingleSelect() throws JAXBException {
        String[] answers = new String[] { "a", "b", "c", "d", "e", "f" };
        boolean[] correct = new boolean[] { true, true, true, false, false, false };

        int count = 300;
        while (count-- >= 0) {
      McSubTaskDef mcSubTaskDef = create(answers, correct, 1, 3, 4, false);
            mcSubTaskDef.setCategory(SubTasklet_MC.CAT_SINGLESELECT);
            mcbuilder.constructAnswersForMCSubTask(subtask, mcSubTaskDef);

            Assert.assertEquals(1, count(getChoosenAnswerTypes(), true));
        }
    }

    @org.junit.Test
    public void testMultipleSelect() throws JAXBException {
        final String[] answers = new String[] { "a", "b", "c", "d", "e", "f" };
        final boolean[] correct = new boolean[] { true, true, true, false, false, false };

        int count = 300;
        while (count-- >= 0) {
      McSubTaskDef mcSubTaskDef = create(answers, correct, 2, 2, 4, false);
            mcSubTaskDef.setCategory(SubTasklet_MC.CAT_MULTIPLESELECT);
            mcbuilder.constructAnswersForMCSubTask(subtask, mcSubTaskDef);

            Assert.assertEquals(2, count(getChoosenAnswerTypes(), true));
        }
    }


    private int count(boolean[] array, boolean toCheck) {
        int count = 0;
        for (boolean val : array)
            if (val == toCheck) {
                count++;
            }
        return count;
    }
    /**
     * Flags for choosen answers, representing correctness.
     *
     * @return
     */
    private boolean[] getChoosenAnswerTypes() {
        boolean[] result = new boolean[subtask.getAnswer().size()];
        int idx = 0;
        for (Object answerObject : subtask.getAnswer()) {
      Answer answer = (Answer) answerObject;
            String refId = answer.getRefId();
            result[idx++] = refId.startsWith("c");
        }
        return result;
    }

    /**
     * @param answers
     * @param isCorrect
     * @param minCorrect
     * @param maxCorrect
     * @param numAnswers
     * @param sorted
     * @return
     */
  private McSubTaskDef create(String[] answers, boolean[] isCorrect, int minCorrect, int maxCorrect, int numAnswers, boolean sorted) {
    subtask = factory.createComplexTaskHandlingTryPageMcSubTask();
    McSubTaskDef mc = factory.createMcSubTaskDef();
    // add all answers
    int idx = 0;
    for (String ans : answers) {
      if (isCorrect[idx]) {
        Correct answer = factory.createMcSubTaskDefCorrect();
        answer.setValue(ans);
        answer.setId("c" + idx++);
        mc.getCorrectOrIncorrect().add(answer);
      } else {
        Incorrect answer = factory.createMcSubTaskDefIncorrect();
        answer.setValue(ans);
        answer.setId("ic" + idx++);
        mc.getCorrectOrIncorrect().add(answer);
      }
    }
    mc.setCategory(SubTasklet_MC.CAT_MULTIPLESELECT);
    mc.setMinCorrectAnswers(minCorrect);
    mc.setMaxCorrectAnswers(maxCorrect);
    mc.setDisplayedAnswers(numAnswers);
    mc.setPreserveOrderOfAnswers(sorted);

    return mc;
  }
}
