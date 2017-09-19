package com.supets.pet.uiwidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supets.commons.utils.UIUtils;
import com.supets.pet.supetsbaseui.R;


public class TabItemView extends RelativeLayout {

	private static final int MAXITEM = 5;
	private LinearLayout mTabItem;
	private TextView mTabText;
	private TextView mTabText2;
	private View mTabLine;
	private ImageView newsTip;

	public TabItemView(Context context) {
		super(context);

		setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
		LayoutInflater.from(context).inflate(R.layout.tab_item, this);
		mTabItem = (LinearLayout) findViewById(R.id.right_relativeLayout);
		mTabItem.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
		mTabItem.setClickable(true);
		mTabText = (TextView) findViewById(R.id.right_textView);
		mTabText2 = (TextView) findViewById(R.id.right_textView2);
		mTabLine = findViewById(R.id.view1);
		newsTip = (ImageView) findViewById(R.id.newsTip);
	}

	public void setTabText(int id) {
		mTabText.setText(id);
	}

	public void setTabText(String id) {
		mTabText.setText(id);
	}

	public void setTabLineVisible(int visible) {
		mTabLine.setVisibility(visible);
	}

	public void setTabItemClickListener(OnClickListener listener) {
		mTabItem.setOnClickListener(listener);
	}

	public LinearLayout getmTabItem() {
		return mTabItem;
	}

	public TextView getmTabText2() {
		mTabText.setVisibility(View.GONE);
		mTabText2.setVisibility(View.VISIBLE);
		return mTabText2;
	}

	public TextView getmTabText() {
		return mTabText;
	}

	public void setTabText2(int id) {
		mTabText.setVisibility(View.GONE);
		mTabText2.setVisibility(View.VISIBLE);
		mTabText2.setText(id);
	}

	public void setTabText2(String id) {
		mTabText.setVisibility(View.GONE);
		mTabText2.setVisibility(View.VISIBLE);
		mTabText2.setText(id);
	}

	public View getmTabLine() {
		return mTabLine;
	}

	public void setShowNewsTip() {
		newsTip.setVisibility(View.VISIBLE);
	}

	public void setHintNewsTip() {
		newsTip.setVisibility(View.GONE);
	}

	public void resetWidth(int tabNums) {
		if (tabNums > 0) {
			int nums = Math.min(tabNums, MAXITEM);
			int width = UIUtils.getScreenWidth() / nums;
			setLayoutParams(new LinearLayout.LayoutParams(width,
					LayoutParams.MATCH_PARENT));
		}
	}

}
