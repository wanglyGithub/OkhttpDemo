package com.wangly.okhttputil.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangly on 2017/1/12.
 */

public class ProgressDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    public static final int LOAD_SUCCEED = 3;
    public static final int LOAD_FAILED = 4;

    private Context mContext;
    private ProgressDialog dialog;
    private boolean isCancel;

    public ProgressDialogHandler(Context context) {
        this.mContext = context;

    }

    private void showProgressDialog(String message) {
        initProgressDialog(message);
    }

    private void initProgressDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(message);
            dialog.setCancelable(isCancel);

            if (!dialog.isShowing()) {
                dialog.show();
            }

        }
    }

    private void showLoadSuccessDialog() {
        dismissProgressDialog();

        initProgressDialog("加载成功!");
//        dismissProgressDialog();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Looper.prepare();
                dismissProgressDialog();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,1500);

    }


    private void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                showProgressDialog((String) msg.obj);
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
            case LOAD_SUCCEED:
                showLoadSuccessDialog();
                break;
            case LOAD_FAILED:

                break;
        }
    }
}
