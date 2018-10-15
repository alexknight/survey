package com.hw.survey.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by haowang on 2018/5/5.
 */

public class DialogUtils {
    public static Dialog createAlertDialog(Context context, String content, String confirm,
                                           final DialogInterface.OnClickListener confirmListener, final DialogInterface.OnClickListener cancelListener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(content);
        dialog.setButton2(confirm,confirmListener);
        dialog.setButton("取消",cancelListener);
        return dialog;
    }

    public static Dialog createAlertDialog(Context context, String content, String confirm, String cancel,
                                           final DialogInterface.OnClickListener confirmListener, final DialogInterface.OnClickListener cancelListener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage(content);
        dialog.setButton2(confirm,confirmListener);
        dialog.setButton(cancel,cancelListener);
        return dialog;
    }
}
