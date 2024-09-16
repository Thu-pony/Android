import 'dart:convert';

import 'package:flutter/material.dart' hide ImageProvider;
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_widget_from_html/flutter_widget_from_html.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/components/common/file_upload/resource_container.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/team_avatar.dart';
import 'package:orginone/components/data_preview/chat/chat_box/message_forward.dart';
import 'package:orginone/components/data_preview/chat/info_item/parse_msg/text_detail.dart';
import 'package:orginone/components/target_activity/list_item_meta.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/model.dart' as model;
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/work/rules/lib/tools.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';

import 'activity_comment.dart';
import 'activity_comment_box.dart';

//渲染动态信息
class ActivityMessageWidget extends StatefulWidget {
  //动态消息
  late Rx<IActivityMessage> item;
  late int currIndex;
  RxBool isDelete = false.obs;
  //动态
  IActivity activity;
  //动态元数据
  model.ActivityType? metadata;
  //隐藏点赞信息和回复信息，只显示统计数量
  bool hideResource;
  ActivityMessageWidget(
      {super.key,
      IActivityMessage? item,
      this.currIndex = -1,
      required this.activity,
      this.hideResource = false}) {
    if (currIndex >= 0 && activity.activityList.length > currIndex) {
      this.item = activity.activityList[currIndex].obs;
    } else if (null != item) {
      this.item = item.obs;
    }
    metadata = this.item.value.metadata;
    //订阅数据变更
    // this.item.value.unsubscribe();
    // this.item.value.subscribe((key, args) {
    //   this.item.refresh();
    // }, false);
  }
  @override
  State<StatefulWidget> createState() => _ActivityMessageState();
}

