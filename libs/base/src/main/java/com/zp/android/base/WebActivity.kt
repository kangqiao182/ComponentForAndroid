package com.zp.android.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.NestedScrollAgentWebView
import com.zp.android.base.widget.akAppToolBarLayout
import com.zp.android.common.*
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.design.themedAppBarLayout


/**
 * Created by zhaopan on 2018/10/21.
 */

@Route(path = RouterPath.Base.WEB, name = "公共的Web页面")
class WebActivity : BaseActivity() {

    companion object {
        val KEY_ID = "id"
        val KEY_URL = "url"
        val KEY_TITLE = "title"
        val KEY_IS_SHOW_TITLE = "isShowTitle"

        @JvmOverloads
        @JvmStatic
        fun open(context: Context, url: String, title: String, id: Int = 0, isShowTitle: Boolean = true) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(KEY_ID, id)
                putExtra(KEY_URL, url)
                putExtra(KEY_TITLE, title)
                putExtra(KEY_IS_SHOW_TITLE, isShowTitle)
            })
        }

        @JvmOverloads
        @JvmStatic
        fun open(url: String, title: String, id: Int = 0, isShowTitle: Boolean = true) {
            ARouter.getInstance().build(RouterPath.Base.WEB)
                .withInt(KEY_ID, id)
                .withString(KEY_URL, url)
                .withString(KEY_TITLE, title)
                .withBoolean(KEY_IS_SHOW_TITLE, isShowTitle)
                .navigation()
        }
    }

    @Autowired
    @JvmField
    var id = 0
    @Autowired
    @JvmField
    var title = ""
    @Autowired
    @JvmField
    var url = ""
    @Autowired
    @JvmField
    var isShowTitle = true

    private val ui: WebActivityUI by lazy { WebActivityUI() }
    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = argument(KEY_ID, 0)
        url = argument(KEY_URL, "")
        title = argument(KEY_TITLE, "")
        isShowTitle = argument(KEY_IS_SHOW_TITLE, true)
        ui.setContentView(this)

        ui.toolbar.run {
            //setTitle(R.string.loading)
            setSupportActionBar(ui.toolbar)
            //supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        ui.tvTitle.apply {
            setText(R.string.loading)
            visible()
            postDelayed({
                ui.tvTitle.isSelected = true
            }, 2000)
        }

        val webView = NestedScrollAgentWebView(this)
        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        agentWeb = AgentWeb.with(this)//传入Activity or Fragment
            .setAgentWebParent(ui.rootView, 1, layoutParams)//传入AgentWeb 的父控件
            .useDefaultIndicator()// 使用默认进度条
            .setWebView(webView)
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(webViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .createAgentWeb()
            //.ready()
            .go(url)

        agentWeb?.webCreator?.webView?.let {
            it.settings.domStorageEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.base_menu_webpage, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                }
                return true
            }
            R.id.action_like -> {

                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(url)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event)!!) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    /**
     * webViewClient
     */
    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            // super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }
    }

    /**
     * webChromeClient
     */
    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            title.let {
                // toolbar.title = it
                ui.tvTitle.text = it
            }
        }
    }
}

class WebActivityUI : AnkoComponent<WebActivity> {
    lateinit var rootView: ConstraintLayout
    lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: Toolbar
    lateinit var tvTitle: TextView

    override fun createView(ui: AnkoContext<WebActivity>) = with(ui) {
        constraintLayout {
            id = View.generateViewId()
            this@WebActivityUI.rootView = this
            backgroundColorResource = R.color.base_bg_white
            layoutParams = ConstraintLayout.LayoutParams(matchParent, wrapContent)

            appBarLayout = themedAppBarLayout(R.style.base_AppTheme_AppBarOverlay) {
                id = View.generateViewId()
                fitsSystemWindows = true
                elevation = dip(0).toFloat()

                toolbar = toolbarV7 {
                    id = View.generateViewId()
                    fitsSystemWindows = true
                    popupTheme = R.style.base_AppTheme_PopupOverlay

                    tvTitle = textView {
                        ellipsize = TextUtils.TruncateAt.MARQUEE
                        singleLine = true
                        textColorResource = R.color.white
                        textSize = 18f
                        gone()
                    }.lparams(matchParent, wrapContent)
                }.lparams(matchParent, attrDimen(android.R.attr.actionBarSize)) {
//                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
//                            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, wrapContent) {
                //fitsSystemWindows = true
                //elevation = dip(0).toFloat()
            }

            /*nestedScrollAgentWebView {

            }.lparams(matchParent, matchParent){

            }*/
        }
    }


    /*@Deprecated("不再使用了.")
    lateinit var webContainer: FrameLayout
    @Deprecated("废弃的, 仅保留")
    fun createView2(ui: AnkoContext<WebActivity>) = with(ui) {
        constraintLayout {
            appBarLayout = themedAppBarLayout(R.style.base_AppTheme_AppBarOverlay) {
                id = View.generateViewId()
                fitsSystemWindows = true
                elevation = dip(0).toFloat()

                toolbar = toolbarV7 {
                    fitsSystemWindows = true
                    popupTheme = R.style.base_AppTheme_PopupOverlay
                }.lparams(matchParent, attrDimen(android.R.attr.actionBarSize)) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, wrapContent) {
            }

            webContainer = frameLayout {
                id = View.generateViewId()
                fitsSystemWindows = true
                backgroundColorResource = R.color.base_bg_white
            }.lparams(matchParent, matchParent) {
                topMargin = attrDimen(android.R.attr.actionBarSize)
                //topToBottom = appBarLayout.id
            }
        }
    }*/

}