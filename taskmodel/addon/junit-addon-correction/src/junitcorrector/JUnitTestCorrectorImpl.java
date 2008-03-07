/*

Copyright (C) 2007 Steffen Dienst

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
package junitcorrector;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.codehaus.groovy.control.CompilationFailedException;

import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;
import correction.junit.*;

public class JUnitTestCorrectorImpl implements JUnitTestCorrector {
	final ExecutorService exs;

	public JUnitTestCorrectorImpl() {
		this.exs=Executors.newFixedThreadPool( 4 );
	}
	public JUnitTestResult runUnitTest( String testInterfaceDef,
			String classUnderTest, String junitTestClass, long timeOut) {

		try {
			SandboxClassLoader sbcl=new SandboxClassLoader(getClass().getClassLoader());
			sbcl.setRestricted( false );//FIXME testinterface gets package java.io, not permitted...
			GroovyClassLoader gcl=new GroovyClassLoader(sbcl);
			Class toTestInterface=gcl.parseClass(testInterfaceDef);
			Class toTest=gcl.parseClass(classUnderTest);//new GroovyCodeSource(classUnderTest,"studentClass","/restrictedGroovy"));//see catalina.policy at {tomcat}/conf
			sbcl.setRestricted(false);
			Class testClass=gcl.parseClass(junitTestClass);
			final TestSuite suite=new TestSuite(testClass);
			for(Enumeration e=suite.tests();e.hasMoreElements();)
				((JavaTaskTester)e.nextElement()).setTestObject(toTest.newInstance());

			final StringBuilder sb=new StringBuilder();
			final ResultPrinter rp=new ResultPrinter(new PrintStream(
					new OutputStream() {
						public void write(int b) throws IOException {
							sb.append((char)b);
						}})) {
				protected void printDefectTrace(TestFailure booBoo) {
					getWriter().println(booBoo.exceptionMessage());
				};//skip stacktraces
			};
			Callable call=new Callable<JUnitTestResult>() {
				public JUnitTestResult call() throws Exception {
					TestRunner tr=new TestRunner(rp);
					TestResult result=tr.doRun(suite);
					return new JUnitTestResultImpl(result.wasSuccessful(),sb.toString());
				}
			};
			Future<JUnitTestResult> future=exs.submit( call );
			try {
				return future.get( timeOut, TimeUnit.MILLISECONDS );
			}catch (ExecutionException e) {
				return new JUnitTestResultImpl(false, "There was an error in this code: "+e.getMessage());
			}catch(TimeoutException e) {
				return new JUnitTestResultImpl(false, "Time limit exceeded!");
			}catch(InterruptedException e) {
				return new JUnitTestResultImpl(false, "Test was interupted, please try again.");
			}
		}catch (CompilationFailedException e) {
			return new JUnitTestResultImpl(false,"compilation failed!: "+e.getMessage());
		} catch (InstantiationException e) {
			return new JUnitTestResultImpl(false,"Could not create an instance of the class: "+e.getMessage());
		} catch (IllegalAccessException e) {
			return new JUnitTestResultImpl(false,"You are not allowed to use this class: "+e.getMessage());
		}

	}


}
