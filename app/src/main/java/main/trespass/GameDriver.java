package main.trespass;


/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {
    private static GameDriver g;
    static Player p;
    static InitializationObject init;
    private static NotifyInterface n;
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
    public static void playGame(){
        int a = init.getTiles()[0][0];
        int b = init.getTiles()[0][1];
        int c = init.getTiles()[0][2];
        int d = init.getTiles()[0][3];
        int e = init.getTiles()[0][4];
        int f = init.getTiles()[1][0];
        int g = init.getTiles()[1][1];
        int h = init.getTiles()[1][2];
        int i = init.getTiles()[1][3];
        int j = init.getTiles()[1][4];

        System.out.println("Tiles:");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
        System.out.println(f);
        System.out.println(g);
        System.out.println(h);
        System.out.println(i);
        System.out.println(j);
        System.out.println("Avatar is: " + p.getAvatar() + '\n');
        System.out.println("Username is: " + p.getUsername() + '\n');
        System.out.println("Secret Number is: " + p.getSecretNum() + '\n');

        //send player and init to storage
        //send moves
        //receive moves
        //go to win or lose state /
        n.notifyTileChanges();

    }

}
