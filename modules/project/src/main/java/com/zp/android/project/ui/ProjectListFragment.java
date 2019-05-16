package com.zp.android.project.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zp.android.base.BaseFragment;
import com.zp.android.base.CtxUtil;
import com.zp.android.base.ui.WebActivity;
import com.zp.android.common.ContextExKt;
import com.zp.android.common.DBViewHolder;
import com.zp.android.common.widget.SpaceItemDecoration;
import com.zp.android.component.RouterPath;
import com.zp.android.component.ServiceManager;
import com.zp.android.net.NetUtils;
import com.zp.android.project.Article;
import com.zp.android.project.ArticleResponseBody;
import com.zp.android.project.BR;
import com.zp.android.project.R;
import me.yokeyword.fragmentation.SupportFragment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.Lazy;
import org.koin.java.KoinJavaComponent;

/**
 * Created by zhaopan on 2018/11/17.
 * 采用Java的形式.
 */

@Route(path = RouterPath.Project.PROJECT_LIST, name = "项目列表显示页")
public class ProjectListFragment extends BaseFragment implements ProjectListContract.View {

    public static SupportFragment newInstance(int cid) {
        Bundle arguments = new Bundle();
        arguments.putInt(RouterPath.Project.PARAM.CID, cid);
        SupportFragment fragment = new ProjectListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Autowired(name = RouterPath.Project.PARAM.CID)
    public int cid = -1;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    BaseQuickAdapter<Article, DBViewHolder> adapter;
    Lazy<ProjectListContract.Presenter> presenter = KoinJavaComponent.inject(ProjectListContract.Presenter.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        return inflater.inflate(R.layout.project_fragment_refresh_layout, container, false);
    }

    @Override
    public void initView(View rootView) {
        //容错保护
        if (cid == -1) cid = getArguments().getInt(RouterPath.Project.PARAM.CID, -1);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.setEnableLoadMore(false);
            getPresenter().getProjectList(1, cid);
        });

        adapter = new BaseQuickAdapter<Article, DBViewHolder>(R.layout.project_item_project_list) {
            @Override
            protected void convert(DBViewHolder holder, Article item) {
                holder.bindTo(BR.item, item);
                holder.addOnClickListener(R.id.item_project_list_like_iv);
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Article item = (Article) adapter.getItem(position);
            if (null != item) {
                WebActivity.open(item.getLink(), item.getTitle(), item.getId());
            }
        });
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Article item = (Article) adapter.getItem(position);
            if (null != item) {
                if (view.getId() == R.id.item_project_list_like_iv) {
                    if (ServiceManager.getUserService().isLogin()) {
                        if (!NetUtils.isNetworkAvailable(CtxUtil.context())) {
                            ContextExKt.snackBarToast(recyclerView, CtxUtil.getString(R.string.no_network));
                            return;
                        }
                        boolean collect = !item.getCollect();
                        ServiceManager.getUserService().collectOrCancelArticle(item.getId(), collect, result -> {
                            if (result.isOk()) {
                                item.setCollect(collect);
                                adapter.setData(position, item); //刷新当前ItemView.
                            }
                            CtxUtil.showToast(result.getData());
                        });
                    } else {
                        ARouter.getInstance().build(RouterPath.User.LOGIN).navigation();
                        CtxUtil.showToast(R.string.login_tint);
                    }
                }
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(_mActivity));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            int page = adapter.getData().size() / 20;
            getPresenter().getProjectList(page, cid);
        }, recyclerView);

        adapter.setEmptyView(R.layout.fragment_view_empty);

        getPresenter().getProjectList(1, cid);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().subscribe(this);
    }

    @Override
    public void onStop() {
        getPresenter().unSubscribe();
        super.onStop();
    }

    @NotNull
    @Override
    public ProjectListContract.Presenter getPresenter() {
        return presenter.getValue();
    }

    @Override
    public void showError(@NotNull Throwable error) {

    }

    @Override
    public void showProjectList(ArticleResponseBody body) {
        List<Article> list;
        if (null != body && null != (list = body.getDatas())) {
            if (swipeRefreshLayout.isRefreshing()) {
                adapter.replaceData(list);
            } else {
                adapter.addData(list);
            }

            if (list.size() < body.getSize()) {
                //如果返回数据小于每页总数, 说明没有新数据了, 不需要上拉加载了.
                adapter.loadMoreEnd(true/*swipeRefreshLayout.isRefreshing()*/);
            } else {
                //可能还有新数据, 加载完成.
                adapter.loadMoreComplete();
            }
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false); //结束刷新状态.
            adapter.setEnableLoadMore(true);    //允许加载更多.
        }
    }

}
