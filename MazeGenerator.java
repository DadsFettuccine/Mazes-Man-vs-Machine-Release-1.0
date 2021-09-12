import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * The MazeGenerator class handles: 
   * MazeGenerator objects and the consequent initialisation
   * The generating of all maze maps
 * 
 * By WS
 * Version: 22/05/2021
 */
public class MazeGenerator
{
    private int[][] mazeLayout; // Array storing/representing the cell based maze layout
    private int endRow; // Row index of end cell in mazeLayout
    private int endCol; // Column index of end cell in mazeLayout
    private int currentRow; // Row index of the current cell in mazeLayout
    private int currentCol; // Column index of the current cell in mazeLayout
    private int unvisitedCellsCount; // The number of cells that are unvisited by the algorithm
    private List <String> unvisitedNeighbouringCells = new ArrayList<String>(); // Stores a list of unvisited neighbouring cells to the current cell
    private Stack<Integer> previouslyVisitedRows = new Stack<Integer>(); // Row indexes for mazeLayout of the cells of the path taken
    private Stack<Integer> previouslyVisitedCols = new Stack<Integer>(); // Column indexes for mazeLayout of the cells of the path taken

    /** Constructor for instantiating a MazeGenerator object */
    public MazeGenerator(String difficulty)
    {
        switch (difficulty) // Sets the size of the mazeLayout array (i.e. the size of the maze) based off of the difficulty given
        {
            case "easy":
                mazeLayout = new int[15][27];
                break;
            case "medium":
                mazeLayout = new int[25][45];
                break;
            case "hard":
                mazeLayout = new int[35][63];
                break;
            case "extreme":
                mazeLayout = new int[45][81];
                break;
        }
        unvisitedCellsCount = (((mazeLayout.length - 3) / 2) + 1) * (((mazeLayout[0].length - 3) / 2) + 1); // Calculates the number of cells in the array and sets this to the unvisitedCellsCount
        endRow = mazeLayout.length - 2; // Calculates and stores the row index of end cell in mazeLayout, which is in the second last row
        endCol = mazeLayout[0].length - 2; // Calculates and stores the column index of end cell in mazeLayout, which is in the second last column
        initialiseMap();
    }
    
    /** Creates the initial array of cells to base the maze generation off of **/
    private void initialiseMap()
    {
        for (int row = 0; row < mazeLayout.length; row++) 
        {
            for (int col = 0; col < mazeLayout[row].length; col++) 
            {
                if (row % 2 == 0 || col % 2 == 0)
                {
                    mazeLayout[row][col] = 1; // 1 represents a cell that is a wall
                }
                else
                {
                    mazeLayout[row][col] = 0; // 0 represents a cell that is a path
                }
            }
        }
    }
    
    /** The method used to generate the maze - recursive backtracker algorithm **/
    public int[][] generateMaze()
    {
        currentRow = 1; // Sets the current row = 1 as this is the row index in the mazeLayout array of the starting cell
        currentCol = 1; // Sets the current column  = 1 as this is the column index in the mazeLayout array of the starting cell
        mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
        unvisitedCellsCount--;
        while (unvisitedCellsCount != 0) // While there are still univisted cells
        {
            getUnvisitedNeighbouringCells();
            if (unvisitedNeighbouringCells.size() > 0)
            {
                moveToRandomUnivistedNeighbouringCell();
                previouslyVisitedRows.push(currentRow); // Add to the previouslyVisitedRows stack the current row
                previouslyVisitedCols.push(currentCol); // Add to the previouslyVisitedCols stack the current column
                mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
                unvisitedCellsCount--;
            }
            else
            {
                currentRow = previouslyVisitedRows.pop(); // Get the top value off of the previouslyVisitedRows stack and store it in currentRow
                currentCol = previouslyVisitedCols.pop(); // Get the top value off of the previouslyVisitedCols stack and store it in currentCol
            }
        }
        mazeLayout[endRow][endCol] = 3; // 3 represents the end cell
        return mazeLayout;
    }
   
    /** Check for neighbouring cells that are unvisited and add them to unvisitedNeighbouringCells list **/
    private void getUnvisitedNeighbouringCells()
    {
        unvisitedNeighbouringCells.clear();
        if (notVisited(currentRow - 2, currentCol)) // If the neighbouring cell north of the current cell is unvisited
        {
            unvisitedNeighbouringCells.add("North");
        }
        if (notVisited(currentRow + 2, currentCol)) // If the neighbouring cell south of the current cell is unvisited
        {
            unvisitedNeighbouringCells.add("South");
        }
        if (notVisited(currentRow, currentCol + 2)) // If the neighbouring cell east of the current cell is unvisited
        {
            unvisitedNeighbouringCells.add("East");
        }
        if (notVisited(currentRow, currentCol - 2)) // If the neighbouring cell west of the current cell is unvisited
        {
            unvisitedNeighbouringCells.add("West");
        }
    }

    /** Returns whether or not a given cell is visited, where the cell is represented by the row and column index in the mazeLayout array **/
    private boolean notVisited(int row, int col)
    {
        try
        {
            if (mazeLayout[row][col] == 2) // 2 represents that the cell has been visited
            {
                return false;
            }
            else
            {
                return true; 
            }
        }
        catch (ArrayIndexOutOfBoundsException e) // Catch case where on the edge cell and checking outside of the array
        {
            return false;
        }
    }
    
    /** Selects a random neighbouring cell from the unvisitedNeighbouringCells list and moves to it **/
    private void moveToRandomUnivistedNeighbouringCell()
    {
        switch (unvisitedNeighbouringCells.get(((int) (Math.random() * (unvisitedNeighbouringCells.size()))))) // Gets a random value in the unvisitedNeighbouringCells list
        {
            case "North":
                currentRow -= 2; // Set the current cell to the neighbouring cell north of the current cell
                mazeLayout[currentRow + 1][currentCol] = 2; // Sets the wall between the current cell and the previous cell to be a visited path
                mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
                break;
            case "South":
                currentRow += 2; // Set the current cell to the neighbouring cell south of the current cell
                mazeLayout[currentRow -1 ][currentCol] = 2; // Sets the wall between the current cell and the previous cell to be a visited path
                mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
                break;
            case "East":
                currentCol += 2; // Set the current cell to the neighbouring cell east of the current cell
                mazeLayout[currentRow][currentCol - 1] = 2; // Sets the wall between the current cell and the previous cell to be a visited path
                mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
                break;
            case "West": 
                currentCol -= 2; // Set the current cell to the neighbouring cell west of the current cell
                mazeLayout[currentRow][currentCol + 1] = 2; // Sets the wall between the current cell and the previous cell to be a visited path
                mazeLayout[currentRow][currentCol] = 2; // Sets the current cell to visited (2 represents a visited cell)
                break;
        }
    }
}
