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
package de.thorstenberger.taskmodel.view.correction;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Steffen Dienst
 *
 */
public class CorrectorsForm extends ActionForm {

  private String[] availableCorrectors;
  private String[] selectedCorrectors;

  /**
   * @param availableCorrectors
   *          the availableCorrectors to set
   */
  public void setAvailableCorrectors(final String[] availableCorrectors) {
    this.availableCorrectors = availableCorrectors;
  }

  /**
   * @return the availableCorrectors
   */
  public String[] getAvailableCorrectors() {
    return availableCorrectors;
  }

  /**
   * @param selectedCorrectors
   *          the selectedCorrectors to set
   */
  public void setSelectedCorrectors(final String[] selectedCorrectors) {
    this.selectedCorrectors = selectedCorrectors;
  }

  /**
   * @return the selectedCorrectors
   */
  public String[] getSelectedCorrectors() {
    return selectedCorrectors;
  }

  @Override
  public void reset(final ActionMapping mapping, final HttpServletRequest request) {
    this.selectedCorrectors = new String[0];
    this.availableCorrectors = new String[0];
    super.reset(mapping, request);
  }
}
