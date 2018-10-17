package com.zp.android.app.ui.main

import android.os.Bundle
import com.zp.android.base.BaseFragment
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/10/18.
 */
class ProjectFragment: BaseFragment() {
    companion object {
        const val TAG: String = "ProjectFragment"
        fun newInstance(): SupportFragment {
            val fragment = ProjectFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}