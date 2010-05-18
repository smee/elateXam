package drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

public class CompleteDrawingPanel extends JPanel {
	private class ImageSwitchListener implements ItemListener{
		private final int BACKGROUND=0;
		private final int FOREGROUND=1;
		private final int CORRECTION=2;

			String fg,bg,corr;
			int crntLayer;
			private final int w,h;

			public ImageSwitchListener(int w, int h) {
				this.w=w;
				this.h=h;
				this.crntLayer=FOREGROUND;
			}

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					switch (((JComboBox)e.getSource()).getSelectedIndex()) {
					case BACKGROUND:
						if(crntLayer==FOREGROUND) {
							fg=p.getForegroundImageString();
						}else {//CORRECTION
							corr=p.getForegroundImageString();
						}
						p.reset(bg);
						crntLayer=BACKGROUND;
						break;
					case FOREGROUND://vordergrund
						if(crntLayer==BACKGROUND) {
							bg=p.getCompletePictureBase64();
						}else {//CORRECTION
							corr=p.getForegroundImageString();
						}
						p.reset(fg);
						p.setBackgroundImage(bg);
						crntLayer=FOREGROUND;
						break;
					case CORRECTION://korrekturbild
						if(crntLayer==BACKGROUND) {
							bg=p.getCompletePictureBase64();
						}else {
							fg=p.getForegroundImageString();
						}
						p.reset(corr);
						p.setBackgroundImage(Util.combineImages(Util.generateBlankImage(w,h,Color.WHITE),new String[] {bg,fg}));
						crntLayer=CORRECTION;
						break;
					default:
						break;
					}
					p.repaint();
				}
			}
			public void setBackGroundImageString(String val) {
				this.bg=val;
				switch (crntLayer) {
				case BACKGROUND:
					p.reset(val);
					break;
				case FOREGROUND:
					p.setBackgroundImage(val);
					break;
				case CORRECTION:
					p.setBackgroundImage(Util.combineImages(Util.generateBlankImage(w,h,Color.WHITE),new String[] {val,fg}));
					break;
				default:
					break;
				}
			}
			public void setForeGroundImageString(String val) {
				this.fg=val;
				switch (crntLayer) {
				case BACKGROUND:
					break;
				case CORRECTION:
					p.setBackgroundImage(Util.combineImages(Util.generateBlankImage(w,h,Color.WHITE),new String[] {bg,fg}));
					break;
				case FOREGROUND:
					p.addForegroundImage(val);
					break;
				default:
					break;
				}
			}
			public void setCorrectionImageString(String val) {
				this.corr=val;
				switch (crntLayer) {
				case BACKGROUND:
				case FOREGROUND:
					break;
				case CORRECTION:
					p.addForegroundImage(val);
					break;
				default:
					break;
				}
			}
			public String getBackGroundString() {
				if (crntLayer==BACKGROUND)
					return p.getCompletePictureBase64();
				else
					return bg;
			}
			public String getForeGroundString() {
				if (crntLayer==FOREGROUND)
					return p.getForegroundImageString();
				else
					return fg;
			}
			public String getCorrectionString() {
				if (crntLayer==CORRECTION)
					return p.getForegroundImageString();
				else
					return corr;
			}
	}

	private DrawingPanel p;
	private JButton colorButton;
	private StrokeWidthSelectionPanel swsp;
	private int w;
	private int h;
	private ImageSwitchListener isl;
	private String defaultFg;
	private boolean resetFlag;
	private JToolBar toolbar;

	public CompleteDrawingPanel(){
		this(600,350,true);
	}

	public CompleteDrawingPanel(int w, int h, boolean tutorMode) {
		this.w=w;
		this.h=h;
		this.resetFlag=false;
		initialize(tutorMode);
	}

	private void initialize(boolean tutorMode) {
		setLayout(new BorderLayout());
		p=new DrawingPanel(w,h);
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(p,BorderLayout.WEST);
		add(panel,BorderLayout.CENTER);
		toolbar = createJToolbar(tutorMode);
		add(toolbar,BorderLayout.NORTH);

	}

	private JToolBar createJToolbar(boolean tutorMode) {
		isl = new ImageSwitchListener(w,h);
		JToolBar tb=new JToolBar(JToolBar.HORIZONTAL);
		final JToggleButton freehandButton=new JToggleButton("Freehand",true);
		freehandButton.setAction(new MyAction("Zeichnen","draw freehanded",CompleteDrawingPanel.class.getClassLoader().getResource("img/freehand.jpg"),null){
			public void actionPerformed(ActionEvent e) {
				p.setFreehandMode();
			}
		});
		tb.add(freehandButton);
		final JToggleButton eraseButton=new JToggleButton("Erase",false);
		eraseButton.setAction(new MyAction("Löschen","erase freehanded",CompleteDrawingPanel.class.getClassLoader().getResource("img/erase.jpg"),null){
			public void actionPerformed(ActionEvent e) {
				p.setEraseMode();
			}
		});
		freehandButton.setSelected(true);
		tb.add(eraseButton);
		ButtonGroup bg=new ButtonGroup();
		bg.add(freehandButton);
		bg.add(eraseButton);
		p.addPropertyChangeListener("mode",new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				int mode=((Integer)evt.getNewValue()).intValue();
				switch (mode) {
				case DrawAction.freehand:
					freehandButton.setSelected(true);
					eraseButton.setSelected(false);
					break;
				case DrawAction.erase:
					freehandButton.setSelected(false);
					eraseButton.setSelected(true);
					break;
				default:
					break;
				}
			}
		});

		final MyAction undoAction=new MyAction("Undo","undo last action",CompleteDrawingPanel.class.getClassLoader().getResource("img/undo.jpg"),null){
			public void actionPerformed(ActionEvent e) {
				p.undo();
			}
		};
		final MyAction redoAction=new MyAction("Redo","redo last action",CompleteDrawingPanel.class.getClassLoader().getResource("img/redo.jpg"),null){
			public void actionPerformed(ActionEvent e) {
				p.redo();
			}
		};
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		tb.add(undoAction);
		tb.add(redoAction);
		p.getActionRecorder().addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("undo")) {
          undoAction.setEnabled(((Boolean)evt.getNewValue()).booleanValue());
        } else
					if(evt.getPropertyName().equals("redo")) {
            redoAction.setEnabled(((Boolean)evt.getNewValue()).booleanValue());
          }
			}
		});

		swsp=new StrokeWidthSelectionPanel(new int[]{1,2,4,8,12},new int[] {6,10,20,30},p);
		tb.add(swsp);
		colorButton = new JButton();
		colorButton.setAction(new MyAction("Farbe","Auswahl der Vordergrundfarbe",null,null){
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(CompleteDrawingPanel.this,"Farbe auswählen",p.getColor());
				if(col!=null){
					p.setColor(col);
					setButtonColor(colorButton, col);
				}
			}
		});
		setButtonColor(colorButton,p.getColor());
		tb.add(colorButton);
		p.addPropertyChangeListener("color",new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				setButtonColor(colorButton,(Color) evt.getNewValue());
			}
		});
		tb.addSeparator();
		tb.add(new MyAction("Alles löschen","löscht das Bild",null,null){
			public void actionPerformed(ActionEvent e) {
				int answer=JOptionPane.showConfirmDialog(CompleteDrawingPanel.this,"Soll alles bisher gezeichnete unwiederbringlich gelöscht werden?");
				if(answer==JOptionPane.OK_OPTION) {
					p.clear(defaultFg);
					resetFlag=true;
					p.setHasChanged( true );

				}
			}
		});

		// assure resetting the reset flag on any drawing action
		p.addPropertyChangeListener( "action", new UnresetPropertyChangeListener() );


