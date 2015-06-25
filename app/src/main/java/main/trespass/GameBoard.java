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
    }

    public int checkForWin(){
        //return 0 for no win
        //return 1 for player win
        //return 2 for opponent win
        return 0;
    }

}
