import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/components/common/dialogs/common_widget.dart';
import 'package:orginone/components/common/size/ui_config.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/config/theme/unified_style.dart';
import 'package:orginone/main_base.dart';

class Dissolution extends StatefulWidget {
  Dissolution({Key? key, required this.confirmFun, required this.phoneNumber})
      : super(key: key);
  Function(String) confirmFun;
  String phoneNumber;

  @override
  State<Dissolution> createState() => _DissolutionState();
}

class _DissolutionState extends State<Dissolution> {
  TextEditingController accountController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      width: UIConfig.screenWidth,
      height: UIConfig.screenHeight * 0.35,
      padding: const EdgeInsets.all(24),
      margin: const EdgeInsets.symmetric(horizontal: 16),
      decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.all(Radius.circular(8))),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            '继续解散请认证身份',
            style: XFonts.size24Black0W700,
          ),
          const SizedBox(height: 10),
          Text(
            '已将验证码发送您+86 ${widget.phoneNumber} 手机中，请注意查收',
            textAlign: TextAlign.center,
            style: XFonts.size22Black3,
          ),
          const SizedBox(height: 10),
          CommonWidget.commonTextInput(
            controller: accountController,
            title: '验证码',
            hint: '请输入验证码',
            inputFormatters: [
              FilteringTextInputFormatter.singleLineFormatter,
            ],
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Expanded(
                  child: CommonWidget.getClickButton('取消', 125.w, () {
                Navigator.pop(context);
              },
                      height: 48,
                      borderRadius: 6,
                      color: Colors.white,
                      textColor: XColors.doorDesGrey,
                      borderColor: XColors.dividerLineColor,
                      fontWeight: FontWeight.w500,
                      margin: const EdgeInsets.only(top: 5),
                      textSize: 16)),
              const SizedBox(width: 8),
              CommonWidget.getClickButton('确认解散群组', 250.w, () {
                if (accountController.text.isEmpty) {
                  ToastUtils.showMsg(msg: "验证码不得为空");
                  return;
                }
                widget.confirmFun.call(accountController.text);
              },
                  height: 48,
                  borderRadius: 6,
                  color: XColors.red,
                  textColor: XColors.white,
                  fontWeight: FontWeight.w500,
                  margin: const EdgeInsets.only(top: 5),
                  textSize: 16),
            ],
          )
        ],
      ),
    );
  }
}

class ConfirmDialog extends StatelessWidget {
  ConfirmDialog(
      {Key? key,
      required this.title,
      this.content,
      required this.confirmFun,
      this.confirmText})
      : super(key: key);
  String title;
  String? content;
  String? confirmText;
  Function confirmFun;
  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      width: UIConfig.screenWidth,
      height: UIConfig.screenHeight * 0.25,
      padding: const EdgeInsets.all(24),
      margin: const EdgeInsets.symmetric(horizontal: 16),
      decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.all(Radius.circular(8))),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            title,
            style: XFonts.size24Black0W700,
          ),
          const SizedBox(height: 10),
          Text(
            content ?? "",
            textAlign: TextAlign.center,
            style: XFonts.size22Black3,
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Expanded(
                  child: CommonWidget.getClickButton('取消', 125.w, () {
                Navigator.pop(context);
              },
                      height: 48,
                      borderRadius: 6,
                      color: Colors.white,
                      textColor: XColors.doorDesGrey,
                      borderColor: XColors.dividerLineColor,
                      fontWeight: FontWeight.w500,
                      margin: const EdgeInsets.only(top: 5),
                      textSize: 16)),
              const SizedBox(width: 8),
              Expanded(
                child: CommonWidget.getClickButton(confirmText ?? "确认", 250.w,
                    () {
                  Navigator.pop(context);
                  confirmFun.call();
                },
                    height: 48,
                    borderRadius: 6,
                    color: XColors.red,
                    textColor: XColors.white,
                    fontWeight: FontWeight.w500,
                    margin: const EdgeInsets.only(top: 5),
                    textSize: 16),
              ),
            ],
          )
        ],
      ),
    );
  }
}
