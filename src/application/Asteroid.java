package application;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// This class is used for the asteroid object, which moves constantly and bounces off walls
public class Asteroid 
{
	// Declare global variables
	private double xPos, yPos, width, height;
	private int index;
	private Image imgAsteroid;
	private ImageView ivAsteroid;
	private Random rnd;
	private int xChange = 1;
	private int dir;
	private double xSpeed = 4, ySpeed = 3; 
	
	// Constructor for the asteroid class
	Asteroid(double xSpeed, double ySpeed)
	{
		// Initialize random object
		rnd = new Random();

		// Initialize direction, index, and speed values
		dir = rnd.nextInt(2) + 1;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		index = rnd.nextInt(3);
		
		// Initialize image and imageview, use image in imageview
		imgAsteroid = new Image("file:images\\asteroid" + index + ".png");
		ivAsteroid = new ImageView(imgAsteroid);
		
		// Set default value for x and y positions
		xPos = 0;
		yPos = 0;
		
		// Initialize width and height
		width = imgAsteroid.getWidth();
		height = imgAsteroid.getHeight();
		
		// Set x and y position for imageview
		ivAsteroid.setX(xPos);
		ivAsteroid.setY(yPos);
	}
	
	// Method that sets x value
	public void setX(double x)
	{
		// Update x position, set imageview to x position
		xPos = x;
		ivAsteroid.setX(xPos);
	}
	
	// Method that sets y value
	public void setY(double y)
	{
		// Update y position, set imageview to y position
		yPos = y;
		ivAsteroid.setY(yPos);
	}
	
	// Method that sets location of asteroid
	public void setLocation(double frameWidth, double frameHeight)
	{
		// Set x position to random position across framewidth, and y position to above the screen
		xPos = (double)rnd.nextInt((int)frameWidth - (int)width);
		yPos = 0 - height;
		
		// Update imageview's x and y position
		ivAsteroid.setX(xPos);
		ivAsteroid.setY(yPos);
	}
	
	// Method that returns x value
	public double getX()
	{
		return xPos;
	}
	
	// Method that returns y value
	public double getY()
	{
		return yPos;
	}

	// Method that returns width
	public double getWidth()
	{
		return width;
	}
	
	// Method that returns height
	public double getHeight()
	{
		return height;
	}
	
	// Method that returns imageview
	public ImageView getImage()
	{
		return ivAsteroid;
	}
	
	// Method that changes x direction
	public void changeXDir()
	{
		xChange = -xChange;
	}
	
	// Method that moves asteroid
	public void move() 
	{
		// If direction is 1, move south west
		if (dir == 1)
		{
			// Decrease x position by x speed multiplied by xchange (to allow asteroid to bounce of walls)
			// Increase y position by y speed
			xPos -= xSpeed * xChange;
			yPos += ySpeed;
		}
		// If direction is 2, move south east
		else
		{
			// Increase x position by x speed multiplied by xchange (to allow asteroid to bounce of walls)
			// Increase y position by y speed
			xPos += xSpeed * xChange;
			yPos += ySpeed;
		}
		// Update imageview to new position
		ivAsteroid.setX(xPos);
		ivAsteroid.setY(yPos);
	}
	
	// Method that sets x position speed
	public void setXSpeed(double x)
	{
		xSpeed = x;
	}
	
	// Method that sets y position speed
	public void setYSpeed(double y)
	{
		ySpeed = y;
	}
}