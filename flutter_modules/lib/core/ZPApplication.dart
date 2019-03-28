import 'package:flutter_modules/api/WeChatApi.dart';
import 'package:flutter_modules/config/CommonRouter.dart';
import 'package:flutter_modules/config/WeChatRouter.dart';
import 'package:flutter_modules/core/Application.dart';
import 'package:fluro/fluro.dart';
import 'package:flutter_modules/core/Env.dart';
import 'package:flutter_modules/utils/log/Log.dart';
import 'package:logging/logging.dart';

class ZPApplication implements Application {
  static const WEB_PAGE = "/kyc/web_page";
  Router router;
  WeChatApi weChatApi;

  @override
  Future<void> onCreate() async {
    _initLog();
    _initRouter();
    await _initAPI();
  }

  @override
  Future<void> onTerminate() async {
  }

  void _initLog(){
    Log.init();

    if (Env.isInDebugMode) {
      Log.setLevel(Level.ALL);
    }
    if (Env.isInProductMode) {
      Log.setLevel(Level.INFO);
    }
  }

  void _initRouter() {
    router = new Router();
    CommonRouter.configureRoutes(router);
    WeChatRouter.configureRoutes(router);
  }

  void _initAPI() async {
    weChatApi = await WeChatApi.getInstance();
  }
}

