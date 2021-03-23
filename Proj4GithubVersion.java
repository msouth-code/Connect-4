import java.util.Random;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/** This code is a game of connect 4.
 * Miaya South
 * Gateway Computing: Java
 * Due: 03/25/2021
 */
public class Proj4GithubVersion {

   public static void main(String[] args) throws IOException {
      Random rnd = new Random(0);
      Scanner kb = new Scanner(System.in);  
      
      printMenu();
      
      String userChoice = kb.next();
      
      while (!validMenu(userChoice)) {
         System.out.print("Invalid choice!\n");
         printMenu();
         userChoice = kb.next();
         validMenu(userChoice);
      } 
      
      while (validMenu(userChoice)) {
         if ("q".equals(userChoice.toLowerCase())) {
            return;
         }
         
         menuSelection(kb, userChoice, rnd);
         printMenu();
         userChoice = kb.next();
      
      }
      
      kb.close();
   
      
   }
   
   /** Checks if the menu option entered is valid.
   *
   * @param userChoice the menu option
   * @return returns boolean of whether choice is valid or not
   */
   public static boolean validMenu(String userChoice) {
      return ("n".equals(userChoice.toLowerCase()) || 
             "l".equals(userChoice.toLowerCase()) || 
             "q".equals(userChoice.toLowerCase()));
   }
   
   
   
   /** Goes through the menu options.
   *
   * @param kb Scanner
   * @param userChoice the inputted menu option
   * @param rnd Random
   * @throws IOException
   */
   public static void menuSelection(Scanner kb, 
                           String userChoice, Random rnd) throws IOException {  
      
      
      
      if ("n".equals(userChoice.toLowerCase())) {
         menuN(kb, rnd);
      
      
      } else if ("l".equals(userChoice.toLowerCase())) {
         char[][] board;
         int row;
         int col;
         
         System.out.print("Enter filename: ");
         String gameToLoad = kb.next();
         
         FileInputStream fileInStream = new FileInputStream(gameToLoad);
         Scanner inFS = new Scanner(fileInStream);
         
         row = inFS.nextInt();
         col = inFS.nextInt();
      
         board = new char[row][col];
         
         boolean redTurn = loadGame(board, inFS);
         
         printBoard(board);
         
         int alreadyFinished = isGameOver(board);
         
         if (alreadyFinished == 0) {
            System.out.print("Congrats, red wins!\n");
         } else if (alreadyFinished == 1) {
            System.out.print("Congrats, yellow wins!\n");
         } else if (alreadyFinished == 2) {
            System.out.print("Game is drawn!\n");
         } else {
         
            int gameResult = play(board, kb, redTurn);
         
            if (gameResult == 0) {
               printBoard(board);
               System.out.print("Congrats, red wins!\n");
            
            } else if (gameResult == 1) {
               printBoard(board);
               System.out.print("Congrats, yellow wins!\n");
            
            } else if (gameResult == 2) {
               printBoard(board);
               System.out.print("Game is drawn!\n");
            
            } else if (gameResult == -1) {
               System.out.print("Enter filename: ");
               String saveName = kb.next();
               saveGame(board, saveName, redTurn);
            
            }
         
         
         }
         
      } else if ("q".equals(userChoice.toLowerCase())) {
         return;
      
      }
                  
      else {
         return;
      }  
    
      
   }
   
