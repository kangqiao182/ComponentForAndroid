package com.zp.android.component

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService

/**
 * Created by zhaopan on 2018/8/24.
 */

//自定义全局降级策略
//@Route(path = RouterPath.SDK_SERVICE_DEGRADE)
class DegradeServiceImpl: DegradeService{

    override fun init(context: Context?) {
    }

    override fun onLost(context: Context?, postcard: Postcard?) {


    }
}

/*
// 实现DegradeService接口，并加上一个Path内容任意的注解即可
ARouter如果发现在目标跳转的情况下失败了，就会回调这个onLost()方法。
onLost()方法的第二个参数postCard翻译过来就是明信片，
这里面就包含了本次跳转中所有的内容，通过拿到这些内容就可以实现自己的降级方案。
下图中所列举的例子是通过跳转到第三方的H5的错误页面来解决的，
因为APP不能够重复发布，但是H5是可以重复发布的，
所以可以通过H5的方式解决降级问题，
把去向的目标页面作为目标的参数传递到H5中
}*/
