package it.beer2beer.kiloliters;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {

    public static final String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the four
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        mViewPager.setCurrentItem(2); //setta la tab centrale come predefinita

        DatabaseAdapter db = new DatabaseAdapter(this);

/*
        // DB TEST

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long id;

        id = db.insertRefuel("Prova 16:38",250,1.599,35.84,50,"ESSO CESSO","PORNOLO","ABBESTIA");
        if(id == -1) Log.w(TAG, "Errore inizializzazione del database");

        Cursor c = db.getRefuel(1);
        Toast t = Toast.makeText(this, c.getString(0), Toast.LENGTH_LONG);
        t.show();
        db.close();

*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_settings:

                Context context_settings = getApplicationContext();
                Toast toast_settings = Toast.makeText(context_settings, "Da implementare", Toast.LENGTH_SHORT);
                toast_settings.show();

                //qui posso lanciare l'activity associata alle impostazioni (creare la classe)

                return true;

            case R.id.action_about:

                Context context_about = getApplicationContext();
                Toast toast_about = Toast.makeText(context_about, "Da implementare", Toast.LENGTH_SHORT);
                toast_about.show();

                //qui posso lanciare l'activity associata alle info sull'app (creare la classe)

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        mViewPager.setOffscreenPageLimit(5); //setta il limite per non ricaricare i fragments

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            Fragment fragment;

            switch(position) {

                case 0:
                    fragment = new FragmentStatisticheTotali();
                    break;

                case 1:

                    fragment = new FragmentStatistiche();
                    break;

                case 2:

                    fragment = new FragmentInserimentoDati();
                    break;

                case 3:

                    fragment = new FragmentRicercaStazioni();
                    break;

                case 4:

                    fragment = new FragmentCalcolaBollo();
                    break;

                default:
                    throw new IllegalArgumentException("Errore");

            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Returns 5 pages
            int fragmentNumber = 5;
            return fragmentNumber;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section0).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }

    }

}
