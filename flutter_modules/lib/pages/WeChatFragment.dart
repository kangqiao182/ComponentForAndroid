import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_modules/api/WeChatApi.dart';
import 'package:flutter_modules/config/ZPFlutterPlugin.dart';
import 'package:flutter_modules/model/wechat/article_response_body.dart';
import 'package:flutter_modules/model/wechat/wx_chapters.dart';
import 'package:flutter_modules/net/exceptions/ResponseException.dart';
import 'package:flutter_modules/utils/ToastUtil.dart';
import 'package:flutter_modules/utils/log/Log.dart';

class WeChatFragment extends StatefulWidget {
  @override
  _WeChatFragment createState() => _WeChatFragment();
}

class _WeChatFragment extends State<WeChatFragment> {
  List<WXChapter> wxChapterList = [];

  @override
  void initState() {
    super.initState();
    WeChatApi.instance.getWXChapters().then((list) {
      setState(() {
        if (null != list && list.isNotEmpty) {
          wxChapterList = list;
        }
      });
    }).catchError((e) {
      if (e.error is ResponseException) {
        ToastUtil.toast(context, e.error.message);
        Log.info(e.error);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: wxChapterList.isEmpty ? Container(
        color: Colors.white,
        alignment: Alignment.center,
        child: Text("无数据.", style: TextStyle(fontSize: 22, color: Colors.blue)))
      : genTabList(wxChapterList)
    );
  }

  Widget genTabList(List<WXChapter> list) {
    return new DefaultTabController(
        length: list.length,
        child: Column(children: <Widget>[
            new Material(
              color: Theme.of(context).primaryColor,
              //elevation: 4.0,
              child: SizedBox(
                height: 48.0,
                width: double.infinity,
                child: TabBar(
                  isScrollable: true,
                  //labelPadding: EdgeInsets.all(12.0),
                  indicatorSize: TabBarIndicatorSize.label,
                  tabs: list.map((chapter) {
                    return Tab(child: Text(chapter.name, style: TextStyle(fontSize: 16, color: Colors.white)));
                  }).toList(),
                ),
              ),
            ),
            new Expanded(child: TabBarView(children: list.map((chapter) {
              return WxChapterListWidget(chapter);
            }).toList()))
          ]),
        );
  }
}

class WxChapterListWidget extends StatefulWidget {
  final WXChapter chapter;

  WxChapterListWidget(this.chapter);

  @override
  _WxChapterListState createState() => _WxChapterListState();
}

class _WxChapterListState extends State<WxChapterListWidget> {
  int cid = 0;
  ArticleResponseBody body;
  List<Article> get dataList => body?.datas ?? [];

  @override
  void initState() {
    super.initState();
    cid = widget.chapter?.id ?? 0;
    WeChatApi.instance.getKnowledgeList(0, cid).then((body) {
      setState(() {
        this.body = body;
      });
    }).catchError((e) {
      if (e is DioError && e.error is ResponseException) {
        ToastUtil.toast(context, e.error.message);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView.separated(
        itemCount: dataList.length,
        separatorBuilder: (context, index) => Divider(height: 1),
        itemBuilder: (BuildContext context, int index) {
          Article article = dataList[index];
          return ListTile(
            leading: Image.network(article.envelopePic, fit: BoxFit.fitWidth),
            title: Text(article.title),
            subtitle: Text(article.chapterName),
            trailing: Text(article.niceDate),
            onTap: () {
              Log.info(">>>"+article.toString());
              ZPFlutterPlugin.instance.nativeWeb(article.link, article.title, id: article.id);
            },
          );
        },
      )
    );
  }
}
