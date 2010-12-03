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

import static org.apache.commons.lang.ArrayUtils.addAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.McSubTaskType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType.CorrectType;
import de.thorstenberger.taskmodel.complex.jaxb.McSubTaskDefType.IncorrectType;
import de.thorstenberger.taskmodel.complex.jaxb.ObjectFactory;
import de.thorstenberger.taskmodel.util.ReflectionHelper;

/**
 * @author Thorsten Berger
 *
 */
public class SubTasklet_MCBuilder {

	Log log = LogFactory.getLog( SubTasklet_MCBuilder.class );
	private ObjectFactory taskHandlingobjectFactory = new ObjectFactory();
    private RandomUtil r;

	public SubTasklet_MCBuilder(RandomUtil r) {
        this.r = r;
    }
	/**
	 * M�gliche Antworten zu MC-Aufgaben hinzuf�gen.
	 * @param newMcSubTask
	 * @param mcSubTaskDef
	 */
	void constructAnswersForMCSubTask( ComplexTaskHandlingType.TryType.PageType.McSubTask newMcSubTask,
            McSubTaskDefType mcSubTaskDef) throws JAXBException {

        List allAvailableAnswers = mcSubTaskDef.getCorrectOrIncorrect();
        int[] availCorrectAnswersIndices = getIndices(allAvailableAnswers, CorrectType.class);
        int[] availIncorrectAnswersIndices = getIndices(allAvailableAnswers, IncorrectType.class);

        // make sure the task definition is correct, i.e. that there are answers at all!
        if (availCorrectAnswersIndices.length == 0 || mcSubTaskDef.getDisplayedAnswers() == 0) {
			log.warn( "Aufgabe " + mcSubTaskDef.getId() + " wird keine Antworten enthalten!" );
			return;
		}
        // how many correct answers will be presented?
        int numOfCorrectAnswersToShow = getChosenNumCorrectAnswers(mcSubTaskDef, availCorrectAnswersIndices.length, availIncorrectAnswersIndices.length);

		// Anzahl der insg. anzuzeigenden Antworten
        int numOfAnswers = Math.min(numOfCorrectAnswersToShow + availIncorrectAnswersIndices.length, mcSubTaskDef.getDisplayedAnswers());
        int numOfIncorrectAnswersToShow = numOfAnswers - numOfCorrectAnswersToShow;


        // random permutation of indices to all available correct answer definitions
        int[] correctAnswersOrder = r.getPermutation(availCorrectAnswersIndices.length);
        // random permutation of indices to all available incorrect answer definitions
        int[] incorrectAnswersOrder = r.getPermutation(availIncorrectAnswersIndices.length);

        // final array of the indices of the answer definition that will be chosen
        int[] choosenAnswerIndices = addAll(
                selectFirst(availCorrectAnswersIndices, correctAnswersOrder, numOfCorrectAnswersToShow),
                selectFirst(availIncorrectAnswersIndices, incorrectAnswersOrder, numOfIncorrectAnswersToShow));

        if (mcSubTaskDef.isPreserveOrderOfAnswers()) {
            Arrays.sort(choosenAnswerIndices);
        } else {
            // use unique random generator to shuffle the selected answers
            // This means everybody may get the same answers, but not necessarily in the same order
            choosenAnswerIndices = new RandomUtil().shuffle(choosenAnswerIndices);
        }

        // Array der ausgew�hlten Antworten
        ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType[] toInsert =
                new ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType[numOfAnswers];

		int insertIndex = 0;
        // insert correct answers to represent
        for (int i = 0; i < numOfAnswers; i++) {
            toInsert[insertIndex++] = createAnswerType(
                    allAvailableAnswers.get(choosenAnswerIndices[i]));
        }

		// und jetzt die Antworten in XML entspr. einf�gen
		List answers = newMcSubTask.getAnswer();
		for( int i=0; i<toInsert.length; i++) {
            answers.add( toInsert[i] );
        }

	}

    /**
     * Return the first <code>numToChoose</code> values from <code>values</code> as indexed by <code>indices</code><br/>
     * Example: selectFirst({10,20,30,40},{2,0,1,3},2) ==> {30,10}
     *
     * @param values
     * @param indices
     * @param numToChoose
     * @return
     */
    private int[] selectFirst(int[] values, int[] indices, int numToChoose) {
        if (values.length != indices.length || indices.length < numToChoose)
            throw new IllegalArgumentException(
                    String.format("Internal implementation error, can't choose the first %d values of %s indexed by %s!", numToChoose, ArrayUtils.toString(values), ArrayUtils.toString(indices)));

        int[] result = new int[numToChoose];
        for (int i = 0; i < numToChoose; i++) {
            result[i] = values[indices[i]];
        }
        return result;
    }

