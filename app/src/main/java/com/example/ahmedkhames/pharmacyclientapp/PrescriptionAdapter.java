package com.example.ahmedkhames.pharmacyclientapp;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ahmedkhames.pharmacyclientapp.model.ImagePrescription;

import java.io.File;
import java.util.ArrayList;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {
    public ArrayList<ImagePrescription> itemInfoArrayList = new ArrayList<ImagePrescription>();
    public MyClickListener myClickListener;
    Activity activity;

    public PrescriptionAdapter(Activity a, ArrayList<ImagePrescription> itemInfoArrayList) {
        this.activity = a;
        this.itemInfoArrayList = itemInfoArrayList;
       // setOnItemClickListener(myClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // ImagePrescription image = get(position);
        holder.imgvItem.setImageURI(Uri.fromFile(new File(itemInfoArrayList.get(position).getPath())));
        Uri photoUri = Uri.fromFile( new File( itemInfoArrayList.get(position).getPath()));

        Glide.with(activity).load( photoUri).into( holder.imgvItem);


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         ImageView imgvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            imgvItem = (ImageView) itemView.findViewById(R.id.item_img_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          //setOnItemClickListener(myClickListener);
            myClickListener.onItemClick(getAdapterPosition(),v);
        }
    }

    @Override
    public int getItemCount() {
        return itemInfoArrayList.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }


}
