package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.view.LayoutInflater;
import android.widget.Toast;


/**
 * Created by federico on 08/03/14.
 */

public class FragmentCalcolaBollo extends Fragment {

    private String url_ACI = "http://online.aci.it/acinet/calcolobollo/#inizio-pagina";
    boolean toastLoadingPageVisualized = false;
    boolean wasNotOnline = false;
    boolean wasNotOnlineVisualized = false;
    boolean wasOnline = false;
    boolean wasOnlineVisualized = false;
    NetworkInfo netInfo;

    WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.view_calcola_bollo, container, false);
        webView = (WebView) view.findViewById(R.id.web_view_calcola_bollo);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl(url_ACI);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);


                if (!toastLoadingPageVisualized) {

                    if(isOnline()) {
                        Context context = getActivity();
                        Toast t = Toast.makeText(context, "Caricamento sito web ACI in corso...", Toast.LENGTH_SHORT);
                        t.show();
                        wasOnline = true;

                    }

                    if (!isOnline()) {

                        Context context = getActivity();
                        Toast t = Toast.makeText(context, "Attivare la rete dati per accedere al servizio", Toast.LENGTH_LONG);
                        t.show();
                        wasNotOnline = true;

                    }

                    toastLoadingPageVisualized = true;

                }

                if (wasNotOnline && isOnline() && !wasNotOnlineVisualized) {

                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Caricamento sito web ACI in corso...", Toast.LENGTH_SHORT);
                    t.show();
                    webView.loadUrl(url_ACI);
                    wasNotOnlineVisualized = true;

                }

                if (wasOnline && !isOnline() && !wasOnlineVisualized) {

                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Attivare la rete dati per accedere al servizio", Toast.LENGTH_LONG);
                    t.show();
                    wasOnlineVisualized = true;
                }
            }
        });

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onResume() {

        super.onResume();
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

} //chiusura classe
