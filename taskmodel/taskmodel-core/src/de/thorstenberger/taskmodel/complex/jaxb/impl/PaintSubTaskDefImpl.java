//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.07.09 at 10:15:46 CEST 
//


package de.thorstenberger.taskmodel.complex.jaxb.impl;

public class PaintSubTaskDefImpl
    extends de.thorstenberger.taskmodel.complex.jaxb.impl.PaintSubTaskDefTypeImpl
    implements de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef, java.io.Serializable, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.UnmarshallableObject, de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.XMLSerializable, de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.ValidatableObject
{

    private final static long serialVersionUID = 1L;
    public final static java.lang.Class version = (de.thorstenberger.taskmodel.complex.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://complex.taskmodel.thorstenberger.de/complexTaskDef";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "paintSubTaskDef";
    }

    public de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.UnmarshallingContext context) {
        return new de.thorstenberger.taskmodel.complex.jaxb.impl.PaintSubTaskDefImpl.Unmarshaller(context);
    }

    public void serializeBody(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://complex.taskmodel.thorstenberger.de/complexTaskDef", "paintSubTaskDef");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (de.thorstenberger.taskmodel.complex.jaxb.PaintSubTaskDef.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0003I\u0000\u000ecachedHashCodeL\u0000\u0013epsilon"
+"Reducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xp\u0015V\u0017\u0099p"
+"p\u0000sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun."
+"msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~"
+"\u0000\u0004\u0015V\u0017\u008eppsq\u0000~\u0000\u0007\u0013;\u0084\fppsq\u0000~\u0000\u0007\u0010\u00ae\u00fb\u0092ppsq\u0000~\u0000\u0007\u000e\u0000I\u00f4ppsq\u0000~\u0000\u0007\u000b\u00d8E\u008cppsq\u0000~"
+"\u0000\u0007\t\u000ec\u0098ppsq\u0000~\u0000\u0007\u0006\u00877#ppsq\u0000~\u0000\u0007\u0004\u00db\u0005\u00bappsq\u0000~\u0000\u0000\u0001\u00e6\u00b6\u00d3pp\u0000sq\u0000~\u0000\u0007\u0001\u00e6\u00b6\u00c8ppsr\u0000"
+"\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/"
+"datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/"
+"StringPair;xq\u0000~\u0000\u0004\u0000\u00ea\u00f4\u001cppsr\u0000#com.sun.msv.datatype.xsd.StringTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.Bu"
+"iltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concr"
+"eteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImp"
+"l\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq"
+"\u0000~\u0000\u001bL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProc"
+"essor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com."
+"sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"p\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004\u0000\u0000\u0000\nppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L"
+"\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esr\u0000\u001dcom.sun."
+"msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\b\u0000\u00fb\u00c2\u00a7ppsr\u0000 com.sun.msv."
+"grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001x"
+"q\u0000~\u0000\u0004\u0000\u00fb\u00c2\u009csr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0013\u0000"
+"\u00f3\u009bJppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018"
+"q\u0000~\u0000\u001et\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcesso"
+"r$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u00000q\u0000~\u0000\u001esr\u0000#com.sun."
+"msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fna"
+"mespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"pt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com."
+"sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0004\u0000\u0000\u0000\tsq\u0000~\u0000+\u0001psq\u0000~\u00004t\u0000\u0007problemt\u00009http://complex.taskmodel.tho"
+"rstenberger.de/complexTaskDefsq\u0000~\u0000\'\u0002\u00f4N\u00e2ppsq\u0000~\u0000\u0000\u0002\u00f4N\u00d7q\u0000~\u0000,p\u0000sq"
+"\u0000~\u0000\u0007\u0002\u00f4N\u00ccppsq\u0000~\u0000\u0000\u0000;\u00c2ypp\u0000sq\u0000~\u0000\'\u0000;\u00c2nppsr\u0000 com.sun.msv.grammar.O"
+"neOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004\u0000;\u00c2cq\u0000~\u0000,psq\u0000~\u0000)\u0000;\u00c2`q\u0000~\u0000,psr\u00002com.sun."
+"msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004\u0000"
+"\u0000\u0000\bq\u0000~\u0000;q\u0000~\u0000Isr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u00005q\u0000~\u0000:sq\u0000~\u00004t\u0000Gde.thorstenberger.taskmodel.complex.jaxb"
+".PaintSubTaskDefType.ImagesTypet\u0000+http://java.sun.com/jaxb/x"
+"jc/dummy-elementssq\u0000~\u0000\'\u0002\u00b8\u008cNppsq\u0000~\u0000)\u0002\u00b8\u008cCq\u0000~\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u0000"
+"7q\u0000~\u00008q\u0000~\u0000:sq\u0000~\u00004t\u0000\u0006imagesq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'\u0001\u00ac1dppsq\u0000~\u0000\u0000\u0001\u00ac1Yq\u0000"
+"~\u0000,p\u0000sq\u0000~\u0000\u0007\u0001\u00ac1Nppsq\u0000~\u0000\u0000\u0000;\u00c2ypp\u0000sq\u0000~\u0000\'\u0000;\u00c2nppsq\u0000~\u0000D\u0000;\u00c2cq\u0000~\u0000,psq"
+"\u0000~\u0000)\u0000;\u00c2`q\u0000~\u0000,pq\u0000~\u0000Iq\u0000~\u0000Kq\u0000~\u0000:sq\u0000~\u00004t\u0000Nde.thorstenberger.task"
+"model.complex.jaxb.PaintSubTaskDefType.TextualAnswerTypeq\u0000~\u0000"
+"Nsq\u0000~\u0000\'\u0001pn\u00d0ppsq\u0000~\u0000)\u0001pn\u00c5q\u0000~\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q\u0000~\u00008q\u0000~\u0000:sq\u0000~\u0000"
+"4t\u0000\rtextualAnswerq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'\u0002\u0087,pppsq\u0000~\u0000\u0000\u0002\u0087,eq\u0000~\u0000,p\u0000sq\u0000~"
+"\u0000\u0007\u0002\u0087,Zppq\u0000~\u0000\u0016sq\u0000~\u0000\'\u0001\u009c89ppsq\u0000~\u0000)\u0001\u009c8.q\u0000~\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q\u0000~"
+"\u00008q\u0000~\u0000:sq\u0000~\u00004t\u0000\u0004hintq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'\u0002\u00c9\u00e1\u00efppsq\u0000~\u0000\u0000\u0002\u00c9\u00e1\u00e4q\u0000~\u0000,p\u0000s"
+"q\u0000~\u0000\u0007\u0002\u00c9\u00e1\u00d9ppsq\u0000~\u0000\u0013\u0000\u0011\u0000\u0013ppsr\u0000$com.sun.msv.datatype.xsd.BooleanT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007booleanq\u0000~\u00002q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000pq\u0000~\u0000"
+"\u001esq\u0000~\u0000\'\u0002\u00b8\u00e1\u00c1ppsq\u0000~\u0000)\u0002\u00b8\u00e1\u00b6q\u0000~\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q\u0000~\u00008q\u0000~\u0000:sq\u0000~\u0000"
+"4t\u0000\u000fcolorChangeableq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'\u0002(\u0004cppsq\u0000~\u0000\u0000\u0002(\u0004Xq\u0000~\u0000,p\u0000sq"
+"\u0000~\u0000\u0007\u0002(\u0004Mppq\u0000~\u0000msq\u0000~\u0000\'\u0002\u0017\u00045ppsq\u0000~\u0000)\u0002\u0017\u0004*q\u0000~\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q"
+"\u0000~\u00008q\u0000~\u0000:sq\u0000~\u00004t\u0000\u0015strokewidthChangeableq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'\u0002\u00ae\u00b1\u0099p"
+"psq\u0000~\u0000\u0000\u0002\u00ae\u00b1\u008eq\u0000~\u0000,p\u0000sq\u0000~\u0000\u0007\u0002\u00ae\u00b1\u0083ppq\u0000~\u0000\u0016sq\u0000~\u0000\'\u0001\u00c3\u00bdbppsq\u0000~\u0000)\u0001\u00c3\u00bdWq\u0000~"
+"\u0000,pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q\u0000~\u00008q\u0000~\u0000:sq\u0000~\u00004t\u0000\u000ecorrectionHintq\u0000~\u0000>q\u0000~"
+"\u0000:sq\u0000~\u0000)\u0002\u008c\u0088uppq\u0000~\u0000\u0016sq\u0000~\u00004t\u0000\u0002idt\u0000\u0000sq\u0000~\u0000\'\u0002\u001a\u0093}ppsq\u0000~\u0000)\u0002\u001a\u0093rq\u0000~\u0000,"
+"pq\u0000~\u0000-sq\u0000~\u00004q\u0000~\u00007q\u0000~\u00008q\u0000~\u0000:sq\u0000~\u00004t\u0000\u000fpaintSubTaskDefq\u0000~\u0000>sr\u0000\""
+"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/L"
+"com/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun."
+"msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0002\u0000\u0004I\u0000\u0005countI\u0000\tt"
+"hresholdL\u0000\u0006parentq\u0000~\u0000\u0091[\u0000\u0005tablet\u0000![Lcom/sun/msv/grammar/Expre"
+"ssion;xp\u0000\u0000\u0000!\u0000\u0000\u00009pur\u0000![Lcom.sun.msv.grammar.Expression;\u00d68D\u00c3]\u00ad"
+"\u00a7\n\u0002\u0000\u0000xp\u0000\u0000\u0000\u00bfppppppppppppq\u0000~\u0000zppppppppppq\u0000~\u0000(q\u0000~\u0000\u000fppq\u0000~\u0000\u0082ppppp"
+"ppppppppppppq\u0000~\u0000ypppppppppppq\u0000~\u0000\tppppq\u0000~\u0000\rppppq\u0000~\u0000wq\u0000~\u0000\u0010q\u0000~\u0000"
+"dpq\u0000~\u0000\u000bpppq\u0000~\u0000rpppppppppppppq\u0000~\u0000\u008bq\u0000~\u0000bppppppq\u0000~\u0000\u0012pppq\u0000~\u0000\u0081ppp"
+"pppq\u0000~\u0000lpppppppppq\u0000~\u0000\u000eq\u0000~\u0000Vpppq\u0000~\u0000Fq\u0000~\u0000Aq\u0000~\u0000Yq\u0000~\u0000\u007fpppq\u0000~\u0000jpp"
+"pq\u0000~\u0000Cq\u0000~\u0000Xq\u0000~\u0000\fppppq\u0000~\u0000Tppppq\u0000~\u0000?pppppppq\u0000~\u0000\npppppq\u0000~\u0000]pppp"
+"q\u0000~\u0000Opppppppppppppppppppq\u0000~\u0000eppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(de.thorstenberger.taskmodel.complex.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return de.thorstenberger.taskmodel.complex.jaxb.impl.PaintSubTaskDefImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("paintSubTaskDef" == ___local)&&("http://complex.taskmodel.thorstenberger.de/complexTaskDef" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("paintSubTaskDef" == ___local)&&("http://complex.taskmodel.thorstenberger.de/complexTaskDef" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  1 :
                        if (("id" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((de.thorstenberger.taskmodel.complex.jaxb.impl.PaintSubTaskDefTypeImpl)de.thorstenberger.taskmodel.complex.jaxb.impl.PaintSubTaskDefImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  1 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                        case  1 :
                            attIdx = context.getAttribute("", "id");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}