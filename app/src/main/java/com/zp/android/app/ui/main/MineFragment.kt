package com.zp.android.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.zp.android.base.BaseFragment
import io.flutter.facade.Flutter
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

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
        return Flutter.createView(_mActivity, lifecycle, "route1")
        /*
        return UI {
            verticalLayout {rootView
                textView("加载Flutter") {
                    onClick {
                        val flutterView = Flutter.createView(_mActivity, lifecycle, "router1")
                        val layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)
                        add
                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view*/
    }
}