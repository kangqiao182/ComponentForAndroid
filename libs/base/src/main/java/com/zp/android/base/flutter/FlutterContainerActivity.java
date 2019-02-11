package com.zp.android.base.flutter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zp.android.base.BaseActivity;
import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

/**
 * Created by zhaopan on 2019/1/11.
 */
public class FlutterContainerActivity extends BaseActivity {
    public static final String PARAM_KEY_ROUTE = "param_key_route";

    public static void open(Activity context, String route) {
        Intent intent = new Intent(context, FlutterContainerActivity.class);
        intent.putExtra(PARAM_KEY_ROUTE, route);
        context.startActivity(intent);
    }

    private FlutterView flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String route = getIntent().getStringExtra(PARAM_KEY_ROUTE);
        FrameLayout rootView = new FrameLayout(this);
        setContentView(rootView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        flutterView = Flutter.createView(this, getLifecycle(), route);
        //FlutterExtensionPlugin.registerWith(flutterView.getPluginRegistry().registrarFor(FlutterExtensionPlugin.class.getName()));
        //FlutterExceptionHandlerPlugin.registerWith(flutterView.getPluginRegistry().registrarFor(FlutterExceptionHandlerPlugin.class.getName()));
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
