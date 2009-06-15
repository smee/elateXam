package drawing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;

public class DrawAction {
	public static final int freehand=0;
	public static final int erase=1;
	public static final int clear=2;
	public static final int setFgColor=3;
	public static final int setBgColor=4;
	public static final int setDrawStrokeWidth=5;
	public static final int setEraseStrokeWidth=6;
	public static final int drawMutableBackground = 7;
	
	private final int action;
	private Object val;
	private int strokeWidth;
	private String options;
	
	public DrawAction(int type) {
		this(type,null);
	}
	public DrawAction(String options){
		this(Integer.parseInt(options.substring(0,options.indexOf(';'))),
			 options.substring(options.indexOf(';')+1));
	}
	public DrawAction(int type,String options){
		this.action=type;
		this.options=options;
		switch (type) {
		case freehand:
		case erase:
			strokeWidth=Integer.parseInt(options.substring(0,options.indexOf(';')));
			int[] option=parseOptions(options.substring(options.indexOf(';')+1));//skipping strokeWidth
			val=createPolygon(option);
			break;
		case drawMutableBackground:
			val=Util.getImageFromBase64String(options);
			break;
		case clear:
			if(options!=null)
				val=Util.getImageFromBase64String(options);
			break;
		default:
			break;
		}
	}
	private Shape createPolygon(int[] options) {
		GeneralPath shape=new GeneralPath(GeneralPath.WIND_EVEN_ODD, options.length/2);
		shape.moveTo(options[0],options[1]);
		shape.lineTo(options[0],options[1]);
		for (int i = 2; i < options.length-2; i+=2) {
			shape.lineTo(options[i],options[i+1]);
		}
		return shape;
	}
	private int[] parseOptions(String optString) {
		StringTokenizer st=new StringTokenizer(optString,";");
		int[] arr=new int[st.countTokens()];
		int idx=0;
		while(st.hasMoreTokens()){
			arr[idx++]=Integer.parseInt(st.nextToken());
		}
		return arr;
	}
	public void draw(Graphics2D g2, DrawingPanel panel){
		switch (action) {
		case clear:
//			g2.clearRect(0,0,panel.getWidth(),panel.getHeight());
			Composite orig=g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			Rectangle2D.Double rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight()); 
			g2.fill(rect);
			g2.setComposite(orig);
			panel.setFreehandMode();
			if(val==null)
				break;
		case drawMutableBackground:
			g2.drawImage((Image)val,0,0,null);
			break;
		case freehand:
			panel.setFreehandMode();
			panel.setDrawingStrokeWidth(strokeWidth);
			g2.draw((Shape) val);
			break;
		case erase:
			panel.setEraseMode();
			panel.setEraseStrokeWidth(strokeWidth);
			g2.draw((Shape) val);
			break;
		case setFgColor:
			panel.setColor(Color.decode(options));
			break;
		case setBgColor:
			g2.setBackground(Color.decode(options));
			break;
		case setDrawStrokeWidth:
			panel.setDrawingStrokeWidth(Integer.parseInt(options));
			break;
		case setEraseStrokeWidth:
			panel.setEraseStrokeWidth(Integer.parseInt(options));
			break;
		default:
			break;
		}
	}
	public String toString(){
		return new StringBuffer("").append(action).append(";").append(options).toString();
	}
	public int getType() {
		return action;
	}
}
