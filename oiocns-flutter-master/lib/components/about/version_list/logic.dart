import 'package:flutter/widgets.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/dialogs/dialog.dart';
import 'package:orginone/components/common/getx/base_controller.dart';
import 'package:orginone/components/version_update/update_utils.dart';

import 'item.dart';
import 'state.dart';

class VersionListController extends BaseController<VersionListState> {
  @override
  VersionListState state = VersionListState();

  @override
  void onReady() {
    // TODO: implement onReady
    super.onReady();
    getVersionList();
  }

  void backToHome() {
    Get.back();
  }

  void showDiaLog(BuildContext context, VersionItem item) {
    showVersionItemDetail(context, item);
  }

  getVersionList() async {
    List<UpdateModel> loadHistoryVersionInfo =
        await UpdateRequest.loadHistoryVersionInfo();
    if (loadHistoryVersionInfo.isNotEmpty) {
      loadHistoryVersionInfo.sort((a1, a2) {
        if (a2.updateTime!.isEmpty || a1.updateTime!.isEmpty) {
          return 0;
        }
        return DateTime.parse(a2.updateTime ?? '')
            .compareTo(DateTime.parse(a1.updateTime ?? ''));
      });
    }

    state.loadHistoryVersionInfo.value = loadHistoryVersionInfo;
  }
}
