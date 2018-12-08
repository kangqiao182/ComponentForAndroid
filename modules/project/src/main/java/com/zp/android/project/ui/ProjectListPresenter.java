package com.zp.android.project.ui;

import com.zp.android.base.mvp.JavaPresenter;
import com.zp.android.base.utils.RxUtil;
import com.zp.android.project.ServerAPI;
import io.reactivex.functions.Action;
import kotlin.Lazy;
import org.koin.java.standalone.KoinJavaComponent;


/**
 * Created by zhaopan on 2018/11/17.
 */
public class ProjectListPresenter extends JavaPresenter<ProjectListContract.View> implements ProjectListContract.Presenter {

    Lazy<ServerAPI> serverAPI = KoinJavaComponent.inject(ServerAPI.class);

    public ProjectListPresenter() {
    }

    @Override
    public void getProjectList(int page, int cid) {
        launch(serverAPI.getValue().getProjectList(page, cid)
                //.doOnSubscribe(disposable -> launch(disposable))
                .compose(RxUtil.INSTANCE.applySchedulersToObservable())
                .subscribe(result -> {
                    if (null != getView()) {
                        if (result.isSuccess()) {
                            getView().showProjectList(result.getData().getDatas());
                        } else {
                            getView().showProjectList(null);
                        }
                    }
                }, throwable -> {
                    if (null != view) view.showError(throwable);
                }));
    }
}
