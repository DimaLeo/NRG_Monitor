package com.example.nrg_monitor.home.config;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nrg_monitor.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>{

    private onItemClickListener mListener;
    private ArrayList<RoomItem> roomList;


    public interface onItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickedListener(onItemClickListener listener){

        mListener = listener;

    }


    public static class RoomViewHolder extends RecyclerView.ViewHolder{

        public ImageView roomImage;
        public TextView roomNumberText;
        public ImageView deleteImage;
        public TextInputEditText roomNameText;

        public RoomViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);

            roomImage = itemView.findViewById(R.id.room_image);
            roomNumberText = itemView.findViewById(R.id.room_no);
            deleteImage = itemView.findViewById(R.id.deleteImage);
            roomNameText = itemView.findViewById(R.id.room_name_field);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }

    }

    public RoomsAdapter(ArrayList<RoomItem> roomList){
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_picker_item,parent,false);
        RoomViewHolder rvh = new RoomViewHolder(v,mListener);
        return  rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

        RoomItem currentItem = roomList.get(position);

        holder.roomImage.setImageResource(R.drawable.test_img);
        holder.roomNameText.setHint("Room name here");
        holder.roomNumberText.setText("Room "+(roomList.indexOf(currentItem)+1));
        holder.deleteImage.setImageResource(R.drawable.ic_delete);




    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
