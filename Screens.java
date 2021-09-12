import greenfoot.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import java.io.FileWriter;
import java.io.FileReader;
import java.awt.FileDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.google.common.base.Splitter;

/**
 * The Screens class handles: 
   * All screens and the consequent initialisation
   * The updating of screen elements 
   * The calculating of scores
   * The saving of mazes
   * The updating of the scores list
   * The reading of files
 * 
 * By WS
 * Version: 22/05/2021
 */
public class Screens extends World
{
    private MazeGenerator mazeGenerator; // Stores the reference to the MazeGenerator object
    private MazeSolver mazeSolver; // Stores the reference to the MazeSolver object
    private Player player; // Stores the reference to the Player object
    private DisplayText timerText; // Stores the reference to the Timer object
    public String screenType; // Stores the current screen type of the world
    private String mazeDifficulty; // Stores the difficulty of the maze
    private int[][] mazeLayout; // Stores the layout of the maze
    private static ArrayList<Integer> playerPathTakenRows = new ArrayList<Integer>(); // Stores the row indexes of the mazeLayout array for the cells of the path the player has taken
    private static ArrayList<Integer> playerPathTakenCols = new ArrayList<Integer>();  // Stores the column indexes of the mazeLayout array for the cells of the path the player has taken
    public boolean completed = false; // Stores if the player has completed the maze
    private double accuracyScore; // Stores the accuracy score for a given maze
    private double pathAccuracy; // Stores the path accuracy for a given maze
    private double timeAccuracy; // Stores the time accuracy for a given maze
    private int playerTimer; // Stores the time taken for the player to complete a given maze
    private int scoresListSize; // Stores the size of a scores list
    private int scoresListPageNumber = 1; // Stores the current page number of a scores list being displayed
    private boolean scoresListRequiresSorting; // Stores if a given scores list requires sorting

    /** Constructor for the default initialisation of the Screen object (Start Screen) **/
    public Screens() 
    {
        super(900, 600, 1); // Create a new world with 900x600 cells with a cell size of 1x1 pixels.
        Greenfoot.start(); // Run the scenario automatically
        setBackground("startScreen.png");
        addObject(new Buttons("start"), 450, 427); /** is just having the comment here fine bc then from here on out ppl know what addobject is doing**/ // Add start button to the world at x coordinate 450, y coordinate 427 
    }

    /** Constructor for the initialisation of all other Screens objects, except Screens for viewing or playing saved mazes. **/
    public Screens(String screenType)
    {    
        super(900, 600, 1); // Create a new world with 900x600 cells with a cell size of 1x1 pixels.
        this.screenType = screenType; // Store the given screen type 
        switch(screenType) // Initialise the given screen type
        {
            case "start": // Start screen
                setBackground("startScreen.png");
                addObject(new Buttons("start"), 450, 427);
                break;
            case "mainMenu": // Main menu screen (The screen displayed after clicking start button)
                setBackground("mainMenuScreen.png");
                addObject(new Buttons("play"), 450, 230);
                addObject(new Buttons("saves"), 450, 297);
                addObject(new Buttons("scores"), 450, 363);
                addObject(new Buttons("guide"), 450, 431);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "selectDifficulty": // Difficulty select screen (The screen displayed after clicking play button)
                setBackground("selectDifficultyScreen.png");
                addObject(new Buttons("playEasyMaze"), 450, 230);
                addObject(new Buttons("playMediumMaze"), 450, 297);
                addObject(new Buttons("playHardMaze"), 450, 363);
                addObject(new Buttons("playExtremeMaze"), 450, 431);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "easyMaze": // Easy maze screen (The screen displayed after clicking playEasyMaze button) generates a new easy difficulty maze world to be played
                initialiseSession("easy");
                timerText = new DisplayText("Time Elapsed 0:00", 35, Color.WHITE, null); // Instantiate and save reference to the timer object with default starting text
                addObject(timerText, 450, 570);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "mediumMaze": // Medium maze screen (The screen displayed after clicking playMediumMaze button) generates a new medium difficulty maze world to be played
                initialiseSession("medium");
                timerText = new DisplayText("Time Elapsed 0:00", 35, Color.WHITE, null); // Instantiate and save reference to the timer object with default starting text
                addObject(timerText, 450, 570);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "hardMaze": // Hard maze screen (The screen displayed after clicking playHardMaze button) generates a new hard difficulty maze world to be played
                initialiseSession("hard");
                timerText = new DisplayText("Time Elapsed 0:00", 35, Color.WHITE, null); // Instantiate and save reference to the timer object with default starting text
                addObject(timerText, 450, 570);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "extremeMaze": // Extreme maze screen (The screen displayed after clicking playExtremeMaze button) generates a new extreme difficulty maze world to be played
                initialiseSession("extreme");
                timerText = new DisplayText("Time Elapsed 0:00", 35, Color.WHITE, null); // Instantiate and save reference to the timer object with default starting text
                addObject(timerText, 450, 570);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "saves": // Saves screen (The screen displayed after clicking saves button)
                setBackground("savesScreen.png");
                addObject(new Buttons("importFile"), 450, 230);
                addObject(new Buttons("playFile"), 450, 363);
                addObject(new Buttons("viewFile"), 450, 431);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "scores": // Scores screen (The screen displayed after clicking scores button)
                setBackground("selectDifficultyScreen.png");
                addObject(new Buttons("easyScores"), 450, 230);
                addObject(new Buttons("mediumScores"), 450, 297);
                addObject(new Buttons("hardScores"), 450, 363);
                addObject(new Buttons("extremeScores"), 450, 431);
                addObject(new Buttons("back"), 30, 30);
                break;
            case "easyScores": // Easy difficulty scores screen (The screen displayed after clicking easyScores button)
                displayScores("easy");
                addObject(new Buttons("back"), 30, 30);
                break;
            case "mediumScores": // Medium difficulty scores screen (The screen displayed after clicking mediumScores button)
                displayScores("medium");    
                addObject(new Buttons("back"), 30, 30);
                break;
            case "hardScores": // Hard difficulty scores screen (The screen displayed after clicking hardScores button)
                displayScores("hard");
                addObject(new Buttons("back"), 30, 30);
                break;
            case "extremeScores": // Extreme difficulty scores screen (The screen displayed after clicking extremeScores button)
                displayScores("extreme");
                addObject(new Buttons("back"), 30, 30);
                break;    
            case "guide": // Guide screen (The screen displayed after clicking guide button)
                setBackground("guideScreen.png");
                addObject(new Buttons("back"), 30, 30);
                break;
        }
    }

