
package de.thorstenberger.examServer.ws.opal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.thorstenberger.examServer.ws.opal package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MemberList_QNAME = new QName("groupmembers.services.webservices.olat.bps.de", "memberList");
    private final static QName _GroupMembersRequest_QNAME = new QName("groupmembers.services.webservices.olat.bps.de", "groupMembersRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.thorstenberger.examServer.ws.opal
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MemberList }
     * 
     */
    public MemberList createMemberList() {
        return new MemberList();
    }

    /**
     * Create an instance of {@link GroupMembersRequestElement }
     * 
     */
    public GroupMembersRequestElement createGroupMembersRequestElement() {
        return new GroupMembersRequestElement();
    }

    /**
     * Create an instance of {@link Member }
     * 
     */
    public Member createMember() {
        return new Member();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MemberList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "groupmembers.services.webservices.olat.bps.de", name = "memberList")
    public JAXBElement<MemberList> createMemberList(MemberList value) {
        return new JAXBElement<MemberList>(_MemberList_QNAME, MemberList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GroupMembersRequestElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "groupmembers.services.webservices.olat.bps.de", name = "groupMembersRequest")
    public JAXBElement<GroupMembersRequestElement> createGroupMembersRequest(GroupMembersRequestElement value) {
        return new JAXBElement<GroupMembersRequestElement>(_GroupMembersRequest_QNAME, GroupMembersRequestElement.class, null, value);
    }

}
