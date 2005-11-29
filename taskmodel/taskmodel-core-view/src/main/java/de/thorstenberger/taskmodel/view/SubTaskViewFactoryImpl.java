/*

Copyright (C) 2004 Thorsten Berger

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
package de.thorstenberger.taskmodel.view;

import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Cloze;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Mapping;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Paint;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_Text;

/**
 * @author Thorsten Berger
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SubTaskViewFactoryImpl implements SubTaskViewFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.thorstenberger.taskmodel.view.SubTaskViewFactory#getSubTaskView(de
     * .thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet)
     */
    public SubTaskView getSubTaskView(final SubTasklet subTasklet) {
        if (subTasklet instanceof SubTasklet_MC) {
            return new SubTaskView_MC((SubTasklet_MC) subTasklet);
        } else if (subTasklet instanceof SubTasklet_Cloze) {
            return new SubTaskView_CLOZE((SubTasklet_Cloze) subTasklet);
        } else if (subTasklet instanceof SubTasklet_Text) {
            return new SubTaskView_TEXT((SubTasklet_Text) subTasklet);
        } else if (subTasklet instanceof SubTasklet_Mapping) {
            return new SubTaskView_Mapping((SubTasklet_Mapping) subTasklet);
        } else if (subTasklet instanceof SubTasklet_Paint) {
            return new SubTaskView_Paint((SubTasklet_Paint) subTasklet);
        } else {
            return null;
        }
    }

}
