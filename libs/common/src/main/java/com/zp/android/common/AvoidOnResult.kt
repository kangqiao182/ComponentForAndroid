package com.zp.android.common

import android.content.Intent
import android.app.Activity
import android.app.Fragment
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import android.os.Bundle
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject


/**
 * Created by zhaopan on 2018/6/11.
 */
class ActivityResultInfo(var resultCode: Int, var data: Intent?)

class AvoidOnResult {
    private val mAvoidOnResultFragment: AvoidOnResultFragment

    constructor(activity: Activity) {
        mAvoidOnResultFragment = getAvoidOnResultFragment(activity)
    }

    constructor(fragment: Fragment) : this(fragment.activity!!)

    private fun getAvoidOnResultFragment(activity: Activity): AvoidOnResultFragment {
        var avoidOnResultFragment: AvoidOnResultFragment? = findAvoidOnResultFragment(activity)
        if (avoidOnResultFragment == null) {
            avoidOnResultFragment = AvoidOnResultFragment()
            val fragmentManager = activity.fragmentManager
            fragmentManager
                    .beginTransaction()
                    .add(avoidOnResultFragment, TAG)
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return avoidOnResultFragment
    }

    private fun findAvoidOnResultFragment(activity: Activity): AvoidOnResultFragment? {
        return activity.fragmentManager.findFragmentByTag(TAG) as? AvoidOnResultFragment
    }

    fun startForResult(intent: Intent): Observable<ActivityResultInfo> {
        return mAvoidOnResultFragment.startForResult(intent)
    }

    fun startForResult(clazz: Class<*>): Observable<ActivityResultInfo> {
        val intent = Intent(mAvoidOnResultFragment.getActivity(), clazz)
        return startForResult(intent)
    }

    fun startForResult(intent: Intent, callback: Callback) {
        mAvoidOnResultFragment.startForResult(intent, callback)
    }

    fun startForResult(clazz: Class<*>, callback: Callback) {
        val intent = Intent(mAvoidOnResultFragment.getActivity(), clazz)
        startForResult(intent, callback)
    }

    interface Callback {
        fun onActivityResult(resultCode: Int, data: Intent?)
    }

    companion object {
        private val TAG = "AvoidOnResult"
    }
}


class AvoidOnResultFragment : Fragment() {
    private val mSubjects = HashMap<Int, PublishSubject<ActivityResultInfo>>()
    private val mCallbacks = HashMap<Int, AvoidOnResult.Callback>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    fun startForResult(intent: Intent): Observable<ActivityResultInfo> {
        val subject = PublishSubject.create<ActivityResultInfo>()
        return subject.doOnSubscribe(object : Consumer<Disposable> {
            @Throws(Exception::class)
            override fun accept(disposable: Disposable) {
                mSubjects.put(subject.hashCode(), subject)
                startActivityForResult(intent, subject.hashCode())
            }
        })
    }

    fun startForResult(intent: Intent, callback: AvoidOnResult.Callback) {
        mCallbacks.put(callback.hashCode(), callback)
        startActivityForResult(intent, callback.hashCode())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //rxjava方式的处理
        val subject = mSubjects.remove(requestCode)
        if (subject != null) {
            subject.onNext(ActivityResultInfo(resultCode, data))
            subject.onComplete()
        }

        //callback方式的处理
        val callback = mCallbacks.remove(requestCode)
        if (callback != null) {
            callback.onActivityResult(resultCode, data)
        }
    }

}