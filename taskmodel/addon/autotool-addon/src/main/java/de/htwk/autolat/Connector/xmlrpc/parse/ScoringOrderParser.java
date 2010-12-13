package de.htwk.autolat.Connector.xmlrpc.parse;
import de.htwk.autolat.Connector.types.Decreasing;
import de.htwk.autolat.Connector.types.Increasing;
import de.htwk.autolat.Connector.types.None;
import de.htwk.autolat.Connector.types.ScoringOrder;

@SuppressWarnings("unused")
public class ScoringOrderParser
{
    private static final Parser<ScoringOrder> inst =
        new AlternativeParser<ScoringOrder>(
            new StructFieldParser<ScoringOrder>(
                "Increasing",
                new Parser<ScoringOrder>()
                {
                    
                    public ScoringOrder parse(Object val) throws ParseErrorBase
                    {
                        return new Increasing(
                            );
                    }
                    
                }
                
            ),
            new AlternativeParser<ScoringOrder>(
                new StructFieldParser<ScoringOrder>(
                    "None",
                    new Parser<ScoringOrder>()
                    {
                        
                        public ScoringOrder parse(Object val) throws ParseErrorBase
                        {
                            return new None(
                                );
                        }
                        
                    }
                    
                ),
                new StructFieldParser<ScoringOrder>(
                    "Decreasing",
                    new Parser<ScoringOrder>()
                    {
                        
                        public ScoringOrder parse(Object val) throws ParseErrorBase
                        {
                            return new Decreasing(
                                );
                        }
                        
                    }
                    
                )
            )
        );
    
    public static Parser<ScoringOrder> getInstance()
    {
        return inst;
    }
    
}





