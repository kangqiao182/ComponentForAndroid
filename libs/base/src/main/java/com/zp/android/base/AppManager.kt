package com.zp.android.base

import android.app.Activity
import java.util.Stack
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.zp.android.base.common.logd
import com.zp.android.base.common.loge
import com.zp.android.base.common.logi
import timber.log.Timber


/**
 * Created by zhaopan on 2018/6/20.
 */
object AppManager {
    private val application: Application by lazy { BaseApp.application }
    private var activityStack: Stack<Activity> = Stack()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (null != activity) activityStack.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity::class.java == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     * Android应用结束自身进程的方法 https://blog.csdn.net/fenggering/article/details/78538777
     */
    fun exitApp(context: Context) {
        try {
            //先让app进入后台
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            finishAllActivity()
            //调用系统API结束进程
            android.os.Process.killProcess(android.os.Process.myPid())
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.killBackgroundProcesses(context.getPackageName())
            manager.killBackgroundProcesses("pushservice")

            //结束整个虚拟机进程，注意如果在manifest里用android:process给app指定了不止一个进程，则只会结束当前进程
            System.exit(0)
            System.gc()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initActivityLifecycle() {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                finishActivity(activity)
            }

        })
        /*application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            private val TAG = "ActivityLifecycleCallbacks"
            private var topActivity: Activity? = null
            private var mFinalCount: Int = 0
            private val EMPTY_MSG_DELAY = 1
            private val handler = Handler()

            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity) {
                mFinalCount++

                //如果mFinalCount ==1，说明是从后台到前台
                if (mFinalCount == 1) {
                    logd("onActivityStarted mFinalCount=" + mFinalCount.toString())
                    //如果当前不是UNLOCK类型的AuthorizationAcitvity解锁页面.
                    if (!(topActivity?.isUnLockActivity() ?: false)) {
                        logd("onActivityStarted is AuthorizationActivity")
                        activity.let {
                            //如果是需要锁定的页面, 并且未超过锁屏延时时间, 打开解锁界面.
                            var settingManager = application.component.settingManager()
                            if (settingManager.hasPIN() && it.needLockApp() && !handler.hasMessages(EMPTY_MSG_DELAY)) {

                                activity.authOnResult(AuthMode.UNLOCK)
                                        .subscribe({
                                            logd("onActivityStarted_authOnResult auth_result="+it)
                                        },{
                                            loge(it.message)
                                        })
                            }
                        }
                    }
                }
                topActivity = activity
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity) {
                mFinalCount--
                //如果mFinalCount ==0，说明是前台到后台
                if (mFinalCount == 0) {
                    logi("onActivityStopped mFinalCount=" + mFinalCount.toString())
                    activity.let {
                        if (it.needLockApp()) {
                            handler.sendEmptyMessageDelayed(EMPTY_MSG_DELAY, PrefsUtil.getLockScreenDelayTime())
                        }
                    }
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                addActivity(activity)
            }

            override fun onActivityDestroyed(activity: Activity?) {
                finishActivity(activity)
            }
        })*/
    }

}