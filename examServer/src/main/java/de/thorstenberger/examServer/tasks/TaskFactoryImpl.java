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
package de.thorstenberger.examServer.tasks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.thorstenberger.examServer.dao.TaskDefDao;
import de.thorstenberger.examServer.dao.TaskHandlingDao;
import de.thorstenberger.examServer.dao.UserComplexTaskHandlingDAO;
import de.thorstenberger.examServer.dao.xml.RoleAndLookupDaoImpl;
import de.thorstenberger.examServer.model.CorrectorTaskletAnnotationVO;
import de.thorstenberger.examServer.model.Role;
import de.thorstenberger.examServer.model.StudentTaskletAnnotationVO;
import de.thorstenberger.examServer.model.TaskDefVO;
import de.thorstenberger.examServer.model.TaskletVO;
import de.thorstenberger.examServer.model.TaskletVO.ManualCorrectionsVO;
import de.thorstenberger.examServer.model.User;
import de.thorstenberger.examServer.service.ExamServerManager;
import de.thorstenberger.examServer.service.UserManager;
import de.thorstenberger.taskmodel.CategoryFilter;
import de.thorstenberger.taskmodel.CorrectorAnnotation;
import de.thorstenberger.taskmodel.CorrectorAnnotationImpl;
import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.StudentAnnotation;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskCategory;
import de.thorstenberger.taskmodel.TaskContants;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskFactory;
import de.thorstenberger.taskmodel.TaskFilter;
import de.thorstenberger.taskmodel.TaskFilterException;
import de.thorstenberger.taskmodel.TaskManager.UserAttribute;
import de.thorstenberger.taskmodel.TaskModelPersistenceException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.TaskmodelUtil;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.complex.ComplexTaskBuilder;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.ComplexTaskletImpl;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.TaskDef_ComplexImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefDAO;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.impl.AbstractTaskFactory;
import de.thorstenberger.taskmodel.impl.ManualCorrectionImpl;
import de.thorstenberger.taskmodel.impl.StudentAnnotationImpl;
import de.thorstenberger.taskmodel.impl.TaskletCorrectionImpl;
import de.thorstenberger.taskmodel.impl.UserInfoImpl;

/**
 * @author Thorsten Berger FIXME: move caching into TaskManagerImpl of Taskmodel-core
 */
public class TaskFactoryImpl extends AbstractTaskFactory implements TaskFactory {

    public class UserAttributeImpl implements UserAttribute {

        private final String key;
        private final String name;

        /**
         * @param key
         * @param name
         */
        public UserAttributeImpl(final String key, final String name) {
            super();
            this.key = key;
            this.name = name;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @return the name
         */
        public String getName(final Locale locale) {
            return name;
        }

    }

    public static final String USER_ATTRIBUTE_SEMESTER = "user.student-info.semester";

    private final List<String> availableTypes;

    private final ExamServerManager examServerManager;

    private final UserManager userManager;

    private final TaskDefDao taskDefDao;

    private final TaskHandlingDao taskHandlingDao;
    private final ComplexTaskDefDAO complexTaskDefDAO;

    private final ComplexTaskBuilder complexTaskBuilder;
    private final Log log = LogFactory.getLog("TaskLogger");
    private List<TaskDef> taskDefCache = null;

		private final UserComplexTaskHandlingDAO complexTaskHandlingDAO;

