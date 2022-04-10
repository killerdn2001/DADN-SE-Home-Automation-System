package com.example.btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {
    public static BottomNavigationView bottom_nav;
    FragmentManager fragmentManager=getFragmentManager();
    private HomeFragment homeFragment;
    private ControlFragment controlFragment;
    private NotifiFragment notifyFragment;
    private StaticFragment staticFragment;
    private AccountFragment accountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        homeFragment=new HomeFragment();

        notifyFragment=new NotifiFragment();
        staticFragment=new StaticFragment();
        accountFragment=new AccountFragment();

        bottom_nav = findViewById(R.id.bot_nav);
        bottom_nav.setOnItemSelectedListener(navListen);
        fragmentManager.beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListen=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment sel=null;
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    sel = homeFragment;
                    break;
                case R.id.controlFragment:
                    if(controlFragment!=null) {
                        ControlFragment.switch_light.setOnCheckedChangeListener(null);
                        ControlFragment.switch_fan.setOnCheckedChangeListener(null);
                        ControlFragment.switch_auto.setOnCheckedChangeListener(null);
                        ControlFragment.fan.setOnCheckedChangeListener(null);
                    }
                    controlFragment=new ControlFragment();
                    sel=controlFragment;
                    break;
                case R.id.notifiFragment:
                    sel = notifyFragment;
                    break;
                case R.id.staticFragment:
                    sel = staticFragment;
                    break;
                case R.id.accountFragment:
                    sel=accountFragment;
                    break;
                default:
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.fragment_container,sel).commit();
            return true;
        }
    };
}