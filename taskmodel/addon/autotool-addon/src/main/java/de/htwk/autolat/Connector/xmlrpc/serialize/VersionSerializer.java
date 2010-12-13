package de.htwk.autolat.Connector.xmlrpc.serialize;
import redstone.xmlrpc.XmlRpcStruct;
import de.htwk.autolat.Connector.types.Version;

@SuppressWarnings({"unused", "unchecked"})
public class VersionSerializer
{
    private static final Serializer<Version> inst =
        new Serializer<Version>()
        {
            Serializer<Integer> majorSerializer = null;
            Serializer<Integer> minorSerializer = null;
            Serializer<Integer> microSerializer = null;
            
            public Object serialize(Version val)
            {
                if (majorSerializer == null) {
                  majorSerializer = IntegerSerializer.getInstance();
                }
                if (minorSerializer == null) {
                  minorSerializer = IntegerSerializer.getInstance();
                }
                if (microSerializer == null) {
                  microSerializer = IntegerSerializer.getInstance();
                }
                
                XmlRpcStruct inner = new XmlRpcStruct();
                inner.put("major", majorSerializer.serialize(new Integer(val.getMajor())));
                inner.put("minor", minorSerializer.serialize(new Integer(val.getMinor())));
                inner.put("micro", microSerializer.serialize(new Integer(val.getMicro())));
                
                XmlRpcStruct outer = new XmlRpcStruct();
                outer.put("Version", inner);
                return outer;
            }
            
        }
        ;
    
    public static Serializer<Version> getInstance()
    {
        return inst;
    }
    
}





