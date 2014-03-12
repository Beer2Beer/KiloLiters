package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentRicercaStazioni extends Fragment {

    GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        try {
            // Loading map
            onStart();
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_ricerca_stazioni, container, false);
        return v;

    }

/*
    @Override
    public void onPause() {
    }
*/

    public void initializeMap() {

        if (googleMap == null) {
            GoogleMapOptions mapOptions = new GoogleMapOptions();

            mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                    .compassEnabled(true)
                    .rotateGesturesEnabled(true);

            googleMap.setMyLocationEnabled(true);

            try {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMap();
            } catch (Exception e) {

                throw new NullPointerException("Errore di inizializzazione mappe");

            }
        }
    }

}
