package com.grabpaisa.navigation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.grabpaisa.R;
import com.grabpaisa.dashboard.DashBoard;


public class HomeActivity extends AppCompatActivity{


     ProgressDialog pd;

	public boolean firstTimeLogin;



     String strResponse, Ticket_No, Ticket_Id, Product_name, Brand_name, Tdate, ToDisplay;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.home_activity);
		 Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		 firstTimeLogin = getIntent().getExtras().getBoolean("fromCreateAccount");

		 setSupportActionBar(toolbar);
		 getSupportActionBar().setHomeButtonEnabled(false);
		 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		 getSupportActionBar().setDisplayShowTitleEnabled(true);
		 getSupportActionBar().setDisplayUseLogoEnabled(true);
		 getSupportActionBar().setLogo(R.mipmap.ic_app);


		 NavigationFragment drawer = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
		 drawer.setup(R.id.fragment, (DrawerLayout) findViewById(R.id.drawer), toolbar);
		 Fragment fragment = new DashBoard();
		 FragmentManager fragmentManager = getSupportFragmentManager();
		 fragmentManager.beginTransaction()
				 .add(R.id.container, fragment)
				 .commit();

	 }


}
