package com.zp.android.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import android.support.v7.widget.Toolbar
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.NestedScrollAgentWebView
import com.zp.android.common.argument
import com.zp.android.common.attrDimen
import com.zp.android.common.toolbarV7
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

        fun open(context: Context, url: String, title: String, id: Int = 0, isShowTitle: Boolean = true) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(KEY_ID, id)
                putExtra(KEY_URL, url)
                putExtra(KEY_TITLE, title)
                putExtra(KEY_IS_SHOW_TITLE, isShowTitle)
            })
        }

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
    @JvmField var id = 0
    @Autowired
    @JvmField var title = ""
    @Autowired
    @JvmField var url = ""
    @Autowired
    @JvmField var isShowTitle = true

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
            setTitle(R.string.loading)
            setSupportActionBar(ui.toolbar)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener{ finish() }
        }
        agentWeb = AgentWeb.with(this)//传入Activity or Fragment
            .setAgentWebParent(ui.webContainer, 1, LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件
            .useDefaultIndicator()// 使用默认进度条
            .setWebView(NestedScrollAgentWebView(this))
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(webViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .createAgentWeb()
            .ready()
            .go(url)
    }

    /**
     * webViewClient
     */
    private val webViewClient = object : WebViewClient() {
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
                ui.toolbar.title = it
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
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}

class WebActivityUI : AnkoComponent<WebActivity> {
    lateinit var appBarLayout: AppBarLayout
    lateinit var toolbar: Toolbar
    lateinit var webContainer: FrameLayout
    override fun createView(ui: AnkoContext<WebActivity>) = with(ui) {
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
    }

}