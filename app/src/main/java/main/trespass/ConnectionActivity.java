package main.trespass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;


public class ConnectionActivity extends Activity implements GameDriver.SocketEventInterface {
    GameDriver g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        findOpponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
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

    public void findOpponent() {
        //find the opponent
        g = GameDriver.getInstance();
        InitializationObject i = g.getInitializationObject();
        g.setSocketListener(this);
        g.connectSocket();
        JSONObject json = i.getJSONObject();
        g.sendUserInfo(json);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            private int dots = 0;

            public void run() {
                String s;
                    if (!GameDriver.isSocketConnected()) {
                        s = "Connecting";
                    } else {
                        s = "Searching for an Opponent";
                    }
                    dots++;
                    int modded = dots % 3;
                    switch (modded) {
                        case 0:
                            s = s.concat(".");
                            break;
                        case 1:
                            s = s.concat("..");
                            break;
                        case 2:
                            s = s.concat("...");
                            break;
                    }
                    setText(s);
                    handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    public void setText(final String text){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                TextView v = (TextView) findViewById(R.id.connectionActivityString);
                v.setText(text);
            }
        });
    }

    public void onDataError(String s) {

    }

    public void onError(String s) {

    }

    public void onInfo(String s) {
        //will tell you if there are no online players
        //display string in text view
    }

    public void onGame() {
        // go to the next activity
        startActivity(new Intent(ConnectionActivity.this, GameActivity.class));
    }

    public void onMove(JSONObject json) {

    }

    public void onEnd(String s) {

    }
}
