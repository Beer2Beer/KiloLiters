package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentInserimentoDati extends Fragment {

    Button saveData;
    public static String TAG = "Inserimento dati";
    DatabaseAdapter db;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_inserimento_dati, container, false);


        saveData = (Button) view.findViewById(R.id.save_data_button);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Tasto premuto");
                getDataFromUser();
            }
        });

        return view;

    };

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onResume () {

        super.onResume();

    }

      @Override
    public void onDestroy() {

        super.onDestroy();

    }

    public void insertRefuelFromUser(String timeStamp, int km, double oilPrice,
                                     double liters, double price, String station,
                                     String city, String description) {

        Log.d(TAG, "Timestamp: " + timeStamp + "km: "+  km + "Prezzo carburante:  "+ oilPrice +
                "litri: "+ liters +"prezzo: " + price  +"Distributore: "+ station +"Città: "+ city +
                        "Desc: "+ description);
        db = new DatabaseAdapter(this.getActivity());
        Toast t = Toast.makeText(getActivity(), "Rifornimento salvato!", Toast.LENGTH_LONG);
        t.show();
        try {
            db.open();
            Log.d(TAG, "Dentro al try");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long id = db.insertRefuel(timeStamp, km, oilPrice, liters, price, station, city, description);
        Log.d(TAG,"chiamata insertRefuel");
        String l = Long.toString(id);
        Log.d(TAG, l);
        if (id==-1) Log.d(TAG, "Errore inizializzazione database");
        db.close();

    }

    public void getDataFromUser() {

        SimpleDateFormat ts = new SimpleDateFormat("ddMMyyyyhhmmss");
        String timeStamp = ts.format(new Date());

        int km = 0;
        double liters = 0.0d;
        double price = 0.0d;
        String station = null;
        String city = null;
        String description = null;

        EditText k = (EditText)getActivity().findViewById(R.id.insert_actual_km);
        if (!k.getText().toString().equals("")) {
            double kil = Double.parseDouble(k.getText().toString());
            kil = Math.round(kil);
            km = (int) kil;
        }


        EditText  l = (EditText)getActivity().findViewById(R.id.insert_liters);
        if (!l.getText().toString().equals("")) {
            double lit = Double.parseDouble(l.getText().toString());
            int i = (int)(lit*100);
            liters = i/100d;
        }

        EditText p = (EditText)getActivity().findViewById(R.id.insert_price);
        if (!p.getText().toString().equals("")) {
            double pr = Double.parseDouble(p.getText().toString());
            int mInt = (int)(pr*100);
            price = mInt/100d;
        }

        double oilPrice = 0.0d;
        if (liters != 0.0d) {
            oilPrice = getOilPrice(price, liters);
        }

        EditText s = (EditText)getActivity().findViewById(R.id.insert_station);
        if (!s.getText().toString().equals("")) {
            station = (s.getText().toString());
        }

        EditText c = (EditText)getActivity().findViewById(R.id.insert_city);
        if (!c.getText().toString().equals("")) {
            city = c.getText().toString();
        }

        EditText d = (EditText)getActivity().findViewById(R.id.insert_description);
        if (!d.getText().toString().equals("")) {
            description = d.getText().toString();
        }

        if (km == 0 || oilPrice == 0.0d || liters == 0.0d || price == 0.0d ||
                station == null || city == null || description == null) {

            Toast t = Toast.makeText(getActivity(), "Completare tutti i campi", Toast.LENGTH_LONG);
            t.show();

        }
        else {
            insertRefuelFromUser(timeStamp, km, oilPrice, liters, price,
                    station.toUpperCase(), city.toUpperCase(), description.toUpperCase());
        }

    }

    public double getOilPrice(double price, double liters) {
        double r = price/liters;
        int i = (int)(r*1000);
        double result = i/1000d;
        return result;
    }

}