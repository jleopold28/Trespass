package main.trespass;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) this.findViewById(R.id.startButton);
        Button rules = (Button) this.findViewById(R.id.rulesButton);
        start.setOnClickListener(buttonClickHandler);
        rules.setOnClickListener(buttonClickHandler);

    }

    @Override
    public void onBackPressed(){}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        //return super.onOptionsItemSelected(item);
    //}

    OnClickListener buttonClickHandler = new OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.startButton:
                        jumpToStartScreen();
                        break;
                case R.id.rulesButton:
                        jumptoRuleScreen();
                        break;
            }
        }
    };

    protected void jumpToStartScreen(){
        Intent intent = new Intent();
        intent.setClass(this, StartActivity.class);
        startActivity(intent);
    }
    protected void jumptoRuleScreen(){
        Intent intent = new Intent();
        intent.setClass(this, RulesActivity.class);
        startActivity(intent);
    }
}
