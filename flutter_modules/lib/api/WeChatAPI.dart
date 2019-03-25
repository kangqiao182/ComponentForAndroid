import 'package:dio/dio.dart';
import 'package:flutter_modules/config/ZPFlutterPlugin.dart';
import 'package:flutter_modules/core/BaseApi.dart';
import 'package:flutter_modules/model/wechat/wx_chapters.dart';
import 'package:flutter_modules/net/exceptions/ResponseException.dart';

class WeChatApi extends BaseApi{

  static const String CODE = "errorCode";
  static const String MESSAGE = "errorMsg";
  static const String DATA = "data";

  //获取公众号列表
  //http://wanandroid.com/wxarticle/chapters/json
  static const String GET_WX_CHAPTERS = "/wxarticle/chapters/json";


  WeChatApi() : super ("");


  @override
  void throwIfResponseNoSuccess(Map<String, dynamic> response) {
    if (response[CODE] != 0) {
      throw new ResponseException(response[CODE], response[MESSAGE]);
    }
  }


  Future<WXChapter> getWXChapters() async {
    Response<Map<String, dynamic>> resp = await net.get(GET_WX_CHAPTERS);
    return WXChapter.fromJson(resp.data[DATA]);
  }

}