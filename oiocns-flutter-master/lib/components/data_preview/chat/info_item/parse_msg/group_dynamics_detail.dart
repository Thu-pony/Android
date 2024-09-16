import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_widget_from_html/flutter_widget_from_html.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/image_widget.dart';
import 'package:orginone/components/common/images/team_avatar.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/data_preview/chat/info_item/parse_msg/text_detail.dart';
import 'package:orginone/components/target_activity/activity_message_from_chat.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/work/rules/lib/tools.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/file_utils.dart';
import 'package:orginone/utils/string_util.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/components/data_preview/chat/info_item/parse_msg/text_detail.dart';
import 'base_detail.dart';

///动态详情
class GroupDynamicsDetail extends BaseDetail {
  late final ActivityType msgBody;

  GroupDynamicsDetail({
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
    msgBody = ActivityType.fromJson(jsonDecode(message.msgBody));
  }

  @override
  Widget body(BuildContext context) {
    XEntity? entity = null != relationCtrl.user
        ? relationCtrl.user!.findMetadata<XEntity>(msgBody.createUser!)
        : null;

    BoxConstraints boxConstraints = BoxConstraints(
        minWidth: 280.w,
        maxWidth:UIConfig.screenWidth- 110);
    Widget child = GestureDetector(
      onTap: () {
        onTap(context);
      },
      child: Container(
          constraints: boxConstraints,
          color: Colors.transparent,
          padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 10.h),
          child:
              Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            SizedBox(
              height: 65,
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(
                      child: Container(
                    padding: const EdgeInsets.only(right: 8.0),
                    child: Text(
                      msgBody.content.trim().isEmpty
                          ? "我发布了一条动态"
                          : StringUtil.breakWord(msgBody.content
                              .replaceAll(RegExp(r'<[^>]*>|&[^;]+;'), '')),
                      style: XFonts.size22Black0,
                      maxLines: 3,
                    ),
                  )),
                  if (msgBody.resource.isNotEmpty)
                    Stack(
                      alignment: Alignment.center,
                      children: [
                        ImageWidget(
                          fit: BoxFit.cover,
                          msgBody.resource[0].poster != null &&
                                  msgBody.resource[0].poster!.isNotEmpty
                              ? shareOpenLink(msgBody.resource[0].poster)
                              : msgBody.resource[0].thumbnailUint8List,
                          radius: 8,
                          size: 60,
                        ),
                        if (FileUtils.isVideo(
                            msgBody.resource[0].extension ?? ""))
                          Positioned(
                            child:
                                XImage.localImage(XImage.videoPlay, width: 20),
                          )
                      ],
                    )
                ],
              ),
            ),
            const Divider(),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    TeamAvatar(
                      info: TeamTypeInfo(userId: msgBody.createUser!),
                      size: 22.w,
                      circular: true,
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 4.0),
                      child: Text(
                        "${entity?.name}",
                        style: XFonts.size18Black3,
                      ),
                    ),
                  ],
                ),
                // Row(
                //   mainAxisAlignment: MainAxisAlignment.spaceAround,
                //   children: [
                //     Row(
                //       children: [
                //         XImage.localImage(XImage.likeOutline, width: 20.w),
                //         Container(
                //             padding: EdgeInsets.only(left: 6.w),
                //             child: Text(
                //               "${msgBody.likes.isEmpty ? '点赞' : msgBody.likes.length}",
                //               style: XFonts.size18Black3,
                //             ))
                //       ],
                //     ),
                //     const SizedBox(width: 8),
                //     Row(
                //       children: [
                //         XImage.localImage(XImage.commentOutline, width: 20.w),
                //         Container(
                //             padding: EdgeInsets.only(left: 6.w),
                //             child: Text(
                //               "${msgBody.comments.isEmpty ? '评论' : msgBody.comments.length}",
                //               style: XFonts.size18Black3,
                //             ))
                //       ],
                //     ),
                //   ],
                // )
              ],
            ),
          ])),
    );
    return child;
  }

  @override
  void onTap(BuildContext context) async {
    //跳转动态详情

    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return ActivityMessageFromChat(
        metadata: msgBody,
      );
    }));
    //  ISession? sessionUser =
    //     relationCtrl.user?.findMemberChat(msgBody.updateUser!);
    // IActivity iActivity = Activity(sessionUser!.metadata, sessionUser);
    // // RoutePages.jumpActivityInfo(iActivity);
    // ActivityMessage imsg = ActivityMessage(msgBody, iActivity);
    // Navigator.push(context, MaterialPageRoute(builder: (context) {
    //   return ActivityMessageOne(item: imsg);
    // }));
  }

  @override
  Widget imageWidget(url) {
    // TODO: implement imageWidget
    throw UnimplementedError();
  }
}
