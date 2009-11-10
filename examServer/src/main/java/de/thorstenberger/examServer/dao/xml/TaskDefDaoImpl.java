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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.JAXBException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefs;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefsType.TaskDefType;
import de.thorstenberger.examServer.dao.xml.jaxb.TaskDefsType.TaskDefType.ComplexTaskDefType;
import de.thorstenberger.examServer.model.TaskDefVO;
import de.thorstenberger.examServer.service.ExamServerManager;

/**
 * 
 * TODO add mutator methods and synchronize
 * 
 * @author Thorsten Berger
 * 
 */
public class TaskDefDaoImpl extends AbstractJAXBDao implements TaskDefDao {

    private TaskDefs taskDefs;
    private AtomicLong crntId;

    private final Log log = LogFactory.getLog(TaskDefDaoImpl.class);

    /**
	 *
	 */
    public TaskDefDaoImpl(final ExamServerManager examServerManager) {
        super("de.thorstenberger.examServer.dao.xml.jaxb", examServerManager.getSystemDir(), "taskdefs.xml");

        try { // JAXBException

            if (!existsWorkingFile()) {
                taskDefs = objectFactory.createTaskDefs();
                this.crntId = new AtomicLong(0);
                save(taskDefs);
                return;
            } else {
                taskDefs = (TaskDefs) load();
                this.crntId = new AtomicLong(findMostRecentId(taskDefs));
            }

        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    private long findMostRecentId(final TaskDefs tds) {
        long max = -1;
        final Iterator it = tds.getTaskDef().iterator();
        while (it.hasNext()) {
            final TaskDefType taskDef = (TaskDefType) it.next();
            max = Math.max(max, taskDef.getId());
        }
        return max;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.thorstenberger.examServer.tasks.TaskDefDao#getTaskDef(long)
     */
    public TaskDefVO getTaskDef(final long id) {

        final Iterator it = taskDefs.getTaskDef().iterator();
        while (it.hasNext()) {
            final TaskDefType taskDef = (TaskDefType) it.next();

            if (taskDef.getId() == id) {

                final TaskDefVO taskDefVO = new TaskDefVO();
                taskDefVO.setType("complex");
                taskDefVO.setTitle(taskDef.getTitle());
                taskDefVO.setShortDescription(taskDef.getShortDescription());
                taskDefVO.setStopped(taskDef.isStopped());
                taskDefVO.setId(taskDef.getId());
                taskDefVO.setDeadline(taskDef.getDeadline() == 0 ? null : taskDef.getDeadline());
                taskDefVO.setVisible(taskDef.isVisible());
                if (taskDef.isSetFollowingTaskId()) {
                    taskDefVO.setFollowingTaskId(taskDef.getFollowingTaskId());
                }

                taskDefVO.setShowSolutionToStudents(taskDef.getComplexTaskDef().isShowSolutionToStudents());
                taskDefVO.setComplexTaskFile(taskDef.getComplexTaskDef().getComplexTaskFile());

                return taskDefVO;

            }

        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.thorstenberger.examServer.tasks.TaskDefDao#getTaskDefs()
     */
    public List<TaskDefVO> getTaskDefs() {

        final List<TaskDefVO> ret = new ArrayList<TaskDefVO>();

        final Iterator it = taskDefs.getTaskDef().iterator();
        while (it.hasNext()) {
            final TaskDefType taskDef = (TaskDefType) it.next();

            final TaskDefVO taskDefVO = new TaskDefVO();
            taskDefVO.setType("complex");
            taskDefVO.setTitle(taskDef.getTitle());
            taskDefVO.setShortDescription(taskDef.getShortDescription());
            taskDefVO.setStopped(taskDef.isStopped());
            taskDefVO.setId(taskDef.getId());
            taskDefVO.setDeadline(taskDef.getDeadline() == 0 ? null : taskDef.getDeadline());
            taskDefVO.setVisible(taskDef.isVisible());
            if (taskDef.isSetFollowingTaskId()) {
                taskDefVO.setFollowingTaskId(taskDef.getFollowingTaskId());
            }

            taskDefVO.setShowSolutionToStudents(taskDef.getComplexTaskDef().isShowSolutionToStudents());
            taskDefVO.setComplexTaskFile(taskDef.getComplexTaskDef().getComplexTaskFile());

            ret.add(taskDefVO);

        }
        return ret;

    }

    public TaskDefVO storeTaskDef(final TaskDefVO td) {
        if (td.getId() >= 0) {
            // update
            final Iterator it = taskDefs.getTaskDef().iterator();
            while (it.hasNext()) {
                final TaskDefType taskDef = (TaskDefType) it.next();
                if (taskDef.getId() == td.getId()) {
                    try {
                        BeanUtils.copyProperties(taskDef, td);
                        taskDef.getComplexTaskDef().setShowSolutionToStudents(td.isShowSolutionToStudents());
                    } catch (final Exception e) {
                        log.error("Exception on BeanUtils.copyProperties().", e);
                    }
                    break;
                }
            }
        } else {
            // set new id
            td.setId(this.crntId.incrementAndGet());
            try {

                final TaskDefType tdt = objectFactory.createTaskDefsTypeTaskDefType();
                BeanUtils.copyProperties(tdt, td);

                final ComplexTaskDefType ctdt = objectFactory.createTaskDefsTypeTaskDefTypeComplexTaskDefType();
                ctdt.setComplexTaskFile(td.getComplexTaskFile());
                ctdt.setShowSolutionToStudents(td.isShowSolutionToStudents());
                tdt.setComplexTaskDef(ctdt);

                taskDefs.getTaskDef().add(tdt);
            } catch (final JAXBException e) {
                throw new RuntimeException(e);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        save(taskDefs);
        return td;
    }
}
