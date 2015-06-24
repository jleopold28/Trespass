package main.trespass;


/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {
    private static GameDriver g;
    static Player p;
    static InitializationObject init;

    public static GameDriver getInstance(){
        if (g == null){
            g = new GameDriver();
        }
        return g;
    }

    public static void createPlayer(String username, int avatar){
        Player p = new Player(username,avatar);
    }

    public static void setSecretNumber(int num){
        //int n = Integer.parseInt(num);
        //p.setSecretNum(num);
        System.out.println(num);
    }

    public static void createInitializationObject(int[][] arr){
        //Convert String array to int array
        InitializationObject init = new InitializationObject(arr);
    }

    public static void playGame(){
        System.out.println("fuckkkkk\n\n\n");
        //System.out.println(init.getTiles().toString() + '\n');
        //System.out.println("Avatar is: " + p.getAvatar() + '\n');
        //System.out.println("Username is: " + p.getUsername() + '\n');
        //System.out.println("Secret Number is: " + p.getSecretNum() + '\n');

        //send player and init to storage
        //send moves
        //receive moves
        //go to win or lose state /

    }

    public static void test(){
        System.out.println("testt");
    }
}
