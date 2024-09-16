import 'dart:io';
import 'dart:math';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:extended_image/extended_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/images/image_widget.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/file_preview/photo_widget.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/core/work/rules/lib/tools.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/file_utils.dart';

/// 资源列表
class ResourceContainerWidget extends StatelessWidget with ResourceMixin {
  final List<FileItemShare> fileList;
  final double maxWidth;
  int columns;
  final List<Widget>? uploadingWidget;
  final Widget? uploadButtonWidget;
  late final bool hasUploadButton;
  final bool hideResource;

  ResourceContainerWidget(this.fileList, this.maxWidth,
      {super.key,
      this.columns = 3,
      this.uploadingWidget,
      this.uploadButtonWidget,
      this.hideResource = false}) {
    columns = min(
        3, null != uploadButtonWidget ? fileList.length + 1 : fileList.length);
    hasUploadButton = null != uploadButtonWidget;
    assert(columns > 0, "资源列表不能为空，或者上传按钮不为空");
  }

  @override
  Widget build(BuildContext context) {
    // int i = 0;
    List<Widget> widgetList = [];
    List tempList = [];
    tempList.addAll(fileList.sublist(
        0, hideResource ? min(3, fileList.length) : fileList.length));
    return tempList.length == 1
        ? SizedBox(
            width: 160,
            height: 160,
            child:
                renderResource(context, tempList[0], widgetList, itemIndex: 0)!)
        : SizedBox(
            width: tempList.length == 4
                ? (ScreenUtil().screenWidth - 28) * 2 / 3
                : ScreenUtil().screenWidth,
            child: GridView.count(
              physics: const NeverScrollableScrollPhysics(),
              shrinkWrap: true,
              crossAxisCount: tempList.length == 4 ? 2 : 3,
              // padding: const EdgeInsets.all(4),
              mainAxisSpacing: 4,
              crossAxisSpacing: 4,
              childAspectRatio: 1,
              children: [
                ...(tempList
                        .asMap()
                        .keys
                        .map((e) => Container(
                            child: renderResource(
                                context, tempList[e], widgetList,
                                itemIndex: e)!))
                        // .map((item) => Container(
                        //     child: renderResource(context, item, widgetList)!))
                        .toList() ??
                    []),
                if (hasUploadButton) ...uploadingWidget!,
                if (hasUploadButton) uploadButtonWidget!
              ],
            ),
          );
  }

  double computedSize() {
    if (fileList.length >= columns) {
      return maxWidth / columns - 8;
    } else if (fileList.length > 1) {
      return maxWidth / fileList.length - 8;
    }
    return maxWidth - 8;
  }
}

mixin ResourceMixin {
  Widget? renderResource(
      BuildContext context, FileItemShare item, List<Widget> imageProviderList,
      {int? itemIndex}) {
    if (FileUtils.isImage(item.extension ?? "")) {
      // 获取屏幕高度
      final height = UIConfig.screenHeight;
      String link = shareOpenLink(item.shareLink);
      bool isLocalResource = link == item.shareLink;
      imageProviderList.add(
        // PhotoWidget(
        //     imageProvider: isLocalResource
        //         ? FileImage(File(link))
        //         : CachedNetworkImageProvider(link)),
        isLocalResource
            ? PhotoWidget(imageProvider: FileImage(File(link)))
            : PhotoWidget(imageProvider: CachedNetworkImageProvider(link)),
      );
      return GestureDetector(
        onTap: () {
          Navigator.of(context).push(
            DialogRoute(
              context: context,
              builder: (BuildContext context) {
                return CarouselSlider(
                    options: CarouselOptions(
                      height: height,
                      initialPage: itemIndex ?? imageProviderList.length - 1,
                      viewportFraction: 1,
                    ),
                    items: imageProviderList);
              },
            ),
          );
        },
        child: ImageWidget(
          fit: BoxFit.cover,
          isLocalResource ? File(link) : link,
          radius: 4,
        ),
      );
    } else if (FileUtils.isVideo(item.extension ?? "")) {
      return GestureDetector(
        onTap: () {
          RoutePages.jumpFile(file: item);
        },
        child: Stack(
          children: [
            ImageWidget(
              fit: BoxFit.cover,
              item.poster != null && item.poster!.isNotEmpty
                  ? shareOpenLink(item.poster)
                  : item.thumbnailUint8List,
              // size: computedSize()
              radius: 4,
              size: 160,
            ),
            Positioned(
              child:
                  Center(child: XImage.localImage(XImage.videoPlay, width: 32)),
            )
          ],
        ),
      );
    } /*else if (FileUtils.isPdf(item.extension ?? "") ||
        FileUtils.isWord(item.extension ?? "")) {
      return getThumbnail(shareOpenLink(item.shareLink));
    }*/
    return null;
  }

  // Widget getThumbnail(String path) {
  //   String img = XImage.otherIcon;
  //   String ext =
  //       path.substring(path.lastIndexOf('.'), path.length).toLowerCase() ?? "";
  //   if (ext == '.jpg' || ext == '.jpeg' || ext == '.png' || ext == '.webp') {
  //     img = path;
  //   } else {
  //     switch (ext) {
  //       case ".xlsx":
  //       case ".xls":
  //       case ".excel":
  //         img = XImage.excelIcon;
  //         break;
  //       case ".pdf":
  //         img = XImage.pdfIcon;
  //         break;
  //       case ".ppt":
  //         img = XImage.pptIcon;
  //         break;
  //       case ".docx":
  //       case ".doc":
  //         img = XImage.wordIcon;
  //         break;
  //       default:
  //         img = XImage.otherIcon;
  //         break;
  //     }
  //   }
  //   return ImageWidget(img);
  // }

  // GridView.extent(
  //       maxCrossAxisExtent: maxWidth,
  //       padding: const EdgeInsets.all(4),
  //       mainAxisSpacing: 4,
  //       crossAxisSpacing: 4,
  //       children:
  //     )
}
