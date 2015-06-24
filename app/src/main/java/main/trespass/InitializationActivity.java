package main.trespass;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;


public class InitializationActivity extends Activity {

    //create InitializationObject
    private EditText secretNumberText;
    private TextView debugText2;
    private EditText num00text, num01text, num02text, num03text, num04text,
                     num10text, num11text, num12text, num13text, num14text;
    private String[][] arr;

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

        debugText2 = (TextView) this.findViewById(R.id.debugText2); // Debug
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
    //arr[0][0] = ?

    public void fillArray(){
        String num00 = num00text.getText().toString();
        String num01 = num01text.getText().toString();
        String num02 = num02text.getText().toString();
        String num03 = num03text.getText().toString();
        String num04 = num04text.getText().toString();

        String num10 = num10text.getText().toString();
        String num11 = num11text.getText().toString();
        String num12 = num12text.getText().toString();
        String num13 = num13text.getText().toString();
        String num14 = num14text.getText().toString();

        arr = new String[][]{{num00,num01,num02,num03,num04},
                {num10,num11,num12,num13,num14}};

    }

    public void onClickFindMatch (View v) {
        //GameDriver.setSecretNumber(secretNumberText.getText().toString());
        fillArray();
        debugText2.setText(arr[0][0] + ' ' + arr[0][1] + ' ' + arr[0][2] + ' ' + arr[0][3] + ' ' + arr[0][4] + ' ' + arr[1][0] + ' ' + arr[1][1] + ' ' + arr[1][2] + arr[1][3] + arr[1][4]
        );
        GameDriver.createInitializationObject(arr);
    }
}
