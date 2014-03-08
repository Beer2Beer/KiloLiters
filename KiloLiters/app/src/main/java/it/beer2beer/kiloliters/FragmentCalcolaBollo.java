package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by federico on 08/03/14.
 */

public class FragmentCalcolaBollo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_calcola_bollo,container,false);

        return v;

    }
}
