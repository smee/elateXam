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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.elatePortal.autotool.view.AutotoolCorrectionSubmitData;
import de.elatePortal.autotool.view.AutotoolSubmitData;
import de.htwk.autolat.Connector.AutolatConnectorException;
import de.htwk.autolat.Connector.AutolatConnector_0_1;
import de.htwk.autolat.Connector.types.Documented;
import de.htwk.autolat.Connector.types.Either;
import de.htwk.autolat.Connector.types.Instance;
import de.htwk.autolat.Connector.types.Pair;
import de.htwk.autolat.Connector.types.Signed;
import de.htwk.autolat.Connector.types.Triple;
import de.htwk.autolat.Connector.xmlrpc.XmlRpcAutolatConnector_0_1;
import de.htwk.autolat.tools.XMLParser.XMLParser;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskModelRuntimeException;
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
	private class AutotoolSubtaskSettings{
		private Element memento;


		public AutotoolSubtaskSettings(AddonSubTask atSubTask) {
			parseSubTask(atSubTask);
			atSubTask.setTaskType(getAddOnType());
		}

		private void parseSubTask(AddonSubTask atSubTask) {
			this.memento=atSubTask.getMemento();
			if(memento==null) {
        Document doc;
        try {
          doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
          memento = doc.createElementNS("http://complex.taskmodel.thorstenberger.de/complexTaskHandling", "Memento");
          atSubTask.setMemento(memento);
        } catch (ParserConfigurationException e) {
          throw new RuntimeException(e);
        }
			}
		}

		public String getAnswer() {
      return getText(memento, "answer", "");
		}
		public void setAnswer(String value) {
			setText(memento,"answer",value);
		}
		public String getLastCorrectedAnswer() {
      return getText(memento, "lastCorrectedAnswer", "");
		}
		public void setLastCorrectedAnswer(String value) {
			setText(memento,"lastCorrectedAnswer",value);
		}
		public void setAutotoolDoc(String doc) {
			setText(memento,"autotoolDoc",doc);
		}

		public String getAutotoolDoc() {
      return getText(memento, "autotoolDoc", "");
		}

		public String getDefaultAnswer() {
      return getText(memento, "defaultAnswer", "");
		}

		public String getProblem() {
      return getText(memento, "problem", "");
		}

		public void setProblem(String problem) {
			setText(memento,"problem",problem);
		}

		public void setDefaultAnswer(String defaultAnswer) {
			setText(memento,"defaultAnswer",defaultAnswer);
		}

		public void setAutotoolScore(double score) {
			setText(memento,"score",Double.toString(score));
		}
		public double getAutotoolScore() {
      return Double.parseDouble(getText(memento, "score", "0"));
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

    public String getTaskSignature() {
      return getText(memento, "tasksignature", "");
    }

    public void setTaskSignature(String sig) {
      setText(memento, "tasksignature", sig);
    }

    public Instance getInstance() {
      return new Instance(getText(memento, "instancetagprivate", ""), getText(memento, "instancecontents", ""));
    }

    public void setInstance(Instance inst) {
      setText(memento, "instancetagprivate", inst.getTag());
      setText(memento, "instancecontents", inst.getContents());
    }

    public String getInstanceTag() {
      return getText(memento, "instancetag", "");
    }

    public void setInstanceTag(String tag) {
      setText(memento, "instancetag", tag);
    }

    public Signed<Pair<String, String>> getSignedTaskConfig() {
      return new Signed<Pair<String, String>>(
          new Pair<String, String>(
              this.getTaskType(),
              this.getConfigString()),
          this.getSignature());
    }
	}

  private AutolatConnector_0_1 ats;
	private AutotoolSubtaskSettings autotoolSubTask;
	private AutotoolTaskConfig autotoolTaskConfig;
	/**
	 *
	 */
	public SubTasklet_AutotoolImpl( ComplexTaskDefRoot root, Block block, SubTaskDefType aoSubTaskDef, AddonSubTask atSubTask ) {
		super(root, block,aoSubTaskDef,atSubTask);
		this.autotoolTaskConfig=new AutotoolTaskConfig(((AddonSubTaskDef) aoSubTaskDef));
		this.autotoolSubTask = new AutotoolSubtaskSettings(atSubTask);
	}

	@Override
    public String getProblem(){
		return autotoolSubTask.getProblem();
	}

	public String getAnswer(){
		String answer=autotoolSubTask.getAnswer();
    if (StringUtils.isEmpty(answer)) {
            answer=autotoolSubTask.getDefaultAnswer();
        }
		return answer;
	}
	public String getLastCorrectedAnswer(){
		String answer=autotoolSubTask.getLastCorrectedAnswer();
    if (StringUtils.isEmpty(answer)) {
            answer=getAnswer();
        }
		return answer;
	}

	public void doSave( SubmitData submitData ) throws IllegalStateException{
		AutotoolSubmitData tsd = (AutotoolSubmitData) submitData;
		autotoolSubTask.setAnswer( tsd.getAnswer() );
	}

	public void doAutoCorrection(){
    AutolatConnector_0_1 auto;
    try {
      auto = new XmlRpcAutolatConnector_0_1(new URL(autotoolTaskConfig.getAutotoolServerUrl()));
		  Instance taskInstance = autotoolTaskConfig.getInstance();
      Either<String, Documented<Double>> grade = auto.gradeTaskSolution(new Signed(new Pair(autotoolTaskConfig.getInstanceTag(), taskInstance), autotoolTaskConfig.getTaskSignature()), getAnswer());

      if (grade.isRight()) {
        String doc = new XMLParser().parseString(grade.getRight().getDocumentation()).toString();
        setCorrection(block.getPointsPerSubTask(), doc, true);
        autotoolSubTask.setAutotoolScore(grade.getRight().getContents());
      } else {
        setCorrection(0, grade.getLeft(), true);
        autotoolSubTask.setAutotoolScore(0);
      }
			autotoolSubTask.setLastCorrectedAnswer(getAnswer());
    } catch (MalformedURLException e) {
      throw new TaskModelRuntimeException("Invalid autotool url used: " + autotoolTaskConfig.getAutotoolServerUrl(), e);
    } catch (AutolatConnectorException e) {
      throw new TaskModelRuntimeException("Could not connect to autotool.", e);
    } catch (IOException e) {
      throw new TaskModelRuntimeException("Could not parse response from autotool.", e);
    } catch (JDOMException e) {
      throw new TaskModelRuntimeException("Could not parse response from autotool.", e);
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
    Signed<Pair<String, String>> signedTaskConfig = autotoolTaskConfig.getSignedTaskConfig();
		try {
      Triple<Signed<Pair<String, Instance>>, String, Documented<String>> signedTask =
          getAutotoolServices().getTaskInstance(signedTaskConfig, "" + new RandomUtil(randomSeed).getInt(1000000));

      autotoolSubTask.setProblem(signedTask.getSecond());
      autotoolSubTask.setDefaultAnswer(signedTask.getThird().getContents());
      autotoolTaskConfig.setInstance(signedTask.getFirst().getContents().getSecond());
      autotoolTaskConfig.setTaskSignature(signedTask.getFirst().getSignature());
      autotoolTaskConfig.setInstanceTag(signedTask.getFirst().getContents().getFirst());
		} catch (Exception e) {
			throw new TaskApiException(e);
		}
	}

  private AutolatConnector_0_1 getAutotoolServices() throws AutolatConnectorException, MalformedURLException {
		if(ats == null) {
      ats = new XmlRpcAutolatConnector_0_1(new URL(autotoolTaskConfig.getAutotoolServerUrl()));
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
