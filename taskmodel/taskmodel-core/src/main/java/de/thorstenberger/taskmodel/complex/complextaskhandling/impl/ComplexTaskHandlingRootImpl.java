/*

Copyright (C) 2006 Thorsten Berger

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
/**
 *
 */
package de.thorstenberger.taskmodel.complex.complextaskhandling.impl;

import java.util.List;

import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDefRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingRoot;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskHandling;

/**
 * @author Thorsten Berger
 *
 */
public class ComplexTaskHandlingRootImpl implements ComplexTaskHandlingRoot {

	private ComplexTaskHandling complexTaskHandlingElem;
	private ComplexTaskFactory complexTaskFactory;
	private ComplexTaskDefRoot complexTaskDefRoot;

	/**
	 * @param root
	 * @param factory
	 * @param elem
	 */
	public ComplexTaskHandlingRootImpl(ComplexTaskDefRoot root, ComplexTaskFactory factory, ComplexTaskHandling elem) {
		complexTaskDefRoot = root;
		complexTaskFactory = factory;
		complexTaskHandlingElem = elem;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.oldtaskhandling.ComplexTaskHandlingHelper#getNumberOfTries()
	 */
	public int getNumberOfTries(){
		return complexTaskHandlingElem.getTry().size();
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingHelper#addTry(de.thorstenberger.taskmodel.complex.complextaskhandling.Try)
	 */
	public void addTry(Try theTry) {
		TryImpl tryImpl = (TryImpl)theTry;
		complexTaskHandlingElem.getTry().add( tryImpl.getTryType() );
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.complex.complextaskhandling.ComplexTaskHandlingHelper#getRecentTry()
	 */
	public Try getRecentTry() {
    List<ComplexTaskHandling.Try> tries = complexTaskHandlingElem.getTry();
		if( tries.size() != 0 )
      return new TryImpl(tries.get(tries.size() - 1), complexTaskFactory, complexTaskDefRoot);
		else
			return null;
	}

	/**
	 * @return Returns the complexTaskDefRoot.
	 */
	public ComplexTaskDefRoot getComplexTaskDefRoot() {
		return complexTaskDefRoot;
	}

	/**
	 * backdoor access to JAXB element
	 * @return Returns the complexTaskHandlingElem.
	 */
	public ComplexTaskHandling getComplexTaskHandlingElem() {
		return complexTaskHandlingElem;
	}

}
