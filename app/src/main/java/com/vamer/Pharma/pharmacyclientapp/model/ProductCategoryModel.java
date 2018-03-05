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

/**
 * @author Hitesh
 */
public class ProductCategoryModel {

    private String categoryName;
    private String categoryDescription;
    private String categoryDiscount;
    private String categoryImageUrl;

    public ProductCategoryModel(String productCategoryName, String productCategoryDescription,
                                String productCategoryDiscount, String productCategoryUrl) {
        super();
        this.categoryName = productCategoryName;
        this.categoryDescription = productCategoryDescription;
        this.categoryDiscount = productCategoryDiscount;
        this.categoryImageUrl = productCategoryUrl;
    }
    public String getProductCategoryName() {
        return categoryName;
    }
    public void setProductCategoryName(String idproductcategory) {
        this.categoryName = idproductcategory;
    }
    /**
     * @return the productDescription
     */
    public String getProductCategoryDescription() {
        return categoryDescription;
    }
    /**
     * @param productDescription the productDescription to set
     */
    public void setProductCategoryDescription(String productDescription) {
        this.categoryDescription = productDescription;
    }
    /**
     * @return the productDiscount
     */
    public String getProductCategoryDiscount() {
        return categoryDiscount;
    }
    /**
     * @param productDiscount the productDiscount to set
     */
    public void setProductCategoryDiscount(String productDiscount) {
        this.categoryDiscount = productDiscount;
    }
    public String getProductCategoryImageUrl() {
        return categoryImageUrl;
    }
    public void setProductCategoryImageUrl(String productUrl) {
        this.categoryImageUrl = productUrl;
    }

}
