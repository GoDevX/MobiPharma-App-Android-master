/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.vamer.Pharma.pharmacyclientapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.FileObserver;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Money;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.ColorGenerator;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.view.customview.ItemTouchHelperAdapter;
import com.vamer.Pharma.pharmacyclientapp.view.customview.ItemTouchHelperViewHolder;
import com.vamer.Pharma.pharmacyclientapp.view.customview.OnStartDragListener;
import com.vamer.Pharma.pharmacyclientapp.view.customview.TextDrawable;
import com.vamer.Pharma.pharmacyclientapp.view.customview.TextDrawable.IBuilder;
import com.vamer.Pharma.pharmacyclientapp.fragment.MyCartFragment;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to
 * respond to move and dismiss events from a
 * {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Hitesh Sahu (hiteshsahu.com)
 */
public class ShoppingListAdapter extends
        RecyclerView.Adapter<ShoppingListAdapter.ItemViewHolder> implements
        ItemTouchHelperAdapter {
    private static final String LOG_TAG = "FileViewerAdapter";

    private static final String ARG_POSITION = "position";
    private static OnItemClickListener clickListener;
    private final OnStartDragListener mDragStartListener;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private String ImageUrl;
    private List<Product> productList = new ArrayList<Product>();
    private Context context;

    public ShoppingListAdapter(Context context,
                               OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

        this.context = context;

        productList = CenterRepository.getCenterRepository().getListOfProductsInShoppingList();
        observer.startWatching();

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_product_list, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {


        if (productList.get(position).getQuantity().equals("")) {
            if (productList.get(position).getItemName().equals("Record")) {
                Glide.with(context).load(ImageUrl).placeholder(drawable)
                        .error(drawable).animate(R.anim.base_slide_right_in)
                        .centerCrop().into(holder.imagView);
                holder.imagView.setImageResource(R.drawable.ic_mic_white_36dp);
                holder.imagView.setBackgroundColor(context.getResources().getColor(R.color.primary));
            }

            holder.itemName.setText(productList.get(position).getItemName());
            holder.itemDesc.setText(productList.get(position).getItemShortDesc());
            holder.removeItem.setVisibility(View.INVISIBLE);
            holder.addItem.setVisibility(View.INVISIBLE);
            holder.itemCost.setVisibility(View.INVISIBLE);
            holder.availability.setVisibility(View.INVISIBLE);
            holder.itemCost.setVisibility(View.INVISIBLE);
            holder.quanitity.setVisibility(View.INVISIBLE);
            Uri uri = Uri.fromFile(new File(productList.get(position).getImageURL()));
            Glide.with(context).load(uri).placeholder(drawable)
                    .error(drawable).animate(R.anim.base_slide_right_in)
                    .centerCrop().into(holder.imagView);
        } else {
            holder.itemName.setText(productList.get(position).getItemName());

            holder.itemDesc.setText(productList.get(position).getItemShortDesc());

            String sellCostString = Money.rupees(
                    BigDecimal.valueOf(Long.valueOf(productList.get(position)
                            .getSellMRP()))).toString()
                    + "  ";

            String buyMRP = Money.rupees(
                    BigDecimal.valueOf(Long.valueOf(productList.get(position)
                            .getMRP()))).toString();

            String costString = sellCostString + buyMRP;

            holder.itemCost.setText(costString, BufferType.SPANNABLE);
            CenterRepository
                    .getCenterRepository()
                    .getListOfProductsInShoppingList()
                    .get(position)
                    .setQuantity(
                            String.valueOf(
                                    Integer.valueOf(CenterRepository
                                            .getCenterRepository().getListOfProductsInShoppingList()
                                            .get(position).getQuantity())));
            Spannable spannable = (Spannable) holder.itemCost.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                    .endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(String.valueOf(productList
                    .get(position).getItemName().charAt(0)), mColorGenerator
                    .getColor(productList.get(position).getItemName()));

            ImageUrl = productList.get(position).getImageURL();

            holder.quanitity.setText(CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(position).getQuantity());

            Glide.with(context).load(ImageUrl).placeholder(drawable)
                    .error(drawable).animate(R.anim.base_slide_right_in)
                    .centerCrop().into(holder.imagView);

            // Start a drag whenever the handle view it touched
            holder.imagView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        //mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });

            holder.addItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CenterRepository
                            .getCenterRepository()
                            .getListOfProductsInShoppingList()
                            .get(position)
                            .setQuantity(
                                    String.valueOf(
                                            Integer.valueOf(CenterRepository
                                                    .getCenterRepository().getListOfProductsInShoppingList()
                                                    .get(position).getQuantity()) + 1));

                    holder.quanitity.setText(CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().get(position).getQuantity());
                    Utils.vibrate(context);
                    ((HomeActivity) context).updateCheckOutAmount(
                            BigDecimal.valueOf(Long.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(position).getSellMRP())), true);

                }
            });

            holder.removeItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Integer.valueOf(CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().get(position).getQuantity()) > 1) {
                        CenterRepository
                                .getCenterRepository()
                                .getListOfProductsInShoppingList()
                                .get(position)
                                .setQuantity(
                                        String.valueOf(
                                                Integer.valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getListOfProductsInShoppingList().get(position)
                                                        .getQuantity()) - 1));

                        holder.quanitity.setText(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(position).getQuantity());

                        ((HomeActivity) context).updateCheckOutAmount(
                                BigDecimal.valueOf(Long.valueOf(CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .get(position).getSellMRP())), false);

                        Utils.vibrate(context);
                    } else {/*if (Integer.valueOf(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(position).getQuantity()) == 1) {
                            CenterRepository.getCenterRepository().getListOfProductsInShoppingList().remove(position);
                           notifyDataSetChanged();
                    ((HomeActivity) context).updateItemCount(false);
                    if(position !=0){
                        ((HomeActivity) context).updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(position).getSellMRP())),false);
                    }
                   else{*/
                       /* ((HomeActivity) context).updateCheckOutAmount(BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(position).getSellMRP())),false);*/
                    }


                    /*if (Integer.valueOf(((HomeActivity) context).getItemCount()) == 0) {

                        MyCartFragment.updateMyCartFragment(false);

                    }*/


                }


            });
        }
        if (Integer.valueOf(((HomeActivity) context)
                .getItemCount()) == 0) {

            MyCartFragment.updateMyCartFragment(false);

        }

        // Utils.vibrate(context);
    }

    @Override
    public void onItemDismiss(int position) {
        if (productList.get(position).getQuantity().equals("")) {
            ((HomeActivity) context).updateItemCount(false);
            CenterRepository.getCenterRepository().getListOfProductsInShoppingList().remove(position);

            if (Integer.valueOf(((HomeActivity) context).getItemCount()) == 0) {

                MyCartFragment.updateMyCartFragment(false);
                notifyItemRemoved(position);

            }
            Utils.vibrate(context);

            // productList.remove(position);
            notifyItemRemoved(position);
        } else {
            ((HomeActivity) context).updateItemCount(false);
            ((HomeActivity) context).updateCheckOutAmount(
                    BigDecimal.valueOf(Long.valueOf(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList().get(position)
                            .getSellMRP()) * Long.valueOf(CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList().get(position)
                            .getQuantity())), false);

            CenterRepository.getCenterRepository().getListOfProductsInShoppingList().remove(position);

            if (Integer.valueOf(((HomeActivity) context).getItemCount()) == 0) {

                MyCartFragment.updateMyCartFragment(false);

            }

            Utils.vibrate(context);

            // productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(productList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return productList.size();

    }


    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    /**
     * Simple example of a view holder that implements
     * {@link ItemTouchHelperViewHolder} and has a "handle" view that initiates
     * a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener {

        // public final ImageView handleView;

        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            // handleView = (ImageView) itemView.findViewById(R.id.handle);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemCost = (TextView) itemView.findViewById(R.id.item_price);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);

            quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            addItem = ((TextView) itemView.findViewById(R.id.add_item));

            removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(v, getPosition());
        }


    }

    FileObserver observer =
            new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()
                    + "/SoundRecorder") {
                // set up a file observer to watch this directory on sd card
                @Override
                public void onEvent(int event, String file) {
                    if (event == FileObserver.DELETE) {
                        // user deletes a recording file out of the app

                        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                                + "/SoundRecorder" + file + "]";

                        Log.d(LOG_TAG, "File deleted ["
                                + android.os.Environment.getExternalStorageDirectory().toString()
                                + "/SoundRecorder" + file + "]");

                        // remove file from database and recyclerview
                        //  mFileViewerAdapter.removeOutOfApp(filePath);
                    }
                }
            };
}
