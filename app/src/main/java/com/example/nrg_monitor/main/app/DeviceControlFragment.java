package com.example.nrg_monitor.main.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;

import java.util.ArrayList;


public class DeviceControlFragment extends Fragment implements View.OnClickListener{

    private DbRequestHandler dbRequestHandler = new DbRequestHandler();
    private ArrayList<Device> devices = new ArrayList<Device>();
    private SharedPreferences mSharedPreferences;
    private String currentUsername;
    private Context mContext;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        currentUsername = mSharedPreferences.getString(getContext().getResources().getString(R.string.logged_in_user),"");
        Button insertDevice = getActivity().findViewById(R.id.insert_device_button);
        insertDevice.setOnClickListener(this);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new getDevicesTask().execute(currentUsername);

        return inflater.inflate(R.layout.fragment_device_control,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }



    private class getDevicesTask extends AsyncTask<String,Void,ArrayList<Device>>{
        @Override
        protected void onPostExecute(ArrayList<Device> fetchedDevices) {
            super.onPostExecute(fetchedDevices);

            devices = fetchedDevices;
            ListView listView = getView().findViewById(R.id.devices_container);

            DevicesAdapter adapter = new DevicesAdapter(mContext,R.layout.device_item,devices);
            listView.setAdapter(adapter);

        }

        @Override
        protected ArrayList<Device> doInBackground(String... strings) {

            return dbRequestHandler.getAllDevices(strings[0]);
        }
    }
}
