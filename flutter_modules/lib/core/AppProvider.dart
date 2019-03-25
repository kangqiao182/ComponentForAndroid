import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modules/core/ZPApplication.dart';


class AppProvider extends InheritedWidget {

  final ZPApplication application;

  AppProvider({Key key, Widget child, this.application})
      : super(key: key, child: child);

  bool updateShouldNotify(_) => true;

  static AppProvider of(BuildContext context) {
    return (context.inheritFromWidgetOfExactType(AppProvider) as AppProvider);
  }

  static Router getRouter(BuildContext context) {
    return (context.inheritFromWidgetOfExactType(AppProvider) as AppProvider).application.router;
  }

  static ZPApplication getApplication(BuildContext context) {
    return (context.inheritFromWidgetOfExactType(AppProvider) as AppProvider).application;
  }

}
