import 'package:custom_pop_up_menu/custom_pop_up_menu.dart';
import 'package:flutter/material.dart' hide SearchBar;
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/extension/index.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/config/theme/space.dart';
import 'package:orginone/config/theme/text.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/routers/index.dart';

class UserBar extends GetView<IndexController> {
  ///实体/对象
  dynamic data;

  ///标题
  String title;

  ///操作按钮
  List<Widget>? actions;

  GlobalKey<ScaffoldState>? scaffoldKey;

  UserBar(
      {super.key, this.title = "", this.data, this.actions, this.scaffoldKey});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      return Container(
        child: _userBar(scaffoldKey),
      );
    });
  }

  ///AppBar
  Widget _userBar(GlobalKey<ScaffoldState>? scaffoldKey) {
    return SizedBox(
      height: 74.h,
      child: <Widget>[
        Row(
          children: [
            GestureDetector(
                onTap: () {
                  scaffoldKey?.currentState?.openDrawer();
                },
                child: _createImgAvatar(EdgeInsets.only(left: 20.w),
                    size: 44, circular: false, radius: 8.w)),
            Text(
              controller.homeEnum.value.label,
              style: AppTextStyles
                  .titleLarge, //?.copyWith(fontWeight: FontWeight.w900)
            ).paddingLeft(AppSpace.listRow),
          ],
        ),
        // _userAvatarWidget(),
        _actionsWidge(),
      ].toRow(mainAxisAlignment: MainAxisAlignment.spaceBetween),
    );
  }

  ///用户头像
  Row _userAvatarWidget() {
    return Row(
      children: [
        _createCustomPopupMenu(
            child: Padding(
              padding: EdgeInsets.only(left: 10.w),
              child: _createImgAvatar(EdgeInsets.only(left: 10.w),
                  size: 44, circular: false, radius: 8.w),
            ),
            children: [
              settingMenuHeaderWidget(),
              ...SettingEnum.values
                  .map(
                    (item) => settingMenuItemWidget(item),
                  )
                  .toList()
            ],
            controller: controller.functionMenuController),
        Text(
          controller.homeEnum.value.label,
          style: AppTextStyles
              .titleLarge, //?.copyWith(fontWeight: FontWeight.w900)
        ).paddingLeft(AppSpace.listRow),
      ],
    );
  }

  ///设置菜单头部 用户信息
  settingMenuHeaderWidget() {
    return GestureDetector(
      onTap: () {
        // Future.delayed(const Duration(milliseconds: 200), () {
        //   Get.toNamed(Routers.userInfo);
        // });

        RoutePages.jumpRelationInfo(data: controller.user!);
      },
      child: SizedBox(
        height: 58.w,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            _createImgAvatar(
                EdgeInsets.only(
                  right: 14.w,
                ),
                size: 56,
                circular: false,
                radius: 7),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    controller.provider.user?.metadata.name ?? "",
                    overflow: TextOverflow.ellipsis,
                    maxLines: 1,
                    style: TextStyle(
                      fontSize: 24.sp,
                      fontWeight: FontWeight.w600,
                      fontFamily: 'PingFang SC',
                      color: const Color(0xff15181d),
                    ),
                  ),
                  // TextWidget(
                  //   size: 18.sp,
                  //   text: controller.provider.user?.metadata.name ?? "",
                  //   style: AppTextStyles.titleSmall
                  //       ?.copyWith(fontWeight: FontWeight.w900),
                  // ),
                  Text(
                    controller.provider.user?.metadata.remark ?? "",
                    overflow: TextOverflow.ellipsis,
                    maxLines: 1,
                    style: TextStyle(
                      color: const Color(0xff424a57),
                      fontSize: 16.sp,
                      fontWeight: FontWeight.w500,
                      fontFamily: 'PingFang SC',
                    ),
                  ),
                ],
              ),
            ),
            GestureDetector(
              onTap: () {
                Future.delayed(const Duration(milliseconds: 200), () {
                  Get.toNamed(
                    Routers.shareQrCode,
                    arguments: {"entity": controller.user?.metadata},
                  );
                });
              },
              child: SizedBox(
                  // color: Colors.green,
                  width: 24.w,
                  height: 24.w,
                  child: XImage.localImage(XImage.qrcode, width: 24.w)),
            )
          ],
        ),
      ),
    );
  }

  ///设置弹框的item
  settingMenuItemWidget(SettingEnum item, {bool isShowRightIcon = true}) {
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: () {
        // controller.functionMenuController.hideMenu();

        Future.delayed(Duration.zero, () {
          controller.jumpSetting(item);
        });
      },
      child: SizedBox(
        height: 56,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            SizedBox(
                width: 24.w,
                height: 24.w,
                child: XImage.localImage(item.icon, width: 24.w)),
            Expanded(
              child: Container(
                margin: EdgeInsets.only(left: 10.w),
                child: Text(
                  item.label,
                  style: TextStyle(
                    color: const Color(0xff15181d),
                    fontSize: 22.sp,
                    fontWeight: FontWeight.w500,
                    fontFamily: 'PingFang SC',
                  ),
                ),
              ),
            ),
            if (isShowRightIcon)
              Icon(
                Icons.arrow_forward_ios,
                size: 24.sp,
              )
          ],
        ),
      ),
    );
  }

  ///右侧++++++++++++++++++++++++++++++++++++++++++++++++
  ///右侧事件
  _actionsWidge() {
    return <Widget>[
      ...actions ??
          XImage.operationIcons([
            XImage.search,
            XImage.scan,
            XImage.add,
          ]), //[searchWidget, qrScanWidget, moreWidget],
      const SizedBox(
        width: 10,
      ),
    ].toRow();
  }

  ///公用构建方法
  ///弹出菜单构建
  Widget _createCustomPopupMenu({
    CustomPopupMenuController? controller,
    required Widget child,
    required List<Widget> children,
  }) {
    return CustomPopupMenu(
      menuBuilder: () => ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: Container(
          color: Colors.white,
          child: IntrinsicWidth(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: children,
            ),
          ),
        ).paddingAll(AppSpace.listView),
      ),
      controller: controller,
      pressType: PressType.singleClick,
      showArrow: false,
      child: child.clipRRect(all: AppSpace.button),
    );
  }

  ///头像
  Widget _createImgAvatar(EdgeInsets insets,
      {BoxFit fit = BoxFit.cover,
      bool circular = true,
      double size = 44,
      double? radius}) {
    dynamic avatar = XImage.entityIcon(
      controller.provider.user,
      width: size.w,
      circular: circular,
      radius: radius,
      fit: fit,
    );

    return Container(margin: insets, child: avatar);
  }
}
