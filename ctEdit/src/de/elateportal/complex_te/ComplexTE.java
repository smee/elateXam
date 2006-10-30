package de.elateportal.complex_te;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import com.jaxfront.core.dom.DOMBuilder;
import com.jaxfront.core.dom.Document;
import com.jaxfront.core.images.SystemImages;
import com.jaxfront.core.schema.SchemaCreationException;
import com.jaxfront.core.schema.ValidationException;
import com.jaxfront.core.type.Type;
import com.jaxfront.core.ui.TypeVisualizerFactory;
import com.jaxfront.core.util.JAXFrontProperties;
import com.jaxfront.core.util.LicenseErrorException;
import com.jaxfront.core.util.URLHelper;
import com.jaxfront.core.util.io.BrowserControl;
import com.jaxfront.pdf.PDFGenerator;
import com.jaxfront.swing.ui.editor.EditorPanel;
import com.jaxfront.swing.ui.editor.ShowXMLDialog;
//import com.jaxfront.swing.ui.beans.TypeTable;
//import com.jaxfront.xuieditor.XUIEditor;
//import com.jaxfront.xuieditor.visualizers.XUIDebugView;

public class ComplexTE extends JFrame
{
	private static ComplexTE instance;
	
	// the title
	private static String applicationTitle = null;
	
	// window preferences
	private final static int width = 1024;
	private final static int height = 768;
//	private final static int divider_location = 550;
	
	// static pathes for the xsd- and the xui-files
	private final static URL xsd_location = Thread.currentThread().getContextClassLoader().getResource("complexTaskDef.xsd");
	private final static URL xui_location = Thread.currentThread().getContextClassLoader().getResource("complexTaskDef.xui");
	private String xml_location = null; //URLHelper.getUserURL("/test.xml"); // needed to create or to open an existing xml-file
	
	// other files
	private final static URL language_location = Thread.currentThread().getContextClassLoader().getResource("language.properties");
	
	// Components
	private JSplitPane splitPane;
	private JTabbedPane tabbedPane;	
	private JPanel mainPanel;
	private JPanel helpPanel;
    private JPanel errorPanel;
    
    private Document currentDom;    
    
    private EditorPanel editorPanel;
    
//    private int _exampleIndex;
//    private String _exampleURL;
	
	// Language
	private String currentLanguage = "de";
	
	// Actions (taken from JAXFront Example "TestFrame.java")
	private AbstractAction _germanLanguageAction;
    private AbstractAction _englishLanguageAction;
    private AbstractAction _frenchLanguageAction;
    private AbstractAction _serializeAction;
    private AbstractAction _printAction;
    private AbstractAction _validateAction;    
    private AbstractAction _exitAction;
    private AbstractAction _myExampleAction;
    
    private AbstractAction mySaveAsAction;
    private AbstractAction myOpenAction;
    private AbstractAction mySaveAction;
	
    // Misc.
    private JFileChooser chooser = null;	//the file chooser (used for open/close-Dialogs)
    private Properties languages;			//the language-specific words (open, save, ...) are stored here
    private int langNo = 0;
    JMenuBar menu;
    JMenu file;
    JMenu edit;
    JMenu language;
    JMenu help;
    
	// Constructor
	public ComplexTE()
	{
		super();
		this.instance = this;
		init();
	}
	
	// Main Method - starts the application (of course, what else should it do?) 
	public static void main( String[] args ) {
//        try {
//            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
//        } catch (Throwable t) {}
        new ComplexTE();
    }
	
