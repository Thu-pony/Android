import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/dart/base/storages/storage.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionUtil {
  static final Map<Permission, String> permissionNameMap = {
    Permission.photos: "相册",
    Permission.camera: "相机",
    Permission.microphone: "录音",
    Permission.storage: "存储"
  };
  static final Map<Permission, Permission> permissionAndroidMap = {
    Permission.photos: Permission.storage
  };

  static showPermissionDialog(BuildContext context, Permission permission,
      {Function? callback, bool isShowTip = true}) async {
    if (kIsWeb) {
      await callback?.call();
      return;
    }
    if (Platform.isAndroid) {
      var androidPer = permissionAndroidMap[permission];
      if (androidPer != null) {
        permission = androidPer;
      }
    }
    PermissionStatus status = await permission.status;
    if (status.isDenied) {
      status = await permission.request();
    }
    var name = permissionNameMap[permission];
    bool isShowDialog =
        Storage.getBool("permission_${permission.value}", def: true);
    if (status.isGranted || status.isLimited) {
      await callback?.call();
    } else if (isShowDialog) {
      Storage.setBool("permission_${permission.value}", false);
      String title = '您需要授予$name权限';
      String content = '"请转到您的手机设置打开相应$name的权限"';

      showCupertinoDialog(
        context: context,
        builder: (context) {
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
                child: const Text('前去设置'),
                onPressed: () {
                  Navigator.pop(context);
                  openAppSettings();
                },
              ),
            ],
          );
        },
      );
    } else if (isShowTip) {
      ToastUtils.showMsg(msg: "您需要授予$name权限");
    }
  }
}
