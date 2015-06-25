package main.trespass;

/**
 * Created by jleopold on 6/24/2015.
 */
public class Tile {
    private int number;
    private int row;
    private int col;
    private boolean isPlayerPiece;

    public Tile(int number, int row,int col, boolean isPlayerPiece){
        this.number = number;
        this.row = row;
        this.col = col;
        this.isPlayerPiece = isPlayerPiece;
    }

    public void setNumber(int n){
        this.number = n;
    }
    public int getNumber(){
        return number;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
