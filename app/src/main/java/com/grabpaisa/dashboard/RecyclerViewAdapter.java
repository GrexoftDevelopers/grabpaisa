package com.grabpaisa.dashboard;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.grabpaisa.R;
import com.grabpaisa.business.SelectBusiness;

/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<RowData> itemList;

    DashBoard dashBoard;

    public RecyclerViewAdapter(DashBoard dashBoard, List<RowData> itemList) {
        this.itemList = itemList;
        this.dashBoard = dashBoard;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RecyclingTest", "onCreateViewHolder method is called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_row, null);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SelectBusiness();
                FragmentManager fragmentManager = dashBoard.getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
      /*  holder.textVieimwCategoryname.setText(itemList.get(position).getCategoryName());
        holder.imageViewCategory.setImageResource(itemList.get(position).categoryId);
        holder.imageViewIcon.setImageResource(itemList.get(position).iconPath);*/

        Log.d("RecyclingTest", "onBindViewHolder method is called");
      //  holder.textVieimwCategoryname.setText(itemList.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}
