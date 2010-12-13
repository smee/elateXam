package de.htwk.autolat.Connector.xmlrpc.parse;
import de.htwk.autolat.Connector.types.Documented;
import de.htwk.autolat.Connector.types.ScoringOrder;
import de.htwk.autolat.Connector.types.TaskDescription;

@SuppressWarnings("unused")
public class TaskDescriptionParser
{
    private static final Parser<TaskDescription> inst =
        new StructFieldParser<TaskDescription>(
            "TaskDescription",
            new Parser<TaskDescription>()
            {
                Parser<Documented<String>> taskSampleConfigParser = null;
                Parser<ScoringOrder> taskScoringOrderParser = null;
                
                public TaskDescription parse(Object val) throws ParseErrorBase
                {
                    if (taskSampleConfigParser == null) {
                      taskSampleConfigParser = new StructFieldParser<Documented<String>>(
                                                   "task_sample_config",
                                                   new DocumentedParser<String>(StringParser.getInstance()));
                    }
                    if (taskScoringOrderParser == null) {
                      taskScoringOrderParser = new StructFieldParser<ScoringOrder>(
                                                   "task_scoring_order",
                                                   ScoringOrderParser.getInstance());
                    }
                    return new TaskDescription(
                        taskSampleConfigParser.parse(val),
                        taskScoringOrderParser.parse(val));
                }
                
            }
            
        );
    
    public static Parser<TaskDescription> getInstance()
    {
        return inst;
    }
    
}





