import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

 public static void main(String[] args) {
  Scanner scan = new Scanner(System.in);
  int row;//user inputed row
  int col;//user inputed col
  int move;//user inputed move decider
  int mode; //user inputed mode
  //rules of the game
  System.out.println("==========================================================Rules===========================================================");
  System.out.println("1) In Minesweeper, you must avoid all of the bombs on the board by revealing spots and deciding your next move CAREFULLY!!");
  System.out.println("2) When you reveal a spot, a number will be shown. This number displays how many bombs are around that spot on the board.");
  System.out.println("3) The bombs are touching any of the 8 spots around that point.");
  System.out.println("4) 2 ways to make a move, you can either place a flag to mark a potential bomb location or continue on with your next move.");
  System.out.println("5) If all spots are revealed and no bombs are hit, you have successfully solved the Minesweeper puzzle!\n");
  System.out.println("======================SELECT MODE=========================");

  do {//ask for user to enter 1, 2, 3, or 4 to select a mode
   System.out.println("Type 1 For Easy (10 Bombs) : Type 2 For Medium (13 Bombs)\n");
   System.out.println("Type 3 For Hard (17 Bombs) : Type 4 For Extreme (24 Bombs)\n");
   mode = scan.nextInt();
  }while(mode != 1 && mode != 2 && mode != 3 && mode != 4);//repeat asking until valid mode is selected

  Minesweeper game = new Minesweeper(mode); //makes a new class object and calls the constructor to set the board
  game.invisBoardSet(); // calls method to Set the invisible board with the bombs and numbers
  do{//repeats asking the user for moves until a win or loss occurs
   game.printBoard(); //print the current visible board
   do{
    System.out.println("Type 1 To Continue : Type 2 For A Flag");// asks for what move the user wants to make
    move = scan.nextInt();
   }while(move != 1 && move != 2);// user must input a 1 or 2 to decide their next move

   System.out.println("Enter The Row And Column You Want:"); // ask for user input for what position they want to select
   row = scan.nextInt()-1; // arrays are 0 indexed so -1 from user input
   col = scan.nextInt()-1;

   while(!game.placeMarker(row, col, move)){ // checks if the inputed position is out of the grid or already chosen. Asks for new input if it is
    //placeMarker will also check if you wanted to place a flag or continue playing
    System.out.println("This Spot Is Taken Or Out Of Bounds. Please Enter A New One: ");//ask for new input
    row = scan.nextInt()-1;
    col = scan.nextInt()-1;
    //run method again to check if input is valid
   }
  }while(!game.bombHit() && !game.win());// if a win or a loss occurs the while loop of asking for moves will stop

  if(game.bombHit()){ //if you hit a bomb
   game.printBoard();//print final board with revealed bomb locations
   System.out.println("GAME OVER");
   System.out.println("YOU HIT A BOMB");
  }
  else{ //win
   game.printBoard(); //print fully revealed board
   System.out.println("WINNER");
   System.out.println("ALL BOMBS HAVE BEEN AVOIDED");
  }
  scan.close(); //close scanner
 }

 //instance/class variables: used in a lot of the methods: doesn't need to be called from main method. Makes code cleaner.
 char[][] invisBoard; //non visible board to the user
 char[][] visBoard; //visible to the user
 final int bombs;// how many bombs will be on the board
 final int size = 9; //board is 9x9 size
 final int[] x = {-1, -1, -1, 0, 0, 1, 1, 1}; // Offsets for row
 final int[] y = {-1, 0, 1, -1, 1, -1, 0, 1}; // Offsets for column

 //constructor: Sets the board size of Minesweeper and game mode
 public Minesweeper(int mode){
  if (mode == 1) { //user selects mode and depending on the entered mode a diff amount of bombs will be on the board
   bombs = 10;
  } else if (mode == 2) {
   bombs = 13;
  } else if (mode == 3) {
   bombs = 17;
  } else {
   bombs = 24;
  }

  invisBoard = new char[size][size];//hidden board with everything revealed and set
  visBoard = new char[size][size];//visual to the user. This board will reveal the spots entered by the user. This board starts off empty
  for (int r = 0; r < visBoard.length; r++) {
   for (int c = 0; c < visBoard.length; c++) {
    visBoard[r][c] = ' ';//set visual board to empty spaces in each slot.
   }
  }
 }
 //checks if a bomb has been hit by the user
 //if a bomb is hit it returns true and will stop the game
 //it also calls the bombReveal method which reveals all bombs on the board after losing the game.
 public boolean bombHit(){
  for (int r = 0; r < size; r++) {
   for (int c = 0; c < size; c++) {
    if (visBoard[r][c] == 'B'){ //checks if the user hit a bomb
     bombReveal();//call bombReveal to reveal all bombs after losing
     return true;
    }
   }
  }
  return false;
 }
 //reveals all bomb locations to the visible board after losing the game
 public void bombReveal(){
  for (int r = 0; r < size; r++) {
   for (int c = 0; c < size; c++) {
    if(invisBoard[r][c] == 'B') {
     visBoard[r][c] = invisBoard[r][c];//reveals all hidden bomb spots after losing the game to see where all bombs were
    }
   }
  }
 }
 //Checks for how many bombs are around a certain point in the grid and returns it to invisBoardSet Method.
 public char bombsAround(int r, int c){
  //each part of the x and y array checks for each of the possible grids around the point passed to the method.
  //It then also checks if the point is on the edge of the grid and it will skip over the possible bomb spots outside of the grid to avoid an error.
  int bombCount = 0;

  for (int i = 0; i < 8; i++) {
   int newRow = r + x[i];//works through each row offset
   int newCol = c + y[i];//works through each col offset
   if ( newRow >= 0 && newRow < size && newCol >= 0 && newCol < size && invisBoard[newRow][newCol] == 'B') {
    bombCount++;//bombCount increase if the grid is inside the board and is = to a bomb
   }
  }

  return (char)(bombCount + '0'); // Convert int to char representation of the count
 }

 //decides if the user wants to place a flag or reveal a new spot on the board
 public boolean decideMove(int move) {

  if(move == 1) {//user input is 1 or 2
   return false;//if 1 they want to do a regular move
  } else {
   return true;//if 2 they want to place a flag to mark a bomb
  }
 }
 //sets the invisible board with bombs and the number of bombs surrounding all the points on the grid
 public void invisBoardSet(){
  setBombs();//calls set bombs method to set bombs in random locations on the board
  for (int r = 0; r < size; r++) {
   for (int c = 0; c < size; c++)
   {
    if(invisBoard[r][c] != 'B') {//checks for empty spaces
     invisBoard[r][c] = bombsAround(r, c);// fills empty spaces with numbers calling bombsAround method to check how many bombs are around a point
    }
   }
  }
 }
 //reveals the position entered by the user onto the visible board. Also checks if the user entered a spot that was already selected or outside of the board.
 //If the input is invalid, the method returns false and asks for a new input until valid.
 public boolean placeMarker(int row, int col, int move){
  for (int r = 0; r < size; r++) {
   for (int c = 0; c < size; c++){
    if (visBoard[r][c] == ' ' || visBoard[r][c] == 'F') {//allows to make a move on a flag or an empty space
     if ((r == row) && (c == col)){//checks if the input is in range of the board
      if(!decideMove(move)) {//checks if the user wanted to do a regular move
       visBoard[r][c] = invisBoard[r][c];//reveals spot
       if (visBoard[r][c] == '0') {
        zeroExpand(r, c);//if a revealed spot = 0, expand and reveal all 0 points touching it and all points around the zeros. Like an explosion
       }
      }
      else {
       visBoard[r][c] = 'F'; //if the user wants to place a flag
      }
      return true;
     }
    }
   }
  }
  return false;
 }
 //prints the visBoard
 public void printBoard(){
  System.out.println("  ======================MINESWEEPER======================");//title
  System.out.print("     ");
  for (int c = 0; c < size; c++) {
   System.out.print((c+1) + "     "); //print columns
  }

  System.out.println();
  System.out.println("  -------------------------------------------------------");//top of board
  for (int r = 0; r < size; r++)
  {
   System.out.print((r+1) + " |  "); //prints rows
   for (int c = 0; c < size; c++)
   {
    if(visBoard[r][c] == 'B') {
     System.out.print("\u001B[31m"); // ANSI escape code for red color
     System.out.print(visBoard[r][c] + "\u001B[0m"); //prints flag or bomb in red and resets the colour back to default
     System.out.print("  |  "); // grid divider
    }
    else if (visBoard[r][c] == 'F') {
     System.out.print("\u001B[34m"); // ANSI escape code for yellow color
     System.out.print(visBoard[r][c] + "\u001B[0m");//prints the number in the grid and resets colour
     System.out.print("  |  ");//grid divider
    }
    else {
     System.out.print("\u001B[33m"); // ANSI escape code for yellow color
     System.out.print(visBoard[r][c] + "\u001B[0m");//prints the number in the grid and resets colour
     System.out.print("  |  ");//grid divider
    }

   }
   System.out.println();
   System.out.println("  -------------------------------------------------------");//row divider
  }
 }
 //sets the bombs in random locations on the invisBoard
 public void setBombs(){
  Random random = new Random();
  int randomRow;//holds random row
  int randomCol;//holds random col
  for (int i = 0; i < bombs; i++){
   randomRow = random.nextInt(size); //apply random number from 0-8 to randomRow
   randomCol = random.nextInt(size); //apply random number from 0-8 to randomCol
   if (invisBoard[randomRow][randomCol] == 'B') {
    i--;//checks if a spot was already assigned as a bomb. Re runs that loop again
   } else {
    invisBoard[randomRow][randomCol] = 'B';// sets a point to'B' if it is a bomb
   }
  }

 }

 //This method checks for a win by counting how many non bomb squares have been revealed. If all non bomb spots have been chosen, the gane will end and outprint winner
 public boolean win(){
  int squareCounter = 0; //keeps track of how many spots are revealed
  for (int r = 0; r < size; r++) {
   for (int c = 0; c < size; c++) {
    if(visBoard[r][c] != ' ' && visBoard[r][c] != 'F' && invisBoard[r][c] != 'B') {
     squareCounter++;//loop works through each point and checks if it is already revealed.
    }
   }
  }
  //if counter is = all possible spots revealed there is a winner.
  if(squareCounter == size*size-bombs) {
   return true; //return true if there is a win. Main method will stop the game
  } else {
   return false;//return false if there is a loss. Main method will continue the game
  }
 }
 public void zeroExpand (int row, int col) { //method creates a q of all of the positions = to 0 that are touching each other. Then reveal all points around each 0 point

  int[] prevRow = new int[size * size];//array stores previous rows
  int[] prevCol = new int[size * size];//array stores previous cols
  int front = 0;//works through the q
  int rear = 0;//adds each 0 position to the q increases q by 1 for each 0

  prevRow[rear] = row;//holds the initial row (selected by user)
  prevCol[rear] = col;//holds initial col (selected by user)
  rear++;//rear increases by 1

  while (front != rear) { //will continue this code until the whole q is worked through
   int curRow = prevRow[front]; //this will hold the current row we are checking
   int curCol = prevCol[front];//this will hold the current col we are checking
   front++;//front increases by 1 moves to next point in q

   for (int i = 0; i < 8; i++) {//check all surrounding points relative to the current row and col
    int newRow = curRow + x[i];//stores new row
    int newCol = curCol + y[i];//stores new col
    // makes sure the point we are checking is within the grid and that it is not revealed already
    if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size && visBoard[newRow][newCol] == ' ') {
     visBoard[newRow][newCol] = invisBoard[newRow][newCol];//reveal new point
     if (invisBoard[newRow][newCol] == '0') { // if one of the new points is 0 it will increase the q by 1 making the code run again for the next point
      prevRow[rear] = newRow;
      prevCol[rear] = newCol;
      rear++;
     }
    }
   }
  }
 }
}


