package drawing;

import java.applet.Applet;

public class DrawingApplet extends Applet{
	private CompleteDrawingPanel panel;
	
	public void init() {
		panel=new CompleteDrawingPanel(getWidth(),getHeight(),false);
		String mutableForeground=getParameter("mutableForeground");
		panel.setDefaultForeGround(mutableForeground);		
		String foreground=getParameter("foreground");
		String undoData=getParameter("undoData");
		if(undoData!=null && undoData.length() > 0)
			panel.setForegroundWithUndoData(foreground+"%%%"+undoData);
		else {
			panel.setForegroundImage(foreground);
		}
		//stupid, panel resets bg when setting undodata :(
		String bgString=getParameter("background");
		panel.setBackGroundImage(bgString);
		boolean wasResetted=Boolean.valueOf(getParameter("resetted")).booleanValue();
		panel.setResetted(wasResetted);
		boolean editable=!Boolean.valueOf(getParameter("viewonly")).booleanValue();
		panel.setEditable(editable);
		add(panel);
	}
	
	public boolean hasChanged() {
		return panel.hasChanged();
	}
	public boolean isResetted(){
		return panel.isResetted();
	}
	public String getForegroundPictureWithUndoData(int numUndoSteps) {
		String s=panel.getForegroundPictureWithUndoData(numUndoSteps);
		return s;
	}
	
    public void start() {
    	panel.setHasChanged(false);
    }
    
    public void stop() {
    	panel.setHasChanged(false);
    }
}
