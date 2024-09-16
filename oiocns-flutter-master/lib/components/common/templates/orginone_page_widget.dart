import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/tab_pages/infoListPage.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/index.dart';
import 'package:orginone/utils/log/log_util.dart';

import 'gy_scaffold.dart';

/// 静态组件
abstract class OrginoneStatelessWidget<P> extends StatelessWidget
    with _OrginoneController<P> {
  late final P _data;
  late final dynamic parentParam;

  P get data => _data;

  OrginoneStatelessWidget({super.key, data}) {
    _data =
        data ?? RoutePages.getRouteParams() ?? RoutePages.getParentRouteParam();
    this.parentParam = RoutePages.getParentRouteParam();
  }

  @override
  Widget build(BuildContext context) {
    return _build(context, _data, parentParam);
  }
}

/// 动态组件
abstract class OrginoneStatefulWidget<P> extends StatefulWidget {
  late final P data;
  late final dynamic parentParam;

  OrginoneStatefulWidget({super.key, data}) {
    this.data =
        data ?? RoutePages.getRouteParams() ?? RoutePages.getParentRouteParam();
    this.parentParam = RoutePages.getParentRouteParam();
  }
}

abstract class OrginoneStatefulState<T extends OrginoneStatefulWidget<P>, P>
    extends State<T> with _OrginoneController<P> {
  late P data;
  dynamic get parentParam => widget.parentParam;
  late final String key;

  @override
  void initState() {
    super.initState();
    data = widget.data;
    key = command.subscribeByFlag("homeChange", ([List<dynamic>? args]) {
      LogUtil.d("did homeChange 1");
      if (mounted) {
        setState(() {});
      }
    });
    LogUtil.d("======== did initState $key");
  }

  @override
  didChangeDependencies() {
    super.didChangeDependencies();
    LogUtil.d("didChangeDependencies");
    LogUtil.d("======== did $key");
  }

  @override
  void dispose() {
    super.dispose();
    command.unsubscribeByFlag(key);
    LogUtil.d("======== did dispose $key");
  }

  @override
  Widget build(BuildContext context) {
    return _build(context, data, parentParam);
  }
}

mixin _OrginoneController<P> {
  Widget _build(BuildContext context, P data, dynamic paramData) {
    Widget body = buildWidget(context, data);
    bool hasTabs = _hasChildOfType(body, InfoListPage);
    return GyScaffold(
        // backgroundColor: Colors.white,
        // titleName: data.name ?? "详情",
        titleWidget: _title(relationCtrl.homeEnum.value, paramData, data),
        isHomePage: isHomePage(),
        parentRouteParam:
            isHomePage() ? null : RoutePages.getParentRouteParam(),
        centerTitle: false,
        titleSpacing: 0,
        leadingWidth: 35,
        // actions: [XImage.localImage(XImage.user)],
        // leading: XImage.localImage(XImage.user),
        operations: buildButtons(context, data),
        // toolbarHeight: _getToolbarHeight(data),
        body: Container(
          color: const Color(0xFFF0F0F0),
          margin: hasTabs ? null : EdgeInsets.symmetric(vertical: 1.h),
          decoration: hasTabs ? null : const BoxDecoration(color: Colors.white),
          child: body,
        ));
  }

  bool isHomePage() {
    return RoutePages.getRouteLevel() == 0;
  }

  bool _hasChildOfType<T>(Widget widget, T type) {
    if (widget.runtimeType is T) {
      return true;
    }
    if (widget is Column) {
      if (widget.children.isEmpty) {
        return false;
      }

      for (var child in widget.children) {
        if (_hasChildOfType(child, type)) {
          return true;
        }
      }
    }

    return false;
  }

  double? _getToolbarHeight(P data) {
    var spaceName = "";
    if (data is IEntity) spaceName = data.groupTags.join(" | ");
    if (spaceName.isNotEmpty) {
      return 78.h;
    }
    return null;
  }

  Widget _title(HomeEnum homeEnum, dynamic data, P receiveData) {
    String name = data?.name ?? ""; //RoutePages.getRouteTitle() ??
    // if (state.chat.members.length > 1) {
    //   name += "(${state.chat.members.length})";
    // }
    // String spaceName = "";
    // if (data is IEntity) {
    //   spaceName = data.groupTags.reversed.firstWhere(
    //       (element) => element != "未读"); //data.groupTags.join(" | ");
    // }
    if (name.isEmpty && receiveData is FileItemShare) {
      name = receiveData.name ?? "";
    }
    int memberCount = 0;
    if (data is ISession) {
      memberCount = data.members.length;
    }
    return null != data || receiveData is FileItemShare
        ? Row(
            children: [
              (receiveData is FileItemShare)
                  ? Container()
                  : Padding(
                      padding: const EdgeInsets.only(right: 5),
                      child: XImage.entityIcon(data, width: 40.w),
                    ),
              Expanded(
                  child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(memberCount > 0 ? '$name($memberCount)' : name,
                      maxLines: 1,
                      style: XFonts.size26Black3.merge(
                          const TextStyle(overflow: TextOverflow.ellipsis))),
                  // Padding(
                  //     padding: const EdgeInsets.only(top: 2),
                  //     child: Text(spaceName, style: XFonts.size16Black9)),
                ],
              ))
            ],
          )
        : Container();
  }

  @protected
  List<Widget>? buildButtons(BuildContext context, P data) {
    if (data is IPerson) {
      // IndexController relationCtrl = Get.find<IndexController>();
      // if (relationCtrl.user!.id == data.id) {
      //   return logoutOpration();
      // }
    }
    return null;
  }
  // {
  //   return <Widget>[
  //     GFIconButton(
  //       color: Colors.white.withOpacity(0),
  //       icon: Icon(
  //         Icons.more_vert,
  //         color: XColors.black3,
  //         size: 32.w,
  //       ),
  //       onPressed: showMore(context, data) ? () => onMore(context, data) : null,
  //     ),
  //   ];
  // }

  @protected
  Widget buildWidget(BuildContext context, P data);

  // @protected
  // bool showMore(BuildContext context, dynamic data) {
  //   return false;
  // }

  // @protected
  // void onMore(BuildContext context, dynamic data) {}
}

