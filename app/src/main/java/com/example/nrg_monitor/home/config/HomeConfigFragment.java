package com.example.nrg_monitor.home.config;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nrg_monitor.R;

import java.util.ArrayList;


public class HomeConfigFragment extends Fragment {

    private ArrayList rooms;
    private Context mContext;

    public HomeConfigFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeConfigFragment newInstance() {
        HomeConfigFragment fragment = new HomeConfigFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rooms = new ArrayList<RoomItem>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_config, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();

        Spinner spinner = view.findViewById(R.id.no_of_rooms_picker);
        RecyclerView recycler = view.findViewById(R.id.available_rooms);
        Button minusButton = view.findViewById(R.id.minus_button);
        Button plusButton = view.findViewById(R.id.plus_button);
        Button continueButoon = view.findViewById(R.id.continue_button);




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

}
