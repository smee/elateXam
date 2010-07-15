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

import java.io.Serializable;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import de.laures.cewolf.ChartPostProcessor;

/**
 * CEWolf is buggy, for charts of type verticalxybar the domain axis is always of type Date.... Use numeric values
 * instead.
 *
 * @author Steffen Dienst
 *
 */
public class FixXAxisChartPostProcessor implements ChartPostProcessor, Serializable {

  /* (non-Javadoc)
   * @see de.laures.cewolf.ChartPostProcessor#processChart(java.lang.Object, java.util.Map)
   */
  public void processChart(Object arg0, Map arg1) {
    JFreeChart chart = (JFreeChart) arg0;
    chart.getXYPlot().setDomainAxis(new NumberAxis(chart.getXYPlot().getDomainAxis().getLabel()));
  }

}
