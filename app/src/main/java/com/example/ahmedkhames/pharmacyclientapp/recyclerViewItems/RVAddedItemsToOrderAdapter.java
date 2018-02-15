package com.example.ahmedkhames.pharmacyclientapp.recyclerViewItems;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ahmedkhames.pharmacyclientapp.R;

import java.util.List;

public class RVAddedItemsToOrderAdapter extends RecyclerView.Adapter<RVAddedItemsToOrderAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView personAge;
        TextView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
          //  cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (TextView)itemView.findViewById(R.id.person_photo);
        }
    }


    List<OrderItems> persons;

    public RVAddedItemsToOrderAdapter(List<OrderItems> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).itemName);
        personViewHolder.personAge.setText(persons.get(i).itemDetails);
        personViewHolder.personPhoto.setText(persons.get(i).itemType);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}
