package com.zp.android.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import com.zp.android.base.utils.I18NUtil
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity

/**
 * Created by zhaopan on 2018/8/28.
 */

open abstract class BaseActivity: SwipeBackActivity() {

    // 更新语言包资源
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(I18NUtil.attachBaseContext(newBase))
    }

}