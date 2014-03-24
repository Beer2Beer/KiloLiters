package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



import java.net.ConnectException;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentRicercaStazioni extends Fragment
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {


    public static String TAG = "LocalizationService";
    private LocationRequest locationRequest;
    private LocationClient locationClient;
    MapFragment googleMap;
    //places of interest
    private Marker[] placeMarkers;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;
    private int otherIcon = R.drawable.gas_station;
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

            if (googleMap!=null) {

                placeMarkers = new Marker[MAX_PLACES];
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

        /*
        Location locationForQuery = locationClient.getLastLocation();
        String locationForQueryToString = locationForQuery.toString();
        String placesQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?\n" +
                locationForQueryToString +
                "    &radius=1000\n" +
                "    &sensor=true\n" +
                "    &types=gas_station\n" +
                "    &key=AIzaSyA7ttpFSTYMgDsan6GvsznzN-xkhN1M8n0";
*/
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

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location=" + lat + "," + lon +
                "&radius=5000&sensor=true" +
                "&keyword=stazioni%20di%20servizio" +
                "&key=AIzaSyDQOzSn_VhdDuz26Hes3wtci9HHW6WZnyQ";

        new GetPlaces().execute(placesSearchStr);

        Log.d(TAG,
                "LocationTrackingService ---> onLocationChanged(): Provider: "
                        + location.getProvider() + " Lat: "
                        + location.getLatitude() + " Lng: "
                        + location.getLongitude() + " Accuracy: "
                        + location.getAccuracy());
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

        if (locationClient.isConnected()) {


            Location location = locationClient.getLastLocation();
            if (location != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 15);
                googleMap.getMap().animateCamera(cameraUpdate);

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                        "json?location=" + lat + "," + lon +
                        "&radius=5000&sensor=true" +
                        "&keyword=stazioni%20di%20servizio" +
                        "&key=AIzaSyDQOzSn_VhdDuz26Hes3wtci9HHW6WZnyQ";


                Log.d(TAG,
                        "LocationTrackingService ---> onLocationChanged(): Provider: "
                                + location.getProvider() + " Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude() + " Accuracy: "
                                + location.getAccuracy()
                );

                new GetPlaces().execute(placesSearchStr);
            }
        }

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onResume() {
    super.onResume();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        try{

            locationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);

        }catch (Exception e) {

            e.printStackTrace();
        }

        locationClient.connect();

        if (locationClient.isConnected()) {

            Location location = locationClient.getLastLocation();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15);
            googleMap.getMap().animateCamera(cameraUpdate);

            double lat = location.getLatitude();
            double lon = location.getLongitude();


        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location=" + lat + "," + lon +
                "&radius=5000&sensor=true" +
                "&keyword=stazioni%20di%20servizio" +
                "&key=AIzaSyDQOzSn_VhdDuz26Hes3wtci9HHW6WZnyQ";

            new GetPlaces().execute(placesSearchStr);
        }
    }


    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places

            //build result as string
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for (String placeSearchURL : placesURL) {
                HttpClient placesClient = new DefaultHttpClient();
                try {
                    //try to fetch the data

                    //HTTP Get receives URL string
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute GET with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    //only carry on if response is OK
                    if (placeSearchStatus.getStatusCode() == 200) {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while ((lineIn = placesReader.readLine()) != null) {
                            placesBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            return placesBuilder.toString();

        }
        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            //remove existing markers
/*
            if (result != null) {

                Toast t = Toast.makeText(getActivity(), result, Toast.LENGTH_LONG);
                t.show();
            }
*/
            if (result == null) {

                Toast t = Toast.makeText(getActivity(), "Nessun risultato", Toast.LENGTH_LONG);
                t.show();
            }

            if(placeMarkers!=null) {

                for (int pm = 0; pm < placeMarkers.length; pm++) {
                    if (placeMarkers[pm] != null)
                        placeMarkers[pm].remove();
                }
            }

            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("results");
                //marker options for each place returned
                places = new MarkerOptions[placesArray.length()];
                //loop through places
                for (int p=0; p<placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue=false;
                    LatLng placeLL=null;
                    String placeName="";
                    String vicinity="";
                    int currIcon = otherIcon;
                    try{
                        //attempt to retrieve place data values
                        missingValue=false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        JSONObject loc = placeObject.getJSONObject("geometry")
                                .getJSONObject("location");
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                Double.valueOf(loc.getString("lng")));
                        //get types
                        JSONArray types = placeObject.getJSONArray("types");
                        //loop through types

                        for(int t=0; t<types.length(); t++){
                            //what type is it
                            String thisType=types.get(t).toString();
                            //check for particular types - set icons
                            if(thisType.contains("gas_station")){
                                currIcon = otherIcon;
                                break;

                            }
                        }
                        //vicinity
                        vicinity = placeObject.getString("vicinity");
                        //name
                        placeName = placeObject.getString("name");

                    }
                    catch(JSONException jse){
                        Log.v("PLACES", "missing value");
                        missingValue=true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if(missingValue)	places[p]=null;
                    else
                        places[p]=new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(places!=null && placeMarkers!=null){
                for(int p=0; p<places.length && p<placeMarkers.length; p++){
                    //will be null if a value was missing
                    if(places[p]!=null)
                        placeMarkers[p]=googleMap.getMap().addMarker(places[p]);
                }
            }

        }
    }

}

