import 'package:flutter/widgets.dart';
import 'package:get/get.dart';
import 'package:logging/logging.dart';

import 'base_get_state.dart';
import 'event_bus_helper.dart';

@Deprecated("待替代优化")
abstract class BaseController<S extends BaseGetState> extends GetxController {
  late BuildContext context;
  late S state;

  late Logger logger;

  String? tag;

  BaseController();

  @override
  void onInit() {
    super.onInit();
    logger = Logger(toString());
    EventBusHelper.register(this, onReceivedEvent);
  }

  @override
  void onReady() async {
    // TODO: implement onReady
    super.onReady();

    await loadData();
  }

  @override
  void onClose() {
    super.onClose();
    EventBusHelper.unregister(this);
  }

  Future<void> loadData() async {}

  void onReceivedEvent(event) {}
}
