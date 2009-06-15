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
package de.thorstenberger.taskmodel.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import de.thorstenberger.taskmodel.ManualCorrection;
import de.thorstenberger.taskmodel.MethodNotSupportedException;
import de.thorstenberger.taskmodel.ReportBuilder;
import de.thorstenberger.taskmodel.TaskApiException;
import de.thorstenberger.taskmodel.TaskDef;
import de.thorstenberger.taskmodel.TaskManager;
import de.thorstenberger.taskmodel.Tasklet;
import de.thorstenberger.taskmodel.UserInfo;
import de.thorstenberger.taskmodel.TaskManager.UserAttribute;
import de.thorstenberger.taskmodel.complex.ComplexTasklet;
import de.thorstenberger.taskmodel.complex.TaskDef_Complex;
import de.thorstenberger.taskmodel.complex.complextaskdef.Block;
import de.thorstenberger.taskmodel.complex.complextaskdef.Category;
import de.thorstenberger.taskmodel.complex.complextaskdef.Choice;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefOrChoice;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Page;
import de.thorstenberger.taskmodel.complex.complextaskhandling.SubTasklet;
import de.thorstenberger.taskmodel.complex.complextaskhandling.Try;
import de.thorstenberger.taskmodel.complex.complextaskhandling.subtasklets.SubTasklet_MC;

/**
 * @author Thorsten Berger
 *
 */
public class ReportBuilderImpl implements ReportBuilder {

	private TaskManager taskManager;
	
	Log log = LogFactory.getLog( ReportBuilderImpl.class );
	
	private static DateFormat df = DateFormat.getDateTimeInstance( );
	
