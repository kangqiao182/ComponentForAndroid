package com.zp.android.user

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.base.RxBus
import com.zp.android.base.mvvm.FailedEvent
import com.zp.android.base.mvvm.LoadingEvent
import com.zp.android.base.mvvm.SuccessEvent
import com.zp.android.component.RouterPath
import com.zp.android.component.event.LoginSuccessEvent
import com.zp.android.user.databinding.UserActivityLoginBinding
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by zhaopan on 2018/11/7.
 */

@Route(path = RouterPath.User.LOGIN, name = "登录")
class LoginActivity : BaseActivity() {

    companion object {
        const val TAG = "LoginActivity"
        fun open() {
            ARouter.getInstance().build(RouterPath.User.LOGIN).navigation()
        }
    }

    private lateinit var binding: UserActivityLoginBinding
    private val vm by viewModel<ViewModel>()
    private val onClickListener by lazy { initClickListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.user_activity_login)
        binding.run {
            viewModel = vm
            clickListener = onClickListener
            setLifecycleOwner(this@LoginActivity)
        }

        vm.events.observe(this, Observer { event ->
            when(event){
                is LoadingEvent -> {
                    toast(getString(R.string.login_ing))
                }
                is SuccessEvent -> {
                    toast(getString(R.string.login_success))
                    RxBus.postSticky(LoginSuccessEvent)
                    finish()
                }
                is FailedEvent -> {
                    event.error.message?.also {
                        toast(it)
                    }
                }
            }
        })
    }

    private fun initClickListener() = View.OnClickListener {
        when(it.id){
            R.id.btn_login -> { //登录
                if (vm.username.value.isNullOrBlank()) {
                    binding.etUsername.error = getString(R.string.username_not_empty)
                    return@OnClickListener
                }
                if (vm.password.value.isNullOrBlank()) {
                    binding.etPassword.error = getString(R.string.password_not_empty)
                    return@OnClickListener
                }
                vm.loginWanAndroid()
            }
            R.id.tv_sign_up -> { //注册
                RegisterActivity.open()
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

}