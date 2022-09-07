import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fuel 
{
	// Declare and initialize global variables
	private double xPos, yPos, width, height;
	private Image imgFuel;
	private ImageView ivFuel;
	private Random rnd;
	
	// Constructor for fuel class
	Fuel()
	{
		// Initialize random object
		rnd = new Random();
		
		// Initialize image and imageview of fuel, use image for imageview
		imgFuel = new Image("file:images\\fuel.png");
		ivFuel = new ImageView(imgFuel);
		
		// Assign default value for x and y position
		xPos = 0;
		yPos = 0;
		
		// Set width and height of fuel
		width = imgFuel.getWidth();
		height = imgFuel.getHeight();
		
		// Set x and y position of imageview
		ivFuel.setX(xPos);
		ivFuel.setY(yPos);
	}
	
	// Method that sets x position
	public void setX(double x)
	{
		// Set x position, update x position of imageview
		xPos = x;
		ivFuel.setX(xPos);
	}
	
	// Method that sets y position
	public void setY(double y)
	{
		// Set y position, update y position of imageview
		yPos = y;
		ivFuel.setY(yPos);
	}
	
	// Method that sets location
	public void setLocation(double frameWidth, double frameHeight)
	{
		// Set x position to random location across x axis, y position on top of screen
		xPos = (double)rnd.nextInt((int)frameWidth - (int)width);
		yPos = 0 - height;
		
		// Update x and y position of imageview
		ivFuel.setX(xPos);
		ivFuel.setY(yPos);
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
		return ivFuel;
	}
	
	// Method that moves fuel down
	public void move()
	{
		// Set y position, update y position of image view
		yPos += 3;
		ivFuel.setY(yPos);
	}
}
