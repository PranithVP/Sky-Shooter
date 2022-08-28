package application;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Bullet class is responsible for bullets shot by aircraft
public class Bullet 
{
	// Declare global variables
	private double xPos, yPos, width, height;
	private int dir;
	final static int STRAIGHT = 0, LEFT = 1, RIGHT = 2;
	private Image imgBullet;
	private ImageView ivBullet;
	private Random rnd;
	private int colour;
	
	// Constructor for bullet class
	Bullet()
	{
		// Initialize direction, random object, and colour variable
		dir = 0;
		rnd = new Random();
		colour = rnd.nextInt(3);
		
		// Initialize image according to chosen colour, initialize imageview
		if (colour == 0)
			imgBullet = new Image("file:images\\red.png");
		else if (colour == 1)
			imgBullet = new Image("file:images\\blue.png");
		else
			imgBullet = new Image("file:images\\green.png");
		ivBullet = new ImageView(imgBullet);
		
		// Assign default value for x and y position
		xPos = 0;
		yPos = 0;
		
		// Set width and height
		width = imgBullet.getWidth();
		height = imgBullet.getHeight();
		
		// Set x and y position of imageview
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
	
	// Method that sets x position
	public void setX(double x)
	{
		// Set x position and update x position of imageview
		xPos = x;
		ivBullet.setX(xPos);
	}
	
	// Method that sets y position
	public void setY(double y)
	{
		// Set y position and update y position of imageview
		yPos = y;
		ivBullet.setY(yPos);
	}
	
	// Method that sets location and direction
	public void setLocationAndDirection(double playerX, double playerY, double playerWidth, double playerHeight, int playerDir)
	{
		// Checks if player direction is straight/idle, sets appropriate x and y position and updates direction
		if (playerDir == 0 || playerDir == 3)
		{
			xPos = playerX + (playerWidth / 2) - 11;
			yPos = playerY - playerHeight + 47;
			dir = 0;
		}
		// Checks if player direction is left, sets appropriate x and y position and updates direction
		else if (playerDir == 1)
		{
			xPos = playerX + (playerWidth / 2) - 11 - 26;
			yPos = playerY - playerHeight + 49;
			dir = 1;
		}
		// Checks if player direction is right, sets appropriate x and y position and updates direction
		else if (playerDir == 2)
		{
			xPos = playerX + (playerWidth / 2) - 11 + 26;
			yPos = playerY - playerHeight + 49;
			dir = 2;
		}
		// Update position of imageview
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
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
		return ivBullet;
	}
	
	// Method that moves bullet according to direction
	public void move()
	{
		// If bullet's direction is straight, move down
		if (dir == 0)
		{
			yPos -= 9;
		}
		// If bullet's direction is left, move up-left
		else if (dir == 1)
		{
			xPos -= 9;
			yPos -= 9;
		}
		// If bullet's direction is right, move up-right
		else
		{
			xPos += 9;
			yPos -= 9;
		}
		// Update x and y position of imageview
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
}
