import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Aircraft class is the aircraft object that the game revolves around:
// the player controls the aircraft (which interacts with other objects)
public class Aircraft 
{
	// Declare global variables
	private double xPos, yPos, width, height;
	private int dir;
	private Image imgStraight, imgLeftBoost, imgRightBoost, imgStraightBoost;
	private ImageView ivAircraft;
	final static int STRAIGHT = 0, LEFT = 1, RIGHT = 2, IDLE = 3;
	
	// Constructor for Aircraft class
	Aircraft()
	{
		// Initialize all images
		imgStraight = new Image("file:images\\straight.png");
		imgLeftBoost = new Image("file:images\\boost_left.png");
		imgRightBoost = new Image("file:images\\boost_right.png");
		imgStraightBoost = new Image("file:images\\boost_straight.png");
		
		// Set default values for direction integer and dead boolean
		dir = IDLE;
		
		// Initialize imageview to defualt direction, straight
		ivAircraft = new ImageView(imgStraight);
		
		// Initialize width and height of aircraft
		width = ivAircraft.getImage().getWidth();
		height = ivAircraft.getImage().getHeight();
		
		// Initialize x and y position to spawn near the center bottom
		xPos = 474/2 - width;
		yPos = 500;

		// Set x/y position of imageview
		ivAircraft.setX(xPos);
		ivAircraft.setY(yPos);
	}
	
	// Method that changes x position
	public void setX(double x)
	{
		// Update x position and imageview's x position
		xPos = x;
		ivAircraft.setX(xPos);
	}
	
	// Method that changes y position
	public void setY(double y)
	{
		// Update y position and imageview's y position
		yPos = y;
		ivAircraft.setY(yPos);
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
	
	// Method that moves the aircraft up
	public void moveUp(double speed)
	{
		// Decrease y position, update imageview, update direction
		yPos -= speed;
		ivAircraft.setY(yPos);
		dir = STRAIGHT;
	}
	
	// Method that moves the aircraft down
	public void moveDown(double speed)
	{
		// Increase y position, update imageview, update direction
		yPos += speed;
		ivAircraft.setY(yPos);
		dir = IDLE;
	}
	
	// Method that moves the aircraft left
	public void moveLeft(double speed)
	{
		// Decrease x position, update imageview, update direction
		xPos -= speed;
		ivAircraft.setX(xPos);
		dir = LEFT;
	}
	
	// Method that moves the aircraft left
	public void moveRight(double speed)
	{
		// Increase x position, update imageview, update direction
		xPos += speed;
		ivAircraft.setX(xPos);
		dir = RIGHT;
	}
	
	// Method that changes direction to idle
	public void idle()
	{
		dir = IDLE;
	}
	
	// Method that updates imageview according to direction
	public ImageView getImage()
	{
		// Check if direction is left, set to appropriate image
		if (dir == LEFT)
		{
			ivAircraft.setImage(imgLeftBoost);
		}
		// Check if direction is right, set to appropriate image
		else if (dir == RIGHT)
		{
			ivAircraft.setImage(imgRightBoost);
		}
		// Check if direction is straight, set to appropriate image
		else if (dir == STRAIGHT)
		{
			ivAircraft.setImage(imgStraightBoost);
		}
		// Check if direction is idle, set to appropriate image
		else if (dir == IDLE)
		{
			ivAircraft.setImage(imgStraight);
		}
		// Return updated imageview
		return ivAircraft;
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
	
	// Method that returns direction
	public int getDirection() 
	{
		return dir;
	}
}
