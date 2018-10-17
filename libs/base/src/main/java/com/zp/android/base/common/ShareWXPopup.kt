package com.zp.android.base.common

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.view.animation.*
//import org.jetbrains.anko.sdk25.coroutines.onClick
import razerdp.basepopup.BasePopupWindow
import timber.log.Timber
//import cn.sharesdk.framework.Platform
//import cn.sharesdk.framework.ShareSDK
import com.zp.android.base.R
import com.zp.android.base.utils.CaptureViewUtil
import com.zp.android.common.copy
import okhttp3.internal.platform.Platform
import org.jetbrains.anko.toast


/**
 * Created by zhaopan on 2018/7/21.
 */
/*
class ShareWXPopup(val context: Activity): BasePopupWindow(context){

    private lateinit var inviteShareView: InviteShareView
    private var rxPermissions: RxPermissions
    var shareParam: Platform.ShareParams? = null

    init {
        rxPermissions = RxPermissions(context)
    }

    override fun onCreateContentView(): View {
        val view = createPopupById(R.layout.base_popup_share_wechat)
        inviteShareView = view.findViewById(R.id.layout_share_img)
        view.findViewById<View>(R.id.tv_friend_circle).onClick {
            doShareWeChat("WechatMoments")
        }
        view.findViewById<View>(R.id.tv_friend).onClick {
            doShareWeChat("Wechat")
        }
        view.findViewById<View>(R.id.tv_copy_link).onClick {
            shareParam?.apply {
                this@ShareWXPopup.context.copy(text)
                this@ShareWXPopup.context.toast(R.string.base_share_copied_link)
            }
        }
        view.findViewById<View>(R.id.tv_save_img).onClick {
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe({ granted ->
                        if (granted!!) {
                            inviteShareView.getInviteShareBitmap()?.let {
                                CaptureViewUtil.saveImg(it, inviteShareView.shareInviteImageName())
                            }
                        }
                    }, { tr -> Timber.e(tr.message) })
            this@ShareWXPopup.context.toast(R.string.share_saved_img)
        }
        view.findViewById<View>(R.id.btn_cancel).onClick {
            dismiss()
        }
        return view
    }

    override fun onCreateShowAnimation(): Animation {
        return AnimationUtils.loadAnimation(ChainApp.app, R.anim.base_bottom_in)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationUtils.loadAnimation(ChainApp.app, R.anim.base_bottom_out)
    }

    fun doShareWeChat(platform: String){
        shareParam?.imageData = inviteShareView.getInviteShareBitmap()
        val plat = ShareSDK.getPlatform(platform)
        plat.platformActionListener = ShareUtil
        plat.share(shareParam)
    }

    fun setShareParam(code: String, content: String, info: String, shareCopy: String, bitmap: Bitmap?){
        if(bitmap == null) {
            inviteShareView.setInviteInfo(code, content, info)
        } else {
            inviteShareView.setInviteInfo(code, bitmap, info)
        }
        shareParam = Platform.ShareParams().apply {
            title = info
            text = shareCopy + content  //分享内容 = 前缀文案shareCopy + 分享Url链接content
            shareType = Platform.SHARE_IMAGE
        }
    }
}*/
