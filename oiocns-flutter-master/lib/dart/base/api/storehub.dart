import 'dart:async';
import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:get/get.dart';
import 'package:orginone/config/constant.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/storages/storage.dart';
import 'package:orginone/dart/core/auth.dart';
import 'package:orginone/dart/core/public/consts.dart';
import 'package:orginone/main_base.dart';
import 'package:signalr_netcore/signalr_client.dart';
import 'package:uuid/uuid.dart';

import 'http_util.dart';

/// 存储集线器
class StoreHub with AuthMixin, RequestStatisticsMixin {
  // 超时重试时间
  final int _timeout;
  // 是否已经启动
  bool _isStarted = false;
  // http 请求客户端
  // axios实例
  final _http = HttpUtil();
  // signalr 连接
  final HubConnection _connection;
  // 连接成功回调
  List<Function([Exception?])> _connectedCallbacks = [];
  // 连接断开回调
  List<Function([Exception?])> _disconnectedCallbacks = [];
  // // 日志
  // final Logger log = Logger("StoreHub");

  StoreHub(
    String url, {
    protocol = 'json',
    int timeout = 8000,
    int interval = 3000,
  })  : _timeout = timeout,
        _connection = HubConnectionBuilder()
            .withUrl(url, options: HttpConnectionOptions())
            .withAutomaticReconnect(retryDelays: [
          0,
          0,
          0,
          ...List.generate(3, (index) => timeout + 2000)
        ]).build() {
    _connection.keepAliveIntervalInMilliseconds = interval;
    _connection.serverTimeoutInMilliseconds = timeout;
    _connection.onclose(({error}) {
      LogUtil.d('>>>===内核断开');
      _refreshIsStartedState();
      // ToastUtils.showMsg(msg: "连接断开,${_timeout}ms后重试。${error.toString()}`");
      LogUtil.e("连接断开,${_timeout}ms后重试。${error.toString()}`");
      Future.delayed(Duration(milliseconds: _timeout), () {
        _starting();
      });
    });
    // _connection.onreconnecting(({error}) {
    // LoadingDialog.showLoading(Get.context!,
    //     msg: "正在重新连接服务器", dismissSeconds: -1);
    // });
    _connection.onreconnected(({connectionId}) {
      LogUtil.d('>>>===内核已连接');
      // kernel.restart();
      // Future.delayed(const Duration(microseconds: 50), () {
      // LoadingDialog.dismiss(Get.context!);
      _refreshIsStartedState();
      // });
    });
    _connection.onreconnecting(({Exception? error}) {
      LogUtil.d('>>>===内核链接中');
      _refreshIsStartedState();
    });
  }

  /// 连接ID
  String get connectionId => _connection.connectionId ?? '';

  /// 是否处于连接着的状态
  /// @return {boolean} 状态
  bool get isConnected {
    if (_isStarted && !_isConnected) {
      _refreshIsStartedState();
    }
    return _isStarted && _isConnected;
  }

  bool get _isConnected {
    return _connection.state == HubConnectionState.Connected;
  }

  Future<void> checkOnline() async {
    if (_connection.state == HubConnectionState.Disconnected) {
      await _starting();
    }
  }

  void _refreshIsStartedState() {
    if (_isConnected) {
      if (!_isStarted) {
        _isStarted = true;
        _callConnectedCallbacks();
      }
    } else if (_connection.state == HubConnectionState.Disconnected) {
      if (_isStarted) {
        _isStarted = false;
        _callDisconnectedCallbacks();
      }
    } else {
      _isStarted = false;
      _callDisconnectedCallbacks();
    }
  }

  void _callDisconnectedCallbacks([Exception? error]) {
    LogUtil.d(
        '>>>===onDisconnected:${_disconnectedCallbacks.length} ${_connection.state}');
    for (final callback in _disconnectedCallbacks) {
      callback(error);
    }
  }

  void _callConnectedCallbacks() {
    LogUtil.d(
        '>>>===onConnected:${_connectedCallbacks.length} ${_connection.state}');
    for (final callback in _connectedCallbacks) {
      callback();
    }
  }

// 获取accessToken
  String get accessToken {
    // LogUtil.d('accessToken');
    return Storage.getString('accessToken');
  }

  // 设置accessToken
  set accessToken(String val) {
    Storage.setString('accessToken', val);
    Storage.setInt(Constants.appTokenGenerationTime,
        DateTime.now().millisecondsSinceEpoch);
  }

  ///连接状态
  HubConnectionState? connectionState() {
    return _connection.state;
  }

