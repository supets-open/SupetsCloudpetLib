package com.supets.pet.uiwidget.recyclelib;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:          zgl_dmw
 * Email:           2559531803@qq.com
 * Date:            2016/8/11 10:19
 * Description:     SupetRecyclerView
 */
public class SupetRecyclerView extends RecyclerView {
    private RecyclerView.Adapter<SupetRecyclerViewHolder> recyclerAdapter;

    public SupetRecyclerView(Context context) {
        super(context);
    }

    public SupetRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SupetRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(SupetRecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
        super.setAdapter(recyclerAdapter);
    }

    public void setAdapter(SupetRecyclerCommonAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
        super.setAdapter(recyclerAdapter);
    }

    public void addHeaderView(View view) {
        if (recyclerAdapter instanceof  SupetRecyclerAdapter) {
            ((SupetRecyclerAdapter) recyclerAdapter).addHeaderView(view);
        } else if (recyclerAdapter instanceof SupetRecyclerCommonAdapter) {
            ((SupetRecyclerCommonAdapter) recyclerAdapter).addHeaderView(view);
        }
    }

    public void addFooterView(View view) {
        if (recyclerAdapter instanceof  SupetRecyclerAdapter) {
            ((SupetRecyclerAdapter) recyclerAdapter).addFooterView(view);
        } else if (recyclerAdapter instanceof SupetRecyclerCommonAdapter) {
            ((SupetRecyclerCommonAdapter) recyclerAdapter).addFooterView(view);
        }
    }
}