class _ActivityMessageState extends State<ActivityMessageWidget>
    with ImageProvider, UrlSpan {
  Rx<IActivityMessage> get item => widget.item;
  bool get hideResource => widget.hideResource;
  RxBool get isDelete => widget.isDelete;
  IActivity get activity => widget.activity;
  int get currIndex => widget.currIndex;
  //动态元数据
  model.ActivityType? get metadata => widget.metadata;

  late String _key;
  @override
  void initState() {
    super.initState();
    //订阅数据变更
    item.value.unsubscribe();
    _key = item.value.subscribe((key, args) {
      if (mounted) {
        setState(() {});
      }
    }, false);
  }

  @override
  void dispose() {
    super.dispose();
    item.value.unsubscribe(_key);
  }

  @override
  Widget build(BuildContext context) {
    Widget content = Container(
      // margin: EdgeInsets.only(top: hideResource ? 6.h : 0, left: 0, right: 0),
      color: Colors.white,
      padding: EdgeInsets.only(top: 12.h, bottom: 12.h),
      child: Offstage(
        offstage: isDelete.value,
        child: ListItemMetaWidget(
          title: title(),
          subTitle: subTitle(),
          avatar: avatar(),
          description: description(context),
          onTap: hideResource
              ? () {
                  activity.currIndex = currIndex;
                  RoutePages.jumpActivityInfo(activity);

                  // Get.toNamed(
                  //   Routers.targetActivity,
                  //   arguments: activity,
                  // );
                }
              : null,
        ),
      ),
    );
    // if (!hideResource) {
    //   content = EasyRefresh(
    //       // controller: state.refreshController,
    //       onRefresh: _onRefresh,
    //       onLoad: _onLoadMore,
    //       header: const MaterialHeader(),
    //       footer: const MaterialFooter(),
    //       child: RefreshIndicator(onRefresh: _onRefresh, child: content));
    // }
    if (!hideResource) {
      content = NotificationListener<ScrollNotification>(
        onNotification: (notification) {
          if (notification is ScrollStartNotification) {
            // 开始滚动
          } else if (notification is ScrollUpdateNotification) {
            // 正在滚动。。。总滚动距离：${notification.metrics.maxScrollExtent}"
          } else if (notification is ScrollEndNotification) {
            // activity.load();
            // "停止滚动"
          }
          return true;
        },
        child: content,
      );
    }
    return content;
  }

  //渲染标题
  Widget title() {
    return Container(
      child: Row(
        children: [
          Text(item.value.activity.metadata.name!,
              style: XFonts.activityListTitle),
          Padding(padding: EdgeInsets.only(left: 10.h)),
          if (metadata?.tags.isNotEmpty ?? false)
            ...metadata!.tags
                .map((e) => OutlinedButton(onPressed: () {}, child: Text(e)))
                .toList()
        ],
      ),
    );
  }

  Widget subTitle() {
    XEntity? entity = null != relationCtrl.user
        ? relationCtrl.user!
            .findMetadata<XEntity>(item.value.metadata.createUser!)
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
              Text(
                  "${showChatTime(item.value.metadata.createTime!)}·${entity?.name}",
                  style: XFonts.activityListSubTitle),
            ],
          ),
        )
      ],
    );
  }

  //渲染内容
  Widget? renderContent() {
    switch (MessageType.getType(metadata!.typeName)) {
      case MessageType.text:
        if (hideResource) {
          return Text(metadata!.content,
              style: XFonts.activitListContent,
              maxLines: 3,
              overflow: TextOverflow.ellipsis);
        } else {
          return getUrlSpan(metadata!.content) ??
              Text(
                metadata!.content,
                style: const TextStyle(height: 1.5)
                    .merge(XFonts.activitListContent),
              );
        }
      case MessageType.html:
        if (hideResource) {
          return (Offstage(
            offstage: !hideResource,
            child: Text(parseHtmlToText(metadata!.content),
                style: const TextStyle(height: 1.5)
                    .merge(XFonts.activitListContent),
                maxLines: 3,
                overflow: TextOverflow.ellipsis),
          ));
        } else {
          return HtmlWidget(
            metadata!.content,
            textStyle: TextStyle(fontSize: 24.sp, height: 1.8),
            onTapUrl: (url) {
              RoutePages.jumpWeb(url: url);
              return true;
            },
            onTapImage: (url) {
              print(">>>>>>>>>$url");
            },
          );
        }
      default:
    }
    return null;
  }

  //渲染头像
  Widget avatar() {
    // return CircleAvatar(
    //   backgroundImage: AssetImage(activity.typeName),
    // );
    return XImage.entityIcon(item.value.activity.metadata,
        width: 35, radius: 4);
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
          if (null != metadata && metadata!.resource.isNotEmpty)
            Container(
              margin: const EdgeInsets.only(bottom: 16),
              child: ResourceContainerWidget(metadata!.resource, 100,
                  hideResource: hideResource),
            ),
          RenderCtxMore(
            activity: activity,
            item: item,
            hideResource: hideResource,
            isDelete: isDelete,
          )
        ],
      ),
    );
  }
}

//渲染动态属性信息
class RenderCtxMore extends StatelessWidget {
  Rx<IActivityMessage> item;
  IActivity activity;
  RxBool isDelete;
  bool hideResource;
  late XEntity? replyTo;
  bool commenting = false;

  RenderCtxMore(
      {super.key,
      required this.activity,
      required this.item,
      required this.hideResource,
      required this.isDelete});

