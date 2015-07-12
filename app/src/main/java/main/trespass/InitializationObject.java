package main.trespass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jleopold on 6/23/2015.
 */
public class InitializationObject {
    //tile arrangmet
    private int[][] tiles;

    public InitializationObject(int[][] arr){
        this.tiles = arr;
    }

    public int[][] getTiles(){
        return tiles;
    }

    public JSONObject getJSONObject(){
        JSONObject json = new JSONObject();
        try{
            String tileString = "" + tiles[0][0] + tiles[0][1] + tiles[0][2] + tiles[0][3] + tiles[0][4]
                    + tiles[1][0] + tiles[1][1] + tiles[1][2] + tiles[1][3] + tiles[1][4];
            json.put("tiles", tileString);
        }
        catch (JSONException e){}
        return json;
    }
}
