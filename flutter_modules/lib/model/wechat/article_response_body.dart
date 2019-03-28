import 'package:json_annotation/json_annotation.dart';

part 'article_response_body.g.dart';

@JsonSerializable()
class ArticleResponseBody {
  @JsonKey(name: "curPage")
  int curPage;

  @JsonKey(name: "datas")
  List<Article> datas;

  @JsonKey(name: "offset")
  int offset;

  @JsonKey(name: "over")
  bool over;

  @JsonKey(name: "pageCount")
  int pageCount;

  @JsonKey(name: "size")
  int size;

  @JsonKey(name: "total")
  int total;

  ArticleResponseBody();

  int get listLength => datas?.length ?? 0;
  
  void updateBy(ArticleResponseBody other) {
    curPage = other.curPage;
    pageCount = other.pageCount;
    if(null != datas) {
      datas.addAll(other.datas);
    } else {
      datas = other.datas;
    }
    total = datas.length;
  }

  factory ArticleResponseBody.fromJson(Map<String, dynamic> json) => _$ArticleResponseBodyFromJson(json);

  Map<String, dynamic> toJson() => _$ArticleResponseBodyToJson(this);

  @override
  String toString() {
    return 'ArticleResponseBody{curPage: $curPage, datas: $datas, offset: $offset, over: $over, pageCount: $pageCount, size: $size, total: $total}';
  }
}

@JsonSerializable()
class Article {
  @JsonKey(name: "apkLink")
  String apkLink;
  @JsonKey(name: "author")
  String author;
  @JsonKey(name: "chapterId")
  int chapterId;
  @JsonKey(name: "chapterName")
  String chapterName;
  @JsonKey(name: "collect")
  bool collect;
  @JsonKey(name: "courseId")
  int courseId;
  @JsonKey(name: "desc")
  String desc;
  @JsonKey(name: "envelopePic")
  String envelopePic;
  @JsonKey(name: "fresh")
  bool fresh;
  @JsonKey(name: "id")
  int id;
  @JsonKey(name: "link")
  String link;
  @JsonKey(name: "niceDate")
  String niceDate;
  @JsonKey(name: "origin")
  String origin;
  @JsonKey(name: "projectLink")
  String rojectLink;
  @JsonKey(name: "publishTime")
  int publishTime;
  @JsonKey(name: "superChapterId")
  int superChapterId;
  @JsonKey(name: "superChapterName")
  String superChapterName;
  @JsonKey(name: "tags")
  List<Tag> tags;
  @JsonKey(name: "title")
  String title;
  @JsonKey(name: "type")
  int type;
  @JsonKey(name: "userId")
  int userId;
  @JsonKey(name: "visible")
  int visible;
  @JsonKey(name: "zan")
  int zan;
  @JsonKey(name: "top")
  String top;

  Article();

  factory Article.fromJson(Map<String, dynamic> json) => _$ArticleFromJson(json);

  Map<String, dynamic> toJson() => _$ArticleToJson(this);

  @override
  String toString() {
    return 'Article{apkLink: $apkLink, author: $author, chapterId: $chapterId, chapterName: $chapterName, collect: $collect, courseId: $courseId, desc: $desc, envelopePic: $envelopePic, fresh: $fresh, id: $id, link: $link, niceDate: $niceDate, origin: $origin, rojectLink: $rojectLink, publishTime: $publishTime, superChapterId: $superChapterId, superChapterName: $superChapterName, tags: $tags, title: $title, type: $type, userId: $userId, visible: $visible, zan: $zan, top: $top}';
  }
}

@JsonSerializable()
class Tag {
  @JsonKey(name: "name")
  String name;
  @JsonKey(name: "url")
  String url;

  Tag();

  factory Tag.fromJson(Map<String, dynamic> json) => _$TagFromJson(json);

  Map<String, dynamic> toJson() => _$TagToJson(this);

  @override
  String toString() {
    return 'Tag{name: $name, url: $url}';
  }
}
