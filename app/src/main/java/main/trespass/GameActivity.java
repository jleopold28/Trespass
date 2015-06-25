package main.trespass;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class GameActivity extends Activity {

    private GameBoard game;
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

        game = new GameBoard();

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
        GameDriver g = GameDriver.getInstance();
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

        //gameButtons[4][0].setBackgroundDrawable(getResources().getIdentifier(test, "drawable","com.app"));
        //for(int row = 0; row < 2; row++){
        //    for(int col = 0; col < 5; col++){
                //gameButtons[row][col].setBackground(getResources().getDrawable(R.drawable.red));
        //        gameButtons[row][col].setBackgroundDrawable(getResources().getDrawable(R.drawable.red));
        //    }
        //}

        //for(int row = 4; row < 6; row++){
        //    for(int col = 0; col < 5; col++){
        //        gameButtons[row][col].setBackgroundDrawable(getResources().getDrawable(R.drawable.green));
        //    }
        //}


    }
}
