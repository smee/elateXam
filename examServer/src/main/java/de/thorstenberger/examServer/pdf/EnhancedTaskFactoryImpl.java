/*

Copyright (C) 2009 Steffen Dienst

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
package de.thorstenberger.examServer.pdf;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO;
import de.thorstenberger.examServer.model.TaskletVO;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.examServer.tasks.TaskFactoryImpl;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.Tasklet.Status;
import de.thorstenberger.taskmodel.TaskmodelUtil;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;

/**
 * @author Steffen Dienst
 *
 */
public class EnhancedTaskFactoryImpl extends TaskFactoryImpl implements ApplicationContextAware {
  final private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
  private ApplicationContext applicationContext;
  /**
   * @param examServerManager
   * @param userManager
   * @param taskDefDao
   * @param taskHandlingDao
   * @param complexTaskDefDAO
   * @param complexTaskHandlingDAO
   * @param complexTaskBuilder
   */
  public EnhancedTaskFactoryImpl(final ExamServerManager examServerManager, final UserManager userManager, final TaskDefDao taskDefDao,
      final TaskHandlingDao taskHandlingDao, final ComplexTaskDefDAO complexTaskDefDAO, final UserComplexTaskHandlingDAO complexTaskHandlingDAO,
      final ComplexTaskBuilder complexTaskBuilder) {
    super(examServerManager, userManager, taskDefDao, taskHandlingDao, complexTaskDefDAO, complexTaskHandlingDAO, complexTaskBuilder);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.tasks.TaskFactoryImpl#onStoreTasklet(de.thorstenberger.taskmodel.Tasklet,
   * de.thorstenberger.examServer.model.TaskletVO)
   */
  @Override
  protected void onStoreTasklet(final Tasklet tasklet, final TaskletVO taskletVO) {
    boolean isComplexTasklet = tasklet instanceof ComplexTasklet;
    boolean isSolved = tasklet.hasOrPassedStatus(Status.SOLVED);
    boolean statusIncreased = TaskmodelUtil.getStatus(taskletVO.getStatus()).getOrder() < tasklet.getStatus().getOrder();
    boolean statusIsCorrected = tasklet.hasOrPassedStatus(Status.CORRECTED);

    if (isComplexTasklet && isSolved && (statusIncreased || statusIsCorrected)) {
      // if the status has changed AND is at least 'SOLVED' we should dump a signed
      // and timestamped pdf file.
      // do so in 5 seconds...
      exec.schedule(new RenderAndSignPDF(tasklet.getTaskId(), tasklet.getUserId(), applicationContext), 5, TimeUnit.SECONDS);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @seeorg.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
   * ApplicationContext)
   */
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
