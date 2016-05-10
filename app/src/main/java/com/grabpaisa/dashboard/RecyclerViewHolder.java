package com.grabpaisa.dashboard;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabpaisa.R;


/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView textVieimwCategoryname;
    public ImageView imageViewCategory;
    public ImageView imageViewIcon;
    CardView cardView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        textVieimwCategoryname = (TextView)itemView.findViewById(R.id.textViewCategoryName);
        imageViewCategory = (ImageView)itemView.findViewById(R.id.imageViewCategory);
        imageViewIcon = (ImageView)itemView.findViewById(R.id.img_icon);

        cardView =(CardView)itemView.findViewById(R.id.card_view);
    }


}