package com.vamer.Pharma.pharmacyclientapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.model.Location;

import java.util.List;

public class SingleCheckAdapter extends RecyclerView.Adapter<SingleCheckAdapter.SingleCheckViewHolder> {

    private int mSelectedItem = -1;
    private List<Location> mSingleCheckList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;

    public SingleCheckAdapter(Context context, List<Location> listItems) {
        mContext = context;
        mSingleCheckList = listItems;
    }

    @Override
    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.location_item_single_check, viewGroup, false);
        return new SingleCheckViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(SingleCheckViewHolder viewHolder, final int position) {
        Location item = mSingleCheckList.get(position);
        try {
            viewHolder.setDateToView(item, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mSingleCheckList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void onItemHolderClick(SingleCheckViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    class SingleCheckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SingleCheckAdapter mAdapter;
        private me.omidh.liquidradiobutton.LiquidRadioButton mRadio;
        private TextView mText;

        public SingleCheckViewHolder(View itemView, final SingleCheckAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            mText = (TextView) itemView.findViewById(R.id.location_name);
            mRadio =  itemView.findViewById(R.id.radio);
            itemView.setOnClickListener(this);
            mRadio.setOnClickListener(this);
        }

        public void setDateToView(Location item, int position) throws Exception {
            mRadio.setChecked(position == mSelectedItem);
            //mRadio.setText(item.getLocationName());
            mText.setText(item.getLocationName());
        }

        @Override
        public void onClick(View v) {
            mSelectedItem = getAdapterPosition();
            notifyItemRangeChanged(0, mSingleCheckList.size());
            mAdapter.onItemHolderClick(SingleCheckViewHolder.this);
        }
    }

}
