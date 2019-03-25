import 'dart:async';
import 'package:flutter/services.dart';
import 'package:flutter_modules/utils/Log.dart';


class ZPFlutterPlugin {
  static const MethodChannel _channel = MethodChannel('plugins.flutter.io/zp_container');

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

  Map<String, dynamic> _cachedRequestHttpHeader;
  String _cachedBaseUrl;

  Future<String> get baseUrl async => _cachedBaseUrl ??= await _channel.invokeMethod('authBaseUrl');

  Future<Map> get httpHeader async {
    if (null == _cachedRequestHttpHeader || _cachedRequestHttpHeader.isEmpty) {
      var map = await _channel.invokeMethod('httpHeader');
      Log.d("Header>>>${map}");
      _cachedRequestHttpHeader = {};
      map.forEach((key, val) {
        _cachedRequestHttpHeader[key] = val;
      });
    }
    return _cachedRequestHttpHeader;
  }

  Future<String> get token async => await _channel.invokeMethod('token');

  Future<String> get cookie async => await _channel.invokeMethod('cookie');

  Future<dynamic> route({bool isBack, String goto}) async {
    return _channel.invokeMethod("route", {'exit': isBack, 'goto': goto});
  }

  //goto 默认不退出当前Native所在页面, 仅跳转.
  Future<dynamic> goto(String goto) => route(isBack: false, goto: goto);

  //exit 退出当前Native所在页面, 可选跳转.
  Future<dynamic> exit({String goto}) => route(isBack: true, goto: goto);

  ////////////////////////////////////////////
  // 原生调用Flutter ///////////////////////////
  ////////////////////////////////////////////
  // 接收原生平台的方法调用处理
  Future<dynamic> _methodHandler(MethodCall call) async {

  }
}