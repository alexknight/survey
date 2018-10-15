package com.hw.survey.util;

import com.weiwangcn.betterspinner.library.BetterSpinner;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by haowang on 2018/5/11.
 */


public class ViewUtils {

    public static void disableSpinner(BetterSpinner spinner){
        spinner.setDropDownHeight(0);
        spinner.setEnabled(false);
        spinner.setBackgroundColor(0xFFDDDDDD);

        spinner.setText("");
    }

    public static void enableSpinner(BetterSpinner spinner){
        spinner.setDropDownHeight(WRAP_CONTENT);
        spinner.setEnabled(true);
        spinner.setBackgroundColor(0x00000000);

    }
}
