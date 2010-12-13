package de.htwk.autolat.Connector.xmlrpc.serialize;
import redstone.xmlrpc.XmlRpcStruct;
import de.htwk.autolat.Connector.types.Signed;

@SuppressWarnings({"unused", "unchecked"})
public class SignedSerializer<A>
    implements Serializer<Signed<A>>
{
    private final Serializer<Signed<A>> serializer;
    
    public SignedSerializer(final Serializer<A> aSerializer)
    {
        serializer =
            new Serializer<Signed<A>>()
            {
                Serializer<A> contentsSerializer = null;
                Serializer<String> signatureSerializer = null;
                
                public Object serialize(Signed<A> val)
                {
                    if (contentsSerializer == null) {
                      contentsSerializer = aSerializer;
                    }
                    if (signatureSerializer == null) {
                      signatureSerializer = StringSerializer.getInstance();
                    }
                    
                    XmlRpcStruct inner = new XmlRpcStruct();
                    inner.put("contents", contentsSerializer.serialize(val.getContents()));
                    inner.put("signature", signatureSerializer.serialize(val.getSignature()));
                    
                    XmlRpcStruct outer = new XmlRpcStruct();
                    outer.put("Signed", inner);
                    return outer;
                }
                
            }
            ;
    }
    
    public Object serialize(Signed<A> val)
    {
        return serializer.serialize(val);
    }
    
}





