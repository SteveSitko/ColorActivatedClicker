package clicker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * This class will perform actions to find a specific color at a pixel contained inside a set of Points (essentially creating a rectangle to search within)
 * @author Steven
 */
public class ColorFinder {

	// Search rectangle
	private Point searchFrom;
	private Point searchOrigin;
	private Point searchTo;
	private int searchRadiusHorizontal;
	private int searchRadiusVertical;
	
	// Robot class to simulate native OS events like clicking or getting pixel color
	private Robot robo;
	
	// The color that the robot should search the rectangle for
	private Color desiredColor;
	private int r;
	private int g;
	private int b;
	
	// The level of tolerance allowed when searching for matching colors
	private int tolerance;
	
	private int actionDelay;		// All setter actions (color, bounds) wait 2 seconds
	
	public ColorFinder() {
		assignDefaults();
	}
	
	// Set the search rectangle (assign corners to a pair of points) after a delay
	public void setCheckLocationAtMouse() {
		
		try {
			// Get the searchFrom point (top left)
			Thread.sleep(actionDelay);								// Wait for actionDelay in milliseconds before getting the position
			searchOrigin = MouseInfo.getPointerInfo().getLocation();	// Gets the position of the mouse cursor
			
			setSearchBoundsByRadius();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Set the color to the color of the pixel at the mouse cursor after a delay
	public void setDesiredColorAtMouse() {
		try {
			Thread.sleep(actionDelay);
			Point currentMousePos = MouseInfo.getPointerInfo().getLocation();
			desiredColor = robo.getPixelColor(currentMousePos.x, currentMousePos.y);
			r = desiredColor.getRed();
			g = desiredColor.getGreen();
			b = desiredColor.getBlue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Search the boundaries for the desired color
	public boolean colorIsPresent() {
		boolean colorFound = false;
		
		for (int x = searchFrom.x; x < searchTo.x; x += 2) {
			for (int y = searchFrom.y; y < searchTo.y; y += 1) {
				Color colorAtPixel = robo.getPixelColor(x, y);		// Get the color at the pixel that is currently being checked
				
				// Return true if the color is found - loop will continue and return false if it is never found
				if (colorMatches(colorAtPixel, 1)) {
					return true;
				}
			}
		}
		
		return colorFound;
	}
	
	// Assign default values to fields
	public void assignDefaults() {
		tolerance = 12;
		actionDelay = 2000;
		desiredColor = new Color(0, 0, 0);
		
		searchOrigin = new Point(0, 0);
		searchFrom = new Point(0, 0);
		searchTo = new Point(0, 0);
		searchRadiusHorizontal = 15;
		searchRadiusVertical = 1;
		setSearchBoundsByRadius();
		
		try {
			robo = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	// Get the search box based on radius and origin
	private void setSearchBoundsByRadius() {
		searchFrom.x = (int) searchOrigin.getX() - searchRadiusHorizontal;
		searchFrom.y = (int) searchOrigin.getY() - searchRadiusVertical;
		searchTo.x = (int) searchOrigin.getX() + searchRadiusHorizontal;
		searchTo.y = (int) searchOrigin.getY() + searchRadiusVertical;
	}
	
	// Determine if a color parameter is appropriately similar to the desired color using tolerance
	private boolean colorMatches(Color pixelColor, int type) {
		boolean matches = false;
		
		int testR = pixelColor.getRed();
		int testG = pixelColor.getGreen();
		int testB = pixelColor.getBlue();
		
		boolean redMatch = false;
		boolean greenMatch = false;
		boolean blueMatch = false;
		
		// Test with tolerance
		if (type == 0) {
			redMatch = (testR <= r + tolerance) && (testR >= r - tolerance);
			greenMatch = (testG <= g + tolerance) && (testG >= g - tolerance);
			blueMatch = (testB <= b + tolerance) && (testB >= b - tolerance);
		} else if (type == 1) {	// Test with threshold
			redMatch = (testR >= 190);
			greenMatch = (testG >= 190);
			blueMatch = (testB <= 30);
		}
		
		if (redMatch && greenMatch && blueMatch) matches = true;
		
		return matches;
	}
	
	// Simulates a left mouse button click
	public void click() {
		int lmbMask = InputEvent.BUTTON1_DOWN_MASK;
		
		robo.mousePress(lmbMask);
		robo.mouseRelease(lmbMask);
		
	}
	
	// Getters, setters
	public int getActionDelay() {
		return actionDelay;
	}
	
	public Point getSearchFrom() {
		return searchFrom;
	}

	public Point getSearchTo() {
		return searchTo;
	}

	public Color getDesiredColor() {
		return desiredColor;
	}
}
