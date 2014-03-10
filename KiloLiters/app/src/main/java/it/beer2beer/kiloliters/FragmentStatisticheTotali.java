package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

/**
 * Created by federico on 10/03/14.
 */
public class FragmentStatisticheTotali extends Fragment {

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_statistiche_totali, container, false);
        return v;
    }
    /*
    @Override
    public void onPause(){

    }
    */

} //chiusura classe
