/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawshape;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
//import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
/**
 *
 * @author hiroki
 */


public class DrawShape {
	public static void main(String[] args) {
		while (true) {
			System.out.println("Please input # of sides: ");
			int sides = TextIO.getlnInt(); // IllegalArgumentException is handled; looped until legal input

			System.out.println("Please input # of jump: ");
			int jump = TextIO.getlnInt(); // IllegalArgumentException is handled; looped until legal input
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					createAndShowGUI(sides, jump);
				}
			});
		}
	}
	
	private static void createAndShowGUI(int sides, int jump) {
//		System.out.println("Created GUI on EDT? " +
//			SwingUtilities.isEventDispatchThread());
		
		JFrame f = new JFrame("Swing Draw Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel(sides, jump));
		f.pack();
		f.setVisible(true);
	}
  
}  

class MyPanel extends JPanel {
	public int sides, jump;
	
	public MyPanel(int sides, int jump) {
		this.sides = sides;
		this.jump = jump;
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}
	@Override
	public void paintComponent(Graphics g) {
		Point2D.Double[] points; // coordinates of polygon vertices
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2);
						
		points = new Point2D.Double[sides];

		for (int j=0; j<points.length; j++) {
			points[j] = getPolygonPoint(sides, j);
		}

		for (int k=0; k<points.length; k++) {
			Point2D.Double start;
			Point2D.Double end;

			if (k == points.length - 1) { // last one connects to first vertex
				start = points[k];
				end = points[0];
			}
			else {
				start = points[k];
				end = points[k+1];
			}

			g2.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
		}

		for (int l=0; l<sides; l++) {
			int fromIndex;
			int toIndex;
			Point2D.Double start;
			Point2D.Double end;

			fromIndex = l;
			toIndex = l * jump;
			while (fromIndex >= sides) {
				fromIndex -= sides;
			}
			while (toIndex >= sides) {
				toIndex -= sides;
			}

			start = points[fromIndex];
			end = points[toIndex];

			g2.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
		}
		
	}
	
	private Point2D.Double getPolygonPoint(int sides, int i) {
		Point2D.Double centerPoint; // center of circle
		int radius;
		Point2D.Double vertexPoint;
		double angle;
		
		// assume 400x400 to fit shapes in
		centerPoint = new Point2D.Double(200.0, 200.0);
		radius = 200;
		vertexPoint = new Point2D.Double();
		angle = (360.0 / sides) * i;
		double x,y; // temp variable; to be values of vertexPoint
		double theta;
		
		if (angle <= 90.0) {
			theta = Math.toRadians(angle);

			x = centerPoint.getX() - (radius * Math.cos(theta));
			y = centerPoint.getY() - (radius * Math.sin(theta));
		}
		else if (90.0 < angle && angle <= 180.0) {
			theta = Math.toRadians(180.0 - angle);
			x = centerPoint.getX() + (radius * Math.cos(theta));
			y = centerPoint.getY() - (radius * Math.sin(theta));
		}
		else if (180.0 < angle && angle <= 270.0) {
			theta = Math.toRadians(270.0 - angle);
			x = centerPoint.getX() + (radius * Math.sin(theta));
			y = centerPoint.getY() + (radius * Math.cos(theta));
		}
		else {
			theta = Math.toRadians(360.0 - angle);
			x = centerPoint.getX() - (radius * Math.cos(theta));
			y = centerPoint.getY() + (radius * Math.sin(theta));
		}
		
		vertexPoint.setLocation(x, y);
		return vertexPoint;
	}
}