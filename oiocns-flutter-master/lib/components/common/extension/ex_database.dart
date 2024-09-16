import 'package:background_downloader/background_downloader.dart';
import 'package:get/get.dart';
import 'package:orginone/utils/log/log_util.dart';

/// 扩展 Database
extension ExDatabase on Database {
  ///判断是否下载
  Future<Task?> recordForName(String fileName) async {
    return await allRecords().then((records) {
      try {
        if (records.isNotEmpty) {
          TaskRecord? task = records
              .firstWhereOrNull((element) => element.task.filename == fileName);
          return task?.task;
        }
      } catch (e) {
        LogUtil.e(e);
      }
      return null;
    });
  }
}
