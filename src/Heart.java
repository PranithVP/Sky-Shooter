

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Heart 
{
	// Declare and initialize global variables
	private double xPos, yPos, width, height;
	private Image imgHeart;
	private ImageView ivHeart;
	private Random rnd;
	
	// Constructor for heart class
	Heart()
	{
		// Initialize random object
		rnd = new Random();
		
		// Initialize image and imageview of heart, use image for imageview
		imgHeart = new Image("file:images\\heart2.png");
		ivHeart = new ImageView(imgHeart);
		
		// Assign default value for x and y position
		xPos = 0;
		yPos = 0;
		
		// Set width and height of heart
		width = imgHeart.getWidth();
		height = imgHeart.getHeight();
		
		// Set x and y position of imageview
		ivHeart.setX(xPos);
		ivHeart.setY(yPos);
	}
	
	// Method that sets x position
	public void setX(double x)
	{
		// Set x position, update x position of imageview
		xPos = x;
		ivHeart.setX(xPos);
	}
	
	// Method that sets y position
	public void setY(double y)
	{
		// Set y position, update y position of imageview
		yPos = y;
		ivHeart.setY(yPos);
	}
	
	// Method that sets location
	public void setLocation(double frameWidth, double frameHeight)
	{
		// Set x position to random location across x axis, y position on top of screen
		xPos = (double)rnd.nextInt((int)frameWidth - (int)width);
		yPos = 0 - height;
		
		// Update x and y position of imageview
		ivHeart.setX(xPos);
		ivHeart.setY(yPos);
	}
	
	// Method that returns x position
	public double getX()
	{
		return xPos;
	}

	// Method that returns y position
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
		return ivHeart;
	}
	
	// Method that moves heart down
	public void move()
	{
		// Set y position, update y position of image view
		yPos += 3;
		ivHeart.setY(yPos);
	}
}
