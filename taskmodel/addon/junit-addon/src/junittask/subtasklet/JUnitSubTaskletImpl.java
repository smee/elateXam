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
package junittask.subtasklet;

import junittask.subtasklet.view.JUnitCorrectionSubmitData;
import junittask.subtasklet.view.JUnitSubmitData;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import correction.junit.JUnitTestCorrector;
import correction.junit.JUnitTestResult;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.AbstractAddonSubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.AddonSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskType;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.AddonSubTask;

public class JUnitSubTaskletImpl extends AbstractAddonSubTasklet implements SubTasklet_JUnit{
	private class JUnitSubtaskDummy{
		private Element memento;

		public JUnitSubtaskDummy(AddonSubTask aos) {
			this.memento=aos.getMemento();
			if(memento==null) {
				Document doc=new CoreDocumentImpl();
				memento=doc.createElementNS("http://complex.taskmodel.thorstenberger.de/complexTaskHandling","Memento");
				aos.setMemento(memento);
			}
			aos.setTaskType(getAddOnType());
		}
		public String getClassDef() {
			return getText(memento,"classdef",null);
		}
		public void setClassDef(String value) {
			setText(memento,"classdef",value);
		}
		public void setTestResult(String result) {
			setText(memento,"result",result);
		}
		public String getTestResult() {
			return getText(memento,"result",null);
		}
	}
	private class JUnitSubTaskDef{
		private Element memento;
		private boolean interactiveFeedback;

		public JUnitSubTaskDef(AddonSubTaskDef aost) {
			this.memento=aost.getMemento();
			this.interactiveFeedback=aost.isInteractiveFeedback();
		}

		public boolean isInteractiveFeedback() {
			return interactiveFeedback;
		}

		public String getTestClassDef() {
			return getText(memento,"unittest",null);//TODO defaultvalue?
		}

		public String getProblem() {
			return getText(memento,"problem",null);
		}
		public String getDefaultClassDef() {
			return getText(memento,"defaultClassDef",null);
		}
		public long getTimeout() {
			long dflt=5000;
			String to=getText(memento,"timeout",Long.toString(dflt));
			try{
				return Long.parseLong(to);
			}catch(NumberFormatException nfe) {
				return dflt;
			}
		}
		public String getInterfaceClassDef() {
			return getText(memento,"interfaceClassDef",null);
		}
	}

	private JUnitSubtaskDummy justd;
	private JUnitSubTaskDef justdd;
	private JUnitTestCorrector taskCorrector;

	public JUnitSubTaskletImpl(ComplexTaskDefRoot complexTaskDefRoot, Block block,SubTaskDefType subTaskDef, SubTaskType subTaskType, JUnitTestCorrector corrector) {
		this(complexTaskDefRoot, block, subTaskDef, subTaskType);
		this.taskCorrector=corrector;
	}
	public JUnitSubTaskletImpl(ComplexTaskDefRoot complexTaskDefRoot, Block block,SubTaskDefType subTaskDef, SubTaskType subTaskType) {
		super(complexTaskDefRoot, block, subTaskDef, subTaskType);
		this.justd=new JUnitSubtaskDummy((AddonSubTask) subTaskType);
		this.justdd=new JUnitSubTaskDef((AddonSubTaskDef) subTaskDef);

	}

	public void doSave(SubmitData submitData) throws IllegalStateException {
		JUnitSubmitData jsd=(JUnitSubmitData) submitData;
		justd.setClassDef(jsd.getClassDef());
	}

	public void doAutoCorrection() {
			JUnitTestResult result=taskCorrector.runUnitTest( justdd.getInterfaceClassDef(), this.getClassDef(), justdd.getTestClassDef(), justdd.getTimeout() );
			if(result != null)
				setCorrection(result.isCorrect()?block.getPointsPerSubTask():0,result.getResult(),true);
			else
				;//TODO needs manual correction
	}
	protected void setCorrection( float points, String result, boolean auto ){
		if(auto)
			super.setAutoCorrection(points);
		else
			;//TODO
		justd.setTestResult(result);
	}


	public void doManualCorrection(CorrectionSubmitData csd) {
		JUnitCorrectionSubmitData jcsd=(JUnitCorrectionSubmitData) csd;
		setCorrection(jcsd.getPoints(), "manually corrected",false);
	}

	public int getHash() {
		StringBuffer ret = new StringBuffer();
		ret.append( this.subTaskType.getRefId() );
		ret.append( getClassDef() );
		ret.append( getVirtualSubtaskNumber() );
		return ret.toString().hashCode();
	}

	public boolean isProcessed() {
		return justd.getClassDef()!=null && justd.getClassDef().length()>0 && !justd.getClassDef().equals(justdd.getDefaultClassDef());
	}

	public void build() throws TaskApiException {
		//TODO
	}
	public String getProblem() {
		return justdd.getProblem();
	}
	public String getAddOnType() {
		return ((AddonSubTaskDef)this.jaxbSubTaskDef).getTaskType();
	}

	public String getClassDef() {
		String cd=justd.getClassDef();
		if(cd==null)
			cd=justdd.getDefaultClassDef();
		return cd;
	}

	public boolean isInteractiveFeedback() {
		return justdd.isInteractiveFeedback();
	}

	//Helper methods........................................................

	private Element getElement(Element memento, String string) {
		Element e=null;
		NodeList nl=memento.getElementsByTagName(string);
		if(nl.getLength()>0)
			e=(Element) nl.item(0);
		return e;
	}

	private String getText(Element memento, String nodeName, String dflt) {
		Element element=getElement(memento,nodeName);
		if(element!=null) {
			Node child=element.getFirstChild();
			if(child!=null) {
				String text = child.getTextContent();
				return text==null?"":text;//getTextContent() laesst sich mit maven nicht kompilieren???
			}
		}
		return dflt;
	}

	private void setText(Element memento, String nodeName, String text) {
		Element element=getElement(memento,nodeName);
		if(element==null) {
			element=memento.getOwnerDocument().createElement(nodeName);
			memento.appendChild(element);
		}
		element.setTextContent(text);//setTextContent() laesst sich mit maven nicht kompilieren??? dom3 nicht im classpath?

	}

	public String getJUnitResults() {
		return justd.getTestResult();
	}

}
