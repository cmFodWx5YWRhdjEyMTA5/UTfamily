package com.ufotech.ufo.utfamily.update.utils;

import android.graphics.Color;

import com.ufotech.ufo.utfamily.R;

import static com.ufotech.ufo.utfamily.utils.UIUtils.getColor;

/**
 * Created by huagnshuyuan on 2017/3/16.
 */

public class ColorData {
    /**
     * 对话框字体主题色
     */

    // 方法 1: parseColor
    // public static int COLOR_THEME = Color.parseColor("#eb2127"); // 紅色
    // 方法 2: 失敗
    // public static int COLOR_THEME = Color.parseColor(String.format("#%06X", (0xFFFFFF & R.color.colorPrimary)));
    // 方法 3: ( ContextCompat.getColor(getContext(), R.color.colorPrimary) )
    public static int COLOR_THEME = getColor(R.color.colorPrimary);
}
