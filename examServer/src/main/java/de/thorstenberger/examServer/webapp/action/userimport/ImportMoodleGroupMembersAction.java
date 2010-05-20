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
package de.thorstenberger.examServer.webapp.action.userimport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.elatexam.httprobot.Robot;
import de.thorstenberger.examServer.webapp.form.ImportMoodleGroupMembersForm;
import de.thorstenberger.examServer.ws.opal.Member;

/**
 * Import user details from a moodle course via webscraping.
 *
 * @author Steffen Dienst
 *
 */
public class ImportMoodleGroupMembersAction extends AbstractImportMembersAction {
  @Override
  public ActionForward execute(
      final ActionMapping mapping,
      final ActionForm form_,
      final HttpServletRequest request,
      final HttpServletResponse response)
      throws Exception {

    final ImportMoodleGroupMembersForm form = (ImportMoodleGroupMembersForm) form_;

    if (!StringUtils.isEmpty(form.getUserId()) && !StringUtils.isEmpty(form.getPassword())) {
      if ("searchCourses".equals(request.getParameter("action")) || StringUtils.isEmpty(form.getCourseName())) {

        // fetch all courses of this user
        final List<String> courses = fetchCoursesOf(form.getUserId(), form.getPassword());
        courses.remove("Help");// remove invalid course
        courses.remove("Hilfe");// remove invalid course
        request.setAttribute("knownCourses", courses);
      } else {
        // fetch all group participants
        final List<Member> members = fetchGroupMembers(form.getUserId(), form.getPassword(), form.getCourseName());
        final List<Member> importedMembers = storeImportedUsers(members);

        request.setAttribute("importedMembers", importedMembers);
      }
    }

    return mapping.findForward("success");
  }

  /**
   * Find this user's courses from moodle.
   *
   * @param userId
   * @param password
   * @return
   */
  private List<String> fetchCoursesOf(final String userId, final String password) {
    final Robot robot = new Robot(new String[]{"un:"+userId,"pw:"+password});
    robot.run(this.getClass().getResourceAsStream("moodleMyCourses.xml"));
    final String[] textResult = robot.getLastTextResult().split("\n");
    // return mutable list
    return new ArrayList<String>(Arrays.asList(textResult));
  }


  /**
   * Fetch all members of the given course.
   *
   * @param form
   *          input data as entered by user
   * @return
   */
  private List<Member> fetchGroupMembers(final String userId, final String password, final String courseName) {
    final Robot robot = new Robot(new String[] { "un:" + userId, "pw:" + password, "coursename:" + courseName });
    robot.run(this.getClass().getResourceAsStream("moodleParticipants.xml"));
    final String participantsCsv = new String(robot.getLastByteResult());

    return loadParticipantsFromCsv(participantsCsv);
  }

  /**
   * @param participantsCsv
   * @return
   */
  private List<Member> loadParticipantsFromCsv(final String participantsCsv) {
    final List<Member> result = new LinkedList<Member>();
    final BufferedReader br = new BufferedReader(new StringReader(participantsCsv));
    try {
      final String header = br.readLine();
      log.debug("Importing moodle users, csv header line: " + header);

      String line = null;
      while ((line = br.readLine()) != null) {
        final String[] lineParts = line.split(";");
        if (lineParts.length == 4) {
          final Member member = new Member();
        // username;email;firstname;lastname
        log.trace("importing line: " + line);
        member.setMemberId(lineParts[0]);
        member.setEmail(lineParts[1]);
        member.setFirstname(lineParts[2]);
        member.setLastname(lineParts[3]);

        result.add(member);
        } else {
          log.warn(String.format("Could not import line '%s' from csv, does not have 4 components seperated by ;", line));
        }
      }

    } catch (final IOException e) {
      log.error("Could not import moodle csv", e);
    }
    return result;
  }

}
