package it.beer2beer.kiloliters;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Francesco on 25/03/2014.
 */
public class AboutActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_about);
        Intent intent = getIntent();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) return true;
        else return super.onOptionsItemSelected(item);
    }
}
