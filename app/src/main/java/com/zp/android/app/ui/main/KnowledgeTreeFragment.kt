package com.zp.android.app.ui.main

import android.os.Bundle
import com.zp.android.base.BaseFragment
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by zhaopan on 2018/10/18.
 */
class KnowledgeTreeFragment: BaseFragment() {

    companion object {
        const val TAG: String = "KnowledgeTreeFragment"
        fun newInstance(): SupportFragment {
            val fragment = KnowledgeTreeFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}