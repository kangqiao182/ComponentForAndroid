package com.zp.android.project.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zp.android.base.BaseFragment;
import com.zp.android.component.RouterPath;
import com.zp.android.project.Article;
import me.yokeyword.fragmentation.SupportFragment;
import org.jetbrains.annotations.NotNull;

import static org.koin.java.standalone.KoinJavaComponent.*;

import java.util.List;

/**
 * Created by zhaopan on 2018/11/17.
 * 采用Java的形式.
 */

@Route(path = RouterPath.Project.PROJECT_LIST, name = "项目列表显示页")
public class ProjectListFragment extends BaseFragment implements ProjectListContract.View {

    public static final String PARAM_CID = RouterPath.Project.PARAM.CID;

    public static SupportFragment newInstance(int cid) {
        Bundle arguments = new Bundle();
        arguments.putInt(PARAM_CID, cid);
        SupportFragment fragment = new ProjectListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static SupportFragment getInstance(int cid) {
        return (SupportFragment) ARouter.getInstance().build(RouterPath.Project.PROJECT_LIST)
                .withInt(RouterPath.Project.PARAM.CID, cid)
                .navigation();
    }

    @Autowired(name = RouterPath.Project.PARAM.CID)
    public int cid = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        if(cid != -1) cid = getArguments().getInt(PARAM_CID, -1);

    }

    @Override
    public void showIsLoading() {

    }

    @Override
    public void showProjectList(List<Article> list) {

    }

    @NotNull
    @Override
    public ProjectListContract.Presenter getPresenter() {
        return get(ProjectListPresenter.class);
    }

    @Override
    public void showError(@NotNull Throwable error) {

    }
}
