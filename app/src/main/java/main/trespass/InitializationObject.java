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
    public int[][] getTilesFromJSON(JSONObject o){
        String t = "";
        int[][] arr = new int[2][5];
        try {
            t += o.get("tiles").toString();
        }
        catch(Exception e){
        }
        arr[0][0] = (int)t.charAt(0);
        arr[0][1] = (int)t.charAt(1);
        arr[0][2] = (int)t.charAt(2);
        arr[0][3] = (int)t.charAt(3);
        arr[0][4] = (int)t.charAt(4);

        arr[1][0] = (int)t.charAt(5);
        arr[1][1] = (int)t.charAt(6);
        arr[1][2] = (int)t.charAt(7);
        arr[1][3] = (int)t.charAt(8);
        arr[1][4] = (int)t.charAt(9);
        return arr;
    }
}
