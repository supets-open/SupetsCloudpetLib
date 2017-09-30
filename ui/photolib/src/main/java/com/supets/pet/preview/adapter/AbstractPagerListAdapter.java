/*
 * Date: 14-8-18
 * Project: Access-Control-V2
 */
package com.supets.pet.preview.adapter;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的PagerAdapter实现类,封装了内容为View,数据为List类型的适配器实现.
 * Author: msdx (645079761@qq.com)
 * Time: 14-8-18 下午2:34
 */
public abstract class AbstractPagerListAdapter<T> extends AbstractViewPagerAdapter {
    protected List<T> mData=new ArrayList<T>();

    @Override
    public int getCount() {
        return mData.size();
    }

    public abstract View newView(int position);

    public T getItem(int position) {
        return mData.get(position);
    }
}