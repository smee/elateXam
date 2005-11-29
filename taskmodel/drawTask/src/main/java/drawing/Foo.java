package drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Foo {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main2(String[] args) throws IOException {
		final BufferedImage backGround=new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB);
		Graphics2D g=(Graphics2D) backGround.getGraphics();
		g.setColor(Color.BLACK);
		g.setBackground(Color.WHITE);
		g.clearRect(0,0,800,600);
		g.drawOval(0,0,800,600);
		
		final BufferedImage transparentImg=Util.generateTransparentImage(800,600);
		Graphics2D g2=(Graphics2D) transparentImg.getGraphics();
		g2.setColor(Color.BLACK);
		g2.drawLine(0,0,800,600);
		
		//g.drawImage(img,0,0,800,600,null);
		JFrame f=new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JPanel(){
			public void paint(Graphics g){
				g.drawImage(backGround,0,0,800,600,null);
				g.drawImage(transparentImg,0,0,800,600,null);
			}
			public Dimension getPreferredSize(){
				return new Dimension(800,600);
			}
		});
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args) throws IOException {
		GZIPOutputStream gos=new GZIPOutputStream(System.out);
		gos.write("teststringggggg".getBytes());
	}
}
