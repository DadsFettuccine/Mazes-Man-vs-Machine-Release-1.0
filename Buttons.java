import greenfoot.*; 
import javax.swing.JOptionPane;

/**
 * The Buttons class handles: 
   * All buttons and the consequent initialisation
   * The animation of all buttons
   * Checking for and responding to button presses
 * 
 * By WS 
 * Version: 22/05/2021
 */
public class Buttons extends Actor
{
    private String buttonType; // Stores the type of button for the object
    private GreenfootImage buttonImage; // Stores the default button image
    private GreenfootImage buttonHoverImage; // Stores the button image to be displayed when the mouse hovers over the button
    // Variable triggers for method execution in the screens class are below:
    public boolean saveMaze = false;
    public boolean importFile = false;;
    public boolean playFile = false;
    public boolean viewFile = false;
    public boolean nextPage = false;
    public boolean previousPage = false;
    
    /** Constructor for initialising the a Button object with its images based off of its type **/
    public Buttons(String buttonType)
    {
        this.buttonType = buttonType; // Store the given button type
        switch(buttonType) // Get image based off of button type
        {
            case "start":
                buttonImage = new GreenfootImage("startButton.png");
                break;
            case "back":
                buttonImage = new GreenfootImage("backButton.png");
                break;
            case "play":
                buttonImage = new GreenfootImage("playButton.png");
                break;
            case "playEasyMaze":
                buttonImage = new GreenfootImage("easyButton.png");
                break;
            case "playMediumMaze":
                buttonImage = new GreenfootImage("mediumButton.png");
                break;
            case "playHardMaze":
                buttonImage = new GreenfootImage("hardButton.png");
                break;
            case "playExtremeMaze":
                buttonImage = new GreenfootImage("extremeButton.png");
                break;
            case "saveMaze":
                buttonImage = new GreenfootImage("saveMazeButton.png");
                break;
            case "saves":
                buttonImage = new GreenfootImage("savesButton.png");
                break;
            case "importFile":
                buttonImage = new GreenfootImage("importButton.png");
                break;
            case "playFile": 
                buttonImage = new GreenfootImage("playButton.png");
                break;
            case "viewFile":
                buttonImage = new GreenfootImage("viewButton.png");
                break;
            case "scores":
                buttonImage = new GreenfootImage("scoresButton.png");
                break;
            case "easyScores":
                buttonImage = new GreenfootImage("easyButton.png");
                break;
            case "mediumScores":
                buttonImage = new GreenfootImage("mediumButton.png");
                break;
            case "hardScores":
                buttonImage = new GreenfootImage("hardButton.png");
                break;
            case "extremeScores":
                buttonImage = new GreenfootImage("extremeButton.png");
                break;
            case "next":
                buttonImage = new GreenfootImage("nextButton.png");
                break;
            case "previous":
                buttonImage = new GreenfootImage("previousButton.png");
                break;
            case "guide":
                buttonImage = new GreenfootImage("guideButton.png");
                break;
        }
        setImage(buttonImage);
        buttonHoverImage = new GreenfootImage(buttonImage); // Set the buttonHoverImage to the same image as buttonImage
        buttonHoverImage.scale(buttonHoverImage.getWidth() + buttonHoverImage.getWidth()/10, buttonHoverImage.getHeight() + buttonHoverImage.getHeight()/10); // Scale buttonHoverImage to be 10% taller and wider
    }
    
    /** This method is called for every act cycle when the program is running which occurs when the run or act button is pressed in the environment **/
    public void act() 
    {
        checkMouseOver();
        checkMouseClick();
    }
    