    /** Constructor for the initialisation of a Screens object for viewing or playing saved mazes. **/
    public Screens(boolean play, int[][] mazeLayout, List<Integer> playerPathTakenRows, List<Integer> playerPathTakenCols, double accuracyScore, double pathAccuracy, double timeAccuracy, int playerTimer)
    {
        super(900, 600, 1); // Create a new world with 900x600 cells with a cell size of 1x1 pixels.
        screenType = "savedMaze";
        addObject(new Buttons("back"), 30, 30);
        this.mazeLayout = mazeLayout; // Store the given mazeLayout 
        int rowsCount = mazeLayout.length; // Stores number of rows in the mazeLayout array
        int colsCount = mazeLayout[0].length; // Stores number of columns in the mazeLayout array
        int cellSize = 500/rowsCount; // Calculate the cell size based off of 500(the number of pixels in the vertical direction available to be used for displaying the maze) divided by the rowsCount
        drawMap(rowsCount, colsCount, cellSize); // Draw the map to the screen
        if (play) // If the user wants to play the saved maze
        {
            player = new Player(cellSize); // Instantiate and save reference to player object
            int startingXCoordinate = (cellSize) + ((900 - (cellSize * colsCount)) / 2) + (cellSize / 2); // startingXCoordinate = the cell size + the padding on the on the left/right sides + half of the cell size(to centre the player within the second cell)
            int startingYCoordinate = (cellSize) + 50 + ((500 - (cellSize * rowsCount)) / 2) + (cellSize / 2); // startingYCoordinate = the cell size + 50 units from the top for padding + the padding on the on the top/bottom sides + halff the cell size (to centre the player within the second cell)
            addObject(player, startingXCoordinate, startingYCoordinate);
            timerText = new DisplayText("Time Elapsed 0:00", 35, Color.WHITE, null); // Instantiate and save reference to the timer object with default starting text
            addObject(timerText, 450, 570);
        }
        else // If the user wants to view the saved maze
        {
            completed = true;
            mazeSolver = new MazeSolver(mazeLayout); // Instantiate and save reference to the MazeSolver object
            mazeSolver.findShortestSolutionPath(); // Recalculate optimal path           
            drawPath("solutionPath", mazeSolver.solutionPathTakenRows, mazeSolver.solutionPathTakenCols, rowsCount, colsCount, cellSize); // Draw the solution path found by the maze solver
            drawPath("playerPath", playerPathTakenRows, playerPathTakenCols, rowsCount, colsCount, cellSize); // Draw the player's path
            addObject(new DisplayText("Accuracy Score: " + String.format("%.2f",accuracyScore) + "%" + "  |  Path Acc: " + String.format("%.2f",pathAccuracy) + "%" + "  |  Time Acc: " + String.format("%.2f",timeAccuracy) + "%" + "  |  Time Elapsed: " + getTimerText(playerTimer), 24, Color.WHITE, null), 450, 570); // Display the players score data rounding accuracy score, path accuracy and time accuracy to 2 d.p.
        }
    }

    /** This method is called for every act cycle when the program is running which occurs when the run or act button is pressed in the environment **/
    public void act()
    {
        updateButtons();
        updateScreen();
    }
    
    /** Respond to and update button triggers and adds/removes buttons in the world where necessary **/ 
    private void updateButtons()
    {
        // Below is for previous and next buttons
        if (screenType == "easyScores" || screenType == "mediumScores" || screenType == "hardScores" || screenType == "extremeScores")
        {
            if (getObjectsAt(732, 440, Buttons.class).size() == 0) // If a next button is not already in the world
            {
                if (scoresListPageNumber == 1 && scoresListSize >= 4) // If there are more pages of scores to display (at least one more score yet to be displayed - special condition for the first page because it only displays 3 scores max whereas consequent pages can display 5)
                {
                    addObject(new Buttons("next"), 732, 440);
                }
                else if(scoresListSize >= ((scoresListPageNumber-1) * 5 + 4)) // If there are more pages of scores to display (at least one more score yet to be displayed)
                {
                    addObject(new Buttons("next"), 732, 440);
                }
            }
            if (getObjectsAt(732, 440, Buttons.class).size() != 0 && getObjectsAt(732, 440, Buttons.class).get(0).nextPage) // If a 'next' page button is in the world and the button has been pressed (signified by the 'nextPage' boolean trigger stored in the buttons class)
            {
                getObjectsAt(732, 440, Buttons.class).get(0).nextPage = false; // set the 'nextPage' boolean = false
                scoresListPageNumber++; // Increment the current page number
                displayScores(mazeDifficulty); // Display the relevant scores for the new page number
                removeObject(getObjectsAt(732, 440, Buttons.class).get(0)); // Remove the 'next' page button from the world
            }
            if (getObjectsAt(167, 440, Buttons.class).size() == 0 && scoresListPageNumber > 1) // If currently not on the first page and a 'previous' page button is not already in the world
            {
                addObject(new Buttons("previous"), 167, 440);
            }
            if (getObjectsAt(167, 440, Buttons.class).size() != 0 && getObjectsAt(167, 440, Buttons.class).get(0).previousPage) // If a 'previous' page button is in the world and the button has been pressed (signified by the 'previousPage' boolean trigger stored in the buttons class)
            {
                getObjectsAt(167, 440, Buttons.class).get(0).previousPage = false; // set the 'previousPage' boolean = false
                scoresListPageNumber--; // Decrement the current page number
                displayScores(mazeDifficulty); // Display the relevant scores for the new page number
                removeObject(getObjectsAt(167, 440, Buttons.class).get(0)); // Remove the 'previous' page button
            }
        }
        // Below is for save button
        if (getObjectsAt(870, 30, Buttons.class).size() != 0 && getObjectsAt(870, 30, Buttons.class).get(0).saveMaze) // If a 'save' button is in the world and the button has been pressed (signified by the 'saveMaze' boolean trigger stored in the buttons class)
        {
            getObjectsAt(870, 30, Buttons.class).get(0).saveMaze = false; // set the 'saveMaze' boolean = false
            saveToFile(null); // Save the file (null signifies that the save is not an autosave)
        }
        // Below is for importFile, playFile and viewFile buttons
        if (screenType == "saves")
        {
            if (getObjectsAt(450, 230, Buttons.class).get(0).importFile) // If 'importFile' button has been pressed (signified by the 'importFile' boolean trigger stored in the buttons class)
            { 
                getObjectsAt(450, 230, Buttons.class).get(0).importFile = false; // set the 'importFile' boolean = false
                importFile();
            }
            if (getObjectsAt(450, 363, Buttons.class).get(0).playFile) // If 'playFile' button has been pressed (signified by the 'playFile' boolean trigger stored in the buttons class)
            {
                if (mazeLayout != null) // Checks if a file was actually imported
                {
                    Greenfoot.setWorld(new Screens(true, mazeLayout, playerPathTakenRows, playerPathTakenCols, accuracyScore, pathAccuracy, timeAccuracy ,playerTimer)); // Set the world to play the file by passing 'true' into the 'play' paramter of the constructor
                }
                else
                {
                    getObjectsAt(450, 363, Buttons.class).get(0).playFile = false; // set the 'playFile' boolean = false
                    removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                    addObject(new DisplayText(" (ERROR) No file selected ", 35, Color.WHITE, Color.RED), 451, 30); // Add an error message saying "No file selected"
                }
            }
            if (getObjectsAt(450, 431, Buttons.class).get(0).viewFile) // If 'viewFile' button has been pressed (signified by the 'viewFile' boolean trigger stored in the buttons class)
            {
                if (mazeLayout != null) // Checks if a file was actually imported
                {
                    Greenfoot.setWorld(new Screens(false, mazeLayout, playerPathTakenRows, playerPathTakenCols, accuracyScore, pathAccuracy, timeAccuracy ,playerTimer)); // Set the world to view the file by passing 'false' into the 'play' paramter of the constructor
                }
                else
                {
                    getObjectsAt(450, 431, Buttons.class).get(0).viewFile = false; // set the 'viewFile' boolean = false
                    removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                    addObject(new DisplayText(" (ERROR) No file selected ", 35, Color.WHITE, Color.RED), 451, 30); // Add an error message saying "No file selected"
                }
            }
        }
    }

