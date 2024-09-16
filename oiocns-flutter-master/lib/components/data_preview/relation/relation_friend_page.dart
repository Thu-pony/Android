import 'package:flutter/material.dart';
import 'package:orginone/components/chat/chat_session_page.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/list/index.dart';
import 'package:orginone/components/common/tab_pages/index.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/data_preview/entity_info/entity_info_page.dart';
import 'package:orginone/components/data_preview/relation/member_list_page.dart';
import 'package:orginone/components/target_activity/activity_list.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/index.dart';
import 'package:orginone/utils/log/log_util.dart';

class RelationFriendPage extends OrginoneStatelessWidget {
  late InfoListPageModel? relationModel;
  RelationFriendPage({super.key, super.data}) {
    relationModel = null;
  }

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    if (null == relationModel) {
      load();
    }

    return InfoListPage(relationModel!);
  }

  void load() {
    relationModel = InfoListPageModel(
        title: RoutePages.getRouteTitle() ?? data.name ?? "好友",
        activeTabTitle: getActiveTabTitle(),
        tabItems: [
          // createTabItemsModel(title: "好友"),
          TabItemsModel(
              title: "沟通", icon: XImage.chatOutline, content: buildChats()),
          TabItemsModel(
              title: "动态",
              icon: XImage.dynamicOutline,
              content: buildActivity()),
          if (isSelf)
            TabItemsModel(
                title: "成员",
                icon: XImage.memberOutline,
                content: buildPersons()),
          TabItemsModel(
              title: "设置",
              icon: XImage.settingOutline,
              content: buildSetting()),
          // TabItemsModel(title: "成员", content: const Text("文件")),
          // TabItemsModel(title: "文件", content: const Text("文件")),
        ]);
  }

  bool get isSelf {
    return data.id == relationCtrl.user?.id;
  }

  /// 获得激活页签
  getActiveTabTitle() {
    return RoutePages.getRouteDefaultActiveTab()?.first;
  }

  Widget buildActivity() {
    ISession? chat;
    if (data is ISession) {
      chat = data;
    } else if (isSelf) {
      chat = relationCtrl.user?.session;
    } else {
      chat = relationCtrl.user?.findMemberChat(data.id);
    }
    return ActivityListWidget(activity: chat?.activity, isFromChat: true);
  }

  TabItemsModel createTabItemsModel({
    required String title,
  }) {
    return TabItemsModel(
        title: title,
        content: ListWidget<XTarget>(
          getDatas: ([dynamic data]) {
            return getFriends();
          },
          getAction: (dynamic data) {
            return GestureDetector(
              onTap: () {
                LogUtil.d('>>>>>>======点击了感叹号');
              },
              child: const IconWidget(
                color: XColors.chatHintColors,
                iconData: Icons.info_outlined,
              ),
            );
          },
          onTap: (dynamic data, List children) {
            LogUtil.d('>>>>>>======点击了列表项 ${data.name}');
          },
        ));
  }

  Widget? buildChats() {
    var session = data;
    if (isSelf) {
      session = relationCtrl.user?.session;
    } else if (session is XTarget) {
      session = relationCtrl.user?.findMemberChat(session.id);
    }
    return ChatSessionPage(data: session);
  }

  List<XTarget> getFriends() {
    return relationCtrl.user?.members ?? [];
  }

  Widget buildPersons() {
    return MemberListPage(data: relationCtrl.user);
  }

  Widget buildSetting() {
    var session = data;
    if (session is XTarget) {
      session = relationCtrl.user?.findMemberChat(session.id);
    }
    return EntityInfoPage(data: session);
  }
}
