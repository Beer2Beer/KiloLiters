package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
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

/**
 * Created by federico on 08/03/14.
 */
public class FragmentRicercaStazioni extends Fragment
        implements LocationListener {


    public static String TAG = "LocalizationService";

    private LocationManager locationManager;

    MapFragment googleMap;

    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;//most returned from google
    private MarkerOptions[] places;
    private int otherIcon = R.drawable.gas_station;

    NetworkInfo netInfo;

    boolean firstSearch = true;
    boolean toastLocationVisualized = false;

    View view;

    public FragmentRicercaStazioni() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.view_ricerca_stazioni, container,
                false);
        try {

            googleMap = ((MapFragment) this.getActivity()
                    .getFragmentManager().findFragmentById(R.id.map));


        } catch (Exception e) {

            Log.d(TAG, "Errore inizializzazione mappa");
            e.printStackTrace();
        }

        if (googleMap != null) {

            placeMarkers = new Marker[MAX_PLACES];
        }

        //setto la mappa e la ottengo

        setMapOptions(googleMap);
        getMapFragment(googleMap);


        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if ((firstSearch) && (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

                    Context context = getActivity();

                    if (isOnline()) {
                        Toast t = Toast.makeText(context, "Ricerca posizione e distributori nelle vicinanze...",
                                Toast.LENGTH_SHORT);
                        t.show();
                    }

                    if (!isOnline()) {

                        Toast toast = Toast.makeText(context, "Per usufruire al meglio del servizio di ricerca distributori, " +
                                "attiva la rete dati", Toast.LENGTH_LONG);
                        toast.show();

                    }

                    firstSearch = false;

                }

                if ((!toastLocationVisualized) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                        !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    Context context = getActivity();

                    if (isOnline()) {
                        Toast t = Toast.makeText(context, "Per usufruire al meglio del servizio di ricerca distributori, " +
                                "attiva la localizzazione", Toast.LENGTH_LONG);
                        t.show();
                        toastLocationVisualized = true;
                    }

                    if(!isOnline()) {
                        Toast t = Toast.makeText(context, "Per usufruire al meglio del servizio di ricerca distributori, " +
                                "attiva la localizzazione e la rete dati", Toast.LENGTH_LONG);
                        t.show();
                        toastLocationVisualized = true;
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try{

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        }catch (Exception e) {

            e.printStackTrace();
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1000, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

                    getMarkers(location);

                    Log.d(TAG,
                            "LocationTrackingService ---> onLocationChanged(): Provider: "
                                    + location.getProvider() + " Lat: "
                                    + location.getLatitude() + " Lng: "
                                    + location.getLongitude() + " Accuracy: "
                                    + location.getAccuracy()
                    );
                }
            }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume(){

        super.onResume();

        Log.d(TAG, "onResume");

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1000, this);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1000, this);
        }

    }

    public void onPause(){

        super.onPause();

        locationManager.removeUpdates(this);
    }

    public void onDestroy() {

        super.onDestroy();

        locationManager.removeUpdates(this);
    }

    private void setMapOptions(MapFragment googleMap) {

        GoogleMapOptions mapOptions = new GoogleMapOptions();

        mapOptions.rotateGesturesEnabled(true);
        mapOptions.compassEnabled(true);
        googleMap.getMap().setMyLocationEnabled(true);
        googleMap.getMap().getUiSettings().setAllGesturesEnabled(true);
        googleMap.getMap().getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getMap().getUiSettings().setZoomControlsEnabled(true);
        googleMap.getMap();

    }

    private void getMapFragment(MapFragment googleMap) {

        googleMap.getMap();
    }

    private void getMarkers(Location location) {

            if (location != null) {
                String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                        "json?location=" + location.getLatitude() + "," + location.getLongitude() +
                        "&radius=7500&sensor=true" +
                        "&keyword=stazioni%20di%20servizio" +
                        "&key=AIzaSyDQOzSn_VhdDuz26Hes3wtci9HHW6WZnyQ";

                Log.d(TAG, "getMarkers() chiamata");
                new GetPlaces().execute(placesSearchStr);
            }
    }

    private boolean isOnline() {

        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        }catch (Exception e) {
            e.printStackTrace();
        }

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;

        }
        return false;
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


            if (result == null) {

                Toast t = Toast.makeText(getActivity(), "Nessun distributore trovato nelle vicinanze",
                        Toast.LENGTH_LONG);
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

                     if(places!=null && placeMarkers!=null){
                        for(int p=0; p<places.length && p<placeMarkers.length; p++){
                        //will be null if a value was missing
                         if(places[p]!=null)
                                placeMarkers[p]=googleMap.getMap().addMarker(places[p]);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

