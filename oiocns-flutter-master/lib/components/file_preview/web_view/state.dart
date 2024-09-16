import 'package:get/get.dart';
import 'package:orginone/components/common/getx/base_get_state.dart';
import 'package:webview_flutter/webview_flutter.dart' as wb;

class WebViewState extends BaseGetState {
  late wb.WebViewController webViewController;
  var title = ''.obs;

  late String url;
  WebViewState() {
    url = Get.parameters['url'] ?? Get.arguments['url'];
  }
}
