//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.09.24 at 05:49:38 CEST 
//


package de.thorstenberger.examServer.dao.xml.jaxb.impl;

public class ConfigImpl
    extends de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigTypeImpl
    implements de.thorstenberger.examServer.dao.xml.jaxb.Config, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.UnmarshallableObject, de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.XMLSerializable, de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (de.thorstenberger.examServer.dao.xml.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (de.thorstenberger.examServer.dao.xml.jaxb.Config.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://examServer.thorstenberger.de/config";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "config";
    }

    public de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.UnmarshallingContext context) {
        return new de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigImpl.Unmarshaller(context);
    }

    public void serializeBody(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://examServer.thorstenberger.de/config", "config");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (de.thorstenberger.examServer.dao.xml.jaxb.Config.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0003I\u0000\u000ecachedHashCodeL\u0000\u0013epsilon"
+"Reducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xp\rh\u008a\u0083p"
+"p\u0000sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun."
+"msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~"
+"\u0000\u0004\rh\u008axppsq\u0000~\u0000\u0007\u000b\u00fd\u00ef\u0016ppsq\u0000~\u0000\u0007\t[\u0093\u001fppsq\u0000~\u0000\u0007\u0005\u0087\u00b7\u00abppsr\u0000\u001dcom.sun.msv."
+"grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\b\u0003\u0016\\\u0002ppsq\u0000~\u0000\u0000\u0003\u0016[\u00f7sr\u0000\u0011java.l"
+"ang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007\u0003\u0016[\u00ecppsr\u0000\u001bcom.sun.m"
+"sv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/D"
+"atatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair"
+";xq\u0000~\u0000\u0004\u0001\u001f\u00f4Fppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomi"
+"cType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nwhi"
+"teSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000"
+" http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.da"
+"tatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com."
+"sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0004\u0000\u0000\u0000\nppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNam"
+"eq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esq\u0000~\u0000\r\u0001\u00f6g\u00a1ppsr\u0000 com.su"
+"n.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClass"
+"q\u0000~\u0000\u0001xq\u0000~\u0000\u0004\u0001\u00f6g\u0096q\u0000~\u0000\u0011psq\u0000~\u0000\u0013\u0000\u0089\u00cf.ppsr\u0000\"com.sun.msv.datatype.xs"
+"d.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0005QNamesr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000$"
+"sq\u0000~\u0000%q\u0000~\u0000-q\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.g"
+"rammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/200"
+"1/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsil"
+"onExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004\u0000\u0000\u0000\tsq\u0000~\u0000\u0010\u0001psq\u0000~\u00001t\u0000\u0005titlet\u0000*ht"
+"tp://examServer.thorstenberger.de/configq\u0000~\u00007sq\u0000~\u0000\r\u0002q[\u00a4ppsq\u0000"
+"~\u0000\u0000\u0002q[\u0099q\u0000~\u0000\u0011p\u0000sq\u0000~\u0000\u0007\u0002q[\u008eppq\u0000~\u0000\u0016sq\u0000~\u0000\r\u0001QgCppsq\u0000~\u0000(\u0001Qg8q\u0000~\u0000\u0011pq"
+"\u0000~\u0000*sq\u0000~\u00001q\u0000~\u00004q\u0000~\u00005q\u0000~\u00007sq\u0000~\u00001t\u0000\u0014RemoteUserManagerURLq\u0000~\u0000;q"
+"\u0000~\u00007sq\u0000~\u0000\u0000\u0003\u00d3\u00dbopp\u0000sq\u0000~\u0000\u0007\u0003\u00d3\u00dbdppsq\u0000~\u0000\u0013\u0001\u00c7OGppsr\u0000$com.sun.msv.dat"
+"atype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007booleanq\u0000~\u0000/q\u0000"
+"~\u0000$sq\u0000~\u0000%q\u0000~\u0000Iq\u0000~\u0000\u001esq\u0000~\u0000\r\u0002\f\u008c\u0018ppsq\u0000~\u0000(\u0002\f\u008c\rq\u0000~\u0000\u0011pq\u0000~\u0000*sq\u0000~\u00001q\u0000"
+"~\u00004q\u0000~\u00005q\u0000~\u00007sq\u0000~\u00001t\u0000\u0014studentsLoginEnabledq\u0000~\u0000;sq\u0000~\u0000\u0000\u0002\u00a2[\u00f2pp\u0000"
+"sq\u0000~\u0000\u0007\u0002\u00a2[\u00e7ppq\u0000~\u0000Fsq\u0000~\u0000\r\u0000\u00db\f\u009bppsq\u0000~\u0000(\u0000\u00db\f\u0090q\u0000~\u0000\u0011pq\u0000~\u0000*sq\u0000~\u00001q\u0000~\u0000"
+"4q\u0000~\u00005q\u0000~\u00007sq\u0000~\u00001t\u0000\u0010loadJVMOnStartupq\u0000~\u0000;sq\u0000~\u0000\r\u0001j\u009b]ppsq\u0000~\u0000(\u0001"
+"j\u009bRq\u0000~\u0000\u0011pq\u0000~\u0000*sq\u0000~\u00001q\u0000~\u00004q\u0000~\u00005q\u0000~\u00007sq\u0000~\u00001t\u0000\u0006configq\u0000~\u0000;sr\u0000\"c"
+"om.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lc"
+"om/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.m"
+"sv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0002\u0000\u0004I\u0000\u0005countI\u0000\tth"
+"resholdL\u0000\u0006parentq\u0000~\u0000][\u0000\u0005tablet\u0000![Lcom/sun/msv/grammar/Expres"
+"sion;xp\u0000\u0000\u0000\u000f\u0000\u0000\u00009pur\u0000![Lcom.sun.msv.grammar.Expression;\u00d68D\u00c3]\u00ad\u00a7"
+"\n\u0002\u0000\u0000xp\u0000\u0000\u0000\u00bfpppq\u0000~\u0000\npppppppppppppq\u0000~\u0000<pppppppppppppppppppppppp"
+"pppppppppq\u0000~\u0000Rppppppppppppq\u0000~\u0000\u0012q\u0000~\u0000Qq\u0000~\u0000\'ppppppppppppppppppp"
+"q\u0000~\u0000\u000epppppppppppppppppppppq\u0000~\u0000\fppppq\u0000~\u0000\u000bpppppppppppppppppppp"
+"ppppppppppppppppppppppppq\u0000~\u0000Wpppppppq\u0000~\u0000Kq\u0000~\u0000\tppppppppppppq\u0000"
+"~\u0000Epppppq\u0000~\u0000>pq\u0000~\u0000?pp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(de.thorstenberger.examServer.dao.xml.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("config" == ___local)&&("http://examServer.thorstenberger.de/config" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("title" == ___local)&&("http://examServer.thorstenberger.de/config" == ___uri)) {
                            spawnHandlerFromEnterElement((((de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigTypeImpl)de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("RemoteUserManagerURL" == ___local)&&("http://examServer.thorstenberger.de/config" == ___uri)) {
                            spawnHandlerFromEnterElement((((de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigTypeImpl)de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("studentsLoginEnabled" == ___local)&&("http://examServer.thorstenberger.de/config" == ___uri)) {
                            spawnHandlerFromEnterElement((((de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigTypeImpl)de.thorstenberger.examServer.dao.xml.jaxb.impl.ConfigImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  2 :
                        if (("config" == ___local)&&("http://examServer.thorstenberger.de/config" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  3 :
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}