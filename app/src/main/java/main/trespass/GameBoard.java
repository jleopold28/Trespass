package main.trespass;

/**
 * Created by jleopold on 6/24/2015.
 */
public class GameBoard {
    private Tile gameBoard[][];

    private final int rowCount = 6;
    private final int colCount = 5;

    public GameBoard(){
        gameBoard = new Tile[rowCount][colCount];
    }

    public void setMove(Tile t1, Tile t2){
        int startRow = t1.getRow();
        int startCol = t1.getCol();
        int startNumber = t1.getNumber();

        int endRow = t2.getRow();
        int endCol = t2.getCol();
        int endNumber = t2.getNumber();

        gameBoard[startRow][startCol].setNumber(endNumber);
        gameBoard[endRow][endCol].setNumber(startNumber);
    }

    public int checkForWin(){
        //return 0 for no win
        //return 1 for player win
        //return 2 for opponent win
        return 0;
    }

}
