//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Mine Sweeper
// Files:           
// Course:          CS200, Fall 2017
//
// Author:          Meghan Whitehead
// Email:           mwhitehead2@wisc.edu 
// Lecturer's Name: Jim Williams
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.  If you received no outside help from either type of 
// source, then please explicitly indicate NONE.
//
// Persons:         (identify each person and describe their help in detail)
// Online Sources:  (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

	/**
	 * This is the main method for Mine Sweeper game! This method contains the
	 * within game and play again loops and calls the various supporting
	 * methods.
	 * 
	 * @param args
	 *            (unused)
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Random randGen = new Random(Config.SEED);

		int constant = 1; // used for the min when asking for a row or column
		String anotherGame; // used in the loop prompting player for game information
		boolean gameEnd = false; // used to keep the game loop going until the player wins/loses

		System.out.println("Welcome to Mine Sweeper!"); 

		/* 
		 * The following strings contain the prompts sent to the prompt user
		 * method to get info on height/width of game board and row/column of location
		 * to sweep
		 */
		String widthPrompt = "What width of map would you like (" + Config.MIN_SIZE + " - " + 
				Config.MAX_SIZE + "): ";
		String heightPrompt = "What height of map would you like (" + Config.MIN_SIZE + " - " + 
				Config.MAX_SIZE + "): ";
		String rowPrompt = "row: ";
		String columnPrompt = "column: ";

		/*
		 * The following do while loop is used to prompt the player for needed
		 * variables, prints the game map, and continues to do so until the
		 * player tells the computer that they do not want to play another game
		 */
		do {
			// the following ints (height/width) are used to create the size of the game board
			int mapWidth = promptUser(in, widthPrompt, Config.MIN_SIZE, Config.MAX_SIZE);
			int mapHeight = promptUser(in, heightPrompt, Config.MIN_SIZE, Config.MAX_SIZE);
			
			// this array is created based on the ints (height/width) to show the game board
			char[][] map = new char[mapHeight][mapWidth];

			System.out.println();

			// this array is created to show where the mines are located (true = mine)
			boolean[][] mines = new boolean[mapHeight][mapWidth];
			// the random number generator is used to randomly place mines within the array
			int minesPlaced = placeMines(mines, randGen); 
			System.out.println("Mines: " + minesPlaced);

			eraseMap(map);
			printMap(map);

			/*
			 * The following do while loop is used as the game loop. The player
			 * is prompted to choose a location. Depending on the chosen
			 * location, it will either end the game and show the mines, or
			 * display the number of mines surrounding that location.
			 */
			do {
				int row = promptUser(in, rowPrompt, constant, mapHeight); 
				int column = promptUser(in, columnPrompt, constant, mapWidth);

				/*
				 * The following converts the int value returned by numNearbyMines to a
				 * char that will be displayed on the printed map as the number
				 * of mines surrounding that location. row and column are
				 * decreased by one to account for the array of the game board
				 * starting at zero, while the player starts it at one.
				 */
				char numMines = (char) ('0' + numNearbyMines(mines, row - 1, column - 1));
				
				/*
				 * The following if/else statement will call the sweepAllNeightbors 
				 * method if the location has zero mines around it (and changes the player's
				 * chosen location to the NO_NEARBY_MINE symbol), and calls the 
				 * sweepLocation method if it is not zero.
				 */
				if (numMines == '0') {
					sweepAllNeighbors(map, mines, row - 1, column - 1);
					map[row - 1][column - 1] = Config.NO_NEARBY_MINE;
				} 
				else if (numMines > '0') {
					sweepLocation(map, mines, row - 1, column - 1);
				}
				else {
					promptUser(in, rowPrompt, constant, mapHeight);
					promptUser(in, columnPrompt, constant, mapWidth);
				}

				/*
				 * Each time the player chooses a location, the if else
				 * statement will check to see if that location is a mine. If
				 * the location is a mine, it will print the game board with all
				 * the mines and tell the user they have lost. If the location
				 * is not a mine, it will continue the game.
				 */
				if (mines[row - 1][column - 1] == true) {
					gameEnd = true;
					showMines(map, mines);
					map[row - 1][column - 1] = Config.SWEPT_MINE;
					printMap(map);
					System.out.println("Sorry, you lost.");
				} else {
					gameEnd = allSafeLocationsSwept(map, mines);
					/*
					 * The following if else statement checks to see if all of
					 * the non-mine locations have been found. If they have,
					 * then the player wins the game. If they have not, the game
					 * continues.
					 */
					if (gameEnd) {
						showMines(map, mines);
						printMap(map);
						System.out.println("You Win!");
					} else {
						System.out.println();
						System.out.println("Mines: " + minesPlaced);
						printMap(map);
					}
					sweepLocation(map, mines, row - 1, column - 1);
				}

			} while (!gameEnd);

			System.out.print("Would you like to play again (y/n)? ");
			anotherGame = in.nextLine();
			anotherGame = anotherGame.trim();
	
		} while (!(anotherGame.charAt(0) == 'n') && 
				(anotherGame.charAt(0) == 'y' || anotherGame.charAt(0) == 'Y'));

		System.out.println("Thank you for playing Mine Sweeper!");

	}

	/**
	 * This method prompts the user for a number, verifies that it is between
	 * min and max, inclusive, before returning the number.
	 * 
	 * If the number entered is not between min and max then the user is shown
	 * an error message and given another opportunity to enter a number. If min
	 * is 1 and max is 5 the error message is: Expected a number from 1 to 5.
	 * 
	 * If the user enters characters, words or anything other than a valid int
	 * then the user is shown the same message. The entering of characters other
	 * than a valid int is detected using Scanner's methods (hasNextInt) and
	 * does not use exception handling.
	 * 
	 * Do not use constants in this method, only use the min and max passed in
	 * parameters for all comparisons and messages. Do not create an instance of
	 * Scanner in this method, pass the reference to the Scanner in main, to
	 * this method. The entire prompt should be passed in and printed out.
	 *
	 * @param in
	 *            The reference to the instance of Scanner created in main.
	 * @param prompt
	 *            The text prompt that is shown once to the user.
	 * @param min
	 *            The minimum value that the user must enter.
	 * @param max
	 *            The maximum value that the user must enter.
	 * @return The integer that the user entered that is between min and max,
	 *         inclusive.
	 */
	public static int promptUser(Scanner in, String prompt, int min, int max) {
		System.out.print(prompt);

		boolean validInt = false; // used to keep the loop going/stop depending on user input
		int playerInt = 0; // variable returned to main method as player's input

		/*
		 * The following do while method checks to see if the player's input is
		 * an integer and is within the given max and min, and will continue to ask
		 * the player for a different input until it meets the requirements.
		 */
		do {
			if (in.hasNextInt()) {
				int userInt = in.nextInt();
				in.nextLine();
				if (userInt >= min && userInt <= max) {
					validInt = true;
					playerInt = userInt;
				} else {
					System.out.println("Expected a number from " + min + " to " + max + ".");
				}
			} else {
				in.nextLine();
				System.out.println("Expected a number from " + min + " to " + max + ".");
			}
		} while (!validInt);

		return playerInt; // returns user specified int
	}

	/**
	 * This initializes the map char array passed in such that all elements have
	 * the Config.UNSWEPT character. Within this method only use the actual size
	 * of the array. Don't assume the size of the array. This method does not
	 * print out anything. This method does not return anything.
	 * 
	 * @param map
	 *            An allocated array. After this method call all elements in the
	 *            array have the same character, Config.UNSWEPT.
	 */
	public static void eraseMap(char[][] map) {

		// changes each value in the passed in map array to the UNSWEPT character
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				map[row][col] = Config.UNSWEPT;
			}
		}

		return; // no return value
	}

	/**
	 * This prints out a version of the map without the row and column numbers.
	 * A map with width 4 and height 6 would look like the following: . . . . .
	 * . . . . . . . . . . . . . . . . . . . For each location in the map a
	 * space is printed followed by the character in the map location.
	 * 
	 * @param map
	 *            The map to print out.
	 */
	public static void simplePrintMap(char[][] map) {
		printMap(map);

		/*
		 * This for loop prints the array sent to this method with a 
		 * space before the symbol to create the game board
		 */
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(" ");
				System.out.print(map[i][j]);
			}
			System.out.println("");
		}

		return; // No return value for this method.
	}

	/**
	 * This prints out the map. This shows numbers of the columns and rows on
	 * the top and left side, respectively. map[0][0] is row 1, column 1 when
	 * shown to the user. The first column, last column and every multiple of 5
	 * are shown.
	 * 
	 * To print out a 2 digit number with a leading space if the number is less
	 * than 10, you may use: System.out.printf("%2d", 1);
	 * 
	 * @param map
	 *            The map to print out.
	 */
	public static void printMap(char[][] map) {
		System.out.print("  ");
		
		/*
		 * The following for loop places the column numbers above the game board.
		 * It will print the first column, last column, and each column that is a 
		 * multiple of 5. Between those numbers, two dashes will be printed.
		 */
		for (int col = 0; col < map[0].length; col++) {

			if (col == 0) {
				System.out.print(" 1");
			} else if (col == (map[0].length - 1)) {
				if ((col + 1) >= 10) {
					System.out.print(map[0].length);
				} else {
					System.out.print(" " + (map[0].length));
				}
			} else if ((col + 1) % 5 == 0) {
				if ((col + 1) < 10) {
					System.out.print(" " + (col + 1));
				} else {
					System.out.print(col + 1);
				}
			} else {
				System.out.print("--");
			}
		}

		System.out.println("");
		
		/*
		 * The following for loop places the row numbers to the left of the game board.
		 * It will print the first row, last row, and each row that is a 
		 * multiple of 5. Between those numbers, ' |' will be printed.
		 */
		for (int row = 0; row < map.length; row++) {
			if (row == 0) {
				System.out.print(" 1");
			} else if (row == (map.length - 1)) {
				System.out.println("");
				if (row >= 10) {
					System.out.print(map.length);
				} else {
					System.out.print(" " + (map.length));
				}
			} else if ((row + 1) % 5 == 0) {
				System.out.println("");
				if ((row + 1) < 10) {
					System.out.print(" " + (row + 1));
				} else {
					System.out.print(row + 1);
				}
			} else {
				System.out.println("");
				System.out.print(" |");
			}
			for (int column = 0; column < map[0].length; column++) {
				System.out.print(" " + map[row][column]);
			}
		}

		System.out.println("");

		return; // No return value.
	}

	/**
	 * This method initializes the boolean mines array passed in. A true value
	 * for an element in the mines array means that location has a mine, false
	 * means the location does not have a mine. The MINE_PROBABILITY is used to
	 * determine whether a particular location has a mine. The randGen parameter
	 * contains the reference to the instance of Random created in the main
	 * method.
	 * 
	 * Access the elements in the mines array with row then column (e.g.,
	 * mines[row][col]).
	 * 
	 * Access the elements in the array solely using the actual size of the
	 * mines array passed in, do not use constants.
	 * 
	 * A MINE_PROBABILITY of 0.3 indicates that a particular location has a 30%
	 * chance of having a mine. For each location the result of
	 * randGen.nextFloat() < Config.MINE_PROBABILITY determines whether that
	 * location has a mine.
	 * 
	 * This method does not print out anything.
	 * 
	 * @param mines
	 *            The array of boolean that tracks the locations of the mines.
	 * @param randGen
	 *            The reference to the instance of the Random number generator
	 *            created in the main method.
	 * @return The number of mines in the mines array.
	 */
	public static int placeMines(boolean[][] mines, Random randGen) {
		int minesPlaced = 0;

		/*
		 * The following for loop randomly places mines within a boolean array
		 * by using a random number generator. If the number produced by the
		 * random number generator is less than the set MINE_PROBABILITY then it
		 * will set the boolean to true, which means there is a mine at that
		 * location in the mines array.
		 */
		for (int row = 0; row < mines.length; row++) {
			for (int col = 0; col < mines[0].length; col++) {
				boolean randMine = (randGen.nextFloat() < Config.MINE_PROBABILITY);
				mines[row][col] = randMine;
				if (randMine) {
					// return value that tells the player the number of mines
					minesPlaced += 1; 
				}
			}
		}

		return minesPlaced; // number of mines places on player's game board returned
	}

	/**
	 * This method returns the number of mines in the 8 neighboring locations.
	 * For locations along an edge of the array, neighboring locations outside
	 * of the mines array do not contain mines. This method does not print out
	 * anything.
	 * 
	 * If the row or col arguments are outside the mines array, then return -1.
	 * This method (or any part of this program) should not use exception
	 * handling.
	 * 
	 * @param mines
	 *            The array showing where the mines are located.
	 * @param row
	 *            The row, 0-based, of a location.
	 * @param col
	 *            The col, 0-based, of a location.
	 * @return The number of mines in the 8 surrounding locations or -1 if row
	 *         or col are invalid.
	 */
	public static int numNearbyMines(boolean[][] mines, int row, int col) {
		int nearbyMines = 0;
		
		// if the input is not valid, -1 will be returned
		if ((row < 0 && row > mines.length) || (col < 0 && col > mines[0].length)) {
			return -1;
		}

		/*
		 * The following for loop tells the player how many mines are
		 * surrounding the location that they chose. It checks eight locations
		 * directly around the player's chosen location and returns the total
		 * number of mines.
		 */
		for (int gameRow = row - 1; gameRow <= row + 1; gameRow++) {
			for (int gameCol = col - 1; gameCol <= col + 1; gameCol++) {
				if ((gameRow >= 0 && gameRow < mines.length) && 
						(gameCol >= 0 && gameCol < mines[gameRow].length)) {
					if ((mines[gameRow][gameCol]) && !(gameRow == row && gameCol == col)) {
						// return value incremented by one each time a mine is found
						nearbyMines += 1;
					}
				}
			}
		}

		return nearbyMines; // returns the number of mines surrounding the player's location
	}

	/**
	 * This updates the map with each unswept mine shown with the
	 * Config.HIDDEN_MINE character. Swept mines will already be mapped and so
	 * should not be changed. This method does not print out anything.
	 * 
	 * @param map
	 *            An array containing the map. On return the map shows unswept
	 *            mines.
	 * @param mines
	 *            An array indicating which locations have mines. No changes are
	 *            made to the mines array.
	 */
	public static void showMines(char[][] map, boolean[][] mines) {

		/*
		 * The following for loop changes each mine to show the HIDDEN_MINE
		 * symbol after the game has ended
		 */
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				if (mines[row][col] == true) {
					map[row][col] = Config.HIDDEN_MINE;
				}
			}
		}

		return; // no return value
	}

	/**
	 * Returns whether all the safe (non-mine) locations have been swept. In
	 * other words, whether all unswept locations have mines. This method does
	 * not print out anything.
	 * 
	 * @param map
	 *            The map showing touched locations that is unchanged by this
	 *            method.
	 * @param mines
	 *            The mines array that is unchanged by this method.
	 * @return whether all non-mine locations are swept.
	 */
	public static boolean allSafeLocationsSwept(char[][] map, boolean[][] mines) {
		boolean locationsSwept = true;

		/*
		 * The following for loop checks to see if the player has chosen all of
		 * the locations on the game board that are not mines. If there are
		 * still locations without mines, the return value is false, otherwise
		 * the return value is true and the player wins the game.
		 */
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				if ((map[row][col] == Config.UNSWEPT) && (mines[row][col] == false)) {
					locationsSwept = false;
				}
			}
		}

		return locationsSwept;

	}

	/**
	 * This method sweeps the specified row and col. - If the row and col
	 * specify a location outside the map array then return -3 without changing
	 * the map. - If the location has already been swept then return -2 without
	 * changing the map. - If there is a mine in the location then the map for
	 * the corresponding location is updated with Config.SWEPT_MINE and return
	 * -1. - If there is not a mine then the number of nearby mines is
	 * determined by calling the numNearbyMines method. - If there are 1 to 8
	 * nearby mines then the map is updated with the characters '1'..'8'
	 * indicating the number of nearby mines. - If the location has 0 nearby
	 * mines then the map is updated with the Config.NO_NEARBY_MINE character. -
	 * Return the number of nearbyMines.
	 * 
	 * @param map
	 *            The map showing swept locations.
	 * @param mines
	 *            The array showing where the mines are located.
	 * @param row
	 *            The row, 0-based, of a location.
	 * @param col
	 *            The col, 0-based, of a location.
	 * @return the number of nearby mines, -1 if the location is a mine, -2 if
	 *         the location has already been swept, -3 if the location is off
	 *         the map.
	 */
	public static int sweepLocation(char[][] map, boolean[][] mines, int row, int col) {
		int returnInt = 0;
		int numMines = numNearbyMines(mines, row, col); // checks number of mines around location
		
		// Checks to see if location is in bounds, if not it returns -3
		if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
			returnInt = -3;
			
		} 
	
		// Checks to see if the location has been swept, if it has been it returns -2
		else if ((map[row][col] == Config.NO_NEARBY_MINE) || map[row][col] == numMines) {
			returnInt = -2;
		} 
		
		/*
		 * Checks to see if the location is a mine, if it is a mine the location is changed to 
		 * the SWEPT_MINE symbol and -1 is returned
		 */
		else if (mines[row][col]) {
			map[row][col] = Config.SWEPT_MINE;
			returnInt = -1;
		} 
		
		/* 
		 * Checks if the location is a mine, if it is not it returns the number of mines around
		 * the location, and changes the location to show the number of nearby mines.
		 * If the number of mines nearby is zero, the number is changed to the NO_NEARBY_MINE 
		 * symbol.
		 */
		else if (!(mines[row][col])) {
			returnInt = numMines;

			map[row][col] = (char) ('0' + numMines);

			if (numMines == 0) {
				map[row][col] = Config.NO_NEARBY_MINE;
			}
		}

		return returnInt; // returns either the locations new symbol/number
	}

	/**
	 * This method iterates through all 8 neighboring locations and calls
	 * sweepLocation for each. It does not call sweepLocation for its own
	 * location, just the neighboring locations.
	 * 
	 * @param map
	 *            The map showing touched locations.
	 * @param mines
	 *            The array showing where the mines are located.
	 * @param row
	 *            The row, 0-based, of a location.
	 * @param col
	 *            The col, 0-based, of a location.
	 */
	public static void sweepAllNeighbors(char[][] map, boolean[][] mines, int row, int col) {
		/*
		 * The following checks the locations surrounding the player's location. It calls the 
		 * sweepLocation method for each of those locations, which will change the value at that 
		 * spot depending on if there is a mine, and how many mines are around that location.
		 */
		for (int gameRow = row - 1; gameRow <= row + 1; gameRow++) {
			for (int gameCol = col - 1; gameCol <= col + 1; gameCol++) {
				if ((gameRow >= 0 && gameRow < map.length) && (gameCol >= 0 && gameCol < map[0].length)) {
					if (!(gameRow == row && gameCol == col)) {
						sweepLocation(map, mines, gameRow, gameCol);
					}
				}
			}
		}

		return; // No return value.
	}
}
