package com.supets.pet.uiwidget.recyclelib;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author:          zgl_dmw
 * Email:           2559531803@qq.com
 * Date:            2016/8/10 18:45
 * Description:     SupetRecyclerViewHolder
 */
public class SupetRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View mWholeView;

    public SupetRecyclerViewHolder(View itemView) {
        super(itemView);
        mWholeView = itemView;
    }

    public View getWholeView() {
        return mWholeView;
    }
}
