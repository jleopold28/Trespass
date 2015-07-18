package main.trespass;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {
    static Player p;
    static InitializationObject init;
    static GameBoard gb = new GameBoard();
    //static boolean isTurn = true; // true if it is the player's turn; false if it the opponent's turn
    private static GameDriver g;
    private static Socket mSocket;
    private static String TAG = GameDriver.class.getCanonicalName();
    private static int gameID;
    private static String deviceString;
    private static SocketEventInterface s;
    private static String opponentTiles;
    protected static boolean myTurn = false;
    protected static boolean myStart = false;
    private static Emitter.Listener onDataErrorListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String errorString;
            try {
                errorString = (String) args[0];
            } catch (ClassCastException e) {
                Log.e(TAG, "Expecting string in onDataErrorListener.");
                return;
            }
            s.onDataError(errorString);
        }
    };
    private static Emitter.Listener onErrorListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String errorString;
            try {
                errorString = (String) args[0];
            } catch (ClassCastException e) {
                Log.e(TAG, "Expecting string in onErrorListener.");
                return;
            }
            s.onError(errorString);
        }
    };
    private static Emitter.Listener onInfoListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String infoString;
            try {
                infoString = (String) args[0];
            } catch (ClassCastException e) {
                Log.e(TAG, "Expecting string in onInfoListener.");
                return;
            }
            s.onInfo(infoString);
        }
    };
    private static Emitter.Listener onUserInfoListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            opponentTiles = (String) args[0];
        }
    };

    private static Emitter.Listener onGameListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try{
                gameID = (Integer)args[0];
            } catch(ClassCastException e){
                Log.e(TAG, "Expecting integer game id in onGameListener.");
            }
            s.onGame();
        }
    };
    private static Emitter.Listener onMoveListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject json = null;

            try {
                String jsonString = (String)args[0];
                if(jsonString.length() == 0){
                    myTurn = true;
                    myStart = true;
                    return;
                }
                json = new JSONObject(jsonString);
            } catch (ClassCastException e) {
                Log.e(TAG, "Expecting json string in onMoveListener.");
                return;
            } catch(JSONException e){
                Log.e(TAG, "Invalid json string in onMoveListener.");
                return;
            }
            s.onMove(json);
        }
    };
    private static Emitter.Listener onEndListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String endString;
            try {
                endString = (String) args[0];
            } catch (ClassCastException e) {
                Log.e(TAG, "Expecting win/lose string in onEndListener.");
                return;
            }
            s.onEnd(endString);
        }
    };

    public static GameDriver getInstance() {
        if (g == null) {
            g = new GameDriver();
        }
        return g;
    }

    public static void createPlayer(String username, int avatar) {
        p = new Player(username, avatar);
    }

    public static void setSecretNumber(int num) {
        p.setSecretNum(num);
    }

    public static void createInitializationObject(int[][] arr) {
        init = new InitializationObject(arr);
    }

    public static InitializationObject getInitializationObject() {
        return init;
    }

    public static void setSocketListener(SocketEventInterface s) {
        GameDriver.s = s;
    }

    public static void connectSocket() {

        try {
            mSocket = IO.socket("http://haolidu.me:3000/trespass");
            Log.v("Created IO socket:" + mSocket.toString(), TAG);
            mSocket.on("dataError", onDataErrorListener);
            mSocket.on("Error", onErrorListener);
            mSocket.on("Info", onInfoListener);
            mSocket.on("userInfo", onUserInfoListener);
            mSocket.on("Game", onGameListener);
            mSocket.on("Move", onMoveListener);
            mSocket.on("End", onEndListener);

            mSocket.connect();
        } catch (URISyntaxException e) {
        }
    }

    public static boolean isSocketConnected(){
        if(mSocket != null)
            return mSocket.connected();
        return false;
    }

    protected static String getDeviceString() {
        Context c;
        try{
            c = (Context)s;
        } catch(ClassCastException e){
            return UUID.randomUUID().toString();
        }
        if(deviceString != null){
            return deviceString;
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        String deviceString = settings.getString("device_id", "");
        if(deviceString.length() == 0){
            final TelephonyManager tm = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceString = deviceUuid.toString();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("device_id", deviceString);
            editor.commit();
            Log.d(TAG, "New device id: " + deviceString);
        } else {
            Log.d(TAG, "Using original device id: " + deviceString);
        }
        GameDriver.deviceString = deviceString;
        return deviceString;
    }

    public static void sendUserInfo(JSONObject o) {
        try {
            o.put("device_id", getDeviceString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        mSocket.emit("user_info", o);
    }

    public static void new_move(int[] from_position, int[] to_position, int game_piece){
        JSONObject o = new JSONObject();
        JSONObject from = new JSONObject();
        JSONObject to = new JSONObject();
        try {
            from.put("row", from_position[0]);
            from.put("column", from_position[1]);

            to.put("row", to_position[0]);
            to.put("column", to_position[1]);

            o.put("game",gameID);
            o.put("device_id",getDeviceString());
            o.put("from_position", from);
            o.put("to_position", to);
            o.put("game_piece", game_piece);

        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
        mSocket.emit("new_move",o);
    }
    public static void start_game(){
        JSONObject o = new JSONObject();

        try{
            o.put("game",gameID);
            o.put("device_id", getDeviceString());
            o.put("secret_number", p.getSecretNum());
            o.put("avatar", p.getAvatar());
            o.put("username", p.getUsername());

        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
        mSocket.emit("start_game",o);
    }

    public static void setUpGameBoard() {
        //take values out of local DB

        //set player side numbers
        int[][] arr = init.getTiles();
        gb.getTile(4, 0).setNumber(arr[0][0]);
        gb.getTile(4, 0).setIsPlayerPiece(true);
        gb.getTile(4, 1).setNumber(arr[0][1]);
        gb.getTile(4, 1).setIsPlayerPiece(true);
        gb.getTile(4, 2).setNumber(arr[0][2]);
        gb.getTile(4, 2).setIsPlayerPiece(true);
        gb.getTile(4, 3).setNumber(arr[0][3]);
        gb.getTile(4, 3).setIsPlayerPiece(true);
        gb.getTile(4, 4).setNumber(arr[0][4]);
        gb.getTile(4, 4).setIsPlayerPiece(true);

        gb.getTile(5, 0).setNumber(arr[1][0]);
        gb.getTile(5, 0).setIsPlayerPiece(true);
        gb.getTile(5, 1).setNumber(arr[1][1]);
        gb.getTile(5, 1).setIsPlayerPiece(true);
        gb.getTile(5, 2).setNumber(arr[1][2]);
        gb.getTile(5, 2).setIsPlayerPiece(true);
        gb.getTile(5, 3).setNumber(arr[1][3]);
        gb.getTile(5, 3).setIsPlayerPiece(true);
        gb.getTile(5, 4).setNumber(arr[1][4]);
        gb.getTile(5, 4).setIsPlayerPiece(true);

        //set up opponent numbers
        Log.d("test", opponentTiles);

        gb.getTile(0, 0).setNumber(Integer.parseInt(opponentTiles.substring(9)));
        //gb.getTile(0,0).setNumber(4);
        gb.getTile(0, 1).setNumber(Integer.parseInt(opponentTiles.substring(8, 9)));
        gb.getTile(0, 2).setNumber(Integer.parseInt(opponentTiles.substring(7, 8)));
        gb.getTile(0, 3).setNumber(Integer.parseInt(opponentTiles.substring(6, 7)));
        gb.getTile(0, 4).setNumber(Integer.parseInt(opponentTiles.substring(5, 6)));

        gb.getTile(1, 0).setNumber(Integer.parseInt(opponentTiles.substring(4, 5)));
        gb.getTile(1, 1).setNumber(Integer.parseInt(opponentTiles.substring(3, 4)));
        gb.getTile(1, 2).setNumber(Integer.parseInt(opponentTiles.substring(2, 3)));
        gb.getTile(1, 3).setNumber(Integer.parseInt(opponentTiles.substring(1, 2)));
        gb.getTile(1, 4).setNumber(Integer.parseInt(opponentTiles.substring(0, 1)));
    }

    public static void playGame(TileChangeNotifier t) {
        start_game();
        //while( gb.checkForWin() == 0){

        //who goes first?
        //server generates random num (1 or 2)
        //chooses who goes first
        //loop
        //player 1
        //notify
        //player 2
        //notify
        //while no win condition

        //n.notifyTileChanges();
    }

    public static void end(){
        mSocket.disconnect();
        g = null;
    }

    public interface SocketEventInterface {
        void onDataError(String s);

        void onError(String s);

        void onInfo(String s);

        void onGame();

        void onMove(JSONObject json);

        void onEnd(String s);
    }
}
