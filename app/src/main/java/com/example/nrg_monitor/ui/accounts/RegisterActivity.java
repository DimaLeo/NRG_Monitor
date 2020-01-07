package com.example.nrg_monitor.ui.accounts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.nrg_monitor.R;
import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {


    //Tab Activity containing fragments in each tab
    //Mostly copied the code from Tabbed activity of android studio
    //nothing interesting here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.reg_fragment_container);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);




        //getSupportFragmentManager().beginTransaction().add(R.id.reg_fragment_container,new RegisterFragment()).commit();



    }
}
