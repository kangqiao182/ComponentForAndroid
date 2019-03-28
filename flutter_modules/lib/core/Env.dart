import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_modules/core/AppComponent.dart';
import 'package:flutter_modules/core/ZPApplication.dart';

class Env {
  static Env value;
  static bool isRunAlone = true;

  String appName;
  String baseUrl;
  Color primarySwatch;

  // Database Config
  int dbVersion = 1;
  String dbName = 'zp_flutter.db';

  Env() {
    value = this;
    isRunAlone = window.defaultRouteName == "/";
    _init();
  }

  void _init() async{
    if(isInDebugMode){
      //todo Debug模式下的配置
    }

    var application = ZPApplication();
    await application.onCreate();
    runApp(AppComponent(application));
  }

  //编译运行模式
  static bool get isInDebugMode {
    bool inDebugMode = false;
    assert(inDebugMode = true);
    return inDebugMode;
  }

  //Release运行模式
  static const bool isInProductMode = const bool.fromEnvironment("dart.vm.product");
}