    /** If the player has not already completed the maze it updates the time elapsed text on the screen and calls the checkCompleted method  **/
    private void updateScreen()
    {
        if ((screenType == "easyMaze" || screenType == "mediumMaze" || screenType == "hardMaze" || screenType == "extremeMaze" || screenType == "savedMaze") && !completed)
        {
            timerText.updateText("Time Elapsed: " +  getTimerText(player.timer), 35, Color.WHITE, null); // Update timer object's text to update the time elapsed text displayed on the screen
            checkCompleted(); // Call checkCompleted() to update the screen once the player has completed the maze
        }
    }
    
    /** Generate the maze, draw the map to the screen and add the player to the world **/
    private void initialiseSession(String mazeDifficulty)
    {
        this.mazeDifficulty = mazeDifficulty; // Store the given maze difficulty
        mazeGenerator = new MazeGenerator(mazeDifficulty); // Instantiate and save reference to the MazeGenerator object
        mazeLayout = mazeGenerator.generateMaze(); // Generate the maze and store the generated maze
        int rowsCount =  mazeLayout.length; // Stores number of rows in the mazeLayout array
        int colsCount =  mazeLayout[0].length; // Stores number of columns in the mazeLayout array
        int cellSize = 500/rowsCount; // Calculate the cell size based off of 500(the number of pixels in the vertical direction available to be used for displaying the maze) divided by the rowsCount
        drawMap(rowsCount, colsCount, cellSize); // Draw the map to the screen
        player = new Player(cellSize); // Instantiate and save reference to player object
        int startingXCoordinate = (cellSize) + ((900 - (cellSize * colsCount)) / 2) + (cellSize / 2); // startingXCoordinate = the cell size + the padding on the on the left/right sides + half of the cell size(to centre the player within the second cell)
        int startingYCoordinate = (cellSize) + 50 + ((500 - (cellSize * rowsCount)) / 2) + (cellSize / 2); // startingYCoordinate = the cell size + 50 units from the top for padding + the padding on the on the top/bottom sides + half the cell size (to centre the player within the second cell)
        addObject(player, startingXCoordinate, startingYCoordinate);
    }

    /** Draws the map specified in the mazeLayout array to the screen **/
    private void drawMap(int rowsCount, int colsCount, int cellSize)
    {
        GreenfootImage backgroundImage = new GreenfootImage(900, 600); // Create a greenfoot image called 'background' with the same size as the world (900x600 pixels)
        backgroundImage.fill(); // Fill the background image with black
        getBackground().drawImage(backgroundImage, 0, 0); // Draw the background image to the background
        GreenfootImage mazeWallImage = new GreenfootImage(cellSize,cellSize); // Create a greenfoot image called 'mazeWallImage' which is a square of height and width equal to cellSize, to represent a cell that is a wall
        GreenfootImage mazePathImage = new GreenfootImage(cellSize,cellSize); // Create a greenfoot image called 'mazePathImage' which is a square of height and width equal to cellSize, to represent a cell that is a path
        GreenfootImage mazeEndImage = new GreenfootImage(cellSize,cellSize); // Create a greenfoot image called 'mazeEndImage' which is a square of height and width equal to cellSize, to represent a cell that is the maze's end
        for (int row = 0; row < mazeLayout.length ; row++) // For every row of the mazeLayout array
        {
            for (int col = 0; col < mazeLayout[0].length; col++) // For every column of the row of mazeLayout array
            {
                int xCoordinate = (col * cellSize) + ((900 - (cellSize * colsCount)) / 2); // xCoordinate = (the column index of the cell in the mazeLayout array * the cell size) + the padding on the on the left/right sides
                int yCoordinate = (row * cellSize) + 50 + ((500 - (cellSize * rowsCount)) / 2); // yCoordinate = (the row index of the cell in the mazeLayout array * the cell size) + 50 units from the top for padding + the padding on the on the top/bottom sides
                if (mazeLayout[row][col] == 1) // 1 in the array represents a wall
                {
                    mazeWallImage.setColor(Color.BLACK); // Set the fill colour to black
                    mazeWallImage.fill(); // Fill the maze wall image with the given colour
                    getBackground().drawImage(mazeWallImage, xCoordinate, yCoordinate); // Draw the mazeWall image to the background
                }
                else if (mazeLayout[row][col] == 2) // 2 in the array represents a path
                {
                    mazePathImage.setColor(Color.WHITE); // Set the fill colour to white
                    mazePathImage.fill(); // Fill the maze path image with the given colour
                    getBackground().drawImage(mazePathImage, xCoordinate, yCoordinate); // Draw the mazePath image to the background
                }
                else // The remaining value in the array(3) will indicate the end cell
                {   
                    mazeEndImage.setColor(Color.GREEN); // Set the fill colour to green
                    mazeEndImage.fill(); // Fill the maze end image with the given colour
                    getBackground().drawImage(mazeEndImage, xCoordinate, yCoordinate); // Draw the end image to the background
                }
            }
        }
    }   

