package drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Util {
	  public static Image makeColorTransparent(Image im, final Color color) {
	    ImageFilter filter = new RGBImageFilter() {
	      // the color we are looking for... Alpha bits are set to opaque
	      public int markerRGB = color.getRGB() | 0xFF000000;

	      public final int filterRGB(int x, int y, int rgb) {
	        if ( ( rgb | 0xFF000000 ) == markerRGB ) {
	          // Mark the alpha bits as zero - transparent
	          return 0x00FFFFFF & rgb;
	          }
	        else {
	          // nothing to do
	          return rgb;
	          }
	        }
	      }; 
	    ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	    }
	  
	  public static BufferedImage generateTransparentImage(int w,int h){
		  BufferedImage image = new BufferedImage(w, h,
				  BufferedImage.TYPE_INT_ARGB);
		  // set completely transparent
		  for (int col = 0; col < w; col++) {
			  for (int row = 0; row < h; row++) {
				  image.setRGB(col, row, 0x0);
			  }
		  }
		  return image;
	  }
	  public static String getBase64StringFromImage(Image img){
		  ByteArrayOutputStream bos=new ByteArrayOutputStream();
		  try {
			ImageIO.write(toBufferedImage(img),"png",bos);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		  return Base64.byteArrayToBase64(bos.toByteArray());
		  
	  }
	  public static Image getImageFromBase64String(String base64){
		  return new ImageIcon(Base64.base64ToByteArray(base64)).getImage();
	  }
	  private final static Map cursorMap=new HashMap();
	  
	public static BufferedImage createDrawingCursorImage(Color color, Color border, int width,boolean showCross) {
		BufferedImage img=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=(Graphics2D) img.getGraphics();
		if(showCross){
			BasicStroke dashed = new BasicStroke(0.0f, 
					BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 
					10.0f, new float[]{1.0f}, 0.0f);
			g2.setStroke(dashed);
			g2.setColor(Color.BLACK);
			g2.drawLine(16,0,16,31);
			g2.drawLine(0,16,31,16);
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		Shape circle=new Ellipse2D.Double(16-width/2,16-width/2,width,width);
		g2.fill(circle);
		g2.setColor(border);
		g2.draw(circle);
		return img;
	}
	public static Cursor createDrawingCursor(Color color, Color border, int width,boolean showCross) {
		if(width>32)
			width=32;
		if(width<1)
			width=1;
		
		String key=new StringBuffer("").append(width).append("-").append(color.getRGB()).append("-").append(border.getRGB()).append("-").append(showCross).toString();
		Cursor cursor=(Cursor) cursorMap.get(key);
		if(cursor!=null)
			return cursor;
		
		BufferedImage img=createDrawingCursorImage(color,border,width,showCross);
		cursor=java.awt.Toolkit.getDefaultToolkit().createCustomCursor(img,new Point(img.getWidth()/2,img.getHeight()/2),key);
		cursorMap.put(key,cursor);
		return cursor;
	}
	   // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }
    // This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
    
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
    
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

	public static Image generateBlankImage(int w, int h, Color col) {
		BufferedImage img=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2=(Graphics2D) img.getGraphics();
		g2.setBackground(col);
		g2.clearRect(0,0,w,h);
		return img;
	}

	public static Image combineImages(Image base,String[] base64) {
		if(base64==null || base64.length==0)
			throw new IllegalArgumentException("no images given as arguments!");
		
		Image[] img=new Image[base64.length];
		for(int i=0;i<base64.length;i++) {
			if(base64[i]!=null)
				img[i]=Util.getImageFromBase64String(base64[i]);
		}
		Graphics2D g2d=(Graphics2D) base.getGraphics();
		for (int i = 0; i < img.length; i++) {
			g2d.drawImage(img[i],0,0,null);
		}
		return base;
	}
}
