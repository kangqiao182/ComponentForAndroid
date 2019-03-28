import 'package:flutter_modules/utils/StringUtil.dart';
import 'package:json_annotation/json_annotation.dart';

part 'wx_chapters.g.dart';

@JsonSerializable()
class WXChapter {
  @JsonKey(name: "id")
  int id;

  @JsonKey(name: "courseId")
  int courseId;

  @JsonKey(name: "name")
  String name;

  @JsonKey(name: "order")
  int order;

  @JsonKey(name: "parentChapterId")
  int parentChapterId;

  @JsonKey(name: "userControlSetTop")
  bool userControlSetTop;

  @JsonKey(name: "visible")
  int visible;

  @JsonKey(name: "children")
  List<String> children;

  WXChapter();

  static List<WXChapter> fromJsonArr(dynamic jsonArr) =>
      (jsonArr as List)?.map((e) => e == null ? null : WXChapter.fromJson(e as Map<String, dynamic>))?.toList();

  factory WXChapter.fromJson(Map<String, dynamic> json) => _$WXChapterFromJson(json);

  Map<String, dynamic> toJson() => _$WXChapterToJson(this);

  @override
  String toString() {
    return 'WXChapter{id: $id, courseId: $courseId, name: $name, order: $order, parentChapterId: $parentChapterId, userControlSetTop: $userControlSetTop, visible: $visible, children: $children}';
  }
}
