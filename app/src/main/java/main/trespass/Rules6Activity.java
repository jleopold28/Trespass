package main.trespass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Rules6Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules6);

        Button next = (Button) this.findViewById(R.id.RulesActivity6NextButton);
        next.setOnClickListener(buttonClickHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rules6, menu);
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

    View.OnClickListener buttonClickHandler = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.RulesActivity6NextButton:
                    jumpToRules7Screen();
                    break;
            }
        }
    };
    protected void jumpToRules7Screen(){
        Intent intent = new Intent();
        intent.setClass(this, Rules7Activity.class);
        startActivity(intent);
    }
}
