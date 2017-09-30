/*
 * Date: 14-8-18
 * Project: Access-Control-V2
 */
package com.supets.pet.preview.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 抽象的PagerAdapter实现类,封装了内容为View的公共操作. Author: msdx (645079761@qq.com) Time:
 * 14-8-18 下午2:34
 */
public abstract class AbstractViewPagerAdapter extends PagerAdapter {
	protected SparseArray<View> mViews;
	private boolean mCache = false;

	public AbstractViewPagerAdapter() {
		mViews = new SparseArray<View>();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position);
		if (view == null) {
			view = newView(position);
			if (mCache) {
				mViews.put(position, view);
			}
		}
		container.addView(view);
		return view;
	}

	public abstract View newView(int position);

	// 有缓存下
	public void notifyUpdateView(int position) {
		View view = updateView(mViews.get(position), position);
		mViews.put(position, view);
		notifyDataSetChanged();
	}

	// 有缓存下
	public View updateView(View view, int position) {
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCache) {
			container.removeView(mViews.get(position));
		} else {
			container.removeView((View) object);
		}
	}

	public void setCache(boolean enable) {
		this.mCache = enable;
	}

}