import 'package:flutter/material.dart';
import 'package:flutter_modules/res/ColorRes.dart';
import 'package:toast/toast.dart';

class ToastUtil {
  static void toast(BuildContext context, String message) {
    Toast.show(message, context,
        backgroundColor: Color(ColorRes.global_toast_bg_color),
        textColor: Colors.white,
        backgroundRadius: 2);
  }
}