package com.dmi.sdksample

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.dmi.mykotlinlib.start.ISdkInstance
import com.dmi.mykotlinlib.start.WCSDK
import com.dmi.mykotlinlib.start.WCSDKConfig
import com.dmi.mykotlinlib.vplayer.WCSDKView

class SampleApplication : Application(), LifecycleObserver{


    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        MainActivity.videoViewBuilder?.pauseVideo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
    }

}