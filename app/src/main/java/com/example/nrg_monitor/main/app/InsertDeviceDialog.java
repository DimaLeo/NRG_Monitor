package com.example.nrg_monitor.main.app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;


public class InsertDeviceDialog extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener, DialogInterface.OnDismissListener {

    private final String typeArray[] = {"Light Bulb","Fridge","Air Conditioner","TV"};
    private final String lightBulbBrands[] = {"Philips","Eurolamp","Xiaomi","Osram","Other"};
    private final String fridgeBrands[] = {"LG","Samsung","Bosch","Whirlpool","Hitachi","Other"};
    private final String airconBrands[] = {"Mitsubishi","Toyotomi","Daikin","Fujistsu","Other"};
    private final String tvBrands[] = {"LG","Samsung","Philips","Sony","F&U","Hitachi","Other"};
    private View mView ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_device_dialog_fragment,container,false);

        mView = view;

        TextInputEditText nameInput = view.findViewById(R.id.insertion_name);
        Spinner typeSpinner = view.findViewById(R.id.insertion_type);

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

                TextInputEditText nameInput = mView.findViewById(R.id.insertion_name);
                Spinner typeSpinner = mView.findViewById(R.id.insertion_type);
                Spinner brandSpinner = mView.findViewById(R.id.insertion_brand);
                TextInputEditText wattageInput = mView.findViewById(R.id.insertion_wattage);

                String name = nameInput.getText().toString();
                String type = typeSpinner.getSelectedItem().toString();
                String brand = brandSpinner.getSelectedItem().toString();
                Integer wattage = Integer.parseInt(wattageInput.getText().toString());


                Device device = new Device(name,type,brand,wattage,0.0,0);

                new InsertDeviceTask().execute(device);

                ((DeviceControlFragment) getParentFragment()).Refresh();

                getDialog().dismiss();

                break;

            case R.id.cancel_insert_button:
                getDialog().dismiss();
                break;
        }



    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner brandSpinner = mView.findViewById(R.id.insertion_brand);
        ArrayAdapter<String> brandAdapter;

        switch(typeArray[position]){
            case "Light Bulb":
                brandAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,lightBulbBrands);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);
                break;

            case "Fridge":
                brandAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,fridgeBrands);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);
                break;

            case "Air Conditioner":
                brandAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,airconBrands);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);
                break;

            case "TV":
                brandAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,tvBrands);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class InsertDeviceTask extends AsyncTask<Device,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Device... devices) {

            Device deviceToInsert = devices[0];


            JsonObject obj = new JsonObject();
            obj.addProperty("device_name",deviceToInsert.getDevice_name());
            obj.addProperty("device_type",deviceToInsert.getDevice_type());
            obj.addProperty("device_brand",deviceToInsert.getDevice_brand());
            obj.addProperty("device_wattage",deviceToInsert.getDevice_wattage());

            SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = mShared.getString(getContext().getResources().getString(R.string.logged_in_user),"");
            obj.addProperty("username",username);

            String jsonString = obj.toString();

            DbRequestHandler handler = new DbRequestHandler();
            String response = handler.insertToDevices(jsonString);

            Log.d("Dima",response);

            return null;
        }
    }
}
