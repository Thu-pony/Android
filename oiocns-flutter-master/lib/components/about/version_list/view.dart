import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/extension/index.dart';
import 'package:orginone/components/common/getx/base_get_view.dart';
import 'package:orginone/components/common/templates/gy_scaffold.dart';

import 'item.dart';
import 'logic.dart';
import 'state.dart';

class VersionListPage
    extends BaseGetView<VersionListController, VersionListState> {
  const VersionListPage({super.key});

  @override
  Widget buildView() {
    return GyScaffold(
        titleName: '版本介绍',
        backgroundColor: Colors.white,
        body: Obx(() {
          return ListView.builder(
            itemCount: state.loadHistoryVersionInfo.length,
            itemBuilder: (context, int index) {
              var item = state.loadHistoryVersionInfo[index];
              var versionItem = VersionItem(
                title: item.content ?? '',
                version: item.version ?? '',
                date: item.updateTime?.dateTimeFormat ?? '',
                content: item.content ?? '',
              );
              return GestureDetector(
                onTap: () {
                  // showDetail(item);
                  controller.showDiaLog(context, versionItem);
                },
                child: versionItem,
              );
            },
          );
        }));
  }
}
