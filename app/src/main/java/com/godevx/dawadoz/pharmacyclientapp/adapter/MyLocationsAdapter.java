package com.godevx.dawadoz.pharmacyclientapp.adapter;


import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.godevx.dawadoz.R;
import com.godevx.dawadoz.pharmacyclientapp.model.Location;
import com.godevx.dawadoz.pharmacyclientapp.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.rmiri.skeleton.SkeletonGroup;


public class MyLocationsAdapter extends RecyclerView.Adapter<MyLocationsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Location> productlist = new ArrayList<>();
    private ProductListAdapter.OnItemClickListener clickListener;
    public MyAdapterListener onClickListener;

    public MyLocationsAdapter(final Context context, final ArrayList<Location> items, MyAdapterListener listener) {
        this.context = context;
        this.productlist = items;
        this.onClickListener = listener;
    }

    public interface MyAdapterListener {

        void editLocation(View v, int position);

        void deleteLocation(View v, int position);


    }

    public void setOnItemClickListener(
            final ProductListAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_list, parent, false);
        return new ViewHolder(view);
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView order_item_name;

        private TextView location_name;
        private Button btnDelete;
        private Button btnEdit;


        private CardView cardView;


        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardlist_item);
            location_name = itemView.findViewById(R.id.location_name);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);

        }


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.cardView.setPreventCornerOverlap(false);

        Location cardObj = productlist.get(position);

        holder.location_name.setText(cardObj.getLocationName());
        // holder.text_view_month.setText(cardObj.getMonth());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.editLocation(v, holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.deleteLocation(v, holder.getAdapterPosition());
            }
        });
        //Picasso.with(context).load(cardObj.getPharmacyLogoName()).into(holder.pharmacy_logo);

        //set photo by Picasso lib

    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }


}
