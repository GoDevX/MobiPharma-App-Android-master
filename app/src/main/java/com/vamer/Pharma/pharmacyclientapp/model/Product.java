/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

/**
 *
 */
package com.vamer.Pharma.pharmacyclientapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Class Product used as model for Products.
 *
 * @author Hitesh
 */
public class Product implements Parcelable {




    public void setOrderItemType(String orderItemType) {
        OrderItemType = orderItemType;
    }

    public String getOrderItemType() {
        return OrderItemType;
    }

    /**
     * The item short desc.
     */

    private String OrderItemType = "";

    private String description = "";

    /**
     * The item detail.
     */
    private String longDescription = "";

    /**
     * The mrp.
     */
    private String mrp;

    /**
     * The discount.
     */
    private String discount;

    /**
     * The sell mrp.
     */
    private String salePrice;

    /**
     * The quantity.
     */
    private String ItemQuantity;

    /**
     * The image url.
     */
    private String FilePath = "";

    /**
     * The item name.
     */
    private String productName = "";

    private String ProductID = "";

    /**
     * @param itemName
     * @param itemShortDesc
     * @param itemDetail
     * @param MRP
     * @param discount
     * @param sellMRP
     * @param quantity
     * @param imageURL
     */
    public Product(String OrderItemType, String itemName, String itemShortDesc, String itemDetail,
                   String MRP, String discount, String sellMRP, String quantity,
                   String imageURL, String orderId) {
        this.OrderItemType = OrderItemType;
        this.productName = itemName;
        this.description = itemShortDesc;
        this.longDescription = itemDetail;
        this.mrp = MRP;
        this.discount = discount;
        this.salePrice = sellMRP;
        this.ItemQuantity = quantity;
        this.FilePath = imageURL;
        this.ProductID = orderId;
    }

    public Product(Parcel in) {
          //  mName = in.readString();
          // mFilePath = in.readString();
          //  mId = in.readInt();
          // mLength = in.readInt();
          // mTime = in.readLong();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    public String getProductId() {
        return ProductID;
    }

    public void setProductId(String productId) {
        this.ProductID = productId;
    }

    public String getItemName() {
        return productName;
    }

    public void setItemName(String itemName) {
        this.productName = itemName;
    }

    public String getItemShortDesc() {
        return description;
    }

    public void setItemShortDesc(String itemShortDesc) {
        this.description = itemShortDesc;
    }

    public String getItemDetail() {
        return longDescription;
    }

    public void setItemDetail(String itemDetail) {
        this.longDescription = itemDetail;
    }

    public String getMRP() {
        return this.mrp;
    }

    public void setMRP(String MRP) {
        this.mrp = MRP;
    }

    public String getDiscount() {
        return discount + "%";
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountNumeric() {
        return discount;
    }

    public String getSellMRP() {
        return salePrice;
    }

    public void setSellMRP(String sellMRP) {
        this.salePrice = sellMRP;
    }

    public String getQuantity() {
        return ItemQuantity;
    }

    public void setQuantity(String quantity) {
        this.ItemQuantity = quantity;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setImageURL(String imageURL) {
        this.FilePath = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //  dest.writeInt(mId);
        // dest.writeInt(mLength);
        // dest.writeLong(mTime);
        //dest.writeString(mFilePath);
        //  dest.writeString(mName);
    }
}
