package com.zp.android.app.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zp.android.app.R
import com.zp.android.base.BaseFragment
import com.zp.android.base.flutter.FlutterContainerActivity
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI

/**
 * Created by zhaopan on 2018/10/18.
 */
class MineFragment: BaseFragment() {
    companion object {
        const val TAG: String = "MineFragment"
        fun newInstance(): SupportFragment {
            val fragment = MineFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return Flutter.createView(_mActivity, lifecycle, "route1")
        return UI {
            verticalLayout {rootView
                textView("加载Flutter") {
                    backgroundResource = R.drawable.base_btn_blue_radius5
                    padding = dip(10)
                    gravity = Gravity.CENTER
                    textColorResource = R.color.base_text_white
                    onClick {
                        FlutterContainerActivity.open(_mActivity, "")
                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view
    }
}