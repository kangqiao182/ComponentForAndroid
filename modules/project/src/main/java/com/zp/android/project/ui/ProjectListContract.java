package com.zp.android.project.ui;

import com.zp.android.base.mvp.BasePresenter;
import com.zp.android.base.mvp.BaseView;
import com.zp.android.project.ArticleResponseBody;

/**
 * Created by zhaopan on 2018/11/17.
 */
public interface ProjectListContract {

    interface View extends BaseView<Presenter> {
        void showProjectList(ArticleResponseBody body);
    }

    interface Presenter extends BasePresenter<View> {
        void getProjectList(int page, int cid);
    }
}
