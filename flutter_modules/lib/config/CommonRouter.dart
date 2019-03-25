import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modules/config/ZPFlutterPlugin.dart';
import 'package:flutter_modules/pages/WelcomePage.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

class CommonRouter {
  static const ROOT = "/";
  static const WEB_PAGE = "web_page";

  static void configureRoutes(Router router) {
    router.notFoundHandler = new Handler(handlerFunc: (BuildContext context, Map<String, List<String>> params) {
      print('ROUTE WAS NOT FOUND !!!');
    });
    router.define(ROOT, handler: rootHandler);
    router.define(WEB_PAGE, handler: webPageHandler);
  }
}

var rootHandler = Handler(handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return WelcomePage();
});

var webPageHandler = Handler(handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  String url = params['url']?.first;
  String title = params['title']?.first;
  return WebviewScaffold(
    url: url,
    appBar: AppBar(
      title: Text(title),
      leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            if (Navigator.canPop(context)) {
              Navigator.pop(context);
            } else {
              ZPFlutterPlugin.instance.exit();
            }
          }),
    ),
  );
});