    /** Draws the path specified(either a  player path or a maze solver path) to the screen **/
    private void drawPath(String type, List<Integer> pathTakenRows, List<Integer> pathTakenCols, int rowsCount, int colsCount, int cellSize)
    {
        GreenfootImage pathImage; // Create a greenfoot image called 'pathImage' to represent each cell of the path taken
        if (type == "playerPath")
        {
            pathImage = new GreenfootImage(cellSize / 2, cellSize / 2); // Set the path image of the player to be half the size of the cells
            pathImage.setColor(Color.BLUE); // Set the fill colour to blue
            pathImage.fill(); // Fill the path image with the given colour
        }
        else
        {
            pathImage = new GreenfootImage(cellSize, cellSize); // Set the path image of the maze solver to be the size of the cells
            pathImage.setColor(new Color(0, 255, 0, 100)); // Set the fill colour to light green
            pathImage.fill(); // Fill the path image with the given colour
        }
        for (int i = 0; i < pathTakenRows.size(); i++) 
        {
            int row = pathTakenRows.get(i); // Stores the row index of the cell in the mazeLayout array for the current cell of the path the player has taken
            int col = pathTakenCols.get(i); // Stores the column index of the cell in the mazeLayout array for the current cell of the path the player has taken
            if (type == "playerPath")
            {
                int xCoordinate = (col * cellSize) + ((900 - (cellSize * colsCount)) / 2) + (cellSize / 4); // xCoordinate = (column index * the cell size) + the padding on the on the left/right sides + the cell size / 4 (to centre the image within the current cell)
                int yCoordinate = (row * cellSize) + 50 + ((500 - (cellSize * rowsCount)) / 2) + (cellSize / 4); // yCoordinate = (row index * the cell size) + 50 units from the top for padding + the padding on the on the top/bottom sides + the cell size / 4 (to centre the image within the current cell)
                getBackground().drawImage(pathImage, xCoordinate, yCoordinate);
            }
            else
            {
                int xCoordinate = (col * cellSize) + ((900 - (cellSize * colsCount)) / 2); // xCoordinate = (column index * the cell size) + the padding on the on the left/right sides
                int yCoordinate = (row * cellSize) + 50 + ((500 - (cellSize * rowsCount)) / 2); // yCoordinate = (row index * the cell size) + 50 units from the top for padding + the padding on the on the top/bottom sides
                getBackground().drawImage(pathImage, xCoordinate, yCoordinate);
            }
        }
    }

    /** Checks if the player has completed the maze if so: remove the player, draw the optimal solution path, draw the player's path, calculate and display the score, and if the maze is not a saved maze meaning: update the scores list, add a save button to give the option to save the maze if the maze was not autosaved **/
    private void checkCompleted()
    {
        if (player.checkCompleted()) // If the player has completed the maze
        {
            completed = true;
            playerPathTakenRows = player.pathTakenRows; // Store the row indexes of the path taken by the player
            playerPathTakenCols = player.pathTakenCols; // Store the column indexes of the path taken by the player
            playerTimer = player.timer; 
            removeObjects(getObjects(Player.class));
            int rowsCount = mazeLayout.length; // Stores number of rows in the mazeLayout array
            int colsCount = mazeLayout[0].length; // Stores number of columns in the mazeLayout array
            int cellSize = 500/rowsCount; // Calculate the cell size based off of 500(the number of pixels in the vertical direction available to be used for displaying the maze) divided by the rowsCount
            mazeSolver = new MazeSolver(mazeLayout); // Instantiate and save reference to the MazeSolver object
            mazeSolver.findShortestSolutionPath(); // Recalculate optimal path
            drawPath("solutionPath", mazeSolver.solutionPathTakenRows, mazeSolver.solutionPathTakenCols, rowsCount, colsCount, cellSize); // Draw the solution path found by the maze solver
            drawPath("playerPath", playerPathTakenRows, playerPathTakenCols, rowsCount, colsCount, cellSize); // Draw the player's path
            removeObjects(getObjectsAt(450, 570, DisplayText.class)); // Removed time elpased text
            calculateScore(playerTimer, playerPathTakenRows.size(), mazeSolver.solutionPathTakenRows.size());
            addObject(new DisplayText("Accuracy Score: " + String.format("%.2f",accuracyScore) + "%" + "  |  Path Acc: " + String.format("%.2f",pathAccuracy) + "%" + "  |  Time Acc: " + String.format("%.2f",timeAccuracy) + "%" + "  |  Time Elapsed: " + getTimerText(playerTimer), 24, Color.WHITE, null), 450, 570); // Display the players score data rounding accuracy score, path accuracy and time accuracy to 2 d.p.
            if (screenType != "savedMaze")
            {
                updateScoresList(true, null); // This is the first time the method is being callied i.e. parameter = true (so we know to check for an autosave), thus there is no fileName determined yet i.e. parameter = null
                if (getObjectsAt(450, 30, DisplayText.class).size() == 0) // If the maze has not already been autosaved
                {
                    addObject(new Buttons("saveMaze"), 870, 30);
                }
            }
        }
    }

    /** Return a string to represent the time elapsed in minutes and seconds when given a number of act cycles that have passed **/
    private String getTimerText(int actsCount)
    {
        int totalSeconds = actsCount / 60; // 60 act cycles per second
        int minutes = totalSeconds / 60; // The integer value of totalSeconds / 60 gives only the full minutes that have passed
        int displaySeconds = totalSeconds % 60; // The remainder of seconds / 60 gives the remaining seconds that didn't amount to another minute
        if (displaySeconds < 10) 
        { 
            return minutes + ":0" + displaySeconds; // Ensures that the string is in the form of x:xx when displaySeconds is in the single digits
        }
        else
        {
            return minutes + ":" + displaySeconds;
        }
    }

    /** Calculates the player's accuracy score, and consequently their path accuracy and time accuracy **/
    private void calculateScore(int playerTimer, int playerPathSize, int solverPathSize)
    {  
        pathAccuracy = (((solverPathSize*100)*100.0)/playerPathSize)/100.0; // pathAccuracy = the number of moves taken by the maze solver / the number of moves taken by the player as a float(*100.0 to obtain the value as a float) percentage(*100 to get the value as a percentage)
        int minTimer = ((solverPathSize-1)*6); // minTimer = the number of moves taken by the maze solver * the movement delay i.e. if the player where go forward non stop how many act cycles would it have taken to complete the maze at a minimum
        timeAccuracy = (((minTimer*100)*100.0)/playerTimer)/100.0; // timeAccuracy = minimum act cycles required / the number of act cycles taken by the player as a float(*100.0 to obtain the value as a float) percentage(*100 to get the value as a percentage)
        accuracyScore = ((pathAccuracy + timeAccuracy)/2); // average the path accuracy and time accuracy to get the accuracy score as a float percentage
    }
    
