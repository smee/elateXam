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
package de.thorstenberger.taskmodel.view;

import java.util.List;

/**
 * @author Thorsten Berger
 *
 */
public class SubTaskletInfoVO {

	private String virtualSubTaskletNumber;
	private String hint;
	private float reachablePoints;
	private String problem;
	private String renderedHTML;
	private boolean corrected;
		private boolean interactiveFeedback;	
//	private boolean needsManualCorrection;
//	private String points;
	private boolean needsManualCorrectionFlag;
	private List<Correction> corrections;
	
	private String correctionHint;
	private String correctedHTML;
	private String correctionHTML;
	
	
/**
	 * @return the corrections
	 */
	public List<Correction> getCorrections() {
		return corrections;
	}
	/**
	 * @param corrections the corrections to set
	 */
	public void setCorrections(List<Correction> corrections) {
		this.corrections = corrections;
	}
	/**
	 * @return the needsManualCorrectionFlag
	 */
	public boolean isNeedsManualCorrectionFlag() {
		return needsManualCorrectionFlag;
	}
	/**
	 * @param needsManualCorrectionFlag the needsManualCorrectionFlag to set
	 */
	public void setNeedsManualCorrectionFlag(boolean needsManualCorrectionFlag) {
		this.needsManualCorrectionFlag = needsManualCorrectionFlag;
	}
	//	/**
//	 * @return the needsManualCorrection
//	 */
//	public boolean isNeedsManualCorrection() {
//		return needsManualCorrection;
//	}
//	/**
//	 * @param needsManualCorrection the needsManualCorrection to set
//	 */
//	public void setNeedsManualCorrection(boolean needsManualCorrection) {
//		this.needsManualCorrection = needsManualCorrection;
//	}
	/**
	 * @return Returns the hint.
	 */
	public String getHint() {
		return hint;
	}
	/**
	 * @param hint The hint to set.
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}
	/**
	 * @return Returns the problem.
	 */
	public String getProblem() {
		return problem;
	}
	/**
	 * @param problem The problem to set.
	 */
	public void setProblem(String problem) {
		this.problem = problem;
	}
	/**
	 * @return Returns the reachablePoints.
	 */
	public float getReachablePoints() {
		return reachablePoints;
	}
	/**
	 * @param reachablePoints The reachablePoints to set.
	 */
	public void setReachablePoints(float reachablePoints) {
		this.reachablePoints = reachablePoints;
	}
	/**
	 * @return Returns the renderedHTML.
	 */
	public String getRenderedHTML() {
		return renderedHTML;
	}
	/**
	 * @param renderedHTML The renderedHTML to set.
	 */
	public void setRenderedHTML(String renderedHTML) {
		this.renderedHTML = renderedHTML;
	}
	/**
	 * @return Returns the virtualSubTaskletNumber.
	 */
	public String getVirtualSubTaskletNumber() {
		return virtualSubTaskletNumber;
	}
	/**
	 * @param virtualSubTaskletNumber The virtualSubTaskletNumber to set.
	 */
	public void setVirtualSubTaskletNumber(String virtualSubTaskletNumber) {
		this.virtualSubTaskletNumber = virtualSubTaskletNumber;
	}
	/**
	 * @return Returns the corrected.
	 */
	public boolean isCorrected() {
		return corrected;
	}
	/**
	 * @param corrected The corrected to set.
	 */
	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}
//	/**
//	 * @return Returns the points.
//	 */
//	public String getPoints() {
//		return points;
//	}
//	/**
//	 * @param points The points to set.
//	 */
//	public void setPoints(String points) {
//		this.points = points;
//	}
	/**
	 * @return Returns the correctedHTML.
	 */
	public String getCorrectedHTML() {
		return correctedHTML;
	}
	/**
	 * @param correctedHTML The correctedHTML to set.
	 */
	public void setCorrectedHTML(String correctedHTML) {
		this.correctedHTML = correctedHTML;
	}
	/**
	 * @return Returns the correctionHTML.
	 */
	public String getCorrectionHTML() {
		return correctionHTML;
	}
	/**
	 * @param correctionHTML The correctionHTML to set.
	 */
	public void setCorrectionHTML(String correctionHTML) {
		this.correctionHTML = correctionHTML;
	}
	/**
	 * @return Returns the correctionHint.
	 */
	public String getCorrectionHint() {
		return correctionHint;
	}
	/**
	 * @param correctionHint The correctionHint to set.
	 */
	public void setCorrectionHint(String correctionHint) {
		this.correctionHint = correctionHint;
	}
	public boolean isInteractiveFeedback() {
		return interactiveFeedback;
	}
	public void setInteractiveFeedback(boolean interactiveFeedback) {
		this.interactiveFeedback = interactiveFeedback;
	}
	
	
	public class Correction{
		
		private String corrector;
		private boolean auto;
		private float points;
		/**
		 * @param corrector
		 * @param points
		 */
		public Correction(String corrector, boolean auto, float points) {
			super();
			this.corrector = corrector;
			this.auto = auto;
			this.points = points;
		}
		/**
		 * @return the corrector
		 */
		public String getCorrector() {
			return corrector;
		}
		/**
		 * @param corrector the corrector to set
		 */
		public void setCorrector(String corrector) {
			this.corrector = corrector;
		}
		/**
		 * @return the auto
		 */
		public boolean isAuto() {
			return auto;
		}
		/**
		 * @param auto the auto to set
		 */
		public void setAuto(boolean auto) {
			this.auto = auto;
		}
		/**
		 * @return the points
		 */
		public float getPoints() {
			return points;
		}
		/**
		 * @param points the points to set
		 */
		public void setPoints(float points) {
			this.points = points;
		}
		
	}

}
