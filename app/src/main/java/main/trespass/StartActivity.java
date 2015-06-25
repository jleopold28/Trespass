package main.trespass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class StartActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{
    private int avatar;
    private EditText usernameTextField;
    private ImageButton avatar1IB;
    private ImageButton avatar2IB;
    private ImageButton avatar3IB;
    private ImageButton avatar4IB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        usernameTextField = (EditText) this.findViewById(R.id.usernameText);
        avatar = 0;
        avatar1IB = (ImageButton) this.findViewById(R.id.avatar1IB);
        avatar2IB = (ImageButton) this.findViewById(R.id.avatar2IB);
        avatar3IB = (ImageButton) this.findViewById(R.id.avatar3IB);
        avatar4IB = (ImageButton) this.findViewById(R.id.avatar4IB);
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

    public void onClickAvatar1IB (View v) {
        avatar = 1;

        avatar1IB.setSelected(true);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(false);
        avatar4IB.setSelected(false);

    }

    public void onClickAvatar2IB (View v) {
        avatar = 2;

        avatar1IB.setSelected(false);
        avatar2IB.setSelected(true);
        avatar3IB.setSelected(false);
        avatar4IB.setSelected(false);

    }

    public void onClickAvatar3IB (View v) {
        avatar = 3;

        avatar1IB.setSelected(false);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(true);
        avatar4IB.setSelected(false);

    }

    public void onClickAvatar4IB (View v) {
        avatar = 4;

        avatar1IB.setSelected(false);
        avatar2IB.setSelected(false);
        avatar3IB.setSelected(false);
        avatar4IB.setSelected(true);

    }

    public void onClick(DialogInterface dialog, int which){}

    public void validateUsername(View v){
        if(usernameTextField.getText().toString().trim().length() == 0){
            onClick(v);
        }
        else{
            validateAvatar(v);
        }

    }
    public void onClick(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("You must enter a username!")
                .setTitle("Error!")
                .setPositiveButton("OK",this)
                .setCancelable(false)
                .create();
        ad.show();
    }
    public void validateAvatar(View v){
        if(avatar == 0){
            onClick2(v);
        }
        else{
            goToInitScreen();
        }
    }
    public void onClick2(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("You must select an avatar!")
                .setTitle("Error!")
                .setPositiveButton("OK",this)
                .setCancelable(false)
                .create();
        ad.show();
    }
    public void onClicknextB (View v) {
        validateUsername(v);
    }

    public void goToInitScreen(){
        GameDriver g = GameDriver.getInstance();
        g.createPlayer(usernameTextField.getText().toString(), avatar);
        startActivity(new Intent(StartActivity.this, InitializationActivity.class));
    }

    public void onClickCancelB (View v) {
        //startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }
}
