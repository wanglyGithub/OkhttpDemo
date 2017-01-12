package com.wangly.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wangly.okhttputil.bean.Flower;
import com.wangly.okhttputil.listener.ResultCallback;
import com.wangly.okhttputil.utils.OkhttpHelper;
import com.wangly.okhttputil.utils.ProgressDialogHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tv_result;
    private ProgressDialogHandler dialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_result = (TextView) findViewById(R.id.tv_result);
        dialogHandler = new ProgressDialogHandler(this);
    }

    /**
     * get 带参数请求
     *
     * @param view
     */
    public void getRequest(View view) {
        String url = "http://192.168.20.244/********";
        OkhttpHelper.getInstance().get(url, null, new ResultCallback<Flower>() {

            @Override
            public void onStart() {
                dialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG,"加载中····").sendToTarget();
            }


            @Override
            public void Success(Response response, Flower flower) {
                Log.i("wangly", "成功 :" + flower.toString());
                Log.i("wangly", "成功 :" + flower.flowernum);
                tv_result.setText(flower.flowernum+"");
                dialogHandler.obtainMessage(ProgressDialogHandler.LOAD_SUCCEED).sendToTarget();
            }


            @Override
            public void Failure(Call call, String exception) {
                Log.i("wangly", "onFailure :" + exception);
                dialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            }
        });
    }

    /**
     * post 表单请求
     *
     * @param view
     */
    public void postRequest(View view) {
        String url = "http://192.168.20.244/********";
        Map<String, String> parmars = new HashMap<>();
        parmars.put("userid", "40");
        OkhttpHelper.getInstance().post(url, parmars, new ResultCallback<String>() {

            @Override
            public void onStart() {
                dialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG,"加载中····").sendToTarget();
            }

            @Override
            public void Success(Response response, String s) {
                Log.i("wangly", "成功 :" + s);
                tv_result.setText(s);
                dialogHandler.obtainMessage(ProgressDialogHandler.LOAD_SUCCEED,"获取数据成功!").sendToTarget();
            }


            @Override
            public void Failure(Call call, String exception) {
                Log.i("wangly", "onFailure :" + exception);
                dialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            }
        });
    }

    /**
     * 单文件上传
     *
     * @param view
     */

    public void fileUpload(View view) {
        String url = "http://192.168.20.244//********";
        Map<String, String> map = new HashMap<String, String>();
        map.put("vid", "1002");
        map.put("userid", "40");
        map.put("title", "测试");
        map.put("description", "走你······");
        String filePath = "/storage/emulated/0/Download/img_1474963491.jpg";
        File file = new File(filePath);
        OkhttpHelper.getInstance().post(url, map, file, new ResultCallback<String>() {

            @Override
            public void onStart() {
                dialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG,"上传中····").sendToTarget();
            }

            @Override
            public void Success(Response response, String s) {
                Log.i("wangly", "成功 :" + s);
                tv_result.setText(s);
                dialogHandler.obtainMessage(ProgressDialogHandler.LOAD_SUCCEED,"上传成功啦~").sendToTarget();
            }

            @Override
            public void Failure(Call call, String exception) {
                Log.i("wangly", "onFailure :" + exception);
                dialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            }
        });
    }


    /**
     * 多文件上传
     *
     * @param view
     */
    public void filesUpload(View view) {


    }


    public void fileDown(View view) {
        String url = "http://sw.bos.baidu.com/sw-search-sp/software/3545f5720dafd/IQIYIsetup_bdtw_5.5.33.3550.exe";
        OkhttpHelper.getInstance().downFile(url, new ResultCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void Success(Response response, String o) {

            }

            @Override
            public void Failure(Call call, String exception) {
                Log.i("wangly", "onFailure :" + exception);
            }
        });
    }
}
