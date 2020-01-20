package com.example.nrg_monitor.main.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nrg_monitor.R;
import com.google.android.material.textfield.TextInputEditText;

public class InsertDeviceDialog extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private final String typeArray[] = {"Light Bulb","Fridge","Air Conditioner","TV"};
    private final String lightBulbBrands[] = {"Philips","Eurolamp","Xiaomi","Osram","Other"};
    private final String fridgeBrands[] = {"LG","Samsung","Bosch","Whirlpool","Hitachi","Other"};
    private final String airconBrands[] = {"Mitsubishi","Toyotomi","Daikin","Fujistsu","Other"};
    private final String tvBrands[] = {"LG","Samsung","Philips","Sony","F&U","Hitachi","Other"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_device_dialog_fragment,container,false);


        TextInputEditText nameInput = view.findViewById(R.id.insertion_name);
        Spinner typeSpinner = view.findViewById(R.id.insertion_type);
        Spinner brandSpinner = view.findViewById(R.id.insertion_brand);
        TextInputEditText wattageInput = view.findViewById(R.id.insertion_wattage);
        Button insertButton = view.findViewById(R.id.insert_dev_button);
        Button cancelButton = view.findViewById(R.id.cancel_insert_button);

        //Assuming the user entered the correct info , proceed , maybe add checks for everything later.

        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,typeArray);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        typeSpinner.setOnItemSelectedListener(this);

        insertButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);



        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.insert_dev_button:


                break;

            case R.id.cancel_insert_button:
                getDialog().dismiss();
                break;
        }



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
