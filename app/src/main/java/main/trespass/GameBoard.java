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
    //private Tile validTiles[];

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
    public void refreshGameBoard() {
        gameBoard = new Tile[rowCount][colCount];
        for(int r = 0; r<rowCount;r++) { //initialize the board
            for(int c = 0; c<colCount;c++) {
                gameBoard[r][c] = new Tile();
            }
        }
    }
    public Tile getTile( int rowCount, int colCount){
        return gameBoard[rowCount][colCount];
    }
    public void setMove(int startRow, int startCol, int endRow, int endCol){ //t1 is previous location , t2 is where you are moving to
        //moving from T1 to T2
        /**
        int startRow = t1.getRow();
        int startCol = t1.getCol();
        int startNumber = t1.getNumber();
        //boolean isPlayerPiece = t1.isPlayerPiece();

        int endRow = t2.getRow();
        int endCol = t2.getCol();
        **/
        //set T1 to blank
        gameBoard[startRow][startCol].setBlank(true);
        //gameBoard[startRow][startCol].setIsPlayerPiece(false);

        //set T2 to new number
        gameBoard[endRow][endCol].setBlank(false);
        gameBoard[endRow][endCol].setNumber(gameBoard[startRow][startCol].getNumber());
        if (gameBoard[startRow][startCol].isPlayerPiece()) {
            gameBoard[endRow][endCol].setIsPlayerPiece(true);
        } else {
            gameBoard[endRow][endCol].setIsPlayerPiece(false);
        }

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
    public ArrayList<int[]> getValidTiles(int row, int col, boolean isPlayerPiece) {
        validTiles.clear();
        if(isPlayerPiece) {
            forwardSlideValidation(row, col); //get the valid tiles when moving forward
            slideForwardLeftValidation(row, col);
            slideForwardRightValidation(row, col);
            jumpValidation(row, col); //get the valid tiles when jumpin
        }
        else{
            backwardSlideValidation(row,col);
            slideBackwardLeftValidation(row,col);
            slideBackwardRightValidation(row,col);
            jumpBackwardValidation(row,col);
        }
        return validTiles;
    }
    private void forwardSlideValidation(int row, int col) {
        if (row>0) {
            if ((gameBoard[row-1][col].isBlank()) && (!validTiles.contains(boardPos[row-1][col])) ) { //check if the front tile is blank
                validTiles.add(boardPos[row-1][col]); //the front tile is valid
                forwardSlideValidation(row-1,col); //get the forward moving valid tiles after moving forward
                forwardSlideLeftValidation(row-1,col); // get the slide moving valid tiles after moving forward
                forwardSlideRightValidation(row-1,col);
                jumpValidation(row-1,col); // get the jumping valid tiles after moving forward
            }
        }
    }
    private void forwardSlideLeftValidation(int row, int col) {
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                if (!validTiles.contains(boardPos[row][col- 1])) {validTiles.add(boardPos[row][col-1]);} //the left tile is valid
                forwardSlideLeftValidation(row, col - 1); //get the slide moving valid tiles after moving to the left
                jumpValidation(row,col-1); //get the jumping valid tiles after moving to the left
                //slideForwardValidation(row,col-1);
            }
        }
    }
    private void forwardSlideRightValidation(int row, int col) {
        if (col<colCount-1) {
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                if (!validTiles.contains(boardPos[row][col+1])) {validTiles.add(boardPos[row][col+1]);} //the right tile is valid
                forwardSlideRightValidation(row, col + 1); //get the slide moving valid tiles after moving to the right
                jumpValidation(row,col+1); //get the jumping valid tiles after moving to the right
                //slideForwardValidation(row,col+1);
            }
        }
    }
    private void slideForwardValidation(int row, int col) {
        if (row>0) {
            if ((gameBoard[row-1][col].isBlank()) && (!validTiles.contains(boardPos[row-1][col])) ) { //check if the front tile is blank
                validTiles.add(boardPos[row - 1][col]); //the front tile is valid
                slideForwardValidation(row - 1, col); //get the forward moving valid tiles after moving forward
                jumpValidation(row - 1, col); // get the jumping valid tiles after moving forward
            }
        }
    }
    private void slideForwardLeftValidation(int row, int col) {
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                if (!validTiles.contains(boardPos[row][col- 1])) {validTiles.add(boardPos[row][col-1]);} //the left tile is valid
                slideForwardLeftValidation(row, col-1); //get the slide moving valid tiles after moving to the left
                slideForwardValidation(row,col-1);
                jumpValidation(row,col-1); //get the jumping valid tiles after moving to the left
                //slideForwardValidation(row,col-1);
            }
        }
    }
    private void slideForwardRightValidation(int row, int col) {
        if (col<colCount-1) {
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                if (!validTiles.contains(boardPos[row][col+1])) {validTiles.add(boardPos[row][col+1]);} //the right tile is valid
                forwardSlideRightValidation(row, col + 1); //get the slide moving valid tiles after moving to the right
                slideForwardValidation(row,col+1);
                jumpValidation(row,col+1); //get the jumping valid tiles after moving to the right
                //slideForwardValidation(row,col+1);
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


    private void backwardSlideValidation(int row, int col) {
        if (row<5) {
            if ((gameBoard[row+1][col].isBlank()) && (!validTiles.contains(boardPos[row+1][col])) ) { //check if the front tile is blank
                validTiles.add(boardPos[row+1][col]); //the front tile is valid
                backwardSlideValidation(row + 1, col); //get the forward moving valid tiles after moving forward
                backwardSlideLeftValidation(row + 1, col); // get the slide moving valid tiles after moving forward
                backwardSlideRightValidation(row + 1, col);
                jumpBackwardValidation(row + 1, col); // get the jumping valid tiles after moving forward
            }
        }
    }
    private void backwardSlideLeftValidation(int row, int col) {
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                if (!validTiles.contains(boardPos[row][col- 1])) {validTiles.add(boardPos[row][col-1]);} //the left tile is valid
                backwardSlideLeftValidation(row, col - 1); //get the slide moving valid tiles after moving to the left
                jumpBackwardValidation(row, col - 1); //get the jumping valid tiles after moving to the left
                //slideForwardValidation(row,col-1);
            }
        }
    }
    private void backwardSlideRightValidation(int row, int col) {
        if (col<colCount-1) {
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                if (!validTiles.contains(boardPos[row][col+1])) {validTiles.add(boardPos[row][col+1]);} //the right tile is valid
                backwardSlideRightValidation(row, col + 1); //get the slide moving valid tiles after moving to the right
                jumpBackwardValidation(row, col + 1); //get the jumping valid tiles after moving to the right
                //slideForwardValidation(row,col+1);
            }
        }
    }
    private void slideBackwardValidation(int row, int col) {
        if (row<5) {
            if ((gameBoard[row+1][col].isBlank()) && (!validTiles.contains(boardPos[row+1][col])) ) { //check if the front tile is blank
                validTiles.add(boardPos[row + 1][col]); //the front tile is valid
                slideBackwardValidation(row + 1, col); //get the forward moving valid tiles after moving forward
                jumpBackwardValidation(row + 1, col); // get the jumping valid tiles after moving forward
            }
        }
    }
    private void slideBackwardLeftValidation(int row, int col) {
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                if (!validTiles.contains(boardPos[row][col- 1])) {validTiles.add(boardPos[row][col-1]);} //the left tile is valid
                slideBackwardLeftValidation(row, col - 1); //get the slide moving valid tiles after moving to the left
                slideBackwardValidation(row, col - 1);
                jumpBackwardValidation(row, col - 1); //get the jumping valid tiles after moving to the left
                //slideForwardValidation(row,col-1);
            }
        }
    }
    private void slideBackwardRightValidation(int row, int col) {
        if (col<colCount-1) {
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                if (!validTiles.contains(boardPos[row][col+1])) {validTiles.add(boardPos[row][col+1]);} //the right tile is valid
                backwardSlideRightValidation(row, col + 1); //get the slide moving valid tiles after moving to the right
                slideBackwardValidation(row, col + 1);
                jumpBackwardValidation(row, col + 1); //get the jumping valid tiles after moving to the right
                //slideForwardValidation(row,col+1);
            }
        }
    }
    private void jumpBackwardValidation(int row, int col) {
        if (row<4) {
            if ((!gameBoard[row+1][col].isBlank()) && (gameBoard[row+2][col].isBlank()) && (!validTiles.contains(boardPos[row+2][col]))) { //check if it could jump forward
                validTiles.add(boardPos[row+2][col]); //the forward jumping is valid
                jumpBackwardValidation(row + 2, col); //get the jumping valid tiles after jumping forward
            }
        }
        if (col>1) {
            if ((!gameBoard[row][col-1].isBlank()) && (gameBoard[row][col-2].isBlank()) && (!validTiles.contains(boardPos[row][col-2]))) { //check if it could jump to the left
                validTiles.add(boardPos[row][col-2]); //the left jumping is valid
                jumpBackwardValidation(row, col - 2); //get the jumping valid tiles after jumping left
            }
        }
        if (col<colCount-2) {
            if ((!gameBoard[row][col+1].isBlank()) && (gameBoard[row][col+2].isBlank()) && (!validTiles.contains(boardPos[row][col+2]))) { //check if it could jump to the right
                validTiles.add(boardPos[row][col+2]); //the right jumping is valid
                jumpBackwardValidation(row, col + 2); //get the jumping valid tiles after jumping right
            }
        }
    }
    public int[] getCoordinate(int row, int col){
        return boardPos[row][col];
    }
}
