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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.ComplexTaskletCorrectorImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefDAOImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.impl.ComplexTaskBuilderImpl;
import de.thorstenberger.taskmodel.complex.impl.ComplexTaskFactoryImpl;

/**
 * @author Steffen Dienst
 *
 */
public class ComplexTaskBuilderImplTest {

    private ComplexTaskFactoryImpl factory;
    private ComplexTaskBuilderImpl builder;
    private ComplexTaskDefDAOImpl dao;

    @Before
    public void setup() {
        this.factory = new ComplexTaskFactoryImpl(new ComplexTaskletCorrectorImpl());
        this.builder = new ComplexTaskBuilderImpl(factory);
        this.dao = new ComplexTaskDefDAOImpl(factory);
    }

    @Test
    public void shouldRecreateSameSubtasklets() throws TaskApiException {
        // given
        final long seed = System.nanoTime();
        int examCount = 20;
        List<Set<String>> chosenSubtasklets = new ArrayList<Set<String>>();
        // when
        while (examCount-- > 0) {
            Try t = builder.generateTry(dao.getComplexTaskDefRoot(loadFile("testexam.xml")), System.currentTimeMillis(), seed);
            chosenSubtasklets.add(new HashSet<String>(getSubtaskletIdsFrom(t)));
        }
        // then
        // sets of selected subtaskdef ids are all the same
        for (int i = 0; i < chosenSubtasklets.size() - 1; i++) {
            assertEquals(chosenSubtasklets.get(i),chosenSubtasklets.get(i+1));
        }
    }

    // TODO add more tests!

    private List<String> getSubtaskletIdsFrom(Try t) {
        List<String> res = new ArrayList<String>();
        for (Page p : t.getPages()) {
            for (SubTasklet st : p.getSubTasklets()) {
                res.add(st.getSubTaskDefId());
            }
        }
        return res;
    }
    private InputStream loadFile(String fileOnClasspath) {
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(fileOnClasspath);
        assertNotNull(ins);
        return ins;
    }

    @Test
    @Ignore
    public void should() {
        // given

        // when

        // then

    }

}
