import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/data_preview/entity_info/entity_info_page.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/utils/string_util.dart';
import 'package:orginone/config/theme/unified_style.dart';

import 'base_detail.dart';

///名片详情
class BusinessCardDetail extends BaseDetail {
  late final XTarget msgBody;

  BusinessCardDetail({
    super.key,
    required super.isSelf,
    required super.message,
    super.clipBehavior = Clip.hardEdge,
    super.padding = const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
    super.bgColor,
    super.constraints,
    super.isReply = false,
    super.chat,
  }) {
    msgBody = XTarget.fromJson(jsonDecode(message.msgBody));
  }

  @override
  Widget body(BuildContext context) {
    BoxConstraints boxConstraints = BoxConstraints(
        minWidth: 280.w,
        maxWidth: UIConfig.screenWidth - 110);
    Widget child = GestureDetector(
      onTap: () {
        onTap(context);
      },
      child: Container(
          constraints: boxConstraints,
          color: Colors.transparent,
          padding: EdgeInsets.symmetric(
            horizontal: 10.w,
          ),
          child:
              Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Container(
              margin: EdgeInsets.symmetric(vertical: 10.h),
              height: 60.w,
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  XImage.entityIcon(msgBody,
                      size: Size(60.w, 60.w), radius: 5.w),
                  // ImageWidget(avatarThumbnail(),
                  //     size: 60.w, fit: BoxFit.fill, radius: 5.w),
                  Expanded(
                    child: Container(
                        height: 60.w,
                        padding: const EdgeInsets.only(left: 8.0, right: 8),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              StringUtil.breakWord(msgBody.name!),
                              style: XFonts.size22Black0,
                              maxLines: 1,
                            ),
                            Text(
                              StringUtil.breakWord(msgBody.remark ?? ""),
                              style: XFonts.size16Black6,
                              overflow: TextOverflow.ellipsis,
                              maxLines: 1,
                            ),
                          ],
                        )),
                  ),
                ],
              ),
            ),
            const Divider(),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(left: 4.0),
                      child: Text(
                        "${msgBody.typeName}名片",
                        style: XFonts.size18Black3,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ])),
    );
    return child;
  }

  Uint8List? avatarThumbnail() {
    try {
      var map = jsonDecode(msgBody.icon ?? "");
      FileItemShare share = FileItemShare.fromJson(map);
      return share.thumbnailUint8List;
    } catch (e) {
      return null;
    }
  }

  @override
  void onTap(BuildContext context) async {
    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return EntityInfoPage(
        data: msgBody,
        isFromShare: true,
      );
    }));
    // Get.toNamed(
    //   Routers.shareQrCode,
    //   arguments: {"entity": msgBody},
    // );
  }
}
