package it.beer2beer.kiloliters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by federico on 08/03/14.
 */
public class FragmentStatistiche extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_statistiche, container, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
            }
        });
        return view;
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

}
