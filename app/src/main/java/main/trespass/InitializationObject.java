package main.trespass;

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

}
