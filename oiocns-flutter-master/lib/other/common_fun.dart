import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/controller/index.dart';

///注销账户
List<Widget> logoutOpration() {
  return [
    GestureDetector(
      onTap: () {
        showCupertinoDialog(context: Get.context!, builder: buildConfirmDialog);
      },
      child: Text(
        "注销账户",
        style: TextStyle(color: XColors.themeColor, fontSize: 20.sp),
      ),
    )
  ];
}

Widget buildConfirmDialogCommon(BuildContext context, Function confirmFun,
    {String title = "确认删除？", String content = "", String confirmTxt = "确认"}) {
  return CupertinoAlertDialog(
    title: Text(title),
    content: Text(content),
    actions: <Widget>[
      CupertinoDialogAction(
        child: const Text('取消'),
        onPressed: () {
          Navigator.pop(context);
        },
      ),
      CupertinoDialogAction(
        child: Text(confirmTxt),
        onPressed: () {
          confirmFun();
        },
      ),
    ],
  );
}

Widget buildConfirmDialog(BuildContext context) {
  return CupertinoAlertDialog(
    title: const Text("确认注销账户？"),
    content: const Text(
        // "进行自我删除用户（注销用户）操作，请点击确定前往https://asset.orginone.cn进行注销操作。"
        "您正在进行高危操作: 账号注销（删除用户）;账号注销后,所有信息将会销毁且无法再找回数据;\r\n\r\n请谨慎操作！！！"),
    actions: <Widget>[
      CupertinoDialogAction(
        child: const Text('取消'),
        onPressed: () {
          Navigator.pop(context);
        },
      ),
      CupertinoDialogAction(
        child: const Text('确定注销'),
        onPressed: () async {
          IndexController relationCtrl = Get.find<IndexController>();
          relationCtrl.user?.delete(notity: true);
          relationCtrl.exitLogin();
          // Navigator.pop(context);
          // String url = "https://asset.orginone.cn";
          // final uri = Uri.parse(url);
          // if (await canLaunchUrl(uri)) {
          //   await launchUrl(uri, mode: LaunchMode.externalApplication);
          // }
          // RoutePages.jumpWeb(url: "https://asset.orginone.cn");
        },
      ),
    ],
  );
}
