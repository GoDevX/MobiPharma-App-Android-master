package com.vamer.Pharma.pharmacyclientapp.OrdersListing;


import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.adapter.ProductListAdapter;
import com.vamer.Pharma.pharmacyclientapp.model.Order;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.AdapterSkeleton;
import io.rmiri.skeleton.Master.IsCanSetAdapterListener;
import io.rmiri.skeleton.SkeletonGroup;


public class OrdersAdapter extends AdapterSkeleton<Order, OrdersAdapter.ViewHolder> {
    private ProductListAdapter.OnItemClickListener clickListener;

    public OrdersAdapter(final Context context, final ArrayList<Order> items, final RecyclerView recyclerView, final IsCanSetAdapterListener isCanSetAdapterListener) {
        this.context = context;
        this.items = items;
        this.isCanSetAdapterListener = isCanSetAdapterListener;
        measureHeightRecyclerViewAndItem(recyclerView, R.layout.item_order_list);// Set height

    }

    public void SetOnItemClickListener(
            final ProductListAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
        return new ViewHolder(view);
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView order_item_name;

        private TextView pharmacy_name;
        private TextView order_date;
        private TextView order_total_price;
        private TextView order_status;
        private ImageView pharmacy_logo;
        private SkeletonGroup skeletonGroup;


        private AppCompatImageButton addToParkingImgBtn;
        private AppCompatImageButton compareImgBtn;
        private RatingBar priceTv;
        private CardView cardView;


        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardlist_item);
            skeletonGroup = (SkeletonGroup) itemView.findViewById(R.id.skeletonGroup);
            order_item_name = itemView.findViewById(R.id.order_item_name);
            pharmacy_name = itemView.findViewById(R.id.pharmacy_name);
            order_date = itemView.findViewById(R.id.order_date);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            order_status = itemView.findViewById(R.id.order_status);
            pharmacy_logo = itemView.findViewById(R.id.pharmacy_logo);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cardView.setPreventCornerOverlap(false);

        if (skeletonConfig.isSkeletonIsOn()) {
            return;
        } else {
            holder.skeletonGroup.setShowSkeleton(false);
            holder.skeletonGroup.finishAnimation();
        }

        Order cardObj = items.get(position);

        holder.order_item_name.setText(cardObj.getOrderID());
        // holder.text_view_month.setText(cardObj.getMonth());
        holder.pharmacy_name.setText(cardObj.getBranchName());
        holder.order_date.setText(cardObj.getDateTimeStamp());

        holder.order_total_price.setText(cardObj.getTotalPrice());
        holder.order_status.setText(cardObj.getOrderStatus());

        holder.order_status.setText(cardObj.getOrderStatus());
        Picasso.with(context).load(cardObj.getPharmacyLogoName()).into(holder.pharmacy_logo);

        //set photo by Picasso lib

    }


}
