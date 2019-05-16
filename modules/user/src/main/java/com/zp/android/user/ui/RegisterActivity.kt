package com.zp.android.user.ui

import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.base.mvvm.ExceptionEvent
import com.zp.android.base.mvvm.FailedEvent
import com.zp.android.base.mvvm.LoadingEvent
import com.zp.android.base.mvvm.SuccessEvent
import com.zp.android.base.showToast
import com.zp.android.component.RouterPath
import com.zp.android.user.R
import com.zp.android.user.databinding.UserActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/11/8.
 */

@Route(path = RouterPath.User.REGISTER, name = "注册")
class RegisterActivity : BaseActivity() {

    companion object {
        const val TAG = "RegisterActivity"
        fun open() {
            ARouter.getInstance().build(RouterPath.User.REGISTER).navigation()
        }
    }

    private lateinit var binding: UserActivityRegisterBinding
    private val onClickListener by lazy { initClickListener() }
    private val vm by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.user_activity_register)
        binding.run {
            clickListener = onClickListener
            setLifecycleOwner(this@RegisterActivity)
        }
        Log.w(TAG, "vm=${vm}")

        vm.events.observe(this, Observer { event ->
            when(event){
                is LoadingEvent -> {
                    showToast(getString(R.string.register_ing))
                }
                is SuccessEvent -> {
                    showToast(getString(R.string.register_success))
                    finish()
                }
                is FailedEvent -> {
                    showToast(event.errorMsg)
                }
                is ExceptionEvent -> {
                    Timber.e(event.error)
                }
            }
        })
    }

    private fun initClickListener() = View.OnClickListener {
        when (it.id) {
            R.id.btn_register -> { //注册
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                val password2 = binding.etPassword2.text.toString()
                if (username.isNullOrBlank()) {
                    binding.etUsername.error = getString(R.string.username_not_empty)
                    return@OnClickListener
                }
                if (password.isNullOrBlank()) {
                    binding.etPassword.error = getString(R.string.password_not_empty)
                    return@OnClickListener
                }
                if (password2.isNullOrBlank()) {
                    binding.etPassword2.error = getString(R.string.confirm_password_not_empty)
                    return@OnClickListener
                }
                if(password != password2) {
                    binding.etPassword2.error = getString(R.string.password_cannot_match)
                    return@OnClickListener
                }
                vm.registerWanAndroid(username, password, password2)
            }
            R.id.tv_sign_in -> { //去登录
                LoginActivity.open()
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}