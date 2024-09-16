import 'package:fluttertoast/fluttertoast.dart';

class ToastUtils {
  static Future<void> showMsgDev({required String msg}) {
    // return Fluttertoast.showToast(
    //     msg: msg,
    //     gravity: ToastGravity.CENTER,
    //     toastLength: Toast.LENGTH_SHORT);
    return Future(() => null);
  }

  static Future<void> showMsg({required String msg}) {
    return Fluttertoast.showToast(
        msg: msg,
        gravity: ToastGravity.CENTER,
        toastLength: Toast.LENGTH_SHORT);
  }

  static Future<void> dismiss() {
    return Fluttertoast.cancel();
  }
}
