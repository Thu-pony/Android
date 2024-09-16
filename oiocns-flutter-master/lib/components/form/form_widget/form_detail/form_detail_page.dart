import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:orginone/components/common/templates/orginone_page_widget.dart';
import 'package:orginone/components/form/form_widget/main_form/view.dart';
import 'package:orginone/dart/base/schema.dart';
import 'package:orginone/utils/log/log_util.dart';

// ignore: must_be_immutable
class FormDetailPage extends OrginoneStatefulWidget<XForm> {
  FormDetailPage({super.key, super.data});

  @override
  State<FormDetailPage> createState() => _FormDetailPageState();
}

class _FormDetailPageState
    extends OrginoneStatefulState<FormDetailPage, XForm> {
  // 主视图
  Widget _buildView(XForm xForm, int infoIndex) {
    if (xForm == null) {
      return Container();
    }
    return SingleChildScrollView(
        child: MainFormPage(
      [xForm],
      infoIndex: infoIndex,
    ));
  }

  @override
  Widget buildWidget(BuildContext context, XForm data) {
    LogUtil.d(Get.arguments);
    return _buildView(data, int.parse(Get.parameters['infoIndex'] ?? '0'));
  }
}
