import 'dart:convert';
import 'dart:io';
import 'package:dio/dio.dart';
import 'package:flutter_modules/config/ZPFlutterPlugin.dart';
import 'package:flutter_modules/core/Env.dart';
import 'package:flutter_modules/net/exceptions/HttpException.dart';
import 'package:flutter_modules/utils/ToastUtil.dart';
import 'package:flutter_modules/utils/log/DioLogger.dart';
import 'package:flutter_modules/utils/log/Log.dart';
import 'package:sprintf/sprintf.dart';

abstract class BaseApi implements Interceptor {
  static const String TAG = 'API';

  Dio _dio;
  final String baseUrl;

  BaseApi({
    this.baseUrl, 
    int connectTimeout: 5000,
    int receiveTimeout: 3000,
    Iterable<Cookie> cookies,
    Map<String, dynamic> queryParameters,
    Map<String, dynamic> extra,
    Map<String, dynamic> headers,
    ResponseType responseType = ResponseType.json,
    ContentType contentType,
    ValidateStatus validateStatus,
    bool receiveDataWhenStatusError = true,
    bool followRedirects = true,
    int maxRedirects = 5,
  }) {
    _dio = Dio(BaseOptions(
        baseUrl: baseUrl,
        connectTimeout: connectTimeout,
        receiveTimeout: receiveTimeout,
        cookies: cookies,
        queryParameters: queryParameters,
        extra: extra,
        headers: headers,
        responseType: responseType,
        contentType: contentType,
        validateStatus: validateStatus,
        receiveDataWhenStatusError: receiveDataWhenStatusError,
        followRedirects: followRedirects,
        maxRedirects: maxRedirects));

    _dio.interceptors.add(this);

    if (Env.isInDebugMode) {
      _dio.interceptors.add(InterceptorsWrapper(onRequest: (RequestOptions options) async {
        DioLogger.onSend(TAG, options);
        return options;
      }, onResponse: (Response response) {
        DioLogger.onSuccess(TAG, response);
        return response;
      }, onError: (DioError error) {
        DioLogger.onError(TAG, error);
        return error;
      }));
    }
  }

  void throwIfHttpNoSuccess(Response response) {
    if (response.statusCode < 200 || response.statusCode > 299) {
      throw new HttpException(response);
    }
  }

  void throwIfResponseNoSuccess(Map<String, dynamic> response);

  @override
  onRequest(RequestOptions options) async {
    var header = await ZPFlutterPlugin.instance.httpHeader;
    //options.baseUrl = await ZPFlutterPlugin.instance.authBaseUrl;
    options.headers.addAll(header);
    return options;
  }

  @override
  onResponse(Response response) {
    //检测Http请求结果
    throwIfHttpNoSuccess(response);
    String res2Json = json.encode(response.data);
    Map<String, dynamic> map = json.decode(res2Json);
    //检测响应数据结果
    throwIfResponseNoSuccess(map);
    response.data = map;
    return response;
  }

  @override
  onError(DioError err) {
    if (err.error is HttpException || err.error is SocketException) {
      //当发生Http请求失败时, 统一toast提示用户
      Log.severe(err.toString());
      //ZPFlutterPlugin.instance.postMessage(StringJP.net_request_failed);
    }
    return err;
  }

  Dio get net => _dio;
}
