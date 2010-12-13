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
package de.elatePortal.autotool.config;

import java.io.IOException;
import java.net.URL;

import org.jdom.JDOMException;

import de.htwk.autolat.Connector.AutolatConnectorException;
import de.htwk.autolat.Connector.AutolatConnector_0_1;
import de.htwk.autolat.Connector.types.Documented;
import de.htwk.autolat.Connector.types.Either;
import de.htwk.autolat.Connector.types.Instance;
import de.htwk.autolat.Connector.types.Pair;
import de.htwk.autolat.Connector.types.Signed;
import de.htwk.autolat.Connector.types.TaskDescription;
import de.htwk.autolat.Connector.types.Triple;
import de.htwk.autolat.Connector.xmlrpc.XmlRpcAutolatConnector_0_1;
import de.htwk.autolat.tools.XMLParser.XMLParser;

/**
 * @author Steffen Dienst
 *
 */
public class TestRemoteAutotool {
  public static void main(String[] args) throws AutolatConnectorException, IOException, JDOMException {
    AutolatConnector_0_1 a = new XmlRpcAutolatConnector_0_1(new URL(args[0]));

    System.out.println(a.getServerInfo());
    System.out.println("\nAll task types:");
    System.out.println(a.getTaskTypes());

    String task = "Robots-Quiz";

    System.out.format("\nTaskdescription for %s:", task);
    TaskDescription taskDescription = a.getTaskDescription(task);
    System.out.println(taskDescription);

    System.out.println("\nverified task config signature: ");
    Either<String, Signed<Pair<String, String>>> verifiedTaskConfig = a.verifyTaskConfig(task, taskDescription.getTaskSampleConfig().getContents());
    System.out.println(verifiedTaskConfig.getRight().getSignature());

    System.out.println("\nNew task instance:");
    Triple<Signed<Pair<String, Instance>>, String, Documented<String>> taskInstance = a.getTaskInstance(verifiedTaskConfig.getRight(), "0815");
    System.out.println(taskInstance);

    System.out.println("\nGrade for task instance:");
    Either<String, Documented<Double>> grade = a.gradeTaskSolution(taskInstance.getFirst(), taskInstance.getThird().getContents());
    System.out.println(new XMLParser().parseString(grade.getLeft()));

    System.out.println("\n\n\n\n\n");
    System.out.println(taskInstance.getThird().getDocumentation());
  }
}
