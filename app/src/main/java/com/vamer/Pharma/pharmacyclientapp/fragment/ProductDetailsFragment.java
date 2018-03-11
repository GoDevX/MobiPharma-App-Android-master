/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */
package com.vamer.Pharma.pharmacyclientapp.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Money;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.ColorGenerator;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.util.Utils.AnimationType;
import com.vamer.Pharma.pharmacyclientapp.adapter.SimilarProductsPagerAdapter;
import com.vamer.Pharma.pharmacyclientapp.view.customview.ClickableViewPager;
import com.vamer.Pharma.pharmacyclientapp.view.customview.ClickableViewPager.OnItemClickListener;
import com.vamer.Pharma.pharmacyclientapp.view.customview.LabelView;
import com.vamer.Pharma.pharmacyclientapp.view.customview.TextDrawable;
import com.vamer.Pharma.pharmacyclientapp.view.customview.TextDrawable.IBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigDecimal;

// TODO: Auto-generated Javadoc

/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
@SuppressLint("ValidFragment")
public class ProductDetailsFragment extends Fragment {

    private int productListNumber;
    private ImageView itemImage;
    private TextView itemSellPrice;
    private TextView itemName;
    private TextView quanitity;
    private TextView itemdescription;
    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String subcategoryKey;
    private boolean isFromCart;
    private ClickableViewPager similarProductsPager;
    private ClickableViewPager topSellingPager;
    private Toolbar mToolbar;
    LinearLayout linearPlus_Minus;
    Product p;


    public ProductDetailsFragment(Product m, String subcategoryKey, int productNumber,
                                  boolean isFromCart) {
        p = new Product();
        this.subcategoryKey = subcategoryKey;
        this.productListNumber = productNumber;
        this.isFromCart = isFromCart;
        this.p = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_product_detail,
                container, false);

