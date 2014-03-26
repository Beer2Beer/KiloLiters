package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by federico on 10/03/14.
 */
public class FragmentStatisticheTotali extends Fragment {

    public static String TAG = "Statistiche totali";
    DatabaseAdapter db;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_statistiche_totali, container, false);

        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
            }
        });

        return v;
    }

    @Override
    public void onPause(){

        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
