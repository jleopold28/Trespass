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
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class InitializationActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{

    private EditText secretNumberText;
    private EditText num00text, num01text, num02text, num03text, num04text,
                     num10text, num11text, num12text, num13text, num14text;
    private int[][] arr;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        secretNumberText = (EditText) this.findViewById(R.id.secretNumber);
        num00text = (EditText) this.findViewById(R.id.num00);
        num01text = (EditText) this.findViewById(R.id.num01);
        num02text = (EditText) this.findViewById(R.id.num02);
        num03text = (EditText) this.findViewById(R.id.num03);
        num04text = (EditText) this.findViewById(R.id.num04);
        num10text = (EditText) this.findViewById(R.id.num10);
        num11text = (EditText) this.findViewById(R.id.num11);
        num12text = (EditText) this.findViewById(R.id.num12);
        num13text = (EditText) this.findViewById(R.id.num13);
        num14text = (EditText) this.findViewById(R.id.num14);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initialization, menu);
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

    public void fillArray(){
        int num00 = (num00text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num00text.getText().toString());
        int num01 = (num01text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num01text.getText().toString());
        int num02 = (num02text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num02text.getText().toString());
        int num03 = (num03text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num03text.getText().toString());
        int num04 = (num04text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num04text.getText().toString());

        int num10 = (num10text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num10text.getText().toString());
        int num11 = (num11text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num11text.getText().toString());
        int num12 = (num12text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num12text.getText().toString());
        int num13 = (num13text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num13text.getText().toString());
        int num14 = (num14text.getText().toString().trim().length() == 0) ? 0: Integer.parseInt(num14text.getText().toString());

        arr = new int[][]{{num00,num01,num02,num03,num04},
                {num10,num11,num12,num13,num14}};

    }
    public void onClick(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("You must use the numbers 0-9 only once.")
                .setTitle("Error!")
                .setPositiveButton("OK",this)
                .setCancelable(false)
                .create();
                ad.show();
    }
    public void onClick2(View v){
        AlertDialog ad = new AlertDialog.Builder(this)
                .setMessage("You must enter a secret number.")
                .setTitle("Error!")
                .setPositiveButton("OK",this)
                .setCancelable(false)
                .create();
        ad.show();
    }
    public void onClick(DialogInterface dialog, int which){}

    public void validateSecretNumber(View v){
        if(secretNumberText.getText().toString().trim().length() == 0){
            onClick2(v);
        }
        else{
            validateArray(v);
        }
    }
    public void validateArray(View v) {
        fillArray();
        Set set = new HashSet<Integer>();
        for(int[] row : arr){
            for(int col : row){
                set.add(col);
            }
        }
        if(set.size() != 10){
            onClick(v);
        }
        else{
            goToGameScreen(v);
        }

    }

    public void onClickFindMatch(View v) {
        validateSecretNumber(v);
    }
    public void goToGameScreen(View v){
        int secretNum = Integer.parseInt(secretNumberText.getText().toString());
        GameDriver g = GameDriver.getInstance();
        g.setSecretNumber(secretNum);
        g.createInitializationObject(arr);
        g.playGame();
        startActivity(new Intent(InitializationActivity.this, GameActivity.class));
    }
}
