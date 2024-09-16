import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get_core/src/get_main.dart';
import 'package:get/get_navigation/src/extension_navigation.dart';
// import 'package:jaguar/serve/server.dart';
// import 'package:jaguar_flutter_asset/jaguar_flutter_asset.dart';
import 'package:orginone/components/common/getx/base_controller.dart';
import 'package:orginone/components/common/tip/toast.dart';
import 'package:orginone/components/file_preview/web_view/state.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/utils/log/log_util.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter/webview_flutter.dart' as wb;

class WebViewController extends BaseController<WebViewState> {
  @override
  final WebViewState state = WebViewState();
  // late final Jaguar server;

  @override
  void onInit() {
    // TODO: implement onInit
    super.onInit();
    state.title.value = "努力加载中......";
    state.webViewController = wb.WebViewController();
    state.webViewController.loadRequest(Uri.parse(state.url),
        headers: {'token': kernel.accessToken});
    print('>>>>>>>>>>>>>>>>>${state.url}');
    // _startService();
    state.webViewController.setOnConsoleMessage((message) {
      LogUtil.ee("web console ${message.message}");
    });
    state.webViewController.addJavaScriptChannel('FlutterChannel',
        onMessageReceived: onMessageReceived);
    state.webViewController.setJavaScriptMode(JavaScriptMode.unrestricted);
    // "alert('123');sessionStorage.setItem('accessToken', '${kernel.accessToken}');"
    state.webViewController.setBackgroundColor(Colors.white);
    state.webViewController.setNavigationDelegate(
      NavigationDelegate(
        onProgress: (int progress) {
          if (progress == 100) {
            LogUtil.ee('>>>>>:$progress');
            state.webViewController.getTitle().then((value) {
              state.title.value = Get.arguments?.name ?? value ?? "";
            });
            // } else if (progress == 80) {
            state.webViewController.runJavaScript("""
              (function() {
                if (window.sessionStorage) {
                  window.sessionStorage.setItem("accessToken", "${kernel.accessToken}");
                  if(window.orgCtrl) {
                    console.log('>>>>>>>>window.orgCtrl');
                    window.orgCtrl.provider.init();
                  }
                }
                // 在这里注入一些JavaScript代码来初始化flutterWebView对象
                // 这个代码通常由Flutter端通过evaluateJavascript方法注入
                // window.postMessage = new Object();
                window.postMessage = function(message) {
                  // 使用自定义的postMessage方法发送消息到Flutter
                  // for(var name in window.FlutterChannel) {
                  //   console.log(name)
                  //   if(name=='flutterWebViewPostMessage') {
                  //     console.log('>>>>>>>>'+name)
                  //     break;
                  //   }
                  // }
                  window.FlutterChannel.postMessage(message);
                };
              })();
            """).then((result) {});
          }
        },
        onPageStarted: (String url) {
          LogUtil.ee('>>>>>started:$url');
          // LoadingDialog.showLoading(context);
        },
        onPageFinished: (String url) {
          LogUtil.ee('>>>>>finished:$url');
          // LoadingDialog.dismiss(context);
        },
        onWebResourceError: (WebResourceError error) {
          LogUtil.ee('>>>>>:${jsonEncode(error)}');
        },
      ),
    );
  }

  void onMessageReceived(JavaScriptMessage message) {
    LogUtil.e('JavaScript executed:${jsonEncode(message.message)}');
    switch (message.message) {
      case "saveSuccess":
        ToastUtils.showMsg(msg: "提交成功");
        Get.back();
        break;
      default:
        break;
    }
  }

  // _startService() async {
  //   server = Jaguar(address: "127.0.0.1", port: 6888);
  //   server.addRoute(serveFlutterAssets(path: "web/"));
  //   await server.serve(logRequests: true);
  // }

  // @override
  // void onClose() {
  //   super.onClose();
  //   server.close();
  // }
}
