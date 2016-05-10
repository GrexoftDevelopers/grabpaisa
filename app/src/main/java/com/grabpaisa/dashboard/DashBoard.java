package com.grabpaisa.dashboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.grabpaisa.R;

public class DashBoard extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    ArrayList<RowData> rowDatas = new ArrayList<RowData>();
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle("Dashboard");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dash_board, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Toast.makeText(getActivity(), "on create view", Toast.LENGTH_SHORT).show();
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        //Toast.makeText(getActivity(), "setting view pager", Toast.LENGTH_SHORT).show();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        if(fragments!= null && !fragments.isEmpty()){
            System.out.println("size of fragments list : " + fragments.size());
            for(Fragment fragment : fragments){
                System.out.println("fragment : " + fragment.toString());
                if(fragment instanceof DashBoardFragment || fragment instanceof ChallengeFragment || fragment instanceof Shopping){
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }
        adapter.addFragment(new DashBoardFragment(), "Task");
        ChallengeFragment challengeFragment = new ChallengeFragment();
        challengeFragment.setParent(this);
        adapter.addFragment(challengeFragment, "Dashboard");
        adapter.addFragment(new Shopping(), "Recharge");
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            //Toast.makeText(getActivity(), "get Item : " + position, Toast.LENGTH_SHORT).show();
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            System.out.println("get count : " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void showApps(){
        viewPager.setCurrentItem(0);
    }
}