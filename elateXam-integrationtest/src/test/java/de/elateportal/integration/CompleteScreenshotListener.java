package de.elateportal.integration;

import java.io.File;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.IResultListener;

import com.thoughtworks.selenium.Selenium;

/**
 * Capture screenshot of ENTIRE page instead of viewport like com.thoughtworks.selenium.ScreenshotListener does.
 * 
 * @author Steffen Dienst
 * 
 */
public class CompleteScreenshotListener
implements IResultListener {
  File outputDirectory;
  Selenium selenium;

  public CompleteScreenshotListener(final File outputDirectory, final Selenium selenium) {
    this.outputDirectory = outputDirectory;
    this.selenium = selenium;
  }

  public void onTestFailure(final ITestResult result) {
    Reporter.setCurrentTestResult(result);
    try {
      this.outputDirectory.mkdirs();
      final File outFile = File.createTempFile("TEST-" + result.getName(), ".png", this.outputDirectory);
      outFile.delete();
      this.selenium.captureEntirePageScreenshot(outFile.getAbsolutePath(), "background=#FFFFFF");
      Reporter.log("<a href='" + outFile.getName() + "'>screenshot</a>");
    } catch (final Exception e) {
      e.printStackTrace();
      Reporter.log("Couldn't create screenshot");
      Reporter.log(e.getMessage());
    }

    Reporter.setCurrentTestResult(null);
  }

  public void onConfigurationFailure(final ITestResult result) {
    onTestFailure(result);
  }

  public void onFinish(final ITestContext context) {
  }

  public void onStart(final ITestContext context) {
    this.outputDirectory = new File(context.getOutputDirectory());
  }

  public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
  }

  public void onTestSkipped(final ITestResult result) {
  }

  public void onTestStart(final ITestResult result) {
  }

  public void onTestSuccess(final ITestResult result) {
  }

  public void onConfigurationSuccess(final ITestResult itr) {
  }

  public void onConfigurationSkip(final ITestResult itr) {
  }
}

/*
 * Location:
 * D:\maven-repository\org\seleniumhq\selenium\client-drivers\selenium-java-testng-helper\1.0.1\selenium-java-testng
 * -helper-1.0.1.jar Qualified Name: com.thoughtworks.selenium.ScreenshotListener Java Class Version: 5 (49.0) JD-Core
 * Version: 0.5.3
 */