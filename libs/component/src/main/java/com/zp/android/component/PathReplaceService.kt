package com.zp.android.component

import android.content.Context
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService

/**
 * Created by zhaopan on 2018/8/24.
 */

//重写跳转URL
//@Route(path = RouterPath.SDK_SERVICE_REPLACE) //必须标明注解
class PathReplaceServiceImpl : PathReplaceService {

    override fun init(context: Context?) {
    }

    /**
     * For normal path.
     * 按照一定的规则处理之后返回处理后的结果
     * @param path raw path
     */
    override fun forString(path: String): String {
        return path
    }

    /**
     * For uri type.
     * 按照一定的规则处理之后返回处理后的结果
     * @param uri raw uri
     */
    override fun forUri(uri: Uri): Uri {
        return uri
    }
}

/*

生成路由文档
// 更新 build.gradle, 添加参数 AROUTER_GENERATE_DOC = enable
// 生成的文档路径 : build/generated/source/apt/(debug or release)/com/alibaba/android/arouter/docs/arouter-map-of-${moduleName}.json
android {
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [AROUTER_MODULE_NAME: project.getName(), AROUTER_GENERATE_DOC: "enable"]
            }
        }
    }
}*/
