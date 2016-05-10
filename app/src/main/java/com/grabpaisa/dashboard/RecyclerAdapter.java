package com.grabpaisa.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.grabpaisa.R;
import com.grabpaisa.business.BussinessDetail;


/**
 * Created by ashok on 3/11/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<RowData> data = Collections.emptyList();
    Context context;

    RecyclerAdapter(Context context, List<RowData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_select_business, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.llList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BussinessDetail.class);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SparseBooleanArray selectedItems;
        TextView textView;
        TextView textViewRound;
        LinearLayout llList;

        public MyViewHolder(final View itemView) {
            super(itemView);
//            llList = (LinearLayout) itemView.findViewById(R.id.ll_list);
//            textView = (TextView) itemView.findViewById(R.id.textView);


        }

        @Override
        public void onClick(View v) {

        }
    }
}
