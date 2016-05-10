package com.grabpaisa.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

import com.grabpaisa.R;

/**
 * Created by ashok on 3/11/15.
 */
public class RecyclerAdapterDrawer extends RecyclerView.Adapter<RecyclerAdapterDrawer.MyViewHolder>{

    public static int selected_item = 0;
    private final LayoutInflater inflater;
    List<RowData> data= Collections.emptyList();
    Context context;
    private clickListner click=null;

    public RecyclerAdapterDrawer(Context context, List<RowData> data)
    {
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_navigation_drawer,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.textView.setText(data.get(position).text);
        holder.imageView.setImageResource(data.get(position).id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setClickListner(clickListner click)
    {
        this.click=click;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;
        public MyViewHolder(final View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.textView1);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.itemClicked(itemView,getAdapterPosition());

                }
            });
        }

    }
    public interface clickListner{
        void itemClicked(View view, int position);
    }
}
