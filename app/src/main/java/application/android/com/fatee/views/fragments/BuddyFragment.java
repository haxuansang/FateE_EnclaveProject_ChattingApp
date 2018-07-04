package application.android.com.fatee.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.android.com.fatee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyFragment extends Fragment {


    public BuddyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buddy, container, false);
    }

}
