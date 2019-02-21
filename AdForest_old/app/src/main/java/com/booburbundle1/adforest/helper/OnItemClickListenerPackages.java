package com.booburbundle1.adforest.helper;

import com.booburbundle1.adforest.modelsList.PackagesModel;

public interface OnItemClickListenerPackages {
    void onItemClick(PackagesModel item);
    void onItemTouch();
    void onItemSelected(PackagesModel packagesModel,int spinnerPosition);
}
