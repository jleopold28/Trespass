package main.trespass;

/**
 * Created by haolidu on 7/14/15.
 */
public class GameThread implements Runnable{
    private TileChangeNotifier listener;

    public GameThread(TileChangeNotifier n){
        this.listener = n;
    }
    @Override
    public void run() {

    }
}
