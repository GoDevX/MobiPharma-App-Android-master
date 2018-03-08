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
    public String getCategoryDesc_AR() {
        return CategoryDesc_AR;
    }

    public void setCategoryDesc_AR(String categoryDesc_AR) {
        CategoryDesc_AR = categoryDesc_AR;
    }

    public String getParent_CategoryID() {
        return Parent_CategoryID;
    }

    public void setParent_CategoryID(String parent_CategoryID) {
        Parent_CategoryID = parent_CategoryID;
    }

    private String Parent_CategoryID;

    private String CategoryDesc_AR;
    private String CategoryID;

    public ProductCategoryModel(String categoryID, String categoryName_EN, String categoryDesc_EN, String categoryName_AR, String categoryLevel, String categoryImage, String defaultProductsImage) {
        CategoryID = categoryID;
        CategoryName_EN = categoryName_EN;
        CategoryDesc_EN = categoryDesc_EN;
        CategoryName_AR = categoryName_AR;
        CategoryLevel = categoryLevel;
        CategoryImage = categoryImage;
        DefaultProductsImage = defaultProductsImage;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public void setCategoryName_EN(String categoryName_EN) {
        CategoryName_EN = categoryName_EN;
    }

    public void setCategoryDesc_EN(String categoryDesc_EN) {
        CategoryDesc_EN = categoryDesc_EN;
    }

    public void setCategoryName_AR(String categoryName_AR) {
        CategoryName_AR = categoryName_AR;
    }

    public void setCategoryLevel(String categoryLevel) {
        CategoryLevel = categoryLevel;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = "http://192.168.1.5:123/_Media/Category_Products_Images/Category_Images/"+categoryImage;
        String a=CategoryImage;
    }

    public void setDefaultProductsImage(String defaultProductsImage) {
        DefaultProductsImage = defaultProductsImage;
    }

    private String CategoryName_EN;
    private String CategoryDesc_EN;
    private String CategoryName_AR;
    private String CategoryLevel;
    private String CategoryImage;

    public String getCategoryID() {
        return CategoryID;
    }

    public String getCategoryName_EN() {
        return CategoryName_EN;
    }

    public String getCategoryDesc_EN() {
        return CategoryDesc_EN;
    }

    public String getCategoryName_AR() {
        return CategoryName_AR;
    }

    public String getCategoryLevel() {
        return CategoryLevel;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public String getDefaultProductsImage() {
        return DefaultProductsImage;
    }

    private String DefaultProductsImage;


    public ProductCategoryModel(){}



}