  /// 销毁连接
  /// @returns {Promise<void>} 异步Promise ts内核
  /// 异步 Future
  Future<void> dispose() async {
    _isStarted = false;
    _callDisconnectedCallbacks();
    _connectedCallbacks = [];
    _disconnectedCallbacks = [];
    return await _connection.stop();
  }

  /// 启动链接
  /// @returns {void} 无返回值
  Future<void> start() async {
    if (!isConnected) {
      if (connectionState() == HubConnectionState.Disconnected) {
        await _starting();
      }
    }
  }

  /// 重新建立连接
  /// @returns {void} 无返回值
  Future<void> restart() async {
    // if (!isConnected && _connection.state != HubConnectionState.Disconnected) {
    //   // await _connection.stop().then((_) {
    //   // start();
    //   // });
    // } else
    LogUtil.d('>>>===$isConnected ${_connection.state}');
    if (_connection.state != HubConnectionState.Connected &&
        _connection.state != HubConnectionState.Connecting &&
        _connection.state != HubConnectionState.Reconnecting) {
      if (isConnected) {
        await _connection.stop();
      }
      await start();
    }
  }

  /// 开始连接
  /// @returns {void} 无返回值
  Future<void> _starting() async {
    await _connection.start()?.then((_) {
      _refreshIsStartedState();
    }, onError: (err) {
      LogUtil.e("url: ${_connection.baseUrl}");
      _refreshIsStartedState();
      if (_isConnected) {
        LogUtil.e("连接失败,连接状态为$_isStarted。长链接状态:${_connection.state}");
      } else {
        LogUtil.e("连接失败,${_timeout}ms后重试。${err != null ? err.toString() : ''}");
        // Future.delayed(Duration(milliseconds: _timeout), () {
        //   restart();
        // });
        _callDisconnectedCallbacks(err);
      }
    });
  }

  /// 连接成功事件
  /// @param {Function} callback 回调
  /// @returns {void} 无返回值
  void onConnected(Function([Exception?])? callback) {
    if (callback != null) {
      _connectedCallbacks.add(callback);
    }
  }

  /// 断开连接事件
  /// @param {Function} callback 回调
  /// @returns {void} 无返回值
  void onDisconnected(Function([Exception?])? callback) {
    if (callback != null) {
      _disconnectedCallbacks.add(callback);
    }
  }

  /// 监听服务端方法
  /// @param {string} methodName 方法名
  /// @param {Function} newMethod 回调
  /// @returns {void} 无返回值
  void on(String methodName, void Function(List<dynamic>?) newMethod) {
    _connection.on(methodName, newMethod);
  }

  /// 请求服务端方法
  /// @param {string} methodName 方法名
  /// @param {any[]} args 参数
  /// @returns {Promise<ResultType>} 异步结果
  Future<ResultType> invoke<T>(String methodName,
      {List<Object>? args, bool? retry = false}) {
    var id = const Uuid().v1();
    return _invoke<T>(id, methodName, args: args, retry: retry);
  }

  Future<ResultType> _invoke<T>(String id, String methodName,
      {List<Object>? args, bool? retry = false}) async {
    Object? resObj;
    try {
      if (_isConnected) {
        resObj = await _connection.invoke(methodName, args: args);
        toStatistic(
          resObj,
          "signalr",
          request: {"id": id, "methodName": methodName, "args": args},
        );
      } else {
        // ToastUtils.showMsgDev(msg: "长链接断开，请重试");
        resObj = await _restRequest(
          'post',
          '${Constant.rest}/${methodName.toLowerCase()}',
          args!.isNotEmpty ? args[0] : {},
        );
        toStatistic(resObj, "http",
            request: {"id": id, "methodName": methodName, "args": args});
      }
      if (null != resObj) {
        ResultType res = ResultType.fromJson(resObj as Map<String, dynamic>);
        // LogUtil.d('接口：${Constant.rest}/${methodName.toLowerCase()}');
        // LogUtil.d('参数：${jsonEncode(args![0])}');

        if (res.success) {
          // LogUtil.d(
          //     'StoreHub返回值=================Start============================');
          // LogUtil.d(resObj);
          // LogUtil.d(
          //     'StoreHub返回值=================End============================');
          return _success(res);
        } else {
          return _error(resObj);
        }
      }
      return _error(resObj);
    } catch (e, s) {
      try {
        LogUtil.d('接口：${Constant.rest}/${methodName.toLowerCase()}');
        LogUtil.d('StoreHub参数：${jsonEncode(args![0])}');
        LogUtil.e("invoke Error${e.toString()}${s.toString()}");
      } catch (e) {}
      return _error(resObj, e, s);
    }
  }

