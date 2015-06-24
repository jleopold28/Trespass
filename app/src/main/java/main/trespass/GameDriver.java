package main.trespass;

/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {
    static Player p;
    static InitializationObject init;

    public static void createPlayer(String username, int avatar){
        Player p = new Player(username,avatar);
    }

    public static void setSecretNumber(String num){
        int n = Integer.parseInt(num);
        p.setSecretNum(n);
    }

    public static void createInitializationObject(String[][] arr){
        //convert to ints
        //InitializationObject init = new InitializationObject(arr);
    }

    public static void playGame(){
        //send player and init to storage
        //send moves
        //receive moves
        //go to win or lose state

    }
}
