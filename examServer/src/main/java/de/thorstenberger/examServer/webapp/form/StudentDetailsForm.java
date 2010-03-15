package de.thorstenberger.examServer.webapp.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


/**
 * @author Steffen Dienst
 * 
 */
public class StudentDetailsForm
    extends    BaseForm
    implements java.io.Serializable
{

    protected String id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String matrikel;
    protected String semester;

    /** Default empty constructor. */
    public StudentDetailsForm() {}

    public String getId()
    {
        return this.id;
    }
   /**
    */

    public void setId( final String id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return this.username;
    }
   /**
    * @struts.validator type="required"
    */

    public void setUsername( final String username )
    {
        this.username = username;
    }


    public String getFirstName()
    {
        return this.firstName;
    }
   /**
    * @struts.validator type="required"
    */

    public void setFirstName( final String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }
   /**
    * @struts.validator type="required"
    */

    public void setLastName( final String lastName )
    {
        this.lastName = lastName;
    }

    /**
     * @return the matrikel
     */
    public String getMatrikel() {
      return matrikel;
    }

    /**
     * @param matrikel the matrikel to set
     */
    public void setMatrikel(final String matrikel) {
      this.matrikel = matrikel;
    }

    /**
     * @return the semester
     */
    public String getSemester() {
      return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(final String semester) {
      this.semester = semester;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *                                                javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(final ActionMapping mapping, final HttpServletRequest request) {
        // reset any boolean data types to false

    }

}
