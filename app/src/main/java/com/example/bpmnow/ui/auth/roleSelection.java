package com.example.bpmnow.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bpmnow.MainActivity;
import com.example.bpmnow.R;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link roleSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class roleSelection extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public roleSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment roleSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static roleSelection newInstance(String param1, String param2) {
        roleSelection fragment = new roleSelection();
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

        // Handle back button press - exit app instead of going back
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Exit the app when back is pressed
                requireActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_role_selection, container, false);

        MaterialCardView cardClubber = view.findViewById(R.id.cardClubber);
        MaterialCardView cardDJ = view.findViewById(R.id.cardDJ);

        cardClubber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Getting the method from MainActivity (this is the approach because the standard is to have one activity
//                and just change the navigation graphs, and we do this from the main activity, but we cant access
//                the elements of the fragment from the activity so we can do this)
                ((MainActivity) requireActivity()).switchToGraphClubber();
            }
        });

        cardDJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Getting the method from MainActivity (this is the approach because the standard is to have one activity
//                and just change the navigation graphs, and we do this from the main activity, but we cant access
//                the elements of the fragment from the activity so we can do this)
        ((MainActivity) requireActivity()).switchToGraphDJ();
            }
        });
        return view;
    }
}