package com.zp.android.home

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zp.android.base.BaseActivity
import com.zp.android.base.utils.RxUtil
import com.zp.android.common.snackBarToast
import com.zp.android.component.RouterExtras
import com.zp.android.component.RouterPath
import com.zp.android.net.RetrofitHelper
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber

@Deprecated("测试, 做独立启动时用. ")
@Route(path = RouterPath.Home.MAIN, name = "", extras = RouterExtras.FLAG_LOGIN)
class MainActivity : BaseActivity() {

    companion object {
        fun open(){
            ARouter.getInstance().build(RouterPath.TEST.MAIN).navigation()
        }
    }

    val homeApi by lazy { RetrofitHelper.createService(HomeApi::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("进入Test")
        UI (true) {
            constraintLayout {
                val tvMain = textView("Test模块首页Main"){
                    id = View.generateViewId()
                    textSize = 16f
                    textColorResource = R.color.base_text_green
                }.lparams(wrapContent, wrapContent){
                }

                button("加载数据"){
                    onClick {
                        homeApi.getArticles(1)
                            .compose(RxUtil.applySchedulersToObservable())
                            .subscribe({
                                it.data.datas
                                toast("加载成功!!!")
                            },{
                                snackBarToast(this@button,"加载失败!!!")
                                Timber.d(it)
                            })
                    }
                }.lparams(matchParent, wrapContent){
                    topToBottom = tvMain.id
                }

            }
        }
    }
}
