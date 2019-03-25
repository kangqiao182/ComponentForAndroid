package com.zp.android.base.flutter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zp.android.base.BaseActivity;
import com.zp.android.component.RouterPath;
import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

/**
 * Created by zhaopan on 2019/1/11.
 */
@Route(path = RouterPath.Flutter.VIEW, name = "Flutter模块的入口, 使用时, 需要指定route参数.")
public class ZPFlutterActivity extends BaseActivity {

    //建议使用ARouter方式调用, 统一管理.
    @Deprecated
    public static void open(Activity context, String route) {
        Intent intent = new Intent(context, ZPFlutterActivity.class);
        intent.putExtra(RouterPath.Flutter.PARAM.ROUTE, route);
        context.startActivity(intent);
    }

    public static void open(String route) {
        ARouter.getInstance().build(RouterPath.Flutter.VIEW)
                .withString(RouterPath.Flutter.PARAM.ROUTE, route)
                .navigation();
    }

    @Autowired(name = RouterPath.Flutter.PARAM.ROUTE)
    public String route = "/";
    private FlutterView flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        if (TextUtils.isEmpty(route)) {
            //如果未指定路由, 跳转至首页RouterPath.Flutter.Router.VIEW
            route = getIntent().getExtras().getString(RouterPath.Flutter.PARAM.ROUTE, RouterPath.Flutter.Router.MAIN);
        }
        FrameLayout rootView = new FrameLayout(this);
        setContentView(rootView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        flutterView = Flutter.createView(this, getLifecycle(), route);
        ZPFlutterPlugin.registerWith(flutterView.getPluginRegistry().registrarFor(ZPFlutterPlugin.class.getName()));
        rootView.addView(flutterView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flutterView.getPluginRegistry().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != flutterView) {
            flutterView = null;
        }
    }
}
