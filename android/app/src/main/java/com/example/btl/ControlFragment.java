//package com.example.btl;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ControlFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ControlFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ControlFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ControlFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ControlFragment newInstance(String param1, String param2) {
//        ControlFragment fragment = new ControlFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_control, container, false);
//    }
//}
package com.example.btl;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.TimeZone;


public class ControlFragment extends Fragment {
    static int id_room;
    private TextView temp,sun,temp_comt,sun_comt;
    private RadioGroup fan;
    private SwitchMaterial switch_fan,switch_light;
    private RadioButton low_btn,medium_btn,high_btn;
    private int level=0;
    private String light="";
    private Query level_query,light_query;
    private boolean change_light=false,change_fan=false;
    private int init_level;
    String init_light;
    private boolean clear=false;
    private long id_rec;
    private int cur_temp,cur_sun;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_control,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
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

        // Set event for fan
        switch_fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if (level==0)low_btn.setChecked(true);
                }
                else{
                    clear=true;
                    if (level==1)low_btn.setChecked(false);
                    else if (level==2)medium_btn.setChecked(false);
                    else if (level==3)high_btn.setChecked(false);
                    clear=false;
                }
            }
        });
        fan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!clear&&(i==R.id.low || i==R.id.medium || i==R.id.high)){
                    if (i==R.id.low){
                        level=1;low_btn.setChecked(true);
                    }
                    else if (i==R.id.medium){
                        level=2;
                        medium_btn.setChecked(true);
                    }
                    else {
                        level=3;
                        high_btn.setChecked(true);
                    }
                    switch_fan.setChecked(true);
                }
                else level=0;
                if (change_fan||(!change_fan&&init_level!=level)){
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
                    Record rec=new Record(id_rec,id_room,id_room,cur_sun,cur_temp,level,"",format.format(Calendar.getInstance().getTime()),Login.email,"Fan",false);
                    FirebaseDatabase.getInstance().getReference("Record").push().setValue(rec);
//                    FanRec fan_rec=new FanRec(id_room,id_rec,level);
//                    FirebaseDatabase.getInstance().getReference("Record Fan").push().setValue(fan_rec);

                    change_fan=true;
                }
                level_query.addListenerForSingleValueEvent(level_listener);
            }
        });

        // Set event for light
        switch_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)light="on";
                else light="off";
                if (change_light|| (!change_light&&init_light!=light)){
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
                    Record rec=new Record(id_rec,id_room,id_room,cur_sun,cur_temp,-1,light,format.format(Calendar.getInstance().getTime()),Login.email,"Light",false);
                    FirebaseDatabase.getInstance().getReference("Record").push().setValue(rec);
//                    LightRec light_rec=new LightRec(id_room,id_rec,light);
//                    FirebaseDatabase.getInstance().getReference("Record Light").push().setValue(light_rec);
                    change_light=true;
                }
                light_query.addListenerForSingleValueEvent(light_listener);
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
//        spinner.s
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_room=i+1;
                change_fan=false;change_light=false;
                Query query=FirebaseDatabase.getInstance().getReference("Location")
                        .orderByChild("id").equalTo(id_room);
                query.addListenerForSingleValueEvent(valueEventListener);
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
                Object init_level1=snap.child("level").getValue();
                if(init_level1.equals((long)1)){
                    init_level=1;
                    level=1;
                    low_btn.setChecked(true);
                }
                else if(init_level1.equals((long)2)){
                    level=2;
                    init_level=2;
                    medium_btn.setChecked(true);
                }
                else if(init_level1.equals((long)3)){
                    level=3;
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
    ValueEventListener init_light_lister=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snap:snapshot.getChildren()) {
                Object init_light1 = snap.child("status").getValue();
                if (init_light1.equals("on")) {
                    init_light="on";
                    switch_light.setChecked(true);
                } else{
                    init_light="off";
                    switch_light.setChecked(false);
                }
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
                if(cur_temp<=26){
                    temp_comt.setText("Very cold");
                }
                else if(cur_temp<=28){
                    temp_comt.setText("Quite cold");
                }
                else if(cur_temp<=32){
                    temp_comt.setText("Cool");
                }
                else if(cur_temp<=34){
                    temp_comt.setText("Quite hot");
                }
                else{
                    temp_comt.setText("Very hot");
                }
                cur_sun=location.getLight();
                sun.setText(""+cur_sun+" cd");
                if(cur_sun<=3){
                    sun_comt.setText("Very dark");
                }
                else if(cur_sun<=4){
                    sun_comt.setText("Quite dark");
                }
                else if(cur_sun<=5){
                    sun_comt.setText("Normal");
                }
                else if(cur_sun<=6){
                    sun_comt.setText("Quite bright");
                }
                else{
                    sun_comt.setText("Very bright");
                }
                light_query=FirebaseDatabase.getInstance().getReference("Light")
                        .orderByChild("id_location").equalTo(id_room);
                light_query.addListenerForSingleValueEvent(init_light_lister);
                level_query=FirebaseDatabase.getInstance().getReference("Fan")
                        .orderByChild("id_location").equalTo(id_room);
                level_query.addListenerForSingleValueEvent(init_level_listener);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
}