  @override
  Widget build(BuildContext context) {
    if (hideResource) {
      return renderTags();
    }
    return Column(children: [
      renderOperate(context),
      Offstage(
        offstage: item.value.metadata.likes.isEmpty &&
            item.value.metadata.comments.isEmpty,
        child: _buildLikeBoxWidget(),
      ),
      Padding(padding: EdgeInsets.only(left: 5.w)),
      Offstage(
        offstage: item.value.metadata.comments.isEmpty,
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
    // ignore: use_build_context_synchronously
    ShowCommentBoxNotification((text) async {
      return await item.value.comment(text, replyTo: replyTo?.id);
    },
            getTipInfo: replyTo != null
                ? () {
                    return "回复${replyTo?.name}：";
                  }
                : null)
        .dispatch(context);
  }

  //渲染操作
  Widget renderOperate(BuildContext context) {
    return Container(child: Obx(() {
      return Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              ButtonWidget.iconTextOutlined(
                onTap: () async {
                  showModalBottomSheet(
                      context: context,
                      builder: (context) {
                        return MessageForward(
                          msgBody: jsonEncode(item.value.metadata.toJson()),
                          msgType: MessageType.activity.label,
                          onSuccess: () {
                            Navigator.pop(context);
                          },
                        );
                      },
                      isScrollControlled: true,
                      isDismissible: false,
                      useSafeArea: true,
                      barrierColor: Colors.white);
                },
                // const ImageWidget(
                //   AssetsImages.iconMsg,
                //   size: 18,
                // ),
                XImage.localImage(XImage.forward, width: 22),
                '转发',
                textColor: XColors.black3,
              ),
              if (item.value.metadata.likes.contains(relationCtrl.user?.id))
                ButtonWidget.iconTextOutlined(
                  onTap: () async {
                    await item.value.like();
                  },
                  XImage.localImage(XImage.likeFill,
                      width: 22, color: Colors.red),
                  '取消',
                  textColor: XColors.black3,
                ),
              if (!item.value.metadata.likes.contains(relationCtrl.user?.id))
                ButtonWidget.iconTextOutlined(
                  onTap: () async {
                    await item.value.like();
                  },
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
              if (item.value.canDelete)
                ButtonWidget.iconTextOutlined(
                  onTap: () async {
                    await item.value.delete();
                    isDelete.value = true;
                    //提醒动态分类更新信息
                    // if (activity.activityList.isNotEmpty) {
                    //   activity.activityList.first.changCallback();
                    // } else {
                    Get.back();
                    // }
                  },
                  // const Icon(
                  //   Icons.delete_outline,
                  //   size: 18,
                  //   color: XColors.black3,
                  // ),
                  XImage.localImage(XImage.deleteOutline,
                      color: XColors.black3, width: 22),
                  '删除',
                  textColor: XColors.black3,
                ),
            ],
          )
        ],
      );
    }));
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
          for (var e in item.value.metadata.likes) ...getUserAvatar(e)
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
            child: Text("全部评论 ${item.value.metadata.comments.length}",
                style: const TextStyle(
                  fontSize: 16,
                  color: XColors.black,
                  fontWeight: FontWeight.bold,
                )),
          ),
          ...item.value.metadata.comments.map((e) => ActivityComment(
              comment: e,
              onTap: (comment) => handleReply(context, comment.userId)))
        ],
      ),
    );
  }

  //渲染发布者信息
  Widget renderTags() {
    XEntity? entity = null != relationCtrl.user
        ? relationCtrl.user!
            .findMetadata<XEntity>(item.value.metadata.createUser!)
        : null;
    var showLikes = item.value.metadata.likes.isEmpty &&
        item.value.metadata.comments.isEmpty;
    return Column(
      children: [
        // Padding(padding: EdgeInsets.only(top: 5.h)),
        Offstage(
          offstage: false, //showLikes,
          child: Container(
              // padding: EdgeInsets.all(5.w),
              child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Row(
                children: [
                  // const ImageWidget(Icons.forward_5, size: 18),
                  XImage.localImage(XImage.forward, width: 22),
                  Container(
                      padding: EdgeInsets.only(left: 6.w),
                      child: Text(
                        "转发",
                        style: XFonts.size20Black3,
                      ))
                ],
              ),
              Row(
                children: [
                  // const ImageWidget(AssetsImages.iconLike,
                  //     size: 18, color: Colors.red),
                  XImage.localImage(XImage.likeOutline,
                      width: 22, color: XColors.primary),
                  Container(
                      padding: EdgeInsets.only(left: 6.w),
                      child: Text(
                        "${item.value.metadata.likes.isEmpty ? '点赞' : item.value.metadata.likes.length}",
                        style: XFonts.size20Black3,
                      ))
                ],
              ),
              Row(
                children: [
                  // const ImageWidget(AssetsImages.iconMsg, size: 18),
                  XImage.localImage(XImage.commentOutline, width: 22),
                  Container(
                      padding: EdgeInsets.only(left: 6.w),
                      child: Text(
                        "${item.value.metadata.comments.isEmpty ? '评论' : item.value.metadata.comments.length}",
                        style: XFonts.size20Black3,
                      ))
                ],
              ),
            ],
          )),
        )
      ],
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
