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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * 
 * some methods for date handling
 * 
 * @author Thorsten Berger
 *
 */
public class DateUtil {


	public static long getMillisFromString( String s ) throws ParseException{

		// TODO locale support
		DateFormat formater=DateFormat.getDateTimeInstance();
		Date date=formater.parse(s);
		
		return date.getTime();
	}
	
	
	public static String getStringFromMillis( long timestamp ){

		DateFormat df = DateFormat.
							getDateTimeInstance( );
		return df.format( new Date( timestamp ) );
	}

	
//	public static String getStringFromMillis( Long date, Locale locale ){
//		if( date == null )
//			return null;
//		return getStringFromMillis( date.longValue(), locale );
//	}

	public static void main( String a[] ){
		System.out.println( getStringFromMillis( System.currentTimeMillis() ) );
	}
	
}
