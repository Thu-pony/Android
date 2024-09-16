import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/dialogs/bottom_sheet_dialog.dart';
import 'package:orginone/components/common/extension/ex_widget.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/progress_bar/progress_bar.dart';
import 'package:orginone/components/common/tab_pages/types.dart';
import 'package:orginone/config/theme/unified_style.dart';

class GyPageWithBottomBtn extends StatefulWidget {
  final Widget? body;
  final List<ActionModel>? listAction;
  final Color? backgroundColor;
  const GyPageWithBottomBtn(
      {Key? key, this.body, this.listAction, this.backgroundColor})
      : super(key: key);
  @override
  State<GyPageWithBottomBtn> createState() => _GyPageWithBottomBtnState();
}

class _GyPageWithBottomBtnState extends State<GyPageWithBottomBtn> {
  late Widget body;
  late List<ActionModel> listAction;
  late Color? backgroundColor;
  double progress = -1;
  @override
  void initState() {
    super.initState();
    body = widget.body ?? Container();
    listAction = widget.listAction ?? [];
    backgroundColor = widget.backgroundColor;
    // progress = 0;
    // Timer.periodic(const Duration(seconds: 1), (Timer t) {
    //   setState(() {
    //     if (progress >= 1) {
    //       t.cancel();
    //       progress = -1;
    //     } else {
    //       progress += 0.1;
    //     }
    //   });
    // });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(child: body),
        Container(
          color: const Color(0xfff7f8fa),
          padding: EdgeInsets.all(listAction.isEmpty ? 0 : 12.w),
          child: Column(
            children: [if (progress >= 0) buildProgress(), buildBottomBtn()],
          ),
        )
      ],
    );
  }

  buildProgress() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: OrginoneProgressBar(progress: progress),
    );
  }

  moreActionWidget() {
    return buildBottomModal(context, listAction.sublist(2, listAction.length));
  }

  Widget buildBottomBtn() {
    List<Widget> btnList = [];
    if (listAction.length == 1) {
      btnList.add(buildBtnOne());
    } else if (listAction.length >= 2) {
      btnList.add(buildBtnOne());
      btnList.add(buildBtnTwo());
      if (listAction.length > 2) {
        btnList.add(GestureDetector(
          onTap: () {
            moreActionWidget();
          },
          child: Container(
              width: 60.w,
              height: 40,
              alignment: Alignment.center,
              decoration: BoxDecoration(
                color: XColors.white,
                borderRadius: BorderRadius.circular(6),
              ),
              child: XImage.localImage(XImage.moreAction,
                  width: 26.w, color: XColors.black)),
        ));
      }
    }
    return Row(
        mainAxisAlignment: listAction.length > 2
            ? MainAxisAlignment.spaceAround
            : MainAxisAlignment.end,
        children: btnList);
  }

  buildBtnOne() {
    return buildButton(
        title: listAction[0].title,
        color: XColors.primary,
        txtColor: XColors.white,
        icon: listAction[0].icon,
        onTap: listAction[0].onTap);
  }

  buildBtnTwo() {
    return buildButton(
        title: listAction[1].title,
        color: XColors.white,
        txtColor: XColors.black,
        icon: listAction[1].icon,
        onTap: listAction[1].onTap);
  }

  buildButton(
      {required String title,
      Color? color,
      Color? txtColor,
      double? width,
      Function? onTap,
      String? icon}) {
    return GestureDetector(
      onTap: () {
        if (onTap != null) {
          onTap();
        }
      },
      child: Container(
          margin: EdgeInsets.only(right: 12.w),
          constraints: BoxConstraints(
              minWidth: Get.width * 0.3, maxWidth: Get.width * 0.35),
          height: 40,
          alignment: Alignment.center,
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(6),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              XImage.localImage(icon ?? XImage.add,
                      width: 20.w, color: txtColor)
                  .paddingRight(2.w),
              Text(
                title,
                style: TextStyle(
                  color: txtColor,
                  fontSize: 22.sp,
                  fontFamily: 'PingFang SC',
                  fontWeight: FontWeight.w600,
                ),
              ),
            ],
          )),
    );
  }
}
