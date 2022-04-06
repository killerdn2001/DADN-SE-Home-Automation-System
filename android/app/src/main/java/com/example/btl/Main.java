package com.example.btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {
    public static BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        bottom_nav = findViewById(R.id.bot_nav);
        System.out.println(bottom_nav);
        bottom_nav.setOnItemSelectedListener(navListen);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListen=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment sel=null;
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    sel = new HomeFragment();
                    break;
                case R.id.controlFragment:
                    sel = new ControlFragment();
                    break;
                case R.id.staticFragment:
                    sel = new StaticFragment();
                    break;
                case R.id.notifiFragment:
                    sel = new NotifiFragment();
                    break;
                case R.id.accountFragment:
                    sel = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,sel).commit();
            return true;
        }
    };
}