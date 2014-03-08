package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentStatistiche extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_statistiche, container, false);
        return v;
    }

}