	/**
	 * @param taskletContainer
	 */
	public ReportBuilderImpl( TaskManager taskManager ) {
		this.taskManager = taskManager;
	}

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.ReportBuilder#createExcelBinary(long, java.io.OutputStream)
	 */
	public void createExcelBinary(long taskId, OutputStream out) throws TaskApiException, MethodNotSupportedException{

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		TaskDef taskDef = taskManager.getTaskDef( taskId );
		
		short r = 0;
		short c = 0;
		
		List<Tasklet> tasklets = taskManager.getTaskletContainer().getTasklets( taskId );

		///////////////////
		// create header
		HSSFRow row = sheet.createRow( r++ );
			row.createCell( c++ ).setCellValue( "Login" );
			row.createCell( c++ ).setCellValue( "Vorname" );
			row.createCell( c++ ).setCellValue( "Name" );
			List<UserAttribute> uas = taskManager.availableUserAttributes();
			for( UserAttribute ua : uas )
				row.createCell( c++ ).setCellValue( ua.getName( null ) );
			row.createCell( c++ ).setCellValue( "Status" );
			
			// add header cols for automatic and manual corrections
			row.createCell( c++ ).setCellValue( "autom. Korrektur" );
			// determine number of correctors
			int maxManualCorrectors = 0;
			for( Tasklet tasklet : tasklets ){
				List<ManualCorrection> mcs = tasklet.getTaskletCorrection().getManualCorrections();
				if( mcs != null && mcs.size() > maxManualCorrectors )
					maxManualCorrectors = mcs.size();
			}
			
			// ok, so add header cols for every corrector, as determined above
			for( int i = 1; i<=maxManualCorrectors; i++ )
				row.createCell( c++ ).setCellValue( "Punkte " + i + ". Korrektor" );
			for( int i = 1; i<=maxManualCorrectors; i++ )
				row.createCell( c++ ).setCellValue( "Name " + i + ". Korrektor" );
			
			row.createCell( c++ ).setCellValue( "zugeordneter Korrektor" );
			row.createCell( c++ ).setCellValue( "Zuordnungs-History" );
			
			// add some more columns, if complex task; e.g. points per category
			if( taskDef instanceof TaskDef_Complex ){
				
				row.createCell( c++ ).setCellValue( "Startzeit" );
				
				TaskDef_Complex ctd = (TaskDef_Complex) taskDef;
				List<Category> categories = ctd.getComplexTaskDefRoot().getCategoriesList();
				for( Category category : categories )
					row.createCell( c++ ).setCellValue( category.getTitle() + " (" + category.getId() + ")" );
							
			}
			
			// show tasklet flags
			row.createCell( c++ ).setCellValue( "Flags" );

		
		// end create header
		//////////////////////
			
		
		for( Tasklet tasklet : tasklets ){

			if( tasklet.getStatus() == Tasklet.Status.INITIALIZED )
				continue;
			
			row = sheet.createRow( r++ );
			c = 0;
			
			c = createUserInfoColumns( tasklet, c, wb, row );
			row.createCell( c++ ).setCellValue( tasklet.getStatus().toString() );
			
			// auto correction points
			row.createCell( c++ ).setCellValue( tasklet.getTaskletCorrection().getAutoCorrectionPoints() != null ? "" + tasklet.getTaskletCorrection().getAutoCorrectionPoints() : "-" );
			List<ManualCorrection> mcs = tasklet.getTaskletCorrection().getManualCorrections();
			for( int i = 0; i < maxManualCorrectors; i++ ){
				if( mcs != null && mcs.size() > i )
					row.createCell( c++ ).setCellValue( mcs.get( i ).getPoints() );
				else
					row.createCell( c++ ).setCellValue( "-" );
			}
			for( int i = 0; i < maxManualCorrectors; i++ ){
				if( mcs != null && mcs.size() > i )
					row.createCell( c++ ).setCellValue( mcs.get( i ).getCorrector() );
				else
					row.createCell( c++ ).setCellValue( "-" );
			}
			
			row.createCell( c++ ).setCellValue( tasklet.getTaskletCorrection().getCorrector() != null ? tasklet.getTaskletCorrection().getCorrector() : "-" );
			row.createCell( c++ ).setCellValue( tasklet.getTaskletCorrection().getCorrectorHistory().toString() );

			// add the additional cols for complex tasks
			if( taskDef instanceof TaskDef_Complex ){
				
				TaskDef_Complex ctd = (TaskDef_Complex) taskDef;
//				if( ctd.getComplexTaskDefRoot().getCorrectionMode().getType() == ComplexTaskDefRoot.CorrectionModeType.MULTIPLECORRECTORS )
//					throw new IllegalStateException( "MultiCorrectorMode not supported yet. Stay tuned!" );
					
				ComplexTasklet ct = (ComplexTasklet)tasklet;
				row.createCell( c++ ).setCellValue( getStringFromMillis( ct.getSolutionOfLatestTry().getStartTime() ) );
				
				List<Category> categories = ctd.getComplexTaskDefRoot().getCategoriesList();
				
				// points per category
				Map<String, Float> pointsInCatMap = new Hashtable<String, Float>();
				
				// init every category with 0 points
				for( Category category : categories )
					pointsInCatMap.put( category.getId(), 0f );
				
				Try studentsLastTry;
				    
				try {
					studentsLastTry = ct.getSolutionOfLatestTry();
				} catch (IllegalStateException e) {
					log.error( "Error generating excel stream: ", e );
					throw new TaskApiException( e );
				}
					
				List<Page> pages = studentsLastTry.getPages();

				for( Page page : pages ){
					Float points = pointsInCatMap.get( page.getCategoryRefId() );
					
					// continue if marked as uncorrected earlier
					if( points == null )
						continue;
					
					boolean markCategoryAsUncorrected = false;
					
					List<SubTasklet> subTasklets = page.getSubTasklets();
					for( SubTasklet subTasklet : subTasklets ){
						if( subTasklet.isCorrected() ){
							points += subTasklet.isAutoCorrected() ? subTasklet.getAutoCorrection().getPoints() : subTasklet.getManualCorrections().get( 0 ).getPoints();
						}else{
							markCategoryAsUncorrected = true;
							break;
						}
					}
					
					if( markCategoryAsUncorrected )
						pointsInCatMap.remove( page.getCategoryRefId() );
					else
						pointsInCatMap.put( page.getCategoryRefId(), points );
					
				}
				
				for( Category category : categories ){
					Float points = pointsInCatMap.get( category.getId() );
					row.createCell( c++ ).setCellValue( points != null ? "" + points : "-" );
				}
				
			}
			
			row.createCell( c++ ).setCellValue( tasklet.getFlags().toString() );
			
		}
		
		try {
			wb.write( out );
			out.flush();
		} catch (IOException e) {
			log.error( "Error writing excel stream!", e );
			throw new TaskApiException( e );
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				throw new TaskApiException( e );
			}			
		}
			
	}
	
	private short createUserInfoColumns( Tasklet tasklet, short c, HSSFWorkbook wb, HSSFRow row ){
		UserInfo userInfo = taskManager.getUserInfo( tasklet.getUserId() );
		List<UserAttribute> uas = taskManager.availableUserAttributes();
		
		String login;
		String firstName;
		String name;
		List<String> userAttributeValues = new LinkedList<String>();;
		boolean notfound;
		
		if( userInfo != null ){
			login = userInfo.getLogin();
			firstName = userInfo.getFirstName();
			name = userInfo.getName();
			for( UserAttribute ua : uas ){
				String s = userInfo.getUserAttributeValue( ua.getKey() );
				if( s == null || s.trim().length() == 0 )
					s = "-";
				userAttributeValues.add( s );
				
			}
			notfound = false;	
		}else{
			login = tasklet.getUserId();
			firstName = "?";
			name = "?";
			for( UserAttribute ua : uas )
				userAttributeValues.add( "?" );
			notfound = true;
		}
		
		
		if( notfound ){
			HSSFCellStyle cs2 = wb.createCellStyle();
			HSSFFont font2 = wb.createFont(); font2.setColor( HSSFColor.RED.index );
			cs2.setFont( font2 );		
			HSSFCell cell2 = row.createCell( c++ );
			cell2.setCellStyle( cs2 );
			cell2.setCellValue( login );
		}else{
			row.createCell( c++ ).setCellValue( login );
		}		
		
		row.createCell( c++ ).setCellValue( firstName );
		row.createCell( c++ ).setCellValue( name );
		for( String uav : userAttributeValues )
			row.createCell( c++ ).setCellValue( uav );
			
		
		return c;
	}	
	

	/* (non-Javadoc)
	 * @see de.thorstenberger.taskmodel.ReportBuilder#createExcelAnalysisForBlock(de.thorstenberger.taskmodel.complex.complextaskdef.Block, java.io.OutputStream)
	 */
	public void createExcelAnalysisForBlock(long taskId, String categoryId, int blockIndex, OutputStream out) throws TaskApiException, MethodNotSupportedException {
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		TaskDef_Complex taskDef;
		try {
			taskDef = (TaskDef_Complex)taskManager.getTaskDef( taskId );
		} catch (ClassCastException e) {
			throw new MethodNotSupportedException( "Only analysis of complexTasks supported (for now)!" );
		}
		Category category = taskDef.getComplexTaskDefRoot().getCategories().get( categoryId );
		Block block = category.getBlock( blockIndex );
		
		short r = 0;
		short c = 0;
		
		HSSFRow row = sheet.createRow( r++ );
		row.createCell( c++ ).setCellValue( "Login" );
		row.createCell( c++ ).setCellValue( "Vorname" );
		row.createCell( c++ ).setCellValue( "Name" );

		if( !block.getType().equals( "mc" ) )
			throw new MethodNotSupportedException( "Only MC analysis supported (for now)!" );
		
		List<SubTaskDefOrChoice> stdocs = block.getSubTaskDefOrChoiceList();
		List<SubTaskDef> stds = new ArrayList<SubTaskDef>();
		// put all SubTaskDefs into stds
		for( SubTaskDefOrChoice stdoc : stdocs ){
			if( stdoc instanceof SubTaskDef ){
				stds.add( (SubTaskDef)stdoc );
				row.createCell( c++ ).setCellValue( ((SubTaskDef)stdoc).getId() );				
			}else{
				Choice choice = (Choice)stdoc;
				for( SubTaskDef std : choice.getSubTaskDefs() ){
					stds.add( std );
					row.createCell( c++ ).setCellValue( std.getId() );
				}
			}
		}
		
		
		List<Tasklet> tasklets = taskManager.getTaskletContainer().getTasklets( taskId );
		
		for( Tasklet tasklet : tasklets ){
			
			if( tasklet.getStatus() == Tasklet.Status.INITIALIZED )
				continue;
			
			if( !(tasklet instanceof ComplexTasklet ) )
				throw new MethodNotSupportedException( "Only analysis of complexTasklets supported (for now)!" );
				
			row = sheet.createRow( r++ );
			c = 0;

			ComplexTasklet ct = (ComplexTasklet)tasklet;
			Try actualTry = ct.getSolutionOfLatestTry();

			c = createUserInfoColumns( tasklet, c, wb, row );
			
			for( SubTaskDef std : stds ){
				
				SubTasklet_MC mcst = (SubTasklet_MC)actualTry.lookupSubTasklet( std );
				if( mcst == null ){
					row.createCell( c++ ).setCellValue( "[n/a]" );
					continue;
				}
				
				StringBuilder sb = new StringBuilder();
				List<SubTasklet_MC.Answer> answers = mcst.getAnswers();
				boolean first = true;
				for( SubTasklet_MC.Answer answer : answers ){
					if( answer.isSelected() ){
						if( !first )
							sb.append( ";" );
						sb.append( answer.getId() );
						first = false;
					}
				}	
				row.createCell( c++ ).setCellValue( sb.toString() );
				
			}
			
		}
		
		try {
			wb.write( out );
			out.flush();
		} catch (IOException e) {
			log.error( "Error writing excel stream!", e );
			throw new TaskApiException( e );
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				throw new TaskApiException( e );
			}			
		}
		
	}

	private static String getStringFromMillis( long timestamp ){
		return df.format( new Date( timestamp ) );
	}
		
}

