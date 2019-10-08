package gui.pack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import biorobot.data.SystemParametersData;
import biorobot.pack.Calculations;
import biorobot.pack.SystemParameters;

/**
 * Draws the route panel in mainGUI.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 */

/*
 * Addtional info:
 * In our picture, we've made an adjustment, that 50 mm (gap between two small holes) 
 * equals 798 steps. By saying "coordinates" we mean steps, ex. if the coordinates of a hole equal
 * [300, 500] it means, that we must make 300 steps on X axis and 500 steps on Y axis to get to this hole.
 */

public class RoutePanel extends JPanel {
	
	public RoutePanel() {
		// Something here?
	}
	private static final long serialVersionUID = 1L;
	final static BasicStroke stroke = new BasicStroke(2.0f);
	public List<Ellipse2D> topPallette;
	public List<Ellipse2D> bottomPallette;
	SystemParametersData systemParametersData = new SystemParametersData();

	// At start initialize it with a dumb variable.
	private int actualHole = 27;
	private Color actualColor;
	private int expectedHole = 27;
	private Color expectedColor;
	private double xCoord = 0;
	private double yCoord = 0;

	// Calculate coordinates of a hole.
	private double[] calculateCoordinates(int holeNo) {
		// Get step coordinates for a hole and map them to [mm]/2.
		double coordinates[] = new double[2];
		// SystemParameters.GAP = 50/2 [mm] (taken from the schema).
		int gap = (systemParametersData.getGap() != 0) ? systemParametersData.getGap() : 768 ;
		xCoord = (Calculations.mapHoleNoToCoordinates(holeNo)[0]*SystemParameters.GAPINMM/2)/gap;
		yCoord = (Calculations.mapHoleNoToCoordinates(holeNo)[1]*SystemParameters.GAPINMM/2)/gap;
		// Now we have the coordinates of the center of hole. We want to have the coordinates of the top left corner.
		xCoord = (holeNo == 0 || holeNo == 25 ) ? xCoord - SystemParameters.BIGHOLEDIAMETER/4: xCoord - SystemParameters.SMALLHOLEDIAMETER/4;
		yCoord = (holeNo == 0 || holeNo == 25 ) ? yCoord - SystemParameters.BIGHOLEDIAMETER/4: yCoord - SystemParameters.SMALLHOLEDIAMETER/4;
		coordinates[0] = xCoord;
		coordinates[1] = yCoord;
		return coordinates;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// Draw smooth circles.
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		double bigHoleDiameter = SystemParameters.BIGHOLEDIAMETER/2;
		double smallHoleDiameter = SystemParameters.SMALLHOLEDIAMETER/2;
		// Top palette.
		topPallette = new ArrayList<Ellipse2D>();
		topPallette.add(new Ellipse2D.Double(calculateCoordinates(0)[0], calculateCoordinates(0)[1], bigHoleDiameter, bigHoleDiameter));
		for (int i = 1; i <= 24; i++) {
			topPallette.add(new Ellipse2D.Double(calculateCoordinates(i)[0], calculateCoordinates(i)[1], smallHoleDiameter, smallHoleDiameter));
		}
		topPallette.add(new Ellipse2D.Double(calculateCoordinates(25)[0], calculateCoordinates(25)[1], bigHoleDiameter, bigHoleDiameter));
		// Bottom palette.
		bottomPallette = new ArrayList<Ellipse2D>();
		bottomPallette.add(new Ellipse2D.Double(calculateCoordinates(0)[0], calculateCoordinates(0)[1] + 100, bigHoleDiameter, bigHoleDiameter));
		for (int i = 1; i <= 24; i++) {
			bottomPallette.add(new Ellipse2D.Double(calculateCoordinates(i)[0], calculateCoordinates(i)[1] + 100, smallHoleDiameter, smallHoleDiameter));
		}
		bottomPallette.add(new Ellipse2D.Double(calculateCoordinates(25)[0], calculateCoordinates(25)[1] + 100, bigHoleDiameter, bigHoleDiameter));
		
		// Draw palettes.
		g2.setStroke(stroke);
		for (int i = 0; i < topPallette.size(); i++) {
			g2.draw(topPallette.get(i));
		}
		for (int i = 0; i < bottomPallette.size(); i++) {
			g2.draw(bottomPallette.get(i));
		}
		// Fill palettes.
		g2.setPaint(Color.LIGHT_GRAY);
		for (int i = 0; i < topPallette.size(); i++) {
			g2.fill(topPallette.get(i));
		}
		for (int i = 0; i < bottomPallette.size(); i++) {
			g2.fill(bottomPallette.get(i));
		}

		// Fill expected hole
		if (expectedHole != 27) {
			g2.setPaint(expectedColor);
			g2.fill(topPallette.get(expectedHole));
			g2.fill(bottomPallette.get(expectedHole));
		}

		// Fill actual hole
		if (actualHole != 27) {
			g2.setPaint(actualColor);
			g2.fill(topPallette.get(actualHole));
			g2.fill(bottomPallette.get(actualHole));
		}

		// Draw the [0,0] point.
		g2.setPaint(Color.BLACK);
		g2.draw(new Ellipse2D.Double(0, 0, 1, 1));

		//		// Draw the route visualize line.
		//		if (visualizeLine == true) {
		//			g2.setPaint(visualizeColor);
		//		}

		//		// Draw the movement line.
		//		if (drawLine == true) {
		//			g2.setPaint(lineColor);
		//		}
		//		
		//		// Draw the cross (actual coordinates).
		//		if (drawLine == true) {
		//			g2.setPaint(crossColor);
		//		}

		// Draw hole numbers.
		g2.setPaint(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			g2.drawString(Integer.toString(i), (float) (topPallette.get(i).getCenterX()-3), (float) (topPallette.get(i).getCenterY()+4.375));
		}
		for (int i = 10; i < topPallette.size(); i++) {
			g2.drawString(Integer.toString(i), (float) (topPallette.get(i).getCenterX()-5), (float) (topPallette.get(i).getCenterY()+4.375));
		}
		for (int i = 0; i < 10; i++) {
			g2.drawString(Integer.toString(i), (float) (bottomPallette.get(i).getCenterX()-3), (float) (bottomPallette.get(i).getCenterY()+4.375));
		}
		for (int i = 10; i < bottomPallette.size(); i++) {
			g2.drawString(Integer.toString(i), (float) (bottomPallette.get(i).getCenterX()-5), (float) (bottomPallette.get(i).getCenterY()+4.375));
		}

	}

	public int getActualHole() {
		return actualHole;
	}

	public void setActualHole(int actualHole) {
		this.actualHole = actualHole;
	}

	public Color getActualColor() {
		return actualColor;
	}

	public void setActualColor(Color actualColor) {
		this.actualColor = actualColor;
	}

	public int getExpectedHole() {
		return expectedHole;
	}

	public void setExpectedHole(int expectedHole) {
		this.expectedHole = expectedHole;
	}

	public Color getExpectedColor() {
		return expectedColor;
	}

	public void setExpectedColor(Color expectedColor) {
		this.expectedColor = expectedColor;
	}

}
