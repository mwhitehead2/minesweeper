/**
 * This file contains testing methods for the MineSweeper project.
 * These methods are intended to serve several objectives:
 * 1) provide an example of a way to incrementally test your code
 * 2) provide example method calls for the MineSweeper methods
 * 3) provide examples of creating, accessing and modifying arrays
 *    
 * Toward these objectives, the expectation is that part of the 
 * grade for the MineSweeper project is to write some tests and
 * write header comments summarizing the tests that have been written. 
 * Specific places are noted with FIXME but add any other comments 
 * you feel would be useful.
 * 
 * Some of the provided comments within this file explain
 * Java code as they are intended to help you learn Java.  However,
 * your comments and comments in professional code, should
 * summarize the purpose of the code, not explain the meaning
 * of the specific Java constructs.
 *    
 */

import java.util.Random;
import java.util.Scanner;


/**
 * This class contains a few methods for testing methods in the MineSweeper
 * class as they are developed. These methods are all private as they are only
 * intended for use within this class.
 * 
 * @author Jim Williams
 * @author Meghan Whitehead
 *
 */
public class TestMineSweeper {

    /**
     * This is the main method that runs the various tests. Uncomment the tests
     * when you are ready for them to run.
     * 
     * @param args  (unused)
     */
    public static void main(String [] args) {

        // Milestone 1
        // testing the main loop, promptUser and simplePrintMap, since they have
        // a variety of output, is probably easiest using a tool such as diffchecker.com
        // and comparing to the examples provided.
        testEraseMap();
        
        // Milestone 2
        testPlaceMines();
        testNumNearbyMines();
        testShowMines();
        testAllSafeLocationsSwept();
        
        // Milestone 3
        testSweepLocation();
        testSweepAllNeighbors();
        //testing printMap, due to printed output is probably easiest using a 
        //tool such as diffchecker.com and comparing to the examples provided.
    }
    
    /**
     * This is intended to run some tests on the eraseMap method. 
     * 1. A new char array is created  
     * 2. The MineSweeper.eraseMap method is called to change each element in 
     * 	  the array the UNSWEPT symbol
     * 3. A for loop is then created to check each element of the array. If the 
     *    array is the UNSWEPT symbol, the counter is incremented by one.
     * 4. Then there is are if statements to check if the counter is equal to 
     *	  the area of the array. If it is, the test has passed, if not, the test
     *    has failed.
     */
    private static void testEraseMap() {
        int counter = 0;
        char [][] testMap = new char[5][5];
        boolean error = false;
        int expectedCounter = testMap.length * testMap[0].length;
        
        MineSweeper.eraseMap(testMap);
        
        for (int row = 0; row < testMap.length; row++){
        	for (int col = 0; col < testMap[row].length; col++){
        		if (testMap[row][col] == Config.UNSWEPT)
        			counter += 1;
        	}
        }
        
        if (counter == expectedCounter){
        	error = false;
        }
        if (!(counter == expectedCounter)){
        	error = true;
        	System.out.println("Expected " + testMap.length * testMap[0].length);
        }
        
        if (error){
        	System.out.println("testEraseMap: failed");
        }
        if (!error){
        	System.out.println("testEraseMap: passed");
        }
    }      
    
    /*
     * Calculate the number of elements in array1 with different values from 
     * those in array2
     */
    private static int getDiffCells(boolean[][] array1, boolean[][] array2) {
        int counter = 0;
        for(int i = 0 ; i < array1.length; i++){
            for(int j = 0 ; j < array1[i].length; j++){
                if(array1[i][j] != array2[i][j]) 
                    counter++;
            }
        }
        return counter;
    }    
    
