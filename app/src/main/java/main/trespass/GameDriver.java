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
        //set values in gb
        int[][] arr = init.getTiles();
        gb.getTile(0,0).setNumber(arr[0][0]);





    }
    public static void playGame(){

        //send player and init to storage
        //send moves
        //receive moves
        //go to win or lose state /
        n.notifyTileChanges();

    }

}
