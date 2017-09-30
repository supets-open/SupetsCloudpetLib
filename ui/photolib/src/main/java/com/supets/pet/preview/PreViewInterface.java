package com.supets.pet.preview;

import android.app.Activity;
import android.content.Intent;

import com.supets.pet.multiimageselector.MultiImageConfig;
import com.supets.pet.preview.view.MultiImageViewPagerSelectorActivity;

import java.util.ArrayList;

public class PreViewInterface {

    public static final int   PreviewRequstCode=0x2000;

    public static void startPreView(Activity activity, int pos, int maxNum, ArrayList<String> result) {
        Intent intent = new Intent(activity, MultiImageViewPagerSelectorActivity.class);
        intent.putExtra(MultiImageConfig.EXTRA_SELECT_COUNT, maxNum);
        intent.putExtra(MultiImageConfig.EXTRA_DEFAULT_SELECTED_LIST, result);
        intent.putExtra(MultiImageConfig.EXTRA_SELECT_MODE, MultiImageConfig.MODE_PREVIEW);
        intent.putExtra(MultiImageConfig.EXTRA_BIGPIC_CURR_POSITION, pos);
        activity.startActivityForResult(intent,PreviewRequstCode);
    }
}
