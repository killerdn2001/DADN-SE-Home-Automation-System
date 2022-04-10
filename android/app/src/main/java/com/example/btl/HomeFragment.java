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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CardView living_room=getView().findViewById(R.id.livingroom);
        CardView bed_room=getView().findViewById(R.id.bedroom);
        CardView kitchen=getView().findViewById(R.id.kitchen);
        CardView bath_room=getView().findViewById(R.id.bathroom);
        TextView name=getView().findViewById(R.id.welcome);
        name.setText("Welcome "+Login.name);

        living_room.setOnClickListener(this);
        bed_room.setOnClickListener(this);
        kitchen.setOnClickListener(this);
        bath_room.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.livingroom:
                ControlFragment.id_room=1;
                break;
            case R.id.bedroom:
                ControlFragment.id_room=2;
                break;
            case R.id.kitchen:
                ControlFragment.id_room=3;
                break;
            case R.id.bathroom:
                ControlFragment.id_room=4;
                break;
            default:
                break;
        }
        Main.bottom_nav.setSelectedItemId(R.id.controlFragment);
//        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new ControlFragment()).commit();
    }




}