package com.supets.pet.preview.view;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.supets.pet.multiimageselector.MultiImageConfig;
import com.supets.pet.multiimageselector.model.Image;
import com.supets.pet.preview.adapter.HPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.HackyViewPager;


public class MultiImageViewPagerView implements
		OnPageChangeListener {

	private HackyViewPager mGridView;
	private HPagerAdapter mImageAdapter;
	private PreviewOnItemClick callback;

	private  int  pos=0;
	private  int total=0;

	private ArrayList<String> resultList;

	public void init(Context context,
					 PreviewOnItemClick  callback,
					 int mode, ArrayList<String> resultList, int pos, HackyViewPager view) {
		this.pos=pos;
		this.resultList = resultList;
		this.callback = callback;
		mGridView = view;

		//总数,预览就是选中集合,多选模式就是多有集合
		total=resultList.size();

		mImageAdapter = new HPagerAdapter(context);
		mImageAdapter.setOnPreviewOnItemClickListener(callback);
		mGridView.setAdapter(mImageAdapter);
		mGridView.addOnPageChangeListener(this);
		if (mode == MultiImageConfig.MODE_PREVIEW) {
			List<Image> images = new ArrayList<>();
			for (int i = 0; i < resultList.size(); i++) {
				images.add(new Image(resultList.get(i), "", 0));
			}
			mImageAdapter.setData(images);
			mImageAdapter.setDefaultSelected(resultList);
			mGridView.setCurrentItem(pos);
		}else{
			//未实现
		}
	}

	public void delete() {
		int pos = mGridView.getCurrentItem();

		if (mGridView.getChildCount() <= 1) {
			resultList.remove(pos);
			callback.onDeleteCallBack();
			return;
		}
		resultList.remove(pos);
		mImageAdapter.remove(pos);
		mGridView.setAdapter(mImageAdapter);
		callback.updataNums(1, resultList.size());
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		pos = arg0;
		callback.updataNums(pos + 1, total);
		callback.updateSelected(mImageAdapter.getSelectPositionObject(arg0).path);
	}

	public Image getImage() {
		int pos = mGridView.getCurrentItem();
		return mImageAdapter.getSelectPositionObject(pos);
	}

}
