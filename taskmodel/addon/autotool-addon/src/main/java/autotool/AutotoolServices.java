package autotool;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class AutotoolServices{

	private static class AutotoolTaskConfigVO implements AutotoolTaskConfig, Serializable {
		private String config;
		private String doc;
		private String taskType;

		private AutotoolTaskConfigVO(String taskType,Map config) {
      Map m = (Map) config.get("TaskDescription");
      System.out.println(m.keySet());
      this.doc = AutotoolServices.trimHtmlTags((String) m.get("documentation"));
      this.config = (String) m.get("task_sample_config");
			this.taskType=taskType;
		}

		public String getDocumentation() {
			return doc;
		}

		public String getConfigString() {
			return config;
		}

		public void setConfigString(String s) {
			this.config=s;
		}
		public String getTaskType() {
			return this.taskType;
		}
	}
	private static class AutotoolGradeVO implements AutotoolGrade, Serializable{
		private String doc;
		private boolean solved;
		private double points;

		public AutotoolGradeVO(Map m, String serverUrlString) {
			this.doc=trimHtmlTags(replaceImgLinks(serverUrlString,(String) m.get("documentation")));
			m=(Map) m.get("contents");
			this.solved=((Boolean)m.get("first")).booleanValue();
			this.points=((Double)m.get("second")).doubleValue();
		}
		public String getGradeDocumentation() {
			return doc;
		}
		public double getPoints() {
			return points;
		}
		public boolean isSolved() {
			return solved;
		}

	}
	private XmlRpcClient client;
	private String serverUrlString;

	public AutotoolServices(URL url) {
		 XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		 config.setServerURL(url);
		 config.setEncoding("ISO-8859-1");
		 config.setConnectionTimeout(10000);
		 client=new XmlRpcClient();
		 client.setConfig(config);
		 this.serverUrlString=url.toExternalForm();
		 this.serverUrlString=serverUrlString.substring(0,serverUrlString.lastIndexOf('/'));
	}

	public List<String> getTaskTypes() throws XmlRpcException {
    Object[] response = (Object[]) client.execute("get_task_types", Collections.EMPTY_LIST);
		List<String> result=new ArrayList<String>(response.length);
		for (Object m : response) {
      result.addAll(findTaskNames(m));
		}
		return result;
	}

  private Collection<? extends String> findTaskNames(Object m) {
    List<String> res = new ArrayList<String>();
    if (m instanceof Map) {
      Map map = (Map) m;
      if (map.containsKey("Task")) {
        res.add((String) ((Map) map.get("Task")).get("task_name"));
      } else {
        for (Object val : map.values()) {
          res.addAll(findTaskNames(val));
        }
      }
    } else if (m instanceof Object[]) {
      for (Object val : (Object[]) m) {
        res.addAll(findTaskNames(val));
      }
    }
    return res;
  }

  public AutotoolTaskConfig getConfig(String taskType) throws XmlRpcException {

    final Object config = client.execute("get_task_description", new Object[] { taskType });
    System.out.println(config);
		return new AutotoolTaskConfigVO(taskType,(Map) config);
	}

	public SignedAutotoolTaskConfig getSignedConfig(AutotoolTaskConfig config) throws XmlRpcException {
    Map m = (Map) client.execute("verify_task_config", new Object[] { struct(config.getTaskType()),
        struct(config.getConfigString()) });
		return new SignedAutotoolTaskConfig.SignedAutotoolTaskConfigVO(m,config);
	}

	public AutotoolTaskInstance getTaskInstance(SignedAutotoolTaskConfig cfg, int seed) throws XmlRpcException {
    Map inst = (Map) client.execute("get_task_instance", new Object[] { struct(cfg.getTaskType()), sigCfg2XmlRpc(cfg),
        struct(new Integer(seed)) });
		return new AutotoolTaskInstance.AutotoolTaskInstanceVO(cfg,inst,serverUrlString);
	}
	public AutotoolGrade gradeTaskInstance(AutotoolTaskInstance inst, String solution) throws XmlRpcException {
    Map grade = (Map) client.execute("grade_task_solution", new Object[] { struct(inst.getTaskType()), inst.getSignedInstance(),
        struct(solution) });
		return new AutotoolGradeVO(grade,serverUrlString);
	}


	@SuppressWarnings("unchecked")
	private Map sigCfg2XmlRpc(SignedAutotoolTaskConfig cfg) {
		Map sigCfg=struct(struct(cfg.getConfigString()));
		sigCfg.put("signature",cfg.getSignature());
		return sigCfg;
	}
	@SuppressWarnings("unchecked")
	private Map struct(Object s) {
		Map parammap=new HashMap();
		parammap.put("contents",s);
		return parammap;

	}

	public static String replaceImgLinks(String serverUrl, String string) {
	if(!serverUrl.endsWith("/")) {
    serverUrl=serverUrl.substring(0,serverUrl.lastIndexOf('/')+1);
  }
		return string.replaceAll("<img src=\"../","<img src=\""+serverUrl);
	}

	public static String trimHtmlTags(String s) {
		StringBuilder sb=new StringBuilder(s.length());
		boolean inTag=false;
		for(int i=0;i<s.length();i++) {
			char c=s.charAt(i);
			switch (c) {
			case '<':
				inTag=true;
				break;
			case '>':
				inTag=false;
				break;
			case '\n':
				if(inTag)
         {
          continue;//skip it
        }
				break;
			default:
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}
}