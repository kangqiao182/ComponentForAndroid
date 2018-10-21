package com.zp.android.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.zp.android.base.BaseFragment
import com.zp.android.base.mvvm.*
import com.zp.android.base.widget.BannerLayout
import com.zp.android.common.*
import com.zp.android.component.RouterPath
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by zhaopan on 2018/10/17.
 */

@Route(path = RouterPath.Home.HOME, name = "Home模块首页入口")
class HomeFragment : BaseFragment() {

    private val ui by lazy { HomeFragmentUI() }
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ui.createView(AnkoContext.create(_mActivity, this))
    }

    override fun initView() {
        viewModel.states.observe(this, Observer { event ->
            when (event) {
                is LoadingEvent -> { /*显示加载中...*/
                }
                is SuccessEvent -> { /*加载完成.*/
                }
                is FailedEvent -> {
                    Timber.e(event.error)
                    toast("加载失败")
                }
            }
        })
        viewModel.articleList.observe(this, Observer {
            it?.let { ui.updateArticleData(it) }
        })
        viewModel.bannerList.observe(this, Observer {
            it?.let { ui.updateBannerList(it) }
        })

        requestHomeData(0)
    }

    fun requestHomeData(num: Int) {
        viewModel.getArticleData(num)
        viewModel.getBannerList()
    }

}

class HomeFragmentUI : AnkoComponent<HomeFragment> {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recycleView: RecyclerView
    lateinit var akAdapter: AKBaseQuickAdapter<Article, AKItemViewUI<Article>>
    lateinit var bannerView: Banner
    private var isRefresh = true

    override fun createView(ui: AnkoContext<HomeFragment>) = with(ui) {
        swipeRefreshLayout {
            swipeRefreshLayout = this
            layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)

            recycleView = recyclerView {
                layoutParams = RelativeLayout.LayoutParams(matchParent, matchParent)
                layoutManager = LinearLayoutManager(ctx)
                addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL).apply {
                    ContextCompat.getDrawable(ctx, R.drawable.base_divider_line)?.let { setDrawable(it) }
                })
            }

            initView(ui)
        }
    }

    private fun initView(ui: AnkoContext<HomeFragment>) {
        akAdapter = object : AKBaseQuickAdapter<Article, AKItemViewUI<Article>>() {
            override fun onCreateItemView() = ArticleAKItemViewUI()
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                (adapter.getItem(position) as? Article)?.let {
                    // 打开文章链接.
                }
            }
            recycleView.adapter = this
            //akAdapter.bindToRecyclerView(recycleView)
            setOnLoadMoreListener({
                isRefresh = false
                swipeRefreshLayout.isRefreshing = false
                val page = akAdapter.data.size / 20
                ui.owner.requestHomeData(page)
            }, recycleView)

            addHeaderView(BannerLayout(ui.ctx).apply {
                bannerView = banner
                onBannerListener { position ->

                }
            })
        }

        swipeRefreshLayout.onRefresh {
            isRefresh = true
            akAdapter.setEnableLoadMore(false)
            ui.owner.requestHomeData(0)
        }
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
        bannerView.update(list, list.map { it.title })
    }
}

class ArticleAKItemViewUI : AKItemViewUI<Article> {
    lateinit var tvTop: TextView
    lateinit var tvFresh: TextView
    lateinit var tvAuthor: TextView
    lateinit var tvDate: TextView
    lateinit var tvTitle: TextView
    lateinit var tvChapterName: TextView
    lateinit var ivThumbnail: ImageView
    lateinit var ivLike: ImageView

    override fun bind(item: Article) {
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
                    contentDescription = "like article"
                }.lparams(wrapContent, wrapContent)
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
                alignParentBottom()
            }
        }
    }
}