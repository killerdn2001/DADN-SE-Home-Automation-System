package com.example.btl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<mAdapter.MyViewHolder> {

    Context context;
    ArrayList<Record> list;

    public mAdapter(Context context, ArrayList<Record> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Record notifiRec=list.get(position);
        String room="";
        switch (notifiRec.getId_sensor()){
            case 1:
                room="Living room";
                break;
            case 2:
                room="Bedroom";
                break;
            case 3:
                room="Kitchen";
                break;
            case 4:
                room="Bathroom";
                break;
        }
        if (notifiRec.getType().equals("Fan")) {
            if(notifiRec.getLevel()==0)holder.textView.setText("Fan in "+room+" automatically turn off!");
            else holder.textView.setText("Fan in "+room+" automatically turn on to level "+notifiRec.getLevel()+"!");
        }
        else if (notifiRec.getType().equals("Light")){
            if(notifiRec.getStatus()=="on")holder.textView.setText("Light in "+room+" automatically turn on!");
            else holder.textView.setText("Light in "+room+" automatically turn off!");
        }

        holder.textView2.setText(notifiRec.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView,textView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.noti);
            textView2=itemView.findViewById(R.id.noti_time);

        }
    }
}
