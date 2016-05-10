package com.grabpaisa.business;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.grabpaisa.R;
import com.grabpaisa.dashboard.App;


/**
 * Created by ashok on 3/11/15.
 */
public class BusinessRecyclerAdapter extends RecyclerView.Adapter<BusinessRecyclerAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<App> apps;
    Activity context;

    public BusinessRecyclerAdapter(Activity context, List<App> data) {
        inflater = LayoutInflater.from(context);
        this.apps = data;
        this.context = context;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_select_business, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtAppName.setText(apps.get(position).getName());
        holder.btnDownlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(apps.get(position).getDLink()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BussinessDetail.class);
//                context.startActivity(intent);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

//    public interface AdapterCallback{
//
//        public void onIntent(Intent intent);
//
//    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SparseBooleanArray selectedItems;
        TextView txtAppName;
        Button btnDownlaod;
        View itemView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            btnDownlaod = (Button) itemView.findViewById(R.id.btn_download);
            txtAppName = (TextView) itemView.findViewById(R.id.txt_app_name);
            this.itemView = itemView;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
