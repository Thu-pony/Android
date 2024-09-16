import 'dart:math';

import 'package:easy_refresh/easy_refresh.dart';
import 'package:flutter/material.dart';
import 'package:orginone/components/common/button/bottom_button_common.dart';
import 'package:orginone/components/common/button_floating/action_container.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/components/empty/empty_activity.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:orginone/routers/pages.dart';

import 'activity_message.dart';

//渲染动态列表
class ActivityListWidget extends StatefulWidget {
  late final ScrollController _scrollController;
  late final IActivity? _activity;
  late final int? showCount;
  late final bool? isFromChat;

  ActivityListWidget(
      {super.key,
      this.showCount,
      this.isFromChat,
      scrollController,
      activity}) {
    var parentParam = RoutePages.getParentRouteParam();
    _scrollController = scrollController ?? ScrollController();
    _activity = activity ?? (parentParam is IActivity ? parentParam : null);
  }
  @override
  State<StatefulWidget> createState() => _ActivityListState();
}

class _ActivityListState extends State<ActivityListWidget> {
  ScrollController get _scrollController => widget._scrollController;
  IActivity? get _activity => widget._activity;
  int? get showCount => widget.showCount;
  bool? get isFromChat => widget.isFromChat ?? false;
  late final String? _key;

  @override
  void initState() {
    super.initState();
    //订阅数据变更
    // _activity?.unsubscribe();
    _key = _activity?.subscribe((key, args) {
      if (mounted) {
        setState(() {});
      }
    }, false);
  }

  @override
  void dispose() {
    super.dispose();
    _activity?.unsubscribe(_key);
  }

  @override
  void didUpdateWidget(covariant ActivityListWidget oldWidget) {
    super.didUpdateWidget(oldWidget);
  }

  @override
  Widget build(BuildContext context) {
    return null != _activity
        ? null != showCount
            ? _buildActivity(activity: _activity!)
            : _buildActivityList(
                activity: _activity!, isFromChat: isFromChat ?? false)
        : const EmptyActivity();
  }

  Widget _buildActivity({required IActivity activity}) {
    int i = 0;
    List<IActivityMessage> activityMessages =
        activity.activityList.sublist(0, min(2, activity.activityList.length));
    return Column(
      children: activityMessages.map((item) {
        return Container(
            decoration: const BoxDecoration(
              color: XColors.bgListItem,
              border: Border(
                bottom: BorderSide(
                  width: 1,
                  color: XColors.dividerLineColor,
                ),
              ),
            ),
            child: ActivityMessageWidget(
              currIndex: i++,
              item: item,
              activity: activity,
              hideResource: true,
            ));
      }).toList(),
    );
  }

  loadDataMore() async {
    await _activity!.load();
    setState(() {});
    print("load more : size=${_activity!.activityList.length}");
  }

  Widget _buildActivityList(
      {required IActivity activity, bool isFromChat = false}) {
    Widget content = Container(
      color: XColors.white,
      child: EasyRefresh(
          header: const MaterialHeader(),
          footer: const MaterialFooter(),
          onRefresh: null,
          onLoad: loadDataMore,
          scrollController: _scrollController,
          child: ListView(
              controller: _scrollController,
              children: _buildActivityItem(activity: _activity!))),
    );
    if (activity.allPublish) {
      if (isFromChat) {
        content = addBottomBtnWidget(
          // buttonTooltip: "发表动态",
          onPressed: () {
            RoutePages.jumpActivityRelease(activity: activity);
          },
          child: content,
        );
      } else {
        content = _actionWidget(
          buttonTooltip: "发表动态",
          onPressed: () {
            RoutePages.jumpActivityRelease(activity: activity);
          },
          child: content,
        );
      }
    }
    return content;
  }

  Widget addBottomBtnWidget({required Widget child, Function()? onPressed}) {
    return GyPageWithBottomBtn(
      body: child,
      listAction: [
        ActionModel(
            title: "发表动态",
            onTap: () {
              if (onPressed != null) {
                onPressed();
              }
            })
      ],
    );
  }

  Widget _actionWidget(
      {required String buttonTooltip,
      Function()? onPressed,
      required Widget child}) {
    return ActionContainer(
      floatingActionButton: FloatingActionButton(
        onPressed: onPressed,
        mini: true,
        tooltip: buttonTooltip,
        child: const Icon(Icons.add),
      ),
      child: child,
    );
  }

  //渲染动态列表
  List<Widget> _buildActivityItem({required IActivity activity}) {
    List<Widget> list = [];
    for (int i = 0; i < activity.activityList.length; i++) {
      list.add(Container(
        decoration: const BoxDecoration(
          color: XColors.bgListItem,
          border: Border(
            bottom: BorderSide(
              width: 1,
              color: XColors.dividerLineColor,
            ),
          ),
        ),
        child: ActivityMessageWidget(
          currIndex: i,
          activity: activity,
          hideResource: true,
        ),
      ));
    }
    return list;
  }
}
