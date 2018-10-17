package com.zp.android.base.utils;

import android.util.Log;

/**
 * Created by zhaopan on 15/5/6.
 */
public final class Zlog {
    private static final boolean LOG_LOCAL_E = true; 
    private static final boolean LOG_LOCAL_W = true; 
    private static final boolean LOG_LOCAL_I = true; 
    private static final boolean LOG_LOCAL_D = true; 
     
    private static final String PREFIX = "\tzp:::";
    private static String THREAD(){
     return Thread.currentThread().toString()+":"+ Thread.currentThread().getId()+":\t";
    } 
    public static void e(String TAG, String msg){
        if(LOG_LOCAL_E) Log.e(TAG, PREFIX + msg);
    } 
    public static void e(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_E) Log.e(TAG, PREFIX + msg, tr);
    } 
    public static void et(String TAG, String msg){
        if(LOG_LOCAL_E) Log.e(TAG, PREFIX + THREAD() + msg);
    } 
    public static void et(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_E) Log.e(TAG, PREFIX + THREAD() + msg, tr);
    } 
    public static void w(String TAG, String msg){
        if(LOG_LOCAL_W) Log.w(TAG, PREFIX + msg);
    } 
    public static void w(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_W) Log.w(TAG, PREFIX + msg, tr);
    } 
    public static void wt(String TAG, String msg){
        if(LOG_LOCAL_W) Log.w(TAG, PREFIX + THREAD() + msg);
    } 
    public static void wt(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_W) Log.w(TAG, PREFIX + THREAD() + msg, tr);
    } 
    public static void i(String TAG, String msg){
        if(LOG_LOCAL_I) Log.i(TAG, PREFIX + msg);
    } 
    public static void i(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_I) Log.i(TAG, PREFIX + msg, tr);
    } 
    public static void it(String TAG, String msg){
        if(LOG_LOCAL_I) Log.i(TAG, PREFIX + THREAD() + msg);
    } 
    public static void it(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_I) Log.i(TAG, PREFIX + THREAD() + msg, tr);
    }
    public static void d(String TAG, String msg){
        if(LOG_LOCAL_D) Log.d(TAG, PREFIX + msg);
    }
    public static void d(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_D) Log.d(TAG, PREFIX + msg, tr);
    }
    public static void dt(String TAG, String msg){
        if(LOG_LOCAL_D) Log.d(TAG, PREFIX + THREAD() + msg);
    }
    public static void dt(String TAG, String msg, Throwable tr){
        if(LOG_LOCAL_D) Log.d(TAG, PREFIX + THREAD() + msg, tr);
    } 
} 