  /// Http请求服务端方法
  /// @param {string} methodName 方法名
  /// @param {any[]} args 参数
  /// @returns {Promise<ResultType>} 异步结果
  Future<dynamic> _restRequest(
    String method,
    String url,
    dynamic args,
  ) async {
    final res = await _http.post(
      url,
      data: args is String || args is Map ? args : args.toJson(),
    );

    return res;
  }

  ResultType<dynamic> _success(ResultType<dynamic> res) {
    return res;
  }

  ResultType<dynamic> _error([dynamic resObj, dynamic err, dynamic s]) {
    var msg = '请求异常';
    ResultType res = badRequest;
    try {
      if (null != resObj && resObj is Map) {
        res = ResultType.fromJson(resObj as Map<String, dynamic>);
        if (res.code == 401) {
          LogUtil.e('==========================================登录已过期处理');
          LogUtil.e(accessToken);
          // 登录已过期处理
          _errorNoAuthLogout();
        } else if (res.msg != '' && !res.msg.contains('不在线')) {
          LogUtil.e('Http:操作失败,${res.msg}');
          // TODO 再实际业务端做提醒，不然刚进入app 有可能会出现很多异常弹框，体验很不好
          // ToastUtils.showMsg(msg: res.msg);
          // throw (res.msg);
        }
      } else {
        LogUtil.e('Http:===========================err');
        LogUtil.e('Http:${res.toJson()}');
        LogUtil.e('Http:$s');
        if (null != err) {
          msg += err is String ? err : err.toString() ?? '';
        }
        LogUtil.e('Http:$msg');
        res = getRequest(msg);
      }
    } catch (e, s) {
      LogUtil.e('Http:$s');
      // throw (res.msg);
    }
    // ToastUtils.dismiss();
    return res;
  }

  Future<void> disconnect() async {
    _isStarted = false;
    _callDisconnectedCallbacks();
    return await _connection.stop();
  }

  String toJson(dynamic jsonObj) {
    String jsonStr = "";

    if (null == jsonObj) {
      return "";
    } else if (jsonObj is List) {
      jsonStr = "[";
      for (var element in jsonObj) {
        jsonStr += toJson(element);
      }
      jsonStr += "]";
    } else if (jsonObj is Map<String, dynamic> || jsonObj is Map) {
      jsonStr += jsonObj.toString();
    } else if (jsonObj is String) {
      jsonStr = jsonObj;
    } else {
      try {
        jsonStr = jsonEncode(jsonObj);
      } catch (e) {
        LogUtil.d('Http:err:${jsonObj.runtimeType}>$jsonObj');
        e.printError();
      }
    }
    return jsonStr;
  } // 退出并重新登录

  Future<void> _errorNoAuthLogout() async {
    await relationCtrl.refreshToken();
    // ToastUtils.showMsg(msg: "http请求，token过期刷新，${tokenExpired()}");
  }
}

///请求统计
mixin RequestStatisticsMixin {
  final Map<String, int> _cache = {};
  final List<dynamic> _cacheError = [];

  void toStatistic(Object? resObj, String key,
      {Map<String, dynamic>? request}) {
    ResultType res = ResultType(success: false, code: 1000, msg: "请求失败");
    if (null != resObj) {
      if (resObj is ResultType) {
        res = resObj;
      } else if (resObj is Map<String, dynamic>) {
        res = ResultType.fromJson(resObj);
      }
    }
    if (res.success) {
      successCount(key);
    } else if (res.code == 401) {
      errorCount(key);
      if (null != request && request.containsKey("password")) {
        _cacheError.add(request);
      }
      _cacheError.add(resObj);
      _cacheError.add("====================$key");
    } else if (res.code == 400) {
      errorCount(key);
    } else {
      errorCount(key);
      if (null != request) {
        _cacheError.add(request);
      }
      _cacheError.add(resObj);
      _cacheError.add("====================$key");
    }
  }

  void successCount(String key) {
    key = '${key}_success';
    _count(key);
  }

  void errorCount(String key) {
    key = '${key}_error';
    _count(key);
  }

  void _count(String key) {
    if (_cache.containsKey(key)) {
      _cache[key] = _cache[key]! + 1;
    } else {
      _cache[key] = 1;
    }
  }

  String getStatisticsInfo() {
    var sb = StringBuffer();
    sb.write("请求统计：\n");
    sb.write("成功：\n");
    _cache.forEach((key, value) {
      if (key.contains("success")) {
        sb.write("$key:$value\n");
      }
    });
    if (_cacheError.isNotEmpty) {
      sb.write("失败：\n");
      _cache.forEach((key, value) {
        if (key.contains("error")) {
          sb.write("$key:$value\n");
        }
      });
      sb.write("失败详情：\n");
      for (var value in _cacheError) {
        sb.write("$value\n");
      }
    }

    return sb.toString();
  }
}
