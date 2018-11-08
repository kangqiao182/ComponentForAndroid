package com.zp.android.component

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Created by zhaopan on 2018/8/22.
 * 注:
 * 建立服务时, 原则上不能过多依赖子module的数据类. 尽量最小粒度对外提供服务,
 * 建议用基础数据返回(例: List, Map, String, JsonString)
 */


interface BaseService : IProvider {
    override fun init(context: Context) {}
}