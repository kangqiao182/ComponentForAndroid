package com.zp.android.api;

import android.util.Log;

import com.lzy.okgo.convert.Converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zhaopan on 2018/4/14.
 */
public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (response.code() == 404 || response.code() >= 500){
            //如果响应为404或500以上, 不予解析, 直接返回CmdException, 并设置错误码为Http响应码.
            throw new CmdException(response.code(), response.message());
        }
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        Reader reader =  body.charStream();

        //在此拦截"code, msg, data"格式的数据处理.
        //以给定具体的data类型时, 判断是否是code, msg, data 格式, 如果是直接按CmdResponse作特殊处理.
        JSONObject jResp = new JSONObject(body.string());
        if(jResp.has(CmdResponse.CODE) && jResp.has(CmdResponse.MSG)){
            CmdResponse cmdResponse = new CmdResponse(jResp.optInt(CmdResponse.CODE, -1), jResp.optString(CmdResponse.MSG));
            if(cmdResponse.isSuccess()) {
                cmdResponse.setData(JsonMappingUtil.INSTANCE.fromJson(jResp.optString(CmdResponse.DATA), rawType));
            } else {
                cmdResponse.setData(jResp.optString(CmdResponse.DATA));
            }
            return (T) cmdResponse;
        } /*else {
            CmdResponse cmdResponse = new CmdResponse();
            //直接将服务端的错误信息抛出，onError中可以获取
            //throw new IllegalStateException(StringUtil.getString(R.string.sys_invalid_response_data_that_not_convert_to_cmdresponse));
            cmdResponse.setCodeMsg(CmdResponse.RESP_FAILED_FORMAT, StringUtil.getString(R.string.sys_invalid_response_data_that_not_convert_to_cmdresponse));
            return (T) cmdResponse;
        }*/

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = JsonMappingUtil.INSTANCE.<T>fromJson(reader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        //JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = JsonMappingUtil.INSTANCE.fromJson(body.charStream(), type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        Reader reader = body.charStream();

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != CmdResponse.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = JsonMappingUtil.INSTANCE.fromJson(reader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<CmdResponse<Void>>(this)
                SimpleResponse simpleResponse = JsonMappingUtil.INSTANCE.fromJson(reader, SimpleResponse.class);
                response.close();
                //noinspection unchecked
                return (T) simpleResponse.toLzyResponse();
            } else {
                CmdResponse cmdResponse = new CmdResponse();
                //以泛型形式调用时, 已确认泛型参数类型是CmdResponse类型, 需要进行解包特殊处理.
                JSONObject jResp = new JSONObject(body.string());
                response.close();
                if(jResp.has(CmdResponse.CODE) && jResp.has(CmdResponse.MSG)){
                    cmdResponse.setCodeMsg(jResp.optInt(CmdResponse.CODE, -1), jResp.optString(CmdResponse.MSG, ""));
                    if(cmdResponse.isSuccess()) {
                        cmdResponse.setData(JsonMappingUtil.INSTANCE.fromJson(jResp.optString(CmdResponse.DATA), rawType));
                    } else {
                        cmdResponse.setData(jResp.optString(CmdResponse.DATA));
                    }
                    return (T) cmdResponse;
                } else {
                    //直接将服务端的错误信息抛出，onError中可以获取
                    //throw new IllegalStateException(StringUtil.getString(R.string.sys_invalid_response_data_that_not_convert_to_cmdresponse));
                    cmdResponse.setCodeMsg(CmdResponse.RESP_FAILED_FORMAT, ApiUtils.INSTANCE.getString(R.string.api_sys_invalid_response_data_that_not_convert_to_cmdresponse));
                    return (T) cmdResponse;
                }

                // 泛型格式如下： new JsonCallback<CmdResponse<内层JavaBean>>(this)
                //CmdResponse cmdResponse = ConvertByJackson.fromJson(reader, type);
                //response.close();
                //int code = cmdResponse.code;
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                /*if (code == 0) {
                    //noinspection unchecked
                    return (T) cmdResponse;
                } else if (code == 104) {
                    throw new IllegalStateException("用户授权信息无效");
                } else if (code == 105) {
                    throw new IllegalStateException("用户收取信息已过期");
                } else {
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + cmdResponse.msg);
                }*/
            }
        }
    }
}
