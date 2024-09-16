import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';

class UIConfig {
  //UI相关
  static double bottomHeight = MediaQueryData.fromView(View.of(Get.context!)).padding.bottom;
  static double safeTopHeight = Platform.isAndroid ? 36 : MediaQueryData.fromView(View.of(Get.context!)).padding.top;
  static double safeBottomHeight = Platform.isIOS ? ((bottomHeight == 34.0 || bottomHeight == 0.0) ? bottomHeight : bottomHeight - 49) : bottomHeight;
  static double screenWidth = MediaQueryData.fromView(View.of(Get.context!)).size.width;
  static double screenHeight = MediaQueryData.fromView(View.of(Get.context!)).size.height + (Platform.isAndroid ? 49.0 : 0);
  static double bottomNavigationBarHeight = 49.0;
  static double vmAlertBottomMarginHeight = Platform.isAndroid ? 34.0 : ((bottomHeight == 34.0 || bottomHeight == 0.0) ? bottomHeight : bottomHeight - 49); //弹窗 底部安全距离




}
  String doubleToPercentage(double value) {
    return (value * 100).ceil().toString() + "%";
}