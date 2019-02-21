package com.booburbundle1.adforest.helper;

import android.view.View;

import com.booburbundle1.adforest.modelsList.myAdsModel;

public interface MyAdsOnclicklinstener {

    void onItemClick(myAdsModel item);
    void delViewOnClick(View v, int position);
    void editViewOnClick(View v, int position);

}
