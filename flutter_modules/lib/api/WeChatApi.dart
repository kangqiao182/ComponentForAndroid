import 'package:dio/dio.dart';
import 'package:flutter_modules/config/ZPFlutterPlugin.dart';
import 'package:flutter_modules/core/BaseApi.dart';
import 'package:flutter_modules/model/wechat/article_response_body.dart';
import 'package:flutter_modules/model/wechat/wx_chapters.dart';
import 'package:flutter_modules/net/exceptions/ResponseException.dart';
import 'package:flutter_modules/utils/log/Log.dart';
import 'package:sprintf/sprintf.dart';

class WeChatApi extends BaseApi {
  static const String CODE = "errorCode";
  static const String MESSAGE = "errorMsg";
  static const String DATA = "data";

  //获取公众号列表
  //http://wanandroid.com/wxarticle/chapters/json
  static const String GET_WX_CHAPTERS = "wxarticle/chapters/json";

  /**
   * 知识体系下的文章
   * http://www.wanandroid.com/article/list/0/json?cid=168
   * @param page
   * @param cid
   */
  static const String GET_KNOWLEDGE_LIST = "article/list/%d/json";

  static WeChatApi _instance;

  static WeChatApi get instance => _instance;

  static Future<WeChatApi> getInstance() async {
    if (_instance == null) {
      var baseUrl = await ZPFlutterPlugin.instance.baseUrl;
      Log.info(baseUrl);
      _instance = WeChatApi._(baseUrl);
    }
    return _instance;
  }

  WeChatApi._(String baseUrl) : super(baseUrl: baseUrl);

  @override
  void throwIfResponseNoSuccess(Map<String, dynamic> response) {
    if (response[CODE] != 0) {
      throw new ResponseException(response[CODE], response[MESSAGE]);
    }
  }

  Future<List<WXChapter>> getWXChapters() async {
    Response<Map<String, dynamic>> resp = await net.get(GET_WX_CHAPTERS);
    return WXChapter.fromJsonArr(resp.data[DATA]);
  }
  
  Future<ArticleResponseBody> getKnowledgeList(int page, int cid) async {
    Response<Map<String, dynamic>> resp = await net.get(sprintf(GET_KNOWLEDGE_LIST, [page]), queryParameters: {
      "cid": cid
    });
    return ArticleResponseBody.fromJson(resp.data[DATA]);
  }
}