    /**
     * This runs some tests on the placeMines method. 
     * 1. A new boolean array is created using a random number generator
     * 2. The expected number of mines is set to 3
     * 3. The MineSweeper.placeMines method is called with the new array
     * 4. If the number of mines placed is not the same as the expected number 
     *    of mines, the test will fail
     * 5. The method getDiffCells will then be called to check if the array created 
     *    is the same as the expected array. If it is not, the test will fail
     * 6. If the number of mines placed is the same as the expected number of 
     *    mines, and the arrays match, the test will pass
     */
    private static void testPlaceMines() {
        boolean error = false;
        
        //These expected values were generated with a Random instance set with
        //seed of 123 and MINE_PROBABILITY is 0.2.
        boolean [][] expectedMap = new boolean[][]{
            {false,false,false,false,false},
            {false,false,false,false,false},
            {false,false,false,true,true},
            {false,false,false,false,false},
            {false,false,true,false,false}};
        int expectedNumMines = 3;
            
        Random studentRandGen = new Random( 123);
        boolean [][] studentMap = new boolean[5][5];
        int studentNumMines = MineSweeper.placeMines( studentMap, studentRandGen);
        
        if ( studentNumMines != expectedNumMines) {
            error = true;
            System.out.println("testPlaceMines 1: expectedNumMines=" + expectedNumMines + " studentNumMines=" + studentNumMines);
        }
        int diffCells = getDiffCells( expectedMap, studentMap);
        if ( diffCells != 0) {
            error = true;
            System.out.println("testPlaceMines 2: mine map differs.");
        }

        // Can you think of other tests that would make sure your method works correctly?
        // if so, add them.

        if (error) {
            System.out.println("testPlaceMines: failed");
        } else {
            System.out.println("testPlaceMines: passed");
        }        
        
    }
    
    /**
     * This runs some tests on the numNearbyMines method. 
     * 1. A new boolean array is created 
     * 2. The array is checked for the number of mines near the location of (1,1)
     * 3. If the number of mines nearby does not equal two, the test fails
     * 4. The array is then checked for the number of miens near the location of 
     *    (2,1)
     * 5. If the number of mines nearby does not equal 0, the test fails
     */
    private static void testNumNearbyMines() {
        boolean error = false;

        boolean [][] mines = new boolean[][]{
            {false,false,true,false,false},
            {false,false,false,false,false},
            {false,true,false,true,true},
            {false,false,false,false,false},
            {false,false,true,false,false}};
        int numNearbyMines = MineSweeper.numNearbyMines( mines, 1, 1);
        
        if ( numNearbyMines != 2) {
            error = true;
            System.out.println("testNumNearbyMines 1: numNearbyMines=" + numNearbyMines + " expected mines=2");
        }
        
       numNearbyMines = MineSweeper.numNearbyMines( mines, 2, 1);
        
        if ( numNearbyMines != 0) {
            error = true;
            System.out.println("testNumNearbyMines 2: numNearbyMines=" + numNearbyMines + " expected mines=0");
        }        
        
        // Can you think of other tests that would make sure your method works correctly?
        // if so, add them.

        if (error) {
            System.out.println("testNumNearbyMines: failed");
        } else {
            System.out.println("testNumNearbyMines: passed");
        }
    }
    
    /**
     * This runs some tests on the showMines method. 
     * 1. A new boolean array is created 
     * 2. A new char array is created to be the same size as the boolean 
     *    array, and some locations are changed to the UNSWEPT character in 
     *    the char array
     * 3. It is then checked to make sure the char locations with UNSWEPT do not 
     *    have mines, if they do the test fails
     * 4. It is then checked to make sure the char locations that have been assigned
     *    true values in the boolean array are represented by the HIDDEN_MINE character
     *    in the char array, if they do not the test fails
     */
    private static void testShowMines() {
        boolean error = false;
        

        boolean [][] mines = new boolean[][]{
            {false,false,true,false,false},
            {false,false,false,false,false},
            {false,true,false,false,false},
            {false,false,false,false,false},
            {false,false,true,false,false}};
            
        char [][] map = new char[mines.length][mines[0].length];
        map[0][2] = Config.UNSWEPT;
        map[2][1] = Config.UNSWEPT;
        map[4][2] = Config.UNSWEPT;
        
        MineSweeper.showMines( map, mines);
        if ( !(map[0][2] == Config.HIDDEN_MINE && map[2][1] == Config.HIDDEN_MINE && map[4][2] == Config.HIDDEN_MINE)) {
            error = true;
            System.out.println("testShowMines 1: a mine not mapped");
        }
        if ( map[0][0] == Config.HIDDEN_MINE || map[0][4] == Config.HIDDEN_MINE || map[4][4] == Config.HIDDEN_MINE) {
            error = true;
            System.out.println("testShowMines 2: unexpected showing of mine.");
        }
        
        // Can you think of other tests that would make sure your method works correctly?
        // if so, add them.

        if (error) {
            System.out.println("testShowMines: failed");
        } else {
            System.out.println("testShowMines: passed");
        }        
    }    
    
