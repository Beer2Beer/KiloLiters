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

        View v = inflater.inflate(R.layout.view_inserimento_dati, container, false);


        saveData = (Button) v.findViewById(R.id.save_data_button);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Tasto premuto");
                getDataFromUser();
            }
        });

        return v;

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

    public void insertRefuelFromUser(String timeStamp, double km, double oilPrice,
                                     double liters, double price, String station,
                                     String city, String description) {

        Log.d(TAG, "Timestamp: " + timeStamp + "km: "+  km + "Prezzo carburante:  "+ oilPrice +
                "litri: "+ liters +"prezzo: " + price  +"Distributore: "+ station +"Città: "+ city +
                        "Desc: "+ description);
        db = new DatabaseAdapter(this.getActivity());
        Toast t = Toast.makeText(getActivity(), "Dentro inserFuelFromUser", Toast.LENGTH_LONG);
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

        Long ts = System.currentTimeMillis()/1000;
        String timeStamp = ts.toString();

        EditText k = (EditText)getActivity().findViewById(R.id.insert_actual_km);
        double km = Double.parseDouble(k.getText().toString());

        EditText  l = (EditText)getActivity().findViewById(R.id.insert_liters);
        double liters = Double.parseDouble(l.getText().toString());

        EditText p = (EditText)getActivity().findViewById(R.id.insert_price);
        double price = Double.parseDouble(p.getText().toString());

        EditText op = (EditText)getActivity().findViewById(R.id.insert_oil_price);
        double oilPrice = getOilPrice(price, liters);
        op.setText(String.valueOf(oilPrice));

        EditText s = (EditText)getActivity().findViewById(R.id.insert_station);
        String station = (s.getText().toString());

        EditText c = (EditText)getActivity().findViewById(R.id.insert_city);
        String city = c.getText().toString();

        EditText d = (EditText)getActivity().findViewById(R.id.insert_description);
        String description = d.getText().toString();

        insertRefuelFromUser(timeStamp, km, oilPrice, liters, price, station, city, description);

    }

    public double getOilPrice(double price, double liters) {
        double result = price/liters;
        return result;
    }

} //chiusura classe