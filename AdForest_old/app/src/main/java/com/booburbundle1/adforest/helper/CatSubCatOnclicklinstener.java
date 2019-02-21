package com.booburbundle1.adforest.helper;

import android.view.View;

import com.booburbundle1.adforest.modelsList.catSubCatlistModel;

public interface CatSubCatOnclicklinstener {
    void onItemClick(catSubCatlistModel item);
    void onItemTouch(catSubCatlistModel item);
    void addToFavClick(View v, String position);

}
