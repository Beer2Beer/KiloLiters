package it.beer2beer.kiloliters;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {

    public static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "FirstRun";
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

        doFirstRun();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here.

        switch (item.getItemId()) {


            case R.id.action_drop_db:

                AlertDialog.Builder dropBuilder = new AlertDialog.Builder(this);
                dropBuilder.setMessage("Sei sicuro di voler eliminare tutti i dati?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Context context = getApplicationContext();
                                try {
                                    context.deleteDatabase("rifornimenti.db");
                                }catch (Throwable t) {
                                    t.printStackTrace();
                                }
                                if (context!=null) {
                                    Toast t = Toast.makeText(context, "Dati eliminati!", Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dropAlert = dropBuilder.create();
                dropAlert.show();

                return true;

            case R.id.action_about:

                AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
                aboutBuilder.setMessage("KiloLiters \n\nCoded by Federico Bertoli and Francesco Trombi.\n \n" +
                        "Versione 0.8 Beta - 2014\n\n" +
                "Disclaimer:\nNon tutti i distributori potrebbero comparire sulla mappa, a causa di una limitazione " +
                        "dei dati forniti da Google Inc.")
                        .setCancelable(false)
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });

                AlertDialog aboutAlert = aboutBuilder.create();
                aboutAlert.show();
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
                    fragment = new FragmentStatistiche();
                    break;

                case 1:

                    fragment = new FragmentStatisticheTotali();
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

    private void doFirstRun() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        boolean firstRun = settings.getBoolean("isFirstRun", true);

        if (firstRun) {

            AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
            aboutBuilder.setMessage("\nBenvenuto!\n\n" +
                    "KiloLiters è un “diario” per i rifornimenti della tua auto, che ti permette di monitorare i consumi," +
                    " i prezzi e le spese sostenute.\n\n" +
                    "L’applicazione è divisa in 5 parti:\n\n" +
                    "- Inserimento Dati: in cui inserire le informazioni sui rifornimenti;\n\n" +
                    "- Statistiche Totali: in cui trovi le informazioni globali che vengono aggiornate ad ogni rifornimento;\n\n" +
                    "- Statistiche: con l’elenco di ogni rifornimento effettuato;\n\n" +
                    "- Ricerca Stazioni: con la quale puoi cercare distributori nelle vicinanze;\n\n" +
                    "- Calcolo Bollo: con collegamento diretto al sito dell’ACI." )
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                        }
                    });

            AlertDialog aboutAlert = aboutBuilder.create();
            aboutAlert.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }
}