    /**
     * This is intended to run some tests on the allSafeLocationsSwept method.
     * 1. A new char and new boolean array are created 
     * 2. The MineSweeper.allSafeLocationsSwept method is then called to see if
     *    all the non-mine locations have been swept
     * 3. If locationsSwept = true, the test fails because not all safe locations
     *    have been swept
     * 4. If locationsSwept = false, the test passes because not all safe locations
     *    have been swept
     */
    private static void testAllSafeLocationsSwept() {
        boolean error = false;
    	
    	char [][] testMap = new char[][]{
        	{'.','.','.','1',' '},
        	{'1','.','2','1',' '},
        	{'1','.','.',' ',' '}
        };
        boolean [][] testMapBoolean = new boolean[][]{
        	{false,false,true,false,false},
            {false,false,false,false,false},
            {false,true,false,false,false}
        };
        
        boolean locationsSwept = MineSweeper.allSafeLocationsSwept(testMap, testMapBoolean);
        
        if (locationsSwept){
        	error = true;
        }
        if (!locationsSwept){
        	error = false;
        }
        
        if (error){
        	System.out.println("testAllSafeLocationsSwept: failed");
        	
        }
        if (!error){
        	System.out.println("testAllSafeLocationsSwept: passed");
        }
    }      

    
    /**
     * This is intended to run some tests on the sweepLocation method. 
     * 1. A new char and new boolean array are created 
     * 2. The method MineSweeper.sweepLocation is then called, and the return
     *    value is then assigned to the int numNearbyMines
     * 3. If the map location is out of bounds and the value returned is -3,
     *    the test will pass
     * 4. If the map location has a mine, and the return value is -1, the test
     *    will pass
     * 5. If the map location has already been swept and the return value is -2,
     *    the test will pass
     * 6. If the map location does not have a mine and the location has been swept,
     *    the test will pass 
     */
    private static void testSweepLocation() {
    	int row = -1;
    	int col = 3;
    	boolean error = true;
    	
    	char [][] testMap = new char[][]{
        	{'.','.','.','1',' '},
        	{'1','.','2','1',' '},
        	{'1','.','.',' ',' '}
        };
        boolean [][] testMapBoolean = new boolean[][]{
        	{false,false,true,false,false},
            {false,false,false,false,false},
            {false,true,false,false,false}
        };
        
        int numNearbyMines = MineSweeper.sweepLocation(testMap, testMapBoolean, row, col);
        
        if ((numNearbyMines == -3) && ((row < 0 || row >= testMap.length) || 
        		(col < 0 || col >= testMap[0].length))){
        	error = false;		
        }
        else{
	        
        	if ((numNearbyMines == -1) && (testMapBoolean[row][col])){
	        	error = false;
	        }
	        else if ((numNearbyMines == -2) && (testMap[row][col] == Config.NO_NEARBY_MINE)){
	        	error = false;
	        }
	        
	        else if ((testMap[row][col] == Config.NO_NEARBY_MINE) && (!testMapBoolean[row][col])){
	        	error = false;
	        }
        }
        if (error){
        	System.out.println("testSweepLocation: failed");
        }
        if (!error){
        	System.out.println("testSweepLocation: passed");
        }
    }      
    
    /**
     * This is intended to run some tests on the sweepAllNeighbors method. 
     * 1. A new char and new boolean array have been created
     * 2. The for loops will go through each location on the map, and find those
     *    that are surrounding the chosen location
     * 3. It then checks to see that the character in that is greater than 0, a 
     *    swept character, or is not a mine. If any are true, the test passes 
     */
    private static void testSweepAllNeighbors() {
    	int row = 1;
    	int col = 1;
    	boolean error = true;

    	char [][] testMap = new char[][]{
    		{'.','.','.','1',' '},
    		{'1','.','2','1',' '},
    		{'1','.','.',' ',' '}
    	};
    	boolean [][] testMapBoolean = new boolean[][]{
    		{false,false,true,false,false},
    		{false,false,false,false,false},
    		{false,true,false,false,false}
    	};

    	for (int i = 0; i < testMap.length; i++){
    		for (int j = 0; j < testMap[i].length; j++){
    			MineSweeper.sweepAllNeighbors(testMap, testMapBoolean, row, col);
    			if ((i >= 0 && i < testMap.length) && (j >= 0 && j < testMap[0].length)) {
    				if (!(i == row && j == col)) {
    					if (!(testMap[row][col] > '0') || !(testMapBoolean[row][col]) || (testMap[row][col] == ' ')){
    							error = false;	
    					}
    				}
    			}
    		}
    	}
    	
    	if (error){
			System.out.println("testSweepAllNeighbors: failed");
		}
		if (!error){
			System.out.println("testSweepallNeighbors: passed");
		}
    }
}

