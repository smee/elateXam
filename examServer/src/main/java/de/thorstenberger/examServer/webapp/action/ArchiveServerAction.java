/*

Copyright (C) 2009 Steffen Dienst

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
package de.thorstenberger.examServer.webapp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.thorstenberger.examServer.service.ExamServerManager;

/**
 * @author Steffen Dienst
 * 
 */
public class ArchiveServerAction extends BaseAction {
	@Override
	public ActionForward execute(
	    final ActionMapping mapping,
	    final ActionForm form,
	    final HttpServletRequest request,
	    final HttpServletResponse response)
	    throws Exception {

		final ExamServerManager esm = (ExamServerManager) getBean("examServerManager");
		File repositoryDirectory = esm.getRepositoryFile();
		String zipName = repositoryDirectory.getName() + ".zip";

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
		ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
		addToZip(repositoryDirectory, out);

		out.flush();
		out.close();
		return null;
	}

	/**
	 * Add directory contents to a {@link ZipOutputStream} recursively.
	 * 
	 * @param directory
	 * @param out
	 * @throws IOException
	 */
	void addToZip(File directory, ZipOutputStream out) throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[20 * 1024];

    for (File file : files) {
			if (file.isDirectory()) {
				addToZip(file, out);
				continue;
			}
			FileInputStream in = new FileInputStream(file.getAbsolutePath());
			out.putNextEntry(new ZipEntry(file.getAbsolutePath()));
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.closeEntry();
			in.close();
		}
	}
}
