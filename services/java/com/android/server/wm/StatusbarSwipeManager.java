package com.android.server.wm;

import android.provider.Settings;
import android.os.Handler;
import android.util.Log;
import android.content.Context;

public class StatusbarSwipeManager {

    private static StatusbarSwipeManager mInstance;
    
    private final String TAG = "StatusbarSwipeManager";
    private final boolean DEBUG = true;
    
    private Context mContext;
    private Runnable mRunnable;
    private Handler mHandler;
    
    public static StatusbarSwipeManager getInstance(){
        if (mInstance == null){
            mInstance = new StatusbarSwipeManager();
        }
        return mInstance;
    }
    
    private StatusbarSwipeManager(){
        mHandler = new Handler();
    }
    
    public void setContext(Context context){
        mContext = context;
    }
    
    private Runnable getRunnable(){
        return new Runnable() {
            public void run() {
                Settings.System.putBoolean(mContext.getContentResolver(), 
                     Settings.System.STATUSBAR_SHOW_HIDDEN_WITH_SWIPE, false);
                done();
            }               
        };
    }

    private long getDelay(){
        return Settings.System.getInt(mContext.getContentResolver(), 
                Settings.System.STATUSBAR_SWIPE_TIMEOUT, 5000); 
    }
    
    public void startTimer(){
        if (DEBUG) Log.d(TAG, "startTimer mRunnable="+mRunnable);
        stopTimer();

        Settings.System.putBoolean(mContext.getContentResolver(), 
            Settings.System.STATUSBAR_SHOW_HIDDEN_WITH_SWIPE, true);
        
        mRunnable = getRunnable();
        mHandler.postDelayed(mRunnable, getDelay());
    }
    
    private void done(){
        if (DEBUG) Log.d(TAG, "done mRunnable="+mRunnable);
        mRunnable = null;
    }
    
    public void stopTimer(){
        if (DEBUG) Log.d(TAG, "stopTimer mRunnable="+mRunnable);
        if (mRunnable != null){
            mHandler.removeCallbacks(mRunnable);
        }
    }
    
    public void resumeTimer(){
        if (DEBUG) Log.d(TAG, "resumeTimer mRunnable="+mRunnable);
        if (mRunnable != null){
            mHandler.post(mRunnable);
        }
    }
}

