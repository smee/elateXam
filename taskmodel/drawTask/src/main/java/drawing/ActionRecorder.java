package drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ActionRecorder {
	private static int INITIAL_ACTIONS_COUNT;
	private final List previousActions;
	private int actionCount;
	private boolean canRedo;
	private final PropertyChangeSupport pcs=new PropertyChangeSupport(this);
	private boolean redrawing;
	public ActionRecorder(){
		previousActions=new ArrayList(30);
		reset();		
	}
	public void reset() {
		reset(null);
	}
	public void reset(List defaultActions){
		previousActions.clear();
		actionCount=0;
		addAction(new DrawAction(DrawAction.setDrawStrokeWidth,"1"));
		addAction(new DrawAction(DrawAction.setEraseStrokeWidth,"6"));
		addAction(new DrawAction(DrawAction.setBgColor,""+Color.WHITE.getRGB()));
		addAction(new DrawAction(DrawAction.setFgColor,""+Color.BLACK.getRGB()));
		addAction(new DrawAction(DrawAction.clear));
		if(defaultActions!=null) {
			for (Iterator it = defaultActions.iterator(); it.hasNext();)
				addAction((DrawAction) it.next());
		}
		INITIAL_ACTIONS_COUNT=actionCount=previousActions.size();
		canRedo=false;
		pcs.firePropertyChange("undo",true,false);
		pcs.firePropertyChange("redo",true,false);
	}
	public void redraw(DrawingPanel panel, Graphics2D workingGraphics){
		redrawing=true;
//		System.out.println("----redrawing----");
		for (int i=0;i<actionCount;i++) {
			DrawAction da = (DrawAction) previousActions.get(i);
//			System.out.println(da);
			da.draw(workingGraphics,panel);
		}		
//		System.out.println("------------------");
		redrawing=false;
	}
	public void addAction(DrawAction action){
		if(redrawing)
			return;
		boolean oldCanUndo=isUndoable();
		boolean oldCanRedo=isRedoable();
		canRedo=false;
		for (int i = previousActions.size()-1;i >= actionCount; i--) {
			previousActions.remove(i);
		}
		previousActions.add(action);
		actionCount++;
		pcs.firePropertyChange("undo",oldCanUndo,isUndoable());
		pcs.firePropertyChange("redo",oldCanRedo,canRedo);
	}
	public boolean isUndoable(){
		return actionCount>INITIAL_ACTIONS_COUNT;
	}
	
	public boolean isRedoable(){
		return canRedo;
	}
	
	public void undo(){
		boolean oldCanRedo=canRedo;
		boolean oldCanUndo=isUndoable();
		actionCount--;
		canRedo=true;		
		pcs.firePropertyChange("undo",oldCanUndo,isUndoable());
		pcs.firePropertyChange("redo",oldCanRedo,canRedo);
	}
	public void redo(){
		boolean oldCanUndo=isUndoable();
		boolean oldCanRedo=canRedo;
		actionCount++;
		canRedo=actionCount<previousActions.size();
		pcs.firePropertyChange("undo",oldCanUndo,isUndoable());
		pcs.firePropertyChange("redo",oldCanRedo,canRedo);
	}
	public String getAllActionData() {
		StringBuffer sb=new StringBuffer();
		for(Iterator it=previousActions.iterator();it.hasNext();)
			sb.append(it.next().toString()).append("|");
		if(sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	public String getLastUndoableActionData(int numSteps){
		StringBuffer sb=new StringBuffer();
		for(int i=Math.max(INITIAL_ACTIONS_COUNT,actionCount-numSteps);i<actionCount;i++)
			sb.append(previousActions.get(i).toString()).append("|");
		
		if(sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	public void readActionData(String s){
		if(s==null || s.length()==0)
			return;
		boolean oldCanUndo=isUndoable();
		boolean oldCanRedo=isRedoable();
		
		StringTokenizer st=new StringTokenizer(s,"|");
		while(st.hasMoreTokens()){
			addAction(new DrawAction(st.nextToken()));
		}
		
		canRedo=false;
		pcs.firePropertyChange("undo",oldCanUndo,isUndoable());
		pcs.firePropertyChange("redo",oldCanRedo,canRedo);
	}
	public void addPropertyChangeListener(PropertyChangeListener l){
		pcs.addPropertyChangeListener(l);
	}
	public void addPropertyChangeListener(String property,PropertyChangeListener l){
		pcs.addPropertyChangeListener(property,l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l){
		pcs.removePropertyChangeListener(l);
	}
	public void removePropertyChangeListener(String property,PropertyChangeListener l){
		pcs.removePropertyChangeListener(property,l);
	}
	public boolean isRedrawing() {
		return redrawing;
	}
	public DrawAction getLastAction() {
		if(actionCount>0)
			return (DrawAction) previousActions.get(actionCount-1);
		else
			return null;
	}
}
