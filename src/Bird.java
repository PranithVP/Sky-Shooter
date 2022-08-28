

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// This class is responsible for the bird object
public class Bird 
{
	// Declare global variables
	private double xPos, yPos, width, height;
	private int index, speed;
	private Image imgBird;
	private ImageView ivBird;
	private Random rnd;
	
	// Constructor for bird class
	Bird()
	{
		// Initialize random object, speed, and index
		rnd = new Random();
		speed = (rnd.nextInt(4)/2) + 4;
		index = rnd.nextInt(3);
		
		// Initialize image/imageview, use image for imageview
		imgBird = new Image("file:images\\bird" + index + ".png");
		ivBird = new ImageView(imgBird);
		
		// Assign default values for x and y position
		xPos = 0;
		yPos = 0;
		
		// Initialize width and height of bird
		width = imgBird.getWidth();
		height = imgBird.getHeight();
		
		// Set x and y position of imageview
		ivBird.setX(xPos);
		ivBird.setY(yPos);
	}
	
	// Method that sets x value
	public void setX(double x)
	{
		// Sets x position value, updates x position of imageview
		xPos = x;
		ivBird.setX(xPos);
	}
	
	// Method that sets y value
	public void setY(double y)
	{
		// Sets y position value, updates y position of imageview
		yPos = y;
		ivBird.setY(yPos);
	}
	
	// Method that sets location of bird at the top of the screen
	public void setLocation(double frameWidth, double frameHeight)
	{
		// Set x to random position across x axis, set y value above the screen
		xPos = (double)rnd.nextInt((int)frameWidth - (int)width);
		yPos = 0 - height;
		
		// Update x and y position of imageview
		ivBird.setX(xPos);
		ivBird.setY(yPos);
	}
	
	// Return x position
	public double getX()
	{
		return xPos;
	}
	
	// Return y position
	public double getY()
	{
		return yPos;
	}

	// Return width
	public double getWidth()
	{
		return width;
	}
	
	// Return height
	public double getHeight()
	{
		return height;
	}
	
	// Return image view of bird
	public ImageView getImage()
	{
		return ivBird;
	}
	
	// Moves the bird straight down
	public void move()
	{
		// Increase y position by speed, update y position of imageview
		yPos += speed;
		ivBird.setY(yPos);
	}
}
