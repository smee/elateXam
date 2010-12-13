package de.htwk.autolat.Connector.xmlrpc.serialize;
import redstone.xmlrpc.XmlRpcStruct;
import de.htwk.autolat.Connector.types.Instance;

@SuppressWarnings({"unused", "unchecked"})
public class InstanceSerializer
{
    private static final Serializer<Instance> inst =
        new Serializer<Instance>()
        {
            Serializer<String> tagSerializer = null;
            Serializer<String> contentsSerializer = null;
            
            public Object serialize(Instance val)
            {
                if (tagSerializer == null) {
                  tagSerializer = StringSerializer.getInstance();
                }
                if (contentsSerializer == null) {
                  contentsSerializer = StringSerializer.getInstance();
                }
                
                XmlRpcStruct inner = new XmlRpcStruct();
                inner.put("tag", tagSerializer.serialize(val.getTag()));
                inner.put("contents", contentsSerializer.serialize(val.getContents()));
                
                XmlRpcStruct outer = new XmlRpcStruct();
                outer.put("Instance", inner);
                return outer;
            }
            
        }
        ;
    
    public static Serializer<Instance> getInstance()
    {
        return inst;
    }
    
}





