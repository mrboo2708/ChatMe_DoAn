package com.chatapp.chatme_doan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chatapp.chatme_doan.fragment.GetGroupDetailFragment;
import com.chatapp.chatme_doan.R;

import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.groupContainer,
                new GetGroupDetailFragment()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}