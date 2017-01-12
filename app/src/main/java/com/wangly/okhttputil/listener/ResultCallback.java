package com.wangly.okhttputil.listener;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wangly on 2017/1/10.
 */

public abstract class ResultCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public ResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }


    public abstract void onStart();


    public abstract void Success(Response response, T t);

    public abstract void Failure(Call call, String exception);

}
