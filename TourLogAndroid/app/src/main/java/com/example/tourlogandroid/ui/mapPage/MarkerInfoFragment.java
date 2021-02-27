package com.example.tourlogandroid.ui.mapPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tourlogandroid.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class MarkerInfoFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button favorites;

    public MarkerInfoFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marker_info, container, false);

        return view;
    }


}