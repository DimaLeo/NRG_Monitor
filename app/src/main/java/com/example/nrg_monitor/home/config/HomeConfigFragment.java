package com.example.nrg_monitor.home.config;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nrg_monitor.R;

import java.util.ArrayList;


public class HomeConfigFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Context mContext;
    private ArrayList rooms;
    private RecyclerView mResyclerView;
    private RoomsAdapter mRcAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner mSpinner;
    private Button plusButton,minusButton,continueButton;
    private final Integer[] SPINNER_ITEMS = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    private ArrayAdapter<Integer> spinnerAdapter;

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
        rooms = new ArrayList<RoomItem>();
        mResyclerView = getView().findViewById(R.id.available_rooms);
        mResyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRcAdapter = new RoomsAdapter(rooms);
        mRcAdapter.setOnItemClickedListener(new RoomsAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                rooms.get(position);
                Log.d("Dima","From home config frag : Position to be erased: "+position);
                rooms.remove(position);
                mRcAdapter.notifyItemRemoved(position);
                for(int i = position;i<rooms.size();i++){
                    mRcAdapter.notifyItemChanged(i);
                }
                if (rooms.isEmpty()) {
                    mSpinner.setSelection(0);
                }
                else{
                    mSpinner.setSelection(rooms.size());

                }
            }
        });
        mResyclerView.setLayoutManager(mLayoutManager);
        mResyclerView.setAdapter(mRcAdapter);

        mSpinner = view.findViewById(R.id.no_of_rooms_picker);
        spinnerAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_item,SPINNER_ITEMS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setSelection(0);

        minusButton = view.findViewById(R.id.minus_button);
        minusButton.setOnClickListener(this);
        plusButton = view.findViewById(R.id.plus_button);
        plusButton.setOnClickListener(this);
        continueButton = view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);




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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.plus_button:

                if(rooms.size()<20){
                    mSpinner.setSelection(mSpinner.getSelectedItemPosition()+1);

                }



                break;
            case R.id.minus_button:
                if(!rooms.isEmpty()){
                    mSpinner.setSelection(mSpinner.getSelectedItemPosition()-1);

                }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int item = (Integer)parent.getItemAtPosition(position);
        int size = rooms.size();

        int difference;
        if(size>item){
            difference = size - item;
            for(int i = size; i>item;i--){
                rooms.remove(i-1);
                mRcAdapter.notifyItemRemoved(i);

            }
        }
        if(size<item){
            for(int i=size;i<item;i++){
                rooms.add(new RoomItem());
                mRcAdapter.notifyItemInserted(i);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
