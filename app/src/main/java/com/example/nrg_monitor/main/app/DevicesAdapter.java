package com.example.nrg_monitor.main.app;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nrg_monitor.DbRequestHandler;
import com.example.nrg_monitor.R;

import java.util.ArrayList;

public class DevicesAdapter extends ArrayAdapter<Device> {

    private int resourceLayout;
    private Context mContext;
    private EventListener listener;

    public interface EventListener{
        void onEvent();
    }


    public DevicesAdapter(Context context,int resource, ArrayList<Device> devices,EventListener listener){

        super(context,resource,devices);
        this.resourceLayout = resource;
        this.mContext = context;
        this.listener = listener;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        final Device device = getItem(position);

        if(device!=null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(resourceLayout,parent,false);

            TextView dev_name = convertView.findViewById(R.id.device_name_label);
            TextView dev_brand = convertView.findViewById(R.id.device_brand_label);
            TextView dev_wattage = convertView.findViewById(R.id.device_wattage_field);
            TextView dev_runtime = convertView.findViewById(R.id.device_runtime_label);
            TextView dev_type = convertView.findViewById(R.id.device_type_label);
            TextView dev_total = convertView.findViewById(R.id.device_total_consumption);
            Switch on_off_switch = convertView.findViewById(R.id.on_off_toggle);
            on_off_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new change_device_state().execute(device);

                }
            });

            dev_name.setText(device.getDevice_name());
            dev_brand.setText(device.getDevice_brand().toString());
            dev_wattage.setText(device.getDevice_wattage().toString());
            dev_type.setText(device.getDevice_type());
            Double device_runtime_mins = device.getDevice_runtime();
            Double device_hours = device_runtime_mins/60;
            dev_runtime.setText(""+device_hours+" hrs");
            double total_kwh = ((device.getDevice_wattage())*(device_hours))/1000;
            dev_total.setText(""+total_kwh+" kwh");

            if(device.getDevice_activity_status()==0){
                on_off_switch.setChecked(false);
            }
            if(device.getDevice_activity_status()==1){
                on_off_switch.setChecked(true);
            }

        }

        return convertView;
    }

    private class change_device_state extends AsyncTask<Device,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onEvent();
        }

        @Override
        protected String doInBackground(Device... devices) {

            long timestamp = System.currentTimeMillis();
            DbRequestHandler handler = new DbRequestHandler();
            handler.changeDeviceState(timestamp,devices[0].getDevice_id());

            return null;
        }
    }



}
