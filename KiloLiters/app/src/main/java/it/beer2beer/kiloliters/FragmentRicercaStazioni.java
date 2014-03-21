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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;



/**
 * Created by federico on 08/03/14.
 */

public class FragmentRicercaStazioni extends Fragment implements LocationListener {

    private LocationManager locationManager;
    MapFragment googleMap;
    LatLng lastLatLng;
    double lat;
    double lng;
    View view;
    boolean toastNoLocation = false;
    boolean toastFindingPosition = false;

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

                GoogleMap.OnMyLocationButtonClickListener locationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                                !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            Context context = getActivity();
                            Toast t = Toast.makeText(context, "Per poter utilizzare al meglio il servizio di Ricerca Distributori, " +
                                    "attiva la localizzazione", Toast.LENGTH_LONG);
                            t.show();

                        }

                        else {

                                Context context = getActivity();
                                Toast t = Toast.makeText(context, "Ricerco posizione...", Toast.LENGTH_SHORT);
                                t.show();
                        }

                        return true;
                    }
                };

                googleMap.getMap().setOnMyLocationButtonClickListener(locationButtonClickListener);

                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    if (!toastNoLocation) {
                        Context context = getActivity();
                        Toast t = Toast.makeText(context, "Per poter utilizzare al meglio il servizio di Ricerca Distributori, " +
                                "attiva la localizzazione", Toast.LENGTH_LONG);
                        t.show();
                        toastNoLocation = true;
                    }

                }

                else {

                    if (!toastFindingPosition) {
                        Context context = getActivity();
                        Toast t = Toast.makeText(context, "Ricerco posizione...", Toast.LENGTH_SHORT);
                        t.show();
                        toastFindingPosition = true;
                    }
                }
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

        }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void updatePlaces(){
        //get location manager

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        } catch (NullPointerException e) {

            e.printStackTrace();
        }

        //get last location
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

               if (lastKnownLocation != null) {
                    lat = lastKnownLocation.getLatitude();
                    lng = lastKnownLocation.getLongitude();

                    lastLatLng = new LatLng(lat, lng);

                    googleMap.getMap().animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
                }
            }

            else if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               if (lastKnownLocation != null) {
                   lat = lastKnownLocation.getLatitude();
                   lng = lastKnownLocation.getLongitude();
                   //create LatLng
                   lastLatLng = new LatLng(lat, lng);
                   googleMap.getMap().animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
               }
            }

        }

        //remove any existing marker
     //   if(userMarker!=null) userMarker.remove();
        //create and set marker properties
      /*
       userMarker = googleMap.addMarker(new MarkerOptions()
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
                "&key=AIzaSyA7ttpFSTYMgDsan6GvsznzN-xkhN1M8n0";

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
}

