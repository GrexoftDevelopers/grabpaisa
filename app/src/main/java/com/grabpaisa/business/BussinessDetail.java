package com.grabpaisa.business;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.grabpaisa.R;
import fr.ganfra.materialspinner.MaterialSpinner;


public class BussinessDetail extends AppCompatActivity {
    MaterialSpinner spinner1, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bussiness_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //     toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



    }
}
