import 'package:flutter/material.dart' hide ImageProvider;
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_widget_from_html/flutter_widget_from_html.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/components/common/file_upload/resource_container.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/team_avatar.dart';
import 'package:orginone/components/common/templates/gy_scaffold.dart';
import 'package:orginone/components/data_preview/chat/info_item/parse_msg/text_detail.dart';
import 'package:orginone/components/target_activity/activity_comment.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/model.dart' as model;
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/work/rules/lib/tools.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';

//渲染动态信息
// ignore: must_be_immutable
class ActivityMessageFromChat extends StatefulWidget {
  //动态元数据
  model.ActivityType metadata;

  ActivityMessageFromChat({
    super.key,
    required this.metadata,
  }) {
    metadata = metadata;
  }
  @override
  State<StatefulWidget> createState() => _ActivityMessageFromChatState();
}

class _ActivityMessageFromChatState extends State<ActivityMessageFromChat>
    with ImageProvider, UrlSpan {
  //动态元数据
  model.ActivityType get metadata => widget.metadata;

  late String _key;
  @override
  void initState() {
    super.initState();
    initData();
  }

  initData() async {
    ISession? sessionUser =
        relationCtrl.user?.findMemberChat(metadata.updateUser!);
    if (sessionUser != null) {
      IActivity iActivity = Activity(sessionUser.metadata, sessionUser);
      await iActivity.load();
    }
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Widget content = GyScaffold(
        titleWidget: Text("动态详情", style: XFonts.size24Black3),
        body: Container(
          width: ScreenUtil().screenWidth,
          height: ScreenUtil().screenHeight,
          // margin: EdgeInsets.only(top: hideResource ? 6.h : 0, left: 0, right: 0),
          color: Colors.white,
          padding: EdgeInsets.only(top: 12.w, bottom: 12.h),
          child: SingleChildScrollView(
            child: Container(
              padding: EdgeInsets.only(left: 20.w, right: 20.w),
              color: Colors.white,
              child: Column(
                children: [
                  Row(
                    children: [
                      avatar(),
                      Padding(
                          padding: EdgeInsets.only(left: 10.w),
                          child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.start,
                              children: [title(), subTitle()]))
                    ],
                  ),
                  description(context),
                ],
              ),
            ),
          ),
        ));
    return content;
  }

  //渲染标题
  Widget title() {
    XEntity? entity = null != relationCtrl.user
        ? relationCtrl.user!.findMetadata<XEntity>(metadata.createUser!)
        : null;
    return Container(
      child: Row(
        children: [
          Text(entity?.name ?? "", style: XFonts.activityListTitle),
          Padding(padding: EdgeInsets.only(left: 10.h)),
          if (metadata.tags.isNotEmpty)
            ...metadata.tags
                .map((e) => OutlinedButton(onPressed: () {}, child: Text(e)))
                .toList()
        ],
      ),
    );
  }

  Widget subTitle() {
    XEntity? entity = null != relationCtrl.user
        ? relationCtrl.user!.findMetadata<XEntity>(metadata.updateUser!)
        : null;
    return Row(
      children: [
        // TeamAvatar(
        //   info: TeamTypeInfo(userId: item.value.metadata.createUser!),
        //   size: 24.w,
        // ),
        Container(
          alignment: Alignment.centerLeft,
          // padding: EdgeInsets.only(left: 5.w),
          child: Row(
            children: [
              Text("${showChatTime(metadata.createTime!)}·${entity?.name}",
                  style: XFonts.activityListSubTitle),
            ],
          ),
        )
      ],
    );
  }

  //渲染内容
  Widget? renderContent() {
    switch (MessageType.getType(metadata.typeName)) {
      case MessageType.text:
        return getUrlSpan(metadata.content) ??
            Text(
              metadata.content,
              style:
                  const TextStyle(height: 1.5).merge(XFonts.activitListContent),
            );

      case MessageType.html:
        return HtmlWidget(
          metadata.content,
          textStyle: TextStyle(fontSize: 24.sp, height: 1.8),
          onTapUrl: (url) {
            RoutePages.jumpWeb(url: url);
            return true;
          },
          onTapImage: (url) {
            print(">>>>>>>>>$url");
          },
        );

      default:
    }
    return null;
  }

  //渲染头像
  Widget avatar() {
    return TeamAvatar(
      info: TeamTypeInfo(userId: metadata.createUser!),
      size: 35,
      radius: 4,
    );
  }

  //渲染描述
  Widget description(BuildContext context) {
    return Container(
      alignment: Alignment.centerLeft,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            alignment: Alignment.centerLeft,
            padding: EdgeInsets.only(top: 10.h, bottom: 12.h),
            child: renderContent() ?? Container(),
          ),
          if (metadata.resource.isNotEmpty)
            Container(
              margin: const EdgeInsets.only(bottom: 16),
              child: ResourceContainerWidget(metadata.resource, 100,
                  hideResource: false),
            ),
          RenderCtxMore(
            metadata: metadata,
          )
        ],
      ),
    );
  }
}

