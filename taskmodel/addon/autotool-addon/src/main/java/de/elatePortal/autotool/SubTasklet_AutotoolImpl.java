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
 *
 */
package de.elatePortal.autotool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import autotool.AutotoolGrade;
import autotool.AutotoolServices;
import autotool.AutotoolTaskInstance;
import autotool.SignedAutotoolTaskConfig;
import de.elatePortal.autotool.view.AutotoolCorrectionSubmitData;
import de.elatePortal.autotool.view.AutotoolSubmitData;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.complex.RandomUtil;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.CorrectionSubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubmitData;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.impl.AbstractAddonSubTasklet;
import de.thorstenberger.taskmodel.complex.jaxb.AddonSubTaskDef;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandlingType.TryType.PageType.AddonSubTask;
import de.thorstenberger.taskmodel.complex.jaxb.SubTaskDefType;

/**
 * @author Steffen Dienst
 *
 */
public class SubTasklet_AutotoolImpl extends AbstractAddonSubTasklet implements SubTasklet_Autotool {
	private class AutotoolSubTaskDummy{
		private Element memento;


		public AutotoolSubTaskDummy(AddonSubTask atSubTask) {
			parseSubTask(atSubTask);
			atSubTask.setTaskType(getAddOnType());
		}

		private void parseSubTask(AddonSubTask atSubTask) {
			this.memento=atSubTask.getMemento();
			if(memento==null) {
				Document doc=new CoreDocumentImpl();
				memento=doc.createElementNS("http://complex.taskmodel.thorstenberger.de/complexTaskHandling","Memento");
				atSubTask.setMemento(memento);
			}
		}

		public String getAnswer() {
			return getText(memento,"answer",null);
		}
		public void setAnswer(String value) {
			setText(memento,"answer",value);
		}
		public String getLastCorrectedAnswer() {
			return getText(memento,"lastCorrectedAnswer",null);
		}
		public void setLastCorrectedAnswer(String value) {
			setText(memento,"lastCorrectedAnswer",value);
		}
		public void setAutotoolDoc(String doc) {
			setText(memento,"autotoolDoc",doc);
		}

		public String getAutotoolDoc() {
			return getText(memento,"autotoolDoc",null);
		}

		public String getDefaultAnswer() {
			return getText(memento,"defaultAnswer",null);
		}


		public String getProblem() {
			return getText(memento,"problem",null);
		}

		public void setProblem(String problem) {
			setText(memento,"problem",problem);
		}

		public void setDefaultAnswer(String defaultAnswer) {
			setText(memento,"defaultAnswer",defaultAnswer);
		}

		public void setAnswerDoc(String answerDoc) {
			setText(memento,"answerDoc",answerDoc);
		}
		public void setAutotoolScore(double score) {
			setText(memento,"score",Double.toString(score));
		}
		public double getAutotoolScore() {
			return Double.parseDouble(getText(memento,"score","-1"));
		}

		public byte[] getAutotoolInstanceBlob() {
			return Base64.decodeBase64(getText(memento,"privateBlob","").getBytes());
		}

		public void setAutotoolInstanceBlob(byte[] bs) {
			setText(memento,"privateBlob",new String(Base64.encodeBase64(bs)));
		}

	}
	private class AutotoolTaskConfig{
		private Element memento;
		private boolean isInteractive;
		public AutotoolTaskConfig(AddonSubTaskDef aostd) {
			this.memento=aostd.getMemento();
			this.isInteractive=aostd.isInteractiveFeedback();
		}
		public boolean isInteractive() {
			return isInteractive;
		}

		public String getTaskType() {
			return getText(memento,"taskType","");
		}

		public String getConfigString() {
			return getText(memento,"configString","");
		}

		public String getSignature() {
			return getText(memento,"signature","");
		}

		public String getAutotoolServerUrl() {
			return getText(memento,"autotoolServerUrl","http://localhost");
		}

	}

	private AutotoolServices ats;
	private AutotoolSubTaskDummy autotoolSubTask;
	private AutotoolTaskConfig autotoolTaskConfig;
	/**
	 *
	 */
	public SubTasklet_AutotoolImpl( ComplexTaskDefRoot root, Block block, SubTaskDefType aoSubTaskDef, AddonSubTask atSubTask ) {
		super(root, block,aoSubTaskDef,atSubTask);
		this.autotoolTaskConfig=new AutotoolTaskConfig(((AddonSubTaskDef) aoSubTaskDef));
		this.autotoolSubTask = new AutotoolSubTaskDummy(atSubTask);
	}

	@Override
    public String getProblem(){
		return autotoolSubTask.getProblem();
	}

	public String getAnswer(){
		String answer=autotoolSubTask.getAnswer();
		if(answer==null) {
            answer=autotoolSubTask.getDefaultAnswer();
        }
		return answer;
	}
	public String getLastCorrectedAnswer(){
		String answer=autotoolSubTask.getLastCorrectedAnswer();
		if(answer==null) {
            answer=getAnswer();
        }
		return answer;
	}

	public void doSave( SubmitData submitData ) throws IllegalStateException{
		AutotoolSubmitData tsd = (AutotoolSubmitData) submitData;
		autotoolSubTask.setAnswer( tsd.getAnswer() );
	}

