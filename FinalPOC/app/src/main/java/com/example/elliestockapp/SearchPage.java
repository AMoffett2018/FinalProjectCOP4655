package com.example.elliestockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpage);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_favorite:
                        intent = new Intent(getApplicationContext(), ProfilePage.class);
                        //intent.putExtra("isLoc", true);
                        startActivity(intent);
                        break;
                    case R.id.action_settings:
                        intent = new Intent(getApplicationContext(), LogOutPage.class);
                        //intent.putExtra(EXTRA_MESSAGE, coordMessage);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}