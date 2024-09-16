import 'package:get/get.dart';
import 'package:orginone/components/data_preview/work/process_details/logic.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/components/common/getx/base_get_state.dart';

class UseTracesState extends BaseGetState {
  ProcessDetailsController processDetailsController =
      Get.find<ProcessDetailsController>();

  XWorkInstance? get flowInstance =>
      processDetailsController.state.todo?.instance;
}
