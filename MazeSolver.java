import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The MazeSolver class handles: 
   * MazeSolver objects and the consequent initialisation
   * The solving of all mazes
 * 
 * By WS
 * Version: 22/05/2021
 */
public class MazeSolver  
{
    private int[][] mazeLayout; // Array storing/representing the cell based maze layout
    private int endRow; // Row index of end cell in mazeLayout
    private int endCol; // Column index of end cell in mazeLayout
    private Queue<int[]> cellQueue = new LinkedList<>(); // Queue of cells representing the cells to be visited
    private int[] currentCell; // Stores the row and column index of mazeLayout for the current cell
    private int[][][] parentCell; // Stores the parent cell's row and column index of mazeLayout for a given cell in mazeLayout
    public ArrayList<Integer> solutionPathTakenRows = new ArrayList<>(); // Stores the row indexes of mazeLayout for the cells of the solution path
    public ArrayList<Integer> solutionPathTakenCols = new ArrayList<>(); // Stores the column indexes of mazeLayout for the cells of the solution path

    /** Constructor for instantiating a MazeSolver object */
    public MazeSolver(int[][] mazeLayout)
    {
        int rowsCount = mazeLayout.length; // Stores number of rows in the mazeLayout array
        int colsCount = mazeLayout[0].length; // Stores number of columns in the mazeLayout array
        this.mazeLayout = new int[rowsCount][colsCount]; // Sets the mazeLayout array in this class to have the same size as the mazeLayout array being passed in 
        for (int row = 0; row < rowsCount; row++)
        {
            for (int col = 0; col < colsCount; col++)
            {
                this.mazeLayout[row][col] = mazeLayout[row][col]; // Cycles through the passed in mazeLayout array and stores its values to the mazeLayout array variable in this class to ensure the original mazeLayout that was passed in is not altered
            }
        }
        endRow = rowsCount - 2; // Calculates and stores the row index of end cell in mazeLayout, which is in the second last row
        endCol = colsCount - 2; // Calculates and stores the column index of end cell in mazeLayout, which is in the second last column
        parentCell = new int[rowsCount - 1][colsCount - 1][]; // Instantiate the parentCell array for the cells in mazeLayout
    }

    /** Calculates the shortest path to solve the maze -  breadth first search algorithm **/
    public void findShortestSolutionPath()
    {
        cellQueue.add(new int[] {1, 1}); // add row/col indexs of start
        mazeLayout[1][1] = 4; // mark row/col indexes of start as visited - 4 means it has been visited by maze solver
        boolean completed = false; // Stores if the maze solver has completed the maze
        while (!completed)
        {
            currentCell = cellQueue.remove(); // Set the current cell to the next cell in the queue and remove it from the queue
            exploreUnvisitedNeighbouringCells(currentCell[0], currentCell[1]); // Explore the unvisited neighbouring cells to the current cell
            if (currentCell[0] == endRow && currentCell[1] == endCol) // If the current cell is the end cell
            {
                completed = true;
            }
        }
        reconstructPath();
    }

    /** Explores the unvisited neighbours of a cell, adding them to the queue, marking them as visited and noting their parent cell **/
    private void exploreUnvisitedNeighbouringCells(int row, int col) 
    {
        if (notVisited(row - 1, col)) // If the cell north of the current cell is unvisited
        {
            cellQueue.add(new int[] {row - 1, col}); // Add the cell north of the current cell to the cell queue
            parentCell[row - 1][col] = currentCell; // Set the parent cell of the cell north of the current cell to the current cell
            mazeLayout[row - 1][col] = 4; // Mark the cell north of the current cell as visited (4 = visited cell)
        }
        if (notVisited(row + 1, col)) // If the cell south of the current cell is unvisited
        {
            cellQueue.add(new int[] {row + 1, col}); // Add the cell south of the current cell to the cell queue
            parentCell[row + 1][col] = currentCell; // Set the parent cell of the cell south of the current cell to the current cell
            mazeLayout[row + 1][col] = 4; // Mark the cell south of the current cell as visited (4 = visited cell)
        }
        if (notVisited(row, col + 1)) // If the cell east of the current cell is unvisited
        {
            cellQueue.add(new int[] {row, col + 1}); // Add the cell east of the current cell to the cell queue
            parentCell[row][col + 1] = currentCell; // Set the parent cell of the cell east of the current cell to the current cell
            mazeLayout[row][col + 1] = 4; // Mark the cell east of the current cell as visited (4 = visited cell)
        }
        if (notVisited(row, col - 1)) // If the cell west of the current cell is unvisited
        {
            cellQueue.add(new int[] {row, col - 1}); // Add the cell west of the current cell to the cell queue
            parentCell[row][col - 1] = currentCell; // Set the parent cell of the cell west of the current cell to the current cell
            mazeLayout[row][col - 1] = 4; // Mark the cell west of the current cell as visited (4 = visited cell)
        }
    }
    
    /** Returns whether or not a given cell is visited, where the cell is represented by the row and column index in the mazeLayout array **/
    private boolean notVisited(int row, int col)
    {
        if (mazeLayout[row][col] == 4 || mazeLayout[row][col] == 1 ) // 4 represents that the cell has been visited, 1 represents the cell is a wall and we don't want to move into walls
        {
            return false;
        }
        else
        {
            return true; 
        }
    }
    
    /** Reconstruct the path that the maze solver took and store it **/
    private void reconstructPath()
    {
        currentCell = new int[] {endRow, endCol}; // Set the current cell to the end cell
        while(parentCell[currentCell[0]][currentCell[1]] != null) // While there is a parent cell of the current cell
        {
            solutionPathTakenRows.add(currentCell[0]); // Add the row index of mazeLayout for the current cell
            solutionPathTakenCols.add(currentCell[1]); // Add the column index of mazeLayout for the current cell
            currentCell = parentCell[currentCell[0]][currentCell[1]]; // Set the current cell to the parent cell of the current cell
        }
        solutionPathTakenRows.add(1); // Add the row index of mazeLayout for the starting cell
        solutionPathTakenCols.add(1); // Add the column index of mazeLayout for the starting cell
        Collections.reverse(solutionPathTakenRows); // Reverse the order of the solutionPathTakenRows list
        Collections.reverse(solutionPathTakenCols); // Reverse the order of the solutionPathTakenCols list
    }
}
