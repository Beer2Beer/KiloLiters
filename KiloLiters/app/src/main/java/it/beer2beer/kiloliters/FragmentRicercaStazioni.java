package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.location.Location;
import android.location.LocationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;


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

    View view;

    public FragmentRicercaStazioni() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    /*
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
    */
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

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
      // lc.requestLocationUpdates(lr, (com.google.android.gms.location.LocationListener) lc);

    }

    @Override
    public void onDisconnected() {

    }
}
