package com.example.btl;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name=getView().findViewById(R.id.name);
        TextView email=getView().findViewById(R.id.email);
        TextView phone=getView().findViewById(R.id.phone);
        TextView time_log=getView().findViewById(R.id.time_log);
        TextView time_create=getView().findViewById(R.id.time_create);
        name.setText(Login.name);
        email.setText(Login.email);
        phone.setText(Login.phone);
        time_log.setText(Login.time_log);
        time_create.setText(Login.time_create);
        CardView sign_out=getView().findViewById(R.id.cardView1);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Login.class));
            }
        });
    }


}