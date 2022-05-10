package com.example.btl;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ControlFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    static int id_room;
    private TextView temp,sun,temp_comt,sun_comt;
    static public RadioGroup fan;
    static public SwitchMaterial switch_fan,switch_light,switch_auto;
    private RadioButton low_btn,medium_btn,high_btn;
    private int level=0;
    private String light="";
    private Query level_query,light_query,auto_query,location_query;
    private boolean change_light=false,change_fan=false;
    private int init_level;
    private String init_light;
    private boolean clear=false;
    private long id_rec;
    private int cur_temp,cur_sun;
    private FirebaseUser user;
    public static boolean user_click;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner spinner = getView().findViewById(R.id.spinner);
        temp=getView().findViewById(R.id.temp);
        temp_comt=getView().findViewById(R.id.temp_comt);
        sun=getView().findViewById(R.id.sun);
        sun_comt=getView().findViewById(R.id.sun_comt);
        fan=getView().findViewById(R.id.radio_fan);
        switch_fan=getView().findViewById(R.id.switch_fan);
        switch_light=getView().findViewById(R.id.light_switch);
        low_btn = getView().findViewById(R.id.low);
        medium_btn = getView().findViewById(R.id.medium);
        high_btn = getView().findViewById(R.id.high);
        switch_auto=getView().findViewById(R.id.auto_button);

        switch_auto.setOnCheckedChangeListener(this);
        switch_fan.setOnCheckedChangeListener(this);
        switch_light.setOnCheckedChangeListener(this);

        auto_query=FirebaseDatabase.getInstance().getReference("User").orderByChild("email").equalTo(Login.email);
        auto_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    Login.auto=(String)snap.child("auto").getValue();
                    if (Login.auto.equals("true"))switch_auto.setChecked(true);
                    else switch_auto.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Set event for fan level change

        fan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!clear&&(i==R.id.low || i==R.id.medium || i==R.id.high)){
                    if (i==R.id.low){
                        level=1;
                        low_btn.setChecked(true);
                    }
                    else if (i==R.id.medium){
                        level=2;
                        medium_btn.setChecked(true);
                    }
                    else {
                        level=3;
                        high_btn.setChecked(true);
                    }
                    if(user_click)switch_auto.setChecked(false);
                    switch_fan.setChecked(true);

                }
                else level=0;
                if (change_fan||init_level!=level){
                    FirebaseDatabase.getInstance().getReference("Record").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            id_rec=snapshot.getChildrenCount()+1;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                    Calendar calendar=Calendar.getInstance();
//                    format.setTimeZone(TimeZone.getTimeZone("GMT+8:12"));
                    if(Login.auto.equals("false")) {
                        Record rec = new Record(id_rec, id_room, id_room, cur_sun, cur_temp, level, "", format.format(Calendar.getInstance().getTime()), Login.email, "Fan", false);
                        FirebaseDatabase.getInstance().getReference("Record").push().setValue(rec);
                        change_fan = true;
                    }
                }
                level_query.addListenerForSingleValueEvent(level_listener);

            }
        });

        ArrayList<String> room = new ArrayList<>();
        room.add("Living Room");
        room.add("Bedroom");
        room.add("Kitchen");
        room.add("Bathroom");
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, room);
        spinner.setAdapter(roomAdapter);
        spinner.setSelection(id_room-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_room=i+1;
                change_fan=false;change_light=false;
                Query query=FirebaseDatabase.getInstance().getReference("Location")
                        .orderByChild("id").equalTo(id_room);
                query.addValueEventListener(valueEventListener);
                if (light_query!=null)light_query.removeEventListener(light_listener_db);
                light_query=FirebaseDatabase.getInstance().getReference("Light")
                        .orderByChild("id_location").equalTo(id_room);
                light_query.addListenerForSingleValueEvent(init_light_listener);
                light_query.addValueEventListener(light_listener_db);
                if(level_query!=null)level_query.removeEventListener(level_listener_db);
                level_query=FirebaseDatabase.getInstance().getReference("Fan")
                        .orderByChild("id_location").equalTo(id_room);
                level_query.addListenerForSingleValueEvent(init_level_listener);
                level_query.addValueEventListener(level_listener_db);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        })    ;
    }
    ValueEventListener init_level_listener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()){
                long init_level1=(long)snap.child("level").getValue();
                user_click=false;
                if(init_level1==1){
                    init_level=1;
                    low_btn.setChecked(true);
                }
                else if(init_level1==2){
                    init_level=2;
                    medium_btn.setChecked(true);
                }
                else if(init_level1==3){
                    init_level=3;
                    high_btn.setChecked(true);
                }
                else {
                    init_level=0;
                    clear=true;
                    if (level==1)low_btn.setChecked(false);
                    else if (level==2)medium_btn.setChecked(false);
                    else if (level==3)high_btn.setChecked(false);
                    clear=false;
                    switch_fan.setChecked(false);
                }
                user_click=true;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    ValueEventListener level_listener_db=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()){
                long level_db=(long)snap.child("level").getValue();
                user_click=false;
                if(level_db==1){
                    level=1;
                    low_btn.setChecked(true);
                }
                else if(level_db==2){
                    level=2;
                    medium_btn.setChecked(true);
                }
                else if(level_db==3){
                    level=3;
                    high_btn.setChecked(true);
                }
                else {
                    clear=true;
                    if (level==1)low_btn.setChecked(false);
                    else if (level==2)medium_btn.setChecked(false);
                    else if (level==3)high_btn.setChecked(false);
                    clear=false;
                    switch_fan.setChecked(false);
                }
                user_click=true;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    ValueEventListener level_listener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()){
                snap.getRef().child("level").setValue(level);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    ValueEventListener init_light_listener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()) {
                String init_light_db = (String)snap.child("status").getValue();
                user_click=false;
                if (init_light_db.equals("on")) {
                    init_light="on";
                    switch_light.setChecked(true);
                }
                else{
                    init_light="off";
                    switch_light.setChecked(false);
                }
                user_click=true;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    ValueEventListener light_listener_db=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()){
                String light_db=(String)snap.child("status").getValue();
                user_click=false;
                if(light_db.equals("on")){
                    switch_light.setChecked(true);
                } else{
                    switch_light.setChecked(false);
                }
                user_click=true;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    ValueEventListener light_listener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()){
                snap.getRef().child("status").setValue(light);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
            for (DataSnapshot snapshot:datasnapshot.getChildren()){
                Location location=snapshot.getValue(Location.class);
                cur_temp=location.getTemp();
                temp.setText(""+cur_temp+" \u2103");
                if(cur_temp<=26)temp_comt.setText("Very cold");
                else if(cur_temp<=28)temp_comt.setText("Quite cold");
                else if(cur_temp<=32)temp_comt.setText("Cool");
                else if(cur_temp<=34)temp_comt.setText("Quite hot");
                else temp_comt.setText("Very hot");
                cur_sun=location.getLight();
                sun.setText(""+cur_sun+" cd");
                if(cur_sun<=3)sun_comt.setText("Very dark");
                else if(cur_sun<=4)sun_comt.setText("Quite dark");
                else if(cur_sun<=5)sun_comt.setText("Normal");
                else if(cur_sun<=6)sun_comt.setText("Quite bright");
                else sun_comt.setText("Very bright");
//                light_query=FirebaseDatabase.getInstance().getReference("Light")
//                        .orderByChild("id_location").equalTo(id_room);
//                light_query.addListenerForSingleValueEvent(init_light_listener);
//                light_query.addValueEventListener(light_listener_db);
//                level_query=FirebaseDatabase.getInstance().getReference("Fan")
//                        .orderByChild("id_location").equalTo(id_room);
//                level_query.addListenerForSingleValueEvent(init_level_listener);
//                level_query.addValueEventListener(level_listener_db);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()){
            case R.id.auto_button:
                if(b)Login.auto="true";
                else Login.auto="false";
                auto_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap:snapshot.getChildren()){
                            snap.getRef().child("auto").setValue(Login.auto);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.switch_fan:
                if(b){
                    if (level==0)low_btn.setChecked(true);
                }
                else{
                    clear=true;
                    if (level==1)low_btn.setChecked(false);
                    else if (level==2)medium_btn.setChecked(false);
                    else if (level==3)high_btn.setChecked(false);
                    clear=false;
                    if(user_click)switch_auto.setChecked(false);
                }
                break;
            case R.id.light_switch:
                if(b)light="on";
                else light="off";
                if (change_light|| init_light!=light){
                    FirebaseDatabase.getInstance().getReference("Record").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            id_rec=snapshot.getChildrenCount()+1;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                    Calendar calendar=Calendar.getInstance();
                    if(Login.auto.equals("false")) {
                        Record rec = new Record(id_rec, id_room, id_room, cur_sun, cur_temp, -1, light, format.format(Calendar.getInstance().getTime()), Login.email, "Light", false);
                        FirebaseDatabase.getInstance().getReference("Record").push().setValue(rec);
                        change_light = true;
                    }
                }
                light_query.addListenerForSingleValueEvent(light_listener);
                if (user_click) switch_auto.setChecked(false);
                break;
            default:
                break;
        }
    }
}