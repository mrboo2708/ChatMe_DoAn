package com.example.chatme_doan.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chatme_doan.fragment.ChatFragment;
import com.example.chatme_doan.fragment.ContactFragment;
import com.example.chatme_doan.fragment.GroupFragment;
import com.example.chatme_doan.fragment.ProfileFragment;
import com.example.chatme_doan.R;
import com.example.chatme_doan.utils.Util;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class DashBoard extends AppCompatActivity {

    private ChipNavigationBar navigationBar;
    Fragment fragment = null;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        util = new Util();
        navigationBar = findViewById(R.id.navigationChip);

        if (savedInstanceState == null) {
            navigationBar.setItemSelected(R.id.chat, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, new ChatFragment()).commit();
        }

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {

                    case R.id.chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.contacts:
                        fragment = new ContactFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.group:
                        fragment = new GroupFragment();
                }

                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.dashboardContainer, fragment).commit();
            }
        });


    }

    @Override
    protected void onResume() {
        util.updateOnlineStatus("online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        util.updateOnlineStatus(String.valueOf(System.currentTimeMillis()));
        super.onPause();
    }
}
