import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modules/pages/WeChatFragment.dart';


class WeChatRouter {
  static const WECHAT = "wechat";

  static void configureRoutes(Router router) {
    router.define(WECHAT, handler: weChatHandler);
  }
}

var weChatHandler = Handler(handlerFunc: (BuildContext context, Map<String, List<String>> params) {
  return WeChatFragment();
});