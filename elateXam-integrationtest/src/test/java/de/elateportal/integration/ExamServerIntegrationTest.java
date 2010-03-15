package de.elateportal.integration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestNgHelper;
import com.thoughtworks.selenium.Selenium;

public class ExamServerIntegrationTest extends SeleneseTestNgHelper {

  @BeforeSuite
  @Parameters( { "selenium.host", "selenium.port" })
  public void attachCompleteScreenshotListener(@Optional("localhost") final String host, @Optional("4444") final String port, final ITestContext context) {
    if (!("localhost".equals(host))) {
      return;
    }
    final Selenium screenshotTaker = new DefaultSelenium(host, Integer.parseInt(port), "", "");
    final TestRunner tr = (TestRunner) context;
    final File outputDirectory = new File(context.getOutputDirectory());
    tr.addListener(new CompleteScreenshotListener(outputDirectory, screenshotTaker));
  }

  @Test(dependsOnMethods = { "testAddStudent", "testEnableLogins", "testAddComplextaskDefinition" })
  public void testCoreviewShowsErrorTexts() throws Exception {
    // log in
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_password", "admin");
    selenium.type("j_username", "admin");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    // open invalid taskmodel-core-view page
    selenium.open("/taskmodel-core-view/execute.do?id=5&todo=new&try=1");
    selenium.waitForPageToLoad("30000");
    verifyTrue(selenium.isTextPresent("Session-Fehler! Der Server kann Ihre Sitzung nicht weiterverfolgen. Bitte überprüfen Sie, ob Cookies aktiviert sind."));

  }
  @Test
  public void testAddComplextaskDefinition() throws Exception {
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_password", "admin");
    selenium.type("j_username", "admin");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    verifyTrue(selenium.isTextPresent("Gratulation, Sie haben sich erfolgreich angemeldet! Als Administrator haben Sie folgende Möglichkeiten:"));
    selenium.click("link=Aufgaben-Verwaltung");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    selenium.click("taskDefFile");
    selenium.type("taskDefFile", getAbsolutePath("testklausur.xml"));
    selenium.click("//input[@value='Hochladen']");
    selenium.waitForPageToLoad("30000");
    selenium.type("title", "Test");
    assertEquals(selenium.getTitle(), "Aufgaben-Konfiguration");
    selenium.click("stopped");
    selenium.click("//input[@value='Speichern']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    assertEquals(selenium.getTable("row.1.0"), "Test");
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login");
  }

  private String getAbsolutePath(final String fileOnClasspath) {
    final URL resourceUrl = this.getClass().getClassLoader().getResource(fileOnClasspath);
    try {
      return new File(resourceUrl.toURI()).getAbsolutePath();
    } catch (final URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testAddStudent() throws Exception {
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_password", "admin");
    selenium.type("j_username", "admin");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    selenium.click("link=Benutzer-Verwaltung");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Benutzerliste");
    selenium.click("//input[@value='Hinzufügen']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Benutzereinstellungen");
    selenium.type("username", "studi");
    selenium.type("password", "test");
    selenium.type("confirmPassword", "test");
    selenium.type("firstName", "Max");
    selenium.type("lastName", "Mustermann");
    selenium.click("method.save");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Benutzereinstellungen");
    verifyTrue(selenium.isElementPresent("//div[@id='successMessages']"));
    // verifyTrue(selenium.isTextPresent("User information for Max Mustermann has been added successfully"));
    verifyTrue(selenium.isElementPresent("//img[@alt='Information' and @class='icon']"));
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login");
  }

  @Test
  public void testEnableLogins() throws Exception {
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_username", "admin");
    selenium.type("j_password", "admin");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    selenium.click("link=Login-Konfiguration");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login-Konfiguration");
    selenium.click("studentsLoginEnabled");
    selenium.click("//input[@value='Speichern']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login-Konfiguration");
    verifyTrue(selenium.isElementPresent("//div[@id='successMessages']"));
    verifyTrue(selenium.isTextPresent("Ok, Studenten haben jetzt Zugriff auf den Prüfungsserver."));
    verifyTrue(selenium.isElementPresent("//img[@alt='Information' and @class='icon']"));
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login");
  }

  @Test
  public void testCompletePersonalDetails() throws Exception {
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_username", "studi");
    selenium.type("j_password", "test");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    verifyTrue(selenium.isTextPresent("Bitte vervollständigen Sie Ihre Daten und klicken Sie auf OK!"));
    selenium.type("matrikel", "111000");
    selenium.type("semester", "15");
    selenium.click("OK");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    verifyTrue(selenium.isTextPresent("In der folgenden Liste finden Sie alle verfügbaren Aufgaben."));
  }

  @Test(dependsOnMethods = { "testEnableLogins", "testAddStudent", "testAddComplextaskDefinition", "testCompletePersonalDetails" })
  public void testStudentRunsExam() throws Exception {
    selenium.open("/examServer/login.jsp");
    assertEquals(selenium.getTitle(), "Login");
    selenium.type("j_username", "studi");
    selenium.type("j_password", "test");
    selenium.click("//input[@name='login']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Hauptmenü");
    // should greet the student with his name
    verifyTrue(selenium.isElementPresent("//div[@id='main']/h1[text()='Hallo Max Mustermann!']"));
    selenium.click("link=Test");
    selenium.waitForPageToLoad("30000");
    // Start should be available, continue not
    verifyTrue(selenium.isEditable("//input[@value='Starten']"));
    verifyFalse(selenium.isEditable("//input[@value='Fortsetzen']"));
    selenium.click("//input[@value='Starten']");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    verifyTrue(selenium.isElementPresent("//fieldset/legend[text()='Aufgabe 1']"));
    verifyTrue(selenium.isElementPresent("//fieldset/legend[text()='Aufgabe 2']"));
    // navigation should be visible
    verifyTrue(selenium.isElementPresent("//div[@class='dtree']"));
    // current page marker should be on page 1
    verifyTrue(selenium.isElementPresent("//div[@id='dd1']/div/img[@src='/taskmodel-core-view/pics/sparkle001bu.gif']"));
    selenium.click("task[0].ms_answer_0");
    selenium.chooseCancelOnNextConfirmation();
    selenium.click("link=Seite 2");
    assertEquals(selenium.getConfirmation(), "Sie haben Ihre Änderungen an dieser Seite noch nicht gespeichert.\n\nOK - Änderungen verwerfen und Seite verlassen\nAbbrechen - Seite noch nicht verlassen");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    selenium.click("save");
    selenium.waitForPageToLoad("30000");
    // current page still page 1?
    verifyTrue(selenium.isElementPresent("//div[@id='dd1']/div/img[@src='/taskmodel-core-view/pics/sparkle001bu.gif']"));
    selenium.click("link=Seite 3");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    verifyTrue(selenium.isElementPresent("task[0].concept_0"));
    verifyTrue(selenium.isElementPresent("task[0].concept_1"));
    verifyTrue(selenium.isElementPresent("task[0].concept_2"));
    verifyTrue(selenium.isElementPresent("task[0].concept_3"));
    verifyTrue(selenium.isElementPresent("task[0].concept_4"));
    // page 3
    verifyTrue(selenium.isElementPresent("//div[@id='dd4']/div/img[@src='/taskmodel-core-view/pics/sparkle001bu.gif']"));
    // page 1 is partly processed
    verifyTrue(selenium.isElementPresent("//div[@id='dd1']/div/img[@src='/taskmodel-core-view/icons/partlyProcessed.gif']"));
    selenium.select("task[0].concept_0", "index=0");
    selenium.select("task[0].concept_1", "index=1");
    selenium.select("task[0].concept_2", "index=2");
    selenium.select("task[0].concept_3", "index=0");
    selenium.select("task[1].concept_0", "index=0");
    selenium.select("task[1].concept_1", "index=1");
    selenium.select("task[1].concept_2", "index=2");
    selenium.select("task[1].concept_3", "index=0");
    selenium.click("save");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    // page 3 is completely processed
    verifyTrue(selenium.isElementPresent("//div[@id='dd4']/div/img[@src='/taskmodel-core-view/icons/processed.gif']"));
    // cloze subtask
    selenium.click("link=Seite 5");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    selenium.type("task[0].gap_0", "a");
    selenium.type("task[1].gap_0", "a");
    selenium.click("save");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    verifyEquals(selenium.getValue("task[0].gap_0"), "a");
    verifyEquals(selenium.getValue("task[1].gap_0"), "a");
    // text subtask
    selenium.click("link=Seite 7");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    verifyTrue(selenium.isElementPresent("task[0].text"));
    selenium.type("task[0].text", "foo");
    selenium.click("save");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Bearbeitung: Test");
    verifyEquals(selenium.getValue("task[0].text"), "foo");
    // submit the exam
    verifyTrue(selenium.isTextPresent(""));
    selenium.click("submit");
    selenium.waitForPageToLoad("30000");
    assertTrue(selenium.getConfirmation().matches("^Mit der Abgabe Ihrer Aufgaben beenden Sie die Bearbeitung\\.\n\nSie haben noch nicht alle Aufgaben bearbeitet\\. Wollen Sie wirklich abgeben[\\s\\S]$"));
    assertEquals(selenium.getTitle(), "Aufgaben abgegeben");
    verifyTrue(selenium.isTextPresent("Ihre Lösung wurde erfolgreich abgegeben."));
    verifyTrue(selenium.isElementPresent("//img[@src='/taskmodel-core-view/pics/info.gif']"));
    selenium.click("link=Übersicht");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "");
    // we have processed one try
    verifyEquals(selenium.getTable("//div[@id='main']/table/tbody/tr/td/fieldset[1]/table.1.1"), "1");
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");
    assertEquals(selenium.getTitle(), "Login");
  }
}

