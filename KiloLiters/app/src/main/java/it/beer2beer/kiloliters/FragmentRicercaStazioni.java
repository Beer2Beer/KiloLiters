package it.beer2beer.kiloliters;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.app.AlertDialog;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by federico on 08/03/14.
 */
public class FragmentRicercaStazioni extends Fragment implements LocationListener {

  //  private LocationRequest locationRequest;
  //  private LocationClient locationClient;
    private LocationManager locationManager;
    MapFragment googleMap;
    LatLng lastLatLng;
    double lat;
    double lng;
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

            view = inflater.inflate(R.layout.view_ricerca_stazioni, container,
                    false);
            if (googleMap == null) {
                try {

                    googleMap = ((MapFragment) this.getActivity()
                            .getFragmentManager().findFragmentById(R.id.map));

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            if (googleMap != null) {

                GoogleMapOptions mapOptions = new GoogleMapOptions();

                mapOptions.rotateGesturesEnabled(true);
                googleMap.getMap().setMyLocationEnabled(true);
                googleMap.getMap().getUiSettings().setAllGesturesEnabled(true);
                googleMap.getMap().getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getMap().getUiSettings().setZoomControlsEnabled(true);
                googleMap.getMap();
                updatePlaces();
            }

            //     MapsInitializer.initialize(this.getActivity());
    /*
        } catch (GooglePlayServicesNotAvailableException e) {
             Toast.makeText(getActivity(), "Google Play Services mancanti",
                     Toast.LENGTH_LONG).show();
        } catch (InflateException e) {
            Toast.makeText(getActivity(), "Problemi con la view",
                    Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Google Play Services mancanti",
                    Toast.LENGTH_LONG).show();
        }
            */

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        latLng = new LatLng(latitude,longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);

        googleMap.getMap().animateCamera(cameraUpdate);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        };

    @Override
    public void onProviderDisabled(String s) {

    }


    private void updatePlaces(){
        //get location manager



        try {

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                    !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                if (googleMap.getUserVisibleHint() == true) {
                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Abilitare servizi di localizzazione", Toast.LENGTH_LONG);
                    t.show();
                }

            }

        } catch (NullPointerException e) {

            e.printStackTrace();
        }

        //get last location
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lat = lastKnownLocation.getLatitude();
                lng = lastKnownLocation.getLongitude();

                lastLatLng = new LatLng(lat, lng);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);

                googleMap.getMap().animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
            }

            else if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat = lastKnownLocation.getLatitude();
                lng = lastKnownLocation.getLongitude();
                //create LatLng
                lastLatLng = new LatLng(lat, lng);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100, this);

                googleMap.getMap().animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);

            }

        }

        //remove any existing marker
     //   if(userMarker!=null) userMarker.remove();
        //create and set marker properties
      /*
       userMarker = theMap.addMarker(new MarkerOptions()
                .position(lastLatLng)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Your last recorded location"));
       */

        //move to location


        //build places query string
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=1000&sensor=true" +
                "&types=gas_station"+
                "&key=AIzaSyA7ttpFSTYMgDsan6GvsznzN-xkhN1M8n0";//ADD KEY

        //execute query
      //  new GetPlaces().execute(placesSearchStr);

    }

    @Override
    public void onPause(){

        super.onPause();
        if (googleMap !=null) {

            locationManager.removeUpdates(this);

        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if(googleMap != null) {

            locationManager.removeUpdates(this);
        }
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
        else {  }
    }
*/
}
