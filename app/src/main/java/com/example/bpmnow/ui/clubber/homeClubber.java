package com.example.bpmnow.ui.clubber;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bpmnow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeClubber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeClubber extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeClubber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeClubber.
     */
    // TODO: Rename and change types and number of parameters
    public static homeClubber newInstance(String param1, String param2) {
        homeClubber fragment = new homeClubber();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_clubber, container, false);

//        Need this because the new approach of android's EdegeToEdge() in the main activity, add also after that
//        some insets and paddings that make the navbar bigger, we dont want it on this navigation so we need to use this
//        on each fragment in the navigation
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            // return CONSUMED so the insets don't propagate to the root view
            return WindowInsetsCompat.CONSUMED;
        });

        return view;
    }
}