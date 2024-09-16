import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/images/team_avatar.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/work/rules/lib/tools.dart';
import 'package:orginone/main_base.dart';
import 'package:flutter/src/widgets/basic.dart' as basic;

//动态评论
class ActivityComment extends StatelessWidget {
  late CommentType comment;
  late void Function(CommentType comment)? onTap;

  ActivityComment({required this.comment, this.onTap, super.key});

  @override
  Widget build(BuildContext context) {
    List<Widget> replyTo = [];
    if (comment.replyTo != null && comment.replyTo!.isNotEmpty) {
      replyTo
        ..add(const Text("回复 "))
        ..addAll(getUserMsgForReply(comment.replyTo!))
        ..add(const Text(" : "));
    }
    //getUserAvatar(comment.replyTo!)
    return GestureDetector(
        onTap: () => onTap?.call(comment),
        child: Container(
            alignment: Alignment.centerLeft,
            padding: const EdgeInsets.only(top: 10),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                getUserAvatar(comment.userId),
                Expanded(
                  child: basic.Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(children: [
                          getUserName(comment.userId),
                          const SizedBox(width: 5),
                          getCreateTime(comment.userId),
                        ]),
                        SizedBox(
                          height: 4.h,
                        ),
                        Wrap(
                          crossAxisAlignment: WrapCrossAlignment.center,
                          children: [
                            ...replyTo,
                            const SizedBox(height: 4),
                            Text(comment.label,
                                textAlign: TextAlign.start,
                                style: XFonts.activitListContent)
                          ],
                        ),
                        // Row(children: [
                        //   ...replyTo,
                        // ]),
                        // const SizedBox(height: 4),
                        // Text(
                        //   comment.label,
                        //   textAlign: TextAlign.start,
                        //   style: const TextStyle(
                        //       fontSize: 17,
                        //       color: XColors.black,
                        //       fontWeight: FontWeight.normal),
                        // )
                      ]),
                )
              ],
            )));
  }

  Widget getUserAvatar(String userId) {
    return Padding(
      padding: const EdgeInsets.only(right: 10),
      child: TeamAvatar(
        info: TeamTypeInfo(userId: userId),
        size: 40.w,
      ),
    );
  }

  Widget getUserName(String userId) {
    XEntity? entity = relationCtrl.user?.findMetadata<XEntity>(userId);

    return Text(entity?.name ?? "",
        style: const TextStyle(
          color: XColors.black9,
          fontSize: 14,
          fontWeight: FontWeight.normal,
        ));
  }

  Widget getCreateTime(String userId) {
    XEntity? entity = relationCtrl.user?.findMetadata<XEntity>(userId);
    return Text(showChatTime(comment.time),
        style: const TextStyle(
          color: XColors.black9,
          fontSize: 14,
          fontWeight: FontWeight.normal,
        ));
  }

  List<Widget> getUserMsgForReply(String userId) {
    XEntity? entity = relationCtrl.user?.findMetadata<XEntity>(userId);
    return [
      Padding(padding: EdgeInsets.only(left: 5.w)),
      TeamAvatar(
        info: TeamTypeInfo(userId: userId),
        size: 24.w,
      ),
      Padding(padding: EdgeInsets.only(left: 5.w)),
      Text(entity?.name ?? "",
          style: const TextStyle(
            fontWeight: FontWeight.bold,
          ))
    ];
  }
}