        linearPlus_Minus = rootView.findViewById(R.id.linearPlus_Minus);

        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((HomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((HomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            // mToolbar.setNavigationIcon(R.drawable.icon_back);

        }

        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*  if (isFromCart) {

                     *//*   Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ((HomeActivity) (getActivity())),
                                AnimationType.SLIDE_UP);*//*

                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new MyCartFragment(), getActivity(), Utils.SHOPPING_LIST_TAG,
                                Utils.AnimationType.SLIDE_UP);

                    } else {

                      *//*  Utils.switchContent(R.id.frag_container,
                                Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                ((HomeActivity) (getActivity())),
                                AnimationType.SLIDE_RIGHT);*//*
                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new ProductOverviewFragment(), getActivity(),   Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                Utils.AnimationType.SLIDE_RIGHT);
                    }*/
                getActivity().onBackPressed();
            }
        });

        ((HomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        similarProductsPager = (ClickableViewPager) rootView
                .findViewById(R.id.similar_products_pager);

        topSellingPager = (ClickableViewPager) rootView
                .findViewById(R.id.top_selleing_pager);

        itemSellPrice = ((TextView) rootView
                .findViewById(R.id.category_discount));

        quanitity = ((TextView) rootView.findViewById(R.id.iteam_amount));

        itemName = ((TextView) rootView.findViewById(R.id.product_name));

        itemdescription = ((TextView) rootView
                .findViewById(R.id.product_description));

        itemImage = (ImageView) rootView.findViewById(R.id.product_image);

        fillProductData();

        rootView.findViewById(R.id.add_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (isFromCart) {

                            //Update Quantity on shopping List
                            CenterRepository
                                    .getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .get(productListNumber)
                                    .setQuantity(
                                            String.valueOf(
                                                    Integer.valueOf(CenterRepository
                                                            .getCenterRepository()
                                                            .getListOfProductsInShoppingList()
                                                            .get(productListNumber)
                                                            .getQuantity()) + 1));


                            //Update Ui
                            quanitity.setText(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity());

                            Utils.vibrate(getActivity());

                            //Update checkout amount on screen
                            ((HomeActivity) getActivity()).updateCheckOutAmount(
                                    BigDecimal.valueOf(Double
                                            .valueOf(CenterRepository
                                                    .getCenterRepository()
                                                    .getListOfProductsInShoppingList()
                                                    .get(productListNumber)
                                                    .getSellMRP())), true);

                        } else {

                            // current object
                            Product tempObj = p;

                            if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList().contains(tempObj)) {

                                // get position of current item in shopping list
                                int indexOfTempInShopingList = CenterRepository.getCenterRepository().getListOfProductsInShoppingList().indexOf(tempObj);

                                // increase quantity of current item in shopping
                                // list
                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {

                                    ((HomeActivity) getContext())
                                            .updateItemCount(true);

                                }

                                // update quanity in shopping list
                                CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(indexOfTempInShopingList)
                                        .setQuantity(
                                                String.valueOf(Integer
                                                        .valueOf(tempObj
                                                                .getQuantity()) + 1));

                                // update checkout amount
                                ((HomeActivity) getContext()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Double.valueOf(tempObj.getSellMRP())), true);

                                // update current item quanitity
                                quanitity.setText(tempObj.getQuantity());

                            } else {

                                ((HomeActivity) getContext())
                                        .updateItemCount(true);

                                tempObj.setQuantity(String.valueOf(1));

                                quanitity.setText(tempObj.getQuantity());

                                CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList().add(tempObj);

                                ((HomeActivity) getContext()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Double
                                                .valueOf(tempObj
                                                        .getSellMRP())), true);

                            }

                            Utils.vibrate(getContext());

                        }
                    }

                });

        rootView.findViewById(R.id.remove_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (isFromCart)

                        {

                            if (Integer.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity()) > 2) {

                                CenterRepository
                                        .getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .get(productListNumber)
                                        .setQuantity(
                                                String.valueOf(

                                                        Integer.valueOf(CenterRepository
                                                                .getCenterRepository()
                                                                .getListOfProductsInShoppingList()
                                                                .get(productListNumber)
                                                                .getQuantity()) - 1));

                                quanitity.setText(CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .get(productListNumber).getQuantity());

                                ((HomeActivity) getActivity()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Double
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getListOfProductsInShoppingList()
                                                        .get(productListNumber)
                                                        .getSellMRP())), false);

                                Utils.vibrate(getActivity());
                            } else if (Integer.valueOf(CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .get(productListNumber).getQuantity()) == 1) {
                                ((HomeActivity) getActivity())
                                        .updateItemCount(false);

                                ((HomeActivity) getActivity()).updateCheckOutAmount(
                                        BigDecimal.valueOf(Double
                                                .valueOf(CenterRepository
                                                        .getCenterRepository()
                                                        .getListOfProductsInShoppingList()
                                                        .get(productListNumber)
                                                        .getSellMRP())), false);

                                CenterRepository.getCenterRepository()
                                        .getListOfProductsInShoppingList()
                                        .remove(productListNumber);

                                if (Integer.valueOf(((HomeActivity) getActivity())
                                        .getItemCount()) == 0) {
                                    MyCartFragment.updateMyCartFragment(false);
                                }
                                Utils.vibrate(getActivity());
                            }
                        } else {
                            Product tempObj = p;
                            if (CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().contains(tempObj)) {
                                int indexOfTempInShopingList = CenterRepository
                                        .getCenterRepository().getListOfProductsInShoppingList()
                                        .indexOf(tempObj);
                                if (Integer.valueOf(tempObj.getQuantity()) != 0) {
                                    CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(indexOfTempInShopingList)
                                            .setQuantity(
                                                    String.valueOf(Integer.valueOf(tempObj
                                                            .getQuantity()) - 1));

                                    ((HomeActivity) getContext()).updateCheckOutAmount(
                                            BigDecimal.valueOf(Double.valueOf(p.getSellMRP())),
                                            false);

                                    quanitity.setText(CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(indexOfTempInShopingList)
                                            .getQuantity());

                                    Utils.vibrate(getContext());

                                    if (Integer.valueOf(CenterRepository
                                            .getCenterRepository()
                                            .getListOfProductsInShoppingList()
                                            .get(indexOfTempInShopingList)
                                            .getQuantity()) == 0) {

                                        CenterRepository
                                                .getCenterRepository()
                                                .getListOfProductsInShoppingList()
                                                .remove(indexOfTempInShopingList);

                                        ((HomeActivity) getContext()).updateItemCount(false);

                                    }

                                }

                            } else {

                            }

                        }

                    }

                });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (isFromCart) {

                     /*   Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ((HomeActivity) (getActivity())),
                                AnimationType.SLIDE_UP);*/

                 /*       Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new MyCartFragment(), getActivity(), null,
                                Utils.AnimationType.SLIDE_UP);*/
                        getActivity().onBackPressed();

                    } else {

                      /*  Utils.switchContent(R.id.frag_container,
                                Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG,
                                ((HomeActivity) (getActivity())),
                                AnimationType.SLIDE_RIGHT);*/
                       /* Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new ProductOverviewFragment(subcategoryKey) , getActivity(), null,
                                Utils.AnimationType.SLIDE_RIGHT);*/
                        getActivity().onBackPressed();

                    }

                }
                return true;
            }
        });

        if (isFromCart) {

            similarProductsPager.setVisibility(View.GONE);

            topSellingPager.setVisibility(View.GONE);

        } else {
            showRecomondation();
        }

        return rootView;
    }


    private void showRecomondation() {

        SimilarProductsPagerAdapter mCustomPagerAdapter = new SimilarProductsPagerAdapter(
                getActivity(), subcategoryKey);

        similarProductsPager.setAdapter(mCustomPagerAdapter);

        similarProductsPager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                productListNumber = position;

                fillProductData();

                Utils.vibrate(getActivity());

            }
        });

        topSellingPager.setAdapter(mCustomPagerAdapter);

        topSellingPager.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                productListNumber = position;

                fillProductData();

                Utils.vibrate(getActivity());

            }
        });
    }

    public void fillProductData() {

        if (!isFromCart) {


            //Fetch and display item from Gloabl Data Model
            itemName.setText(p.getItemName());
          /*  itemName.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getItemName());*/

        /*    quanitity.setText(CenterRepository.getCenterRepository()
                    .getMapOfProductsInCategory().get(subcategoryKey).get(productListNumber)
                    .getQuantity());*/
            quanitity.setText(p.getQuantity());

            itemdescription.setText(p.getScientificName());

            String sellCostString =
                    BigDecimal.valueOf(Double.valueOf(p.getSellMRP())).toString()
                            + "  ";
/*

            String buyMRP =
                    BigDecimal.valueOf(Long.valueOf(CenterRepository
                            .getCenterRepository().getMapOfProductsInCategory()
                            .get(subcategoryKey).get(productListNumber)
                            .getMRP())).toString();
*/

            String costString = sellCostString /*+ buyMRP*/;

            itemSellPrice.setText(costString, BufferType.SPANNABLE);

            Spannable spannable = (Spannable) itemSellPrice.getText();

            spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                    costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDrawableBuilder = TextDrawable.builder().beginConfig()
                    .withBorder(4).endConfig().roundRect(10);

            drawable = mDrawableBuilder.build(
                    String.valueOf(p.getItemName().charAt(0)),
                    mColorGenerator.getColor(p.getItemName()));

           /* Picasso.with(getActivity())
                    .load(p.getFilePath()).placeholder(drawable)
                    .error(drawable).fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(itemImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed

                            Picasso.with(getActivity())
                                    .load(CenterRepository.getCenterRepository()
                                            .getMapOfProductsInCategory()
                                            .get(subcategoryKey)
                                            .get(productListNumber)
                                            .getFilePath())
                                    .placeholder(drawable).error(drawable)
                                    .fit().centerCrop().into(itemImage);
                        }
                    });*/

            LabelView label = new LabelView(getActivity());

           /* label.setText(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey).get(productListNumber).getDiscount());*/
            label.setBackgroundColor(0xffE91E63);

            label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);
        } else {

            if (CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList().get(productListNumber).getQuantity() == "") {
                itemName.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getItemName());
                itemdescription.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getItemDetail());
                Uri uri = Uri.fromFile(new File(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getFilePath()));

                Glide.with(getActivity()).load(uri).placeholder(drawable)
                        .error(drawable).animate(R.anim.base_slide_right_in)
                        .into(itemImage);
                linearPlus_Minus.setVisibility(View.INVISIBLE);

            }
            //Fetch and display products from Shopping list
            else {
                itemName.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getItemName());

                quanitity.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getQuantity());

                itemdescription.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getItemDetail());

                String sellCostString =
                        BigDecimal.valueOf(Double.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(productListNumber).getSellMRP())).toString()
                                + "  ";

           /*     String buyMRP =
                        BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(productListNumber).getMRP())).toString();*/

                String costString = sellCostString /*+ buyMRP*/;

                itemSellPrice.setText(costString, BufferType.SPANNABLE);

                Spannable spannable = (Spannable) itemSellPrice.getText();

                spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                        costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                mDrawableBuilder = TextDrawable.builder().beginConfig()
                        .withBorder(4).endConfig().roundRect(10);

                drawable = mDrawableBuilder.build(
                        String.valueOf(CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().get(productListNumber)
                                .getItemName().charAt(0)),
                        mColorGenerator.getColor(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(productListNumber).getItemName()));
//todO loading image in case of from cart fragment to details fragment
               /* Picasso.with(getActivity())
                        .load(CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().get(productListNumber)
                                .getFilePath()).placeholder(drawable)
                        .error(drawable).fit().centerCrop()
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(itemImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                // Try again online if cache failed

                                Picasso.with(getActivity())
                                        .load(CenterRepository.getCenterRepository()
                                                .getListOfProductsInShoppingList()
                                                .get(productListNumber)
                                                .getFilePath())
                                        .placeholder(drawable).error(drawable)
                                        .fit().centerCrop().into(itemImage);
                            }
                        });*/

                LabelView label = new LabelView(getActivity());

                label.setText(CenterRepository.getCenterRepository()
                        .getListOfProductsInShoppingList().get(productListNumber).getDiscount());
                label.setBackgroundColor(0xffE91E63);

                label.setTargetView(itemImage, 10, LabelView.Gravity.RIGHT_TOP);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
    }
}
