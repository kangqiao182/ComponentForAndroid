package com.zp.android.project.ui;

import com.zp.android.base.mvp.BasePresenter;
import com.zp.android.base.mvp.BaseView;
import com.zp.android.project.Article;

import java.util.List;

/**
 * Created by zhaopan on 2018/11/17.
 */
public interface ProjectListContract {

    interface View extends BaseView<Presenter> {
        void showIsLoading();
        void showProjectList(List<Article> list);
    }

    interface Presenter extends BasePresenter<View> {
        void getProjectList(int page, int cid);
    }
}
