package main.trespass;


/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {

    private static GameDriver g;
    static Player p;
    static InitializationObject init;
    private static NotifyInterface n;
    static GameBoard gb = new GameBoard();

    public static GameDriver getInstance(){
        if (g == null){
            g = new GameDriver();
        }
        return g;
    }

    public static void createPlayer(String username, int avatar){
        p = new Player(username,avatar);
    }

    public static void setSecretNumber(int num){
        p.setSecretNum(num);
    }

    public static void createInitializationObject(int[][] arr){
        init = new InitializationObject(arr);
    }
    public static InitializationObject getInitializationObject(){
        return init;
    }

    public static void setListener(NotifyInterface n){
        GameDriver.n = n;
    }
    public static void setUpGameBoard(){
        //take values out of local DB

        //set player side numbers
        int[][] arr = init.getTiles();
        gb.getTile(4,0).setNumber(arr[0][0]);
        gb.getTile(4,0).setIsPlayerPiece(true);
        gb.getTile(4,0).setCoordinates(4,0);
        gb.getTile(4,1).setNumber(arr[0][1]);
        gb.getTile(4,1).setIsPlayerPiece(true);
        gb.getTile(4,2).setNumber(arr[0][2]);
        gb.getTile(4,2).setIsPlayerPiece(true);
        gb.getTile(4,3).setNumber(arr[0][3]);
        gb.getTile(4,3).setIsPlayerPiece(true);
        gb.getTile(4,4).setNumber(arr[0][4]);
        gb.getTile(4,4).setIsPlayerPiece(true);

        gb.getTile(5,0).setNumber(arr[1][0]);
        gb.getTile(5,0).setIsPlayerPiece(true);
        gb.getTile(5,1).setNumber(arr[1][1]);
        gb.getTile(5,1).setIsPlayerPiece(true);
        gb.getTile(5,2).setNumber(arr[1][2]);
        gb.getTile(5,2).setIsPlayerPiece(true);
        gb.getTile(5,3).setNumber(arr[1][3]);
        gb.getTile(5,3).setIsPlayerPiece(true);
        gb.getTile(5,4).setNumber(arr[1][4]);
        gb.getTile(5,4).setIsPlayerPiece(true);

        //set up opponent numbers
        //gb.getTile(0,0).setNumber();
        //gb.getTile(0,1).setNumber();
        //gb.getTile(0,2).setNumber();
        //gb.getTile(0,3).setNumber();
        //gb.getTile(0,4).setNumber();

        //gb.getTile(1,0).setNumber();
        //gb.getTile(1,1).setNumber();
        //gb.getTile(1,2).setNumber();
        //gb.getTile(1,3).setNumber();
        //gb.getTile(1,4).setNumber();
    }
    public static void playGame(){
        //who goes first?
        //server generates random num (1 or 2)
        //chooses who goes first
        //loop
            //player 1
            //notify
            //player 2
            //notify
        //while no win condition

        n.notifyTileChanges();

    }

}
