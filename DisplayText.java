import greenfoot.*; 

/**
 * The DisplayText class handles: 
   * All text to be displayed and the consequent initialisation
   * The setting/updating of a DisplayText object's image to account for the desired text to be displayed
 * 
 * By WS
 * Version: 22/05/2021
 */
public class DisplayText extends Actor
{    
    /** Constructor for initialising a displayText Object with an image that corresponds to the desired text to be displayed, text size, text colour and background colour **/
    public DisplayText(String textToDisplay, int textSize, Color textColour, Color backgroundColour)
    {
        GreenfootImage textImage = new GreenfootImage(textToDisplay, textSize, textColour, backgroundColour); // Create and store the corresponding GreenfootImage for the desired text to be displayed
        setImage(textImage);
    }
    
    /** Method to update the image for the displayText object with the new desired text to be displayed, text size, text colour and background colour **/
    public void updateText(String textToDisplay, int textSize, Color textColour, Color backgroundColour)
    {
        GreenfootImage textImage = new GreenfootImage(textToDisplay, textSize, textColour, backgroundColour); // Create and store the corresponding GreenfootImage for the desired text to be displayed
        setImage(textImage);
    }
}
