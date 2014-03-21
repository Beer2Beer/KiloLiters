package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentInserimentoDati extends Fragment {

     @Override
     public void onCreate(Bundle savedInstanceState) {

          super.onCreate(savedInstanceState);

          }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_inserimento_dati, container, false);
        return v;
    }


    @Override
    public void onPause() {

        super.onPause();
    }

      @Override
    public void onDestroy() {

        super.onDestroy();
    }

} //chiusura classe