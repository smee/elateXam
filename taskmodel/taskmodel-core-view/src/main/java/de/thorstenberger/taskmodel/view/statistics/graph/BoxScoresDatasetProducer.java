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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.taglib.CewolfChartFactory;
import de.laures.cewolf.taglib.IncompatibleDatasetException;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ManualSubTaskletCorrection;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Mapping;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text;

/**
 * @author Steffen Dienst
 *
 */
public class BoxScoresDatasetProducer implements DatasetProducer {
  static {
    CewolfChartFactory.registerFactory(new CewolfChartFactory("box") {

      @Override
      public JFreeChart getChartInstance(String title, String xAxisLabel, String yAxisLabel, Dataset data) throws IncompatibleDatasetException {
        CewolfChartFactory.check(data, BoxAndWhiskerCategoryDataset.class, "box");
        return ChartFactory.createBoxAndWhiskerChart(title, xAxisLabel, yAxisLabel, (BoxAndWhiskerCategoryDataset) data, true);
      }
    });
  }
  private List mc, cloze, mapping, text;

  public BoxScoresDatasetProducer(List<Tasklet> tasklets) {
    mc = filterScores(tasklets, SubTasklet_MC.class);
    mapping = filterScores(tasklets, SubTasklet_Mapping.class);
    cloze = filterScores(tasklets, SubTasklet_Cloze.class);
    text = filterScores(tasklets, SubTasklet_Text.class);

  }

  private List filterScores(List<Tasklet> tasklets, Class<? extends SubTasklet> clazz) {
    List<Float> scores = new LinkedList<Float>();
    for (Tasklet t : tasklets) {
      scores.addAll(countScoresOfType(t, clazz));
    }
    return scores;
  }

  private List<Float> countScoresOfType(Tasklet tasklet, Class<? extends SubTasklet> taskletClazz) {
    List<Float> res = new LinkedList<Float>();

    if (tasklet instanceof ComplexTasklet) {
      ComplexTasklet ct = (ComplexTasklet) tasklet;
      if (ct.getSolutionOfLatestTry() != null) {
        for (Page page : ct.getSolutionOfLatestTry().getPages()) {
          for (SubTasklet st : page.getSubTasklets()) {
            if (!taskletClazz.isAssignableFrom(st.getClass()) || !st.isCorrected()) {
              continue;
            }

            if (st.isAutoCorrected()) {
              res.add( st.getAutoCorrection().getPoints());
            } else {
              float sum = 0;
              for (ManualSubTaskletCorrection mcorr : st.getManualCorrections()) {
                sum+=mcorr.getPoints();
              }
              res.add(sum / st.getManualCorrections().size());
            }
          }
        }
      }
    }
    return res;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
   */
  public Object produceDataset(Map params) throws DatasetProduceException {
    DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
    dataset.add(mc, "MC", "MC");
    dataset.add(cloze, "Lückentext", "Lückentext");
    dataset.add(mapping, "Zuordnung", "Zuordnung");
    dataset.add(text, "Freitext", "Freitext");
    return dataset;

  }

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
    return "box plot for tasklet types";
  }

}