	// main initialisation method
	public void init()
	{
		try
		{	
			this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					attemptToExitApp();
				}
			});
			this.setIconImage( JAXFrontProperties.getImage( SystemImages.ICON_XCENTRIC ) );
			
			chooser = new JFileChooser();
			
			languages = new Properties();
			
			loadLanguages( language_location , false );
			
			for ( int i=1; i<=Integer.parseInt( languages.getProperty("language_counter") ); i++)
				if ( languages.getProperty("language_"+i).equals(currentLanguage) ) langNo = i;
			
			applicationTitle = languages.getProperty("language_"+langNo+"_applicationTitle");
			setTitle(applicationTitle);
			
			initActions();
			initMenuBar();
			initToolBar();
		
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.setBorder(null);
        
			tabbedPane = new JTabbedPane();
			helpPanel = new JPanel();
			errorPanel = new JPanel(new BorderLayout());
        
			tabbedPane.addTab( languages.getProperty("language_"+langNo+"_hintsErrors"), errorPanel);
			tabbedPane.addTab( languages.getProperty("language_"+langNo+"_help"), helpPanel);
			
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerSize(4);
			splitPane.setDividerLocation(500);
			splitPane.setBorder(null);
			splitPane.setTopComponent(mainPanel);
			splitPane.setBottomComponent(tabbedPane);
			getContentPane().add(splitPane, BorderLayout.CENTER);
        
			setSize(width, height);
			setVisible(true);
		}
		catch (LicenseErrorException licEx)
		{
			licEx.showLicenseDialog(this);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	protected void attemptToExitApp() {
    	int result = 2;
    	JOptionPane jop = new JOptionPane();
    	
    	if ( currentDom != null && currentDom.hasChanged() )
    	{
    		result = jop.showConfirmDialog( null , "Exit without saving?", "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
    		//System.out.println(result);
    	}
    	
    	if ( result == JOptionPane.NO_OPTION ) return;
    	else  exit();		
	}

	public void loadLanguages( URL fileName, boolean showProperties )
	{
		try
		{
			InputStream fis = fileName.openStream();
			languages.load( fis );
			if (showProperties)
				languages.list( System.out );
		}
		catch ( IOException  e )
		{
			e.printStackTrace();
		} 
	}
	
	public void initMenuBar()
	{
		menu = new JMenuBar();
		
		file = new JMenu( languages.getProperty("language_"+langNo+"_file") );
		addFileMenu();
		
		edit = new JMenu( languages.getProperty("language_"+langNo+"_edit") );
		addEditMenu();		
		
		language = new JMenu( languages.getProperty("language_"+langNo+"_language") );
		addLanguageMenu();
		
		help = new JMenu( languages.getProperty("language_"+langNo+"_help") );
		addHelpMenu();
		
		menu.add(file);
		menu.add(edit);
		menu.add(language);
		menu.add(help);
		
		setJMenuBar(menu);
	}
	
	private void addFileMenu()
	{
		file.add(_myExampleAction);
		file.addSeparator();
		file.add( myOpenAction );
		file.addSeparator();
		file.add( mySaveAction );
		file.add( mySaveAsAction );
		file.addSeparator();
		file.add(_exitAction);
	}
	
	private void addEditMenu()
	{
		edit.add(_serializeAction);
		edit.add(_validateAction);
		edit.add(_printAction);
	}
	
	private void addLanguageMenu()
	{
		language.add(_englishLanguageAction);
		language.add(_germanLanguageAction);
		language.add(_frenchLanguageAction);
	}
	
	private void addHelpMenu()
	{
//		 TODO Hilfe für den Editor schreiben (oder was ähnliches)
	}
	
	public void initToolBar()
	{
		JToolBar toolBar = new JToolBar();
        
		toolBar.add(_myExampleAction);
		toolBar.addSeparator();
        toolBar.add(_serializeAction);
        toolBar.add(_validateAction);
        toolBar.addSeparator();
        toolBar.add(_printAction);
        
        getContentPane().add(toolBar, BorderLayout.NORTH);
	}
	
	/*
	 * Parts are taken from the JAXFront Example "TestFrame.java"
	 */
	public void initActions()
	{
		_germanLanguageAction = new AbstractAction("German", JAXFrontProperties.getImageIcon(SystemImages.ICON_XUI_DEFINITION_GLOBAL)) {
            public void actionPerformed( ActionEvent e ) {
                setLanguage("de");                
            }
        };
        _englishLanguageAction = new AbstractAction("English", JAXFrontProperties.getImageIcon(SystemImages.ICON_XUI_DEFINITION_GLOBAL)) {
            public void actionPerformed( ActionEvent e ) {
                setLanguage("en");
            }
        };
        _frenchLanguageAction = new AbstractAction("French", JAXFrontProperties.getImageIcon(SystemImages.ICON_XUI_DEFINITION_GLOBAL)) {
            public void actionPerformed( ActionEvent e ) {
                setLanguage("fr");
            }
        };
        _serializeAction = new AbstractAction( languages.getProperty("language_"+langNo+"_serialize"), JAXFrontProperties.getImageIcon(SystemImages.ICON_EDIT_XML)) {
            public void actionPerformed( ActionEvent e ) {
                serialize();
            }
        };
        _printAction = new AbstractAction( languages.getProperty("language_"+langNo+"_printPDF"), JAXFrontProperties.getImageIcon(SystemImages.ICON_PDF)) {
            public void actionPerformed( ActionEvent e ) {
                print();
            }
        };
        _myExampleAction = new AbstractAction( languages.getProperty("language_"+langNo+"_new"), JAXFrontProperties.getImageIcon(SystemImages.ICON_NEW_FILE)) {
            public void actionPerformed( ActionEvent e ) {
                xml_location = null; //reset the xml_location because this Action is now used to create a new DOM
            	myExample();
            }
        };
        _validateAction = new AbstractAction( languages.getProperty("language_"+langNo+"_validate"), JAXFrontProperties.getImageIcon(SystemImages.ICON_VALIDATE)) {
            public void actionPerformed( ActionEvent e ) {
                validateXML();
            }
        };
        _exitAction = new AbstractAction( languages.getProperty("language_"+langNo+"_exit"), JAXFrontProperties.getImageIcon(SystemImages.ICON_CLOSE)) {
            public void actionPerformed( ActionEvent e ) {
            	attemptToExitApp();
            }
        };
        
        mySaveAsAction = new AbstractAction( languages.getProperty("language_"+langNo+"_saveAs"), JAXFrontProperties.getImageIcon(SystemImages.ICON_SAVEAS_FILE) ) {        	
    		public void actionPerformed(ActionEvent ae)
    		{
    			chooser.setCurrentDirectory(new File("."));
    			ExtensionFileFilter eff = new ExtensionFileFilter();
    			eff.addExtension( "xml" );
    			eff.setDescription( ".xml files" );
    			chooser.setFileFilter( eff );
    			
    			int result = chooser.showSaveDialog( ComplexTE.this );    			
    			if (result == JFileChooser.APPROVE_OPTION)
    			{
    				xml_location = chooser.getSelectedFile().getPath();
    				if ( !xml_location.endsWith( ".xml" ) ) xml_location = xml_location + ".xml";
    			}    				
    			else
    				return;    			
    			
    			
    			// Save the XML-DOM to the specified file
    			//System.out.println(xml_location.getFile()); //For test purposes only
    			try {
    				
    				//System.out.println(xml_location);
					currentDom.saveAs(new File( xml_location ));
    				
				} catch (ValidationException e) {
					// Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
    		}			
    	};    	
    	myOpenAction = new AbstractAction( languages.getProperty("language_"+langNo+"_open"), JAXFrontProperties.getImageIcon(SystemImages.ICON_OPEN_FILE) ) {        	
    		public void actionPerformed(ActionEvent ae)
    		{
    			chooser.setCurrentDirectory(new File("."));
    			ExtensionFileFilter eff = new ExtensionFileFilter();
    			eff.addExtension( "xml" );
    			eff.setDescription( ".xml files" );
    			chooser.setFileFilter( eff );
    			
    			int result = chooser.showOpenDialog( ComplexTE.this );    			
    			if (result == JFileChooser.APPROVE_OPTION)				
    			{
					xml_location = chooser.getSelectedFile().getPath();
					if ( !xml_location.endsWith( ".xml" ) ) xml_location = xml_location + ".xml";
				}				
    			else
    				return;
    			
    			// Opens a file from a specified location
    			myExample(); // uses the same method as to create a new DOM, but this time a xml_location is specified
    		}			
    	};
    	mySaveAction = new AbstractAction( languages.getProperty("language_"+langNo+"_save"), JAXFrontProperties.getImageIcon(SystemImages.ICON_SAVE_FILE) ) {        	
    		public void actionPerformed(ActionEvent ae)
    		{
    			//if ( xml_location != null ) System.out.println(xml_location);
    			
    			// if no xml file was specified yet, select a path and create one
    			if ( xml_location == null )
    			{
    				chooser.setCurrentDirectory(new File("."));
    				ExtensionFileFilter eff = new ExtensionFileFilter();
    				eff.addExtension( "xml" );
    				eff.setDescription( ".xml files" );
    				chooser.setFileFilter( eff );
    				
    				int result = chooser.showSaveDialog( ComplexTE.this );    			
    				if (result == JFileChooser.APPROVE_OPTION)				
    				{
    					xml_location = chooser.getSelectedFile().getPath();
    					if ( !xml_location.endsWith( ".xml" ) ) xml_location = xml_location + ".xml";
    				}
    				else
    					return;
    			}
    			
    			try {
					currentDom.saveAs(new File( xml_location ));
    				
				} catch (ValidationException e) {
					// Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
    		}			
    	};
        
//        _englishLanguageAction.setEnabled(false);
//        _germanLanguageAction.setEnabled(false);
//        _frenchLanguageAction.setEnabled(false);
        _serializeAction.setEnabled(false);
        _printAction.setEnabled(false);
        _validateAction.setEnabled(false);
        
        mySaveAction.setEnabled(false);
        mySaveAsAction.setEnabled(false);
	}
	
	private void setLanguage( String language ) {
        currentLanguage = language;
        
        for ( int i=1; i<=Integer.parseInt( languages.getProperty("language_counter") ); i++)
			if ( languages.getProperty("language_"+i).equals(currentLanguage) ) langNo = i;
        
        initActions();
        
        this.file.setText( languages.getProperty("language_"+langNo+"_file") );
        this.edit.setText( languages.getProperty("language_"+langNo+"_edit") );
        this.language.setText( languages.getProperty("language_"+langNo+"_language") );
        this.help.setText( languages.getProperty("language_"+langNo+"_help") );
        
        this.file.removeAll();
        this.edit.removeAll();
        this.language.removeAll();
        this.help.removeAll();
        
        addFileMenu();
        addEditMenu();
        addLanguageMenu();
        addHelpMenu();
        
        tabbedPane.removeAll();
        
        tabbedPane.addTab( languages.getProperty("language_"+langNo+"_hintsErrors"), errorPanel);
		tabbedPane.addTab( languages.getProperty("language_"+langNo+"_help"), helpPanel);
        
		applicationTitle = languages.getProperty("language_"+langNo+"_applicationTitle");
		setTitle(applicationTitle);
		
        visualizeDOM();
    }
	
	private String getCurrentLanguage() {
        return currentLanguage;
    }
	
	private void visualizeDOM() {
        // set language
        currentDom.getGlobalDefinition().setLanguage(getCurrentLanguage());
        Type lastSelectedType = null;
        if (editorPanel != null && editorPanel.getSelectedTreeNode() != null) {
            lastSelectedType = editorPanel.getSelectedTreeNode().getType();
        }
        TypeVisualizerFactory.getInstance().releaseCache(currentDom);
        EditorPanel tempEditorPanel = new EditorPanel(currentDom.getRootType(), this);
        if (lastSelectedType != null) {
        	tempEditorPanel.selectNode(lastSelectedType);
        }
        tempEditorPanel.setBorder(null);
        JPanel validationErrorPanel = new JPanel(new BorderLayout());
        validationErrorPanel.setBorder(null);
        tempEditorPanel.setTargetMessageTable(validationErrorPanel);
        if (editorPanel != null)
            mainPanel.remove(editorPanel);
        editorPanel = tempEditorPanel;
        mainPanel.add(editorPanel, BorderLayout.CENTER);
//        int lastDividerLocation = divider_location;
        if (splitPane.getBottomComponent() != null) {
            //lastDividerLocation = splitPane.getDividerLocation();
            errorPanel.removeAll();
            errorPanel.add(validationErrorPanel, BorderLayout.CENTER);
	        splitPane.validate();
        }
        // activate actions
//        _englishLanguageAction.setEnabled(true);
//        _germanLanguageAction.setEnabled(true);
//        _frenchLanguageAction.setEnabled(true);
        _serializeAction.setEnabled(true);
        _printAction.setEnabled(true);
        _validateAction.setEnabled(true);
        
        mySaveAction.setEnabled(true);
        mySaveAsAction.setEnabled(true);
    }
	
	private void serialize() {
        ShowXMLDialog dialog = new ShowXMLDialog(currentDom);
        dialog.prettyPrint();
        Dimension dialogDim = dialog.getSize();
        Dimension thisDim = getSize();
        int x = (thisDim.width - dialogDim.width) / 2;
        int y = (thisDim.height - dialogDim.height) / 2;
        if (getLocation().x > 0 || getLocation().y > 0) {
            x = x + getLocation().x;
            y = y + getLocation().y;
        }
        dialog.setLocation(((x > 0) ? x : 0), ((y > 0) ? y : 0));
        dialog.setVisible(true);
    }    
    
    private void print() {
        if (currentDom != null) {
            ByteArrayOutputStream bos = PDFGenerator.getInstance().print(currentDom);
            if (bos != null) {
                try {
                    String tempPDFName; //= ".\\ComplexTE_printed.pdf";
                	
                    chooser.setCurrentDirectory(new File("."));
                    ExtensionFileFilter eff = new ExtensionFileFilter();
        			eff.addExtension( "pdf" );
        			eff.setDescription( ".pdf files" );
        			chooser.setFileFilter( eff );
                    
    				//int result = chooser.showSaveDialog( ComplexTE.this );
    				int result = chooser.showDialog( ComplexTE.this, languages.getProperty("language_"+langNo+"_printPDF") );
    				if (result == JFileChooser.APPROVE_OPTION)				
    				{
    					tempPDFName = chooser.getSelectedFile().getPath();
    					if ( !tempPDFName.endsWith( ".pdf" ) ) tempPDFName = tempPDFName + ".pdf";
    				}
    				else
    					return;
    				
                    FileOutputStream fos = new FileOutputStream(tempPDFName);
                    bos.writeTo(fos);
                    fos.close();
                    BrowserControl.displayURL(tempPDFName);
                } catch (Exception t) {
                    t.printStackTrace();
                }
            }
        }
    }
    
    private void validateXML() {
        if (currentDom != null) {
            currentDom.validate();
        }
    }
    
    private void exit() {
        System.exit(0);
    }
    
    public void myExample() {
    	
    	URL xml_loc = URLHelper.getUserURL(xml_location);
    	
        if (xsd_location == null)
           return;					// little change here
        else {
            try {        
                currentDom = DOMBuilder.getInstance().build(xsd_location, xml_loc, xui_location, null);
            	//currentDom = DOMBuilder.getInstance().build(xsd_location);	// this was only a test
            	currentDom.getGlobalDefinition().setIsUsingButtonBar(false);
                currentDom.getGlobalDefinition().setIsUsingStatusBar(false);
                if (editorPanel != null)
                    editorPanel.selectNode((Type) null);
                visualizeDOM();
            } catch (Exception ex) {
                ex.printStackTrace();
                String xsd = null;
                String xml = null;
                String xui = null;
                if (xsd_location != null)
                    xsd = xsd_location.toExternalForm();
                if (xml_location != null)
                    xml = xml_loc.toExternalForm();
                if (xui_location != null)
                    xui = xui_location.toExternalForm();
                //JOptionPane.showConfirmDialog(this, "Unable to visualize: \n " + "xsd - " + xsd + "\n" + "xml - " + xml + "\n" + "xui - " + xui + "\n");
                JOptionPane.showMessageDialog( this, languages.getProperty("language_"+langNo+"_visualisationError")+": \n " + "xsd - " + xsd + "\n" + "xml - " + xml + "\n" + "xui - " + xui + "\n" +stack2string(ex));
            }
        }
    }



    static public String stack2string(Exception e) {
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
     try {
       e.printStackTrace(pw);
       if(e instanceof SchemaCreationException) {
    	   SchemaCreationException sce=(SchemaCreationException)e;
    	   Class c=sce.getClass();
    	   Field nested=c.getField("_occuredException");
    	   Throwable t=(Throwable) nested.get(sce);
    	   pw.write("\ncaused by:\n");
    	   t.printStackTrace(pw);
       }
       return "------\r\n" + sw.toString() + "------\r\n";
       }
     catch(Exception e2) {
       e2.printStackTrace(pw);
       return pw.toString();
       }
     }
    
	/**
	 * @return Returns the instance.
	 */
	public static ComplexTE getInstance() {
		return instance;
	}

	/**
	 * @return Returns the editorPanel.
	 */
	public EditorPanel getEditorPanel() {
		return editorPanel;
	}
    
    
}
