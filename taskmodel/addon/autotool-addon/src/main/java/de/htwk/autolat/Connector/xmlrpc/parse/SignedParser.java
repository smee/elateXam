package de.htwk.autolat.Connector.xmlrpc.parse;


import de.htwk.autolat.Connector.types.Signed;

@SuppressWarnings("unused")
public class SignedParser<A>
    implements Parser<Signed<A>>
{
    private final Parser<Signed<A>> parser;
    
    public SignedParser(final Parser<A> aParser)
    {
        parser = new StructFieldParser<Signed<A>>(
                     "Signed",
                     new Parser<Signed<A>>()
                     {
                         Parser<A> contentsParser = null;
                         Parser<String> signatureParser = null;
                         
                         public Signed<A> parse(Object val) throws ParseErrorBase
                         {
                             if (contentsParser == null) {
                              contentsParser = new StructFieldParser<A>(
                                                    "contents",
                                                    aParser);
                            }
                             if (signatureParser == null) {
                              signatureParser = new StructFieldParser<String>(
                                                     "signature",
                                                     StringParser.getInstance());
                            }
                             return new Signed<A>(
                                 contentsParser.parse(val),
                                 signatureParser.parse(val));
                         }
                         
                     }
                     
                 );
    }
    
    public Signed<A> parse(Object val) throws ParseErrorBase
    {
        return parser.parse(val);
    }
    
}





