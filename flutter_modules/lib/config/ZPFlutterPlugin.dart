import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:flutter_modules/config/NativeRouter.dart';
import 'package:flutter_modules/core/Env.dart';
import 'package:flutter_modules/utils/log/Log.dart';


class ZPFlutterPlugin {
  static const MethodChannel _channel = MethodChannel('plugins.flutter.io.zp_container');
  static const METHOD_BASE_URL      = "baseUrl";
  static const METHOD_HTTP_HEADER   = "httpHeader";
  static const METHOD_TOKEN         = "token";
  static const METHOD_COOKIE        = "cookie";
  static const METHOD_ROUTE         = "route";

  static ZPFlutterPlugin get instance => _getInstance();
  static ZPFlutterPlugin _instance;

  static ZPFlutterPlugin _getInstance() {
    if (_instance == null) {
      _instance = new ZPFlutterPlugin._internal();
    }
    return _instance;
  }

  factory ZPFlutterPlugin() => _getInstance();

  ZPFlutterPlugin._internal() {
    _channel.setMethodCallHandler(_methodHandler);
  }

  ////////////////////////////////////////////
  // Flutter调用原生 ///////////////////////////
  ////////////////////////////////////////////

  Map<String, dynamic> _cachedRequestHttpHeader = {"Content-type": "application/json; charset=utf-8",};
  String _cachedBaseUrl = "https://www.wanandroid.com/";

  Future<String> get baseUrl async => _cachedBaseUrl ??= await _channel.invokeMethod('baseUrl');

  Future<Map> get httpHeader async {
    if (null == _cachedRequestHttpHeader || _cachedRequestHttpHeader.isEmpty) {
      var map = await _channel.invokeMethod('httpHeader');
      Log.info("Header>>>${map}");
      _cachedRequestHttpHeader = {};
      map?.forEach((key, val) {
        _cachedRequestHttpHeader[key] = val;
      });
    }
    return _cachedRequestHttpHeader;
  }

  Future<String> get token async => await _channel.invokeMethod('token');

  Future<String> get cookie async => await _channel.invokeMethod('cookie');

  Future<dynamic> route(bool exit, String goto, String param) async {
    return _channel.invokeMethod("route", {'exit': exit, 'goto': goto, 'param': param});
  }

  //goto 默认不退出当前Native所在页面, 仅跳转.
  Future<dynamic> goto(String goto, {bool isExit: false, String param}) => route(isExit, goto, param);

  //exit 退出当前Native所在页面, 可选跳转.
  Future<dynamic> exit({String goto, String param}) => route(true, goto, param);

  //用Native的WebView打开网页
  Future<dynamic> nativeWeb(String url, String title, {int id, bool isExit: false}) {
    var param = json.encode({"url": url, "title": title, "id": id});
    return route(isExit, NativeRouter.WEB, param);
  }

  ////////////////////////////////////////////
  // 原生调用Flutter ///////////////////////////
  ////////////////////////////////////////////
  // 接收原生平台的方法调用处理
  Future<dynamic> _methodHandler(MethodCall call) async {

  }
}