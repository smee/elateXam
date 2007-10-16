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

import java.util.Map;

public interface SignedAutotoolTaskConfig{
	
	public String getSignature();
	public String getTaskType();
	public String getDocumentation();		
	public String getConfigString();
	
	public class SignedAutotoolTaskConfigVO implements SignedAutotoolTaskConfig{
		private String signature;
		private String doc;
		private String cfg;
		private String taskType;
		
		SignedAutotoolTaskConfigVO(Map m, AutotoolTaskConfig atc) {
			this.signature=(String) m.get("signature");
			this.doc=AutotoolServices.trimHtmlTags(atc.getDocumentation());
			this.cfg=atc.getConfigString();
			this.taskType=atc.getTaskType();
		}
		
		public SignedAutotoolTaskConfigVO(String taskType, String cfg, String doc, String signature) {
			this.signature=signature;
			this.taskType=taskType;
			this.doc=doc;
			this.cfg=cfg;
		}
		public String getSignature() {
			return signature;
		}
		public String getTaskType() {
			return taskType;
		}
		public String getDocumentation() {
			return doc;
		}
		public String getConfigString() {
			return cfg;
		}
	}	
}