package main.trespass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class GameActivity extends Activity implements GameDriver.SocketEventInterface, TileChangeNotifier {

    TextView oppoUsername;
    ImageView oppoAvatar;
    TextView playerUsername;
    ImageView playerAvatar;
    GameDriver g;
    int[] prevTileCoordinate = new int[]{-1,-1};
    int[] lastMovedTile = new int[]{2,0};
    boolean hasTileSelected = false;
    private ImageButton gameButtons[][];
    private boolean gameOver = false;
    private boolean pieceSelected = false;
    private static String TAG = GameActivity.class.getCanonicalName();

    @Override
    public void onDataError(String s) {
    }

    @Override
    public void onError(final String s) {
        final Context c = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(c)
                        .setTitle("Error")
                        .setMessage(s)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                g.end();
                                startActivity(new Intent(GameActivity.this,InitializationActivity.class));
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public void onInfo(final String s) {
        final Context c = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(c)
                        .setTitle("Wait...")
                        .setMessage(s)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

    }

    @Override
    public void onGame() {
    }

    @Override
    public void onMove(final JSONObject json) {
        //Log.e("MOVE FOUND", json.toString());
        if(json == null){
            onInfo("Your turn");
            return;
        }
        final Context c = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(c, json.toString(), Toast.LENGTH_LONG).show();
                int prev_row = 0;
                int prev_col = 0;
                int to_row = 0;
                int to_col = 0;

                try {
                    JSONObject from = json.getJSONObject("from");
                    prev_row = 5 - from.getInt("row");
                    prev_col = 4 - from.getInt("column");

                    JSONObject to = json.getJSONObject("to");
                    to_row = 5 - to.getInt("row");
                    to_col = 4 - to.getInt("column");
                    lastMovedTile[0] = to_row;
                    lastMovedTile[1] = to_col;

                } catch (Exception e) {
                    Log.e(TAG, "somethings wrong with the move logic!");
                }
                g.gb.setMove(prev_row, prev_col, to_row, to_col);

                if(g.gb.getTile(to_row,to_col).isPlayerPiece()){gameButtons[to_row][to_col].setBackgroundResource(getResources().
                        getIdentifier("num" + Integer.toString(g.gb.getTile(prev_row,prev_col).getNumber()), "drawable", c.getPackageName()));

                }
                else {
                    gameButtons[to_row][to_col].setBackgroundResource(getResources().
                            getIdentifier("opponum" + Integer.toString(g.gb.getTile(prev_row, prev_col).getNumber()), "drawable", c.getPackageName()));
                }
                gameButtons[prev_row][prev_col].setBackgroundResource(R.drawable.blank);
                g.myTurn = true;
                Toast.makeText(c, "Your turn", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onEnd(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(s.equals("You win")){
                    showEndGameDialog(true);
                } else {
                    showEndGameDialog(false);
                }
            }
        });
        g.end();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        oppoUsername = (TextView) this.findViewById(R.id.oppoUsername);
        oppoAvatar = (ImageView) this.findViewById(R.id.oppoAvatarIV);
        playerUsername = (TextView) this.findViewById(R.id.playerUsername);
        playerAvatar = (ImageView) this.findViewById(R.id.playerAvatarIV);


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
        GameDriver.setSocketListener(this);
        setOppoInfo(g.opponentUsername, g.opponentAvatar);
        setPlayerInfo(g.p.getUsername(), g.p.getAvatar());
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("End Game?")
                .setMessage("Are you sure you want to leave the game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        g.end();
                        startActivity(new Intent(GameActivity.this,InitializationActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    private void startNewGame() {
        g = GameDriver.getInstance();
        InitializationObject i = g.getInitializationObject();
        int[][] arr = i.getTiles();
        g.setUpGameBoard();

        String num40 = "num" + arr[0][0];
        gameButtons[4][0].setBackgroundResource(getResources().getIdentifier(num40, "drawable", this.getPackageName()));

        String num41 = "num" + arr[0][1];
        gameButtons[4][1].setBackgroundResource(getResources().getIdentifier(num41, "drawable", this.getPackageName()));

        String num42 = "num" + arr[0][2];
        gameButtons[4][2].setBackgroundResource(getResources().getIdentifier(num42, "drawable", this.getPackageName()));

        String num43 = "num" + arr[0][3];
        gameButtons[4][3].setBackgroundResource(getResources().getIdentifier(num43, "drawable", this.getPackageName()));

        String num44 = "num" + arr[0][4];
        gameButtons[4][4].setBackgroundResource(getResources().getIdentifier(num44, "drawable", this.getPackageName()));

        String num50 = "num" + arr[1][0];
        gameButtons[5][0].setBackgroundResource(getResources().getIdentifier(num50, "drawable", this.getPackageName()));

        String num51 = "num" + arr[1][1];
        gameButtons[5][1].setBackgroundResource(getResources().getIdentifier(num51, "drawable", this.getPackageName()));

        String num52 = "num" + arr[1][2];
        gameButtons[5][2].setBackgroundResource(getResources().getIdentifier(num52, "drawable", this.getPackageName()));

        String num53 = "num" + arr[1][3];
        gameButtons[5][3].setBackgroundResource(getResources().getIdentifier(num53, "drawable", this.getPackageName()));

        String num54 = "num" + arr[1][4];
        gameButtons[5][4].setBackgroundResource(getResources().getIdentifier(num54, "drawable", this.getPackageName()));

        //Set the opponent side
        String num00 = "opponum" + g.gb.getTile(0, 0).getNumber();
        gameButtons[0][0].setBackgroundResource(getResources().getIdentifier(num00, "drawable", this.getPackageName()));

        String num01 = "opponum" + g.gb.getTile(0, 1).getNumber();
        gameButtons[0][1].setBackgroundResource(getResources().getIdentifier(num01, "drawable", this.getPackageName()));

        String num02 = "opponum" + g.gb.getTile(0, 2).getNumber();
        gameButtons[0][2].setBackgroundResource(getResources().getIdentifier(num02, "drawable", this.getPackageName()));

        String num03 = "opponum" + g.gb.getTile(0, 3).getNumber();
        gameButtons[0][3].setBackgroundResource(getResources().getIdentifier(num03, "drawable", this.getPackageName()));

        String num04 = "opponum" + g.gb.getTile(0, 4).getNumber();
        gameButtons[0][4].setBackgroundResource(getResources().getIdentifier(num04, "drawable", this.getPackageName()));

        String num10 = "opponum" + g.gb.getTile(1, 0).getNumber();
        gameButtons[1][0].setBackgroundResource(getResources().getIdentifier(num10, "drawable", this.getPackageName()));

        String num11 = "opponum" + g.gb.getTile(1, 1).getNumber();
        gameButtons[1][1].setBackgroundResource(getResources().getIdentifier(num11, "drawable", this.getPackageName()));

        String num12 = "opponum" + g.gb.getTile(1, 2).getNumber();
        gameButtons[1][2].setBackgroundResource(getResources().getIdentifier(num12, "drawable", this.getPackageName()));

        String num13 = "opponum" + g.gb.getTile(1, 3).getNumber();
        gameButtons[1][3].setBackgroundResource(getResources().getIdentifier(num13, "drawable", this.getPackageName()));

        String num14 = "opponum" + g.gb.getTile(1, 4).getNumber();
        gameButtons[1][4].setBackgroundResource(getResources().getIdentifier(num14, "drawable", this.getPackageName()));

        //g.setUpGameBoard();
        g.playGame(this);
    }

    public void clickOnIB00(View v) {
        clickOnIB(0, 0);
    }

    public void clickOnIB01(View v) {
        clickOnIB(0, 1);
    }

    public void clickOnIB02(View v) {
        clickOnIB(0, 2);
    }

    public void clickOnIB03(View v) {
        clickOnIB(0, 3);
    }

    public void clickOnIB04(View v) {
        clickOnIB(0, 4);
    }

    public void clickOnIB10(View v) {
        clickOnIB(1, 0);
    }

    public void clickOnIB11(View v) {
        clickOnIB(1, 1);
    }

    public void clickOnIB12(View v) {
        clickOnIB(1, 2);
    }

    public void clickOnIB13(View v) {
        clickOnIB(1, 3);
    }

    public void clickOnIB14(View v) {
        clickOnIB(1, 4);
    }

    public void clickOnIB20(View v) {
        clickOnIB(2, 0);
    }

    public void clickOnIB21(View v) {
        clickOnIB(2, 1);
    }

    public void clickOnIB22(View v) {
        clickOnIB(2, 2);
    }

    public void clickOnIB23(View v) {
        clickOnIB(2, 3);
    }

    public void clickOnIB24(View v) {
        clickOnIB(2, 4);
    }

    public void clickOnIB30(View v) {
        clickOnIB(3, 0);
    }

    public void clickOnIB31(View v) {
        clickOnIB(3, 1);
    }

    public void clickOnIB32(View v) {
        clickOnIB(3, 2);
    }

    public void clickOnIB33(View v) {
        clickOnIB(3, 3);
    }

    public void clickOnIB34(View v) {
        clickOnIB(3, 4);
    }

    public void clickOnIB40(View v) {
        clickOnIB(4, 0);
    }

    public void clickOnIB41(View v) {
        clickOnIB(4, 1);
    }

    public void clickOnIB42(View v) {
        clickOnIB(4, 2);
    }

    public void clickOnIB43(View v) {
        clickOnIB(4, 3);
    }

    public void clickOnIB44(View v) {
        clickOnIB(4, 4);
    }

    public void clickOnIB50(View v) {
        clickOnIB(5, 0);
    }

    public void clickOnIB51(View v) {
        clickOnIB(5, 1);
    }

    public void clickOnIB52(View v) {
        clickOnIB(5, 2);
    }

    public void clickOnIB53(View v) {
        clickOnIB(5, 3);
    }

    public void clickOnIB54(View v) {
        clickOnIB(5, 4);
    }

    public void clickOnIB(int row, int col) {
        //showEndGameDialog(true);
        if(g.myTurn) {
            if (g.gb.getTile(row, col).isBlank()) {
                if (hasTileSelected
                        && g.gb.getValidTiles(prevTileCoordinate[0], prevTileCoordinate[1],g.gb.getTile(prevTileCoordinate[0],prevTileCoordinate[1]).isPlayerPiece()).contains(g.gb.getCoordinate(row, col))){
                    cleanBlankTile();
                    g.gb.setMove(prevTileCoordinate[0], prevTileCoordinate[1], row, col);

                    int[] to = {row, col};
                    g.new_move(prevTileCoordinate, to, g.gb.getTile(row, col).getNumber());
                    g.myTurn = false;

                    if(g.gb.getTile(row,col).isPlayerPiece()) {
                        gameButtons[row][col].setBackgroundResource(getResources().
                                getIdentifier("num" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                    }
                    else{
                        gameButtons[row][col].setBackgroundResource(getResources().
                                getIdentifier("opponum" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                    }
                    gameButtons[prevTileCoordinate[0]][prevTileCoordinate[1]].setBackgroundResource(R.drawable.blank);
                    prevTileCoordinate[0] = -1;
                    prevTileCoordinate[1] = -1;
                    hasTileSelected = false;
                }
            } else { //its a game piece
                if (!(row==lastMovedTile[0] && col==lastMovedTile[1])) {
                    if (prevTileCoordinate[0] == row && prevTileCoordinate[1] == col) {
                        if (g.gb.getTile(row, col).isPlayerPiece()) {
                            gameButtons[row][col].setBackgroundResource(getResources().
                                    getIdentifier("num" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                        } else {
                            gameButtons[row][col].setBackgroundResource(getResources().
                                    getIdentifier("opponum" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                        }
                        prevTileCoordinate[0] = -1;
                        prevTileCoordinate[1] = -1;
                        hasTileSelected = false;
                        cleanBlankTile();
                        return;
                    }
                    //if (g.gb.getTile(row, col).isPlayerPiece()) { //if its owned by you
                    if (hasTileSelected) { //we have already selected a tile
                        if (g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).isPlayerPiece()) {
                            gameButtons[prevTileCoordinate[0]][prevTileCoordinate[1]].setBackgroundResource(getResources().
                                    getIdentifier("num" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                        } else {
                            gameButtons[prevTileCoordinate[0]][prevTileCoordinate[1]].setBackgroundResource(getResources().
                                    getIdentifier("opponum" + Integer.toString(g.gb.getTile(prevTileCoordinate[0], prevTileCoordinate[1]).getNumber()), "drawable", this.getPackageName()));
                        }
                        cleanBlankTile();
                    }
                    int currNum = g.gb.getTile(row, col).getNumber();
                    prevTileCoordinate[0] = row;
                    prevTileCoordinate[1] = col;
                    gameButtons[row][col].setBackgroundResource(getResources().
                            getIdentifier("selectednum" + Integer.toString(currNum), "drawable", this.getPackageName()));
                    for (int[] validTile : g.gb.getValidTiles(row, col, g.gb.getTile(row, col).isPlayerPiece())) {
                        gameButtons[validTile[0]][validTile[1]].setBackgroundResource(getResources().
                                getIdentifier("validtile", "drawable", this.getPackageName()));
                    }
                    hasTileSelected = true;
                }
            }
        }
    }

    public void cleanBlankTile() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (g.gb.getTile(i, j).isBlank()) {
                    gameButtons[i][j].setBackgroundResource(R.drawable.blank);
                }
            }
        }
    }

    public void updateTiles(GameBoard gameBoard) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (gameBoard.getTile(i, j).isBlank()) {
                    gameButtons[i][j].setBackgroundResource(R.drawable.blank);
                } else if (gameBoard.getTile(i, j).isPlayerPiece()) {
                    gameButtons[i][j].setBackgroundResource(getResources().
                            getIdentifier("num" + Integer.toString(gameBoard.getTile(i, j).getNumber()), "drawable", this.getPackageName()));
                } else {
                    gameButtons[i][j].setBackgroundResource(getResources().
                            getIdentifier("opponum" + Integer.toString(gameBoard.getTile(i, j).getNumber()), "drawable", this.getPackageName()));
                }
            }
        }
    }

    private void setOppoInfo(String username, int avatar) {
        oppoUsername.setText(username);
        oppoAvatar.setImageResource(getResources().getIdentifier("selectedavatar" + Integer.toString(avatar), "drawable", this.getPackageName()));
    }

    private void setPlayerInfo(String username, int avatar) {
        playerUsername.setText(username);
        playerAvatar.setImageResource(getResources().getIdentifier("selectedavatar" + Integer.toString(avatar), "drawable", this.getPackageName()));
    }

    public void showEndGameDialog(boolean ifWin) {
        String message;
        if (ifWin) {
            message = "Congrats! You Win!";
        } else {
            message = "You Lose!";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle("Game Over");
            builder.setMessage(message);
            builder.setPositiveButton("Again", new DialogInterface.OnClickListener() { // click on Again, back to ConnectionActivity looking for another game
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(GameActivity.this,InitializationActivity.class));
                }
            });
            builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() { // click on Main Menu, back to the MainActivity
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(GameActivity.this,MainActivity.class));
                }
            });
        builder.show();
    }

    @Override
    public void tileChanged() {
        updateTiles(g.gb);
    }
}
