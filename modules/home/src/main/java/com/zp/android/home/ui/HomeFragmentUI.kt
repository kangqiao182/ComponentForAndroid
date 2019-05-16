package com.zp.android.home.ui

import androidx.core.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.zp.android.base.CtxUtil
import com.zp.android.base.ui.WebActivity
import com.zp.android.common.*
import com.zp.android.common.widget.GlideImageLoader
import com.zp.android.component.RouterPath
import com.zp.android.component.ServiceManager
import com.zp.android.component.service.BackResult
import com.zp.android.component.service.HandleCallBack
import com.zp.android.home.Article
import com.zp.android.home.ArticleResponseBody
import com.zp.android.home.BannerItem
import com.zp.android.home.R
import com.zp.android.net.NetUtils
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by zhaopan on 2018/12/12.
 */

class HomeFragmentUI : AnkoComponent<HomeFragment> {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var akAdapter: AKBaseQuickAdapter<Article>
    val bannerView: Banner by lazy { initAndAddBannerView() }
    private var bannerList: List<BannerItem>? = null
    private var isRefresh = true

    override fun createView(ui: AnkoContext<HomeFragment>) = with(ui) {
        swipeRefreshLayout {
            swipeRefreshLayout = this
            layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
            onRefresh {
                isRefresh = true
                akAdapter.setEnableLoadMore(false)
                ui.owner.requestHomeData(isRefresh, 0)
            }

            recyclerView = recyclerView {
                layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
                layoutManager = LinearLayoutManager(ctx)
                addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL).apply {
                    ContextCompat.getDrawable(ctx, R.drawable.base_divider_line)?.let { setDrawable(it) }
                })
            }

