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
package de.thorstenberger.taskmodel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.Tasklet.Status;

/**
 * Distribute a collection of {@link Tasklet}s among several correctors (tutors). Takes care of prior corrections (no
 * tutor will get a tasklet he had corrected earlier.).
 *
 * @author Steffen Dienst
 *
 */
public class TaskletDistributor {

  /**
   * Find the constraints and distribute the tasklets among the correctors randomly.
   *
   * @param correctors
   * @returns statistics about the number of assigned tasklets per corrector.
   * @throws TaskApiException
   */
  public Map<String, Integer> distributeAmong(final List<Tasklet> tasklets, final String... correctorIds) throws TaskApiException {
    // all tasklets that have not been manually corrected yet
    final Map<String, Integer> statistics = new HashMap<String, Integer>();
    for (final String corr : correctorIds) {
      statistics.put(corr, 0);
    }

    final Map<String, List<Tasklet>> possibleAssignments = filterPossibleAssignments(tasklets, correctorIds);

    final Random r = new Random();

    while (isAnyUnassignedTaskletLeft(tasklets)) {
      boolean noMoreAssignableTasklets = true;
      for (final String corrector : possibleAssignments.keySet()) {
        final List<Tasklet> possible = possibleAssignments.get(corrector);
        if (!possible.isEmpty()) {
          noMoreAssignableTasklets = false;
          final Tasklet tasklet = possible.get(r.nextInt(possible.size()));

          // nobody else may correct this tasklet
          for (final List<Tasklet> list : possibleAssignments.values()) {
            list.remove(tasklet);
          }
          tasklet.assignToCorrector(corrector);
          statistics.put(corrector, statistics.get(corrector) + 1);
        }
        // System.out.format("Corr: %s, to go: %d\n", corrector, possible.size());
      }
      if (noMoreAssignableTasklets) {
        break;
      }
    }
    return statistics;
  }



  private boolean isAnyUnassignedTaskletLeft(final List<Tasklet> tasklets) {
    for (final Tasklet tasklet : tasklets) {
      if (!tasklet.hasOrPassedStatus(Status.CORRECTED) && StringUtils.isEmpty(tasklet.getTaskletCorrection().getCorrector())) {
        return true;
      }
    }
    return false;
  }

  private Map<String, List<Tasklet>> filterPossibleAssignments(final List<Tasklet> tasklets, final String[] correctorIds) {
    final Map<String, List<Tasklet>> result = new HashMap<String, List<Tasklet>>();
    for (final String corrector : correctorIds) {
      final List<Tasklet> possible = new ArrayList<Tasklet>();
      result.put(corrector, possible);

      for (final Tasklet tasklet : tasklets) {
        if (needsCorrection(tasklet) && !wasCorrectedBy(corrector, tasklet)) {
          possible.add(tasklet);
        }
      }

    }
    return result;
  }



  private boolean wasCorrectedBy(final String corrector, final Tasklet tasklet) {
    for (final ManualCorrection mc : tasklet.getTaskletCorrection().getManualCorrections()) {
      if (mc.getCorrector().equals(corrector)) {
        return true;
      }
    }
    return false;
  }

  private boolean needsCorrection(final Tasklet tasklet) {
    return ((tasklet.getStatus() == Tasklet.Status.SOLVED || tasklet.getStatus() == Tasklet.Status.CORRECTING) && tasklet.getTaskletCorrection().getCorrector() == null);
  }

}
