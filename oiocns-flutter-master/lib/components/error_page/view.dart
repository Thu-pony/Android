import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/button/button.dart';
import 'package:orginone/components/common/extension/index.dart';
import 'package:orginone/components/common/text/text.dart';
import 'package:orginone/dart/base/storages/storage.dart';
import 'package:orginone/main_base.dart';
import 'package:orginone/utils/system/system_utils.dart';

import 'index.dart';

class ErrorPage extends GetView<ErrorPageController> {
  const ErrorPage({Key? key}) : super(key: key);

  // 主视图
  Widget _buildView() {
    List errorArray = [];
    var json = Storage.getString('work_page_error');
    if (json != '') {
      errorArray = jsonDecode(json);
    }
    errorArray = errorArray.reversed.toList();
    var t =
        DateUtil.formatDate(DateTime.now(), format: "yyyy-MM-dd HH:mm:ss.SSS");
    errorArray.insert(0, {'t': t, 'errorText': kernel.statisticsInfo});
    return ListView.builder(
      scrollDirection: Axis.vertical,
      itemBuilder: (context, index) {
        var data = errorArray[index];

        return ListTile(
            title: TextWidget.title3(data['t']),
            subtitle: TextWidget.body1(
              data['errorText'],
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ) //Text(data['errorText']),
            ).height(60).onTap(() {
          Get.to(ErrorSubPage(data['t'], data['errorText']));
        });
      },
      itemCount: errorArray.length,
    );
  }

  @override
  Widget build(BuildContext context) {
    return GetBuilder<ErrorPageController>(
      init: ErrorPageController(),
      id: "error_page",
      builder: (_) {
        return Scaffold(
          appBar: AppBar(title: const Text("错误日志"), actions: [
            ButtonWidget.text(
              '清除日志',
              onTap: () {
                Storage.remove('work_page_error');
                controller.update(['error_page']);
              },
            )
          ]),
          body: SafeArea(
            child: _buildView(),
          ),
        );
      },
    );
  }
}

class ErrorSubPage extends GetView<ErrorPageController> {
  const ErrorSubPage(this.title, this.errInfo, {Key? key}) : super(key: key);
  final String title;
  final String errInfo;

  // 主视图
  Widget _buildView() {
    return <Widget>[Text(errInfo)].toColumn();
  }

  @override
  Widget build(BuildContext context) {
    return GetBuilder<ErrorPageController>(
      init: ErrorPageController(),
      id: "error_sub_page",
      builder: (_) {
        return Scaffold(
          appBar: AppBar(
            title: Text(title ?? ''),
            actions: [
              ButtonWidget.text(
                '复制',
                onTap: () => SystemUtils.copyToClipboard(errInfo),
              )
            ],
          ),
          body: Scrollbar(
              child: SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: _buildView(),
          )),
        );
      },
    );
  }
}
