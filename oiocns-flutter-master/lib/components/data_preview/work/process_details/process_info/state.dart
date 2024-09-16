import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/data_preview/work/process_details/logic.dart';
import 'package:orginone/dart/base/model.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/components/common/getx/base_get_state.dart';
import 'package:orginone/dart/core/work/task.dart';

class ProcessInfoState extends BaseGetState {
  ProcessDetailsController processDetailsController =
      Get.find<ProcessDetailsController>();

  IWorkTask? get todo => processDetailsController.state.todo;

  WorkNodeModel? get node => processDetailsController.state.node;

  List<XForm> get mainForm => processDetailsController.state.mainForm;

  List<XForm> get subForm => processDetailsController.state.subForm;

  TabController get subTabController =>
      processDetailsController.state.subTabController;

  TabController get mainTabController =>
      processDetailsController.state.mainTabController;

  var mainIndex = 0.obs;

  var subIndex = 0.obs;
}
