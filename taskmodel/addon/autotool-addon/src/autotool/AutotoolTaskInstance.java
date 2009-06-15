/*

Copyright (C) 2006 Steffen Dienst

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
 * @author Steffen Dienst
 */
package autotool;

import java.io.Serializable;
import java.util.Map;

public interface AutotoolTaskInstance extends Serializable{
	public class AutotoolTaskInstanceVO implements AutotoolTaskInstance{

		private String taskType;
		private String problem;
		private String solution;
		private String solutionDoc;
		private Map sigInst;

		protected AutotoolTaskInstanceVO(SignedAutotoolTaskConfig cfg, Map inst, String serverUrl) {
			this.taskType=cfg.getTaskType();
			Map first=(Map) inst.get("first");
			Map second=(Map) inst.get("second");
			this.problem=AutotoolServices.replaceImgLinks(serverUrl,AutotoolServices.trimHtmlTags((String) first.get("documentation")));
		this.sigInst=(Map) first.get("contents");
			this.solutionDoc=AutotoolServices.trimHtmlTags((String) second.get("documentation"));
			this.solution=(String)((Map)second.get("contents")).get("contents");
		}
		public AutotoolTaskInstanceVO(String taskType, Map sigInst) {
			this.taskType=taskType;
			this.sigInst=sigInst;
		}

		public String getTaskType() {
			return taskType;
		}
		public String getProblem() {
			return problem;
		}

		public String getDefaultAnswer() {
			return solution;
		}
		public String getAnswerDoc() {
			return solutionDoc;
		}
		public Map getSignedInstance() {
			return sigInst;
		}
	}
	public String getTaskType();
	public String getProblem();
	public String getDefaultAnswer();
	public String getAnswerDoc();
	public Map getSignedInstance();
}