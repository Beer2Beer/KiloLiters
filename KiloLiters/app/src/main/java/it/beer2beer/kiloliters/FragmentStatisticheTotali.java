package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by federico on 10/03/14.
 */
public class FragmentStatisticheTotali extends Fragment {

    public static String TAG = "Statistiche totali";
    DatabaseAdapter db;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_statistiche_totali, container, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
            }
        });

        initializeStatistics(view);


        return view;
    }

    @Override
    public void onPause(){

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

    private void initializeStatistics (View view) {

        db = new DatabaseAdapter(this.getActivity());

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView statisticKm = (TextView) view.findViewById(R.id.statistic_total_km);
        int km = db.getTotalKilometers();
        statisticKm.setText(Integer.toString(km));

        TextView statisticLiters = (TextView) view.findViewById(R.id.statistic_total_liters);
        int l = db.getSumLiters();
        statisticLiters.setText(Integer.toString(l));

        TextView statisticPaid = (TextView) view.findViewById(R.id.statistic_total_paid);
        int p = db.getSumPaid();
        statisticPaid.setText(Integer.toString(p));

        TextView statisticMediumPrice = (TextView) view.findViewById(R.id.statistic_medium_price);
        double mp = db.getAvgPrice();
        statisticMediumPrice.setText(Double.toString(mp));

        TextView statisticFavStation = (TextView) view.findViewById(R.id.statistic_fav_station);
        statisticFavStation.setText(db.getMostUsedStation());

        TextView statisticKiloliters = (TextView) view.findViewById(R.id.statistic_kiloliters);
        double k = db.getKiloliters(km, l);
        statisticKiloliters.setText(Double.toString(k));

        db.close();
    }

}
