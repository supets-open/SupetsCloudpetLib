package com.supets.pet.preview.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supets.pet.multiimageselector.MultiImageConfig;
import com.supets.pet.multiimageselector.R;
import com.supets.pet.multiimageselector.model.Image;

import java.util.ArrayList;

import uk.co.senab.photoview.HackyViewPager;

/**
 * 默认多选:需要传输文件夹集合和结果
 */
public class MultiImageViewPagerSelectorActivity extends FragmentActivity
		implements PreviewOnItemClick{

	private ArrayList<String> resultList = new ArrayList<>();//选中集合
	private int mDefaultCount;
	private int mode;
	private int pos;

	private MultiImageViewPagerView mMultiImageViewPagerView;
	private HackyViewPager contentView;
	private View controlsView2, controlsView;
	private Button mSubmitButton;
	private ImageView mBack, mCheckBox;
	private TextView mNums;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_image_pager_activity);
		initData();
		initView();
		initTop();
		initPager();
		initCheckBox();
		if (mode==MultiImageConfig.MODE_PREVIEW){
			pos++;
			Log.v("pageview",pos+"");
		}
		updataNums(pos, resultList.size());
	}

	private void initCheckBox() {
		mCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImageFromGrid(mMultiImageViewPagerView.getImage());
			}
		});
	}

	private void selectImageFromGrid(Image image) {
		if (image != null) {
			if (resultList.contains(image.path)) {
				resultList.remove(image.path);
				onImageUnselected(image.path);
			} else {
				// 判断选择数量问题
				if (mDefaultCount == resultList.size()) {
					Toast.makeText(this, R.string.msg_amount_limit,
							Toast.LENGTH_SHORT).show();
					return;
				}
				resultList.add(image.path);
				onImageSelected(image.path);
			}
		}
	}

	// pagechange
	public void updateSelected(String path) {
		if (resultList.contains(path)) {
			mCheckBox.setSelected(true);
		} else {
			mCheckBox.setSelected(false);
		}
	}

	public void updataNums(int page, int total) {
		if (mode == MultiImageConfig.MODE_PREVIEW) {
			mNums.setText(page + "/" + resultList.size());
		} else {
			mNums.setText(page + "/" + total);
		}
	}

	private void initView() {
		controlsView = findViewById(R.id.fullscreen_content_controls);
		controlsView2 = findViewById(R.id.fullscreen_content_controls2);
		controlsView2
				.setVisibility(mode == MultiImageConfig.MODE_PREVIEW ? View.GONE
						: View.VISIBLE);
		contentView = (HackyViewPager) findViewById(R.id.fullscreen_content);
		mCheckBox = (ImageView) findViewById(R.id.checkBox);
		mBack = (ImageView) findViewById(R.id.btn_back);
		mSubmitButton = (Button) findViewById(R.id.commit);
		mNums = (TextView) findViewById(R.id.nums);
	}

	private void initData() {
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(MultiImageConfig.EXTRA_SELECT_COUNT, MultiImageConfig.MaxNum);
		resultList = intent.getStringArrayListExtra(MultiImageConfig.EXTRA_DEFAULT_SELECTED_LIST);

		if (resultList == null) {
			resultList = new ArrayList<>();
		}
		mode = intent.getIntExtra(MultiImageConfig.EXTRA_SELECT_MODE,
				MultiImageConfig.MODE_MULTI);
		pos = intent.getIntExtra(MultiImageConfig.EXTRA_BIGPIC_CURR_POSITION, 0);

	}

	private void initPager() {
		mMultiImageViewPagerView = new MultiImageViewPagerView();
		mMultiImageViewPagerView.init(this,this, mode, resultList, pos,contentView);
	}

	private void initTop() {

		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = getIntent();
				in.putStringArrayListExtra(
						MultiImageConfig.EXTRA_RESULT, resultList);
				setResult(Activity.RESULT_OK, in);
				finish();
			}
		});
		if (mode != MultiImageConfig.MODE_PREVIEW) {
			multBtn();
		} else {
			deleBtn();
		}
	}

	//删除按钮
	private void deleBtn() {
		mSubmitButton.setText("删除");
		mSubmitButton.setEnabled(true);
		mSubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMultiImageViewPagerView.delete();
			}
		});
	}

	//多选完成
	private void multBtn() {
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText("完成");
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		}

		mSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() <= 0) {
					String path = mMultiImageViewPagerView.getImage().path;
					resultList.add(path);
				}

				Intent data = getIntent();
				data.putStringArrayListExtra(MultiImageConfig.EXTRA_RESULT,
						resultList);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText("完成(" + resultList.size() + "/"
					+ mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}

		updateSelected(path);
	}

	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText("完成(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText("完成");
		}
		updateSelected(path);

	}


	//返回键回调结果处理
	@Override
	public void onBackPressed() {
		Intent in = getIntent();
		in.putStringArrayListExtra(MultiImageConfig.EXTRA_RESULT,
				resultList);
		setResult(Activity.RESULT_OK, in);
		finish();
	}

	@Override
	public void onItemClick(Image image) {
		if (mode == MultiImageConfig.MODE_PREVIEW ){
			boolean visible=controlsView.getVisibility()==View.GONE;
			controlsView.setVisibility(visible? View.VISIBLE
					: View.GONE);
		}else{
			boolean visible=controlsView.getVisibility()==View.GONE;
			controlsView.setVisibility(visible? View.VISIBLE
					: View.GONE);
			controlsView2.setVisibility(visible ? View.VISIBLE
					: View.GONE);
		}

	}

	//删除完了回调
	@Override
	public void onDeleteCallBack() {
		Intent in = getIntent();
		in.putStringArrayListExtra(MultiImageConfig.EXTRA_RESULT,
				resultList);
		setResult(Activity.RESULT_OK, in);
		finish();
	}
}
