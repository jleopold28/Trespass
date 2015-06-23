package main.trespass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class StartActivity extends Activity {
    private int avatar;
    private EditText usernameTextField;
    private ImageButton avatar0IB;
    private ImageButton avatar1IB;
    private ImageButton avatar2IB;
    private ImageButton avatar3IB;

    private TextView debugText; //Debug

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        usernameTextField = (EditText) this.findViewById(R.id.usernameText);
        avatar = 0;
        debugText = (TextView) this.findViewById(R.id.debugText); // Debug
        avatar0IB = (ImageButton) this.findViewById(R.id.avatar0IB);
        avatar1IB = (ImageButton) this.findViewById(R.id.avatar1IB);
        avatar2IB = (ImageButton) this.findViewById(R.id.avatar2IB);
        avatar3IB = (ImageButton) this.findViewById(R.id.avatar3IB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void onClickAvatar0IB (View v) {
        avatar = 0;

        avatar0IB.setSelected(true);
        avatar1IB.setSelected(false);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(false);

    }

    public void onClickAvatar1IB (View v) {
        avatar = 1;

        avatar0IB.setSelected(false);
        avatar1IB.setSelected(true);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(false);

    }

    public void onClickAvatar2IB (View v) {
        avatar = 2;

        avatar0IB.setSelected(false);
        avatar1IB.setSelected(false);
        avatar2IB.setSelected(true);
        avatar3IB.setSelected(false);

    }

    public void onClickAvatar3IB (View v) {
        avatar = 3;

        avatar0IB.setSelected(false);
        avatar1IB.setSelected(false);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(true);

    }

    public void onClicknextB (View v) {
        GameDriver.createPlayer(usernameTextField.getText().toString(), avatar);
        startActivity(new Intent(StartActivity.this, InitializationActivity.class));
        //Player player = new Player(usernameTextField.getText().toString(), avatar);
        //debugText.setText(player.getUsername() + Integer.toString(player.getAvatar())); //Debug
    }

    public void onClickCancelB (View v) {
        //startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }
}
