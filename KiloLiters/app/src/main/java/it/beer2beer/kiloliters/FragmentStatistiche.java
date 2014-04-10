package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentStatistiche extends Fragment {

    DatabaseAdapter db;
    View view;
    LinearLayout root;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_statistiche, container, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
                root.removeAllViews();
                initializeStatistics();
            }
        });

        root = (LinearLayout) view.findViewById(R.id.root_view);

        initializeStatistics();

        view.setBackgroundColor(Color.parseColor("#ff373537"));

        return view;
    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void initializeStatistics () {

        db = new DatabaseAdapter(this.getActivity());

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.checkOrInitializeDB ();

        long maxId = db.getLastId();

        for (int i = 1; i <= maxId; i++) {

            Cursor c = db.getRefuel(i);
            String timestamp = c.getString(0);
            int kilometers = c.getInt(1);
            Double price = c.getDouble(2);
            Double liters = c.getDouble(3);
            Double paid = c.getDouble(4);
            String station = c.getString(5);
            String city = c.getString(6);
            String description = c.getString(7);

            printLayout (i, timestamp, kilometers, price, liters, paid,
                    station, city, description);

        }

        db.close();
    }

    private void printLayout (int id, String t, int k, double pr, double l, double pa,
                              String s, String c, String d) {

        ScrollView sv = new ScrollView(this.getActivity());
        sv.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT));


        LinearLayout child = new LinearLayout(this.getActivity());
        child.setOrientation(LinearLayout.VERTICAL);

        TextView upperBlankLine = new TextView(this.getActivity());
        upperBlankLine.setText("\n");
        upperBlankLine.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(upperBlankLine);

        TextView title = new TextView(this.getActivity());
        title.setText("Rifornimento numero: " + Long.toString(id));
        title.setAllCaps(true);
        title.setTypeface(null, Typeface.NORMAL);
        title.setTypeface(null, Typeface.BOLD_ITALIC);
        title.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(title);

        TextView lowerBlankLine = new TextView(this.getActivity());
        lowerBlankLine.setText("\n");
        lowerBlankLine.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(lowerBlankLine);

        TextView date = new TextView(this.getActivity());
        date.setText("Data Rifornimento: " + db.getCorrectDataFormat(t));
        date.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(date);

        TextView kilometers = new TextView(this.getActivity());
        kilometers.setText("Chilometri al momento del rifornimento: " + Integer.toString(k) + " KM");
        kilometers.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(kilometers);

        TextView liters = new TextView(this.getActivity());
        liters.setText("Litri erogati: " + Double.toString(l) + " L");
        liters.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(liters);

        TextView price = new TextView(this.getActivity());
        price.setText("Prezzo carburante: " + Double.toString(pr) + " €");
        price.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(price);

        TextView paid = new TextView(this.getActivity());
        paid.setText("Importo pagato: " + Double.toString(pa) + " €");
        paid.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(paid);

        TextView station = new TextView(this.getActivity());
        station.setText("Stazione di servizio: " + s);
        station.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(station);

        if (d != null) {
            TextView description = new TextView(this.getActivity());
            description.setText("Info distributore: " + d);
            description.setTextColor(Color.parseColor("#ffe7d4c7"));
            child.addView(description);
        }

        TextView city = new TextView(this.getActivity());
        city.setText("Città: " + c);
        city.setTextColor(Color.parseColor("#ffe7d4c7"));
        child.addView(city);

        child.addView(sv);

        root.addView(child);

    }

}
