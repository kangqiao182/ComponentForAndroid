package com.zp.android.base.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.AppManager
import com.zp.android.base.BaseActivity
import com.zp.android.base.CtxUtil
import com.zp.android.base.R
import com.zp.android.base.utils.RxUtil
import com.zp.android.base.utils.SPUtil
import com.zp.android.common.AvoidOnResult
import com.zp.android.component.RouterPath
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by zhaopan on 2019/1/8.
 * 应用解锁方式, 实现, 控制.
 */

//认证方式(用什么去认证)
enum class AuthMode {
    NONE,       //不用认证
    FINGER,     //用指纹认证
    GESTURE,    //用手势认证
    PASSWORD;   //用密码认证

    companion object {
        val DEFAULT = NONE

        fun valueOF(name: String): AuthMode {
            return values().find { it.name.equals(name) } ?: NONE
        }
    }
}

//认证类型(认证什么)
enum class AuthType {
    DEFAULT,    //默认
    UNLOCK,     //解锁应用
    IDENTITY;   //校验身份

    fun needExitAppOnKeyBack() = this == UNLOCK

    companion object {
        fun valueOF(name: String): AuthType {
            return values().find { it.name.equals(name) } ?: AuthType.DEFAULT
        }
    }
}

/**
 * 去认证的快捷方式, 以Observable返回认证结果(true/false)
 */
fun Activity.authWith(mode: AuthMode, type: AuthType = AuthType.UNLOCK): Observable<Boolean> {
    return AvoidOnResult(this)
        .startForResult(AuthorizationActivity.getAuthIntent(this, mode, type))
        .filter { it.resultCode == Activity.RESULT_OK }
        .flatMap {
            val result = it.data?.getBooleanExtra(RouterPath.Base.Param.AUTH_RESPONSE_RESULT, false) ?: false
            Observable.just(result)
        }
}

@Route(path = RouterPath.Base.AUTH, name = "应用解锁方式")
class AuthorizationActivity: BaseActivity() {

    companion object {
        val TAG = AuthorizationActivity.javaClass.simpleName
        val RESPONSE_RESULT = RouterPath.Base.Param.AUTH_RESPONSE_RESULT
        fun open(context: Activity, mode: AuthMode = AuthMode.DEFAULT, type: AuthType = AuthType.DEFAULT) {
            val intent = getAuthIntent(context, mode, type)
            context.startActivityForResult(intent, RouterPath.Base.Param.AUTH_REQUEST_CODE)
        }

        fun getAuthIntent(context: Activity, mode: AuthMode, type: AuthType) = Intent(context, AuthorizationActivity::class.java)
            .apply {
                putExtra(RouterPath.Base.Param.AUTH_MODE, mode.name)
                putExtra(RouterPath.Base.Param.AUTH_TYPE, type.name)
            }

        fun route(activity: Activity, mode: AuthMode = AuthMode.DEFAULT, type: AuthType = AuthType.DEFAULT) {
            ARouter.getInstance()
                .build(RouterPath.Base.AUTH)
                .withString(RouterPath.Base.Param.AUTH_MODE, mode.name)
                .withString(RouterPath.Base.Param.AUTH_TYPE, type.name)
                .navigation(activity, RouterPath.Base.Param.AUTH_REQUEST_CODE)
        }
    }

    @JvmField
    @Autowired(name = RouterPath.Base.Param.AUTH_MODE)
    var mode: String = AuthMode.NONE.name
    @JvmField
    @Autowired(name = RouterPath.Base.Param.AUTH_TYPE)
    var type: String = AuthType.DEFAULT.name
    lateinit var authMode: AuthMode
    lateinit var authType: AuthType
    private var retryCount = SPUtil.getInt(SETTING_AUTH_UNLOCK_LIMIT_RETRY_TIMES, 5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_authorization)
        initlize()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initlize()
    }

    private fun initlize() {
        authMode = AuthMode.valueOF(mode)
        authType = AuthType.valueOf(type)
        when(authMode) {
            AuthMode.GESTURE -> {
                toGesture()
            }
            AuthMode.FINGER -> {
                toFinger()
            }
            AuthMode.PASSWORD -> {
                toPassword()
            }
            else -> {
                finish()
            }
        }
    }

    private fun toPassword() {

    }

    private fun toFinger() {

    }

    private fun toGesture() {

    }

    //判断是否需要等待计时结束,解锁.
    private fun checkAndLockUtilTimerFinished(): Boolean {
        val currentTimer = System.currentTimeMillis()
        val unlockCountDown = SPUtil.getLong(SETTING_KEY_UNLOCK_TIME, 0L)
        val interval = (unlockCountDown - currentTimer) / 1000
        if (interval > 0) {
            /*binding.passwordView.clearAll()
            binding.passwordView.setCursorEnable(false)
            binding.passwordView.setEnabled(false)
            hideSoftKeyboard()*/
            Observable.intervalRange(1, interval, 0, 1, TimeUnit.SECONDS)
                .compose(RxUtil.applySchedulersToObservable())
                .subscribe({
                    val countTimerDesc = CtxUtil.getString(R.string.base_auth_password_lock_until_timer_finished).format(interval - it)
                    //binding.tvErrPrompt.setText(countTimerDesc)
                    if (it >= interval) {
                        //binding.tvErrPrompt.setText("")
                        retryCount = SPUtil.getInt(SETTING_AUTH_UNLOCK_LIMIT_RETRY_TIMES, 5)
                        SPUtil.put(SETTING_KEY_UNLOCK_TIME, 0L)
                        initlize()
                    }
                }, {
                    Timber.e(TAG, it)
                })
            return true
        } else {
            /*binding.passwordView.setCursorEnable(true)
            binding.passwordView.setEnabled(true)
            binding.passwordView.requestFocus()
            showSoftKeyboard(binding.passwordView)*/
            return false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //若是在解锁和启动时, 最终结果为失败, 需要退出应用.
            if (authType.needExitAppOnKeyBack()) {
                AppManager.exitApp(this)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}