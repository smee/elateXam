package drawing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener {
	private final ActionRecorder rec;
	private final Image workingImage;
	private final Graphics2D workingGraphics;
	
	private Color fgColor;
	private int crntAction;
	private int drawingStrokeWidth, eraseStrokeWidth;
	
	private final int w;
	private final int h;
	
	private StringBuffer coords;
	private int newY;
	private int newX;
	private int oldY;
	private int oldX;
	
	private boolean mouseInPanel;
	private boolean mousePressed;
	private Image backGround;
	private Composite standardComposite;
	private boolean hasChanged=false;
	private boolean editable;
	
	
	public DrawingPanel(){
		this(800,600);
	}
	public DrawingPanel(int width, int height){
		this.w=width;
		this.h=height;
		this.editable=true;
		rec=new ActionRecorder();
		Dimension dim=new Dimension(width,height);
		setPreferredSize(dim);
		setSize(dim);
		workingImage=Util.generateTransparentImage(width,height);
		workingGraphics=(Graphics2D) workingImage.getGraphics();
		workingGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		standardComposite=workingGraphics.getComposite();
		addMouseMotionListener(this);
		addMouseListener(this);
		reset();
	}
	public void reset() {
		reset(null);
	}
	/**
	 * resets the drawing panel. If the given base64 encoded picture isn't null, the foreground picture
	 * gets set without showing up as undoable.
	 * @param foreGroundImageString
	 */
	public void reset(String foreGroundImageString) {
		setColor(Color.BLACK);
		setDrawingStrokeWidth(1);
		setEraseStrokeWidth(6);
		setFreehandMode();
		setBackgroundImage(Util.generateBlankImage(w,h,Color.WHITE));
		List l=new LinkedList();
		if(foreGroundImageString!=null) {
			l.add(new DrawAction(DrawAction.drawMutableBackground,foreGroundImageString));
		}
		rec.reset(l);
		rePaintCanvas();		
	}
	public void setForegroundWithUndoData(String s) {
		if(s==null || s.indexOf("%%%")<0)
			return;
		String[] split=s.split("%%%");
		reset(split[0]);
		rec.readActionData(split[1]);
		rePaintCanvas();
	}
	
	public String getForegroundWithUndoString(int numUndoSteps){
		int i;
		for(i=0;i<numUndoSteps && rec.isUndoable();i++)
			rec.undo();
		rePaintCanvas();
		String mbg=Util.getBase64StringFromImage(workingImage);		
		while(i-->0)
			rec.redo();
		rePaintCanvas();
		
		return mbg+"%%%"+rec.getLastUndoableActionData(numUndoSteps);
	}
	public String getCompletePictureBase64() {
		BufferedImage img=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		this.paint(img.getGraphics());//dirty hack :)
		return Util.getBase64StringFromImage(img);
	}
	public void setBackgroundImage(Image bgImg){
		Image oldimg=this.backGround;
		this.backGround=bgImg;
		repaint();
		firePropertyChange("backgroundImage",oldimg,backGround);
	}
	
	public Image getBackgroundImage(){
		return backGround;
	}
	public void setBackgroundImage(String value) {
		if(value==null || value.length()==0)
			return;
		setBackgroundImage(Util.getImageFromBase64String(value));
	}
	
	/**
	 * Adds a foregroundimage to the workingimage. Shows up as undoaction!
	 * @param value
	 */
	public void addForegroundImage(String value) {
		if(value==null || value.length()==0)
			return;
		rec.addAction(new DrawAction(DrawAction.drawMutableBackground,value));
		rePaintCanvas();
	}
	public String getBackgroundImageString() {
		return Util.getBase64StringFromImage(backGround);
	}
	public String getForegroundImageString() {
		return Util.getBase64StringFromImage(workingImage);
	}
	public void paint(Graphics g){
		Graphics2D g2=(Graphics2D) g;
		drawCurrentLine();
		g2.setBackground(Color.LIGHT_GRAY);
		if(backGround!=null){
			g2.drawImage(backGround,0,0,null);
		}
		g2.drawImage(workingImage,0,0,null);
	}
	private void drawCurrentLine() {
		if(mouseInPanel && mousePressed){
			workingGraphics.drawLine(oldX,oldY,newX,newY);
		}
	}
	private void rePaintCanvas() {
		rec.redraw(this,workingGraphics);
		repaint();
	}
	public void mouseDragged(MouseEvent e) {
		if(!editable)
			return;
		oldX=newX;
		oldY=newY;
		newX=e.getX();
		newY=e.getY();
		coords.append(';').append(newX).append(';').append(newY);
		repaint();
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if(!editable)
			return;
		mousePressed=true;
		hasChanged=true;
		newX=oldX=e.getX();
		newY=oldY=e.getY();
		coords=new StringBuffer();
		if(crntAction==DrawAction.freehand)
			coords.append(drawingStrokeWidth);
		else
			coords.append(eraseStrokeWidth);
		coords.append(';').append(newX).append(';').append(newY);
		repaint();
	}
	public void mouseReleased(MouseEvent e) {
		if(!editable)
			return;
		mousePressed=false;
		DrawAction oldAction=rec.getLastAction();
		DrawAction da=new DrawAction(crntAction,coords.toString());
		rec.addAction(da);
		rePaintCanvas();
		firePropertyChange("action",oldAction,da);
	}
	public void mouseEntered(MouseEvent mouseevent)
	{
		mouseInPanel = true;
		repaint();
	}
	
	public void mouseExited(MouseEvent mouseevent)
	{
		mouseInPanel = false;
		repaint();
	}
	
	public void undo() {
		hasChanged=true;
		if(rec.isUndoable()){
			rec.undo();
			rePaintCanvas();
		}
	}
	public void redo() {
		hasChanged=true;
		if(rec.isRedoable()){
			rec.redo();
			rePaintCanvas();
		}			
	}
	public void clear(String image) {
		DrawAction a=new DrawAction(DrawAction.clear,image);
		rec.addAction(a);
		a.draw(workingGraphics,this);
	}
	public int getMode() {
		return crntAction;
	}
	public void setMode(int mode) {
		switch (mode) {
		case DrawAction.freehand:
			setFreehandMode();
			break;
		case DrawAction.erase:
			setEraseMode();
		default:
			break;
		}
	}
	public Color getColor(){
		return fgColor;
	}
	public void setColor(Color color) {
		if(color!=null && color.equals(fgColor))
			return;
		workingGraphics.setColor(color);
		updateDrawingCursorNStroke(crntAction,crntAction==DrawAction.freehand?drawingStrokeWidth:eraseStrokeWidth);
		Color oldColor=fgColor;
		this.fgColor=color;
		rec.addAction(new DrawAction(DrawAction.setFgColor,""+color.getRGB()));
		firePropertyChange("color",oldColor,fgColor);
	}
	public void setFreehandMode() {
		if(crntAction==DrawAction.erase) {
			workingGraphics.setComposite(standardComposite);
			workingGraphics.setColor(fgColor);
			int oldMode=crntAction;
			crntAction=DrawAction.freehand;
			updateDrawingCursorNStroke(DrawAction.freehand,drawingStrokeWidth);
			firePropertyChange("mode",oldMode,crntAction);
		}
	}
	public void setEraseMode() {
		if(crntAction==DrawAction.freehand) {
			workingGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			int oldMode=crntAction;
			crntAction=DrawAction.erase;
			updateDrawingCursorNStroke(DrawAction.erase,eraseStrokeWidth);
			firePropertyChange("mode",oldMode,crntAction);
		}
	}
	public void setDrawingStrokeWidth(int width){
		if(width <=0)
			return;
		int oldWidth=-1;
//		System.out.println("drawingStroke: "+width);
		oldWidth=drawingStrokeWidth;
		drawingStrokeWidth=width;
		if(crntAction==DrawAction.freehand)
			updateDrawingCursorNStroke(crntAction,width);
		firePropertyChange("drawingStrokeWidth",oldWidth,drawingStrokeWidth);
	}
	public void setEraseStrokeWidth(int width){
		if(width <=0)
			return;
		int oldWidth=-1;
//		System.out.println("eraseStroke: "+width);
		oldWidth=eraseStrokeWidth;
		eraseStrokeWidth=width;
		if(crntAction==DrawAction.erase)
			updateDrawingCursorNStroke(crntAction,width);
		firePropertyChange("eraseStrokeWidth",oldWidth,eraseStrokeWidth);
	}
	public int getEraseStrokeWidth() {
		return eraseStrokeWidth;
	}
	public int getDrawingStrokeWidth() {
		return drawingStrokeWidth;
	}
	private void updateDrawingCursorNStroke(int mode,int newStrokeWidth) {
		workingGraphics.setStroke(new BasicStroke(newStrokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		
		Color fgcol=mode==DrawAction.erase?Color.WHITE:workingGraphics.getColor();
		Cursor cursor=Util.createDrawingCursor(
				fgcol,
				fgcol.equals(Color.WHITE)?Color.BLACK:fgcol,
						newStrokeWidth,
						mode==DrawAction.freehand);		
		if(!getCursor().getName().equals(cursor.getName())) {//sind eh alles meine instanzen
			setCursor(cursor);
		}
	}
	
	public ActionRecorder getActionRecorder(){
		return rec;
	}
	
	
	public static void main(String[] args) {
		final JFrame f=new JFrame();
		CompleteDrawingPanel cdp=new CompleteDrawingPanel(600,350,true);
		cdp.setStrokeChangeable(true);
		cdp.setColorChangeable(true);
		f.add(cdp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	public boolean hasChanged() {
		return hasChanged;
	}
	public void setHasChanged(boolean flag) {
		this.hasChanged=flag;
	}
	public void setEditable(boolean flag) {
		this.editable=flag;
	}
}
