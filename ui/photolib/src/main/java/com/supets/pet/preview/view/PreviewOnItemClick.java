package com.supets.pet.preview.view;

import com.supets.pet.multiimageselector.model.Image;

/**
 * supets_adroid
 *
 * @user lihongjiang
 * @description
 * @date 2017/1/15
 * @updatetime 2017/1/15
 */

public interface PreviewOnItemClick {

    void onItemClick(Image image);

    void onDeleteCallBack();

    void updataNums(int curr, int total);

    void updateSelected(String path);
}
