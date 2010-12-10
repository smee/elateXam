/*

Copyright (C) 2010 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package de.thorstenberger.taskmodel.util;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefDAOImpl;

/**
 * @author Steffen Dienst
 *
 */
public class JAXB2Validation {

  /**
   * Tries to load an xml schema file from classpath.
   *
   * @return null if there is none
   */
  public static Schema loadSchema(String nameOnClasspath) {
    try {
      SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      return sf.newSchema(ComplexTaskDefDAOImpl.class.getClassLoader().getResource(nameOnClasspath));
    } catch (SAXException e) {
      ComplexTaskDefDAOImpl.logger.warn("Could not load xml schema complexTaskDef.xsd, not available on classpath.");
      return null;
    }
  }

}
