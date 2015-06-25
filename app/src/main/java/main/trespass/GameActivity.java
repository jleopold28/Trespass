package main.trespass;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class GameActivity extends Activity {

    private ImageButton IB40,IB41,IB42,IB43,IB44,IB50,IB51,IB52,IB53,IB54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        IB40 = (ImageButton) this.findViewById(R.id.IB40);
        IB41 = (ImageButton) this.findViewById(R.id.IB41);
        IB42 = (ImageButton) this.findViewById(R.id.IB42);
        IB43 = (ImageButton) this.findViewById(R.id.IB43);
        IB44 = (ImageButton) this.findViewById(R.id.IB44);
        IB50 = (ImageButton) this.findViewById(R.id.IB50);
        IB51 = (ImageButton) this.findViewById(R.id.IB51);
        IB52 = (ImageButton) this.findViewById(R.id.IB52);
        IB53 = (ImageButton) this.findViewById(R.id.IB53);
        IB54 = (ImageButton) this.findViewById(R.id.IB54);

        GameDriver g = GameDriver.getInstance();
        g.playGame();
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
    public void onClickIB40(View v){
        IB40.setSelected(true);
    }
}
