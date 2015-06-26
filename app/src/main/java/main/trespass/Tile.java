package main.trespass;

/**
 * Created by jleopold on 6/24/2015.
 */
public class Tile {
    private int number;
    private boolean isPlayerPiece = false;
    private boolean isBlank = true;
    private int row;
    private int col;

    public void setNumber(int n){
        this.number = n;
        this.isBlank = false;
    }
    public void setBlank(boolean b){
        this.isBlank = b;
    }
    public void setIsPlayerPiece(boolean b){
        this.isPlayerPiece = b;
    }
    public void setCoordinates(int row, int col){
        this.row = row;
        this.col = col;
    }
    public boolean isBlank(){
        return isBlank;
    }
    public boolean isPlayerPiece(){
        return isPlayerPiece;
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