	public void doAutoCorrection(){
		try {
			AutotoolTaskInstance ati=new AutotoolTaskInstance.AutotoolTaskInstanceVO(autotoolTaskConfig.getTaskType(),(Map) deserialize(autotoolSubTask.getAutotoolInstanceBlob()));
			AutotoolGrade grade=getAutotoolServices().gradeTaskInstance(ati,getAnswer());
			setCorrection((grade.isSolved()?block.getPointsPerSubTask():0), grade.getGradeDocumentation(), true);//TODO autotools laenge der loesung speichern?
			autotoolSubTask.setAutotoolScore(grade.getPoints());
			autotoolSubTask.setLastCorrectedAnswer(getAnswer());
		} catch (Exception e) {//TODO bloed
			e.printStackTrace();
		}
	}


	public void doManualCorrection( CorrectionSubmitData csd ){
		AutotoolCorrectionSubmitData acsd=(AutotoolCorrectionSubmitData) csd;
		setCorrection(acsd.getPoints(), "manually corrected", false);
	}

	protected void setCorrection( float points, String doc, boolean auto ){
		if(auto) {
            super.setAutoCorrection(points);
        }
        else {
            ;//TODO
        }
		autotoolSubTask.setAutotoolDoc(doc);
	}


	public boolean isProcessed(){
		return autotoolSubTask.getAnswer()!=null
		&& autotoolSubTask.getAnswer().length() > 0
		&& !autotoolSubTask.getAnswer().equals(autotoolSubTask.getDefaultAnswer());//mmh, was wenn die aber die richtige ist? wohl unwahrscheinlich
	}

	/*
	 *  (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet#build()
	 */
    public void build(long randomSeed) throws TaskApiException {
		SignedAutotoolTaskConfig satc=new SignedAutotoolTaskConfig.SignedAutotoolTaskConfigVO(this.autotoolTaskConfig.getTaskType(),autotoolTaskConfig.getConfigString(),"",autotoolTaskConfig.getSignature());
		try {
            AutotoolTaskInstance task = getAutotoolServices().getTaskInstance(satc, new RandomUtil(randomSeed).getInt(1000000));
			autotoolSubTask.setProblem(task.getProblem());
			autotoolSubTask.setDefaultAnswer(task.getDefaultAnswer());
			autotoolSubTask.setAnswerDoc(task.getAnswerDoc());
			autotoolSubTask.setAutotoolInstanceBlob(serialize(task.getSignedInstance()));//nur fuer autotool interessant
		} catch (Exception e) {
			throw new TaskApiException(e);
		}
	}

	private byte[] serialize(Object obj) {
		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.close();
			byte[] arr=bos.toByteArray();
			bos.close();
			return arr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private Object deserialize(byte[] arr) {
		try {
			ByteArrayInputStream bos=new ByteArrayInputStream(arr);
			ObjectInputStream oos=new ObjectInputStream(bos);
			Object o=oos.readObject();
			oos.close();
			bos.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private AutotoolServices getAutotoolServices() throws IOException {
		if(ats == null) {
            ats=new AutotoolServices(new URL(autotoolTaskConfig.getAutotoolServerUrl()));
        }
		return ats;
	}


	public String getAutotoolGradeDoc() {
		if(subTaskType.isSetAutoCorrection())
			return autotoolSubTask.getAutotoolDoc();
		else
			return "";
	}

	public int getHash(){
		StringBuffer ret = new StringBuffer();
		ret.append( subTaskType.getRefId() );
		ret.append( getAnswer() );
		ret.append( getVirtualSubtaskNumber() );
		return ret.toString().hashCode();
	}

//Helper methods........................................................

	private Element getElement(Element memento, String string) {
		Element e=null;
		NodeList nl=memento.getElementsByTagName(string);
		if(nl.getLength()>0) {
            e=(Element) nl.item(0);
        }
		return e;
	}

	private String getText(Element memento, String nodeName, String dflt) {
		Element element=getElement(memento,nodeName);
		if(element!=null) {
			String text = element.getFirstChild().getTextContent();
			return text;
		}
		return dflt;
	}

	private String deEscapeXML(String text) {
		return text.replaceAll("&amp;", "&")
		.replaceAll("&lt;", "<")
		.replaceAll("&gt;", ">")
		.replaceAll("&apos;", "\\'")
		.replaceAll("&quot;", "\\\"");
	}

	private String escapeXML(String text) {
		return text.replaceAll("&", "&amp;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;")
		.replaceAll("\\'", "&apos;")
		.replaceAll("\\\"", "&quot;");
	}

	private void setText(Element memento, String nodeName, String text) {
		Element element=getElement(memento,nodeName);
		if(element==null) {
			element=memento.getOwnerDocument().createElement(nodeName);
			memento.appendChild(element);
		}
		element.setTextContent(text);//setTextContent() laesst sich mit maven nicht kompilieren??? dom3 nicht im classpath?

	}


	public String getAddOnType() {
		return ((AddonSubTaskDef)this.jaxbSubTaskDef).getTaskType();
	}

	@Override
    public boolean isInteractiveFeedback() {
		return this.autotoolTaskConfig.isInteractive();
	}

	public double getAutotoolScore() {
		return autotoolSubTask.getAutotoolScore();
	}

}
