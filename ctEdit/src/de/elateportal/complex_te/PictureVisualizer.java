package de.elateportal.complex_te;

import java.util.Iterator;

import javax.swing.JComponent;

import com.jaxfront.core.dom.DOMChangeEvent;
import com.jaxfront.core.dom.DOMChangeListener;
import com.jaxfront.core.type.SimpleGroup;
import com.jaxfront.core.type.SimpleType;
import com.jaxfront.swing.ui.beans.AbstractSimpleVisualizer;

import drawing.CompleteDrawingPanel;

public class PictureVisualizer extends AbstractSimpleVisualizer {
	private CompleteDrawingPanel cdp;
	private final int w;
	private final int h;

	public PictureVisualizer(){
		this(600,350);
	}
	public PictureVisualizer(int w, int h) {
		this.w=w;
		this.h=h;
	}
	protected JComponent createEditorComponent() {
		if(cdp==null){
			cdp=new CompleteDrawingPanel(w,h,true);
			getDOM().addDOMChangeListener(new DOMChangeListener() {
				public void domChanged(DOMChangeEvent dce) {
					if(dce.getType()==DOMChangeEvent.DOM_IS_ABOUT_TO_SAVE)
						populateModel();
				}
			});
		}
		return cdp;
	}

	public String getLabelText(){
		return "Bilder";
	}
	public void populateView() {
		if(getModel()!=null && cdp!=null){
			SimpleGroup sg=(SimpleGroup) getModel();
			for (Iterator it = sg.getAllSimpleChildren().iterator(); it.hasNext();) {
				SimpleType s = (SimpleType) it.next();
				if(s.getName().equals("immutableBackgroundImage")) {
					cdp.setBackGroundImage(s.getValue());
				}else if(s.getName().equals("mutableTemplateImage")) {
					cdp.setForegroundImage(s.getValue());
				}else if(s.getName().equals("correctionTemplateImage")) {
					cdp.setCorrectionImage(s.getValue());
				}
			}
		}
	}
	public String getText() {
		return null;
	}
    public void populateModel() {
		if(getModel()!=null && cdp!=null){
			SimpleGroup sg=(SimpleGroup) getModel();
			for (Iterator it = sg.getAllSimpleChildren().iterator(); it.hasNext();) {
				SimpleType s = (SimpleType) it.next();
				if(s.getName().equals("immutableBackgroundImage")) {
					s.setValue(cdp.getBackGroundImage());
				}else if(s.getName().equals("mutableTemplateImage")) {
					s.setValue(cdp.getForegroundImage());
				}else if(s.getName().equals("correctionTemplateImage")) {
					s.setValue(cdp.getCorrectionImage());
				}
			}
		}
    }
}
