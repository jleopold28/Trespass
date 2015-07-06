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

    int[] prevTile = new int[] {-1,-1};
    boolean hasNumSelected = false;
    //for image button at 5,0
    public void clickOnIB50(View v) {
        clickOnIB(5,0);
    }

    //for image button at 3,0
    public void clickOnIB30(View v) {
        clickOnIB(3,0);
    }

    public void clickOnIB(int r, int c) {
        if (g.gb.getTile(r,c).isBlank()) {
            if (hasNumSelected) {
                g.gb.setMove(g.gb.getTile(prevTile[0],prevTile[1]),g.gb.getTile(r,c));
                gameButtons[r][c].setBackgroundResource(getResources().
                        getIdentifier("num" + Integer.toString(g.gb.getTile(prevTile[0],prevTile[1]).getNumber()), "drawable", this.getPackageName()));
                gameButtons[prevTile[0]][prevTile[1]].setBackgroundResource(R.drawable.blank);
                prevTile[0]=-1;
                prevTile[1]=-1;
                hasNumSelected=false;
            }
        } else {
            if (g.gb.getTile(r,c).isPlayerPiece()) {
                if (hasNumSelected) {
                    gameButtons[prevTile[0]][prevTile[1]].setBackgroundResource(getResources().
                            getIdentifier("num" + Integer.toString(g.gb.getTile(prevTile[0], prevTile[1]).getNumber()), "drawable", this.getPackageName()));
                    cleanBlankTile();
                }
                int currNum = g.gb.getTile(r, c).getNumber();
                prevTile[0] = r;
                prevTile[1] = c;
                gameButtons[r][c].setBackgroundResource(getResources().
                        getIdentifier("selectednum" + Integer.toString(currNum), "drawable", this.getPackageName()));
                for (int[] validTile : g.gb.getValidTiles(r, c)) {
                    gameButtons[validTile[0]][validTile[1]].setBackgroundResource(getResources().
                            getIdentifier("validtile", "drawable", this.getPackageName()));
                }
                hasNumSelected = true;
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
