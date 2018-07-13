package application.android.com.fatee.views.fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.android.com.fatee.R;

public class RoomFragment extends Fragment {
    private static RoomFragment instance;

    public static RoomFragment getInstance() {
        if(instance == null)
            instance = new RoomFragment();
        return instance;
    }

    public RoomFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

}
