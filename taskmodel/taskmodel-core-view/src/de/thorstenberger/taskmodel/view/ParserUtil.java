/*

Copyright (C) 2005 Thorsten Berger

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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Thorsten Berger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParserUtil {

    /**
     * 
     */
    public ParserUtil() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static String escapeCR( String text ){
    	if( text == null )
    		return null;
        return text.replaceAll( "\n", "<br>" );
    }
    
    public static String escapeHTML( String text ){
    	if( text == null )
    		return null;
        return text.replaceAll( "<" , "&lt;" ).
        				replaceAll( ">" , "&gt;" );
    }

	public static String parseAndMerge( String text, Map<String, String> context ){
		
		StringBuffer ret = new StringBuffer();
		StringTokenizer st = new StringTokenizer(text, "$", true);
		Set<String> keys = context.keySet();
		boolean found = false;
		
		
		while (st.hasMoreTokens()) {
			found = false;
			
			String token = st.nextToken();
			
			if (token.equals("$")) {
				token = st.nextToken();
	
				Iterator<String> it = keys.iterator();
				
				while( it.hasNext() ){
					String key = it.next();
					
					if ( token.startsWith( key ) ){
						found = true;				
						ret.append( context.get( key ) );
						ret.append(token.substring( key.length()) );
						break;
					}
					
				}
				
				if( !found )  { // unknown key, leave in text
					ret.append( "$" + token );
				}
			}
			else
				ret.append(token);
		}
		
		return ret.toString();

	}

}