   /** Helper method for the N menu option.
   *
   * @param kb Scanner
   * @param rnd Random
   * @throws IOException
   */
   public static void menuN(Scanner kb, Random rnd) throws IOException {  
         
      char[][] board;
      int col;
      int row;
         
      System.out.print("Enter number of columns: ");
      col = kb.nextInt();
         
      while (!(col >= 4 && col <= 10)) { ///possible helper method
         System.out.print("Error, enter number of columns: ");
         col = kb.nextInt();
      }
         
      System.out.print("Enter number of rows: ");
      row = kb.nextInt(); 
         
      while (!(row >= 4 && row <= 10)) {
         System.out.print("Error, enter number of rows: ");
         row = kb.nextInt();
      }
         
      String playerTurn;
         
      boolean redTurn;
         
      int whoseTurn = rnd.nextInt(2); //does it matter b/c the seed
         
      if (whoseTurn == 0) {
         playerTurn = "R";
         redTurn = true;
      } else {
         playerTurn = "Y";
         redTurn = false;
      }
         
      board = new char[row][col];
         
      initBoard(board); 
         
      int gameResult = play(board, kb, redTurn);
         
      gameResults(gameResult, board, kb, redTurn);
         
   }    
   
   /** Helper method for menuN method.
   *
   * @param gameResult game result from play method
   * @param board board in play
   * @param kb Scanner
   * @param redTurn determines whose turn it is
   * @throws IOException
   */  
   public static void gameResults(int gameResult, 
                           char[][] board, Scanner kb, 
                              boolean redTurn) throws IOException {
      if (gameResult == 0) {
         printBoard(board);
         System.out.print("Congrats, red wins!\n");
         System.out.println();
            
      }
         
      if (gameResult == 1) {
         printBoard(board);
         System.out.print("Congrats, yellow wins!\n");
            
      }
         
      if (gameResult == 2) {
         printBoard(board);
         System.out.print("Game is drawn!\n");
            
      }
         
      if (gameResult == -1) {
         System.out.print("Enter filename: ");
         String saveName = kb.next();
         saveGame(board, saveName, redTurn);
      }
   }   

   /** This method controls the entire game flow. As long as the game is being 
    * player by the players and "save" (i.e., -1) choice is not selected, the 
    * game continues. The flow stops when either the game ends 
    * (i.e., a player wins) or because -1 is entered to save the game.
    * @param board a 2D array which is the game's board representing 
    * the current state of the game
    * @param kb the scanner to be used to collect user inputs from 
    * standard input
    * @param redTurn a boolean indicating if it is red's player to move or not
    * @return -1 if save is selected, 0 if red wins, 1 if yellow wins,
    *          2 if the game is drawn.
    * @throws IOException
    */
   public static int play(char[][] board, Scanner kb, 
                     boolean redTurn) throws IOException {
      
      int gameOver = isGameOver(board);
      
      if (gameOver != 3) {
         return gameOver;
      } else {
                  
         String playerTurn;
      
         if (redTurn) {
            playerTurn = "red";
         } else {
            playerTurn = "yellow";
         }
       
         System.out.print(playerTurn + 
                  "'s turn, enter the move (-1 to save): ");
         int userMove = kb.nextInt(); 
      
         gameOver = -3;
            
         while (userMove != -1) {  
                      
         
            boolean validMove = move(board, userMove, redTurn);
         
            while (!(validMove)) {
               printBoard(board);
               System.out.print(playerTurn + 
                           "'s turn, enter the move (-1 to save): ");
               userMove = kb.nextInt();
            
               validMove = move(board, userMove, redTurn);
            }
         
            gameOver = isGameOver(board);
         
            if (gameOver != 3) {
               break;
            }
         
            redTurn = !(redTurn);
         
            printBoard(board);
         
            if (redTurn) {
               playerTurn = "red";
            } else {
               playerTurn = "yellow";
            }               
         
            System.out.print(playerTurn + 
               "'s turn, enter the move (-1 to save): ");
            userMove = kb.nextInt();
         
         }
      
         if (userMove == -1) {
            return -1;
         }       
      
      
      
      
      }               
      return gameOver; 
   }

