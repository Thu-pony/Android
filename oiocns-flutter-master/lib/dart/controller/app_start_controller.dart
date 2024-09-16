import 'dart:async';

import 'package:connectivity/connectivity.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/dart/base/common/commands.dart';
import 'package:orginone/dart/base/common/emitter.dart';
import 'package:orginone/dart/core/auth.dart';
import 'package:orginone/dart/core/public/enums.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/routers/pages.dart';
import 'package:orginone/utils/log/log_util.dart';

///app启动控制器
class AppStartController with EmitterMixin, AuthMixin {
  ///app网络状态（手机网络，内核长链接状态）
  late ConnectivityResult _result;

  // ///手机生命周期状态
  // late String _appLifecycleState;
  ///app启动状态
  late ExecuteStatus appStartStatus;

  ///调用进行中计数
  int _warpUnderwayCount = 0;

  ///断网络计数
  int _noneConnectivityCount = 0;

  AppStartController() {
    _result = ConnectivityResult.none;
    _stop();
    subscribe((key, args) async {
      LogUtil.ee("开始刷新数据");
      await _networkFluctuations();
    }, false);
    kernel.onConnectedChanged((isConnected) async {
      LogUtil.ee("长链接状态：$isConnected");
      await checkNetWorkConnected();
      if (isConnected) {
        _noneConnectivityCount += 1;
        changeCallback();
      } else {
        RoutePages.jumpTo(initialRoute);
      }
    });
  }

  bool get isStart {
    return appStartStatus == ExecuteStatus.success ||
        appStartStatus == ExecuteStatus.failed;
  }

  ///检查网络连接并更新最新状态
  Future<void> checkNetWorkConnected() async {
    var res = await Connectivity().checkConnectivity();
    if (_result != res) {
      connectivityResult = res;
    }
  }

  // 判断网络连接
  bool get isNetworkConnected {
    return _result == ConnectivityResult.wifi ||
        _result == ConnectivityResult.mobile;
  }

  bool get hasDataDelay {
    return _noneConnectivityCount > 0;
  }

  /// 设置手机网络状态
  set connectivityResult(ConnectivityResult result) {
    // ToastUtils.showMsg(
    //     msg: "手机网络状态：${_result.name} ${result.name} $_noneConnectivityCount");
    _result = result;
    if (_result == ConnectivityResult.none) {
      _noneConnectivityCount += 1;
      RoutePages.jumpTo(initialRoute);
    }
    changeCallback();
  }

  Timer? _time;

  /// 设置手机生命周期状态
  /// resumed APP的状态从paused切换到resumed进入前台状态
  /// paused 应用挂起，比如退到后台
  /// inactive 界面退到后台或弹出对话框情况下
  /// suspending iOS中没有该状态，安卓里就是挂起
  set appLifecycleState(String state) {
    ToastUtils.showMsgDev(msg: state);
    if (state == 'AppLifecycleState.resumed') {
      // _time?.cancel();
      // _time = null;
      refreshData();
    } else if (state == "AppLifecycleState.paused") {
      ToastUtils.showMsgDev(msg: "App退到后台");
      // // RoutePages.jumpTo(initialRoute);
      // _time = Timer(const Duration(minutes: 3), () {
      //   _noneConnectivityCount += 1;
      // });
    }
    // _appLifecycleState = state;
  }

  ///网络波动处理
  Future<void> _networkFluctuations() async {
    LogUtil.ee(
        "hasDataDelay:$hasDataDelay isNetworkConnected:$isNetworkConnected appStartStatus:$appStartStatus");
    if ((hasDataDelay && isNetworkConnected) ||
        appStartStatus == ExecuteStatus.failed) {
      // relationCtrl.wakeUp();

      // ToastUtils.showMsg(msg: "手机网络状态：${_result.name}刷新数据");
      await refreshData();
    }
  }

  Future<void> refreshData() async {
    checkNetWorkConnected();
    LogUtil.ee(
        "isNetworkConnected:$isNetworkConnected loginStatus:$loginStatus appStartStatus:$appStartStatus");
    if (!isNetworkConnected ||
        appStartStatus == ExecuteStatus.starting ||
        !loginStatus) {
      ///TODO带优化跳转到网络无法使用的页面
      // ToastUtils.showMsg(
      //     msg:
      //         "唤醒：直接退出 $isNetworkConnected ${appStartStatus == ExecuteStatus.starting}");
      return;
    }

    await kernel.checkOnline();

    ///TODO判断token是否过期
    if (tokenExpired()) {
      // ToastUtils.showMsg(msg: "唤醒：token过期刷新");
      LogUtil.ee("token过期刷新");
      await onInit();
    } else if (kernel.user == null) {
      LogUtil.ee("唤醒：user销毁重连");
      await onInit();
    } else if (_noneConnectivityCount > 0) {
      LogUtil.ee("唤醒：网络断开$_noneConnectivityCount重连");
      // ToastUtils.showMsg(msg: "唤醒：网络断开$_noneConnectivityCount重连");
      // await relationCtrl.refreshData();
      await _refreshData();
      command.emitterFlag('session');
    } else {
      LogUtil.ee("唤醒：刷新数据");
      await _wakeUp();
      command.emitterFlag('session');
    }
  }

  Future<void> _wakeUp() async {
    if (!_start()) {
      return;
    }
    try {
      await relationCtrl.provider.wakeUp();
    } catch (e) {}
    _end();
  }

  Future<void> _refreshData() async {
    if (!_start()) {
      return;
    }
    try {
      await relationCtrl.refreshData();
    } catch (e) {}
    _end();
  }

  ///开始启动
  Future<void> onInit() async {
    bool res = true;
    if (!loginStatus || !_start()) {
      return;
    }

    try {
      // if (tokenExpired()) {
      res = await relationCtrl.autoLogin();
      // } else {
      //   await _refreshData();
      // }
    } catch (e) {}
    _end(success: res);
  }

  bool _start() {
    if (_warpUnderwayCount > 0) {
      return false;
    }
    appStartStatus = ExecuteStatus.starting;
    _warpUnderwayCount += 1;
    LogUtil.d("APP启动====================开始");
    return true;
  }

  void _end({bool success = true}) {
    if (success) {
      _noneConnectivityCount = 0;
      appStartStatus = ExecuteStatus.success;
    } else {
      appStartStatus = ExecuteStatus.failed;
    }
    _warpUnderwayCount -= 1;
    LogUtil.d("APP启动####################结束");
    LogUtil.d("APP启动 ${kernel.statisticsInfo}");
  }

  void _stop() {
    appStartStatus = ExecuteStatus.notStart;
  }
}
