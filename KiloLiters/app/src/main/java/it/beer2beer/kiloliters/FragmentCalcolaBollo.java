package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.LayoutInflater;
import android.widget.Toast;


/**
 * Created by federico on 08/03/14.
 */

public class FragmentCalcolaBollo extends Fragment {

    private String url_ACI = "http://online.aci.it/acinet/calcolobollo/#inizio-pagina";
    boolean toastLoadingPage = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        View mainView = (View) inflater.inflate(R.layout.view_calcola_bollo, container, false);
        webView = (WebView) mainView.findViewById(R.id.web_view_calcola_bollo);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl(url_ACI);

        mainView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);


                if (!toastLoadingPage) {

                    Context context = getActivity();
                    Toast t = Toast.makeText(context, "Caricamento pagina ACI in corso...", Toast.LENGTH_SHORT);
                    t.show();

                    toastLoadingPage = true;
                }

            }
        });

        return mainView;

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
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }


    };
*/
} //chiusura classe