///有背景的静态组件
abstract class BeautifulBGStatelessWidget extends StatelessWidget
    with _BeautifulBGController {
  BeautifulBGStatelessWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return _build(context);
  }
}

/// 有背景的动态组件
abstract class BeautifulBGStatefulWidget extends StatefulWidget {
  const BeautifulBGStatefulWidget({super.key});
}

abstract class BeautifulBGStatefulState<T extends BeautifulBGStatefulWidget>
    extends State<T> with _BeautifulBGController {
  @override
  Widget build(BuildContext context) {
    return _build(context);
  }
}

mixin _BeautifulBGController {
  @protected
  Widget buildWidget(BuildContext context);

  Widget _build(BuildContext context) {
    return Scaffold(
        body: Container(
      clipBehavior: Clip.antiAlias,
      decoration: const BoxDecoration(color: Colors.white),
      child: Stack(
        children: [
          //背景图模块
          _background(),
          buildWidget(context)
        ],
      ),
    ));
  }

  ///上下对其布局
  Widget topAndBottomLayout({required Widget top, required Widget bottom}) {
    return Stack(
      children: [
        Positioned(top: 0, left: 0, right: 0, child: top),
        Positioned(bottom: 0, left: 0, right: 0, child: bottom),
      ],
    );
  }

  ///表单布局
  Widget formLayout(
      {required Widget form, bool showBack = true, bool showLogo = true}) {
    return Stack(
      children: [
        if (showBack)
          Positioned(
            top: 60,
            left: 0,
            right: 0,
            child: Container(
                padding: const EdgeInsets.only(left: 20),
                alignment: Alignment.centerLeft,
                child: GestureDetector(
                  onTap: (() {
                    Get.back();
                  }),
                  child: XIcons.arrowBack32,
                )),
          ),
        if (showLogo)
          Positioned(top: 100, left: 0, right: 0, child: buildLogo()),
        Positioned(
          top: showLogo ? 160 : 10,
          left: 0,
          right: 0,
          child: form,
        ),
      ],
    );
  }

  Widget buildLogo() {
    return Center(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            margin: const EdgeInsets.fromLTRB(0, 5, 5, 0),
            width: 30,
            height: 30,
            clipBehavior: Clip.antiAlias,
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage(XImage.logoNotBg),
                fit: BoxFit.fill,
              ),
            ),
          ),
          const Text.rich(TextSpan(
            text: '奥集能',
            style: TextStyle(
                color: Color(0xFF15181D),
                fontSize: 22.91,
                fontFamily: 'PingFang SC',
                fontWeight: FontWeight.w500,
                decoration: TextDecoration.none),
          )),
        ],
      ),
    );
  }

  //背景图
  Widget _background() {
    return Stack(
      children: [
        Positioned(
          left: -200,
          child: Container(
            width: 900,
            height: 500,
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage(XImage.logoBackground),
                fit: BoxFit.cover,
              ),
            ),
          ),
        ),
        Positioned(
          left: -200,
          child: Container(
            width: 900,
            height: 500,
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                colors: [
                  Color.fromRGBO(249, 249, 249, 0),
                  Color.fromRGBO(255, 255, 255, 1),
                ],
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
              ),
            ),
          ),
        ),
      ],
    );
  }
}

class BeautifulBGPage extends BeautifulBGStatelessWidget {
  bool showBack;
  bool showLogo;
  Widget body;

  BeautifulBGPage(
      {super.key,
      required this.body,
      this.showBack = true,
      this.showLogo = true});

  @override
  Widget buildWidget(BuildContext context) {
    return formLayout(form: body, showBack: showBack, showLogo: showLogo);
  }
}
