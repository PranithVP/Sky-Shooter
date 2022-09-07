// Import necessary Java utilities and JavaFX components
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Main extends Application 
{
	// Declare global variables
	private Scene titleScreen, mapSelect, inGame, gameOver;
	private Pane root;
	private Random rnd;
	private Timeline airplaneMoveTimer, moveTimer, birdSpawnTimer, asteroidSpawnTimer, increaseSpeedTimer, powerupSpawnTimer, limitBulletTimer, lightningTimer;
	private int score, seconds, highScore, highestValueIndex, thirdSeconds;
	private double health, asteroidSpeedX, asteroidSpeedY, asteroidSpawnSpeed, aircraftSpeed;
	private boolean goUp, goDown, goLeft, goRight, space, noBirds, reachedFinalSpeed, limitBullets;
	private ArrayList<Bird> birds;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Star> stars;
	private ArrayList<Heart> hearts;
	private ArrayList<Fuel> fuels;
	private ArrayList<Lightning> lightnings;
	private ArrayList<Bullet> bullets;
	private Label lblScore;
	private Canvas canvas;
	private GraphicsContext gc;
	private Rectangle healthBar;
    private String highScoreName;
	private String userName = "";
	
	public static void main(String[] args) 
	{
		launch(args);
	}
    
	public void start(Stage primaryStage) 
	{
		titleScreen(primaryStage);
	}
	
	// Method that creates and sets the scene to the title screen
	public void titleScreen(Stage primaryStage)
	{
		// Declare and initialize pane, imageview and image for background
		Image imgTitleBackground = new Image("file:images\\titleScreenBackground.jpg");
		ImageView ivTitleBackground = new ImageView(imgTitleBackground);
		Pane titleRoot = new Pane();
		
		// Initialize titleScreen scene to use titleRoot with dimensions of scene to match background, 
		// add background to pane
		titleScreen = new Scene(titleRoot, imgTitleBackground.getWidth(), imgTitleBackground.getHeight());
		titleRoot.getChildren().add(ivTitleBackground);
		
		// Create blue play button with the text "PLAY GAME" that calls the method mapSelect when clicked
		Button btnPlay = new Button();
		btnPlay.setText("PLAY GAME");
		btnPlay.setFont(Font.font("Britannic", FontWeight.BOLD, FontPosture.ITALIC, 35));
		btnPlay.setPrefSize(350, 50);
		btnPlay.setTextFill(Color.DEEPSKYBLUE);
		btnPlay.setStyle("-fx-background-color: midnightblue");
		btnPlay.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID, null, null)));
		btnPlay.setAlignment(Pos.CENTER);
		btnPlay.setLayoutX(100);
		btnPlay.setLayoutY((titleScreen.getHeight() / 2) - 45);
		btnPlay.setOnAction(e -> mapSelect(primaryStage));
		
		// Create blue how-to-play button with the text "HOW TO PLAY" that creates an alert when clicked
		Button btnHowToPlay = new Button();
		btnHowToPlay.setText("HOW TO PLAY");
		btnHowToPlay.setFont(Font.font("Britannic", FontWeight.BOLD, FontPosture.ITALIC, 35));
		btnHowToPlay.setPrefSize(350, 50);
		btnHowToPlay.setStyle("-fx-background-color: midnightblue");
		btnHowToPlay.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID, null, null)));
		btnHowToPlay.setTextFill(Color.DEEPSKYBLUE);
		btnHowToPlay.setAlignment(Pos.CENTER);
		btnHowToPlay.setLayoutX(100);
		btnHowToPlay.setLayoutY((titleScreen.getHeight() / 2) + 50);
		btnHowToPlay.setOnAction(new EventHandler<>() 
		{
			public void handle(ActionEvent event) 
			{
				// Create an alert informing the user of the controls and how to play, show alert
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("HOW TO PLAY");
				alert.setContentText("Use WASD to move, and spacebar to shoot bullets. "
						+ "\n\nThe objective is to dodge and shoot obstacles for as long as possible. "
						+ "Shooting has a cooldown of 1.25 seconds, and extra points are added if "
						+ "an obstacle is shot rather than dodged. \n\nThe heart powerup adds 20% more "
						+ "health (unless the user is already full), the star powerup adds to the "
						+ "player's score, and the fuel power-up increases the aircraft's movement speed.");
				alert.setHeaderText(null);
				alert.setResizable(false);
				alert.showAndWait();
			}
		});
		
		// Create blue high score button with the text "HIGH SCORE" that calls alert when clicked
		Button btnHighScore = new Button();
		btnHighScore.setText("HIGH SCORE");
		btnHighScore.setFont(Font.font("Britannic", FontWeight.BOLD, FontPosture.ITALIC, 35));
		btnHighScore.setPrefSize(350, 50);
		btnHighScore.setStyle("-fx-background-color: midnightblue");
		btnHighScore.setBorder(new Border(new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID, null, null)));
		btnHighScore.setTextFill(Color.DEEPSKYBLUE);
		btnHighScore.setAlignment(Pos.CENTER);
		btnHighScore.setLayoutX(100);
		btnHighScore.setLayoutY((titleScreen.getHeight() / 2) + 145);
		btnHighScore.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event) 
			{
				try 
				{
					// Search for high score, display alert informing the user of the current highest score
					// and the name of the user that achieved it
					getHighScore();
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("HIGH SCORE");
					alert.setContentText("The current record is held by " + highScoreName 
										+ " with a score of " + highScore + "!");
					alert.setHeaderText(null);
					alert.setResizable(false);
					alert.showAndWait();
				} 
				catch (Exception e) 
				{
					// If an exception is thrown, create alert that informs the user that there are
					// no high scores available, show alert
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("HIGH SCORE");
					alert.setContentText("There are no scores recorded as of right now!");
					alert.setHeaderText(null);
					alert.setResizable(false);
					alert.showAndWait();
				}
			}
		});
		
		// Create label that contains image of aircraft, preserve ratio and resize
		Label lblAircraft = new Label();
		ImageView ivGraphic = new ImageView(new Image("file:images\\aircraft_graphic.png"));
		lblAircraft.setGraphic(ivGraphic);
		ivGraphic.setPreserveRatio(true);
		ivGraphic.setFitWidth(300);
		lblAircraft.setLayoutX(525);
		lblAircraft.setLayoutY((titleScreen.getHeight() / 2) - 80);
		
		// Create label that contains image of title, preserve ratio and resize
		Label lblTitle = new Label();
		ImageView ivTitleGraphic= new ImageView(new Image("file:images\\title.png"));
		lblTitle.setGraphic(ivTitleGraphic);
		ivTitleGraphic.setPreserveRatio(true);
		ivTitleGraphic.setFitWidth(650);
		lblTitle.setLayoutX(titleScreen.getWidth()/2 - 325);
		lblTitle.setLayoutY(50);
		
		// Create animation timer that calls method every frame
		AnimationTimer timer = new AnimationTimer()
		{
			public void handle(long now) 
			{
				// Change background/text colour to brighter colours when mouse hovers over play button
				btnPlay.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnPlay.setTextFill(Color.WHITE);
						btnPlay.setStyle("-fx-background-color: mediumblue");
					}
				});
				// Change background/text colour to brighter colours when mouse hovers over how-to-play button
				btnHowToPlay.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnHowToPlay.setTextFill(Color.WHITE);
						btnHowToPlay.setStyle("-fx-background-color: mediumblue");
					}
				});
				// Change background/text colour to brighter colours when mouse hovers over high score button
				btnHighScore.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnHighScore.setTextFill(Color.WHITE);
						btnHighScore.setStyle("-fx-background-color: mediumblue");
					}
				});
				// Change background/text colour back to dark colours when mouse exits play button
				btnPlay.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnPlay.setTextFill(Color.DEEPSKYBLUE);
						btnPlay.setStyle("-fx-background-color: midnightblue");
					}
				});
				// Change background/text colour back to dark colours when mouse exits how-to-play button
				btnHowToPlay.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnHowToPlay.setTextFill(Color.DEEPSKYBLUE);
						btnHowToPlay.setStyle("-fx-background-color: midnightblue");
					}
				});
				// Change background/text colour back to dark colours when mouse exits high score button
				btnHighScore.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnHighScore.setTextFill(Color.DEEPSKYBLUE);
						btnHighScore.setStyle("-fx-background-color: midnightblue");
					}
				});
			}
		};
		// Start animation timer "timer"
		timer.start();
		
		// Add all buttons and labels to pane
		titleRoot.getChildren().addAll(btnHowToPlay, btnPlay, btnHighScore, lblAircraft, lblTitle);

		// Call handle method when user closes program
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent e) 
			{
				// Display an alert confirming if user wants to exit
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Are you sure you want to exit?");
				alert.setHeaderText(null);
				alert.setTitle("Exit");
				Optional<ButtonType> result = alert.showAndWait();
				
				// If user cancels, don't exit
				if (result.get() == ButtonType.CANCEL)
				{
					e.consume();
				}
				// If user clicks ok, exit program
				else
				{
					Platform.exit();
					System.exit(0);
				}
			}
		});
		
		// Show primary stage, do not allow user to resize, center on screen, 
		// set stage's scene to title screen, set title
		primaryStage.centerOnScreen();
		primaryStage.setTitle("SKY SHOOTER");
		primaryStage.setScene(titleScreen);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	// Method that creates and set the scenes to the map selection screen
	public void mapSelect(Stage primaryStage)
	{
		// Declare and initialize pane, random object, imageview and image for background
		Image imgBack = new Image("file:images\\gameOverBackground.jpg");
		ImageView ivBack = new ImageView(imgBack);
		Random rnd = new Random();
		Pane mapRoot = new Pane();
		
		// Set scene to mapRoot pane, with dimensions of 400x500
		mapSelect = new Scene(mapRoot, 400, 500);
		mapRoot.getChildren().add(ivBack);
		
		// Create and initialize canvas and graphics context objects, set stroke line width to 5 pixels and colour to black
		Canvas canvas = new Canvas(400, 500);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(5);
		
		// Declare and initialize an array of Image objects that contains 3 different backgrounds
		Image[] maps = 
		{				
			new Image("file:images\\sky.png"), 
			new Image("file:images\\night.png"),
			new Image("file:images\\sunset.png")
		};
		
		// Create label for title image, center near the top of the screen
		Label lblMapSelect = new Label();
		lblMapSelect.setGraphic(new ImageView(new Image("file:images\\mapSelect.png")));
		lblMapSelect.setPrefSize(400, 125);
		lblMapSelect.setLayoutX(6);
		lblMapSelect.setLayoutY(0);
		lblMapSelect.setAlignment(Pos.CENTER);
		
		// Create label for sky image preview, position at top left, stroke outline on canvas
		Label lblSky = new Label();
		lblSky.setGraphic(new ImageView(new Image("file:images\\skyPreview.png")));
		lblSky.setPrefSize(150, 150);
		lblSky.setLayoutX(mapSelect.getWidth()/2 - 150 - 15);
		lblSky.setLayoutY(mapSelect.getHeight()/2 - 150 - 15 + 35);
		gc.strokeRect(mapSelect.getWidth()/2 - 150 - 15, mapSelect.getHeight()/2 - 150 - 15 + 35, 150, 150);
		
		// Create label for night image preview, position at top right, stroke outline on canvas
		Label lblNight = new Label();
		lblNight.setGraphic(new ImageView(new Image("file:images\\nightPreview.png")));
		lblNight.setPrefSize(150, 150);
		lblNight.setLayoutX(mapSelect.getWidth()/2 + 15);
		lblNight.setLayoutY(mapSelect.getHeight()/2 - 150 - 15 + 35);
		gc.strokeRect(mapSelect.getWidth()/2 + 15, mapSelect.getHeight()/2 - 150 - 15 + 35, 150, 150);
		
		// Create label for sunset image preview, position at bottom left, stroke outline on canvas
		Label lblSunset = new Label();
		lblSunset.setGraphic(new ImageView(new Image("file:images\\sunsetPreview.png")));
		lblSunset.setPrefSize(150, 150);
		lblSunset.setLayoutX(mapSelect.getWidth()/2 - 150 - 15);
		lblSunset.setLayoutY(mapSelect.getHeight()/2 + 15 + 35);
		gc.strokeRect(mapSelect.getWidth()/2 - 150 - 15, mapSelect.getHeight()/2 + 15 + 35, 150, 150);
		
		// Create label for random image preview, position at bottom right, stroke outline on canvas
		Label lblRandom = new Label();
		lblRandom.setGraphic(new ImageView(new Image("file:images\\randomPreview.png")));
		lblRandom.setPrefSize(150, 150);
		lblRandom.setLayoutX(mapSelect.getWidth()/2 + 15);
		lblRandom.setLayoutY(mapSelect.getHeight()/2 + 15 + 35);
		gc.strokeRect(mapSelect.getWidth()/2 + 15, mapSelect.getHeight()/2 + 15 + 35, 150, 150);
		
		// When user clicks each button, call inGame method with unique map accordingly
		lblSky.setOnMouseClicked(e -> enterName(primaryStage, maps[0]));
		lblNight.setOnMouseClicked(e -> enterName(primaryStage, maps[1]));
		lblSunset.setOnMouseClicked(e -> enterName(primaryStage, maps[2]));
		
		// When user clicks random button, return a random map
		lblRandom.setOnMouseClicked(e -> enterName(primaryStage, maps[rnd.nextInt(3)]));
		
		// Add canvas and labels to pane
		mapRoot.getChildren().addAll(canvas, lblSky, lblNight, lblSunset, lblRandom, lblMapSelect);
		
		// Create animation timer where handle method is called every frame
		AnimationTimer gui = new AnimationTimer()
		{
			public void handle(long now) 
			{				
				// Update label's graphic to new imageview of sky label to become lighter when mouse hovers over label
				lblSky.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblSky.setGraphic(new ImageView(new Image("file:images\\skyPreviewHover.png")));
					}
				});
				// Update label's graphic to new imageview of night label to become lighter when mouse hovers over label
				lblNight.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblNight.setGraphic(new ImageView(new Image("file:images\\nightPreviewHover.png")));
					}
				});
				// Update label's graphic to new imageview of sunset label to become lighter when mouse hovers over label
				lblSunset.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblSunset.setGraphic(new ImageView(new Image("file:images\\sunsetPreviewHover.png")));
					}
				});
				// Update label's graphic to new imageview of random label to become lighter when mouse hovers over label
				lblRandom.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblRandom.setGraphic(new ImageView(new Image("file:images\\randomPreviewHover.png")));
					}
				});
				
				// Update label's graphic to new imageview of sky label back to normal when mouse exits label
				lblSky.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblSky.setGraphic(new ImageView(new Image("file:images\\skyPreview.png")));
					}
				});
				// Update label's graphic to new imageview of night label back to normal when mouse exits label
				lblNight.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblNight.setGraphic(new ImageView(new Image("file:images\\nightPreview.png")));
					}
				});
				// Update label's graphic to new imageview of sunset label back to normal when mouse exits label
				lblSunset.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblSunset.setGraphic(new ImageView(new Image("file:images\\sunsetPreview.png")));
					}
				});
				// Update label's graphic to new imageview of random label back to normal when mouse exits label
				lblRandom.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						lblRandom.setGraphic(new ImageView(new Image("file:images\\randomPreview.png")));
					}
				});
			}
		};
		// Start animationtimer "gui"
		gui.start();
		
		// Set stage's scene to mapSelect, set title
		primaryStage.setScene(mapSelect);
		primaryStage.setTitle("SELECT MAP");
	}
	
	// Method that allows the user to enter their name
	public void enterName(Stage primaryStage, Image map)
	{	
		if (userName.equals(""))
		{
			// Create dialog that prompts user to enter their name
			TextInputDialog enterName = new TextInputDialog();
			enterName.setTitle("NAME");
			enterName.setContentText("Please enter your name:   ");
			enterName.setHeaderText(null);
			
			// Declare and initialize variables that check if input is correct
			boolean isNumber = true;
			boolean isBlank = true;
			
			// Show dialog, store result in Optional string object
			Optional<String> result = enterName.showAndWait();
			
			// Execute code if result exists
			if (result.isPresent())
			{
				// Loop through each index of user's input in the dialog
				for (int i = 0; i < enterName.getResult().length(); i++)
				{
					// Check if character at current index is a digit
					if (Character.isDigit(enterName.getResult().charAt(i)))
					{
						// Display alert that informs the user not to enter digits
						// set isNumber boolean to true, break out of loop
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText("Please do not enter digits!");
						alert.setHeaderText(null);
						alert.setTitle("ERROR!");
						alert.showAndWait();
						isNumber = true;
						break;
					}
					// if character at current index is not a digit
					else
					{
						// Set isNumber boolean to false
						isNumber = false;
					}
				}
				
				// Check if user's input in dialog is blank or null
				if (enterName.getResult().equals("") == true && enterName.getResult() != null)
				{
					// Display alert that informs the user to enter a name,
					// set isBlank boolean to true, break out of loop
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Please enter a name!");
					alert.setHeaderText(null);
					alert.setTitle("ERROR!");
					alert.showAndWait();
					isBlank = true;
				}
				// If input is not blank
				else
				{
					// Set isBlank boolean to false
					isBlank = false;
				}
			}
			
			// If the user's input is not a number and is not blank, execute code
			if (!isNumber && !isBlank)
			{
				// If the user has entered any input, execute code
				if (result.isPresent())
				{	
					userName = enterName.getResult();
					inGame(primaryStage, map, userName);
				}
			}
		}
		else
		{
			inGame(primaryStage, map, userName);
		}
	}

	// Method that creates, starts, and sets the scene to the game
	public void inGame(Stage primaryStage, Image map, String name)
	{	
		// Initialize variables to default values that allow the game to function
		health = 1;
		seconds = 0;
		space = false;
		noBirds = true;
		reachedFinalSpeed = false;
		limitBullets = false;
		
		// Initialize random object
		rnd = new Random();
		
		// Declare and initialize 2d array "scoreAmounts" containing different possible amounts for the				
		// star powerup to add to user's score
		int[][] scoreAmounts = 
		{ 	
			{300, 350, 400}, // SMALL SCORE BOOST
			{700, 750, 800}, // MEDIUM SCORE BOOST
			{1100, 1150, 1200} // LARGE SCORE BOOST
		};
		
		// Declare and initialize two image and imageview variables, set one image to the image from parameter
		// of the inGame method, and intialize other to the space background
		Image imgBack = map;
		Image imgBack2 = new Image("file:images\\space.png");
		ImageView ivBack = new ImageView(imgBack);
		ImageView ivBack2 = new ImageView(imgBack2);
		
		// Initialize pane root, add both backgrounds (first one in front), and initialize scene
		// to contain root with the dimensions of imgBack
		root = new Pane();
		root.getChildren().addAll(ivBack2, ivBack);
		inGame = new Scene(root, imgBack.getWidth(), imgBack.getHeight());
		
		// Create and instantiate aircraft object, add image of object to root
		Aircraft aircraft = new Aircraft();
		root.getChildren().add(aircraft.getImage());
		
		// Set score and speed starting values for aircraft and asteroids
		score = 0;
		aircraftSpeed = 0.055;
		asteroidSpeedX = 0.5;
		asteroidSpeedY = 1.25;
		
		// Initialize movement animation timer that calls handle method each frame
		// Start the AnimationTimer "movement"
		
		KeyFrame move = new KeyFrame(Duration.millis(17), new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent arg0) 
			{
				// Loop through each index in bullets arraylist
				for (int i = 0; i < bullets.size(); i++)
				{
					// Move bullet at current index, update imageview
					bullets.get(i).move();
					bullets.get(i).getImage();
				}
				
				// Loop through each index in birds arraylist
				for (int i = 0; i < birds.size(); i++)
				{
					// Move bird at current index
					birds.get(i).move();
					
					// Remove bird at current index if past bottom of the screen
					if (birds.get(i).getY() > inGame.getHeight())
					{
						root.getChildren().remove(birds.get(i).getImage());
						birds.remove(i);
					}
				}
				
				// Loop through each index in asteroids arraylist
				for (int i = 0; i < asteroids.size(); i++)
				{
					// Move asteroid at current index
					asteroids.get(i).move();

					// If user touches left wall, change x direction so asteroid bounces
					if (asteroids.get(i).getX() <= 0)
					{
						asteroids.get(i).changeXDir();
						asteroids.get(i).move();
					}
					// If user touches right wall, change x direction so asteroid bounces
					else if (asteroids.get(i).getX() + asteroids.get(i).getWidth() >= inGame.getWidth())
					{
						asteroids.get(i).changeXDir();
						asteroids.get(i).move();
					}
				}
				
				// Check if background is not already space
				if (space == false)
				{
					// If user's score passes 1500, stop spawning birds and change to space
					if (score >= 1500)
					{
						// Set spawn speed of asteroids, stop bird timers, and start increasing 
						// frequency of asteroid spawns
						asteroidSpawnSpeed = 1700;
						birdSpawnTimer.stop();
						increaseSpeedTimer.play();
						createAsteroidSpawnTimer(asteroidSpawnSpeed);
						
						// Set space to true and noBirds to false
						space = true;
						noBirds = false;
					}
				}
				else
				{
					for (int i = 0; i < asteroids.size(); i++)
					{
						// MOVE EVERY ASTEROID
						asteroids.get(i).move();
						// IF ASTEROID PASSES BOTTOM OF SCREEN, REMOVE
						if (asteroids.get(i).getY() > inGame.getHeight())
						{
							root.getChildren().remove(asteroids.get(i).getImage());
							asteroids.remove(i);
						}
					}
				}
				
				// Check if there are still birds on screen
				if (noBirds == false)
				{
					// If there are no birds on screen, transition backgrounds
					if (birds.size()-1 == 0)
					{
						// Create fade transition object with a 5s duration that goes from 
						// full opacity to none. Set node to background image, so image transitions
						// to space. Set noBirds boolean to true
						FadeTransition fade = new FadeTransition();
						fade.setDuration(Duration.millis(5000));
						fade.setFromValue(10);
						fade.setToValue(0);
						fade.setCycleCount(1);
						fade.setNode(ivBack);
						fade.play();
						noBirds = true;
					}
				}
				
				// Loop through each index of stars arraylist
				for(int i = 0; i < stars.size(); i++)
				{
					// Move star at current index, update imageview
					stars.get(i).move();
					stars.get(i).getImage();
					
					// If star passes bottom boundary, remove star object from arraylist and pane
					if (stars.get(i).getY() > inGame.getHeight())
					{
						root.getChildren().remove(stars.get(i).getImage());
						stars.remove(i);
					}
				}
				
				// Loop through each index of hearts arraylist
				for(int i = 0; i < hearts.size(); i++)
				{
					// Move heart at current index, update imageview
					hearts.get(i).move();
					hearts.get(i).getImage();
					
					// If heart passes bottom boundary, remove heart object from arraylist and pane
					if (hearts.get(i).getY() > inGame.getHeight())
					{
						root.getChildren().remove(hearts.get(i).getImage());
						hearts.remove(i);
					}
				}
				
				// Loop through each index of fuels arraylist
				for(int i = 0; i < fuels.size(); i++)
				{
					// Move fuel at current index, update imageview
					fuels.get(i).move();
					fuels.get(i).getImage();
					
					// If fuel passes bottom boundary, remove fuel object from arraylist and pane
					if (fuels.get(i).getY() > inGame.getHeight())
					{
						root.getChildren().remove(fuels.get(i).getImage());
						fuels.remove(i);
					}
				}

				// Loop through each index of lightnings arraylist
				for(int i = 0; i < lightnings.size(); i++)
				{
					// Move lightning at current index, update imageview
					lightnings.get(i).move();
					lightnings.get(i).getImage();
					
					// If lightning passes bottom boundary, remove fuel object from arraylist and pane
					if (lightnings.get(i).getY() > inGame.getHeight())
					{
						root.getChildren().remove(lightnings.get(i).getImage());
						lightnings.remove(i);
					}
				}
				
				// Loop through each index of bullets arraylist
				for (int i = 0; i < bullets.size(); i++)
				{
					// If fuel passes bottom boundary, remove fuel object from arraylist and pane
					if (bullets.get(i).getX() < -bullets.get(i).getWidth() || bullets.get(i).getY() < -bullets.get(i).getHeight() || bullets.get(i).getX() > inGame.getWidth())
					{
						root.getChildren().remove(bullets.get(i).getImage());
						bullets.remove(i);
					}
				}
				
				// Loop through each index of birds arraylist
				for (int i = 0; i < birds.size(); i++)
				{
					// Check intersection of aircraft image and bird image at current index
					if (aircraft.getImage().getBoundsInParent().intersects(birds.get(i).getImage().getBoundsInParent()))
					{
						// Remove bird from arraylist and pane, decrease health by 20%
						root.getChildren().remove(birds.get(i).getImage());
						birds.remove(i);
						health -= 0.2;
					}
				}
				
				// Loop through each index of birds arraylist
				for (int i = 0; i < birds.size(); i++)
				{
					// Loop through each index of bullets arraylist
					for (int j = 0; j < bullets.size(); j++)
					{
						// Check intersection of bird image at current index and bullet image at current index
						if (bullets.get(j).getImage().getBoundsInParent().intersects(birds.get(i).getImage().getBoundsInParent()))
						{
							// Remove bird and bullet from arraylist and pane, increase score by 100
							root.getChildren().remove(birds.get(i).getImage());
							birds.remove(i);
							root.getChildren().remove(bullets.get(j).getImage());
							bullets.remove(j);
							score += 100;
						}
					}
				}
				
				try
				{
					// Loop through each index of asteroids arraylist
					for (int i = 0; i < bullets.size(); i++)
					{
						// Loop through each index of bullets arraylist
						for (int j = 0; j < asteroids.size(); j++)
						{
							// Check intersection of asteroid image at current index and bullet image at current index
							if (bullets.get(i).getImage().getBoundsInParent().intersects(asteroids.get(j).getImage().getBoundsInParent()))
							{
								// Remove asteroid and bullet from arraylist and pane, increase score by 150
								root.getChildren().remove(asteroids.get(j).getImage());
								asteroids.remove(j);
								root.getChildren().remove(bullets.get(i).getImage());
								bullets.remove(i);
								score += 150;
							}
						}
					}
				}
				catch (IndexOutOfBoundsException e) {}
				
				// Loop through each index of asteroids arraylist
				for (int i = 0; i < asteroids.size(); i++)
				{
					// Check intersection of aircraft and asteroid image at current index 
					if (aircraft.getImage().getBoundsInParent().intersects(asteroids.get(i).getImage().getBoundsInParent()))
					{
						// Remove asteroid from arraylist and pane, decrease health by 20%
						root.getChildren().remove(asteroids.get(i).getImage());
						asteroids.remove(i);
						health -= 0.2;
					}
				}
				
				// Loop through each index of hearts arraylist
				for (int i = 0; i < hearts.size(); i++)
				{
					// Check intersection of aircraft and heart image at current index 
					if (aircraft.getImage().getBoundsInParent().intersects(hearts.get(i).getImage().getBoundsInParent()))
					{
						// Remove heart from arraylist and pane
						root.getChildren().remove(hearts.get(i).getImage());
						hearts.remove(i);
						
						// If health is below 80%, add 20%
						if (health <= 0.8)
						{
							health += 0.2;
						}
						// If health is over 80%, set to 100%
						else
						{
							health = 1;
						}
					}
				}
				
				// Loop through each index of stars arraylist
				for (int i = 0; i < stars.size(); i++)
				{
					// Check intersection of aircraft and star image at current index
					if (aircraft.getImage().getBoundsInParent().intersects(stars.get(i).getImage().getBoundsInParent()))
					{
						// Remove star from arraylist and pane
						root.getChildren().remove(stars.get(i).getImage());
						stars.remove(i);
						
						// Declare and initialize amount to add to score, randomize row and column
						int addToScore = 0;
						int row = rnd.nextInt(scoreAmounts.length);
						int col = rnd.nextInt(scoreAmounts[row].length);
						
						// If the first row is selected, add 100 plus the random amount of score
						if (row == 0)
						{
							addToScore = scoreAmounts[row][col] + 100;
						}
						// If the second row is selected, add 100 plus the random amount of score
						else if (row == 1)
						{
							addToScore = scoreAmounts[row][col] + 250;
						}
						// If the third row is selected, add 100 plus the random amount of score
						else
						{
							addToScore = scoreAmounts[row][col] + 500;
						}
						
						// Add addToScore to user's score
						score += addToScore;
					}
				}
				
				// Loop through each index of fuels arraylist
				for (int i = 0; i < fuels.size(); i++)
				{
					// Check intersection of aircraft and fuel image at current index
					if (aircraft.getImage().getBoundsInParent().intersects(fuels.get(i).getImage().getBoundsInParent()))
					{
						// Remove fuel from arraylist and pane
						root.getChildren().remove(fuels.get(i).getImage());
						fuels.remove(i);
						
						// Increase the aircraft's speed if the speed is below the limit
						if (aircraftSpeed < 0.95)
						{
							aircraftSpeed += 0.01;
						}
					}
				}

				// Loop through each index of lightnings arraylist
				for (int i = 0; i < lightnings.size(); i++)
				{
					// Check intersection of aircraft and fuel image at current index
					if (aircraft.getImage().getBoundsInParent().intersects(lightnings.get(i).getImage().getBoundsInParent()))
					{
						// Remove fuel from arraylist and pane
						root.getChildren().remove(lightnings.get(i).getImage());
						lightnings.remove(i);
						
						thirdSeconds = 0;
						KeyFrame kfLightning = new KeyFrame(Duration.millis(333), new EventHandler<ActionEvent>()
						{
							public void handle(ActionEvent arg0) 
							{
								thirdSeconds++;
								bullets.add(new Bullet());
								bullets.get(bullets.size()-1).setLocationAndDirection(aircraft.getX(), aircraft.getY(), aircraft.getWidth(), aircraft.getHeight(), aircraft.getDirection());
								root.getChildren().add(bullets.get(bullets.size()-1).getImage());

								if (thirdSeconds > 54)
								{
									lightningTimer.stop();
									thirdSeconds = 0;
								}
							}
						});
						limitBullets = true;
						limitBullets(18000);
						lightningTimer = new Timeline(kfLightning);
						lightningTimer.setCycleCount(Timeline.INDEFINITE);
						lightningTimer.play();
					}
				}
				
				// Constantly update the score, healthbar object's width according
				// to user's health and score label's text to user's score
				healthBar.setWidth(172*(health));
				score += 1;
				lblScore.setText(""+ score);
				
				// Check if user drops below 0 health
				if (health <= 0.01)
				{
					// Stop all timers
					moveTimer.stop();
					birdSpawnTimer.stop();
					increaseSpeedTimer.stop();
					powerupSpawnTimer.stop();
					if (space == true)
					{
						asteroidSpawnTimer.stop();
					}
					
					// Call gameOver method for user's name and score
					aircraft.idle();
					goUp = false;
					goDown = false;
					goLeft = false;
					goRight = false;
					gameOver(primaryStage, userName, score);
				}
			}
		});
		moveTimer = new Timeline(move);
		moveTimer.setCycleCount(Timeline.INDEFINITE);
		moveTimer.play();

		KeyFrame airplaneMove = new KeyFrame(Duration.millis(0.1), new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent arg0) 
			{
				// Check for key pressed events
				inGame.setOnKeyPressed(new EventHandler<KeyEvent>() 
				{
					// Called when key event occurs
					public void handle(KeyEvent e) 
					{
						// Check if the user presses right, goRight boolean to true
						if (e.getCode() == KeyCode.D)
						{
							goRight = true;
						}
						// Check if the user presses left, goLeft boolean to true
						if (e.getCode() == KeyCode.A)
						{
							goLeft = true;
						}
						// Check if the user presses up, goUp boolean to true
						if (e.getCode() == KeyCode.W)
						{
							goUp = true;
						}
						// Check if the user presses down, goDown boolean to true
						if (e.getCode() == KeyCode.S)
						{
							goDown = true;
						}
						// Check if user presses space
						if (e.getCode() == KeyCode.SPACE)
						{
							// Check if bullet cooldown is not active
							if (limitBullets == false)
							{
								// Add new bullet to arraylist, set location and direction, add to root,
								// set cooldown "limitBullets" to true, call limitBullets method
								bullets.add(new Bullet());
								bullets.get(bullets.size()-1).setLocationAndDirection(aircraft.getX(), aircraft.getY(), aircraft.getWidth(), aircraft.getHeight(), aircraft.getDirection());
								root.getChildren().add(bullets.get(bullets.size()-1).getImage());
								limitBullets = true;
								limitBullets(1250);
							}
						}
					}
				});
				
				// Check for key released events
				inGame.setOnKeyReleased(new EventHandler<KeyEvent>() 
				{
					// Called when key event occurs
					public void handle(KeyEvent e) 
					{
						// Check if the user releases right, set the goRight boolean to false
						if (e.getCode() == KeyCode.D)
						{
							goRight = false;
							aircraft.idle();
						}
						// Check if the user releases left, set the goLeft boolean to false
						if (e.getCode() == KeyCode.A)
						{
							goLeft = false;
							aircraft.idle();
						}
						// Check if the user releases up, set the goUp boolean to false
						if (e.getCode() == KeyCode.W)
						{
							goUp = false;
							aircraft.idle();
						}
						// Check if the user releases down, set the goDown boolean to false
						if (e.getCode() == KeyCode.S)
						{
							goDown = false;
							aircraft.idle();
						}
					}
				});
				
				// If the goUp boolean is true, move up
				if (goUp == true)
				{
					aircraft.moveUp(aircraftSpeed);
					
					// Check top boundary, do not allow user to pass
					if (aircraft.getY() < 0)
					{
						aircraft.setY(0);
					}
				}
				// If the goDown boolean is true, move down
				if (goDown == true)
				{
					aircraft.moveDown(aircraftSpeed);
					
					// Check bottom boundary, do not allow user to pass
					if (aircraft.getY() + aircraft.getHeight() > inGame.getHeight())
					{
						aircraft.setY(inGame.getHeight() - aircraft.getHeight());
					}
				}
				// If the goLeft boolean is true, move left
				if (goLeft == true)
				{
					aircraft.moveLeft(aircraftSpeed);
					
					// Check left boundary, do not allow user to pass
					if (aircraft.getX() < 0)
					{
						aircraft.setX(0);
					}
				}
				// If the goRight boolean is true, move right
				if (goRight == true)
				{
					aircraft.moveRight(aircraftSpeed);
					
					// Check right boundary, do not allow user to pass
					if (aircraft.getX() + aircraft.getWidth() > inGame.getWidth())
					{
						aircraft.setX(inGame.getWidth() - aircraft.getWidth());
					}
				}
				// Update aircraft imageview
				aircraft.getImage();
			}
		});
		airplaneMoveTimer = new Timeline(airplaneMove);
		airplaneMoveTimer.setCycleCount(Timeline.INDEFINITE);
		airplaneMoveTimer.play();

		// Initialize canvas and graphics context to scene's width and height
		canvas = new Canvas(inGame.getWidth(), inGame.getHeight());
		gc = canvas.getGraphicsContext2D();
		
		// Create a label that informs the user of their score that is black with white text
		lblScore = new Label();
		lblScore.setText("" + score);
		lblScore.setFont(Font.font("Brittanic", FontWeight.BOLD, 25));
		lblScore.setTextFill(Color.WHITE);
		lblScore.setLayoutX(10);
		lblScore.setLayoutY(10);
		lblScore.setAlignment(Pos.CENTER);
		lblScore.setPrefSize(125, 40);
		lblScore.setStyle("-fx-background-color: BLACK");
		
		// Stroke a rounded rectangle outline in the canvas around the score label
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3);
		gc.strokeRoundRect(10, 10, 125, 40, 10, 10);
		
		// Add label to pane
		root.getChildren().add(lblScore);
		
		// Initialize Rectangle object for health bar
		healthBar = new Rectangle(172, 28, Color.RED);
		healthBar.setLayoutX(inGame.getWidth() - 188 - 3);
		healthBar.setLayoutY(15);
		
		// Stroke a rounded rectangle outline in the canvas around healthbar object
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);
		gc.strokeRoundRect(inGame.getWidth() - 194, 15, 176, 28, 10, 10);
		
		// Add healthbar and canvas to pane
		root.getChildren().addAll(healthBar, canvas);
		
		// Create heart image, use for imageview
		Image imgHeart = new Image("file:images\\heart.png");
		ImageView ivHeart = new ImageView(imgHeart);
		
		// Set dimensions and location of heart, add to pane
		ivHeart.setFitHeight(51);
		ivHeart.setX(inGame.getWidth() - 188 - 36);
		ivHeart.setY(5);
		root.getChildren().add(ivHeart);
		
		// Initialize arraylist of each object
		stars = new ArrayList<Star>();
		hearts = new ArrayList<Heart>();
		fuels = new ArrayList<Fuel>();
		lightnings = new ArrayList<Lightning>();
		birds = new ArrayList<Bird>();
		bullets = new ArrayList<Bullet>();
		asteroids = new ArrayList<Asteroid>();
		
		// Create a keyframe that spawns a bird every 600 ms
		KeyFrame kfBird = new KeyFrame(Duration.millis(600), new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event) 
			{
				// Add a new bird to the birds arraylist, set the location, add image to pane
				birds.add(new Bird());
				birds.get(birds.size()-1).setLocation((int)inGame.getWidth(), (int)inGame.getHeight());
				root.getChildren().add(birds.get(birds.size()-1).getImage());
			}	
		});
		// Initialize timeline that uses kfBird keyframe, play indefinitely
		birdSpawnTimer = new Timeline(kfBird);
		birdSpawnTimer.setCycleCount(Timeline.INDEFINITE);
		birdSpawnTimer.play();
		
		// Create a keyframe that increases asteroid spawn frequency with the duration of 1 seconds 
		KeyFrame kfIncreaseSpeed = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event) 
			{	
				// Add to seconds variable
				seconds++;
				
				// On the third second, execute code
				if (seconds == 3)
				{
					// If this is the and the final speed has not been reached, increase x speed
					if (!reachedFinalSpeed)
					{
						if (asteroidSpeedX <= 2.5)
						{
							asteroidSpeedX += 0.15;
						}
						// If x speed of asteroid is above 2.5, set reachedFinalSpeed to true
						else
						{
							reachedFinalSpeed = true;
						}
					}
					
					// Increase y speed by 0.2 if below 3.5
					if (asteroidSpeedY <= 3.5)
					{
						asteroidSpeedY += 0.15;
					}
				}
				
				// On the sixth second, execute code
				if (seconds >= 6)
				{
					// Reset seconds variable
					seconds = 0;
					
					// If this is the and the final speed has not been reached, increase x speed
					if (!reachedFinalSpeed)
					{
						if (asteroidSpeedX <= 2.5)
						{
							asteroidSpeedX += 0.15;
						}
						// If x speed of asteroid is above 2.5, set reachedFinalSpeed to true
						else
						{
							reachedFinalSpeed = true;
						}
					}
					// Increase y speed by 0.2 if below 3.5
					if (asteroidSpeedY <= 3.5)
					{
						asteroidSpeedY += 0.15;
					}
					
					// Decrease asteroid spawn frequency if above 50 ms
					if (asteroidSpawnSpeed > 600)
					{
						// Stop the asteroid spawn timer, decrease delay, call method to create new timer with 
						// the new delay
						asteroidSpawnTimer.pause();
						asteroidSpawnTimer.stop();
						asteroidSpawnSpeed -= 60;
						createAsteroidSpawnTimer(asteroidSpawnSpeed);
					}
				}
			}	
		});
		// Initialize timeline increaseSpeedTimer, set cycle count to indefinite
		increaseSpeedTimer = new Timeline(kfIncreaseSpeed);
		increaseSpeedTimer.setCycleCount(Timeline.INDEFINITE);
		
		// Create a keyframe that spawns powerups with the duration of 20 seconds 
		KeyFrame kfSpawnPowerup = new KeyFrame(Duration.seconds(20), new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event) 
			{
				// Choose random integer between 1 to 3
				int spawnPowerup = rnd.nextInt(7) + 1;
				
				// If random number is 1, spawn star powerup
				if (spawnPowerup == 1)
				{
					// Add star to arraylist and pane, set location of star
					stars.add(new Star());
					stars.get(stars.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(stars.get(stars.size()-1).getImage());
				}
				// If random number is 2, spawn heart powerup
				else if (spawnPowerup == 2)
				{
					// Add fuel to arraylist and pane, set location of fuel
					hearts.add(new Heart());
					hearts.get(hearts.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(hearts.get(hearts.size()-1).getImage());
				}
				// If random number is 3, spawn fuel powerup
				else if (spawnPowerup == 3)
				{
					// Add fuel to arraylist and pane, set location of fuel
					fuels.add(new Fuel());
					fuels.get(fuels.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(fuels.get(fuels.size()-1).getImage());
				}
				else if (spawnPowerup == 4)
				{
					lightnings.add(new Lightning());
					lightnings.get(lightnings.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(lightnings.get(lightnings.size()-1).getImage());
				}
				else if (spawnPowerup == 5)
				{
					lightnings.add(new Lightning());
					lightnings.get(lightnings.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(lightnings.get(lightnings.size()-1).getImage());
				}
				else if (spawnPowerup == 6)
				{
					lightnings.add(new Lightning());
					lightnings.get(lightnings.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(lightnings.get(lightnings.size()-1).getImage());
				}
				else if (spawnPowerup == 7)
				{
					lightnings.add(new Lightning());
					lightnings.get(lightnings.size()-1).setLocation(inGame.getWidth(), inGame.getHeight());
					root.getChildren().add(lightnings.get(lightnings.size()-1).getImage());
				}
			}
		});
		// Initialize timeline using kfSpawnPowerup keyframe, set cycle count to indefinite, play timer
		powerupSpawnTimer = new Timeline(kfSpawnPowerup);
		powerupSpawnTimer.setCycleCount(Timeline.INDEFINITE);
		powerupSpawnTimer.play();
		
		// Set stage's scene to inGame, clear title
		primaryStage.setScene(inGame);
		primaryStage.setTitle("");
	}
	
	// Method that creates and sets the scene to the game over screen
	public void gameOver(Stage primaryStage, String name, int score)
	{		
		// Declare and initialize pane, image and imageview for background
		Image imgBack = new Image("file:images\\gameOverBackground.jpg");
		ImageView ivBack = new ImageView(imgBack);
		Pane endRoot = new Pane();
		
		// Initialize scene using endRoot with dimentions matching background, add background to pane
		gameOver = new Scene(endRoot, imgBack.getWidth(), imgBack.getHeight());
		endRoot.getChildren().add(ivBack);
		
		// Try to add score to HighScores.txt file
		try 
		{
			// Call addScore method with parameters of user's name and score
			addScore(name, score);
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
		// Try to recieve high score from HighScores.txt file
		try 
		{
			// Call getHighScore method
			getHighScore();
		} 
		catch (Exception e2) 
		{
			e2.printStackTrace();
		}
		
		// Declare and initialize label using graphic of new imageview of new image, add to pane
		Image imgGameOver = new Image("file:images\\gameOver.png");
		ImageView ivGameOver = new ImageView(imgGameOver);
		Label lblGameOver = new Label();
		lblGameOver.setPrefWidth(imgBack.getWidth());;
		lblGameOver.setPrefHeight(150);
		lblGameOver.setLayoutY(20);
		lblGameOver.setAlignment(Pos.CENTER);
		lblGameOver.setGraphic(ivGameOver);
		endRoot.getChildren().add(lblGameOver);
		
		// Declare and initialize new label for the user's score, add to pane
		Label lblScore = new Label();
		lblScore.setPrefWidth(600);
		lblScore.setPrefHeight(50);
		lblScore.setText("YOUR SCORE: " + score);
		lblScore.setFont(Font.font("Brittanic", FontWeight.BOLD, FontPosture.ITALIC, 45));
		lblScore.setAlignment(Pos.CENTER);
		lblScore.setTextFill(Color.BLACK);
		lblScore.setLayoutX(gameOver.getWidth()/2 - 300);
		lblScore.setLayoutY(gameOver.getHeight()/2 - 72);
		endRoot.getChildren().add(lblScore);
		
		// Declare and initialize new label for the highest score, add to pane
		Label lblHighScore = new Label();
		lblHighScore.setPrefWidth(600);
		lblHighScore.setPrefHeight(50);
		lblHighScore.setText("HIGH SCORE: " + highScore);
		lblHighScore.setFont(Font.font("Brittanic", FontWeight.BOLD, FontPosture.ITALIC, 30));
		lblHighScore.setAlignment(Pos.CENTER);
		lblHighScore.setTextFill(Color.BLACK);
		lblHighScore.setLayoutX(gameOver.getWidth()/2 - 300);
		lblHighScore.setLayoutY(gameOver.getHeight()/2 - 12);
		endRoot.getChildren().add(lblHighScore);
		
		// Declare and initialize new label for the username attatched to the highest score, add to pane
		Label lblHighScorePlayer = new Label();
		lblHighScorePlayer.setPrefWidth(600);
		lblHighScorePlayer.setPrefHeight(50);
		lblHighScorePlayer.setText("(" + highScoreName.toUpperCase() + ")");
		lblHighScorePlayer.setFont(Font.font("Brittanic", FontWeight.BOLD, FontPosture.ITALIC, 30));
		lblHighScorePlayer.setAlignment(Pos.CENTER);
		lblHighScorePlayer.setTextFill(Color.BLACK);
		lblHighScorePlayer.setLayoutX(gameOver.getWidth()/2 - 300);
		lblHighScorePlayer.setLayoutY(gameOver.getHeight()/2 + 23);
		endRoot.getChildren().add(lblHighScorePlayer);
		
		// Declare and initialize play again button with black text and a white background to play again
		// When clicked, call the titleScreen method
		Button btnPlayAgain = new Button();
		btnPlayAgain.setText("PLAY AGAIN");
		btnPlayAgain.setFont(Font.font("Britannic", FontWeight.BOLD, FontPosture.ITALIC, 28));
		btnPlayAgain.setPrefSize(250, 50);
		btnPlayAgain.setTextFill(Color.BLACK);
		btnPlayAgain.setStyle("-fx-background-color: white");
		btnPlayAgain.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
		btnPlayAgain.setAlignment(Pos.CENTER);
		btnPlayAgain.setLayoutX(gameOver.getWidth()/2 - 10 - 250);
		btnPlayAgain.setLayoutY(gameOver.getHeight() - 120);
		btnPlayAgain.setOnAction(e -> titleScreen(primaryStage));
		endRoot.getChildren().add(btnPlayAgain);
		
		// Declare and initialize exit game button with black text and a white background to exit game
		// When clicked, call the exitAlert method
		Button btnExit = new Button();
		btnExit.setText("EXIT GAME");
		btnExit.setFont(Font.font("Britannic", FontWeight.BOLD, FontPosture.ITALIC, 28));
		btnExit.setPrefSize(250, 50);
		btnExit.setTextFill(Color.BLACK);
		btnExit.setStyle("-fx-background-color: white");
		btnExit.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
		btnExit.setAlignment(Pos.CENTER);
		btnExit.setLayoutX(gameOver.getWidth()/2 + 10);
		btnExit.setLayoutY(gameOver.getHeight() - 120);
		btnExit.setOnAction(e -> exitAlert());
		endRoot.getChildren().add(btnExit);
		
		AnimationTimer timer = new AnimationTimer()
		{
			public void handle(long now) 
			{
				// Change play again button's background to light gray when mouse hovers label
				btnPlayAgain.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnPlayAgain.setStyle("-fx-background-color: lightgray");
					}
				});
				// Change exit game button's background to light gray when mouse hovers label
				btnExit.setOnMouseEntered(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnExit.setStyle("-fx-background-color: lightgray");
					}
				});
				// Change play again button's background back to white when mouse exits label
				btnPlayAgain.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnPlayAgain.setStyle("-fx-background-color: white");
					}
				});
				// Change exit game button's background back to white when mouse exits label
				btnExit.setOnMouseExited(new EventHandler<>()
				{
					public void handle(MouseEvent event) 
					{
						btnExit.setStyle("-fx-background-color: white");
					}
				});
			}
		};
		// Start the timer
		timer.start();
		
		// Set the stage's scene to gameOver, set title
		primaryStage.setScene(gameOver);
		primaryStage.setTitle("GAME OVER");
	}
	
	// Method that creates a timer for asteroid spawning
	public void createAsteroidSpawnTimer(double milli)
	{
		// Create new keyframe object that spawns asteroids with the duration of the parameter
		asteroidSpawnSpeed = milli;
		KeyFrame kfAsteroid = new KeyFrame(Duration.millis(asteroidSpawnSpeed), new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event) 
			{
				// Add asteroid object to the arraylist, set the location of the asteroid, add the image to arraylist
				asteroids.add(new Asteroid(asteroidSpeedX + ((rnd.nextInt(5) + 1.5)/2), asteroidSpeedY));
				asteroids.get(asteroids.size()-1).setLocation((int)inGame.getWidth(), (int)inGame.getHeight());
				root.getChildren().add(asteroids.get(asteroids.size()-1).getImage());
			}	
		});
		// Initialize asteroidSpawnTimer to new timer and play indefinitely
		asteroidSpawnTimer = new Timeline(kfAsteroid);
		asteroidSpawnTimer.setCycleCount(Timeline.INDEFINITE);
		asteroidSpawnTimer.play();
	}
	
	// Method that creates a timer for the cooldown of shooting bullets
	public void limitBullets(double milli)
	{
		// Create new keyframe object with a 1250 ms delay that limits bullets
		KeyFrame kfLimitBullets = new KeyFrame(Duration.millis(milli), new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event) 
			{
				// Sets limitBullets variable to false, so bullet cooldown ends every 1.25 seconds after the bullet is shot
				limitBullets = false;
			}
		});
		// Initialize timeline limitBulletTimer for new keyframe, play once
		limitBulletTimer = new Timeline(kfLimitBullets);
		limitBulletTimer.setCycleCount(1);
		limitBulletTimer.play();
	}
	
	// Method that reads a document and checks for the highest score
	// and the name associated with that score
	public void getHighScore() throws Exception
	{
        try 
        {
        	// Initialize bufferedreader object to read HighScores.txt document
            BufferedReader readFile = new BufferedReader(new FileReader("HighScores.txt"));
            
            // Initialize lineOfText string and scores/names arraylists 
            // (scores is integer arraylist, names is string arraylist)
            String lineOfText;
            ArrayList<Integer> scores = new ArrayList<Integer>();
            ArrayList<String> names = new ArrayList<String>();
            
            // Check if line has characters in it
            while ((lineOfText = readFile.readLine()) != null)
            {
            	// Try parsing current line to integer and adding to score arraylist
				try 
				{
					scores.add(Integer.parseInt(lineOfText));
				} 
				// If line does not have a number, add to names arraylist
				catch (NumberFormatException e) 
				{
					names.add(lineOfText);
				}
            }
            // Close the buffered reader
            readFile.close();
            
            // Find maximum values by using method from the collections class to search arraylist
            highScore = Collections.max(scores);
            
            // Search index of highest value, initialize name at that index in the names arraylist
            highestValueIndex = linearSearch(scores, highScore);
            highScoreName = names.get(highestValueIndex);
        }
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method that adds a name and a score to a document
	public void addScore(String name, int score) throws Exception
	{
		// Declare and initialize buffered writer object to write to HighScores.txt file, append to end of document
		BufferedWriter writeFile = new BufferedWriter(new FileWriter("HighScores.txt", true));
		
		// Write the user's name (taken from parameter) to the current line, add new line
		writeFile.write(name);
		writeFile.newLine();
		
		// Write the user's score (taken from parameter) to the current line, add new line
		writeFile.write(Integer.toString(score));
		writeFile.newLine();
		
		// Close the buffered writer
		writeFile.close();
	}
	
	// Method that prompts the user if they would like to exit the program
	public void exitAlert()
	{
		// Create an alert confirming if the user wants to exit
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to exit?");
		alert.setHeaderText(null);
		alert.setTitle("EXIT");
		alert.setResizable(false);
		
		// Store the result in an optional buttontype object named "result", show alert
		Optional<ButtonType> result = alert.showAndWait();
		
		// If the user clicks ok, exit the program
		if (result.get() == ButtonType.OK)
		{
			Platform.exit();
			System.exit(0);			
		}
		// If the user does not click ok, return from this method
		else
		{
			return;
		}
	}
	
	// Method that searches an arraylist of integers for a value and 
	// returns the index of that value (returns -1 if no value is found)
	public int linearSearch(ArrayList<Integer> data, int value)
	{
		// Declare and initialize default value for the index of the search value
		int indexOfValue = -1;
		
		// Loop through each index in the arraylist
		for (int i = 0; i < data.size(); i++)
		{
			// If integer at current index matches search value, initialize indexOfValue to current index
			if (data.get(i) == value)
			{
				indexOfValue = i;
			}
		}
		// Return index of the search value
		return indexOfValue;
	}
}