   /** This method plays a move on the board by adding a new piece 
    * to one of the columns on the correct row which is lowest 
    * empty row on the chosen column.
    * @param board the game board
    * @param index the index of the column a peice is being added
    * @param redTurn a boolean indicating which player's piece is being played
    * @return true if a piece is added successfully added on the 
    * board, false otherwise
    */
   public static boolean move(char[][] board, int index, boolean redTurn) {
   
      char playerTurn;
      
      if (redTurn) {
         playerTurn = 'R';
      } else {
         playerTurn = 'Y';
      }
      
      int bottomRow = board.length - 1;
   
      
      while (board[bottomRow][index] != '-') {
         bottomRow--;
         if (bottomRow < 0) {
            return false;
         }
      }
      
      board[bottomRow][index] = playerTurn;
      
      return true; 
   }

   /** This method prints the menu.
    * DO NOT CHANGE THIS!
    */
   public static void printMenu() {
      System.out.println();
      System.out.println("n/N: New game");
      System.out.println("l/L: Load a game");
      System.out.println("q/Q: Quit");
      System.out.println("-------------");
      System.out.print("Enter your choice: ");
   }

   /** This method initialize the board to all empty cells.
    * DO NOT CHANGE THIS!
    * @param board the board
    */
   public static void initBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            board[i][j] = '-';
         }
      }
   }

   /** This method prints the board.
    * DO NOT CHANGE THIS!
    * @param board the board
    */
   public static void printBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            System.out.print(board[i][j] + " ");
         }
         System.out.println();
      }
   }

   /** This method saves the game in an external text file.
    *
    * @param board the current board
    * @param fileName the file name the game is going to be saved to
    * @param redTurn which player's turn it is next time the game will resume
    * @throws IOException
    */
   public static void saveGame(char[][] board, String fileName, 
                      boolean redTurn) throws IOException {
                                      
                      
      FileOutputStream fileOutStream = new FileOutputStream(fileName);
      PrintWriter outFS = new PrintWriter(fileOutStream);
      
      int row = board.length;
      int col = board[0].length;
      
      outFS.print(row + " " + col);
      outFS.print("\n");
      
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            outFS.print(board[i][j]);
            
         }
         outFS.println();
      }
   
      if (redTurn) {
         outFS.print("R");
      } else { 
         outFS.print("Y");
      }
            
      outFS.flush();
      fileOutStream.close();
      
   }

   /** This method loads a game from a saved file. The board must be filled
    * according to the content of the file and whose turn it is to make a move
    * is also read and returned as a boolean from this method.
    * @param board the game board
    * @param inFS the open scanner associated with the file the game 
    * is being loaded from
    * @return true if it is red to move, false otherwise
    * @throws IOException
    */
   public static boolean loadGame(char[][] board, 
                 Scanner inFS) throws IOException {
                 
      int row = board.length;
      int col = board[0].length;
         
      String[] loadChars = new String[row];
         
         //could be separate method (fillBoard)
      String playerTurn = "";
      int count = 0;
      int m = 0;
      while (inFS.hasNext()) {
         if (count == loadChars.length) {
            playerTurn = inFS.next();
            break;
         } else {         
            
            loadChars[m] = inFS.next();
            m++;
            count++;
               
             
         }
         
      }
         
      int k = 0;
      int p = 0;     
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
                  
            if (p >= col) {
               k++;
               if (k == loadChars.length) {
                  break;
               }
               p = 0;
               board[i][j] = loadChars[k].charAt(p);
               p++;
            } else {
               board[i][j] = loadChars[k].charAt(p); 
               p++;
            }
               
         }
      }   
      
      boolean redTurn = true;
      
      if ("Y".equals(playerTurn)) {
         redTurn = false;
      }
   
                       
      return redTurn; 
   }

   /** This method decides whether the game is over or not. The game is 
    * over if the player as indicated by red wins the game based on the 
    * current state of the board. The method can also tell if the game 
    * is drawn or not.
    *
    * @param board the game board
    * @return 0 if red wins the game, 1 if
    *         yellow wins, 2 if draw, 3 otherwise.
    */
   public static int isGameOver(char[][] board) {
      
      if (hasBackDiagonal(board) == 'R') {
         return 0;
      } else if (hasBackDiagonal(board) == 'Y') {
         return 1;
      } 
      
      if (hasForwardDiagonal(board) == 'R') {
         return 0;
      } else if (hasForwardDiagonal(board) == 'Y') {
         return 1;
      }    
      
      String rowCheck = boardRows(board);
      String colCheck = boardCols(board);
      
      if (rowCheck.contains("RRRR")) {
         return 0;
      } else if (colCheck.contains("RRRR")) {
         return 0;
      } else if (rowCheck.contains("YYYY")) {
         return 1;
      } else if (colCheck.contains("YYYY")) {
         return 1;
      } else if (!(rowCheck.contains("-"))) {
         return 2;
      } else {
         return 3; 
      }
      
      
   }
   
   /** Helper method for isGameOver.
   *   puts board into readable string for method
   *
   * @param board board in play
   * @return rowCheck the board as a string
   */
   public static String boardRows(char[][] board) {
      String rowCheck = "";   
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            rowCheck = rowCheck + Character.toString(board[i][j]);
            if (j == board[0].length - 1) {
               rowCheck = rowCheck + " ";
            }
         }
      }
      
      return rowCheck;
   } 
   
   /** Helper method for isGameOver.
   *   puts board into readable string for method
   *
   * @param board board in play
   * @return colCheck the board as a string
   */
   public static String boardCols(char[][] board) {
      String colCheck = ""; 
      for (int j = 0; j < board[0].length; j++) {
         for (int i = 0; i < board.length; i++) {
            colCheck = colCheck + Character.toString(board[i][j]);
            if (i == board.length - 1) {
               colCheck = colCheck + " ";
            }
         }
      }
      
      return colCheck;
   }
   
   /** Checks if the game is over with a back diagonal.
   *
   * @param board the board in play
   * @return winner the character of the winner if there is a back diagonal
   */
   public static char hasBackDiagonal(char[][] board) {
      //backward diagonal
      boolean allMatch = true;
      char winner = 'N';
      int row = -1;
      int col = -1;
      
      for (int i = board.length - 1; i >= 3; i--) {
         for (int j = board[0].length - 1; j >= 3; j--) {
            //int lastRow = board.length - 1;
            allMatch = true;
         
            if (!(board[i][j] == board[i - 1][j - 1])) {
               allMatch = false;
            }
         
            if (!(board[i][j] == board[i - 2][j - 2])) {
               allMatch = false;
            }
         
            if (!(board[i][j] == board[i - 3][j - 3])) {
               allMatch = false;
            }
         
            if (allMatch) {
               row = i;
               col = j;
               break;
            }
            
         }
         
         if (allMatch) {
            winner = board[row][col];
            break;
         }
      
      }
      
      return winner;
      
   }
   
   /** Checks if the game is over with a forward diagonal.
   *
   * @param board the board in play
   * @return winner the character of the winner if there is a back diagonal
   */
   public static char hasForwardDiagonal(char[][] board) {
      //forward diagonal
      boolean allMatch = true;
      char winner = 'N';
      int row = -1;
      int col = -1;
      
      for (int i = board.length - 1; i >= 3; i--) {
         for (int j = 0; j < board[0].length - 3; j++) {
         //int lastRow = board.length - 1;
            allMatch = true;
         
            if (!(board[i][j] == board[i - 1][j + 1])) {
               allMatch = false;
            }
         
            if (!(board[i][j] == board[i - 2][j + 2])) {
               allMatch = false;
            }
         
            if (!(board[i][j] == board[i - 3][j + 3])) {
               allMatch = false;
            }
         
            if (allMatch) {
               row = i;
               col = j;
               break;
            }
                     
         }
         
         if (allMatch) {
            winner = board[row][col];
            break;
         }
      
         
      }
      return winner;
      
   
   
   }
}
