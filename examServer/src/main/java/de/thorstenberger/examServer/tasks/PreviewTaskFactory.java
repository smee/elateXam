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
package de.thorstenberger.examServer.tasks;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;

/**
 * Implementaion of {@link TaskFactory} that does not persist tasklets. This makes it a readonly taskfactory.
 * 
 * @author Steffen Dienst
 * 
 */
public class PreviewTaskFactory extends TaskFactoryImpl {

    /**
     * @param examServerManager
     * @param userManager
     * @param taskDefDao
     * @param taskHandlingDao
     * @param complexTaskDefDAO
     * @param complexTaskHandlingDAO
     * @param complexTaskBuilder
     */
    public PreviewTaskFactory(ExamServerManager examServerManager, UserManager userManager, TaskDefDao taskDefDao,
            TaskHandlingDao taskHandlingDao, ComplexTaskDefDAO complexTaskDefDAO,
            UserComplexTaskHandlingDAO complexTaskHandlingDAO, ComplexTaskBuilder complexTaskBuilder) {
        super(examServerManager, userManager, taskDefDao, taskHandlingDao, complexTaskDefDAO, complexTaskHandlingDAO, complexTaskBuilder);
    }

    @Override
    public void storeTasklet(Tasklet tasklet) throws TaskApiException {
        // do nothing, we don't want to persist tasklets that are used for previewing only
    }

}
