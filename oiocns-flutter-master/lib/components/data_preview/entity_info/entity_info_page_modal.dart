import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/text/text.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:orginone/dart/core/chat/session.dart';
import 'package:orginone/dart/core/public/entity.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/router_const.dart';

// ignore: must_be_immutable
class EntityUserinfoModal extends StatelessWidget {
  EntityUserinfoModal({Key? key, this.data}) : super(key: key);
  dynamic data;
  dynamic receivedData;
  bool isFriend = false;
  bool? isFromShare = true;
  IndexController controller = Get.find<IndexController>();
  @override
  Widget build(BuildContext context) {
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
        clipBehavior: Clip.hardEdge,
        height: ScreenUtil().screenHeight * 0.6,
        width: ScreenUtil().screenWidth - 32,
        padding: const EdgeInsets.symmetric(vertical: 16),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Scrollbar(
          child: SingleChildScrollView(
              scrollDirection: Axis.vertical,
              child: Column(
                children: [
                  publicInfo(context, data),
                ],
              )),
        ),
      );
    }
    return content ??
        Container(
            clipBehavior: Clip.hardEdge,
            height: ScreenUtil().screenHeight * 0.6,
            width: ScreenUtil().screenWidth - 32,
            alignment: Alignment.center,
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(10),
            ),
            child: const Text("暂无数据"));
  }

  /// 公开信息
  publicInfo(BuildContext context, dynamic entity) {
    // XTarget? target = _getStorageTarget(entity);

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
        if (!isFriend) _buildAddBtn(context, entity)
      ],
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
            if (Get.isDialogOpen!) {
              Get.back();
            }
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
}
