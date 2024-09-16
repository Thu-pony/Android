import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/extension/index.dart';
import 'package:orginone/components/common/getx/base_get_view.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/image_widget.dart';
import 'package:orginone/components/common/templates/gy_scaffold.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/common/text/text_widget.dart';
import 'package:orginone/config/constant.dart';
import 'package:orginone/config/theme/space.dart';
import 'package:orginone/config/theme/text.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:qr_flutter/qr_flutter.dart';

import 'logic.dart';
import 'state.dart';

class ShareQrCodePage
    extends BaseGetView<ShareQrCodeController, ShareQrCodeState> {
  const ShareQrCodePage({super.key});

  @override
  Widget buildView() {
    return GyScaffold(
        titleName: getTitleData(),
        backgroundColor: XColors.backgroundColor,
        body: buildMainView());
  }

  Widget buildMainView() {
    return <Widget>[
      buildUserQrInfoView(),
      const Expanded(
        child: SizedBox(),
      ),
      buildActions().paddingBottom(16)
    ].toColumn();
  }

  Widget buildUserQrInfoView() {
    return Container(
      width: ScreenUtil().screenWidth,
      height: 450,
      padding: EdgeInsets.only(
          top: AppSpace.paragraph * 2,
          bottom: AppSpace.paragraph,
          left: AppSpace.paragraph,
          right: AppSpace.paragraph),
      child: RepaintBoundary(
          key: controller.globalKey,
          child: BeautifulBGPage(
              showBack: false,
              showLogo: false,
              body: <Widget>[
                infoView(),
                buildQRImageView().paddingTop(AppSpace.paragraph),
                buildTipTextView().paddingTop(AppSpace.card),
              ].toColumn().paddingAll(AppSpace.paragraph))),
    );
  }

  /// 用户信息相关
  Widget infoView() {
    var image = state.entity.avatarThumbnail();
    return <Widget>[
      // ImageWidget(image, size: 60.w, fit: BoxFit.fill, radius: 5.w)
      //     .paddingRight(AppSpace.listItem),
      XImage.entityIcon(state.entity, size: Size(60.w, 60.w), radius: 5.w)
          .paddingRight(AppSpace.listItem),
      <Widget>[
        TextWidget(
          text: state.entity.name ?? '',
          style: AppTextStyles.titleMedium,
        ),
        Text(
          state.entity.remark ?? '',
          style: AppTextStyles.bodySmall,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
        ).width(300.w).paddingTop(AppSpace.button),
      ].toColumn(crossAxisAlignment: CrossAxisAlignment.start)
    ].toRow();
  }

  ///二维码
  Align buildQRImageView() {
    // var image = state.entity.avatarThumbnail();
    return Align(
      alignment: Alignment.center,
      child: QrImageView(
        data: '${Constant.host}/${state.entity.id}',
        version: QrVersions.auto,
        size: 260.w,
        // embeddedImage: image != null ? MemoryImage(image) : null,
        errorCorrectionLevel: QrErrorCorrectLevel.H,
        // embeddedImageStyle: QrEmbeddedImageStyle(size: Size(60.w, 60.w)),
        dataModuleStyle: const QrDataModuleStyle(
          dataModuleShape: QrDataModuleShape.square,
          color: Colors.black,
        ),
        eyeStyle: const QrEyeStyle(
          eyeShape: QrEyeShape.square,
          color: Colors.black,
        ),
      ),
    );
  }

  ///提示语
  TextWidget buildTipTextView() {
    return TextWidget(
      text: getRemarkData(),
      style: const TextStyle(
        color: XColors.gray_66,
        fontSize: 12,
      ),
    );
  }

  Widget buildActions() {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          buildButton(
              iconData: Icons.download_sharp,
              title: '保存二维码',
              onTap: () => controller.save(),
              txtColor: Colors.white,
              color: XColors.primary),
          buildButton(
              iconData: Icons.share_sharp,
              title: '分享',
              onTap: () => controller.share(),
              txtColor: Colors.black,
              color: XColors.white),
        ],
      ),
    );
  }

  buildButton(
      {required String title,
      required IconData iconData,
      required Color color,
      required Color txtColor,
      required Function onTap}) {
    return <Widget>[
      Container(
        width: (ScreenUtil().screenWidth - 40) / 2,
        height: 40,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(6),
        ),
        child: TextWidget(
          text: title,
          style: TextStyle(
            color: txtColor,
            fontSize: 16,
            fontFamily: 'PingFang SC',
            fontWeight: FontWeight.w600,
          ),
        ),
      )
    ].toColumn().onTap(() => onTap());
  }

  String getTitleData() {
    return "${state.entity.typeName}二维码";
  }

  String getRemarkData() {
    return state.entity.typeName == null
        ? "'扫一扫上面的二维码图案，一起分享吧'"
        : state.entity.typeName == TargetType.person.label
            ? "扫描二维码,申请添加好友"
            : "扫描二维码,申请加入${state.entity.typeName}";
  }
}
