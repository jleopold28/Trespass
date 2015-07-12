package main.trespass;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by jleopold on 6/23/2015.
 */
public class GameDriver {
    public interface socketEventInterface{
        void onDataError(String s);
        void onError(String s);
        void onInfo(String s);
        void onGame();
        void onMove(JSONObject json);
        void onEnd(String s);
    }
    private static GameDriver g;
    static Player p;
    static InitializationObject init;
    private static NotifyInterface n;
    static GameBoard gb = new GameBoard();
    private static Socket mSocket;
    private static String TAG = GameDriver.class.getCanonicalName();
    private static int gameID;
    private static socketEventInterface s;
    private static String opponentTiles;


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

    public static boolean connectSocket(socketEventInterface s){
        GameDriver.s = s;

        try {
            mSocket = IO.socket("http://haolidu.me:3000");
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
            return false;
        }
        Log.v("Connected",TAG);
        return true;
    }

    protected static String getDeviceString(){
        return UUID.randomUUID().toString();
    }

    public static void sendUserInfo(JSONObject o){
        try{
            o.put("device_id", getDeviceString());
        }
        catch(Exception e){
        }
        mSocket.emit("user_info", o);
    }
    private static Emitter.Listener onDataErrorListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            return;
        }
    };
    private static Emitter.Listener onErrorListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            return;
        }
    };
    private static Emitter.Listener onInfoListener= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            return;
        }
    };
    private static Emitter.Listener onUserInfoListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            opponentTiles = (String) args[0];

            return;
        }
    };
    private static Emitter.Listener onGameListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("test", "onGameListener was called");
            s.onGame();
        }
    };
    private static Emitter.Listener onMoveListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

        }
    };
    private static Emitter.Listener onEndListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            return;
        }
    };
    public static void setListener(NotifyInterface n){
        GameDriver.n = n;
    }
    public static void setUpGameBoard(){
        //take values out of local DB

        //set player side numbers
        int[][] arr = init.getTiles();
        gb.getTile(4,0).setNumber(arr[0][0]);
        gb.getTile(4,0).setIsPlayerPiece(true);
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
        Log.d("test", opponentTiles);

        gb.getTile(0,0).setNumber(Integer.parseInt(opponentTiles.substring(9)));
        //gb.getTile(0,0).setNumber(4);
        gb.getTile(0,1).setNumber(Integer.parseInt(opponentTiles.substring(8,9)));
        gb.getTile(0,2).setNumber(Integer.parseInt(opponentTiles.substring(7,8)));
        gb.getTile(0,3).setNumber(Integer.parseInt(opponentTiles.substring(6,7)));
        gb.getTile(0,4).setNumber(Integer.parseInt(opponentTiles.substring(5,6)));

        gb.getTile(1,0).setNumber(Integer.parseInt(opponentTiles.substring(4,5)));
        gb.getTile(1,1).setNumber(Integer.parseInt(opponentTiles.substring(3,4)));
        gb.getTile(1,2).setNumber(Integer.parseInt(opponentTiles.substring(2,3)));
        gb.getTile(1,3).setNumber(Integer.parseInt(opponentTiles.substring(1,2)));
        gb.getTile(1,4).setNumber(Integer.parseInt(opponentTiles.substring(0,1)));
    }
    public static void playGame(){
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
        //}
        if(gb.checkForWin() == 1){

            //player 1 wins -> splash screen
        }
        if(gb.checkForWin() == 2){

            //player 2 wins -> splash screen
        }
    }

}
