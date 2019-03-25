// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'wx_chapters.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

WXChapter _$WXChapterFromJson(Map<String, dynamic> json) {
  return WXChapter()
    ..id = json['id'] as int
    ..courseId = json['courseId'] as int
    ..name = json['name'] as String
    ..order = json['order'] as int
    ..parentChapterId = json['parentChapterId'] as int
    ..userControlSetTop = json['userControlSetTop'] as bool
    ..visible = json['visible'] as int
    ..children = json['children'] as List;
}

Map<String, dynamic> _$WXChapterToJson(WXChapter instance) => <String, dynamic>{
      'id': instance.id,
      'courseId': instance.courseId,
      'name': instance.name,
      'order': instance.order,
      'parentChapterId': instance.parentChapterId,
      'userControlSetTop': instance.userControlSetTop,
      'visible': instance.visible,
      'children': instance.children
    };
