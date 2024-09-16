import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/bottom_button_common.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/components/common/button/buttons.dart';
import 'package:orginone/components/common/dialogs/loading_dialog.dart';
import 'package:orginone/components/common/getx/events.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/tab_pages/index.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/common/text/text.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/config/constant.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/consts.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/dart/core/target/person.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/other/common_fun.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/routers/router_const.dart';
import 'package:qr_flutter/qr_flutter.dart';

import 'widget_utils.dart/part_widget.dart';

/// 实体详情页面
class EntityInfoPage extends OrginoneStatelessWidget {
  EntityInfoPage({super.key, super.data, this.isFromShare = false});
  dynamic receivedData;
  bool isFriend = false;
  bool? isFromShare;
  IndexController controller = Get.find<IndexController>();
  String dynamicId = '';
  @override
  Widget buildWidget(BuildContext context, dynamic data) {
    receivedData = data;
    Widget? content;
    if (data is ISession) {
      isFriend = data.isFriend;
      data = data.metadata;
    }
    if (data is XTarget || data is IEntity) {
      if (data.typeName == TargetType.person.label) {
        isFriend = null != relationCtrl.user
            ? relationCtrl.user!.members
                    .firstWhereOrNull((element) => element.id == data.id) !=
                null
            : false;
        if (relationCtrl.user!.id == data.id) {
          isFriend = true;
        }
      } else {
        if (isFromShare!) {
          if (data.typeName == TargetType.cohort.label) {
            isFriend = null != relationCtrl.user
                ? relationCtrl.user!.cohorts
                        .firstWhereOrNull((element) => element.id == data.id) !=
                    null
                : false;
          } else if (data.typeName == TargetType.company.label) {
            isFriend = null != relationCtrl.user
                ? relationCtrl.user!.companys
                        .firstWhereOrNull((element) => element.id == data.id) !=
                    null
                : false;
          }
        } else {
          isFriend =
              receivedData.members.any((i) => i.id == relationCtrl.user!.id);
        }
      }
      content = Container(
        color: Colors.white,
        child: Scrollbar(
          child: SingleChildScrollView(
              scrollDirection: Axis.vertical,
              child: Column(
                children: [
                  publicInfo(context, data),
                  if (data is IPerson) privateInfo(context, data),
                  if (data is IPerson && Platform.isIOS) buildLogoutBtn()
                ],
              )),
        ),
      );
    }

    return content ?? const Center(child: Text("空白"));
  }