    /**
     * How many correct answers will be chosen for representation?
     *
     * @param mcSubTaskDef
     * @param availCorrectAnswers
     *            number of existing correct answer definitions
     * @param availIncorrectAnswers
     *            number of existing incorrect answer definitions
     * @return
     */
    protected int getChosenNumCorrectAnswers(McSubTaskDefType mcSubTaskDef, int availCorrectAnswers, int availIncorrectAnswers) {
        int numOfCorrectAnswers;
		// nach Kategorie die Anzahl der richtigen Antworten festlegen
		if( mcSubTaskDef.getCategory().equals( SubTasklet_MC.CAT_SINGLESELECT ) ){
			numOfCorrectAnswers = 1;
		}else{

			int wantedMinCorrect = mcSubTaskDef.isSetMinCorrectAnswers() ?
										mcSubTaskDef.getMinCorrectAnswers() : 1;

			// mindestens so viele richtige Antworten
			// das kommt daher, dass zu wenig falsche Fragen da sein k�nnten, um die gew�nschte
			// Anzahl angezeigter Antworten zu erzeugen
            int minCorr = Math.max(mcSubTaskDef.getDisplayedAnswers() - availIncorrectAnswers, wantedMinCorrect);
			// und sicherheitshalber beschr�nken
            minCorr = Math.min(minCorr, availCorrectAnswers);
			minCorr = Math.min( minCorr, mcSubTaskDef.getDisplayedAnswers() );

			// maximal so viele richtige Antworten
            int maxCorr = Math.min(availCorrectAnswers, mcSubTaskDef.getDisplayedAnswers());
			if( mcSubTaskDef.isSetMaxCorrectAnswers() ) {
                maxCorr = Math.min( maxCorr, mcSubTaskDef.getMaxCorrectAnswers() );
            }

			// TODO Algorithmus verifizieren
			if( maxCorr < minCorr ) {
                maxCorr = minCorr;
            }

			// ok, jetzt zuf�llig mit den berechneten Grenzen festlegen
            numOfCorrectAnswers = r.getInt(maxCorr - minCorr + 1) + minCorr;

		}
        return numOfCorrectAnswers;
    }

	void constructPreviewAnswersForMCSubTask( ComplexTaskHandlingType.TryType.PageType.McSubTask newMcSubTask,
			McSubTaskDefType mcSubTaskDef) throws JAXBException{

        List<CorrectType> correctAnswers = SubTasklet_MCBuilder.filterList(mcSubTaskDef.getCorrectOrIncorrect(), CorrectType.class);
        List<IncorrectType> incorrectAnswers = SubTasklet_MCBuilder.filterList(mcSubTaskDef.getCorrectOrIncorrect(), IncorrectType.class);

		// sicherheitshalber pr�fen, d�rfte aber schon durch XML-Schema ausgeschlossen sein
		if( correctAnswers.size() == 0 || mcSubTaskDef.getDisplayedAnswers() == 0 ){
			log.warn( "Aufgabe " + mcSubTaskDef.getId() + " enth�lt keine Antworten" );
			return;
		}

		List answers = newMcSubTask.getAnswer();
		for( CorrectType ct : correctAnswers ) {
            answers.add( createAnswerType( ct ) );
        }
		for( IncorrectType ict : incorrectAnswers ) {
            answers.add( createAnswerType( ict ) );
        }

	}


	/**
	 * neuen AnswerType aus korrekter Antwort erstellen
	 * @param correct
	 * @return
	 */
    private McSubTaskType.AnswerType createAnswerType(Object answer) throws JAXBException {

        ComplexTaskHandlingType.TryType.PageType.McSubTaskType.AnswerType ret;

		ret = taskHandlingobjectFactory.createComplexTaskHandlingTypeTryTypePageTypeMcSubTaskTypeAnswerType();

        ret.setRefId((String) ReflectionHelper.callMethod(answer, "getId", new Object[0]));
		ret.setSelected( false );

		return ret;

	}

    /**
     * Return all instances of <code>clazz</code> from the given list.
     *
     * @param correctOrIncorrect
     * @param clazz
     * @return
     */
    static List filterList(List correctOrIncorrect, Class<?> clazz) {
        List result = new ArrayList();
        for (Object o : correctOrIncorrect) {
            if (clazz.isInstance(o)) {
                result.add(o);
            }
        }
        return result;
    }

    /**
     * Return all instances of <code>clazz</code> from the given list.
     *
     * @param correctOrIncorrect
     * @param clazz
     * @return
     */
    static int[] getIndices(List correctOrIncorrect, Class<?> clazz) {
        List<Integer> selected = new ArrayList<Integer>();
        int idx = 0;
        for (Object o : correctOrIncorrect) {
            if (clazz.isInstance(o)) {
                selected.add(idx);
            }
            idx++;
        }
        return ArrayUtils.toPrimitive(selected.toArray(new Integer[selected.size()]));
    }


}
