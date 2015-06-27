package main.trespass;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;
import java.util.UUID;


/**
 * Created by haolidu on 6/23/15.
 */
public class StorageService {
    public interface Callback{
        void saved();
    }
    private Socket mSocket;
    private String TAG = StorageService.class.getCanonicalName();
    private Activity activity;
    /**
     *
     */
    protected StorageService(Context c){
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
    protected String getDeviceString(){

        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }
}