    /** Checks if the mouse is hovering over the button and sets the image based off of whether the mouse is hovering over the button **/
    private void checkMouseOver()
    {
        if (Greenfoot.mouseMoved(this)) // If the mouse has moved over the button
        {
            setImage(buttonHoverImage);
        }
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) // If the mouse has moved off of the button
        {
            setImage(buttonImage);
        }
    }
    
    /** Checks if a button has been clicked and executes relevant operations **/
    private void checkMouseClick()
    {
        if (Greenfoot.mouseClicked(this)) // If the button has been clicked
        {
            switch(buttonType) // Determine what action to take based off of the button type
            {
                case "start":
                    Greenfoot.setWorld(new Screens("mainMenu"));
                    break;
                case "back":
                    String currentScreen  = ((Screens) getWorld()).screenType; // Get and store the current screen type of the world
                    String parentScreen; // Variable to store the relevant parent screen (The screen that comes before the current screen)
                    switch (currentScreen) // Set parentScreen based off of current screen
                    {
                        case "mainMenu":
                            parentScreen = "start";
                            break;
                        case "selectDifficulty":
                            parentScreen = "mainMenu";
                            break;
                        case "easyMaze":
                            parentScreen = "selectDifficulty";
                            break;
                        case "mediumMaze":
                            parentScreen = "selectDifficulty";
                            break;
                        case "hardMaze":
                            parentScreen = "selectDifficulty";
                            break;
                        case "extremeMaze":
                            parentScreen = "selectDifficulty";
                            break;    
                        case "saves":
                            parentScreen = "mainMenu";
                            break; 
                        case "savedMaze":
                            parentScreen = "saves";
                            break;
                        case "scores":
                            parentScreen = "mainMenu";
                            break;    
                        case "easyScores":
                            parentScreen = "scores";
                            break;
                        case "mediumScores":
                            parentScreen = "scores";
                            break;
                        case "hardScores":
                            parentScreen = "scores";
                            break;
                        case "extremeScores":
                            parentScreen = "scores";
                            break;    
                        case "guide":
                            parentScreen = "mainMenu";
                            break;
                        default:
                            parentScreen = null;
                            break;
                    }
                    if (currentScreen != "easyMaze" && currentScreen != "mediumMaze" && currentScreen != "hardMaze" && currentScreen != "extremeMaze")
                    {
                        Greenfoot.setWorld(new Screens(parentScreen)); // Set the world to the parent screen
                    }
                    else if ((getWorld().getObjectsAt(450, 30, DisplayText.class)).size() != 0) // If there is text in the world indicating that the maze has already been saved
                    {
                        Greenfoot.setWorld(new Screens(parentScreen)); // Set the world to the parent screen
                    }
                    else
                    {
                        int reply; // Stores input of the user (If the user wanted to leave the session)
                        boolean completed = ((Screens)getWorld()).completed; // Gets and stores whether the player has completed the maze
                        if (!completed) 
                        {
                            reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave, this session will be lost?", "Leave session?", JOptionPane.YES_NO_OPTION); // Display a yes/no window and store the input to determine if the user wishes to leave the session
                        }
                        else
                        {
                            reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave without saving this maze? Your scores will still be affected.", "Leave without saving the maze?", JOptionPane.YES_NO_OPTION); // Display a yes/no window and store the input to determine if the user wishes to leave the session
                        }
                        if (reply == JOptionPane.YES_OPTION) // If the user selected yes
                        {
                            if (completed)
                            {
                                ((Screens)getWorld()).updateScoresList(false, null); // Update the scores list now that we know the scoresListObject will not have a corresponding file name becaue the maze is not being saved. This is not first time the method is being callied i.e. parameter = true, the fileName is not given i.e. parameter = null
                            }
                            Greenfoot.setWorld(new Screens(parentScreen));
                        }
                    }
                    break;
                case "play":
                    Greenfoot.setWorld(new Screens("selectDifficulty"));
                    break;
                case "playEasyMaze":
                    Greenfoot.setWorld(new Screens("easyMaze"));
                    break;
                case "playMediumMaze":
                    Greenfoot.setWorld(new Screens("mediumMaze"));
                    break;
                case "playHardMaze":
                    Greenfoot.setWorld(new Screens("hardMaze"));
                    break;
                case "playExtremeMaze":
                    Greenfoot.setWorld(new Screens("extremeMaze"));
                    break;
                case "saveMaze":
                    saveMaze = true;
                    break;
                case "saves":
                    Greenfoot.setWorld(new Screens("saves"));
                    break;            
                case "importFile":
                    importFile = true;
                    break;
                case "playFile":
                    playFile = true;
                    break;
                case "viewFile":
                    viewFile = true;
                    break;
                case "scores":
                    Greenfoot.setWorld(new Screens("scores"));
                    break;
                case "easyScores":
                    Greenfoot.setWorld(new Screens("easyScores"));
                    break;
                case "mediumScores":
                    Greenfoot.setWorld(new Screens("mediumScores"));
                    break;
                case "hardScores":
                    Greenfoot.setWorld(new Screens("hardScores"));
                    break;
                case "extremeScores":
                    Greenfoot.setWorld(new Screens("extremeScores"));
                    break;
                case "next":
                    nextPage = true;
                    break;
                case "previous":
                    previousPage = true;
                    break;
                case "guide":
                    Greenfoot.setWorld(new Screens("guide"));
                    break;
            }
        }
    }
}
