package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.view.LayoutInflater;
import android.widget.Toast;


/**
 * Created by federico on 08/03/14.
 */

public class FragmentCalcolaBollo extends Fragment {

    private String url_ACI = "http://online.aci.it/acinet/calcolobollo/#inizio-pagina";

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onPause() {


    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Toast toast_caricamento = Toast.makeText(getActivity(), "Caricamento in corso...", Toast.LENGTH_SHORT);
        toast_caricamento.show();
        View mainView = (View) inflater.inflate(R.layout.view_calcola_bollo, container, false);
        WebView webView = (WebView) mainView.findViewById(R.id.web_view_calcola_bollo); //faccio un cast così uso la funzione findViewById

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.loadUrl(url_ACI);

        return mainView;

    }
}
