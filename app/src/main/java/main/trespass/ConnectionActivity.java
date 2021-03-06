package main.trespass;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;


public class ConnectionActivity extends Activity implements GameDriver.SocketEventInterface {
    GameDriver g;
    private static boolean visible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    @Override
    protected void onStart(){
        super.onStart();
        visible = true;
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

    @Override
    public void onStop(){
        super.onStop();
        visible = false;
    }
    public void findOpponent() {
        //find the opponent
        g = GameDriver.getInstance();
        g.gb.refreshGameBoard();
        InitializationObject i = g.getInitializationObject();
        g.setSocketListener(this);

        if( !g.isSocketConnected() )
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
                    int modded = dots % 4;
                    switch (modded) {
                        case 0:
                            break;
                        case 1:
                            s = s.concat(".");
                            break;
                        case 2:
                            s = s.concat("..");
                            break;
                        case 3:
                            s = s.concat("...");
                            break;
                    }
                    setText(s);
                    handler.postDelayed(this, 1000);
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
        if(!visible) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("Trespass")
                            .setContentText("Game Started!");

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(001, mBuilder.build());
        }
        // go to the next activity
        // Intent for the activity to open when user selects the notification
        Intent GameActivityIntent = new Intent(this, GameActivity.class);

        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        TaskStackBuilder builder =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(GameActivityIntent);

        builder.startActivities();
    }

    public void onMove(JSONObject json) {

    }

    public void onEnd(String s) {

    }
}
