import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/icons/icon.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/config/constant.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/controller/index.dart';
import 'package:qr_flutter/qr_flutter.dart';

// ignore: must_be_immutable
class FileinfoModal extends StatelessWidget {
  FileinfoModal({Key? key, this.data}) : super(key: key);
  dynamic data;
  IndexController controller = Get.find<IndexController>();
  @override
  Widget build(BuildContext context) {
    Widget content = Container(
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

    return content;
  }

  /// 公开信息
  publicInfo(BuildContext context, dynamic entity) {
    // XTarget? target = _getStorageTarget(entity);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _buildColumnTextInfo('名称', entity.name),
        const Divider(
          indent: 16,
        ),
        _buildColumnTextInfo('代码', entity.code),
        const Divider(
          indent: 16,
        ),
        // _buildColumnTextInfo('创建人', entity.shareIcon),
        _buildColumnInfo(
            '创建人',
            Row(
              children: [
                XImage.entityIcon(entity.creater, width: 35),
                const SizedBox(width: 4),
                Text(entity.creater.name)
              ],
            )),
        const Divider(
          indent: 16,
        ),
        _buildIntroduction('简介', entity.remark),
        const Divider(
          indent: 16,
        ),
        _buildIntroduction('创建时间', entity.updateTime),
        const Divider(
          indent: 16,
        ),

        QrImageView(
          data: '${Constant.host}/${entity.id}',
          version: QrVersions.auto,
          size: 260.w,
          errorCorrectionLevel: QrErrorCorrectLevel.H,
          dataModuleStyle: const QrDataModuleStyle(
            dataModuleShape: QrDataModuleShape.square,
            color: Colors.black,
          ),
          eyeStyle: const QrEyeStyle(
            eyeShape: QrEyeShape.square,
            color: Colors.black,
          ),
        ),
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
