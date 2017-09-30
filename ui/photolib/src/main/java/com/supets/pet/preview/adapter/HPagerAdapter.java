package com.supets.pet.preview.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.facebook.imagepipeline.image.ImageInfo;
import com.supets.corenetwork.fresco.FrescoUtils;
import com.supets.corenetwork.fresco.ImageLoadingListener;
import com.supets.pet.multiimageselector.model.Image;
import com.supets.pet.preview.view.PreviewOnItemClick;

import java.util.ArrayList;
import java.util.List;

import photodraweeview.PhotoDraweeView;

public class HPagerAdapter extends AbstractPagerListAdapter<Image> {

	private Context mContext;
	private List<Image> mSelectedImages = new ArrayList<>();
	public PreviewOnItemClick mListener;

	public HPagerAdapter(Context context) {
		this.mContext = context;
	}

	public void setOnPreviewOnItemClickListener(PreviewOnItemClick mListener) {
		this.mListener = mListener;
	}

	/**
	 * 选择某个图片，改变选择状态
	 * 
	 * @param image
	 */
	public void select(Image image) {
		if (mSelectedImages.contains(image)) {
			mSelectedImages.remove(image);
		} else {
			mSelectedImages.add(image);
		}
		notifyDataSetChanged();
	}

	/**
	 * 通过图片路径设置默认选择
	 *
	 * @param resultList
	 */
	public void setDefaultSelected(ArrayList<String> resultList) {
		for (String path : resultList) {
			Image image = getImageByPath(path);
			if (image != null) {
				mSelectedImages.add(image);
			}
		}
		if (mSelectedImages.size() > 0) {
			notifyDataSetChanged();
		}
	}

	private Image getImageByPath(String path) {
		if (mData != null && mData.size() > 0) {
			for (Image image : mData) {
				if (image.path.equalsIgnoreCase(path)) {
					return image;
				}
			}
		}
		return null;
	}

	/**
	 * 设置数据集
	 * 
	 * @param images
	 */
	public void setData(List<Image> images) {
		mSelectedImages.clear();

		if (images != null && images.size() > 0) {
			mData = images;
		} else {
			mData.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public int getSelectPosition(Image image) {
		return mData.indexOf(image);
	}

	public Image getSelectPositionObject(int pos) {
		return mData.get(pos);
	}

	public void remove(int pos) {
		mData.remove(pos);
		mSelectedImages.remove(pos);
	}

	@Override
	public View newView(final int position) {
		final PhotoDraweeView photoView = new PhotoDraweeView(mContext);
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		photoView.setLayoutParams(params);
		photoView.setMaximumScale(2);
		photoView.setMinimumScale(1f);
		String  url=mData.get(position).path;
		if (!mData.get(position).path.toLowerCase().startsWith("http")){
			url="file://"+url;
		}
		FrescoUtils.displayImage(url,photoView, new ImageLoadingListener<ImageInfo>() {
			@Override
			public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
				if (imageInfo == null || photoView == null) {
					return;
				}
				photoView.update(imageInfo.getWidth(), imageInfo.getHeight());
			}
		});

		photoView.setOnPhotoTapListener(new photodraweeview.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (mListener != null) {
					mListener.onItemClick(mData.get(position));
				}
			}
		});
		return photoView;
	}
}
