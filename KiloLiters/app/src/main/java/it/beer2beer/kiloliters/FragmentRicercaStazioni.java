package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;


import java.net.ConnectException;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentRicercaStazioni extends Fragment
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private LocationRequest locationRequest;
    private LocationClient locationClient;
    MapFragment googleMap;
    boolean firstSearch = true;
    boolean toastLocationVisualized = false;

    View view;

    public FragmentRicercaStazioni() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         try {

            view = inflater.inflate(R.layout.view_ricerca_stazioni, container,
                    false);
            try {

                googleMap = ((MapFragment) this.getActivity()
                        .getFragmentManager().findFragmentById(R.id.map));

            }catch (Exception e){

                e.printStackTrace();
            }

            GoogleMapOptions mapOptions = new GoogleMapOptions();

            mapOptions.rotateGesturesEnabled(true);
            mapOptions.compassEnabled(true);
            googleMap.getMap().setMyLocationEnabled(true);
            googleMap.getMap().getUiSettings().setAllGesturesEnabled(true);
            googleMap.getMap().getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getMap().getUiSettings().setZoomControlsEnabled(true);
            googleMap.getMap();

            MapsInitializer.initialize(this.getActivity());

        } catch (GooglePlayServicesNotAvailableException e) {
             Toast.makeText(getActivity(), "Google Play Services missing !",
                     Toast.LENGTH_LONG).show();
         }
        /*
         } catch (InflateException e) {
            Toast.makeText(getActivity(), "Problems inflating the view !",
                    Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Google Play Services missing !",
                    Toast.LENGTH_LONG).show();
        }
        */

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if ((firstSearch) && (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Ricerca posizione...", Toast.LENGTH_SHORT);
                    t.show();

                    firstSearch = false;
                }

                if ((!toastLocationVisualized) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Per usufruire al meglio del servizio Ricerca Distributori, " +
                            "attiva la localizzazione", Toast.LENGTH_LONG);
                    t.show();
                    toastLocationVisualized = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        try{

            locationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);

        }catch (Exception e) {

            e.printStackTrace();
        }

        locationClient.connect();


    }

    @Override
    public void onLocationChanged(Location location) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15);
        googleMap.getMap().animateCamera(cameraUpdate);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {


    }

    @Override
    public void onConnected(Bundle connectionHint) {

        locationClient.getLastLocation();

    }

    @Override
    public void onDisconnected() {

    }
}