            akAdapter = object : AKBaseQuickAdapter<Article>() {
                override fun onCreateItemView() = ArticleAKItemViewUI()
            }.apply {
                setOnItemClickListener { adapter, view, position ->
                    (adapter.getItem(position) as? Article)?.run {
                        // 打开文章链接.
                        WebActivity.open(link, title, id)
                    }
                }

                setOnItemChildClickListener { adapter, view, position ->
                    (adapter.getItem(position) as? Article)?.run {
                        if(ArticleAKItemViewUI.CLICK_LICK.equals(view.getTag())) {
                            if (ServiceManager.getUserService().isLogin()) {
                                if (!NetUtils.isNetworkAvailable(CtxUtil.context())) {
                                    snackBarToast(recyclerView, CtxUtil.getString(R.string.no_network))
                                    return@setOnItemChildClickListener
                                }
                                val collect = !this.collect
                                ServiceManager.getUserService()
                                    .collectOrCancelArticle(this.id, collect, object : HandleCallBack<String> {
                                        override fun onResult(result: BackResult<String>) {
                                            if (result.isOk()) {
                                                this@run.collect = collect
                                                adapter.setData(position, this@run) //刷新当前ItemView.
                                            }
                                            result.data?.let { CtxUtil.showToast(it) }
                                        }
                                    })
                            } else {
                                ARouter.getInstance().build(RouterPath.User.LOGIN).navigation()
                                CtxUtil.showToast(R.string.login_tint)
                            }
                        }
                    }
                }

                setOnLoadMoreListener({
                    isRefresh = false
                    swipeRefreshLayout.isRefreshing = false
                    val page = akAdapter.data.size / 20
                    ui.owner.requestHomeData(isRefresh, page)
                }, recyclerView)
            }
            recyclerView.adapter = akAdapter
            //akAdapter.bindToRecyclerView(recyclerView)
        }
    }

    private fun initAndAddBannerView(): Banner {
        val bannerLayout = LayoutInflater.from(recyclerView.context).inflate(R.layout.home_view_banner, null, false)
        val banner: Banner = bannerLayout.find(R.id.banner)
        banner.run {
            setImageLoader(GlideImageLoader())
            setBannerAnimation(Transformer.Default)
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            setOnBannerListener { position ->
                bannerList?.get(position)?.run {
                    WebActivity.open(url, title, id)
                }
            }
        }
        akAdapter.addHeaderView(bannerLayout)
        return banner
    }

    fun updateArticleData(articles: ArticleResponseBody) {
        if (isRefresh) {
            swipeRefreshLayout.isRefreshing = false
            akAdapter.setEnableLoadMore(true)
        }
        articles.datas?.let {
            akAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < articles.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    fun updateBannerList(list: List<BannerItem>) {
        bannerList = list
        bannerView.update(list, list.map { it.title })
    }
}

class ArticleAKItemViewUI : AKItemViewUI<Article> {
    companion object {
        const val CLICK_LICK = "ivLike"
    }

    lateinit var tvTop: TextView
    lateinit var tvFresh: TextView
    lateinit var tvAuthor: TextView
    lateinit var tvDate: TextView
    lateinit var tvTitle: TextView
    lateinit var tvChapterName: TextView
    lateinit var ivThumbnail: ImageView
    lateinit var ivLike: ImageView

    override fun bind(akViewHolder: AKViewHolder<Article>, item: Article) {
        item.run {
            tvTitle.text = Html.fromHtml(title)
            tvAuthor.text = author
            tvDate.text = niceDate
            ivLike.imageResource = if (collect) R.drawable.ic_like else R.drawable.ic_like_not
            if (chapterName.isNotEmpty()) {
                tvChapterName.text = chapterName
                tvChapterName.visible()
            } else {
                tvChapterName.invalidate()
            }

            akViewHolder.addChildClickListener(ivLike, CLICK_LICK)

            if (envelopePic.isNotEmpty()) {
                ivThumbnail.visible()
                Glide.with(ivThumbnail).load(envelopePic).into(ivThumbnail)
            } else {
                ivThumbnail.gone()
            }

            tvFresh.setVisible(item.fresh)
            tvTop.setVisible(item.top == "1")
        }

    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            topPadding = dip(10)
            rightPadding = dip(10)
            bottomPadding = dip(10)
            tvTop = textView(R.string.top_tip) {
                id = View.generateViewId()
                setPadding(dip(4), dip(2), dip(4), dip(2))
                backgroundResource = R.drawable.bg_fresh
                textSize = 10f
                textColorResource = R.color.base_text_red
                gone()
            }.lparams(wrapContent, wrapContent) {
                leftMargin = dip(10)
            }

            tvFresh = textView(R.string.new_fresh) {
                id = View.generateViewId()
                setPadding(dip(4), dip(2), dip(4), dip(2))
                backgroundResource = R.drawable.bg_fresh
                textSize = 10f
                textColorResource = R.color.base_text_red
                gone()
            }.lparams(wrapContent, wrapContent) {
                leftMargin = dip(10)
            }

            tvAuthor = themedTextView(R.style.base_text_secondary3) {
                id = View.generateViewId()
            }.lparams(wrapContent, wrapContent) {
                leftMargin = dip(10)
                baselineOf(tvFresh)
                rightOf(tvFresh)
            }

            tvDate = themedTextView(R.style.base_text_secondary3) {
                id = View.generateViewId()
            }.lparams(wrapContent, wrapContent) {
                leftMargin = dip(10)
                baselineOf(tvFresh)
                alignParentRight()
            }

            ivThumbnail = imageView() {
                id = View.generateViewId()
                contentDescription = "article thumbnail"
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(dimen(R.dimen.item_img_width), dimen(R.dimen.item_img_height)) {
                leftMargin = dip(10)
                topMargin = dip(8)
                below(tvAuthor)
            }

            tvTitle = themedTextView(R.style.base_text_primary1) {
                id = View.generateViewId()
                bottomPadding = dip(6)
                gravity = Gravity.TOP or Gravity.START
                ellipsize = TextUtils.TruncateAt.END
                setLineSpacing(dip(2).toFloat(), lineSpacingMultiplier)
                maxLines = 2
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(10)
                topMargin = dip(8)
                below(tvAuthor)
                rightOf(ivThumbnail)
            }

            tvChapterName = themedTextView(R.style.base_text_white3) {
                id = View.generateViewId()
                backgroundResource = R.drawable.bg_tag_one
                gravity = Gravity.CENTER
            }.lparams(wrapContent, wrapContent) {
                margin = dip(10)
                alignParentBottom()
                below(tvTitle)
                rightOf(ivThumbnail)
            }

            linearLayout {
                id = View.generateViewId()
                orientation = LinearLayout.HORIZONTAL
                ivLike = imageView(R.drawable.ic_like_not) {
                    id = View.generateViewId()
                    contentDescription = "like article"
                }.lparams(wrapContent, wrapContent)
            }.lparams(dimen(R.dimen.dp_26), dimen(R.dimen.dp_26)) {
                alignParentRight()
                alignParentBottom()
            }
        }
    }
}