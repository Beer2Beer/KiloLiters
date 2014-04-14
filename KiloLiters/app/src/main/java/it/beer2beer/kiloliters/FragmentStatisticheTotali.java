package it.beer2beer.kiloliters;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
    View view;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view_statistiche_totali, container, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
                initializeStatistics(view);
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
        statisticKm.setText(Integer.toString(km) + " KM");

        TextView statisticLiters = (TextView) view.findViewById(R.id.statistic_total_liters);
        int l = db.getSumLiters();
        statisticLiters.setText(Integer.toString(l) + " L");

        TextView statisticPaid = (TextView) view.findViewById(R.id.statistic_total_paid);
        int p = db.getSumPaid();
        statisticPaid.setText(Integer.toString(p) + " €");

        TextView statisticMediumPrice = (TextView) view.findViewById(R.id.statistic_medium_price);
        double mp = db.getAvgPrice();
        statisticMediumPrice.setText(Double.toString(mp) + " €/L");

        TextView statisticFavStation = (TextView) view.findViewById(R.id.statistic_fav_station);
        final TextView statisticFavStationDescription = (TextView) view.findViewById(R.id.statistic_fav_station_description);
        String temp = db.getMostUsedStation();
        if (temp.equals("Nessun rifornimento")) {

            statisticFavStation.setText(temp);
            statisticFavStationDescription.setText(temp);
        }
        else {

          String[] parts = temp.split("-");
          String favStation = parts[0];
          final String favStationDescription = parts[1];
          statisticFavStation.setText(favStation);
          if (favStationDescription.length() > 20) {
              statisticFavStationDescription.setText(favStationDescription.substring(0, 20) + "...");
              statisticFavStationDescription.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                      builder.setMessage(favStationDescription.substring(0, 1) + favStationDescription.toLowerCase().substring(1))
                              .setCancelable(true)
                              .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialogInterface, int i) {
                                      dialogInterface.cancel();
                                  }
                              });

                      AlertDialog aboutAlert = builder.create();
                      aboutAlert.show();
                  }
              });
          }
          else {
              statisticFavStationDescription.setText(favStationDescription);
          }

        }

        TextView statisticKiloliters = (TextView) view.findViewById(R.id.statistic_kiloliters);
        double k = db.getKiloliters(km, l);
        statisticKiloliters.setText(Double.toString(k) + " KM/L");

        TextView statisticLastRefuel = (TextView) view.findViewById(R.id.statistic_last_refuel);
        statisticLastRefuel.setText(db.getLastRefuel());

        db.close();
    }

}
