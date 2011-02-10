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
package de.thorstenberger.examServer.dao.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet.AutoCorrection;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet.CorrectorAnnotation;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet.CorrectorHistory;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet.ManualCorrection;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskHandling.Tasklet.StudentAnnotation;
import de.thorstenberger.examServer.model.CorrectorTaskletAnnotationVO;
import de.thorstenberger.examServer.model.StudentTaskletAnnotationVO;
import de.thorstenberger.examServer.model.TaskletVO;
import de.thorstenberger.examServer.model.TaskletVO.ManualCorrectionsVO;
import de.thorstenberger.examServer.service.ExamServerManager;

/**
 * @author Thorsten Berger
 *
 */
public class TaskHandlingDaoImpl extends AbstractJAXBDao implements TaskHandlingDao {

  private TaskHandling taskHandling;

  private final Log log = LogFactory.getLog(TaskHandlingDaoImpl.class);

  /**
	 *
	 */
  public TaskHandlingDaoImpl(final ExamServerManager examServerManager) {
    super("de.thorstenberger.examServer.dao.xml.jaxb", examServerManager.getSystemDir(), "taskhandling.xml");

    if (!existsWorkingFile()) {
      taskHandling = objectFactory.createTaskHandling();
      taskHandling.setIdCount(0);
      save(taskHandling);
      return;
    } else {
      taskHandling = (TaskHandling) load();
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#getTasklet(long, java.lang.String)
   */
  public TaskletVO getTasklet(final long taskId, final String login) {

    Tasklet tt = null;

    synchronized (taskHandling.getTasklet()) {

      final Iterator it = taskHandling.getTasklet().iterator();
      while (it.hasNext()) {
        final Tasklet tasklet = (Tasklet) it.next();

        if (tasklet.getTaskDefId() == taskId && tasklet.getLogin().equals(login)) {
          tt = tasklet;
          break;
        }
      }
    }

    if (tt != null)
      return instantiateTaskletVO(tt);

    return null;

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#getTasklets()
   */
  public List<TaskletVO> getTasklets() {
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#getTasklets(long)
   */
  public List<TaskletVO> getTasklets(final long taskId) {

    final List<Tasklet> matchingTasklets = new ArrayList<Tasklet>();

    synchronized (taskHandling.getTasklet()) {

      final Iterator it = taskHandling.getTasklet().iterator();
      while (it.hasNext()) {
        final Tasklet tasklet = (Tasklet) it.next();

        if (tasklet.getTaskDefId() == taskId) {
          matchingTasklets.add(tasklet);
        }

      }
    }

    final List<TaskletVO> ret = new ArrayList<TaskletVO>();

    for (final Tasklet taskletType : matchingTasklets) {
      ret.add(instantiateTaskletVO(taskletType));
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#getTasklets(java.lang.String)
   */
  public List<TaskletVO> getTasklets(final String login) {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#getUserIdsOfAvailableTasklets(long)
   */
  public List<String> getUserIdsOfAvailableTasklets(final long taskId) {
    final List<String> ret = new ArrayList<String>();

    synchronized (taskHandling.getTasklet()) {
      final Iterator it = taskHandling.getTasklet().iterator();
      while (it.hasNext()) {
        final Tasklet taskletType = (Tasklet) it.next();
        if (taskletType.getTaskDefId() == taskId) {
          ret.add(taskletType.getLogin());
        }
      }
    }

    return ret;
  }

  public List<String> getUserIdsOfTaskletsAssignedToCorrector(final long taskId, final String correctorId) {
    if (correctorId == null)
      throw new NullPointerException();

    final List<String> ret = new ArrayList<String>();
    synchronized (taskHandling.getTasklet()) {

      final Iterator it = taskHandling.getTasklet().iterator();
      while (it.hasNext()) {
        final Tasklet taskletType = (Tasklet) it.next();
        if (taskletType.getTaskDefId() == taskId && correctorId.equals(taskletType.getAssignedCorrector())) {
          ret.add(taskletType.getLogin());
        }
      }

    }
    return ret;
  }

  private TaskletVO instantiateTaskletVO(final Tasklet tt) {
    final TaskletVO ret = new TaskletVO();
    ret.setTaskDefId(tt.getTaskDefId()); //
    ret.setStatus(tt.getStatus()); //
    ret.setLogin(tt.getLogin()); //
    ret.setId(tt.getId()); //
    ret.setCorrectorLogin(tt.getAssignedCorrector()); //

    // set corrections (points)
    ret.setAutoCorrectionPoints(tt.isSetAutoCorrection() ? tt.getAutoCorrection().getPoints() : null);
    final List<ManualCorrectionsVO> mvos = new LinkedList<ManualCorrectionsVO>();
    final List<ManualCorrection> mcs = tt.getManualCorrection();
    for (final ManualCorrection mc : mcs) {
      mvos.add(ret.new ManualCorrectionsVO(mc.getCorrector(), mc.getPoints()));
    }
    ret.setManualCorrections(mvos);

    // annotations
    final List<CorrectorAnnotation> cas = tt.getCorrectorAnnotation();
    final List<CorrectorTaskletAnnotationVO> correctorAnnotationVOs = new LinkedList<CorrectorTaskletAnnotationVO>();
    for (final CorrectorAnnotation ca : cas) {
      correctorAnnotationVOs.add(new CorrectorTaskletAnnotationVO(ca.getCorrector(), ca.getValue()));
    }
    ret.setCorrectorAnnotations(correctorAnnotationVOs);

    final List<StudentAnnotation> sas = tt.getStudentAnnotation();
    final List<StudentTaskletAnnotationVO> studentAnnotationVOs = new LinkedList<StudentTaskletAnnotationVO>();
    for (final StudentAnnotation sa : sas) {
      studentAnnotationVOs.add(new StudentTaskletAnnotationVO(sa.getValue(), sa.getDate(), sa.isAcknowledged()));
    }
    ret.setStudentAnnotations(studentAnnotationVOs);

    // flags
    final List<String> fs = tt.getFlag();
    final List<String> flags = new LinkedList<String>();
    for (final String f : fs) {
      flags.add(f);
    }
    ret.setFlags(flags);

    // correctors history
    final List<CorrectorHistory> ch = tt.getCorrectorHistory();
    final List<String> correctors = new LinkedList<String>();
    for (final CorrectorHistory c : ch) {
      correctors.add(c.getCorrector());
    }
    ret.setCorrectorHistory(correctors);

    return ret;

  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.examServer.dao.TaskHandlingDao#saveTasklet(de.thorstenberger.examServer.model.TaskletVO)
   */
	synchronized public void saveTasklet(final TaskletVO taskletVO) {

		Tasklet taskletType = null;

		final Iterator it = taskHandling.getTasklet().iterator();
		while (it.hasNext()) {
			final Tasklet tempTaskletType = (Tasklet) it.next();

			if (tempTaskletType.getTaskDefId() == taskletVO.getTaskDefId()
					&& tempTaskletType.getLogin().equals(taskletVO.getLogin())) {
				taskletType = tempTaskletType;
				break;
			}
		}

		if (taskletType == null) {
			taskletType = objectFactory.createTaskHandlingTasklet();
			// FIXME handle overflow
			synchronized (taskHandling) {
				taskHandling.setIdCount(taskHandling.getIdCount() + 1);
			}
			taskletType.setId(taskHandling.getIdCount());
			taskletType.setTaskDefId(taskletVO.getTaskDefId());
			taskletType.setLogin(taskletVO.getLogin());

			taskHandling.getTasklet().add(taskletType);

		}

		taskletType.setStatus(taskletVO.getStatus());
		taskletType.setAssignedCorrector(taskletVO.getCorrectorLogin());

		// set corrections (points)
		if (taskletType.isSetAutoCorrection()) {
			if (taskletVO.getAutoCorrectionPoints() == null) {
				taskletType.setAutoCorrection(null);
			} else {
				taskletType.getAutoCorrection().setPoints(
						taskletVO.getAutoCorrectionPoints());
			}
		} else {
			if (taskletVO.getAutoCorrectionPoints() != null) {
				AutoCorrection act = objectFactory
						.createTaskHandlingTaskletAutoCorrection();

				act.setPoints(taskletVO.getAutoCorrectionPoints());
				taskletType.setAutoCorrection(act);
			}
		}
		if (taskletType.isSetManualCorrection()) {
			taskletType.getManualCorrection().clear();
		}
		if (taskletVO.getManualCorrections() != null) {
			for (final ManualCorrectionsVO mcvo : taskletVO
					.getManualCorrections()) {
				ManualCorrection mct = objectFactory
						.createTaskHandlingTaskletManualCorrection();
				mct.setCorrector(mcvo.getCorrector());
				mct.setPoints(mcvo.getPoints());
				taskletType.getManualCorrection().add(mct);
			}
		}

		// the annotations
		taskletType.getCorrectorAnnotation().clear();
		for (final CorrectorTaskletAnnotationVO tavo : taskletVO
				.getCorrectorAnnotations()) {
			CorrectorAnnotation cat = objectFactory
					.createTaskHandlingTaskletCorrectorAnnotation();
			cat.setValue(tavo.getText() == null ? "" : tavo.getText());
			cat.setCorrector(tavo.getCorrector());
			taskletType.getCorrectorAnnotation().add(cat);
		}
		taskletType.getStudentAnnotation().clear();
		for (final StudentTaskletAnnotationVO tavo : taskletVO
				.getStudentAnnotations()) {
			StudentAnnotation sat = objectFactory
					.createTaskHandlingTaskletStudentAnnotation();
			sat.setDate(tavo.getDate());
			sat.setValue(tavo.getText());
			sat.setAcknowledged(tavo.isAcknowledged());
			taskletType.getStudentAnnotation().add(sat);
		}

		// flags
		taskletType.getFlag().clear();
		taskletType.getFlag().addAll(taskletVO.getFlags());

		// correctors history
		taskletType.getCorrectorHistory().clear();

		if (taskletVO.getCorrectorHistory() != null
				&& taskletVO.getCorrectorHistory().size() > 0) {
			final List<String> ch = taskletVO.getCorrectorHistory();
			for (final String c : ch) {
				CorrectorHistory cht = objectFactory
						.createTaskHandlingTaskletCorrectorHistory();
				cht.setCorrector(c);
				taskletType.getCorrectorHistory().add(cht);
			}
		}

		save(taskHandling);

	}

}
