import 'package:flutter/material.dart';
import 'package:orginone/components/common/keep_alive/keep_alive_widget.dart';
import 'package:orginone/components/common/tab_pages/index.dart';
import 'package:orginone/components/empty/empty_activity.dart';
import 'package:orginone/dart/core/chat/activity.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/pages/portal/cohort/view.dart';
import 'package:orginone/pages/portal/workBench/view.dart';
import 'package:orginone/routers/pages.dart';

/// 门户页面
class PortalPage extends StatefulWidget {
  late InfoListPageModel? relationModel;

  PortalPage({super.key}) {
    relationModel = null;
  }

  @override
  State<StatefulWidget> createState() => _PortalPageState();
}

class _PortalPageState extends State<PortalPage> {
  InfoListPageModel? get relationModel => widget.relationModel;
  set relationModel(InfoListPageModel? relationModel) {
    widget.relationModel = relationModel;
  }

  late String? key;

  @override
  void initState() {
    super.initState();
    key = relationCtrl.provider.chatProvider?.activityProvider
        .subscribe((key, ages) {
      if (mounted) {
        setState(() {});
      }
    });
  }

  @override
  dispose() {
    super.dispose();
    if (null != key) {
      relationCtrl.provider.chatProvider?.activityProvider.unsubscribe(key);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (null == relationModel) {
      load();
    }

    return InfoListPage(relationModel!);
  }

  void load() {
    relationModel = InfoListPageModel(
        title: "门户",
        activeTabTitle: getActiveTabTitle(),
        tabItems: [
          TabItemsModel(title: "工作台", content: buildWorkBench()),
          TabItemsModel(title: "群动态", content: buildDynamic()),
          TabItemsModel(title: "好友圈", content: buildFriends())
        ]);
  }

  /// 获得激活页签
  getActiveTabTitle() {
    return RoutePages.getRouteDefaultActiveTab()?.firstOrNull;
  }

  /// 构建工作台
  Widget buildWorkBench() {
    return WorkBenchPage("workbench", "工作台");
  }

  /// 构建群动态
  Widget buildDynamic() {
    GroupActivity? cohortActivity =
        relationCtrl.provider.chatProvider?.activityProvider.cohortActivity;
    return null != cohortActivity && null != relationCtrl.user
        ? KeepAliveWidget(
            child: CohortActivityPage(
            "activity",
            "群动态",
            cohortActivity,
          ))
        : const EmptyActivity();
    // } else {
    //   return Container(
    //     child: const Center(child: Text("--暂无动态内容--")),
    //   );
    // }
  }

  /// 构建好友圈
  Widget buildFriends() {
    GroupActivity? friendsActivity =
        relationCtrl.provider.chatProvider?.activityProvider.friendsActivity;
    return null != friendsActivity && null != relationCtrl.user
        ? KeepAliveWidget(
            child: CohortActivityPage(
            "circle",
            "好友圈",
            friendsActivity,
          ))
        : const EmptyActivity();
    // } else {
    //   return Container(
    //     child: const Center(child: Text("--暂无好友圈内容--")),
    //   );
    // }
  }
}
