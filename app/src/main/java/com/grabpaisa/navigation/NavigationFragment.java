package com.grabpaisa.navigation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.grabpaisa.R;
import com.grabpaisa.dashboard.DashBoard;
import com.grabpaisa.login.AboutUs;
import com.grabpaisa.login.LikeUs;
import com.grabpaisa.login.MySingletone;
import com.grabpaisa.login.UserProfile;
import com.grabpaisa.navigation.RecyclerAdapterDrawer;
import com.grabpaisa.navigation.RowData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment implements RecyclerAdapterDrawer.clickListner {

     public static final String preFile = "textFile";
     public static final String userKey = "key";
     public static ActionBarDrawerToggle mDrawerToggle;
     public static DrawerLayout mDrawerLayout;
     boolean mUserLearnedDrawer;
     boolean mFromSavedInstance;
     View view;
     RecyclerView recyclerView;
     RecyclerAdapterDrawer adapter;
     public static final String PREFS_NAME = "Custom3rLoginPrefsFile";
     private SharedPreferences loginPreferences;
     public static SharedPreferences.Editor loginPrefsEditor;

     public NavigationFragment() {
	    // Required empty public constructor
     }


     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
	    // Inflate the layout for this fragment
	    final View view;
	    view = inflater.inflate(R.layout.fragment_navigation, container, false);


	    recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
	    adapter = new RecyclerAdapterDrawer(getContext(), setDrawer());
	    TextView userName = (TextView) view.findViewById(R.id.username);
	    adapter.setClickListner(this);
	    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
	    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
	    recyclerView.setLayoutManager(layoutManager);
	    recyclerView.setAdapter(adapter);
	    //sharedprefrance
	    loginPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	    loginPrefsEditor = loginPreferences.edit();

	    return view;
     }

     private List<RowData> setDrawer() {
	    List<RowData> list = new ArrayList<>();
	    String title[] = getResources().getStringArray(R.array.drawerItem);
	    int drawableId[] = {R.drawable.home_icon, R.drawable.about_icon, R.drawable.like_us_icon,R.drawable.my_profile_icon, R.drawable.logout_icon};
		//   .drawable.add_product_drawer, R.drawable.my_products, R.drawable.rate, R.drawable
		 //  .share, R.drawable.my_profile, R.drawable.about_black, R.drawable.request_brand,
		  // R.drawable.logout};
	    for (int i = 0; i < title.length; i++) {
		   RowData current = new RowData();
		   current.text = title[i];
		   current.id = drawableId[i];
		   list.add(current);

	    }
	    return list;
     }

     public static void openDrawer() {

     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), userKey, "false"));
	    if (savedInstanceState != null) {
		   mFromSavedInstance = true;
	    }
     }

     public void setup(int id, final DrawerLayout drawer, Toolbar toolbar) {
	    view = getActivity().findViewById(id);

	    mDrawerLayout = drawer;
	    Fragment fragment = null;
	    FragmentManager fragmentManager = getFragmentManager();
	    mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.drawer_open, R
		   .string.drawer_close) {
		   @Override
		   public void onDrawerOpened(View drawerView) {
			  hideSoftKeyboard(getActivity());

			  super.onDrawerOpened(drawerView);
			  if (!mUserLearnedDrawer) {
				 mUserLearnedDrawer = true;
				 savedInstances(getActivity(), userKey, mUserLearnedDrawer + "");
			  }
			  getActivity().invalidateOptionsMenu();
		   }

		   @Override
		   public void onDrawerClosed(View drawerView) {
			  super.onDrawerClosed(drawerView);
			  getActivity().invalidateOptionsMenu();

		   }

	    };
	    if (!mUserLearnedDrawer && !mFromSavedInstance) {
		   mDrawerLayout.openDrawer(view);
	    }
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

     }

     public static void savedInstances(Context context, String preferenceName, String preferenceValue) {
	    SharedPreferences sharePreference = context.getSharedPreferences(preFile, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharePreference.edit();
	    editor.putString(preferenceName, preferenceValue);
	    editor.apply();
     }

     public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
	    SharedPreferences sharePreference = context.getSharedPreferences(preFile, Context.MODE_PRIVATE);
	    return sharePreference.getString(preferenceName, defaultValue);
     }

     @Override
     public void itemClicked(View view, int position) {
	    try {

			if(RecyclerAdapterDrawer.selected_item == position) {
				mDrawerLayout.closeDrawers();
				return;
			}

			if(position != 4){
				RecyclerAdapterDrawer.selected_item = position;
			}
		   adapter.notifyDataSetChanged();
		   mDrawerLayout.closeDrawers();
		   Fragment fragment = null;
		   FragmentManager fragmentManager = getFragmentManager();
		   switch (position) {
			  case 0:
				 fragment = new DashBoard();



				 if (fragment != null && fragment.isVisible()) {


					// add your code here
					fragmentManager.beginTransaction()
					     .add(R.id.container, fragment).addToBackStack(null).commit();
				 } else {
					 //Toast.makeText(getActivity(), "new dashboard", Toast.LENGTH_SHORT).show();

					fragmentManager.beginTransaction()
					     .replace(R.id.container, fragment).addToBackStack(null).commit();
				 }

//			  if (fragment != null && fragment instanceof DashBoard) {
//				 //Fragment already exists
//				 fragmentManager.beginTransaction()
//					.replace(R.id.container, fragment).addToBackStack(null).commit();
//
//			  } else {
//				 //Add Fragment
//				 fragmentManager.beginTransaction()
//					.add(R.id.container, fragment).addToBackStack(null).commit();
//			  }
				 break;
			  case 1:
				 fragment = new AboutUs();
				 fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).addToBackStack(null).commit();
				 break;
			  case 2:
				 fragment = new LikeUs();
				 fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).addToBackStack(null).commit();
				 break;

			  case 3:
				 fragment = new UserProfile();
				 fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).addToBackStack(null).commit();
				 break;

			  case 4:
				  dialogeBoxe();
				 break;



		   }
	    } catch (Exception e) {

	    }

     }

     private void dialogeBoxe() {

//		 Toast.makeText(getActivity(), "logout", Toast.LENGTH_SHORT).show();

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder
		   .setTitle("All your Grab Paisa data will be removed from this device.")
		   .setMessage("Are you sure?")
		   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int which) {
				 //Yes button clicked, do something
				 Toast.makeText(getActivity(), "Logged out successfully",
						 Toast.LENGTH_SHORT).show();
				  SharedPreferences sharedPreferences = getActivity().getSharedPreferences("grabpaisa", Context.MODE_PRIVATE);
				  SharedPreferences.Editor editor = sharedPreferences.edit();
				  editor.putString("reg_id", null);
				  editor.commit();
				  MySingletone.getInstance().resetData();
				  getActivity().finish();

			  }
		   })
		   .setNegativeButton("No", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int which) {
				  dialog.dismiss();
				 //Yes button clicked, do something

//				 Toast.makeText(getActivity(), "Logged out successfully",
//					Toast.LENGTH_SHORT).show();
			  }
		   })                                    //Do nothing on no
		   .show();

     }

     public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

     }

}

