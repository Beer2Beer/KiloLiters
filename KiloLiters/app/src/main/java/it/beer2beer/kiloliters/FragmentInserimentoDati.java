package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.sql.SQLException;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentInserimentoDati extends Fragment {

    DatabaseAdapter db = new DatabaseAdapter(getActivity());

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

     }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_inserimento_dati, container, false);
        return v;
    }


    @Override
    public void onPause() {

        super.onPause();
        db.close();
    }

    @Override
    public void onResume () {

        super.onResume();
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

      @Override
    public void onDestroy() {

        super.onDestroy();
        db.close();
    }

    public void insertRefuelFromUser(String timeStamp, double km, double oilPrice,
                                     double liters, double price, String station,
                                     String city, String description) {
        db.insertRefuel(timeStamp, km, oilPrice, liters, price, station, city, description);

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