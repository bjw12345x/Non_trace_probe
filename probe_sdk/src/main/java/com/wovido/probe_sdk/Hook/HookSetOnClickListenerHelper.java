package com.wovido.probe_sdk.Hook;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookSetOnClickListenerHelper {


    public static void hook(Context context, final View v) {//
        try {
            Method method = View.class.getDeclaredMethod("111");
            method.setAccessible(true);
            Object mListenerInfo = method.invoke(v);
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field field = listenerInfoClz.getDeclaredField("mOnClickListener");
            final View.OnClickListener onClickListenerInstance = (View.OnClickListener) field.get(mListenerInfo);//取得真实的mOnClickListener对象
            Object proxyOnClickListener = Proxy.newProxyInstance(context.getClass().getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.d("HookSetOnClickListener", "onclic hook");
                    return method.invoke(onClickListenerInstance, args);
                }
            });
            field.set(mListenerInfo, proxyOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class ProxyOnClickListener implements View.OnClickListener {
        View.OnClickListener oriLis;

        public ProxyOnClickListener(View.OnClickListener oriLis) {
            this.oriLis = oriLis;
        }

        @Override
        public void onClick(View v) {
            Log.d("HookSetOnClickListener", "点击事件被hook到了");
            if (oriLis != null) {
                oriLis.onClick(v);
            }
        }
    }
}