    /** Save a maze and the corresponding player data to a JSON file **/
    private void saveToFile(String autosaveFileName)
    {
        String saveFileName;
        boolean fileNameInUse = false;
        if (autosaveFileName == null) // If the file is not being autosaved and thus does not already have a designated file name
        {
            saveFileName = JOptionPane.showInputDialog("Input a name for the file:"); // Get a file name from the user 
            if (saveFileName != null)
            {
                if (isExistingFile(saveFileName)) // If the users inputed save file name is already in use
                {
                    fileNameInUse = true;
                    removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                    addObject(new DisplayText(" (ERROR) DID NOT SAVE: file '" + saveFileName + "' already exists ", 35, Color.WHITE, Color.RED), 451, 30);
                }
            }
            else
            {
                removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                addObject(new DisplayText(" (ERROR) DID NOT SAVE: No file name given ", 35, Color.WHITE, Color.RED), 451, 30);
            }
        }
        else
        {
            saveFileName = autosaveFileName; 
        }
        if (!fileNameInUse && saveFileName != null)
        {
            JSONObject saveData = new JSONObject(); // Creates a JSONObject to store the save data
            JSONArray mazeLayoutArray = new JSONArray(); // Creates a JSONArray to store the mazeLayout array
            JSONArray playerPathTakenRowsArray = new JSONArray(); // Creates a JSONArray to store the playerPathTakenRows array
            JSONArray playerPathTakenColsArray = new JSONArray(); // Creates a JSONArray to store the playerPathTakenCols array
            saveData.put("mazeDifficulty", mazeDifficulty); // Store the maze difficulty in saveData with the key 'mazeDifficulty'
            saveData.put("accuracyScore", accuracyScore); // Store the accuracy Score in saveData with the key 'accuracyScore'
            saveData.put("pathAccuracy", pathAccuracy); // Store the path accuracy in saveData with the key 'pathAccuracy'
            saveData.put("timeAccuracy", timeAccuracy); // Store the time accuracy in saveData with the key 'timeAccuracy'
            saveData.put("playerTimer", playerTimer); // Store the player timer in saveData with the key 'playerTimer'
            for(int row = 0; row < mazeLayout.length; row++) // For every row of the mazeLayout array
            {
                for(int col = 0; col < mazeLayout[row].length; col++) // For every column of the row of mazeLayout array
                {
                    mazeLayoutArray.add(mazeLayout[row][col]); // Store the value at the row, col index. Note this is going from a 2D array to a 1D array
                }
            }
            saveData.put("mazeLayout", mazeLayoutArray); // Store the maze layout in saveData with the key 'mazeLayout'
            for (int i = 0; i < playerPathTakenRows.size(); i++) // For every cell of the playerPath (note playerPathTakenRows.size() = playerPathTakenCols.size() and every value at a certain index in each path array corresponds to a value at the same index in the other path array)
            {
                playerPathTakenRowsArray.add(playerPathTakenRows.get(i)); // Store the row index value for the given cell.
                playerPathTakenColsArray.add(playerPathTakenCols.get(i)); // Store the column index value for the given cell.
            }
            saveData.put("playerPathTakenRows", playerPathTakenRowsArray); // Store the row indexes for each cell of the path taken by the player in saveData with the key 'playerPathTakenRows'
            saveData.put("playerPathTakenCols", playerPathTakenColsArray); // Store the column indexes for each cell of the path taken by the player in saveData with the key 'playerPathTakenRows'
            try
            {
                writeToJson(saveFileName, saveData); // Write saveData to a Json file with file name: saveFileName
                removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                removeObjects(getObjectsAt(870, 30, Buttons.class)); // Remove the save button
                if (autosaveFileName != null) // If the file was being autosaved
                {
                    addObject(new DisplayText("Autosaved as: " + autosaveFileName, 35, Color.WHITE, null), 450, 30);
                }
                else
                {
                    addObject(new DisplayText("Saved", 35, Color.WHITE, null), 450, 30); // 
                    updateScoresList(false, saveFileName); // Update the scores list now that we know the scoresListObject will have a corresponding file name. Note: this isn't called in the if statement above because the scores list will already have been updated if the file is being autosaved, as we will have known the scoresListObject's corresponding fileName. This is not first time the method is being callied i.e. parameter = true, the fileName is given i.e. parameter = saveFileName
                }
            }
            catch (Exception e)
            {
                removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                addObject(new DisplayText(" (ERROR) DID NOT SAVE: failed to save game file ", 35, Color.WHITE, Color.RED), 451, 30);
                System.out.println("(ERROR) DID NOT SAVE: failed to save game file");
                e.printStackTrace();
            }
            Screens.playerPathTakenRows.clear();
            Screens.playerPathTakenCols.clear();
        }
    }
    
    /** Checks if a file of a given name already exists **/
    private boolean isExistingFile(String fileName)
    {
        File file = new File(fileName); // Instantiate a File with name fileName
        if (file.exists())
        {
            return true;
        }
        else
        {
            return false;  
        }
    }

    /** Write a given JSONObject to a file with a given filename **/
    private  FileWriter writeToJson(String fileName, JSONObject jObject) throws Exception 
    {
        FileWriter fileWriter = new FileWriter(fileName); // Instantiate a fileWriter for a given fileName and store a reference to it
        fileWriter.write(jObject.toJSONString()); // Write to the file of the fileWriter a given JSONObject
        fileWriter.flush();
        fileWriter.close();
        return fileWriter;
    }