//		tb.add(new MyAction("debug save","prints imagedata to console",null,null){
//			public void actionPerformed(ActionEvent e) {
//				String data=p.getForegroundWithUndoString(5);
//				String[] stuff=data.split("%%%");
//				System.out.println(stuff[0]+"%%%");
//				StringTokenizer st=new StringTokenizer(stuff[1],"|");
//				while(st.hasMoreTokens()) {
//					System.out.println(st.nextToken());
//				}
//				System.out.println("-----------------------"+data.length()+"bytes");
//				saveToFile(data);
//			}
//
//			private void saveToFile(String data) {
//				JFileChooser jfc=new JFileChooser(".");
//				int res=jfc.showSaveDialog(CompleteDrawingPanel.this);
//				if(res==JFileChooser.APPROVE_OPTION) {
//					BufferedWriter bw;
//					try {
//						bw = new BufferedWriter(new FileWriter(jfc.getSelectedFile()));
//						bw.write(data);
//						bw.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//		});
//		tb.add(new MyAction("debug load","loads undodata",null,null){
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser jfc=new JFileChooser(".");
//				int res=jfc.showOpenDialog(CompleteDrawingPanel.this);
//				if(res==JFileChooser.APPROVE_OPTION) {
//					BufferedReader bw;
//					try {
//						bw = new BufferedReader(new FileReader(jfc.getSelectedFile()));
//						setForegroundWithUndoData(bw.readLine());
//						bw.close();
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}
//				}
//
//			}
//		});

		if(tutorMode) {
			tb.add(new MyAction("Hintergrund laden","Bild aus Datei laden",null,null) {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc=new JFileChooser(".");
					jfc.setFileFilter(new FileFilter() {
						@Override
            public boolean accept(File f) {
							boolean ret=f.isDirectory();
							String name=f.getAbsolutePath().toLowerCase();
							return ret || name.endsWith("jpeg")
							|| name.endsWith("jpg")
							|| name.endsWith("gif")
							|| name.endsWith("png")
							|| name.endsWith("bmp");
						}
						@Override
            public String getDescription() {
							return "Bilder (*.jpeg, *.png, *.gif, *.bmp)";
						}

					});
					int ret=jfc.showOpenDialog(CompleteDrawingPanel.this);
					if(ret==JFileChooser.APPROVE_OPTION) {
						File file=jfc.getSelectedFile();
						Image img;
						try {
							img = ImageUtil.getScaledInstance(ImageIO.read(file),w,h);
							isl.setBackGroundImageString(Util.getBase64StringFromImage(img));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			final JComboBox cb=new JComboBox(new Object[] {"Hintergrundbild","Vordergrundbild","Lösungsschablone"});
			cb.addItemListener(isl);
			cb.setSelectedIndex(1);
			cb.setSelectedIndex(0);
			tb.add(cb);
		}
		tb.add(Box.createGlue());
    tb.add(new MyAction("Copy to clipboard", "prints imagedata to console", null, null) {
      public void actionPerformed(final ActionEvent e) {
        final StringBuilder sb = new StringBuilder("<paintSubTaskDef id=\"paint_").append(new Random().nextInt());
        sb.append("\">\n");
        sb.append("<problem>bla bla</problem>\n");
        sb.append("  <images>\n");
        sb.append("    <mutableTemplateImage>\n");
        sb.append("      ").append(isl.getForeGroundString()).append("\n");
        sb.append("    </mutableTemplateImage>\n");
        sb.append("    <immutableBackgroundImage>\n");
        sb.append("      ").append(isl.getBackGroundString()).append("\n");
        sb.append("    </immutableBackgroundImage>\n");
        sb.append("    <correctionTemplateImage>\n");
        sb.append("      ").append(isl.getCorrectionString()).append("\n");
        sb.append("    </correctionTemplateImage>\n");
        sb.append("  </images>\n");
        sb.append("</paintSubTaskDef>\n");
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(sb.toString()), null);
        JOptionPane.showMessageDialog(null, "Now paste clipboard contents into complex taskdef.");
      }
    });
		return tb;
	}
	private void setButtonColor(final JButton colorButton, Color col) {
		colorButton.setBackground(col);
		colorButton.setForeground(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
	}
	public void setStrokeChangeable(boolean val) {
		swsp.setVisible(val);
	}
	public void setColorChangeable(boolean val) {
		colorButton.setVisible(val);
	}

	public void setBackGroundImage(String value) {
		isl.setBackGroundImageString(value);
	}

	public void setForegroundImage(String value) {
		isl.setForeGroundImageString(value);
	}
	public void setCorrectionImage(String value) {
		isl.setCorrectionImageString(value);
	}
	public String getBackGroundImage() {
		return isl.getBackGroundString();
	}

	public String getForegroundImage() {
		return isl.getForeGroundString();
	}

	public String getCorrectionImage() {
		return isl.getCorrectionString();
	}
	public void setPictureString(String s) {
		p.setForegroundWithUndoData(s);
	}
	public String getForegroundPictureWithUndoData(int numUndoSteps) {
		return p.getForegroundWithUndoString(numUndoSteps);
	}
	@Override
  public synchronized void addFocusListener(FocusListener l) {
		super.addFocusListener(l);
		p.addFocusListener(l);
	}

	public boolean hasChanged() {
		return p.hasChanged();
	}
	public boolean isResetted() {
		return resetFlag;
	}
	public void setForegroundWithUndoData(String undoData) {
		p.setForegroundWithUndoData(undoData);
	}

	public void setDefaultForeGround(String mutableForeground) {
		this.defaultFg=mutableForeground;
	}

	public void setHasChanged(boolean b) {
		p.setHasChanged(b);
	}
	private final class UnresetPropertyChangeListener implements PropertyChangeListener{

		public void propertyChange(PropertyChangeEvent evt) {
			DrawAction action=(DrawAction) evt.getNewValue();
			if( action.getType() == DrawAction.freehand || action.getType() == DrawAction.erase ){
				resetFlag = false;
			}
		}

	}
	public void setResetted(boolean wasResetted) {
		resetFlag=wasResetted;
	}
	public void setEditable(boolean flag) {
		toolbar.setVisible(flag);
		p.setEditable(flag);
		invalidate();
	}
}
