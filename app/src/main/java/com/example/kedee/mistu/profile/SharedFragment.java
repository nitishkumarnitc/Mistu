package com.example.kedee.mistu.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kedee.mistu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharedFragment extends Fragment {


    public SharedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared, container, false);
    }

}
