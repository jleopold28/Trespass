package main.trespass;

import java.util.ArrayList;

/**
 * Created by jleopold on 6/24/2015.
 */
public class GameBoard {
    private Tile gameBoard[][];
    private int[][][] boardPos; // a 3d array to record the position of each spot on the board

    private final int rowCount = 6;
    private final int colCount = 5;

    private ArrayList<int[]> validTiles = new ArrayList<>();

    public GameBoard(){
        gameBoard = new Tile[rowCount][colCount];
        for(int r = 0; r<rowCount;r++) { //initialize the board
            for(int c = 0; c<colCount;c++) {
                gameBoard[r][c] = new Tile();
            }
        }
        boardPos = new int[rowCount][colCount][2]; //create the position array
        for(int r = 0; r<rowCount;r++) {
            for(int c = 0; c<colCount;c++) {
                boardPos[r][c][0]=r;
                boardPos[r][c][1]=c;
            }
        }
    }
    public Tile getTile( int rowCount, int colCount){
        return gameBoard[rowCount][colCount];
    }
    public void setMove(Tile t1, Tile t2){
        //moving from T1 to T2

        int startRow = t1.getRow();
        int startCol = t1.getCol();
        int startNumber = t1.getNumber();
        boolean isPlayerPiece = t1.isPlayerPiece();

        int endRow = t2.getRow();
        int endCol = t2.getCol();

        //set T1 to blank
        gameBoard[startRow][startCol].setBlank(true);
        gameBoard[startRow][startCol].setIsPlayerPiece(false);

        //set T2 to new number
        gameBoard[endRow][endCol].setNumber(startNumber);
        gameBoard[endRow][endCol].setIsPlayerPiece(isPlayerPiece);

        //set T2 is not blank
        gameBoard[endRow][endCol].setBlank(false);
    }

    public int checkForWin(){
        //return 0 for no win
        //return 1 for player win
        //return 2 for opponent win
        return 0;
    }

    /*
    ** Get the valid tiles
    ** args row  the row of the tile that player select
    ** args col  the column of the tile that player select
    *
    * return  An ArrayList of arrays, each array has two indices, the first index is the row of the
    *         valid tile, and the second index is the column of the valid tile
     */
    public ArrayList<int[]> getValidTiles(int row, int col) {
        validTiles.clear();
        forwardValidation(row, col); //get the valid tiles when moving forward
        leftSlideValidation(row, col); //get the valid tiles when moving left or right
        rightSlideValidation(row, col);
        jumpValidation(row, col); //get the valid tiles when jumping
        return validTiles;
    }
    private void forwardValidation(int row, int col) {
        if (row>0) {
            if ((gameBoard[row-1][col].isBlank()) && (!validTiles.contains(boardPos[row-1][col])) ) { //check if the front tile is blank
                validTiles.add(boardPos[row-1][col]); //the front tile is valid
                forwardValidation(row-1,col); //get the forward moving valid tiles after moving forward
                leftSlideValidation(row-1,col); // get the slide moving valid tiles after moving forward
                rightSlideValidation(row-1,col);
                jumpValidation(row-1,col); // get the jumping valid tiles after moving forward
            }
        }
    }
    private void leftSlideValidation(int row, int col) {
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                if (!validTiles.contains(boardPos[row][col- 1])) {validTiles.add(boardPos[row][col-1]);} //the left tile is valid
                leftSlideValidation(row,col-1); //get the slide moving valid tiles after moving to the left
                jumpValidation(row,col-1); //get the jumping valid tiles after moving to the left
            }
        }
    }
    private void rightSlideValidation(int row, int col) {
        if (col<colCount-1) {
            //System.out.println(boardPos[row][col+1][0]+","+boardPos[row][col+1][1]);
            //System.out.println(validTiles.contains(boardPos[row][col+1]));
            //if (row==5 && col==2) {
            //    System.out.println(validTiles.contains(boardPos[row][col+1]));
            //    for(int[] intarr:validTiles){
            //        System.out.println(intarr[0]+","+intarr[1]);
            //    }
            //}
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                if (!validTiles.contains(boardPos[row][col+1])) {validTiles.add(boardPos[row][col+1]);} //the right tile is valid
                rightSlideValidation(row,col+1); //get the slide moving valid tiles after moving to the right
                jumpValidation(row,col+1); //get the jumping valid tiles after moving to the right
            }
        }
    }
    private void jumpValidation(int row, int col) {
        if (row>1) {
            if ((!gameBoard[row-1][col].isBlank()) && (gameBoard[row-2][col].isBlank()) && (!validTiles.contains(boardPos[row-2][col]))) { //check if it could jump forward
                validTiles.add(boardPos[row-2][col]); //the forward jumping is valid
                jumpValidation(row-2,col); //get the jumping valid tiles after jumping forward
            }
        }
        if (col>1) {
            if ((!gameBoard[row][col-1].isBlank()) && (gameBoard[row][col-2].isBlank()) && (!validTiles.contains(boardPos[row][col-2]))) { //check if it could jump to the left
                validTiles.add(boardPos[row][col-2]); //the left jumping is valid
                jumpValidation(row, col-2); //get the jumping valid tiles after jumping left
            }
        }
        if (col<colCount-2) {
            if ((!gameBoard[row][col+1].isBlank()) && (gameBoard[row][col+2].isBlank()) && (!validTiles.contains(boardPos[row][col+2]))) { //check if it could jump to the right
                validTiles.add(boardPos[row][col+2]); //the right jumping is valid
                jumpValidation(row,col+2); //get the jumping valid tiles after jumping right
            }
        }
    }

}
