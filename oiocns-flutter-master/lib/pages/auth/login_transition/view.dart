import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/icons/ximage.dart';
import 'package:orginone/components/common/progress_bar/progress_bar.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/index.dart';

class LoginTransPage extends StatefulWidget {
  const LoginTransPage({super.key});

  @override
  State<LoginTransPage> createState() => _LoginTransPageState();
}

class _LoginTransPageState extends State<LoginTransPage> {
  late String _key="login_key";
  @override
  void initState() {
    super.initState();
    try {
      _key = relationCtrl.subscribe((key, args) {
        if (Get.currentRoute == Routers.logintrans) {
          RoutePages.jumpHome(home: relationCtrl.homeEnum.value);
        }
        // Get.offAndToNamed(Routers.home, arguments: true);
        // Future.delayed(const Duration(milliseconds: 10), () {
        //   relationCtrl.unsubscribe(key);
        // });
      },
          (Get.arguments ?? false)
              ? relationCtrl.provider.inited
              : relationCtrl.appStartController.isStart);
    } catch (e) {
      //
    }
  }

  @override
  void dispose() {
    super.dispose();
    relationCtrl.unsubscribe(_key);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // backgroundColor: Colors.white,
      // body: SizedBox(
      //   width: MediaQuery.of(context).size.width,
      //   height: MediaQuery.of(context).size.height,
      //   child: Image.asset(
      //     AssetsImages.loginTransition,
      //     fit: BoxFit.cover,
      //   ),
      // ),

      body: _login(),
    );
  }

  Widget _login() {
    return Container(
      clipBehavior: Clip.antiAlias,
      decoration: const BoxDecoration(color: Colors.white),
      child: Stack(
        children: [
          //背景图模块
          background(),
          //奥集能 模块
          logo(),
          //文字Orginone 区域
          Positioned(
            left: 0,
            right: 0,
            top: (MediaQuery.maybeOf(context)?.size.height ?? 600) * 0.30,
            child: const Text(
              '物以类聚  人以群分',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Color(0xFF366EF4),
                fontSize: 25,
                fontFamily: 'DingTalk',
                fontWeight: FontWeight.w400,
                height: 0.06,
              ),
            ),
          ),
          Positioned(
            left: 0,
            right: 0,
            top: (MediaQuery.maybeOf(context)?.size.height ?? 600) * 0.33,
            child: const Text(
              'Orginone',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Color(0xFF366EF4),
                fontSize: 18,
                fontFamily: 'DingTalk',
                fontWeight: FontWeight.w400,
                height: 0,
              ),
            ),
          ),
          Positioned(left: 0, right: 0, bottom: 0, child: OrginoneProgressBar())
        ],
      ),
    );
  }

  //logo
  Widget logo() {
    return Positioned(
      bottom: 100.h,
      left: 0,
      right: 0,
      child: Center(
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
                fontSize: 17,
                fontFamily: 'PingFang SC',
                fontWeight: FontWeight.w500,
              ),
            )),
          ],
        ),
      ),
    );
  }

  //背景图
  Widget background() {
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
