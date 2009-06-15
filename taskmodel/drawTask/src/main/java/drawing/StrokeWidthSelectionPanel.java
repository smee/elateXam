package drawing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class StrokeWidthSelectionPanel extends JPanel {
	private Map map;
	private JPanel drawStrokesPanel;
	private JPanel eraseStrokesPanel;
	final private int minDrawSize, minEraseSize;
	
	public StrokeWidthSelectionPanel(int[] drawStrokeWidths, int[] eraseStrokeWidths, DrawingPanel p){
		if(drawStrokeWidths==null || drawStrokeWidths.length==0 || eraseStrokeWidths==null || eraseStrokeWidths.length==0)
			throw new IllegalArgumentException("needs at least one strokewidth");
		minDrawSize=drawStrokeWidths[0];
		minEraseSize=eraseStrokeWidths[0];
		initialize(drawStrokeWidths,eraseStrokeWidths,p);
	}
	private void initialize(int[] drawStrokeWidths, int[] eraseStrokeWidths, final DrawingPanel p) {
		setLayout(new CardLayout());
		map=new HashMap();
		ButtonGroup bgdraw=new ButtonGroup();
		drawStrokesPanel=new JPanel();
		drawStrokesPanel.setLayout(new BoxLayout(drawStrokesPanel,BoxLayout.X_AXIS));
		for(int i=0;i<drawStrokeWidths.length;i++){
			final int w=drawStrokeWidths[i];
			JToggleButton btn=new JToggleButton(new ImageIcon(Util.createDrawingCursorImage(Color.BLACK,Color.BLACK,w,false))){
				public Dimension getPreferredSize(){
					return new Dimension(32,32);
				}
			};
			btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					p.setDrawingStrokeWidth(w);
				}
			});
			btn.setBorderPainted(false);
			bgdraw.add(btn);
			drawStrokesPanel.add(btn);
			map.put("draw"+w,btn);
		}
		((JToggleButton)map.get("draw"+drawStrokeWidths[0])).doClick();
		p.addPropertyChangeListener("drawingStrokeWidth",new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				JToggleButton btn=(JToggleButton) map.get("draw"+evt.getNewValue());
				if(btn==null)
					btn=(JToggleButton) map.get("draw"+minDrawSize);
				btn.setSelected(true);
			}
		});
		
		ButtonGroup bgerase=new ButtonGroup();
		eraseStrokesPanel=new JPanel();
		eraseStrokesPanel.setLayout(new BoxLayout(eraseStrokesPanel,BoxLayout.X_AXIS));
		for(int i=0;i<eraseStrokeWidths.length;i++){
			final int w=eraseStrokeWidths[i];
			JToggleButton btn=new JToggleButton(new ImageIcon(Util.createDrawingCursorImage(Color.WHITE,Color.BLACK,w,false))){
				public Dimension getPreferredSize(){
					return new Dimension(32,32);
				}
			};
			btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					p.setEraseStrokeWidth(w);
				}
			});
			btn.setBorderPainted(false);
			bgerase.add(btn);
			eraseStrokesPanel.add(btn);
			map.put("erase"+w,btn);
		}
		((JToggleButton)map.get("erase"+eraseStrokeWidths[0])).doClick();
		p.addPropertyChangeListener("eraseStrokeWidth",new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				JToggleButton btn=(JToggleButton) map.get("erase"+evt.getNewValue());
				if(btn==null)
					btn=(JToggleButton) map.get("erase"+minEraseSize);
				btn.setSelected(true);					
			}
		});
		p.addPropertyChangeListener("mode",new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				boolean freehandFlag=((Integer)evt.getNewValue()).intValue()==DrawAction.freehand;
				CardLayout layout = (CardLayout)getLayout();
				if(freehandFlag) 
					layout.show(StrokeWidthSelectionPanel.this,"draw");
				else
					layout.show(StrokeWidthSelectionPanel.this,"erase");
			}
		});
		add(drawStrokesPanel,"draw");
		add(eraseStrokesPanel,"erase");
	}
}
