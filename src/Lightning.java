import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Lightning 
{
	// Declare and initialize global variables
	private double xPos, yPos, width, height;
	private Image imgLightning;
	private ImageView ivLightning;
	private Random rnd;
	
	// Constructor for lightning class
	Lightning()
	{
		// Initialize random object
		rnd = new Random();
		
		// Initialize image and imageview of lightning, use image for imageview
		imgLightning = new Image("file:images\\lightning.png");
		ivLightning = new ImageView(imgLightning);
		
		// Assign default value for x and y position
		xPos = 0;
		yPos = 0;
		
		// Set width and height of lightning
		width = imgLightning.getWidth();
		height = imgLightning.getHeight();
		
		// Set x and y position of imageview
		ivLightning.setX(xPos);
		ivLightning.setY(yPos);
	}
	
	// Method that sets x position
	public void setX(double x)
	{
		// Set x position, update x position of imageview
		xPos = x;
		ivLightning.setX(xPos);
	}
	
	// Method that sets y position
	public void setY(double y)
	{
		// Set y position, update y position of imageview
		yPos = y;
		ivLightning.setY(yPos);
	}
	
	// Method that sets location
	public void setLocation(double frameWidth, double frameHeight)
	{
		// Set x position to random location across x axis, y position on top of screen
		xPos = (double)rnd.nextInt((int)frameWidth - (int)width);
		yPos = 0 - height;
		
		// Update x and y position of imageview
		ivLightning.setX(xPos);
		ivLightning.setY(yPos);
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
		return ivLightning;
	}
	
	// Method that moves lightning down
	public void move()
	{
		// Set y position, update y position of image view
		yPos += 3;
		ivLightning.setY(yPos);
	}
}