    /**
     *
     */
    public TaskFactoryImpl(final ExamServerManager examServerManager, final UserManager userManager, final TaskDefDao taskDefDao,
            final TaskHandlingDao taskHandlingDao, final ComplexTaskDefDAO complexTaskDefDAO,
            final UserComplexTaskHandlingDAO complexTaskHandlingDAO, final ComplexTaskBuilder complexTaskBuilder) {

        this.examServerManager = examServerManager;
        this.userManager = userManager;

        this.taskDefDao = taskDefDao;
        this.complexTaskDefDAO = complexTaskDefDAO;
        this.taskHandlingDao = taskHandlingDao;
        this.complexTaskHandlingDAO = complexTaskHandlingDAO;
        this.complexTaskBuilder = complexTaskBuilder;

        availableTypes = new ArrayList<String>();
        availableTypes.add(TaskContants.TYPE_COMPLEX);

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#addTaskCategory(java.lang.String, java.lang.String)
     */
    public TaskCategory addTaskCategory(final String name, final String description) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#availableTypes()
     */
    public List<String> availableTypes() {
        return availableTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#availableUserAttributeKeys()
     */
    public List<UserAttribute> availableUserAttributes() {
        final List<UserAttribute> ret = new LinkedList<UserAttribute>();
        ret.add(new UserAttributeImpl(USER_ATTRIBUTE_SEMESTER, "Semester")); // TODO externalize String
        return ret;
    }

    private List<CorrectorTaskletAnnotationVO> copyCorrectorAnnotations(final List<CorrectorAnnotation> annotations) {
        final List<CorrectorTaskletAnnotationVO> ret = new LinkedList<CorrectorTaskletAnnotationVO>();
        for (final CorrectorAnnotation a : annotations) {
            ret.add(new CorrectorTaskletAnnotationVO(a.getCorrector(), a.getText()));
        }
        return ret;
    }

    private List<ManualCorrectionsVO> copyManualCorrections(final TaskletVO taskletVO, final List<ManualCorrection> manualCorrections) {
        final List<ManualCorrectionsVO> ret = new LinkedList<ManualCorrectionsVO>();
        for (final ManualCorrection mc : manualCorrections) {
            ret.add(taskletVO.new ManualCorrectionsVO(mc.getCorrector(), mc.getPoints()));
        }
        return ret;
    }

    private List<StudentTaskletAnnotationVO> copyStudentAnnotations(final List<StudentAnnotation> annotations) {
        final List<StudentTaskletAnnotationVO> ret = new LinkedList<StudentTaskletAnnotationVO>();
        for (final StudentAnnotation a : annotations) {
            ret.add(new StudentTaskletAnnotationVO(a.getText(), a.getDate(), a.isAcknowledged()));
        }
        return ret;
    }

    /**
     * Creates pathname for taskDef files
     *
     * @param filename
     * @return
     */
    private String createPath(final String filename) {
        return new File(examServerManager.getTaskDefDir(), filename).getAbsolutePath();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#createTasklet(java.lang.String, long)
     */
    public Tasklet createTasklet(final String userId, final long taskId)
    throws TaskApiException {

        TaskletVO taskletVO = taskHandlingDao.getTasklet(taskId, userId);
        final TaskDefVO taskDefVO = taskDefDao.getTaskDef(taskId);

        if (taskDefVO == null)
            throw new TaskApiException("TaskDef " + taskId + " does not exist!");

        if (taskletVO != null)
            throw new TaskApiException("Tasklet (" + userId + ", " + taskId + ") does already exist!");

        taskletVO = new TaskletVO();
        taskletVO.setLogin(userId);
        taskletVO.setTaskDefId(taskId);
        taskletVO.setStatus(Tasklet.Status.INITIALIZED.getValue());
        taskletVO.setAutoCorrectionPoints(null);
        taskletVO.setFlags(new LinkedList<String>());
        taskletVO.setStudentAnnotations(new LinkedList<StudentTaskletAnnotationVO>());
        taskletVO.setCorrectorAnnotations(new LinkedList<CorrectorTaskletAnnotationVO>());
        taskletVO.setManualCorrections(new LinkedList<ManualCorrectionsVO>());

        taskHandlingDao.saveTasklet(taskletVO);

        return instantiateTasklet(taskletVO, taskDefVO, userId, taskId);

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskCategory(long)
     */
    public void deleteTaskCategory(final long id) throws MethodNotSupportedException {
        // TODO Auto-generated method stub
        throw new MethodNotSupportedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#deleteTaskDef(long)
     */
    synchronized public void deleteTaskDef(final long id) throws MethodNotSupportedException {
        final TaskDefVO tdvo = taskDefDao.getTaskDef(id);
        if (tdvo != null) {
            tdvo.setVisible(false); // make invisible rather than delete physically
            taskDefDao.storeTaskDef(tdvo);
            this.taskDefCache = null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getCategories()
     */
    public List<TaskCategory> getCategories() {
        throw new MethodNotSupportedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getCategories(de.thorstenberger.taskmodel.CategoryFilter)
     */
    public List<TaskCategory> getCategories(final CategoryFilter categoryFilter) {
        throw new MethodNotSupportedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getCategory(long)
     */
    public TaskCategory getCategory(final long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getCorrectors()
     */
    public List<UserInfo> getCorrectors() {
        final List<UserInfo> ret = new LinkedList<UserInfo>();
        User uLookup = new User();
        uLookup.addRole(new Role(RoleAndLookupDaoImpl.TUTOR));
        final List<User> users = userManager.getUsers(uLookup);
        uLookup = new User();
        uLookup.addRole(new Role(RoleAndLookupDaoImpl.ADMIN));

        final List<User> adminUsers = userManager.getUsers(uLookup);
        for (final User u : adminUsers) {
            if (!users.contains(u)) {
                users.add(u);
            }
        }

        for (final User u : users) {
            // ignore invalid/locked tutors
            if(u.isAccountExpired() || u.isAccountLocked()) {
                continue;
            }

            final UserInfoImpl ui = new UserInfoImpl();
            ui.setLogin(u.getUsername());
            ui.setFirstName(u.getFirstName());
            ui.setName(u.getLastName());
            ui.setEMail(u.getEmail());
            ui.setUserAttribute(USER_ATTRIBUTE_SEMESTER, u.getPhoneNumber());
            ret.add(ui);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDef(long)
     */
    public synchronized TaskDef getTaskDef(final long taskId) {

        final List<TaskDef> taskDefs = getTaskDefs();
        final Iterator it = taskDefs.iterator();
        while (it.hasNext()) {
            final TaskDef td = (TaskDef) it.next();
            if (td.getId() == taskId)
                return td;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs()
     */
    public synchronized List<TaskDef> getTaskDefs() {

        if (taskDefCache == null) {

            final List<TaskDefVO> taskDefVOs = taskDefDao.getTaskDefs();

            taskDefCache = new ArrayList<TaskDef>();

            for (final TaskDefVO t : taskDefVOs) {

                TaskDef_ComplexImpl tdci;
                try {
                    tdci = new TaskDef_ComplexImpl(t.getId(), t.getTitle(),
                            t.getShortDescription(), t.getDeadline(), t.isStopped(), t.getFollowingTaskId(), complexTaskDefDAO,
                            new FileInputStream(createPath(t.getComplexTaskFile())),
                            t.isShowSolutionToStudents(), t.isVisible());
                } catch (final FileNotFoundException e) {
                    throw new TaskModelPersistenceException(e);
                }
                taskDefCache.add(tdci);

            }

        }

        return taskDefCache;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getTaskDefs(de.thorstenberger.taskmodel.TaskFilter)
     */
    public List<TaskDef> getTaskDefs(final TaskFilter filter)
    throws TaskFilterException {
        throw new MethodNotSupportedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getTasklet(java.lang.String, long)
     */
    public Tasklet getTasklet(final String userId, final long taskId) {

        final TaskletVO taskletVO = taskHandlingDao.getTasklet(taskId, userId);
        if (taskletVO == null)
            return null;
        final TaskDefVO taskDefVO = taskDefDao.getTaskDef(taskId);
        if (taskDefVO == null)
            throw new RuntimeException("No corresponding taskDef found: " + taskId);


        return instantiateTasklet(taskletVO, taskDefVO, userId, taskId);

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getTasklets(long)
     */
    public List<Tasklet> getTasklets(final long taskId) {
    return getTasklets(taskId, null);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.thorstenberger.taskmodel.TaskFactory#getTasklets(long, de.thorstenberger.taskmodel.Tasklet.Status)
   */
  public List<Tasklet> getTasklets(final long taskId, final Tasklet.Status status) {
    final List<Tasklet> ret = new ArrayList<Tasklet>();
    final List<TaskletVO> taskletVOs = taskHandlingDao.getTasklets(taskId);
    final TaskDefVO taskDefVO = taskDefDao.getTaskDef(taskId);

    for (final TaskletVO taskletVO : taskletVOs) {
      // filter tasklets by status if there is one given
      if (status != null && TaskmodelUtil.getStatus(taskletVO.getStatus()) != status) {
        continue;
      }

      final String userId = taskletVO.getLogin();

      ret.add(instantiateTasklet(taskletVO, taskDefVO, userId, taskId));
    }

    return ret;
  }


    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.impl.AbstractTaskFactory#getUserIdsOfAvailableTasklets(long)
     */
    @Override
    public List<String> getUserIdsOfAvailableTasklets(final long taskId) {
        return taskHandlingDao.getUserIdsOfAvailableTasklets(taskId);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.impl.AbstractTaskFactory#getUserIdsOfTaskletsAssignedToCorrector(long,
     * java.lang.String, boolean)
     */
    @Override
    public List<String> getUserIdsOfTaskletsAssignedToCorrector(final long taskId, final String correctorId) {
        return taskHandlingDao.getUserIdsOfTaskletsAssignedToCorrector(taskId, correctorId);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#getUserInfo(java.lang.String)
     */
    public UserInfo getUserInfo(final String login) {

        User user;

        try {
            user = userManager.getUserByUsername(login);
        } catch (final UsernameNotFoundException e) {
            return null;
        }

        final UserInfoImpl ret = new UserInfoImpl();
        ret.setLogin(user.getUsername());
        ret.setFirstName(user.getFirstName());
        ret.setName(user.getLastName());
        ret.setEMail(user.getEmail());
        ret.setUserAttribute(USER_ATTRIBUTE_SEMESTER, user.getPhoneNumber());

        return ret;

    }

    private Tasklet instantiateTasklet(final TaskletVO taskletVO, final TaskDefVO taskDefVO, final String userId, final long taskId) {

        // corrector annotations
        final List<CorrectorAnnotation> cas = new LinkedList<CorrectorAnnotation>();
        final List<CorrectorTaskletAnnotationVO> ctavos = taskletVO.getCorrectorAnnotations();
        if (ctavos != null && ctavos.size() > 0) {
            for (final CorrectorTaskletAnnotationVO ctavo : ctavos) {
                cas.add(new CorrectorAnnotationImpl(ctavo.getCorrector(), ctavo.getText()));
            }
        }
        // student annotations
        final List<StudentAnnotation> studentAnnotations = new ArrayList<StudentAnnotation>();
        for (final StudentTaskletAnnotationVO tavo : taskletVO.getStudentAnnotations()) {
            studentAnnotations.add(new StudentAnnotationImpl(tavo.getText(), tavo.getDate(), tavo.isAcknowledged()));
        }

        // manual corrections
        final List<ManualCorrection> mcs = new LinkedList<ManualCorrection>();
        final List<ManualCorrectionsVO> mcvos = taskletVO.getManualCorrections();
        if (mcvos != null && mcvos.size() > 0) {
            for (final ManualCorrectionsVO mcvo : mcvos) {
                mcs.add(new ManualCorrectionImpl(mcvo.getCorrector(), mcvo.getPoints()));
            }
        }

        final TaskletCorrection correction =
            new TaskletCorrectionImpl(taskletVO.getAutoCorrectionPoints(), cas,
                    taskletVO.getCorrectorLogin(), taskletVO.getCorrectorHistory(), studentAnnotations, mcs);

        final TaskDef_Complex complexTaskDef = (TaskDef_Complex) getTaskDef(taskDefVO.getId());
        final ComplexTasklet tasklet =
                new ComplexTaskletImpl(this, complexTaskBuilder, taskletVO.getLogin(), complexTaskDef, complexTaskHandlingDAO.load(userId, Long.toString(taskId), complexTaskDef.getComplexTaskDefRoot()), TaskmodelUtil.getStatus(taskletVO.getStatus()), taskletVO.getFlags(), correction, new HashMap<String, String>());
        // new ComplexTaskletImpl(this, complexTaskBuilder, taskletVO.getLogin(), (TaskDef_Complex)
        // getTaskDef(taskDefVO.getId()),
        // TaskmodelUtil.getStatus(taskletVO.getStatus()), taskletVO.getFlags(), correction, complexTaskHandlingDAO,
        // fis, new HashMap<String, String>());

        return tasklet;

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String, de.thorstenberger.taskmodel.Tasklet,
     * java.lang.String)
     */
    public void logPostData(final String msg, final Tasklet tasklet, final String ip) {
        final String prefix = tasklet.getUserId() + "@" + ip + ": ";
        log.info(prefix + msg);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#logPostData(java.lang.String, java.lang.Throwable,
     * de.thorstenberger.taskmodel.Tasklet, java.lang.String)
     */
    public void logPostData(final String msg, final Throwable throwable, final Tasklet tasklet, final String ip) {
        final String prefix = tasklet.getUserId() + "@" + ip + ": ";
        log.info(prefix + msg, throwable);
    }

    private boolean objectsDiffer(final Object a, final Object b) {
        if (a == null && b == null)
            return false;
        if (a == null && b != null)
            return true;
        if (b == null && a != null)
            return true;

        return !a.equals(b);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#removeTasklet(java.lang.String, long)
     */
    public void removeTasklet(final String userId, final long taskId)
    throws TaskApiException {
        throw new MethodNotSupportedException();
    }

    /**
     * Stores a new taskDef via DAO. Returns its id.
     *
     * @param filename
     * @param fileContent
     * @throws TaskApiException
     */
    synchronized public long storeNewTaskDef(final String filename, final byte[] fileContent) throws TaskApiException {
        try {
            this.taskDefCache = null;
            String title = "[new task]";
            // throw exception if the file is no valid taskDef
            try {
                final ByteArrayInputStream bis = new ByteArrayInputStream(fileContent);
                ComplexTaskDefRoot complexTaskDefRoot = complexTaskDefDAO.getComplexTaskDefRoot(bis);
                title = complexTaskDefRoot.getTitle();
            } catch (final TaskApiException e) {
                throw new TaskApiException("Invalid taskDef format.", e);
            }

            // write to file system
            File file = new File(createPath(filename));
            int idx = 0;
            // if file exists, create a new unique name for it
            while (file.exists()) {
                file = new File(createPath(filename + "." + idx));
                idx++;
            }
            final FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileContent);
            fos.close();

            final TaskDefVO td = new TaskDefVO();
            td.setComplexTaskFile(file.getName());
            td.setTitle(title);
            td.setType(TaskContants.TYPE_COMPLEX);
            td.setStopped(true);
            td.setVisible(true);
            td.setShowSolutionToStudents(false);

            return taskDefDao.storeTaskDef(td).getId();

        } catch (final IOException e) {
            log.error("Error on persisting new taskDef.", e);
            throw new TaskApiException("Error on persisting new taskDef", e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#storeTaskCategory(de.thorstenberger.taskmodel.TaskCategory)
     */
    public void storeTaskCategory(final TaskCategory category) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#storeTaskDef(de.thorstenberger.taskmodel.TaskDef, long)
     */
    synchronized public void storeTaskDef(final TaskDef taskDef, final long taskCategoryId) throws TaskApiException {
        if (taskDef instanceof TaskDef_Complex) {
            this.taskDefCache = null;

            TaskDefVO tdvo = taskDefDao.getTaskDef(taskDef.getId());
            if (tdvo == null) {
                tdvo = new TaskDefVO();
                tdvo.setType(TaskContants.TYPE_COMPLEX);// FIXME
                // tdvo.setComplexTaskFile( ??? );
            }
            tdvo.setId(taskDef.getId());
            tdvo.setTitle(taskDef.getTitle());
            tdvo.setStopped(taskDef.isStopped());
            tdvo.setShowSolutionToStudents(((TaskDef_Complex) taskDef).isShowCorrectionToUsers());
            tdvo.setShortDescription(taskDef.getShortDescription());
            taskDefDao.storeTaskDef(tdvo);

        } else
            throw new TaskApiException("Unsupported task of type \"" + taskDef.getType() + "\".");
    }

    /*
     * (non-Javadoc)
     *
     * @see de.thorstenberger.taskmodel.TaskFactory#storeTasklet(de.thorstenberger.taskmodel.Tasklet)
     */
    public void storeTasklet(final Tasklet tasklet) throws TaskApiException {

        TaskletVO taskletVO = taskHandlingDao.getTasklet(tasklet.getTaskId(), tasklet.getUserId());

        onStoreTasklet(tasklet,taskletVO);

        boolean changed = false;

        if (taskletVO == null) {
            // potential NPE, see lines below
            // but has no influence due to createTasklet()
            taskletVO = new TaskletVO();
            changed = true;
        }

        if (taskletVO.getTaskDefId() != tasklet.getTaskId()) {
            taskletVO.setTaskDefId(tasklet.getTaskId());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getLogin(), tasklet.getUserId())) {
            taskletVO.setLogin(tasklet.getUserId());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getStatus(), tasklet.getStatus().getValue())) {
            taskletVO.setStatus(tasklet.getStatus().getValue());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getCorrectorLogin(), tasklet.getTaskletCorrection().getCorrector())) {
            taskletVO.setCorrectorLogin(tasklet.getTaskletCorrection().getCorrector());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getAutoCorrectionPoints(), tasklet.getTaskletCorrection().getAutoCorrectionPoints())) {
            taskletVO.setAutoCorrectionPoints(tasklet.getTaskletCorrection().getAutoCorrectionPoints());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getCorrectorHistory(), tasklet.getTaskletCorrection().getCorrectorHistory())) {
            taskletVO.setCorrectorHistory(tasklet.getTaskletCorrection().getCorrectorHistory());
            changed = true;
        }

        if (objectsDiffer(taskletVO.getFlags(), tasklet.getFlags())) {
            taskletVO.setFlags(tasklet.getFlags());
            changed = true;
        }

        // student annotations
        if (taskletVO.getStudentAnnotations().size() != tasklet.getTaskletCorrection().getStudentAnnotations().size()) {
            taskletVO.setStudentAnnotations(copyStudentAnnotations(tasklet.getTaskletCorrection().getStudentAnnotations()));
            changed = true;
        } else {
            for (int i = 0; i < tasklet.getTaskletCorrection().getStudentAnnotations().size(); i++) {
                final StudentAnnotation a = tasklet.getTaskletCorrection().getStudentAnnotations().get(i);
                final StudentTaskletAnnotationVO tavo = taskletVO.getStudentAnnotations().get(i);
                if (objectsDiffer(a.getText(), tavo.getText()) || objectsDiffer(a.getDate(), tavo.getDate())
                        || objectsDiffer(a.isAcknowledged(), tavo.isAcknowledged())) {
                    taskletVO.setStudentAnnotations(copyStudentAnnotations(tasklet.getTaskletCorrection().getStudentAnnotations()));
                    changed = true;
                    break;
                }
            }
        }

        // corrector annotations
        if (taskletVO.getCorrectorAnnotations().size() != tasklet.getTaskletCorrection().getCorrectorAnnotations().size()) {
            taskletVO.setCorrectorAnnotations(copyCorrectorAnnotations(tasklet.getTaskletCorrection().getCorrectorAnnotations()));
            changed = true;
        } else {
            for (int i = 0; i < tasklet.getTaskletCorrection().getCorrectorAnnotations().size(); i++) {
                final CorrectorAnnotation a = tasklet.getTaskletCorrection().getCorrectorAnnotations().get(i);
                final CorrectorTaskletAnnotationVO tavo = taskletVO.getCorrectorAnnotations().get(i);
                if (objectsDiffer(a.getText(), tavo.getText()) || objectsDiffer(a.getCorrector(), tavo.getCorrector())) {
                    taskletVO.setCorrectorAnnotations(copyCorrectorAnnotations(tasklet.getTaskletCorrection().getCorrectorAnnotations()));
                    changed = true;
                    break;
                }
            }
        }

        // manual corrections
        if (taskletVO.getManualCorrections().size() != tasklet.getTaskletCorrection().getManualCorrections().size()) {
            taskletVO.setManualCorrections(copyManualCorrections(taskletVO, tasklet.getTaskletCorrection().getManualCorrections()));
            changed = true;
        } else {
            for (int i = 0; i < tasklet.getTaskletCorrection().getManualCorrections().size(); i++) {
                final ManualCorrection m = tasklet.getTaskletCorrection().getManualCorrections().get(i);
                final ManualCorrectionsVO mcvo = taskletVO.getManualCorrections().get(i);
                if (objectsDiffer(m.getCorrector(), mcvo.getCorrector()) || objectsDiffer(m.getCorrector(), m.getPoints())) {
                    taskletVO.setManualCorrections(copyManualCorrections(taskletVO, tasklet.getTaskletCorrection().getManualCorrections()));
                    changed = true;
                    break;
                }
            }
        }

        if (tasklet instanceof ComplexTasklet) {
            // TODO introduce transactions
            // get the taskHandling xml file!
        	  final String userId = tasklet.getUserId();
        	  final String taskId = Long.toString(tasklet.getTaskId());
            final ComplexTasklet ct = (ComplexTasklet) tasklet;
                complexTaskHandlingDAO.save(userId,taskId,ct.getComplexTaskHandlingRoot());
        }

        if (changed) {
            taskHandlingDao.saveTasklet(taskletVO);
        }

    }

    /**
     * Hook for subclasses to react on calls to {@link #storeTasklet(Tasklet)}.
     *
     * @param tasklet tasklet to store
     * @param taskletVO old value object (unmodified)
     */
    protected void onStoreTasklet(final Tasklet tasklet, final TaskletVO taskletVO) {

    }

}
