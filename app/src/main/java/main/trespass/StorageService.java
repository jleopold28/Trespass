package main.trespass;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;


/**
 * Created by haolidu on 6/23/15.
 */
public class StorageService {
    public interface Callback{
        void saved();
    }
    private Socket mSocket;
    private String TAG = StorageService.class.getCanonicalName();
    /**
     *
     */
    protected StorageService(){
        try {
            mSocket = IO.socket("http://chat.socket.io");
            Log.v("Created IO socket:" + mSocket.toString(), TAG);
        } catch (URISyntaxException e) {
        }
    }

    protected boolean storeInitialGame(InitializationObject gI, Player p){
        Callback callbackGame;
        try{
            callbackGame = (Callback)gI;
        } catch(ClassCastException e){
           return true;
        }
        callbackGame.saved();
        return true;
    }

    protected boolean storePlayer(Player p){
        return true;
    }

    protected boolean storeMove(Move m){
        return true;
    }

}
