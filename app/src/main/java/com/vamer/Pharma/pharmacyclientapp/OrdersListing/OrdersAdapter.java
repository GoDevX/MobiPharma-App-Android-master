package com.vamer.Pharma.pharmacyclientapp.OrdersListing;


import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.model.Order;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.AdapterSkeleton;
import io.rmiri.skeleton.Master.IsCanSetAdapterListener;
import io.rmiri.skeleton.SkeletonGroup;


public class OrdersAdapter extends AdapterSkeleton<Order,OrdersAdapter.ViewHolder> {

    public OrdersAdapter(final Context context, final ArrayList<Order> items, final RecyclerView recyclerView, final IsCanSetAdapterListener isCanSetAdapterListener) {
        this.context = context;
        this.items = items;
        this.isCanSetAdapterListener = isCanSetAdapterListener;
        measureHeightRecyclerViewAndItem(recyclerView, R.layout.order_list_content);// Set height

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private SkeletonGroup skeletonGroup;
        private AppCompatImageView photoACImgV;
        private TextView text_view_date;
        private TextView text_view_month;
        private TextView text_view_order_id;
        private TextView text_view_price;
        private TextView text_view_status;


        private AppCompatImageButton addToParkingImgBtn;
        private AppCompatImageButton compareImgBtn;
        private RatingBar priceTv;


        ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            skeletonGroup = (SkeletonGroup) itemView.findViewById(R.id.skeletonGroup);
            photoACImgV = (AppCompatImageView) itemView.findViewById(R.id.image_view_status);
            text_view_date = (TextView) itemView.findViewById(R.id.text_view_date);
           // text_view_month = (TextView) itemView.findViewById(R.id.text_view_month);
            text_view_order_id = (TextView) itemView.findViewById(R.id.text_view_order_id);
            text_view_status = (TextView) itemView.findViewById(R.id.text_view_status);
            text_view_price = (TextView) itemView.findViewById(R.id.text_view_price);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cardView.setPreventCornerOverlap(false);

//        holder.skeletonGroup.setPosition(position);//just for debug log

       /* if (skeletonConfig.isSkeletonIsOn()) {
            return;
        } else {
            holder.skeletonGroup.setShowSkeleton(false);
            holder.skeletonGroup.finishAnimation();
        }*/

        //set data in view
        final Order cardObj = items.get(position);

        holder.text_view_date.setText(cardObj.getDate());
       // holder.text_view_month.setText(cardObj.getMonth());
        holder.text_view_order_id.setText(cardObj.getOrderId());

        if (cardObj.getStatus().equals("0")) {
            holder.text_view_status.setText("Pending");
            holder.text_view_status.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            holder.text_view_status.setText("Confirmed");
            holder.text_view_status.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            holder.photoACImgV.setImageResource((R.drawable.tag_confirmed));

        }

        //set photo by Picasso lib

    }



}
