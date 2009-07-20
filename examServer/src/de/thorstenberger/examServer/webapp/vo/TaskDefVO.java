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
package de.thorstenberger.examServer.webapp.vo;

/**
 * @author Thorsten Berger
 * 
 */
public class TaskDefVO {

    private String id;
    private String title;
    private String shortDescription;
    private String type;
    private boolean stopped;
    private String deadline;
    private boolean active;
    private boolean visible;
    private int numberOfOpenCorrections;

    /**
     * @return Returns the deadline.
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * How many tasklets are there where status==SOLVED?
     * 
     * @return the numberOfOpenCorrections
     */
    public int getNumberOfOpenCorrections() {
        return numberOfOpenCorrections;
    }

    /**
     * @return Returns the shortDescription.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @return Returns the active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return Returns the stopped.
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param active
     *            The active to set.
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * @param deadline
     *            The deadline to set.
     */
    public void setDeadline(final String deadline) {
        this.deadline = deadline;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @param numberOfOpenCorrections
     *            the numberOfOpenCorrections to set
     */
    public void setNumberOfOpenCorrections(final int numberOfOpenCorrections) {
        this.numberOfOpenCorrections = numberOfOpenCorrections;
    }

    /**
     * @param shortDescription
     *            The shortDescription to set.
     */
    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * @param stopped
     *            The stopped to set.
     */
    public void setStopped(final boolean stopped) {
        this.stopped = stopped;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @param visible
     *            the visible to set
     */
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

}
