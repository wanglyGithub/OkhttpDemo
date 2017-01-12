package com.wangly.okhttputil.utils;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wangly.okhttputil.listener.ResultCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by wangly on 2017/1/10.
 */

public class OkhttpHelper {
    public static final String TAG = OkhttpHelper.class.getSimpleName();
    public static final MediaType MEDIATYPE = MediaType.parse("image/jpeg; charset=utf-8");
    public boolean DOWN_FILE = false;
    private OkHttpClient httpClient;
    public static OkhttpHelper instance;
    private Gson gson;
    private Handler handler;

    private OkhttpHelper() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        handler = new Handler();


        gson = new Gson();
    }

    public static OkhttpHelper getInstance() {
        if (instance == null) {
            instance = new OkhttpHelper();
        }
        return instance;
    }


    public void get(String url, Map<String, String> params, ResultCallback callback) {

        Request request = doRequest(url, null, null, null, RequestMethod.GET);

        executeRequest(request, callback);


    }


    public void post(String url, Map<String, String> params, ResultCallback callback) {
        Request request = doRequest(url, params, null, null, RequestMethod.POST);

        executeRequest(request, callback);
    }

    public void post(String url, Map<String, String> params, File file, ResultCallback callback) {


        Request request = doRequest(url, params, file, null, RequestMethod.FILE);
        executeRequest(request, callback);


    }


    public void post(String url, Map<String, String> params, List<File> files, ResultCallback callback) {
        Request request = doRequest(url, params, null, files, RequestMethod.FILES);
        executeRequest(request, callback);
    }

    public void downFile(String url, ResultCallback callback) {
        Request request = doRequest(url, null, null, null, RequestMethod.GET);
        DOWN_FILE = true;
        executeRequest(request, callback);
    }


    /**
     * 执行
     *
     * @param request
     */
    private void executeRequest(final Request request, final ResultCallback callback) {
        callback.onStart();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {


                Log.w(TAG, "得到的异常信息" + e.getMessage());

                Throwable throwable = e.getCause();
                if (null != throwable) {

                    final String exception = ApiException.getExceptionInfo(throwable);

                    runMainThreadFailure(call, callback, exception);
                }

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (!DOWN_FILE) {
                        final String jsonString = response.body().string();
                        if (!"".equals(jsonString) && null != jsonString) {

                            if (callback.mType == String.class) {
                                runMainThreadSuccess(response, callback, jsonString);

                            } else {
                                final Object object;
                                try {
                                    object = gson.fromJson(jsonString, callback.mType);
                                    runMainThreadSuccess(response, callback, object);
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                    final Throwable throwable = e.getCause();
                                    runMainThreadFailure(call, callback, ApiException.getExceptionInfo(throwable));
                                }

                            }

                        }
                        return;
                    }

                    InputStream inputStream = response.body().byteStream();
                    long fileSize = response.body().contentLength();
                    FileUtils.write(inputStream, fileSize);


                }
            }
        });
    }


    private Request doRequest(String url, Map<String, String> parames, File file, List<File> files, RequestMethod type) {

        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (type == RequestMethod.GET) {
            builder.get();
        } else if (type == RequestMethod.POST) {
            builder.post(doRequestBody(parames));
        } else if (type == RequestMethod.FILE) {
            builder.post(doRequestBody(parames, file));
        } else {
            builder.post(doRequestBody(parames, files));
        }

        return builder.build();

    }


    private RequestBody doRequestBody(Map<String, String> parames) {

        FormBody.Builder builder = new FormBody.Builder();

        if (null != parames && parames.size() > 0) {
            for (Map.Entry<String, String> entry : parames.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        } else {
            Log.w(TAG, "parames is null!");
        }
        return builder.build();


    }

    private RequestBody doRequestBody(Map<String, String> parames, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if ((file.exists() && null != file)) {
            RequestBody fileBody = RequestBody.create(MEDIATYPE, file);
            builder.setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : parames.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
            builder.addFormDataPart("file", "image", fileBody);
            return builder.build();
        } else {
            Log.e(TAG, "file not exists！");

        }

        return null;
    }

    private RequestBody doRequestBody(Map<String, String> parames, List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : parames.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        if (null != files && files.size() > 0) {
            for (File file : files) {
                if (file.exists() && null != file) {
                    RequestBody fileBody = RequestBody.create(MEDIATYPE, file);
                    builder.addFormDataPart("file", "image", fileBody);
                } else {
                    Log.e(TAG, "files not exists");
                }
            }

            return builder.build();
        } else {
            Log.e(TAG, "files null");
        }
        return null;
    }


    private void runMainThreadSuccess(final Response response, final ResultCallback callback, final Object result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.Success(response, result);
                }

            }
        });
    }

    private void runMainThreadFailure(final Call call, final ResultCallback callback, final String exception) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.Failure(call, exception);
                }

            }
        });
    }


    enum RequestMethod {
        GET,
        POST,
        FILE,
        FILES

    }
}
