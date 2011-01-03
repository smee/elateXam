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
package de.thorstenberger.taskmodel.view.statistics.graph;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import org.jfree.data.statistics.HistogramDataset;
//import org.jfree.data.statistics.HistogramType;
//
//import de.laures.cewolf.DatasetProduceException;
//import de.laures.cewolf.DatasetProducer;
import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.TaskletCorrection;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;

/**
 * Chart dataset for tasklet score distribution, automatically scored vs. manually scored.
 *
 * @author Steffen Dienst
 *
 */
public class ScoresDatasetProducer {//implements DatasetProducer {

  private double[] scores, autoscores;

  public ScoresDatasetProducer(List<Tasklet> tasklets) {
    List<Float> scorelist = new LinkedList<Float>();
    List<Float> autoscorelist = new LinkedList<Float>();

    for (Tasklet tasklet : tasklets) {
      float score = 0;
      TaskletCorrection tc = tasklet.getTaskletCorrection();
      if (tc.isAutoCorrected()) {
        score += tasklet.getTaskletCorrection().getAutoCorrectionPoints();
      } else {

        // manual points
        for (ManualCorrection mc : tc.getManualCorrections()) {
          score += mc.getPoints();
        }
        // mean score for multiple manual corrections
        if (!tc.getManualCorrections().isEmpty()) {
          score /= tc.getManualCorrections().size();
        }
      }
      scorelist.add(score);
      Float auto = countAutoScores(tasklet);
      if (auto != null) {
        autoscorelist.add(auto);
      }
    }
    this.scores = new double[scorelist.size()];
    int i = 0;
    for (Float f : scorelist) {
      this.scores[i++] = f;
    }
    this.autoscores = new double[autoscorelist.size()];
    i = 0;
    for (Float f : autoscorelist) {
      this.autoscores[i++] = f;
    }
  }

  private Float countAutoScores(Tasklet tasklet) {
    float sum = 0;
    boolean atLeastOneAutoCorrection = false;
    if (tasklet instanceof ComplexTasklet) {
      ComplexTasklet ct = (ComplexTasklet) tasklet;
      if (ct.getSolutionOfLatestTry() != null) {
        for (Page page : ct.getSolutionOfLatestTry().getPages()) {
          for (SubTasklet st : page.getSubTasklets()) {
            if (st.isAutoCorrected()) {
              atLeastOneAutoCorrection = true;
              sum += st.getAutoCorrection().getPoints();
            }
          }
        }
      }
      if (atLeastOneAutoCorrection)
        return sum;
    }
    return null;
  }

//  /*
//   * (non-Javadoc)
//   *
//   * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
//   */
//  public Object produceDataset(Map params) throws DatasetProduceException {
//    HistogramDataset dataset = new HistogramDataset();
//    dataset.addSeries("Gesamt", scores, 50);
//    dataset.addSeries("Automatisch", autoscores, 50);
//    dataset.setType(HistogramType.FREQUENCY);
//    return dataset;
//
//  }

  /*
   * (non-Javadoc)
   *
   * @see de.laures.cewolf.DatasetProducer#hasExpired(java.util.Map, java.util.Date)
   */
  public boolean hasExpired(Map params, Date since) {
    return (System.currentTimeMillis() - since.getTime()) > 5000;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.laures.cewolf.DatasetProducer#getProducerId()
   */
  public String getProducerId() {
    return "auto vs. manual scores";
  }

}
