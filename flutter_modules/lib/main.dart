import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_modules/widgets/home.dart';

void main() => runApp(_widgetForRoute(window.defaultRouteName));

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'route1':
      return HomePage(title:"首页");
    case 'route2':
      return HomePage(title:"Route2");
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}