    /** Updates the relevant scores list in the scoresLists file **/
    public void updateScoresList(boolean firstCall, String fileName) 
    {
        try 
        {
            JSONObject scoresListsFile = (JSONObject) readJson("scoresLists"); // Reads the scoresLists File and saves it into the created JSONObject called 'scoresListsFile'
            JSONObject scoresList = (JSONObject) scoresListsFile.get(mazeDifficulty + "ScoresList"); // Creates a JSONObject to store the scores list from scoresListsFile of a desired difficulty into a JSONObject called 'scoresList'
            if (firstCall) // If this is the first call of the method it it still yet to be determined if the maze will be autosaved and thus we need to check if this will be the case, and if the scores lsit will require sorting if this isn't the case
            {
                updateCumulativeScores(scoresListsFile); // Update the average scores and total games completed values
                int key = scoresList.size() - 1; // Stores the current key (The keys are positive integers including 0 to represent the order of each scoresListObject, i.e. scoresListObject at key 3 contains the third best score) of the scoresListObject being viewed. Starts at the end to improve efficiency of the while loop as scores list is ordered from best to worst (i.e. scoresListObject at key 0 contains the best accuracy score)
                while (key != -1 && Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("accuracyScore"))) < accuracyScore) // While there are still keys left to check and the accuracy score at the current key < the new accuracyScore to be saved
                {
                    key--;
                }
                if (key != scoresList.size() - 1) // If the new accuracy score was actually better than any exisiting accuracy scores in the scoresList
                {
                    if (key < 9) // If the new accuracy score was in the top 10, autosave the maze
                    {
                        String saveFileName = selectAutosaveFileName(); // Get and store an an available file name for the autsaved file 
                        saveToFile(saveFileName); // Save the maze to file with name = saveFileName
                        JSONObject updatedScoresList = getUpdatedScoresList(scoresList, saveFileName); // Get and store in the created JSONObject an updated and sorted scores list including the new accuracy score
                        scoresListsFile.put(mazeDifficulty + "ScoresList", updatedScoresList); // Store the updated scores list in scoresListsFile with the key: mazeDifficulty + "ScoresList"
                    }
                    else
                    {
                        scoresListRequiresSorting = true;  // Indicates for when the updateScoresList method is called again that it will have to sort the scores list
                    }
                }
                else if (scoresList.size() < 10) // If there are not already 10 accuracy scores in the scores list
                {
                    String saveFileName = selectAutosaveFileName(); // Get and store an an available file name for the autsaved file 
                    saveToFile(saveFileName); // Save the maze to file with name = saveFileName
                    scoresList.put(scoresList.size(), getScoresListObject(saveFileName)); // Create and store scoresListObject in scoresList with key = scoresList.size() i.e. place it at the end of the scores list
                    scoresListsFile.put(mazeDifficulty + "ScoresList", scoresList); // Store scoresList in scoresListsFile with key = mazeDifficulty + "ScoresList"
                }
                else
                {
                    scoresListRequiresSorting = false; // Indicates for when the updateScoresList method is called again that it will not have to sort the scores list
                }
            }
            else // When it is not the first call of the method we know that we do not need to check if the file will be autosaved as it already would have if it was appropraite, we also know what the file name will be now because this is called when there is no autosave, thus the user had the option to save the maze, so we now know if the new scoresListObject should have a corresponding file name
            {
                String saveFileName;  
                if (fileName != null) 
                {
                    saveFileName = fileName;
                }
                else
                {
                    saveFileName = "N/A";
                }
                if (scoresListRequiresSorting == true) // i.e. The new scoresListObject cannot just be placed at the end because it needs to slot in somewhere because it contains an accuracy score that beats another scoresListObjects accuracy score
                {
                    JSONObject updatedScoresList = getUpdatedScoresList(scoresList, saveFileName); // Get and store in the created JSONObject an updated and sorted scores list including the new accuracy score
                    scoresListsFile.put(mazeDifficulty + "ScoresList", updatedScoresList); // Store the updated scores list in scoresListsFile with the key: mazeDifficulty + "ScoresList"
                }
                else
                {
                    scoresList.put(scoresList.size(), getScoresListObject(saveFileName)); // Create and store scoresListObject in scoresList with key = scoresList.size() i.e. place it at the end of the scoresList
                    scoresListsFile.put(mazeDifficulty + "ScoresList", scoresList); // Store scoresList in scoresListsFile with key = mazeDifficulty + "ScoresList"
                }
            }
            writeToJson("scoresLists", scoresListsFile); // Write scoresListsFile to a Json file with file name: 'scoresLists'
        }
        catch (Exception e)
        {
            System.out.println("(ERROR) failed to update scoresList");
            e.printStackTrace();
        }
    }    
    
    /** Read a given JSON file **/
    private static Object readJson(String fileName) throws Exception
    {
        FileReader fileReader = new FileReader(fileName); // Instantiate a fileReader for a given fileName and store a reference to it
        JSONParser jParser = new JSONParser(); // Instantiate an JSONParser and store a reference to it
        return jParser.parse(fileReader); // Return the parsed data from file of fileReader
    }
    
    /** Updates the cumulative scores for the given difficulty - i.e. the average accuracy score and the games completed count **/
    private void updateCumulativeScores(JSONObject fileObject)
    {
        try    
        {
            JSONObject averageScoresOut = (JSONObject) fileObject.get("averageScores"); // Creates a JSONObject to store the averageScores JSONObject
            JSONObject gamesCompletedOut = (JSONObject) fileObject.get("gamesCompleted"); // Creates a JSONObject to store the gamesCompleted JSONObject
            double averageScore = Double.parseDouble(averageScoresOut.get(mazeDifficulty).toString()); // Stores the average score for a given difficuly from averageScoresOut
            int gamesCompleted = Integer.parseInt(gamesCompletedOut.get(mazeDifficulty).toString()); // Stores the games completed for a given difficuly from gamesCompletedOut
            averageScoresOut.put(mazeDifficulty, ((averageScore * gamesCompleted) + accuracyScore)/(gamesCompleted + 1)); // Recalculates and stores the average score for the given difficulty in averageScoresOut with the key: mazeDifficulty
            gamesCompletedOut.put(mazeDifficulty, gamesCompleted + 1); // Recalculates and stores the games completed for the given difficulty in gamesCompletedOut with the key: mazeDifficulty
            fileObject.put("averageScores", averageScoresOut); // Store updated average scores in fileObject with the key 'averageScores'
            fileObject.put("gamesCompleted", gamesCompletedOut); // Store the updated games completed in fileObject with the key 'gamesCompleted
        }
        catch (Exception e)
        {
            System.out.println("(ERROR) failed to update cumulative scores");
            e.printStackTrace();
        }
    }
    
    /** Returns an available  file name for an autosave file **/
    private String selectAutosaveFileName()
    {
        int autosaveNumber = 1;
        File file = new File("Autosave-" + Integer.toString(autosaveNumber)); // Instantiate a file with name Autosave-1
        while (file.exists()) // While the file exisits
        {
            autosaveNumber++; 
            file = new File("Autosave-" + Integer.toString(autosaveNumber)); // Set file to a new file with name: "Autosave-" + Integer.toString(autosaveNumber)
        }
        return "Autosave-" + autosaveNumber; // Return the available autosave file name
    }
    
    /** Creates and returns a scoresListObject for the scores being appended **/
    private JSONObject getScoresListObject(String saveFileName)
    {
        JSONObject scoresListObject = new JSONObject(); // Creates a JSONObject to store the new scoresList object being appended
        scoresListObject.put("accuracyScore", accuracyScore); // Store the accuracy score in scoresListObject with the key 'accuracyScore'
        scoresListObject.put("pathAccuracy", pathAccuracy); // Store the path accuracy in scoresListObject with the key 'pathAccuracy'
        scoresListObject.put("timeAccuracy", timeAccuracy); // Store the time accuracy in scoresListObject with the key 'timeAccuracy'  
        scoresListObject.put("playerTimer", playerTimer); // Store the player timer in scoresListObject with the key 'playerTimer'
        scoresListObject.put("saveFileName", saveFileName); // Store the save file name in scoresListObject with the key 'saveFileName'
        return scoresListObject;
    }
    
    /** Returns an updated and sorted scores list **/
    private JSONObject getUpdatedScoresList(JSONObject scoresList, String saveFileName) 
    {
        try 
        {
            ArrayList<Integer> orderedKeys = new ArrayList<Integer>(); // Stores the keys of each scoresListObject(each score) in the scoresList - used for reordering without directly reordering the passed in scoresList JSONObject as this cannot occur
            for (int i = 0; i < scoresList.size() + 1; i++) // For every key in the scoresList (The keys are positive integers including 0 to represent the order of each scoresListObject, i.e. scoresListObject at key 3 contains the third best accuracy score, however the keys are currently unsorted)
            {
                orderedKeys.add(i); // Add the key to the orderedKeys array
            }
            int unsortedKeysCount = orderedKeys.size(); // Stores the number of unsorted keys
            boolean swapped = true; // Stores whether or not two values have been swapped
            while (swapped) // Bubble sort going from left to right (index 0 - infinity) sorting in descending order
            {
                swapped = false;
                double currentAccuracyScore; // Stores the accuracy score for the scoresListObject at the current index
                double adjacentAccuracyScore; // Stores the accuracy score for the scoresListObject adjacent to the current index(to the right i.e. index + 1)
                for (int i = 0; i < unsortedKeysCount - 1; i++) // Selection sort going from left to right(i.e. index 0 - infinity) placing the highest value at the left(i.e. index 0)
                {
                    currentAccuracyScore = Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(orderedKeys.get(i)))).get("accuracyScore"))); // currentAccuracyScore = the accuracy score of the scoresListObject with key = the value of orderedKeys at index = i
                    if (orderedKeys.get(i) != unsortedKeysCount - 2)
                    {
                        adjacentAccuracyScore = Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(orderedKeys.get(i+1)))).get("accuracyScore"))); // adjacentAccuracyScore = the accuracy score of the scoresListObject with key = the value of orderedKeys at index = i + 1
                    }
                    else // This occurs when the key is the key of the new scoresListObject being appended (which is when the key for the scoresListObject of the currentAccuracyScore is second from the end of the scores list(i.e. unsortedKeysCount - 2) , this must be like so because appending to the scoresList JSONObject then getting its value cannot occur
                    {
                        adjacentAccuracyScore = accuracyScore; // the new scoresListObject being appended will have accuracyScore = the worlds accuracyScore variable
                    }
                    if (currentAccuracyScore < adjacentAccuracyScore)
                    {
                        // Below swaps the position of the keys for the scoresListObjects for the currentAccuracyScore and adjacentAccuracyScore
                        int tempKey = orderedKeys.get(i);
                        orderedKeys.set(i, orderedKeys.get(i+1));
                        orderedKeys.set(i+1, tempKey);
                        swapped = true;
                    }
                }
                unsortedKeysCount--;
            }
            JSONObject updatedScoresList = new JSONObject(); // Creates a JSONObject to store the updated scores list. Note the scores list is ordered from best to worst. (i.e. scoresListObject a key 0 will contain the best accuracy score)
            for (int i = 0; i < orderedKeys.size(); i++) // For every key in the orderedKeys array 
            {
                if (orderedKeys.get(i) != scoresList.size()) // If the value of that key at index i != scoresList.size() - i.e. the key is not the key of the new scoresListObject being appended
                {
                    updatedScoresList.put(i, (JSONObject)scoresList.get(Integer.toString(orderedKeys.get(i)))); // Store the scoresListObject of scoresList at key = the orderedKeys value at index i, in updatedScoresList with the key i
                }
                else
                {
                    updatedScoresList.put(i, getScoresListObject(saveFileName)); // Create and store scoresListObject in updatedScoresList with key = i 
                }
            }
            return updatedScoresList;
        }
        catch (Exception e )
        {
            System.out.println("(ERROR) failed to create the updated scoresList");
            e.printStackTrace();
            return null;
        }
    }
    
    /** Displays the scores on a page for a given difficulty **/
    private void displayScores(String mazeDifficulty)
    {
        try 
        {
            this.mazeDifficulty = mazeDifficulty; // Store the given mazeDifficulty 
            JSONObject scoresListsFile = (JSONObject) readJson("scoresLists"); // Reads the scoresLists File and saves it into the created JSONObject called 'scoresListsFile'
            JSONObject scoresList = (JSONObject) scoresListsFile.get(mazeDifficulty + "ScoresList"); // Creates a JSONObject to store the scores list from scoresListsFile of a desired difficulty into a JSONObject called 'scoresList'
            scoresListSize = scoresList.size(); // Store the size of scoresList
            removeObjects(getObjects(DisplayText.class)); // Remove all DisplayText objects from the world
            int row = 0; // The current row in the scores list table
            if (scoresListSize != 0) // If there is data to display
            {
                int key; // The key of the current record (i.e. scoresListObject) in scoresList to be displayed
                int lastKeyToDisplay; // The key of the last record (i.e. scoresListObject) in scoresList to be displayed on a given page number
                int yCoordiante; // The yCoordinate of the text to be displayed
                if (scoresListPageNumber == 1)
                {
                    key = 0;
                    lastKeyToDisplay = 2;
                    yCoordiante = 280;
                }
                else
                {
                    key = (scoresListPageNumber-1)*5 - 2;
                    lastKeyToDisplay = key + 4;
                    yCoordiante = 206;
                }     
                int tempYCoordinate = yCoordiante; // Store yCoordiante in tempYCoordinate for manipulation without changing the orginal value
                while (key < scoresList.size() && key < lastKeyToDisplay + 1)
                {
                    tempYCoordinate = tempYCoordinate + 33; // 33 units between each row
                    addObject(new DisplayText(Integer.toString(key + 1), 24, Color.WHITE, null), 132, tempYCoordinate); // Get and display the rank of the scoresListObject which equals to its key + 1 as indexing starts at 0
                    addObject(new DisplayText(String.format("%.2f",Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("accuracyScore")))) + "%", 24, Color.WHITE, null), 245, tempYCoordinate); // Get and display the accuracy score
                    addObject(new DisplayText(String.format("%.2f",Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("pathAccuracy")))) + "%", 24, Color.WHITE, null), 378, tempYCoordinate); // Get and display the path accuracy
                    addObject(new DisplayText(String.format("%.2f",Double.parseDouble(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("timeAccuracy")))) + "%", 24, Color.WHITE, null), 482, tempYCoordinate); // Get and display the time accuracy
                    addObject(new DisplayText(getTimerText(Integer.parseInt(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("playerTimer")))), 24, Color.WHITE, null), 571, tempYCoordinate); // Get and display the timer
                    addObject(new DisplayText(cutStringToLength(String.valueOf(((JSONObject)scoresList.get(Integer.toString(key))).get("saveFileName")), 15), 24, Color.WHITE, null), 703, tempYCoordinate); // Get and display the save file name cut down to 15 characters in length
                    key++;
                    row++;
                } 
                if (scoresListPageNumber == 1)
                {
                    JSONObject averageScoresOut = (JSONObject) scoresListsFile.get("averageScores"); // Creates a JSONObject to store the averageScores JSONObject
                    JSONObject gamesCompletedOut = (JSONObject) scoresListsFile.get("gamesCompleted"); // Creates a JSONObject to store the gamesCompleted JSONObject
                    addObject(new DisplayText("Average Accuracy Score: " + String.format("%.2f",averageScoresOut.get(mazeDifficulty)) + "%", 28, Color.WHITE, null), 298, 219); // Gets and displays the average score for the given difficulty rounded to 2 d.p. 
                    addObject(new DisplayText("Games Completed: " + gamesCompletedOut.get(mazeDifficulty).toString(), 28, Color.WHITE, null), 615, 219); // Gets and displays the games completed for the given difficulty
                }
                // Below displays the column headings for the scores list table
                addObject(new DisplayText("Rank", 24, Color.RED, null), 132, yCoordiante); 
                addObject(new DisplayText("Accuracy Score", 24, Color.RED, null), 245, yCoordiante);
                addObject(new DisplayText("Path Acc", 24, Color.RED, null), 378, yCoordiante);
                addObject(new DisplayText("Time Acc", 24, Color.RED, null), 482, yCoordiante);
                addObject(new DisplayText("Timer", 24, Color.RED, null), 571, yCoordiante);
                addObject(new DisplayText("Save File Name", 24, Color.RED, null), 703, yCoordiante);
            }
            else
            {
                addObject(new DisplayText("You have not yet completed any " + mazeDifficulty + " mazes", 35, Color.WHITE, null), 450, 300);
            }
            if (scoresListPageNumber == 1)
            {
                setBackground("1stPage" + row + mazeDifficulty + "ScoresScreen.png"); // Set background based off of how how many scores are on the page, the first page is different as it also has the box for displaying the average score and total games completed
            }
            else
            {
                setBackground(row + mazeDifficulty + "ScoresScreen.png"); // Set background based off of how how many scores are on the page
            }  
        }
        catch (Exception e)
        {
            System.out.println("(ERROR) failed to display scores");
            e.printStackTrace();
        }
    }
    
   /** Reads and stores data from a given file **/
    private void importFile()
    {
        try 
        {
            Screens.playerPathTakenRows.clear();
            Screens.playerPathTakenCols.clear();
            String fileName = getFileName(); // Get the desired file to be imported from the user and store its name
            if (fileName != null)
            {
                if (isExistingFile(fileName)) // If the specified file exists
                {
                    JSONObject saveFile = (JSONObject) readJson(fileName); // Reads the save file and saves it into the created JSONObject called 'saveFile'
                    mazeDifficulty = (String) saveFile.get("mazeDifficulty"); // Creates a JSONObject to store the maze diffifculty from saveFile into a JSONObject called 'mazeDifficulty'
                    accuracyScore = ((Double) saveFile.get("accuracyScore")); // Creates a JSONObject to store the accuracy score from saveFile into a JSONObject called 'accuracyScore'
                    pathAccuracy = ((Double) saveFile.get("pathAccuracy")); // Creates a JSONObject to store the path accuracy from saveFile into a JSONObject called 'pathAccuracy'
                    timeAccuracy = ((Double) saveFile.get("timeAccuracy")); // Creates a JSONObject to store the time accuracy from saveFile into a JSONObject called 'timeAccuracy'
                    playerTimer = ((Long) saveFile.get("playerTimer")).intValue(); // Creates a JSONObject to store the player timer from saveFile into a JSONObject called 'playerTimer'
                    List playerPathTakenRowsOut = Splitter.on(",").splitToList(saveFile.get("playerPathTakenRows").toString().replaceAll("(^\\[|\\]$)", "")); // Gets and saves to a List the playerPathTakenRows from saveFile in a readable format (formats the data by removing the square brackets and spltting to a list every comma separated value)
                    List playerPathTakenColsOut = Splitter.on(",").splitToList(saveFile.get("playerPathTakenCols").toString().replaceAll("(^\\[|\\]$)", "")); // Gets and saves to a list the playerPathTakenCols from saveFile in a readable format (formats the data by removing the square brackets and spltting to a list every comma separated value)
                    for (int i = 0; i < playerPathTakenRowsOut.size() ; i ++) // For every cell of the playerPathTaken (note playerPathTakenRowsOut.size() = playerPathTakenColsOut.size() and every value at a certain index in each path array corresponds to a value at the same index in the other path array)
                    {   
                        playerPathTakenRows.add(Integer.parseInt(String.valueOf(playerPathTakenRowsOut.get(i)))); // Store the row index value for the given cell.
                        playerPathTakenCols.add(Integer.parseInt(String.valueOf(playerPathTakenColsOut.get(i)))); // Store the column index value for the given cell.
                    }
                    int rowsCount; // Stores the number of rows in mazeLayout
                    int colsCount; // Stores the number of columns in mazeLayout
                    switch(mazeDifficulty) // Set the rowsCount and colsCount for mazeLayout based off of the mazeDifficulty
                    {
                        case "easy":
                            rowsCount = 15;
                            colsCount = 27;
                            break;
                        case "medium":
                            rowsCount = 25;
                            colsCount = 45;
                            break;
                            case "hard":
                            rowsCount = 35;
                            colsCount = 63;
                            break;
                        case "extreme":
                            rowsCount = 45;
                            colsCount = 81;
                            break;
                        default:
                            rowsCount = 0;
                            colsCount = 0;
                            break;
                    }
                    mazeLayout = new int[rowsCount][colsCount];
                    int row = 0; // Keeps track of the row index in the mazeLayout array
                    int col = 0; // Keeps track of the column index in the mazeLayout array
                    String mazeLayoutOut = saveFile.get("mazeLayout").toString(); // Gets and saves to a string the mazeLayout from saveFile
                    for (int i = 1; i < mazeLayoutOut.length(); i += 2) // += 2 to skip over commas in the string
                    {
                        if (i < mazeLayoutOut.length()) // Ensures no index out of bounds
                        {
                            mazeLayout[row][col] = Integer.parseInt(String.valueOf((mazeLayoutOut.charAt(i)))); // Stores as an integer the value of mazeLayoutOut at index i
                            col++; 
                        }
                        if (col == colsCount) // When the current column = the number of columns in each row, increment the row and reset the current column to 0. This is neccessary as reading a '1D' string into a 2D array
                        {
                            row++; 
                            col = 0;
                        }
                    }
                    removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                }
                else
                {
                    removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                    addObject(new DisplayText(" (ERROR) File '" + fileName + "' does not exist ", 35, Color.WHITE, Color.RED), 451, 30);
                }
            }
            else
            {
                removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
                addObject(new DisplayText(" (ERROR) No file selected ", 35, Color.WHITE, Color.RED), 451, 30);
            } 
        }
        catch (Exception e)
        {
            removeObjects(getObjectsAt(451, 30, DisplayText.class)); // Remove any old existing error messages on the screen
            addObject(new DisplayText(" (ERROR) failed to import file ", 35, Color.WHITE, Color.RED), 451, 30);
            System.out.println("(ERROR) failed to import file");
            e.printStackTrace();
        }
    }
    
    /** Get the name of a desired file from the user and display their input **/
    private String getFileName()
    {
        FileDialog fileDialog = new FileDialog(new JFrame(), "Select a file", FileDialog.LOAD); // Instantiate a file browser window to get a file from the user 
        fileDialog.setVisible(true); // Set the file browser window to visible so that the user can input their choice
        if (fileDialog.getFile() != null)
        {
            File file = new File(fileDialog.getDirectory() + fileDialog.getFile()); // Instantiate store a reference to the file selected by the user
            removeObjects(getObjectsAt(450, 297, DisplayText.class)); // Remove the existing text that was displaying previous file selection
            String fileName = file.getName();
            addObject(new DisplayText("File: " + cutStringToLength(fileName, 14), 25, Color.WHITE, null), 450, 297); // Display the name of the file chosen by the user cut down to 14 characters in length
            return fileName;
        }
        else
        {
            removeObjects(getObjectsAt(450, 297, DisplayText.class)); // Remove the existing text that was displaying previous file selection
            return null;
        }
    }
    
    /** Return a given string cut down to a given length with "..." added on the end if characters have been cut off from the end **/
    private String cutStringToLength(String string, int length)
    {
        if (string.length() > length)
        {
            return string.substring(0, length) + "..."; // return the string up to the nth character inclusive (where n = length) + "..."
        }
        else
        {
            return string;
        }
    }
}

