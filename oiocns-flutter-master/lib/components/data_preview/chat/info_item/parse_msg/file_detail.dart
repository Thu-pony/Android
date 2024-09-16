import 'dart:convert';

import 'package:background_downloader/background_downloader.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/extension/ex_database.dart';
import 'package:orginone/components/common/extension/ex_list.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/shadow/shadow_widget.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/data_preview/chat/info_item/info_item.dart';
import 'package:orginone/dart/base/model.dart' as model;

import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/file_utils.dart';

import 'base_detail.dart';
import 'image_detail.dart';

class FileDetail extends BaseDetail {
  final bool showShadow;
  late final model.FileItemShare msgBody;

  FileDetail(
      {super.key,
      this.showShadow = false,
      required super.isSelf,
      required super.message,
      super.constraints,
      super.clipBehavior = Clip.hardEdge,
      super.padding = EdgeInsets.zero,
      super.bgColor,
      super.isReply = false,
      super.chat}) {
    msgBody = model.FileItemShare.fromJson(jsonDecode(message.msgBody));
  }

  @override
  Widget build(BuildContext context) {
    String extension = msgBody.extension ?? '';
    if (imageExtension.contains(extension.toLowerCase())) {
      return ImageDetail(
        isSelf: isSelf,
        message: message,
      );
    }
    return super.build(context);
  }

  @override
  Widget body(BuildContext context) {
    /// 限制大小
    BoxConstraints boxConstraints = BoxConstraints(
        minWidth: 280.w,
        maxWidth: UIConfig.screenWidth - 110);
    String extension = msgBody.extension ?? "";
    Widget child = Container(
        constraints: boxConstraints,
        color: bgColor != null ? Colors.transparent : Colors.white,
        padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 10.h),
        child: Column(children: [
          Expanded(
            flex: 0,
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                // ImageWidget(AssetsImages.iconFile, size: 40.w),
                XImage.entityIcon(msgBody, width: 40),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        msgBody.name ?? "",
                        style: XFonts.chatSMInfo.merge(
                            const TextStyle(overflow: TextOverflow.ellipsis)),
                        maxLines: 2,
                      ),
                      Text(
                        getFileSizeString(bytes: msgBody.size ?? 0),
                        style: XFonts.size14Black9,
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
          const Divider(),
          SizedBox(
            // color: Colors.red,
            height: 20,
            child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  if (!FileUtils.isDownloadFile(extension))
                    TextButton(
                        onPressed: () {
                          _onTap(context);
                        },
                        style: ButtonStyle(
                            padding: MaterialStateProperty.all(
                                const EdgeInsets.all(0))),
                        child: const Text("在线预览")),
                  if (FileUtils.isDownloadFile(extension))
                    FutureBuilder(
                        future: FileDownloader()
                            .database
                            .recordForName(msgBody.name ?? ""),
                        initialData: null,
                        builder: (BuildContext context,
                            AsyncSnapshot<Task?> snapshot) {
                          String btnTxt = "点击下载";
                          var onPressed = () {
                            _onTap(context);
                          };
                          if (snapshot.hasData && null != snapshot.data) {
                            btnTxt = "点击打开";
                            onPressed = () {
                              FileDownloader().openFile(task: snapshot.data);
                            };
                          }
                          return TextButton(
                              onPressed: onPressed,
                              style: ButtonStyle(
                                  padding: MaterialStateProperty.all(
                                      const EdgeInsets.all(0))),
                              child: Text(btnTxt));
                        }),
                  if (FileUtils.isWord(extension))
                    TextButton(
                        onPressed: () {
                          ToastUtils.showMsg(msg: "敬请期待！！！");
                        },
                        style: ButtonStyle(
                            padding: MaterialStateProperty.all(
                                const EdgeInsets.all(0))),
                        child: const Text("在线编辑")),
                ].addDivisions(const VerticalDivider())),
          )
        ]));

    if (showShadow) {
      child = ShadowWidget(
        child: child,
      );
    }
    return child;
  }

  // @override
  void _onTap(BuildContext context) {
    RoutePages.jumpFile(file: model.FileItemShare.fromJson(msgBody.toJson()));
  }
}
