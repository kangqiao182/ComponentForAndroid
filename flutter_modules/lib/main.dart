import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_modules/config/KycRouter.dart';
import 'package:flutter_modules/pages/kyc/KycLevel2Page.dart' show KycLevel2Page;
import 'package:flutter_modules/pages/kyc/KycLevel3Page.dart' show KycLevel3Page;

/*void main() {
  // 修改系统顶部导航栏颜色
  //https://github.com/tuozhaobing/NativeFlutter/blob/e5162f2a4568880fd218a91acb7eddedccf7dc89/my_flutter/lib/main.dart
  *//*SystemChrome.setSystemUIOverlayStyle(
      SystemUiOverlayStyle(
        statusBarColor: Colors.blue, //top bar color
        statusBarIconBrightness: Brightness.dark, //top bar icons
        systemNavigationBarColor: Colors.white, //bottom bar color
        systemNavigationBarIconBrightness: Brightness.dark, //bottom bar icons
      )
  );


  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp, DeviceOrientation.portraitDown])
      .then((_) => runApp(_widgetForRoute(window.defaultRouteName)));*//*

  runApp(_widgetForRoute(window.defaultRouteName));
}

Widget _widgetForRoute(String route) {
  switch (route) {
    case KycRouter.MAIN:
      return MyApp();
    case KycRouter.LEVEL1:
      return MyApp();
    case KycRouter.LEVEL2:
      return KycLevel2Page(title: "Level2 ^|_|^");
    default:
      return MyApp();
      //return KycLevel2Page(title: "Level2 ^_^");
      /*return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );*/
  }
}
*/

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'USA KYC',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: KycLevel3Page(title: "Level3 Page"),
      //initialRoute: KycRouter.LEVEL3,
      routes: <String, WidgetBuilder>{
        KycRouter.LEVEL2: (BuildContext context) => KycLevel2Page(title: "Level2 ^_^"),
        KycRouter.LEVEL3: (BuildContext context) => KycLevel3Page(title: "Level3 ^_^"),
      },
    );
  }
}
