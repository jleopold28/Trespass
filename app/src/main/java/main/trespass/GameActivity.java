package main.trespass;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class GameActivity extends Activity implements NotifyInterface {


    public interface Notify{
        public void notifyTileChanges();
    }

    GameDriver g;
    private ImageButton gameButtons[][];
    private boolean gameOver = false;
    private boolean pieceSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameButtons = new ImageButton[6][5];

        gameButtons[0][0] = (ImageButton) findViewById(R.id.IB00);
        gameButtons[0][1] = (ImageButton) findViewById(R.id.IB01);
        gameButtons[0][2] = (ImageButton) findViewById(R.id.IB02);
        gameButtons[0][3] = (ImageButton) findViewById(R.id.IB03);
        gameButtons[0][4] = (ImageButton) findViewById(R.id.IB04);

        gameButtons[1][0] = (ImageButton) findViewById(R.id.IB10);
        gameButtons[1][1] = (ImageButton) findViewById(R.id.IB11);
        gameButtons[1][2] = (ImageButton) findViewById(R.id.IB12);
        gameButtons[1][3] = (ImageButton) findViewById(R.id.IB13);
        gameButtons[1][4] = (ImageButton) findViewById(R.id.IB14);

        gameButtons[2][0] = (ImageButton) findViewById(R.id.IB20);
        gameButtons[2][1] = (ImageButton) findViewById(R.id.IB21);
        gameButtons[2][2] = (ImageButton) findViewById(R.id.IB22);
        gameButtons[2][3] = (ImageButton) findViewById(R.id.IB23);
        gameButtons[2][4] = (ImageButton) findViewById(R.id.IB24);

        gameButtons[3][0] = (ImageButton) findViewById(R.id.IB30);
        gameButtons[3][1] = (ImageButton) findViewById(R.id.IB31);
        gameButtons[3][2] = (ImageButton) findViewById(R.id.IB32);
        gameButtons[3][3] = (ImageButton) findViewById(R.id.IB33);
        gameButtons[3][4] = (ImageButton) findViewById(R.id.IB34);

        gameButtons[4][0] = (ImageButton) findViewById(R.id.IB40);
        gameButtons[4][1] = (ImageButton) findViewById(R.id.IB41);
        gameButtons[4][2] = (ImageButton) findViewById(R.id.IB42);
        gameButtons[4][3] = (ImageButton) findViewById(R.id.IB43);
        gameButtons[4][4] = (ImageButton) findViewById(R.id.IB44);

        gameButtons[5][0] = (ImageButton) findViewById(R.id.IB50);
        gameButtons[5][1] = (ImageButton) findViewById(R.id.IB51);
        gameButtons[5][2] = (ImageButton) findViewById(R.id.IB52);
        gameButtons[5][3] = (ImageButton) findViewById(R.id.IB53);
        gameButtons[5][4] = (ImageButton) findViewById(R.id.IB54);
        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNewGame(){
        g = GameDriver.getInstance();
        InitializationObject i = g.getInitializationObject();
        int[][] arr = i.getTiles();

        String num40 = "num" + arr[0][0];
        gameButtons[4][0].setBackgroundResource(getResources().getIdentifier(num40,"drawable",this.getPackageName()));

        String num41 = "num" + arr[0][1];
        gameButtons[4][1].setBackgroundResource(getResources().getIdentifier(num41,"drawable",this.getPackageName()));

        String num42 = "num" + arr[0][2];
        gameButtons[4][2].setBackgroundResource(getResources().getIdentifier(num42,"drawable",this.getPackageName()));

        String num43 = "num" + arr[0][3];
        gameButtons[4][3].setBackgroundResource(getResources().getIdentifier(num43,"drawable",this.getPackageName()));

        String num44 = "num" + arr[0][4];
        gameButtons[4][4].setBackgroundResource(getResources().getIdentifier(num44,"drawable",this.getPackageName()));

        String num50 = "num" + arr[1][0];
        gameButtons[5][0].setBackgroundResource(getResources().getIdentifier(num50,"drawable",this.getPackageName()));

        String num51 = "num" + arr[1][1];
        gameButtons[5][1].setBackgroundResource(getResources().getIdentifier(num51,"drawable",this.getPackageName()));

        String num52 = "num" + arr[1][2];
        gameButtons[5][2].setBackgroundResource(getResources().getIdentifier(num52,"drawable",this.getPackageName()));

        String num53 = "num" + arr[1][3];
        gameButtons[5][3].setBackgroundResource(getResources().getIdentifier(num53,"drawable",this.getPackageName()));

        String num54 = "num" + arr[1][4];
        gameButtons[5][4].setBackgroundResource(getResources().getIdentifier(num54,"drawable",this.getPackageName()));

        //set up oppenent side
        //String num00 = "oppnum" + ??
        //gameButtons[0][0].setBackgroundResource(getResources().getIdentifier(num00,"drawable", this.getPackageName()));

        g.setUpGameBoard();
        g.playGame();
    }
    public void notifyTileChanges(){

    }

    int[] prevTileCoordinate = new int[]{-1,-1};
    boolean hasTileSelected = false;

    public void clickOnIB00(View v) {clickOnIB(0,0);}
    public void clickOnIB01(View v) {clickOnIB(0,1);}
    public void clickOnIB02(View v) {clickOnIB(0,2);}
    public void clickOnIB03(View v) {clickOnIB(0,3);}
    public void clickOnIB04(View v) {clickOnIB(0,4);}

    public void clickOnIB10(View v) {clickOnIB(1,0);}
    public void clickOnIB11(View v) {clickOnIB(1,1);}
    public void clickOnIB12(View v) {clickOnIB(1,2);}
    public void clickOnIB13(View v) {clickOnIB(1,3);}
    public void clickOnIB14(View v) {clickOnIB(1,4);}

    public void clickOnIB20(View v) {clickOnIB(2,0);}
    public void clickOnIB21(View v) {clickOnIB(2,1);}
    public void clickOnIB22(View v) {clickOnIB(2,2);}
    public void clickOnIB23(View v) {clickOnIB(2,3);}
    public void clickOnIB24(View v) {clickOnIB(2,4);}

    public void clickOnIB30(View v) {clickOnIB(3,0);}
    public void clickOnIB31(View v) {clickOnIB(3,1);}
    public void clickOnIB32(View v) {clickOnIB(3,2);}
    public void clickOnIB33(View v) {clickOnIB(3,3);}
    public void clickOnIB34(View v) {clickOnIB(3,4);}

    public void clickOnIB40(View v) {clickOnIB(4,0);}
    public void clickOnIB41(View v) {clickOnIB(4,1);}
    public void clickOnIB42(View v) {clickOnIB(4,2);}
    public void clickOnIB43(View v) {clickOnIB(4,3);}
    public void clickOnIB44(View v) {clickOnIB(4,4);}

    public void clickOnIB50(View v) {clickOnIB(5,0);}
    public void clickOnIB51(View v) {clickOnIB(5,1);}
    public void clickOnIB52(View v) {clickOnIB(5,2);}
    public void clickOnIB53(View v) {clickOnIB(5,3);}
    public void clickOnIB54(View v) {clickOnIB(5,4);}

    public void clickOnIB(int row, int col) {
        if (g.gb.getTile(row,col).isBlank()) {
            if (hasTileSelected) {
                g.gb.setMove(g.gb.getTile(prevTileCoordinate[0],prevTileCoordinate[1]),g.gb.getTile(row,col));
                gameButtons[row][col].setBackgroundResource(getResources().
                        getIdentifier("num" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                gameButtons[prevTileCoordinate[0]][prevTileCoordinate[1]].setBackgroundResource(R.drawable.blank);
                prevTileCoordinate[0]=-1;
                prevTileCoordinate[1]=-1;
                hasTileSelected=false;
            }
        } else { //its a game piece
            if (g.gb.getTile(row,col).isPlayerPiece()) { //if its owned by you
                if (hasTileSelected) { //we have already selected a tile
                    gameButtons[prevTileCoordinate[0]][prevTileCoordinate[1]].setBackgroundResource(getResources().
                            getIdentifier("num" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                    cleanBlankTile();
                }
                int currNum = g.gb.getTile(row, col).getNumber();
                prevTileCoordinate[0] = row;
                prevTileCoordinate[1] = col;
                gameButtons[row][col].setBackgroundResource(getResources().
                        getIdentifier("selectednum" + Integer.toString(currNum), "drawable", this.getPackageName()));
                for (int[] validTile : g.gb.getValidTiles(row, col)) {
                    gameButtons[validTile[0]][validTile[1]].setBackgroundResource(getResources().
                            getIdentifier("validtile", "drawable", this.getPackageName()));
                }
                hasTileSelected = true;
            }
        }
    }
    public void cleanBlankTile() {
        for (int i=0;i<6;i++){
            for (int j=0;j<5;j++) {
                if (g.gb.getTile(i,j).isBlank()) {
                    gameButtons[i][j].setBackgroundResource(R.drawable.blank);
                }
            }
        }
    }
}
