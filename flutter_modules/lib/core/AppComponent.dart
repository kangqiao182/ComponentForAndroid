import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_modules/core/AppProvider.dart';
import 'package:flutter_modules/core/Env.dart';
import 'package:flutter_modules/core/ZPApplication.dart';
import 'package:flutter_modules/utils/log/Log.dart';

class AppComponent extends StatefulWidget {
  final ZPApplication _application;

  AppComponent(this._application);

  @override
  State createState() => AppComponentState(_application);
}

class AppComponentState extends State<AppComponent> {
  final ZPApplication _application;

  AppComponentState(this._application);

  @override
  void dispose() async {
    Log.info('dispose');
    await _application.onTerminate();
  }

  @override
  Widget build(BuildContext context) {
    final app = new MaterialApp(
      title: Env.value.appName,
      theme: new ThemeData(
        splashColor: Color.fromRGBO(255, 255, 255, 0),
        highlightColor: Color.fromRGBO(255, 255, 255, 0),
        textTheme: new TextTheme(
            title: new TextStyle(fontFamily: 'HanSansMedium'),
            button: new TextStyle(fontFamily: 'HanSansRegular'),
            subtitle: new TextStyle(fontFamily: 'HanSansMedium'),
            caption: new TextStyle(fontFamily: 'HanSansRegular'),
            body2: new TextStyle(fontFamily: 'HanSansRegular'),
            body1: new TextStyle(fontFamily: 'HanSansRegular')),
        primaryTextTheme: new TextTheme(title: new TextStyle(fontFamily: 'HanSansRegular')),
        accentTextTheme: new TextTheme(title: new TextStyle(fontFamily: 'HanSansRegular')),
        primarySwatch: Colors.blue,
      ),
      initialRoute: window.defaultRouteName,
      onGenerateRoute: _application.router.generator,
    );

    Log.info('initial core.route = ${app.initialRoute}');

    final appProvider = AppProvider(child: app, application: _application);
    return appProvider;
  }
}
