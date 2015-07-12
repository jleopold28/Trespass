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
            json.put("tiles", "");
        }
        catch (JSONException e){}
        return null;
    }
}
