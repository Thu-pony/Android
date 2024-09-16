import 'package:extended_tabs/extended_tabs.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/badge_tab/badge_widget.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/keep_alive/keep_alive_widget.dart';
import 'package:orginone/components/common/tab_bar/expand_tab_bar.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/version_update/update_utils.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/pages/chats/chat_page.dart';
import 'package:orginone/pages/portal/portal_page.dart';
import 'package:orginone/pages/relation/relation_page.dart';
import 'package:orginone/pages/store/store_page.dart';
import 'package:orginone/pages/work/work_page.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/log/log_util.dart';
import 'package:orginone/config/theme/unified_style.dart';

/// 首页
class HomePage extends OrginoneStatefulWidget {
  HomeEnum homeEnum;
  bool isHomePage;
  @override
  HomePage(
      {super.key,
      this.homeEnum = HomeEnum.door,
      this.isHomePage = true,
      super.data});

  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends OrginoneStatefulState<HomePage, dynamic> {
  var currentIndex = 0.obs;
  DateTime? lastCloseApp;
  @override
  void initState() {
    super.initState();
    AppUpdate.instance.update();
  }

  @override
  bool isHomePage() {
    return widget.isHomePage ? true : super.isHomePage();
  }

  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    // LogUtil.d(">>>>>>>=======home");
    return WillPopScope(
        onWillPop: () async {
          if (null == data &&
              (lastCloseApp == null ||
                  DateTime.now().difference(lastCloseApp!) >
                      const Duration(seconds: 1))) {
            lastCloseApp = DateTime.now();
            ToastUtils.showMsg(msg: '再按一次退出');
            return false;
          }
          return true;
        },
        child: DefaultTabController(
            length: 5,
            initialIndex: _getTabCurrentIndex(),
            animationDuration: Duration.zero,
            child: Column(
              children: [
                Expanded(
                  child: Column(
                    children: [
                      Expanded(
                        child: ExtendedTabBarView(
                          // shouldIgnorePointerWhenScrolling: false,
                          children: [
                            KeepAliveWidget(child: ChatPage()),
                            KeepAliveWidget(child: WorkPage()),
                            KeepAliveWidget(child: PortalPage()),
                            const StorePage(),
                            const RelationPage(),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                // InfoListPage(relationCtrl.relationModel),
                // bottomButton(),
                _HomeBottomBar(isHomePage())
              ],
            )));
  }

  // Widget bottomButton() {
  //   return Container(
  //     decoration: BoxDecoration(
  //       color: Colors.white,
  //       border: Border(
  //         top: BorderSide(color: Colors.grey.shade400, width: 0.4),
  //       ),
  //     ),
  //     child: ExpandTabBar(
  //       tabs: [
  //         ExtendedTab(child: button(homeEnum: HomeEnum.chat, path: 'chat')),
  //         ExtendedTab(child: button(homeEnum: HomeEnum.work, path: 'work')),
  //         ExtendedTab(child: button(homeEnum: HomeEnum.door, path: 'home')),
  //         ExtendedTab(child: button(homeEnum: HomeEnum.store, path: 'store')),
  //         ExtendedTab(
  //           child: button(homeEnum: HomeEnum.relation, path: 'relation'),
  //         ),
  //       ],
  //       onTap: (index) {
  //         LogUtil.d(">>>>====ModelTabs.onTap");
  //         jumpTab(HomeEnum.values[index]);
  //       },
  //       indicator: const BoxDecoration(),
  //     ),
  //   );
  // }

  // Widget button({
  //   required HomeEnum homeEnum,
  //   required String path,
  // }) {
  //   return Obx(() {
  //     var isSelected = relationCtrl.homeEnum == homeEnum;
  //     // LogUtil.d('>>>>>>>======$isSelected ${relationCtrl.homeEnum} $homeEnum');
  //     var mgsCount = 0;
  //     if (homeEnum == HomeEnum.work) {
  //       mgsCount = relationCtrl.provider.work?.todos.length ?? 0;
  //     } else if (homeEnum == HomeEnum.chat) {
  //       mgsCount = relationCtrl.noReadMgsCount.value;
  //     }
  //     return BadgeTabWidget(
  //       imgPath: path,
  //       foreColor: isSelected ? XColors.selectedColor : XColors.doorDesGrey,
  //       body: Text(homeEnum.label),
  //       mgsCount: mgsCount,
  //     );
  //   });
  // }

  // void jumpTab(HomeEnum relation) {
  //   relationCtrl.homeEnum.value = relation;
  //   RoutePages.clearRoute();
  //   setState(() {});
  //   // RoutePages.jumpHome(home: relation, preventDuplicates: true);
  // }

  int _getTabCurrentIndex() {
    return HomeEnum.values
        .indexWhere((element) => element == relationCtrl.homeEnum.value);
  }

  @override
  List<Widget>? buildButtons(BuildContext context, dynamic data) {
    if (relationCtrl.homeEnum == HomeEnum.work) {
      return XImage.operationIcons([
        XImage.search,
        XImage.scan,
        XImage.startWork,
      ]);
    } else if (relationCtrl.homeEnum == HomeEnum.store) {
      return XImage.operationIcons([
        XImage.search,
        XImage.addStorage,
      ]);
    } else if (relationCtrl.homeEnum == HomeEnum.relation) {
      return XImage.operationIcons([
        XImage.search,
        XImage.joinGroup,
      ]);
    }
    return null;
  }
}

class _HomeBottomBar extends StatefulWidget {
  final bool isHomePage;
  const _HomeBottomBar(this.isHomePage);

  @override
  State<StatefulWidget> createState() => __HomeBottomBarState();
}

class __HomeBottomBarState extends State<_HomeBottomBar> {
  @override
  void initState() {
    super.initState();
    relationCtrl.provider.chatProvider?.subscribe((key, args) {
      if (mounted) {
        setState(() {});
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return bottomButton();
  }

  Widget bottomButton() {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          top: BorderSide(color: Colors.grey.shade400, width: 0.4),
        ),
      ),
      child: ExpandTabBar(
        tabs: [
          ExtendedTab(child: button(homeEnum: HomeEnum.chat, path: 'chat')),
          ExtendedTab(child: button(homeEnum: HomeEnum.work, path: 'work')),
          ExtendedTab(child: button(homeEnum: HomeEnum.door, path: 'home')),
          ExtendedTab(child: button(homeEnum: HomeEnum.store, path: 'store')),
          ExtendedTab(
            child: button(homeEnum: HomeEnum.relation, path: 'relation'),
          ),
        ],
        onTap: (index) {
          LogUtil.d(">>>>====ModelTabs.onTap");
          jumpTab(HomeEnum.values[index]);
        },
        indicator: const BoxDecoration(),
      ),
    );
  }

  Widget button({
    required HomeEnum homeEnum,
    required String path,
  }) {
    return Obx(() {
      var isSelected = relationCtrl.homeEnum == homeEnum;
      // LogUtil.d('>>>>>>>======$isSelected ${relationCtrl.homeEnum} $homeEnum');
      var mgsCount = 0;
      if (homeEnum == HomeEnum.work) {
        mgsCount = relationCtrl.provider.work?.todos.length ?? 0;
      } else if (homeEnum == HomeEnum.chat) {
        mgsCount = relationCtrl.provider.chatProvider?.noReadChatCount ?? 0;
      }
      return BadgeTabWidget(
        imgPath: path,
        foreColor: isSelected ? XColors.selectedColor : XColors.doorDesGrey,
        body: Text(homeEnum.label),
        mgsCount: mgsCount,
      );
    });
  }

  void jumpTab(HomeEnum relation) {
    // if (!widget.isHomePage) {
    // RoutePages.jumpPortal(home: relation);
    // } else {
    relationCtrl.homeEnum.value = relation;
    RoutePages.clearRoute();
    // setState(() {});
    command.emitterFlag("homeChange", [relation]);
    // }
  }
}
