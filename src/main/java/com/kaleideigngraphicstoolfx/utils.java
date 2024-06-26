package com.kaleideigngraphicstoolfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class utils {
	
	public static int distFromCenter(Point center, int x, int y){
		return (int) Math.sqrt((Math.pow((center.x-x),2)+Math.pow((center.y-y),2)));
	}
	
	public static void drawSimple(Graphics2D g, int x1, int y1, int x2, int y2, Color c){
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(c);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.dispose();
	}
	
	public static void rotateCornerDraw(Graphics2D g, int x1, int y1, int x2, int y2, Color c, Squnit square){
		Point closestCorner = findClosestCorner(x2, y2, square);
		for(int i=0; i<=4; i++){
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(c);
			g2d.setStroke(new BasicStroke(2));
			g2d.translate(closestCorner.x, closestCorner.y);
			g2d.rotate(Math.toRadians(i*90));
			g2d.translate(-closestCorner.x, -closestCorner.y);
			g2d.drawLine(x1, y1, x2, y2);
			g2d.dispose();
		}
	}
	
	public static void scaleDraw(Graphics2D g, int x1, int y1, int x2, int y2, Color c, int initx, int inity, float scale){
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.drawLine((int) (initx*(1-scale)+x1*scale), (int)(inity*(1-scale)+y1*scale),(int) (initx*(1-scale)+x2*scale),(int) (inity*(1-scale)+y2*scale));
		g2d.dispose();
	}
	
	public static Point findClosestCorner(int x, int y, Squnit squnit){
		Point closest = new Point();
		int size = squnit.getSize();
		int sx = squnit.getX();
		int sy = squnit.getY();
		int c1 = (int) Math.sqrt((Math.pow(sx-x, 2) + Math.pow(sy-y, 2)));
		int c2 = (int) Math.sqrt((Math.pow(sx+size-x, 2) + Math.pow(sy-y, 2)));
		int c3 = (int) Math.sqrt((Math.pow(sx-x, 2) + Math.pow(sy+size-y, 2)));
		int c4 = (int) Math.sqrt((Math.pow(sx+size-x, 2) + Math.pow(sy+size-y, 2)));
		int minc = Math.min(Math.min(c1, c2), Math.min(c3, c4));
		if(minc == c1){
			System.out.println("tl");
			closest.x = sx;
			closest.y = sy;
		}else if(minc == c2){
			System.out.println("tr");
			closest.x = sx+size;
			closest.y = sy;
		}else if(minc == c3){
			System.out.println("bl");
			closest.x = sx;
			closest.y = sy+size;
		}else if(minc == c4){
			System.out.println("br");
			closest.x = sx+size;
			closest.y = sy+size;
		}
		return closest;
	}
	
	//rotateDraw takes in the coordinates of a line and rotates it a number of times (nseg)
	//around another given point (center)
	public static void rotateDraw(Graphics2D g, int nseg, int x1, int y1, int x2, int y2, Point center, boolean mirrored){
		for(int i=0; i<=nseg; i++){
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.translate(center.x, center.y);
			g2d.rotate(Math.toRadians(i*360/nseg));
			g2d.translate(-center.x, -center.y);
			g2d.drawLine(x1, y1, x2, y2);
			if(mirrored){ //reflect the stroke within the segment
				g2d.drawLine(center.x+(center.x-x1), y1, center.x+(center.x-x2), y2);
			}
			g2d.dispose();
		}
	}
	public static void rotateScaleDraw(Graphics2D g, int nseg, int x1, int y1, int x2, int y2, Point center, Color c, boolean ref, int initx, int inity, float scale, int num){
		for(int i=0; i<=nseg; i++){
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(c);
			g2d.setStroke(new BasicStroke(2));
			g2d.translate(center.x, center.y);
			g2d.rotate(Math.toRadians(i*360/nseg));
			g2d.translate(-center.x, -center.y);
			utils.scaleDraw(g2d, x1, y1, x2, y2, c, initx, inity, 1);
			utils.scaleDraw(g2d, x1, y1, x2, y2, c, initx, inity, scale);
			if(ref){
				utils.scaleDraw(g2d, center.x+(center.x-x1), y1, center.x+(center.x-x2), y2, c, initx, inity, 1);
				utils.scaleDraw(g2d, center.x+(center.x-x1), y1, center.x+(center.x-x2), y2, c, initx, inity, scale);
			}
			g2d.dispose();
			}
			
		
	}
	
	

}
