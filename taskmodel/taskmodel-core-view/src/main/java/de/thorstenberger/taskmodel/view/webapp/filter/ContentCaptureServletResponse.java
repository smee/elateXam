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
package de.thorstenberger.taskmodel.view.webapp.filter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrapper for {@link HttpServletResponse} that stores every written byte
 * instead of writing it to the original outputstream.
 * 
 * @author sdienst
 * 
 */
public class ContentCaptureServletResponse extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer;
    private PrintWriter writer;

    /**
     * @param resp
     */
    public ContentCaptureServletResponse(final HttpServletResponse resp) {
        super(resp);
    }


    /**
     * @return
     */
    public byte[] getBinaryContent() {
        if (writer != null) {
            writer.flush();
        }
        return buffer.toByteArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (buffer == null) {
            buffer = new ByteArrayOutputStream();
        }
        return new ServletOutputStream() {

            @Override
            public void write(final int b) throws IOException {
                buffer.write(b);
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            buffer = new ByteArrayOutputStream();
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(buffer, "UTF8")),  false);
        }
        return writer;
    }

}
