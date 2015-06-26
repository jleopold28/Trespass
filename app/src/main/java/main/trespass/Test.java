package main.trespass;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Liu on 6/26/2015.
 */
public class Test {
    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard();
        gameBoard.getTile(5,2).setBlank(false);
        gameBoard.getTile(3,2).setBlank(false);
        ArrayList<int[]> result = gameBoard.getValidTiles(5,1);
        System.out.println(result.size());
        for(int[] intarr:result){
            System.out.println(intarr[0]+","+intarr[1]);
        }

        /*
        for(int i: new int[]{1,2}){
            System.out.println(i);
        }
        */
        /*
        int[][][] boardPos = new int[6][5][2];
        for(int r = 0; r<6;r++) {
            for(int c = 0; c<5;c++) {
                boardPos[r][c][0]=r;
                boardPos[r][c][0]=c;
            }
        }
        for(int[][] introw:boardPos){
            for(int[] intcol:introw){
                System.out.println(intcol[0]+","+intcol[1]);
            }
        }
        */
    }
}

/*
        ** Get the valid tiles
    ** args row  the row of the tile that player select
    ** args col  the column of the tile that player select
    *
    * return  An ArrayList of arrays, each array has two indices, the first index is the row of the
    *         valid tile, and the second index is the column of the valid tile

    public ArrayList<int[]> getValidTiles(int row, int col) {
        ArrayList<int[]> result = new ArrayList<>();
        result.addAll(forwardValidation(row, col)); //get the valid tiles when moving forward
        result.addAll(slideValidation(row,col)); //get the valid tiles when moving left or right
        result.addAll(slideValidation(row,col)); //get the valid tiles when jumping
        return result;
    }
    public ArrayList<int[]> forwardValidation(int row, int col) {
        ArrayList<int[]> result = new ArrayList<>();
        if (row>0) {
            if (gameBoard[row-1][col].isBlank()) { //check if the front tile is blank
                result.add(new int[]{row - 1, col}); //the front the tile is valid
                result.addAll(forwardValidation(row - 1, col)); //get the forward moving valid tiles after moving forward
                result.addAll(slideValidation(row-1,col)); // get the slide moving valid tiles after moving forward
                result.addAll(jumpValidation(row-1,col)); // get the jumping valid tiles after moving forward
            }
        }

        return result;
    }
    public ArrayList<int[]> slideValidation(int row, int col) {
        ArrayList<int[]> result = new ArrayList<>();
        if (col>0) {
            if (gameBoard[row][col-1].isBlank()) { //check if the left tile is blank
                result.add(new int[]{row,col-1}); //the left the tile is valid
                result.addAll(slideValidation(row,col-1)); //get the slide moving valid tiles after moving to the left
                result.addAll(jumpValidation(row,col-1)); //get the jumping valid tiles after moving to the left
            }
        }
        if (col<colCount-1) {
            if (gameBoard[row][col+1].isBlank()) { //check if the right tile is blank
                result.add(new int[]{row,col+1}); //the right the tile is valid
                result.addAll(slideValidation(row,col+1)); //get the slide moving valid tiles after moving to the right
                result.addAll(jumpValidation(row,col+1)); //get the jumping valid tiles after moving to the right
            }
        }
        return result;
    }
    public ArrayList<int[]> jumpValidation(int row, int col) {
        ArrayList<int[]> result = new ArrayList<>();
        if (row>1) {
            if ((!gameBoard[row-1][col].isBlank())&&(gameBoard[row-2][col].isBlank())) { //check if it could jump forward
                result.add(new int[]{row-2,col}); //the forward jumping is valid
                result.addAll(jumpValidation(row-2,col)); //get the jumping valid tiles after jumping forward
            }
        }
        if (col>1) {
            if ((!gameBoard[row][col-1].isBlank())&&(gameBoard[row][col-2].isBlank())) { //check if it could jump to the left
                result.add(new int[]{row,col-2}); //the left jumping is valid
                result.addAll(jumpValidation(row,col-2)); //get the jumping valid tiles after jumping left
            }
        }
        if (col<colCount-2) {
            if ((!gameBoard[row][col+1].isBlank())&&(gameBoard[row][col+2].isBlank())) { //check if it could jump to the right
                result.add(new int[]{row,col+2}); //the right jumping is valid
                result.addAll(jumpValidation(row,col+2)); //get the jumping valid tiles after jumping right
            }
        }
        return result;
    }
*/
