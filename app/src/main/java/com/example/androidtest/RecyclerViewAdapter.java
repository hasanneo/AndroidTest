package com.example.androidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom adapter class for the recyclerview
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewRowHolder> {
    private Context mCtx;
    List<FeedItem> feedList;
    private OnItemClickListner itemClickListener;
    private final int CARD_CLICK=1;//to know that the calling activity is from this class when calling DataScreen
    /**
     * Card holder class
     */
    public static class ViewRowHolder extends RecyclerView.ViewHolder {
        public TextView id_text;
        public ImageView iconImage;
        public TextView rank_text;
        ViewRowHolder(View itemView, final RecyclerViewAdapter.OnItemClickListner listener) {
            super(itemView);
            id_text = itemView.findViewById(R.id.id_rank_text);
            iconImage = itemView.findViewById(R.id.icon);
            rank_text = itemView.findViewById(R.id.rank_name_txt);
            //item positon on card click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Constructor
     * @param mCtx Context for the activity
     * @param feedList List of the objects
     */
    public RecyclerViewAdapter(final Context mCtx, final List<FeedItem> feedList) {
        this.mCtx = mCtx;
        this.feedList = feedList;
        //Listener for a row click
        this.setOnItemClickListener(new OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mCtx, DataScreen.class);
                intent.putExtra("activity", CARD_CLICK);//form recyclerview click (1 as flag)
                intent.putExtra("idRank", position);
                intent.putExtra("nameRank", feedList.get(position).getNameRank());
                intent.putExtra("imageURL", feedList.get(position).getIconUrL());
                intent.putExtra("textColor", feedList.get(position).getColorText());
                intent.putExtra("bgColor", feedList.get(position).getColorBackground());
                mCtx.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Set layout for the recyclerview row
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_layout,
                parent, false);
        ViewRowHolder itemViewHolder = new ViewRowHolder(view, itemClickListener);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewRowHolder holder, int position) {
        //Bind values to the card holder
        FeedItem item = feedList.get(position);
        String rankName = item.getNameRank();
        int id = item.getIdRank();
        String txtColor = item.getColorText();
        String iconURL = item.getIconUrL();
        holder.id_text.setText(Integer.toString(id));
        holder.rank_text.setText(rankName);
        holder.rank_text.setTextColor(Color.parseColor(txtColor));
        Picasso.with(mCtx).load(iconURL).fit().centerInside().into(holder.iconImage);
    }


    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public interface OnItemClickListner {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListner listener) {
        itemClickListener = listener;
    }

}
