package com.vamer.Pharma.pharmacyclientapp.adapter;


import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;


import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.model.Pharmacy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.AdapterSkeleton;
import io.rmiri.skeleton.Master.IsCanSetAdapterListener;
import io.rmiri.skeleton.SkeletonGroup;


public class PharmacyAdapter extends AdapterSkeleton<Pharmacy, PharmacyAdapter.ViewHolder> {


    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void btnOrderOnClick(View v, int position);

    }

    public PharmacyAdapter(final Context context, final ArrayList<Pharmacy> items, final RecyclerView recyclerView, final IsCanSetAdapterListener isCanSetAdapterListener, MyAdapterListener listener) {
        this.context = context;
        this.items = items;
        this.isCanSetAdapterListener = isCanSetAdapterListener;
        measureHeightRecyclerViewAndItem(recyclerView, R.layout.pharmacy_item);// Set height
        onClickListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private SkeletonGroup skeletonGroup;
        private AppCompatImageView photoACImgV;
        private Button btnOrder;
        private TextView titleTv;
        private TextView descriptionTv;
        private AppCompatImageButton addToParkingImgBtn;
        private AppCompatImageButton compareImgBtn;
        private RatingBar priceTv;


        ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            skeletonGroup = (SkeletonGroup) itemView.findViewById(R.id.skeletonGroup);
            photoACImgV = (AppCompatImageView) itemView.findViewById(R.id.photoACImgV);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            descriptionTv = (TextView) itemView.findViewById(R.id.descriptionTv);
            addToParkingImgBtn = (AppCompatImageButton) itemView.findViewById(R.id.addToParkingImgBtn);
            compareImgBtn = (AppCompatImageButton) itemView.findViewById(R.id.compareImgBtn);
            priceTv = (RatingBar) itemView.findViewById(R.id.priceTv);
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.btnOrderOnClick(v, getAdapterPosition());
                }
            });



        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cardView.setPreventCornerOverlap(false);

//        holder.skeletonGroup.setPosition(position);//just for debug log

        if (skeletonConfig.isSkeletonIsOn()) {
            return;
        } else {
            holder.skeletonGroup.setShowSkeleton(false);
            holder.skeletonGroup.finishAnimation();
        }

        //set data in view
        Pharmacy cardObj = items.get(position);

        holder.titleTv.setText(cardObj.getPharmacyName());
        holder.descriptionTv.setText(cardObj.getPharmacyDesc());
        holder.priceTv.setRating(Float.parseFloat(cardObj.getPharmacyRate()));


        //set photo by Picasso lib
        Picasso.with(context).load(cardObj.getPharmacyLogo()).into(holder.photoACImgV);

    }


}