//渲染动态属性信息
// ignore: must_be_immutable
class RenderCtxMore extends StatelessWidget {
  model.ActivityType metadata;
  late XEntity? replyTo;

  RenderCtxMore({super.key, required this.metadata});

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      // renderOperate(context),
      Offstage(
        offstage: metadata.likes.isEmpty,
        child: _buildLikeBoxWidget(),
      ),
      Padding(padding: EdgeInsets.only(left: 5.w)),
      Offstage(
        offstage: metadata.comments.isEmpty,
        child: _buildCommentBoxWidget(context),
      )
    ]);
  }

  //判断是否有回复
  Future<void> handleReply(BuildContext context, [String userId = '']) async {
    replyTo = null;
    if (userId.isNotEmpty) {
      var user = await relationCtrl.user?.findEntityAsync(userId);
      replyTo = user;
    }
  }

  //渲染操作
  Widget renderOperate(BuildContext context) {
    return Container(
        child: Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            ButtonWidget.iconTextOutlined(
              onTap: () async {
                // showModalBottomSheet(
                //     context: context,
                //     builder: (context) {
                //       return MessageForward(
                //         msgBody: jsonEncode(metadata.toJson()),
                //         msgType: MessageType.activity.label,
                //         onSuccess: () {
                //           Navigator.pop(context);
                //         },
                //       );
                //     },
                //     isScrollControlled: true,
                //     isDismissible: false,
                //     useSafeArea: true,
                //     barrierColor: Colors.white);
              },
              // const ImageWidget(
              //   AssetsImages.iconMsg,
              //   size: 18,
              // ),
              XImage.localImage(XImage.forward, width: 22),
              '转发',
              textColor: XColors.black3,
            ),
            if (metadata.likes.contains(relationCtrl.user?.id))
              ButtonWidget.iconTextOutlined(
                onTap: () async {},
                // const ImageWidget(
                //   AssetsImages.iconLike,
                //   size: 18,
                //   color: Colors.red,
                // ),
                XImage.localImage(XImage.likeFill,
                    width: 22, color: Colors.red),
                '取消',
                textColor: XColors.black3,
              ),
            if (!metadata.likes.contains(relationCtrl.user?.id))
              ButtonWidget.iconTextOutlined(
                onTap: () async {},
                // const ImageWidget(
                //   AssetsImages.iconLike,
                //   size: 18,
                // ),
                XImage.localImage(XImage.likeOutline, width: 22),
                '点赞',
                textColor: XColors.black3,
              ),
            // Padding(padding: EdgeInsets.only(left: 5.w)),
            ButtonWidget.iconTextOutlined(
              onTap: () async {
                handleReply(context);
              },
              // const ImageWidget(
              //   AssetsImages.iconMsg,
              //   size: 18,
              // ),
              XImage.localImage(XImage.commentOutline, width: 22),
              '评论',
              textColor: XColors.black3,
            ),
          ],
        )
      ],
    ));
  }

  ///渲染点赞信息
  Widget _buildLikeBoxWidget() {
    return Container(
      alignment: Alignment.centerLeft,
      color: XColors.bgListItem1,
      padding: EdgeInsets.all(5.w),
      child: Wrap(
        direction: Axis.horizontal,
        crossAxisAlignment: WrapCrossAlignment.center,
        runSpacing: 5,
        spacing: 1,
        children: [
          // const ImageWidget(AssetsImages.iconLike,
          //     size: 18, color: Colors.red),
          XImage.localImage(XImage.likeFill, width: 24.w, color: Colors.red),
          for (var e in metadata.likes) ...getUserAvatar(e)
        ],
      ),
    );
  }

  ///渲染评论信息
  Widget _buildCommentBoxWidget(BuildContext context) {
    return Container(
      alignment: Alignment.centerLeft,
      color: XColors.white,
      margin: EdgeInsets.only(top: 5.w),
      padding: EdgeInsets.all(5.w),
      child: Wrap(
        direction: Axis.horizontal,
        runSpacing: 5,
        children: [
          Padding(
            padding: EdgeInsets.only(top: 5.h, bottom: 5.h),
            child: Text("全部评论 ${metadata.comments.length}",
                style: const TextStyle(
                  fontSize: 16,
                  color: XColors.black,
                  fontWeight: FontWeight.bold,
                )),
          ),
          ...metadata.comments.map((e) => ActivityComment(
              comment: e,
              onTap: (comment) => handleReply(context, comment.userId)))
        ],
      ),
    );
  }

  List<Widget> getUserAvatar(String userId) {
    XEntity? entity = relationCtrl.user?.findMetadata<XEntity>(userId);
    return [
      Padding(padding: EdgeInsets.only(left: 5.w)),
      TeamAvatar(
        info: TeamTypeInfo(userId: userId),
        size: 24.w,
      ),
      Padding(padding: EdgeInsets.only(left: 5.w)),
      Text(
        entity?.name ?? "",
      )
    ];
  }
}