  buildLogoutBtn() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        GestureDetector(
          onTap: () {
            showCupertinoDialog(
                context: Get.context!, builder: buildConfirmDialog);
          },
          child: Container(
              margin: EdgeInsets.only(top: 12.h, right: 24.w),
              width: Get.width * 0.35,
              height: 40,
              alignment: Alignment.center,
              decoration: BoxDecoration(
                border: Border.all(color: Colors.red, width: 1),
                borderRadius: BorderRadius.circular(6),
              ),
              child: Text(
                "注销账户",
                style: TextStyle(
                  color: Colors.red,
                  fontSize: 22.sp,
                  fontFamily: 'PingFang SC',
                  fontWeight: FontWeight.w600,
                ),
              )),
        ),
      ],
    );
  }

  /// 公开信息
  publicInfo(BuildContext context, dynamic entity) {
    XTarget? target = _getStorageTarget(entity);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // _buildHeaderTitle('公开信息'),
        _buildColumnInfo('头像', XImage.entityIcon(entity, width: 35)),
        const Divider(indent: 16),
        _buildColumnTextInfo('名称', entity.name),
        const Divider(
          indent: 16,
        ),
        _buildColumnTextInfo('账号', entity.code),
        const Divider(
          indent: 16,
        ),
        // if (null != target)
        //   target.typeName == TargetType.storage.label
        //       ? _buildColumnInfo(
        //           '当前数据核',
        //           Row(
        //             children: [
        //               XImage.entityIcon(target, width: 30.w),
        //               SizedBox(width: 5.h),
        //               Text(
        //                 target.name ?? '奥集能数据核',
        //                 style: const TextStyle(
        //                   color: Color(0xFF366EF4),
        //                   fontSize: 14,
        //                   fontFamily: 'PingFang SC',
        //                   fontWeight: FontWeight.w500,
        //                 ),
        //               )
        //             ],
        //           ))
        //       : _buildColumnInfo(
        //           '归属',
        //           Row(
        //             children: [
        //               XImage.entityIcon(target, width: 30.w),
        //               SizedBox(width: 5.h),
        //               Text(
        //                 target.name ?? '奥集能数据核',
        //                 style: const TextStyle(
        //                   color: Color(0xFF366EF4),
        //                   fontSize: 14,
        //                   fontFamily: 'PingFang SC',
        //                   fontWeight: FontWeight.w500,
        //                 ),
        //               )
        //             ],
        //           )),
        _buildIntroduction('简介', entity.remark),
        const Divider(
          indent: 16,
        ),
        _buildColumnInfo('二维码', _buildQRCodeRight(), onTapFunc: () {
          if (entity is XTarget) {
            Get.toNamed(
              Routers.shareQrCode,
              arguments: {"entity": entity},
            );
          } else {
            Get.toNamed(
              Routers.shareQrCode,
              arguments: {"entity": entity.metadata},
            );
          }
        }),
        const Divider(
          indent: 16,
        ),
        (!isFriend)
            ? _buildAddBtn(context, entity)
            : _buildExitOrDissolutionBtn(context, entity)
      ],
    );
  }

  Widget _buildExitOrDissolutionBtn(BuildContext context, dynamic entity) {
    return entity.typeName == TargetType.person.label
        ? Container()
        : GestureDetector(
            onTap: () async {
              if (entity.createUser == relationCtrl.user!.id) {
                Get.dialog(
                    Center(
                      child: Material(
                          color: Colors.transparent,
                          child: ConfirmDialog(
                            title: "解散${entity.typeName}",
                            content: "确认解散 ${entity.name} 吗？解散后将无法查看历史记录",
                            confirmText: "解散",
                            confirmFun: () async {
                              //发送验证码
                              String phone = relationCtrl.user!.code;
                              RegExp regex = RegExp(Constants.accountRegex);
                              if (!regex.hasMatch(phone)) {
                                ToastUtils.showMsg(msg: "手机号验证失败！");
                                return;
                              }
                              var res = await relationCtrl.auth
                                  .dynamicCode(DynamicCodeModel.fromJson({
                                'account': phone,
                                'platName': '资产共享云',
                                'dynamicId': '',
                              }));
                              if (res.success && res.data != null) {
                                dynamicId = res.data!.dynamicId;
                                Get.dialog(
                                    Center(
                                      child: Material(
                                          color: Colors.transparent,
                                          child: Dissolution(
                                              phoneNumber: phone,
                                              confirmFun: (yzm) async {
                                                //验证验证码
                                                var res = await relationCtrl
                                                    .provider
                                                    .verifyCodeLogin(
                                                        phone, yzm, dynamicId);
                                                if (res.success) {
                                                  dynamic dataTargets =
                                                      data.target;
                                                  bool success =
                                                      await dataTargets.delete(
                                                          notity: false);

                                                  if (success) {
                                                    Get.back();
                                                    ToastUtils.showMsg(
                                                        msg:
                                                            "已解散 ${entity.name}");
                                                    RoutePages.jumpHome(
                                                        home: HomeEnum.chat);
                                                  } else {
                                                    Get.back();
                                                    ToastUtils.showMsg(
                                                        msg: "解散失败，请稍后重试");
                                                  }
                                                } else {
                                                  ToastUtils.showMsg(
                                                      msg: res.msg ??
                                                          '验证失败,请重试');
                                                }
                                              })),
                                    ),
                                    barrierColor:
                                        Colors.black.withOpacity(0.6));
                              } else {
                                ToastUtils.showMsg(msg: '发送验证码失败，请稍后重试');
                                Get.back();
                              }
                            },
                          )),
                    ),
                    barrierColor: Colors.black.withOpacity(0.6));
              } else {
                //退出
                Get.dialog(
                    Center(
                        child: Material(
                            color: Colors.transparent,
                            child: ConfirmDialog(
                                title: "退出${entity.typeName}",
                                content:
                                    "确认退出 ${entity.name} 吗？退出后将无法查看历史记录且不会再收到此群组的消息",
                                confirmText: "退出",
                                confirmFun: () async {
                                  if (data is ISession) {
                                    dynamic dataTargets = data.target;
                                    // bool success = await dataTargets
                                    //     .removeMembers([relationCtrl.user!.metadata]);
                                    bool success = await dataTargets.exit();
                                    if (success) {
                                      ToastUtils.showMsg(msg: "退出成功");
                                      RoutePages.jumpHome(home: HomeEnum.chat);
                                    } else {
                                      ToastUtils.showMsg(msg: "退出失败，请稍后重试");
                                    }
                                  }
                                }))),
                    barrierColor: Colors.black.withOpacity(0.6));
              }
            },
            child: Container(
              margin: const EdgeInsets.all(16),
              width: (ScreenUtil().screenWidth - 40),
              height: 40,
              alignment: Alignment.center,
              decoration: BoxDecoration(
                color: XColors.bgColor,
                borderRadius: BorderRadius.circular(6),
              ),
              child: TextWidget(
                text: entity.createUser == relationCtrl.user!.id
                    ? "解散${entity.typeName}"
                    : "退出${entity.typeName}",
                style: const TextStyle(
                  color: XColors.red,
                  fontSize: 16,
                  fontFamily: 'PingFang SC',
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          );
  }

  Widget _buildAddBtn(BuildContext context, dynamic entity) {
    return GestureDetector(
      onTap: () async {
        List<XTarget>? targets = await relationCtrl.user!
            .searchTargets(entity.code, [entity.typeName]);
        if (targets.isNotEmpty) {
          bool success = await controller.user!.applyJoin(targets);
          if (success) {
            ToastUtils.showMsg(msg: "发送申请成功");
          }
        }
      },
      child: Container(
        margin: const EdgeInsets.all(16),
        width: (ScreenUtil().screenWidth - 40),
        height: 40,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: XColors.primary,
          borderRadius: BorderRadius.circular(6),
        ),
        child: TextWidget(
          text: entity.typeName == TargetType.person.label
              ? "添加好友"
              : "加入${entity.typeName}",
          style: const TextStyle(
            color: XColors.white,
            fontSize: 16,
            fontFamily: 'PingFang SC',
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
    );
  }

  Widget _buildQRCodeRight() {
    return Row(
      children: [
        XImage.localImage(XImage.qrcode, width: 24.w),
        const SizedBox(width: 4),
        Icon(
          Icons.arrow_forward_ios,
          size: 24.sp,
        )
      ],
    );
  }

  Widget _buildQRCode() {
    return Stack(
      children: [
        QrImageView(
          data: '${Constant.host}/${data.id}',
          semanticsLabel: "${Constant.host}/${data.id}",
          version: QrVersions.auto,
          size: 300.w,
        ),
        if (data is IEntity && data.share.avatar != null)
          Positioned(
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              child: Center(
                child: Container(
                  width: 70.w,
                  height: 70.w,
                  decoration: BoxDecoration(
                      border: Border.all(width: 5, color: Colors.white),
                      borderRadius: BorderRadius.circular(8),
                      image: DecorationImage(
                          image: MemoryImage(
                              data.share.avatar?.thumbnailUint8List))),
                ),
              ))
      ],
    );
    // return QrImageView(
    //   data: '${Constant.host}/${data.id}',
    //   semanticsLabel: "${Constant.host}/${data.id}",
    //   version: QrVersions.auto,
    //   size: 300.w,
    //   embeddedImage: data is IEntity && data.share.avatar != null
    //       ? MemoryImage(data.share.avatar?.thumbnailUint8List)
    //       : null,
    //   // errorCorrectionLevel: QrErrorCorrectLevel.H,
    //   embeddedImageStyle: QrEmbeddedImageStyle(size: Size(60.w, 60.w)),
    //   dataModuleStyle: const QrDataModuleStyle(
    //     dataModuleShape: QrDataModuleShape.square,
    //     color: Colors.black,
    //   ),
    //   eyeStyle: const QrEyeStyle(
    //     eyeShape: QrEyeShape.square,
    //     color: Colors.black,
    //   ),
    // );
  }

  XTarget? _getStorageTarget(dynamic entity) {
    XTarget? target;
    if (entity is XTarget && null != entity.storeId) {
      target = relationCtrl.user?.findMetadata(entity.storeId!);
    } else if (entity is IEntity &&
        entity.metadata is XTarget &&
        null != entity.metadata.storeId) {
      target = relationCtrl.user?.findMetadata(entity.metadata.storeId!);
    } else if (entity is IEntity &&
        entity.metadata is XTarget &&
        null != entity.metadata.belongId &&
        entity.metadata.belongId != entity.id) {
      target = relationCtrl.user?.findMetadata(entity.metadata.belongId!);
    }
    return target;
  }

  Widget _buildHeaderTitle(String title) {
    return Row(
      children: [
        Text(
          title,
          style: const TextStyle(
            color: Colors.black,
            fontSize: 14,
            fontFamily: 'PingFang SC',
            fontWeight: FontWeight.w500,
          ),
        )
      ],
    );
  }

  /// 构建文本信息
  Widget _buildColumnTextInfo(String title, String value) {
    return Container(
        padding: const EdgeInsets.all(16),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            commonTitleLeft(title),
            // const SizedBox(height: 20),
            Expanded(
              child: Text(value,
                  maxLines: 1,
                  textDirection: TextDirection.rtl,
                  style: XFonts.chatSMInfo),
            ),
          ],
        ));
  }

  /// 构建简介
  Widget _buildIntroduction(String title, String value) {
    return Container(
        color: Colors.white,
        padding: const EdgeInsets.fromLTRB(16, 16, 16, 14),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            commonTitleLeft(title),
            const SizedBox(height: 4),
            Text(
              value,
              style: XFonts.chatSMInfo,
            )
          ],
        ));
  }

  Widget commonTitleLeft(String title) {
    return Text(
      title,
      style: TextStyle(
        color: Colors.black.withOpacity(0.6),
        fontSize: 20.sp,
        fontFamily: 'PingFang SC',
      ),
    );
  }

  /// 构建组件信息
  Widget _buildColumnInfo(String title, Widget value, {Function? onTapFunc}) {
    return InkWell(
      radius: 0,
      highlightColor: Colors.transparent,
      onTap: () {
        if (onTapFunc != null) {
          onTapFunc();
        }
      },
      child: Container(
          padding: const EdgeInsets.all(16),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              commonTitleLeft(title),
              value,
            ],
          )),
    );
  }

  /// 隐私信息
  Widget privateInfo(BuildContext context, dynamic entity) {
    return Card(
        child: Padding(
      padding: const EdgeInsets.all(12),
      child: Column(
        children: [
          _buildHeaderTitle('隐私信息'),
          _buildRowTextInfo("手机号", entity.code),
          _buildRowTextInfo("单位", ""),
          _buildRowTextInfo("邮箱", ''),
          _buildRowTextInfo("微信", ''),
        ],
      ),
    ));
  }

  Widget _buildRowTextInfo(String label, String value) {
    return Padding(
      padding: EdgeInsets.only(top: 32.h),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            label,
            style: const TextStyle(
              color: Colors.black,
              fontSize: 16,
              fontFamily: 'PingFang SC',
              fontWeight: FontWeight.w400,
            ),
          ),
          Row(
            children: [
              Text(
                value,
                style: const TextStyle(
                  color: Color(0xFF6F7686),
                  fontSize: 14,
                  fontFamily: 'PingFang SC',
                  fontWeight: FontWeight.w400,
                ),
              ),
              Padding(
                padding: EdgeInsets.only(left: 10.w),
                child: const IconWidget(
                  iconData: Icons.edit,
                ),
              )
            ],
          )
        ],
      ),
    );
  }

  /// 卡包设置
  cardPackageSettings(BuildContext context, data) {}
}
